package panteao.make.ready.activities.search.ui.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.HeadersSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.DataUpdateCallBack
import panteao.make.ready.callbacks.commonCallbacks.OnKeywordSearchFragmentListener
import panteao.make.ready.cardlayout.cardpresenter.PopularSearchCardPresenter
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.tvBaseModels.basemodels.TVBaseFragment
import panteao.make.ready.utils.MediaTypeConstants
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper
import kotlin.collections.ArrayList


class KeyWordSearchFragment constructor() : TVBaseFragment(), OnItemViewClickedListener,
    DataUpdateCallBack, NoInternetFragment.OnFragmentInteractionListener {

    private var count = 0
    private val totalCount = 0
    var channelId: String? = null
    private var mGridPresenter: Presenter? = null
    private var gridRowAdapter: ArrayObjectAdapter? = null
    private var searchString: String? = null
    private lateinit var mBackgroundManager: BackgroundManager
    private var rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
    private lateinit var customListRowPresenter: ListRowPresenter
    private var mOnFragmentListener: OnKeywordSearchFragmentListener? = null
    private lateinit var railInjectionHelper: RailInjectionHelper


    override fun onFragmentInteraction() {
//        connectionObserver()
    }

    override fun onDataClick(searchString: String) {
        activity?.applicationContext?.let {
            if (NetworkConnectivity.isOnline(activity)) {
                setupFragment(searchString)
            } else {
                noInternetFragment()
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnFragmentListener = context as OnKeywordSearchFragmentListener
        railInjectionHelper = RailInjectionHelper(activity?.application!!)
    }

    private fun connectionObserver() {
        activity?.applicationContext?.let {
            if (NetworkConnectivity.isOnline(activity)) {
                connectionValidation(true)
            } else {
                connectionValidation(false)
            }
        }

    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {

            if (arguments != null) {
                searchString = arguments?.getString(AppConstants.SEARCH_STRING_KEY)
            }
            setupEventListeners()
            customListRowPresenter = object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE) {
                override fun isUsingDefaultListSelectEffect() = false
            }.apply {
                shadowEnabled = false
            }
            mBackgroundManager = BackgroundManager.getInstance(activity)
            setupFragment(searchString)
        } else {
            noInternetFragment()
        }
    }

    fun noInternetFragment() {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.content, NoInternetFragment())
        fragmentTransaction?.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionObserver()
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = this
    }

    private var tagValue = -1

    private fun callViewModel(searchKeyword: String) {
        if (searchKeyword.length >= 3) {
            mOnFragmentListener?.showProgressBarView(true)
            mOnFragmentListener?.showNoDataFoundView(false, "")
            hitApiSearchKeyword(searchKeyword)
        }
    }

    private fun hitApiSearchKeyword(searchKeyword: String) {
        railInjectionHelper.getSearch(searchKeyword, 20, 0).observe(this, Observer { it ->
            var index = 1
            if (it != null && it.isNotEmpty()) {
                mOnFragmentListener?.showNoDataFoundView(false, "")
                for (i in it.indices) {
                    if (mGridPresenter == null) {
                        mGridPresenter = PopularSearchCardPresenter()
                    }
                    if (it[i].enveuVideoItemBeans!!.size > 0) {
                        gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
                        gridRowAdapter?.addAll(0, it.get(i).getEnveuVideoItemBeans())
                        var headerText = "";
                        headerText = it[i].enveuVideoItemBeans!![0].assetType
//                        when (it[i].enveuVideoItemBeans!![0].assetType) {
//                            MediaTypeConstants.getInstance().movie
//                            -> {
//                                headerText = getString(R.string.heading_movies);
//                            }
//                            MediaTypeConstants.getInstance().series
//                            -> {
//                                headerText = getString(R.string.heading_series);
//                            }
//                            MediaTypeConstants.getInstance().episode
//                            -> {
//                                headerText = getString(R.string.heading_episodes);
//                            }
//                            MediaTypeConstants.getInstance().live
//                            -> {
//                                headerText = getString(R.string.heading_live);
//                            }
//                            MediaTypeConstants.getInstance().instructor
//                            -> {
//                                headerText = MediaTypeConstants.getInstance().instructor;
//                            }
//                            MediaTypeConstants.getInstance().trailor
//                            -> {
//                                headerText = MediaTypeConstants.getInstance().trailor;
//                            }
//                            MediaTypeConstants.getInstance().chapter
//                            -> {
//                                headerText = MediaTypeConstants.getInstance().chapter;
//                            }
//                            MediaTypeConstants.getInstance().tutorial
//                            -> {
//                                headerText = MediaTypeConstants.getInstance().tutorial
//                            }
//                            MediaTypeConstants.getInstance().show
//                            -> {
//                                headerText = MediaTypeConstants.getInstance().show
//                            }
//                        }
                        val gridHeader = HeaderItem(
                            index.toLong(),
                            headerText
                        )
                        gridHeader.contentDescription = channelId.toString()
                        val listRow = ListRow(gridHeader, gridRowAdapter)
                        rowsAdapter.add(listRow)
                        adapter = rowsAdapter
                        index++
                    } else {
                        mOnFragmentListener?.showProgressBarView(false)
                    }
                }

                if (rowsAdapter.size() == 0) {
                    mOnFragmentListener?.showProgressBarView(false)
                } else {
                    mOnFragmentListener?.searchResultsFound(searchKeyword)
                }
            } else {
                activity?.applicationContext?.let {
                    if (!NetworkConnectivity.isOnline(it)) {
                        Toast.makeText(
                            activity,
                            resources.getString(R.string.no_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
            if (rowsAdapter.size() == 0) {
                mOnFragmentListener?.showNoDataFoundView(true, getString(R.string.no_data_found))
            }
            mOnFragmentListener?.showProgressBarView(false)
        })
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            mOnFragmentListener?.showProgressBarView(false)
        } else {
            count = 0
            rowsAdapter.clear()
        }
    }

    private fun setupFragment(searchString: String?) {
        count = 0
        rowsAdapter.clear()
        if (searchString != null && searchString.length >= 2) {
            searchedStringValue = searchString
            tagValue++
            callSearchByKeywordApi()
        }
    }


    override fun onItemClicked(
        p0: Presenter.ViewHolder?,
        asset: Any?,
        p2: RowPresenter.ViewHolder?,
        p3: Row?
    ) {
        if (asset is EnveuVideoItemBean) {
            context?.let { context ->
                AppCommonMethod.launchDetailScreen(
                    context,
                    0L,
                    asset.assetType,
                    asset.id,
                    "",
                    "",
                    "0",
                    false,
                    asset
                )
            }
        }
        //                else if (asset.assetType == MediaTypeConstants.getInstance().episode) {
//                    railInjectionHelper.getAssetDetails((asset as EnveuVideoItemBean).id.toString())
//                            .observe(this, Observer {
//                                if (it != null && it.getEnveuVideoItemBeans()!!.size > 0) {
//                                    if (it.getEnveuVideoItemBeans()!![0].responseCode == AppConstants.RESPONSE_CODE_SUCCESS
//                                    ) {
//                                        var videoDetail = it.getEnveuVideoItemBeans()!!.get(0)
//                                        LogUtils.e("Search", Gson().toJson(videoDetail))
//                                        AppCommonMethod.loadPlayer(
//                                                videoDetail,
//                                                activity!!,
//                                                ArrayList(),
//                                                videoDetail.seriesId!!.toInt()
//                                        )
//                                    }
//                                }
//                            })
//                } else if (asset.assetType == MediaTypeConstants.getInstance().live) {
//                    loadLivePlayer(asset)
//                } else {
//                    if (asset.assetType != null) {
//                        if (asset.season == null || asset.series == null) {
//                            asset.assetType?.let { assetType ->
//                                AppCommonMethod.launchDetailScreen(
//                                        context,
//                                        0L,
//                                        assetType,
//                                        asset.id,
//                                        " ",
//                                        " ",
//                                        "0",
//                                        asset.isPremium
//                                )
//                            }
//                        } else {
//                            asset.assetType?.let { assetType ->
//                                asset.series?.let { series ->
//                                    asset.season?.let { season ->
//                                        AppCommonMethod.launchDetailScreen(
//                                                context,
//                                                0L,
//                                                assetType,
//                                                asset.id,
//                                                series,
//                                                season,
//                                                "0",
//                                                asset.isPremium
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        AppCommonMethod.launchDetailScreen(
//                                context,
//                                0L,
//                                AppConstants.Video,
//                                asset.id,
//                                "",
//                                "",
//                                "0",
//                                asset.isPremium
//                        )
//                    }
//                }
//            }
//
//        }

    }

//    private fun hitApiEntitlement(contentItem: EnveuVideoItemBean, sku: String?) {
//        EntitlementLayer.getInstance().hitApiEntitlement(UserPreference.instance.userAuthToken, sku)
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

    private fun callSearchByKeywordApi() {
        mhandler.removeCallbacks(refresh)
        mhandler.postDelayed(refresh, 2000)
    }

    var searchedStringValue = ""

    val mhandler = Handler()

    var refresh = Runnable {
        callViewModel(searchedStringValue)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.context.setTheme(R.style.CustomBrowseTheme)
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.nextFocusUpId = requireActivity().findViewById<SearchBar>(R.id.search_view).id
        return view;
    }

    override fun onCreateHeadersSupportFragment(): HeadersSupportFragment {
        super.setHeadersState(HEADERS_DISABLED)
        return super.onCreateHeadersSupportFragment()
    }

    override fun onDetach() {
        super.onDetach()
        mOnFragmentListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mOnFragmentListener?.showNoDataFoundView(false, "")
        mOnFragmentListener?.showProgressBarView(false)
    }

    fun onUpKeyClicked() {
        if (selectedRowViewHolder != null && selectedRowViewHolder.row.id.toInt() == 1) {
            requireActivity().findViewById<FrameLayout>(R.id.recent_searches_layout)
                .requestFocus()
        }
    }
}