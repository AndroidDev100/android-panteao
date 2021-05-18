package panteao.make.ready.tvBaseModels.basemodels

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.app.HeadersSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.make.enums.Layouts
import panteao.make.ready.PanteaoApplication
import panteao.make.ready.R
import panteao.make.ready.activities.homeactivity.ui.TVHomeActivity
import panteao.make.ready.baseModels.HomeBaseViewModel
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.*
import panteao.make.ready.cardlayout.cardpresenter.AssetCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.HeroCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.PotraitCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.SquareCardPresenter
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.utils.CustomListRowPresenter
import panteao.make.ready.utils.MediaTypeConstants
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.config.bean.ConfigBean
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys


open class TabBaseTVFragment<T : HomeBaseViewModel> : TVBaseFragment(), OnItemViewClickedListener,
        OnItemViewSelectedListener, BrowseSupportFragment.MainFragmentAdapterProvider {

    private val handler = Handler()
    private var previousView: View? = null
    open val TAG = "TabBaseFragment"

    private val mMainFragmentAdapter = MainFragmentAdapter(this)

    override fun getMainFragmentAdapter(): MainFragmentAdapter<*> {
        return mMainFragmentAdapter
    }

    private var lastClickTime: Long = 0
    lateinit var viewModel: T
    private lateinit var dataUpdateCallBack: DataUpdateCallBack

    private var rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private var mGridPresenter: Presenter? = null
    private var gridRowAdapter: ArrayObjectAdapter? = null
    lateinit var mActivity: TVHomeActivity
    private var railCommonDataList: ArrayList<RailCommonData> = ArrayList<RailCommonData>()

    private lateinit var dataLoadingListener: DataLoadingListener
    private lateinit var changeUi: ChangableUi
    private var counter = 0
    private lateinit var customListRowPresenter: ListRowPresenter
    private var isCrousal: Boolean = false
    private var counterValueApiFail = 0
    private var mIsLoading: Boolean = true
    private lateinit var railInjectionHelper :RailInjectionHelper
    private var mOnFragmentListener: OnTabBaseFragmentListener? = null


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity as TVHomeActivity
        dataLoadingListener = activity as DataLoadingListener
        railInjectionHelper = ViewModelProviders.of(this)[RailInjectionHelper::class.java]
        changeUi = activity
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectionObserver()

    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(mActivity)) {
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    protected fun setViewModel(viewModelClass: Class<*>) {
        viewModel =
                ViewModelProviders.of(this).get<ViewModel>(viewModelClass as Class<ViewModel>) as T
    }


    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            setupEventListeners()
            mActivity = activity as TVHomeActivity
            dataLoadingListener = mActivity as DataLoadingListener
            changeUi = mActivity
            customListRowPresenter = object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE) {
                override fun isUsingDefaultListSelectEffect() = false
            }.apply {
                shadowEnabled = false
            }
            customListRowPresenter = CustomListRowPresenter()
