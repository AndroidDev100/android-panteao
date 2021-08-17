package panteao.make.ready.activities.internalpages.fragments


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.HeadersSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.make.baseCollection.baseCategoryModel.BaseCategory
import com.make.enums.ImageType
import com.make.enums.Layouts
import panteao.make.ready.R
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.DataUpdateCallBack
import panteao.make.ready.callbacks.commonCallbacks.OnKeywordSearchFragmentListener
import panteao.make.ready.cardlayout.cardpresenter.PopularSearchCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.PotraitCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.SquareCardPresenter
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.servicelayer.APIServiceLayer
import panteao.make.ready.tvBaseModels.basemodels.TVBaseFragment
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class InternalPlaylistListingTVFragment constructor() : TVBaseFragment(), OnItemViewClickedListener,
    DataUpdateCallBack, NoInternetFragment.OnFragmentInteractionListener {

    private lateinit var playLists: List<String>
    private var count = 0
    private var mGridPresenter: Presenter? = null
    private var gridRowAdapter: ArrayObjectAdapter? = null
    private lateinit var playListContent: String
    private lateinit var mBackgroundManager: BackgroundManager
    private lateinit var rowsAdapter: ArrayObjectAdapter
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
        railInjectionHelper = RailInjectionHelper(activity?.application!!)
        mOnFragmentListener = context as OnKeywordSearchFragmentListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
                playListContent =
                    arguments?.getString(AppConstants.PLAYLIST_CONTENT)!!
            }
            setupEventListeners()
            customListRowPresenter = object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE) {
                override fun isUsingDefaultListSelectEffect() = false
            }.apply {
                shadowEnabled = false
            }
            rowsAdapter = ArrayObjectAdapter(customListRowPresenter)
            mBackgroundManager = BackgroundManager.getInstance(activity)
            setupFragment(playListContent)
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
        adapter = rowsAdapter;
        playLists = playListContent.split(",");
        checkActivity()
    }

    private fun checkActivity() {
        activity?.let { getPlayListDetails(it) }
            ?: Handler(Looper.getMainLooper()).postDelayed({
                checkActivity()
            }, 3000)
    }

    private fun getPlayListDetails(activity: FragmentActivity) {
        var playListId = ""
        var imageType = ""
        var title = ""
        if (count < playLists.size) {
            playLists[count].split("|").forEachIndexed { i, s ->
                if (i == 0) {
                    playListId = s.replace("\"", "").trim()
                } else if (i == 2) {
                    imageType = s.replace("\"", "").trim()
                } else if (i == 1) {
                    title = s.replace("\"", "").trim()
                }
            }
            Logger.e("DETAILS", playListId + " " + imageType + " " + title)
            APIServiceLayer.getInstance().getPlayListById(playListId, 0, 20)
                .observe(
                    activity,
                    { enveuCommonResponse: EnveuCommonResponse? ->
                        if (enveuCommonResponse != null && enveuCommonResponse.data != null) {
                            mOnFragmentListener?.showNoDataFoundView(false, "")
                            if (imageType == "LANDSCAPE") {
                                mGridPresenter = PopularSearchCardPresenter("16x9")
                            } else if (imageType == "PORTRAIT_2_3" || imageType == "PORTRAIT") {
                                mGridPresenter = PotraitCardPresenter(100, activity, "9x16")

                            } else if (imageType == "SQUARE") {
                                mGridPresenter = SquareCardPresenter(0, "16x9")
                            }
                            val screenWidget = BaseCategory()
                            screenWidget.layout = Layouts.HOR.name
                            screenWidget.contentImageType = ImageType.LDS.name
                            val railCommonData =
                                RailCommonData(enveuCommonResponse.data, screenWidget, false)
                            gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
                            gridRowAdapter?.addAll(0, railCommonData.enveuVideoItemBeans)

                            val gridHeader = HeaderItem(
                                count.toLong(),
                                title
                            )
                            val listRow = ListRow(gridHeader, gridRowAdapter)
                            rowsAdapter.add(listRow)
                            count++
                            getPlayListDetails(activity)
                        }
                    })
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
                    asset.assetType,
                    asset.id,
                    "",
                    "",
                    false,
                    asset
                )
            }
        }
    }

    var searchedStringValue = ""

    val mhandler = Handler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.context.setTheme(R.style.InternalPlayListBrowseTheme)
        return super.onCreateView(inflater, container, savedInstanceState);
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