package panteao.make.ready.tvBaseModels.basemodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import panteao.make.ready.R
import panteao.make.ready.activities.KalturaPlayerActivity
import panteao.make.ready.activities.detailspage.activity.VideoDetailActivity
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

    private val handler = Handler(Looper.getMainLooper())
    open val TAG = "TabBaseFragment"

    private val mMainFragmentAdapter = MainFragmentAdapter(this)

    override fun getMainFragmentAdapter(): MainFragmentAdapter<*> {
        return mMainFragmentAdapter
    }

    lateinit var viewModel: T
    private lateinit var dataUpdateCallBack: DataUpdateCallBack

    private var rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private var mGridPresenter: Presenter? = null
    private var gridRowAdapter: ArrayObjectAdapter? = null
    lateinit var mActivity: TVHomeActivity
    private lateinit var dataLoadingListener: DataLoadingListener
    private lateinit var changeUi: ChangableUi
    private var counter = 0
    private lateinit var customListRowPresenter: ListRowPresenter
    private var isCrousal: Boolean = false
    private lateinit var railInjectionHelper: RailInjectionHelper
    private var mOnFragmentListener: OnTabBaseFragmentListener? = null


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity as TVHomeActivity
        dataLoadingListener = activity
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
                            enveuVideoItemBean.assetType = item.assetType
                            enveuVideoItemBean.thumbnailImage =
                                item.enveuVideoItemBeans[0].thumbnailImage
                            enveuVideoItemBeanList.add(enveuVideoItemBean)
                            item.enveuVideoItemBeans = enveuVideoItemBeanList
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
        val isPlayerCard = cardRowAdapterSelected.getPresenter(item) is AssetCardPresenter
        if (item is EnveuVideoItemBean) {
            railInjectionHelper.getAssetDetailsV2(item.id.toString())
                .observe(this, Observer {
                    if (it != null && it.baseCategory != null) {
                        val railCommonData = it.baseCategory as RailCommonData
                        dataLoadingListener.onCardSelected(
                            cardRowAdapterSelected.indexOf(item),
                            railCommonData.enveuVideoItemBeans[0]
                        )
                    }
                })
            dataLoadingListener.onDataLoading(item, true)
            val listRowSelected = row.id.toInt().let { rowsAdapter.get(it) } as ListRow
            val cardRowAdapterSelected = listRowSelected.adapter as ArrayObjectAdapter
            if ((cardRowAdapterSelected.indexOf(item) == 1)) {
                changeUi.uichanged(1)
            } else {
                changeUi.uichanged(0)
            }
            if (row.headerItem?.contentDescription == "HERO") {
                Logger.e("ASSET_DETAILS", Gson().toJson(item))
                changeUi.uichanged(3)
            }
            if ((cardRowAdapterSelected.indexOf(item) == cardRowAdapterSelected.size() - 1) && (cardRowAdapterSelected.size() % AppConstants.PAGE_SIZE == 0)) {
//                railInjectionHelper.getPlaylistById(activity,
//                        row.headerItem.name,
//                        if (((cardRowAdapterSelected.size() / AppConstants.PAGE_SIZE) == 1)) 1 else (cardRowAdapterSelected.size() / AppConstants.PAGE_SIZE) + 1,
//                        AppConstants.PAGE_SIZE,
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
        asset: Any?,
        p2: RowPresenter.ViewHolder?,
        p3: Row?
    ) {
        if (asset is EnveuVideoItemBean) {
            AppCommonMethod.launchDetailScreen(
                context,
                asset.assetType,
                asset.id,
                "",
                "",
                false,
                asset
            )
        }
    }

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