//            customListRowPresenter.selectEffectEnabled = false

            rowsAdapter = ArrayObjectAdapter(customListRowPresenter)
            adapter = rowsAdapter
            val bundle = arguments?.getString(AppConstants.BUNDLE_TAB_ID)
            callRailApi(mActivity, bundle)
        } else {
            addFragment(activity, NoInternetFragment(), false)
        }
    }

    private fun callRailApi(fragmentActivity: FragmentActivity, tabId: String?) {
        mOnFragmentListener?.showProgressBarView(true)
        tabId?.let {
            railInjectionHelper.getScreenWidgets(fragmentActivity, it, object :
                    CommonApiCallBack {
                override fun onSuccess(item: Any?) {
                    if (item is RailCommonData) {
                        if (item.screenWidget?.layout!!.equals(Layouts.HRO.name, true)) {
                            val enveuVideoItemBeanList =
                                    ArrayList<EnveuVideoItemBean>()
                            val enveuVideoItemBean =
                                    EnveuVideoItemBean()
                            if (item.screenWidget?.landingPageAssetId != null) {
                                enveuVideoItemBean.id =
                                        item.screenWidget?.landingPageAssetId!!.toInt()
                            }
//                            enveuVideoItemBean.thumbnailURL =
//                                   ImageLayer.getInstance()
//                                            .getThumbNailImageUrl(item)
                            enveuVideoItemBean.assetType = item.assetType
                            enveuVideoItemBeanList.add(enveuVideoItemBean)
                            item.setEnveuVideoItemBeans(enveuVideoItemBeanList)
                        }
                        setRows(
                                item.enveuVideoItemBeans!!,
                                if (item.screenWidget?.name != null) item.screenWidget?.name.toString() else " ",
                                0,
                                item.railType,
                                item
                        )
                        mOnFragmentListener?.showProgressBarView(false)
                    }
                }

                override fun onFailure(throwable: Throwable?) {
                    mOnFragmentListener?.showNoDataFoundView(
                            false,
                            getString(R.string.no_data_found)
                    )
                    mOnFragmentListener?.showProgressBarView(false)
                }

                override fun onFinish() {
                }
            })
        }
    }

    private fun setHero(item: RailCommonData) {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DataUpdateCallBack) {
            dataUpdateCallBack = context
        }
        mOnFragmentListener = context as OnTabBaseFragmentListener
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.MyMaterialTheme)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        return super.onCreateView(localInflater, container, savedInstanceState)
    }

    private fun setRows(
            result: List<EnveuVideoItemBean>,
            channelName: String,
            channelId: Long,
            contentType: Int,
            item: RailCommonData
    ) {
        dataLoadingListener.onLoadingOfFirstRow()

        when (contentType) {
            AppConstants.CAROUSEL_LDS_LANDSCAPE,
            AppConstants.HORIZONTAL_LDS_LANDSCAPE -> {
                mGridPresenter = AssetCardPresenter(contentType)
            }
            AppConstants.CAROUSEL_PR_POSTER,
            AppConstants.CAROUSEL_PR_POTRAIT,
            AppConstants.HORIZONTAL_PR_POSTER,
            AppConstants.HORIZONTAL_PR_POTRAIT -> {
                mGridPresenter = PotraitCardPresenter(contentType, activity)

            }
            AppConstants.CAROUSEL_SQR_SQUARE,
            AppConstants.HORIZONTAL_SQR_SQUARE,
            AppConstants.HORIZONTAL_CIR_CIRCLE -> {
                mGridPresenter = SquareCardPresenter(contentType)
            }
            AppConstants.HERO_LDS_LANDSCAPE -> {
                mGridPresenter = HeroCardPresenter(contentType)
            }
        }
        gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        gridRowAdapter?.addAll(0, result)
        var gridHeader = HeaderItem(rowsAdapter.size().toLong(), "")
        val dmsResponse =
                KsPreferenceKeys.getInstance().getString("DMS_Response", "")
        if (!dmsResponse!!.isEmpty()) {
            val configBean = Gson()
                    .fromJson(dmsResponse, ConfigBean::class.java)
            gridHeader = HeaderItem(rowsAdapter.size().toLong(), channelName)
            gridHeader.contentDescription = channelId.toString()
            if (contentType == AppConstants.HERO_LDS_LANDSCAPE)
                gridHeader.contentDescription = "HERO"
            val listRow = ListRow(gridHeader, gridRowAdapter)
            rowsAdapter.add(listRow)
        }

    }

    private fun setupEventListeners() {
        onItemViewClickedListener = this
        onItemViewSelectedListener = this
    }

    override fun onCreateHeadersSupportFragment(): HeadersSupportFragment {
        super.setHeadersState(HEADERS_DISABLED)
        return super.onCreateHeadersSupportFragment()
    }


    override fun onItemSelected(
            p0: Presenter.ViewHolder?,
            item: Any?,
            p2: RowPresenter.ViewHolder?,
            row: Row?
    ) {
        handler.removeCallbacksAndMessages(null)
        val listRowSelected = row?.id?.toInt()?.let { rowsAdapter.get(it) } as ListRow
        val cardRowAdapterSelected = listRowSelected.adapter as ArrayObjectAdapter
        dataLoadingListener.onCardSelected(cardRowAdapterSelected.indexOf(item))
        val isPlayerCard = cardRowAdapterSelected.getPresenter(item) is AssetCardPresenter
        if (item is EnveuVideoItemBean) {
            dataLoadingListener.onDataLoading(item, true)
            val listRowSelected = row.id.toInt().let { rowsAdapter.get(it) } as ListRow
            val cardRowAdapterSelected = listRowSelected.adapter as ArrayObjectAdapter
            if ((cardRowAdapterSelected.indexOf(item) == 1)) {
                changeUi.uichanged(1)
            } else {
                changeUi.uichanged(0)
            }
            if (row.headerItem?.contentDescription == "HERO") {
                changeUi.uichanged(3)
            }
            if ((cardRowAdapterSelected.indexOf(item) == cardRowAdapterSelected.size() - 1) && (cardRowAdapterSelected.size() % AppConstants.PAGE_SIZE == 0)) {
//                railInjectionHelper.getPlaylistById(activity,
//                        row.headerItem.name,
//                        if (((cardRowAdapterSelected.size() / AppLevelConstants.PAGE_SIZE) == 1)) 1 else (cardRowAdapterSelected.size() / AppLevelConstants.PAGE_SIZE) + 1,
//                        AppLevelConstants.PAGE_SIZE,
//                        object : panteao.make.ready.ui.callbacks.CommonApiCallBack {
//                            override fun onSuccess(item: Any?) {
//                                if (item != null && item is RailCommonData) {
//                                    cardRowAdapterSelected.addAll(
//                                            cardRowAdapterSelected.size(),
//                                            item.getEnveuVideoItemBeans()
//                                    )
//                                }
//                            }
//
//                            override fun onFailure(throwable: Throwable?) {
//
//                            }
//
//                            override fun onFinish() {
//
//                            }
//                        })
//                APIServiceLayer.getInstance().getPlayListById(screenWidget.contentID, 0, contentSize).observe((activity as LifecycleOwner?)!!, Observer { enveuCommonResponse: EnveuCommonResponse? ->
//                    if (enveuCommonResponse != null && enveuCommonResponse.data != null) {
//                        val railCommonData = RailCommonData(enveuCommonResponse.data, screenWidget, false)
//                        commonApiCallBack.onSuccess(railCommonData)
//                        i++
//                        getScreenListing(activity, commonApiCallBack)
//                    } else {
//                        i++
//                        getScreenListing(activity, commonApiCallBack)
//                    }
//                })
            }
        }
    }

    override fun onItemClicked(
            p0: Presenter.ViewHolder?,
            contentItem: Any?,
            p2: RowPresenter.ViewHolder?,
            p3: Row?
    ) {
        if (contentItem is EnveuVideoItemBean) {
            context?.let {
                if (contentItem.assetType.equals(
                                panteao.make.ready.utils.MediaTypeConstants.getInstance().series,
                                true
                        )
                ) {
                    AppCommonMethod.launchDetailScreen(
                            it,
                            0L,
                            AppConstants.Series,
                            contentItem.id,
                            "",
                            false
                    )
                } else if (contentItem.assetType.equals(
                                MediaTypeConstants.getInstance().live,
                                true
                        )
                ) {
//                    loadLivePlayer(contentItem)
//                    railInjectionHelper.getAssetDetails(contentItem.id.toString())
//                            .observe(this, Observer {
//                                if (it != null && it.getEnveuVideoItemBeans()!!.size > 0) {
//                                    if (it.getEnveuVideoItemBeans()!!
//                                                    .get(0).responseCode == AppConstants.RESPONSE_CODE_SUCCESS
//                                    ) {
//                                        var videoDetail = it.getEnveuVideoItemBeans()!!.get(0)
//                                        AppCommonMethod.loadPlayer(
//                                                videoDetail,
//                                                mActivity,
//                                                ArrayList(),
//                                                videoDetail.seriesId!!.toInt()
//                                        )
//                                    }
//                                }
//                            })
                } else if (contentItem.assetType.equals(
                                panteao.make.ready.utils.MediaTypeConstants.getInstance().episode,
                                true
                        )
                ) {
                    railInjectionHelper.getAssetDetailsV2(contentItem.id.toString())
                            .observe(this, Observer {
                                if (it != null && it.baseCategoriesList!!.size > 0) {
                                    if (it.baseCategoriesList!!
                                                    .get(0).responseCode == AppConstants.RESPONSE_CODE_SUCCESS
                                    ) {
                                        var videoDetail = it.baseCategoriesList!!.get(0)
//                                        AppCommonMethod.loadPlayer(
//                                                videoDetail,
//                                                mActivity,
//                                                ArrayList(),
//                                                videoDetail.seriesId!!.toInt()
//                                        )
                                    }
                                }
                            })
                } else {
                    if (contentItem.assetType != null) {
                        if (contentItem.season == null || contentItem.series == null) {
                            contentItem.assetType?.let { assetType ->
                                AppCommonMethod.launchDetailScreen(
                                        it,
                                        0L,
                                        assetType,
                                        contentItem.id,
                                        " ",
                                        contentItem.isPremium
                                )
                            }
                        } else {
                            contentItem.assetType?.let { assetType ->
                                contentItem.series?.let { series ->
                                    contentItem.season?.let { season ->
                                        AppCommonMethod.launchDetailScreen(
                                                it,
                                                0L,
                                                assetType,
                                                contentItem.id,
                                                series,
                                                contentItem.isPremium
                                        )
                                    }
                                }
                            }

                        }
                    } else {
                        AppCommonMethod.launchDetailScreen(
                                it,
                                0L,
                                AppConstants.Video,
                                contentItem.id,
                                "0",
                                contentItem.isPremium
                        )
                    }
                }
            }
        }
    }

