package panteao.make.ready.activities.internalpages.fragments


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BackgroundManager
import androidx.leanback.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.make.baseCollection.baseCategoryModel.BaseCategory
import com.make.enums.ImageType
import com.make.enums.Layouts
import com.make.enums.RailCardType
import panteao.make.ready.R
import panteao.make.ready.activities.listing.callback.ItemClickListener
import panteao.make.ready.adapters.CommonListingAdapter
import panteao.make.ready.adapters.commonRails.*
import panteao.make.ready.baseModels.BaseBindingFragment
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.DataUpdateCallBack
import panteao.make.ready.callbacks.commonCallbacks.OnKeywordSearchFragmentListener
import panteao.make.ready.databinding.InternalPlaylistListingFragmentBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.servicelayer.APIServiceLayer
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.ShimmerDataModel
import panteao.make.ready.utils.helpers.GridSpacingItemDecoration
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper
import panteao.make.ready.utils.helpers.RecyclerAnimator
import java.util.*


class InternalPlayListGridFragment constructor() :
    BaseBindingFragment<InternalPlaylistListingFragmentBinding>(), OnItemViewClickedListener,
    DataUpdateCallBack, NoInternetFragment.OnFragmentInteractionListener, ItemClickListener {

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
    private var commonLandscapeAdapter: LandscapeListingAdapter? = null
    private var commonPotraitTwoAdapter: CommonPotraitTwoAdapter? = null
    private var commonPosterPotraitAdapter: CommonPosterPotraitAdapter? = null
    private var squareCommonAdapter: SquareListingAdapter? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectionObserver()
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
    }

    private fun setupEventListeners() {
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
        playLists = playListContent.split(",");
        callShimmer()
    }

    private fun callShimmer() {
        val shimmerAdapter = CommonListingAdapter(requireActivity())
        binding.listRecyclerview.hasFixedSize()
        binding.listRecyclerview.isNestedScrollingEnabled = false
        var num = 2
        val tabletSize: Boolean = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            num =
                if (resources
                        .configuration.orientation == 2
                ) 4 else 3
        }
        shimmerAdapter.setDataList(ShimmerDataModel(requireActivity()).getList(4))
        binding.listRecyclerview.addItemDecoration(GridSpacingItemDecoration(num, 6, true))
        var gridLayoutManager = GridLayoutManager(requireActivity(), num)
        binding.listRecyclerview.layoutManager = gridLayoutManager
        binding.listRecyclerview.adapter = shimmerAdapter
        binding.listRecyclerview.visibility = View.VISIBLE
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
            var num = 3
            APIServiceLayer.getInstance().getPlayListById(playListId, 0, 20)
                .observe(
                    activity,
                    { enveuCommonResponse: EnveuCommonResponse? ->
                        if (enveuCommonResponse != null && enveuCommonResponse.data != null) {
                            mOnFragmentListener?.showNoDataFoundView(false, "")
                            RecyclerAnimator(activity).animate(binding.listRecyclerview)
                            val screenWidget = BaseCategory()
                            screenWidget.layout = Layouts.HOR.name
                            screenWidget.contentImageType = ImageType.LDS.name
                            val baseCategory = BaseCategory()
                            baseCategory.railCardType = RailCardType.IMAGE_ONLY.name
                            val railCommonData = RailCommonData(
                                enveuCommonResponse.data,
                                screenWidget,
                                false
                            )
                            if (imageType == "LANDSCAPE") {
                                if (commonLandscapeAdapter == null) {
                                    commonLandscapeAdapter = LandscapeListingAdapter(
                                        activity,
                                        railCommonData.enveuVideoItemBeans,
                                        ArrayList(),
                                        "VIDEO",
                                        this,
                                        baseCategory
                                    )

                                    num = 2
                                    val tabletSize: Boolean =
                                        resources.getBoolean(R.bool.isTablet)
                                    if (tabletSize) {
                                        //landscape
                                        num = if (resources
                                                .configuration.orientation == 2
                                        ) 4 else 3
                                    }
                                    //itemDecoration(num,baseCategory.getContentImageType());

                                    //itemDecoration(num,baseCategory.getContentImageType());
                                    binding.listRecyclerview.adapter = commonLandscapeAdapter
                                }
                            } else if (imageType == "PORTRAIT") {
//                                    mGridPresenter = PotraitCardPresenter(100, activity, "9x16")
                                if (commonPosterPotraitAdapter == null) {
                                    RecyclerAnimator(activity).animate(binding.listRecyclerview)
                                    commonPosterPotraitAdapter = CommonPosterPotraitAdapter(
                                        activity,
                                        railCommonData.enveuVideoItemBeans,
                                        ArrayList(),
                                        "VIDEO",
                                        ArrayList(),
                                        this@InternalPlayListGridFragment,
                                        baseCategory
                                    )
                                    num = 3
                                    val tabletSize: Boolean =
                                        getResources()
                                            .getBoolean(R.bool.isTablet)
                                    if (tabletSize) {
                                        //landscape
                                        num = if (getResources()
                                                .getConfiguration().orientation == 2
                                        ) 5 else 4
                                    }
//                                    itemDecoration(num, baseCategory.contentImageType)
                                    //     getBinding().listRecyclerview.addItemDecoration(new SpacingItemDecoration(12, SpacingItemDecoration.HORIZONTAL));
                                    binding.listRecyclerview.adapter =
                                        commonPosterPotraitAdapter
                                } else commonPosterPotraitAdapter!!.notifydata(railCommonData.getEnveuVideoItemBeans())
                            } else if (imageType == "PORTRAIT_2_3") {

                                if (commonPotraitTwoAdapter == null) {
                                    RecyclerAnimator(activity).animate(binding.listRecyclerview)
                                    commonPotraitTwoAdapter = CommonPotraitTwoAdapter(
                                        activity,
                                        railCommonData.getEnveuVideoItemBeans(),
                                        "VIDEO",
                                        ArrayList(),
                                        0,
                                        this,
                                        baseCategory
                                    )
                                    num = 3
                                    val tabletSize: Boolean =
                                        getResources().getBoolean(R.bool.isTablet)
                                    if (tabletSize) {
                                        //landscape
                                        num = if (getResources()
                                                .getConfiguration().orientation == 2
                                        ) 5 else 4
                                    }
//                                    itemDecoration(num, baseCategory.contentImageType)
                                    binding.listRecyclerview.adapter = commonPotraitTwoAdapter
                                } else commonPotraitTwoAdapter!!.notifydata(railCommonData.getEnveuVideoItemBeans())

//                                mIsLoading =
//                                    playlistRailData.getMaxContent() != commonPotraitTwoAdapter!!.itemCount

                            } else if (imageType == "SQUARE") {
                                if (squareCommonAdapter == null) {
                                    RecyclerAnimator(activity).animate(binding.listRecyclerview)
                                    squareCommonAdapter = SquareListingAdapter(
                                        activity,
                                        railCommonData.getEnveuVideoItemBeans(),
                                        "VIDEO",
                                        this
                                    )
                                    num = 3
                                    val tabletSize: Boolean =
                                       getResources().getBoolean(R.bool.isTablet)
                                    if (tabletSize) {
                                        //landscape
                                        num = if (getResources()
                                                .getConfiguration().orientation == 2
                                        ) 5 else 4
                                    }
//                                    itemDecoration(num, baseCategory.contentImageType)
                                    binding.listRecyclerview.adapter = squareCommonAdapter
                                } else squareCommonAdapter!!.notifydata(railCommonData.getEnveuVideoItemBeans())
                          }
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

    }

    override fun inflateBindingLayout(inflater: LayoutInflater): InternalPlaylistListingFragmentBinding {
        return InternalPlaylistListingFragmentBinding.inflate(inflater)
    }

    override fun onRowItemClicked(itemValue: EnveuVideoItemBean?, position: Int) {

    }
}