//    private fun loadLivePlayer(contentItem: EnveuVideoItemBean) {
//        if (NetworkConnectivity.isOnline(mActivity)) {
//            railInjectionHelper.getAssetDetailsV2(contentItem.id.toString()).observe(this, Observer {
//                if (it != null && it.baseCategoriesList!!.size > 0) {
//                    if (it.baseCategoriesList
//                                    ?.get(0)?.responseCode == AppConstants.RESPONSE_CODE_SUCCESS
//                    ) {
//                        val liveVideo = it.baseCategoriesList?.get(0)
//                        if (liveVideo?.isPremium!! && !UserPreference.instance.entitlementState) {
//                            if (UserPreference.instance.isLogin) {
//                                hitApiEntitlement(
//                                        contentItem,
//                                        liveVideo.sku
//                                )
//                            } else {
//                                val loginActivity =
//                                        Intent(activity?.applicationContext, LoginActivity::class.java)
//                                startActivity(loginActivity)
//                            }
//                        } else {
//                            if (liveVideo.brightcoveVideoId.isEmpty()) {
//                                if (UserPreference.instance.isLogin) {
//                                    panteao.make.ready.purchase.planslayer.GetPlansLayer.getInstance()
//                                            .getEntitlementStatus(
//                                                    UserPreference.instance.userAuthToken
//                                            ) { entitlementStatus, apiStatus ->
////                                            if (entitlementStatus && apiStatus) {
//                                                val livePlayerIntent =
//                                                        Intent(activity, LivePlayerActivity::class.java)
//                                                val args = Bundle()
//                                                args.putString(
//                                                        AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE,
//                                                        liveVideo?.brightcoveVideoId
//                                                )
//                                                args.putString(
//                                                        AppConstants.BUNDLE_ASSET_TYPE,
//                                                        panteao.make.ready.utils.MediaTypeConstants.getInstance().live
//                                                )
//
//                                                val isLiveDrm: String =
//                                                        liveVideo.islivedrm
//                                                if (!isLiveDrm.equals(
//                                                                "",
//                                                                ignoreCase = true
//                                                        )
//                                                ) {
//                                                    if (isLiveDrm.equals(
//                                                                    "true",
//                                                                    ignoreCase = true
//                                                            )
//                                                    ) {
//                                                        if (liveVideo.widevineLicence != null && !liveVideo?.widevineLicence.equals(
//                                                                        "", true
//                                                                ) && liveVideo.getWidevineURL != null && !liveVideo?.getGetWidevineURL()
//                                                                        .equals("", ignoreCase = true)
//                                                        ) {
//                                                            args.putString(
//                                                                    "isLivedrm",
//                                                                    liveVideo?.getIslivedrm()
//                                                            )
//                                                            args.putString(
//                                                                    "widevine_licence",
//                                                                    liveVideo?.getWidevineLicence()
//                                                            )
//                                                            args.putString(
//                                                                    "widevine_url",
//                                                                    liveVideo?.getGetWidevineURL()
//                                                            )
//                                                            if (liveVideo?.getThumbnailImage() != null) {
//                                                                // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getThumbnailImage());
//                                                            } else if (liveVideo?.getPosterURL() != null) {
//                                                                // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getPosterURL());
//                                                            }
//                                                            if (liveVideo?.isPremium() && liveVideo?.getThumbnailImage() != null) {
//                                                                args.putString(
//                                                                        AppConstants.BUNDLE_BANNER_IMAGE,
//                                                                        liveVideo?.getThumbnailImage()
//                                                                )
//                                                            }
//
//                                                        } else {
//                                                            Toast.makeText(
//                                                                    activity,
//                                                                    "Some thing went wrong",
//                                                                    Toast.LENGTH_SHORT
//                                                            ).show()
//                                                        }
//                                                    } else {
//                                                        if (liveVideo.getWidevineURL != null && !liveVideo.getWidevineURL
//                                                                        .equals("", ignoreCase = true)
//                                                        ) {
//                                                            // args.putString("widevine_licence",videoDetails.getWidevineLicence());
//                                                            args.putString(
//                                                                    "isLivedrm",
//                                                                    liveVideo.islivedrm
//                                                            )
//                                                            args.putString(
//                                                                    "widevine_url",
//                                                                    liveVideo.getWidevineURL
//                                                            )
//                                                            if (liveVideo.isPremium && liveVideo.thumbnailImage != null) {
//                                                                args.putString(
//                                                                        AppConstants.BUNDLE_BANNER_IMAGE,
//                                                                        liveVideo.thumbnailImage
//                                                                )
//                                                            }
//                                                            livePlayerIntent.putExtra(
//                                                                    AppConstants.BUNDLE_ASSET_BUNDLE,
//                                                                    args
//                                                            )
//                                                        } else {
//                                                            Toast.makeText(
//                                                                    activity,
//                                                                    "Some thing went wrong",
//                                                                    Toast.LENGTH_SHORT
//                                                            ).show()
//                                                        }
//                                                    }
//                                                } else {
//                                                    if (liveVideo.getWidevineURL != null && !liveVideo.getWidevineURL
//                                                                    .equals("", ignoreCase = true)
//                                                    ) {
//                                                        // args.putString("widevine_licence",videoDetails.getWidevineLicence());
//                                                        args.putString(
//                                                                "isLivedrm",
//                                                                liveVideo.islivedrm
//                                                        )
//                                                        args.putString(
//                                                                "widevine_url",
//                                                                liveVideo.getWidevineURL
//                                                        )
//                                                        if (liveVideo.thumbnailImage != null) {
//                                                            // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getThumbnailImage());
//                                                        } else if (liveVideo.posterURL != null) {
//                                                            // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getPosterURL());
//                                                        }
//                                                        if (liveVideo.isPremium && liveVideo.thumbnailImage != null) {
//                                                            args.putString(
//                                                                    AppConstants.BUNDLE_BANNER_IMAGE,
//                                                                    liveVideo.thumbnailImage
//                                                            )
//                                                        }
//                                                        livePlayerIntent.putExtra(
//                                                                AppConstants.BUNDLE_ASSET_BUNDLE,
//                                                                args
//                                                        )
//                                                    } else {
//                                                        Toast.makeText(
//                                                                activity,
//                                                                "Some thing went wrong",
//                                                                Toast.LENGTH_SHORT
//                                                        ).show()
//                                                    }
//                                                }
//                                                startActivity(livePlayerIntent)
////                                            } else {
////                                                Toast.makeText(
////                                                    activity!!,
////                                                    "Buy Now",
////                                                    Toast.LENGTH_LONG
////                                                ).show()
////                                            }
//                                            }
//                                } else {
//                                    val loginActivity = Intent(
//                                            activity?.applicationContext,
//                                            LoginActivity::class.java
//                                    )
//                                    startActivity(loginActivity)
//                                }
//                            } else {
//                                if (liveVideo.getWidevineURL != null && !liveVideo.getWidevineURL
//                                                .equals("", ignoreCase = true)
//                                ) {
//                                    val livePlayerIntent =
//                                            Intent(activity, LivePlayerActivity::class.java)
//                                    val args = Bundle()
//                                    // args.putString("widevine_licence",videoDetails.getWidevineLicence());
//                                    args.putString(
//                                            "isLivedrm",
//                                            liveVideo.islivedrm
//                                    )
//                                    args.putString(
//                                            "widevine_url",
//                                            liveVideo.getWidevineURL
//                                    )
//                                    if (liveVideo.thumbnailImage != null) {
//                                        // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getThumbnailImage());
//                                    } else if (liveVideo.posterURL != null) {
//                                        // ImageHelper.getInstance(LiveActivity.this).loadListImage(getBinding().channelLogo, videoDetails.getPosterURL());
//                                    }
//                                    if (liveVideo.isPremium && liveVideo.thumbnailImage != null) {
//                                        args.putString(
//                                                AppConstants.BUNDLE_BANNER_IMAGE,
//                                                liveVideo.thumbnailImage
//                                        )
//                                    }
//                                    livePlayerIntent.putExtra(
//                                            AppConstants.BUNDLE_ASSET_BUNDLE,
//                                            args
//                                    )
//                                    startActivity(livePlayerIntent)
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    Toast.makeText(activity, "Some thing went wrong", Toast.LENGTH_SHORT).show()
//                }
//            })
//        } else {
//            connectionValidation(false)
//        }
//    }

//    private fun hitApiEntitlement(
//            contentItem: EnveuVideoItemBean,
//            sku: String?
//    ) {
//        panteao.make.ready.beanmodel.entitle.EntitlementLayer.getInstance()
//                .hitApiEntitlement(UserPreference.instance.userAuthToken, sku)
//                .observe(this,
//                        Observer {
//                            if (it.status && it.data != null) {
//                                if (it.data.entitled) {
//                                    UserPreference.instance.entitlementState = true
//                                    loadLivePlayer(contentItem)
//                                } else {
//                                    if (it.data != null) {
//                                        UserPreference.instance.entitlementState = false
//                                        startActivity(Intent(activity!!, MembershipPlans::class.java))
//                                    }
//                                }
//                            } else if (it?.responseCode != null && it.responseCode > 0 && it.responseCode == 4302) {
//                                val loginActivity = Intent(
//                                        activity?.applicationContext,
//                                        LoginActivity::class.java
//                                )
//                                startActivity(loginActivity)
//                            }
//                        })
//    }

    private fun logoutUser() {
        if (KsPreferenceKeys.getInstance().appPrefLoginStatus) {
            if (NetworkConnectivity.isOnline(activity)) {
                KsPreferenceKeys.getInstance().clear()
                hitApiLogout(requireActivity(), KsPreferenceKeys.getInstance().appPrefAccessToken);
            }
        }
    }

    private fun hitApiLogout(activity: FragmentActivity, userAuthToken: String?) {

//        val isFacebook: String = UserPreference.instance.loginType!!
//
//        if (isFacebook.equals(AppConstants.UserLoginType.FbLogin.toString(), ignoreCase = true)) {
//            panteao.make.ready.manager.FbLoginManager.getInstance().clear()
//        }
//        ApiCallManager.instance.logoutCall(
//                UserPreference.instance.userAuthToken!!,
//                object : LogoutCallBack {
//                    override fun failure(status: Boolean, errorCode: Int, message: String) {
//                        super.failure(status, errorCode, message)
//                        val jsonObject = JsonObject()
//                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, 500)
//                    }
//
//                    override fun success(status: Boolean, response: Response<JsonObject>) {
//                        super.success(status, response)
//                        if (status) {
//                            try {
//                                if (response.code() == 404) {
//                                    val jsonObject =
//                                            JsonObject()
//                                    jsonObject.addProperty(
//                                            AppConstants.API_RESPONSE_CODE,
//                                            response.code()
//                                    )
//                                }
//                                if (response.code() == 403) {
//                                    val jsonObject =
//                                            JsonObject()
//                                    jsonObject.addProperty(
//                                            AppConstants.API_RESPONSE_CODE,
//                                            response.code()
//                                    )
//                                } else if (response.code() == 200) {
//                                    Objects.requireNonNull(response.body())
//                                            ?.addProperty(AppConstants.API_RESPONSE_CODE, response.code())
//                                } else if (response.code() == 401) {
//                                    val jsonObject =
//                                            JsonObject()
//                                    jsonObject.addProperty(
//                                            AppConstants.API_RESPONSE_CODE,
//                                            response.code()
//                                    )
//                                } else if (response.code() == 500) {
//                                    val jsonObject =
//                                            JsonObject()
//                                    jsonObject.addProperty(
//                                            AppConstants.API_RESPONSE_CODE,
//                                            response.code()
//                                    )
//                                }
//                            } catch (e: Exception) {
//                                val jsonObject =
//                                        JsonObject()
//                                jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code())
//                            }
//                        }
//                    }
//                })
//        startActivity(Intent(activity, LoginActivity::class.java))
    }


    fun refreshData(fragmentActivity: FragmentActivity, tabId: String?) {
        isCrousal = !isCrousal
        counter = 0
        rowsAdapter = ArrayObjectAdapter(customListRowPresenter)
        adapter = rowsAdapter
        callRailApi(fragmentActivity, tabId)
    }

    fun reloadData(id: Int) {
        if (NetworkConnectivity.isOnline(mActivity)) {
            isCrousal = !isCrousal
            counter = 0
            rowsAdapter = ArrayObjectAdapter(customListRowPresenter)
            adapter = rowsAdapter
        } else {
            addFragment(activity, NoInternetFragment(), false)
        }
    }
}

