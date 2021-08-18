package panteao.make.ready.activities.internalpages.fragments


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.make.baseCollection.baseCategoryModel.BaseCategory
import com.make.enums.ImageType
import com.make.enums.Layouts
import com.make.enums.RailCardType
import panteao.make.ready.R
import panteao.make.ready.activities.listing.callback.ItemClickListener
import panteao.make.ready.adapters.commonRails.CommonAdapterNew
import panteao.make.ready.adapters.shimmer.ShimmerAdapter
import panteao.make.ready.baseModels.BaseBindingFragment
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.CommonRailtItemClickListner
import panteao.make.ready.callbacks.commonCallbacks.MoreClickListner
import panteao.make.ready.databinding.InternalPlaylistListFragmentBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.servicelayer.APIServiceLayer
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_LDS_LANDSCAPE
import panteao.make.ready.utils.constants.AppConstants.HORIZONTAL_PR_POTRAIT
import panteao.make.ready.utils.cropImage.helpers.PrintLogging
import panteao.make.ready.utils.cropImage.helpers.ShimmerDataModel
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper
import panteao.make.ready.utils.helpers.RecyclerAnimator


class InternalPlaylistListingFragment :
    BaseBindingFragment<InternalPlaylistListFragmentBinding>(), OnItemViewClickedListener,
    NoInternetFragment.OnFragmentInteractionListener,
    CommonRailtItemClickListner, MoreClickListner {

    private val mScrollY = 0
    private lateinit var railCommonDataList: ArrayList<RailCommonData>
    private lateinit var playLists: ArrayList<String>
    private var count = 0
    private lateinit var playListContent: String
    private lateinit var railInjectionHelper: RailInjectionHelper
    private var adapterDetailRail: CommonAdapterNew? = null


    override fun onFragmentInteraction() {
//        connectionObserver()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        railInjectionHelper = RailInjectionHelper(activity?.application!!)
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
            binding.listRecyclerview.visibility = View.VISIBLE
            railCommonDataList = ArrayList<RailCommonData>()
            setupFragment()
        } else {
            noInternetFragment()
        }
    }

    fun noInternetFragment() {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.content, NoInternetFragment())
        fragmentTransaction?.commit()
    }

    private fun setupFragment() {
        playLists = playListContent.split(",") as ArrayList<String>;
        playLists.addAll(playLists)
//        callShimmer()
        setRecyclerProperties(binding.listRecyclerview)
    }

    fun setRecyclerProperties(recyclerView: RecyclerView) {
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(false)
        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        getPlayListDetails(requireActivity())
    }

    private fun callShimmer() {

        val shimmerAdapter = ShimmerAdapter(
            activity, ShimmerDataModel(activity).getList(0), ShimmerDataModel(activity).slides
        )//        binding.listRecyclerview.hasFixedSize()
        binding.listRecyclerview.isNestedScrollingEnabled = false
        var num = 2
        val tabletSize: Boolean = resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            num =
                if (resources
                        .configuration.orientation == 2
                ) 4 else 3
        }
//        shimmerAdapter.setDataList(ShimmerDataModel(requireActivity()).getList(4))
        var gridLayoutManager = LinearLayoutManager(requireActivity())
        binding.listRecyclerview.layoutManager = gridLayoutManager
        binding.listRecyclerview.adapter = shimmerAdapter
        binding.listRecyclerview.visibility = View.VISIBLE

//        binding.listRecyclerview.setHasFixedSize(true)
        binding.listRecyclerview.setItemViewCacheSize(20)
        binding.listRecyclerview.isNestedScrollingEnabled = false
        binding.listRecyclerview.layoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL,
            false
        )

        binding.listRecyclerview.adapter = shimmerAdapter
        getPlayListDetails(requireActivity())
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
            APIServiceLayer.getInstance().getPlayListById(playListId, 0, 20)
                .observe(
                    activity,
                    { enveuCommonResponse: EnveuCommonResponse? ->
                        if (enveuCommonResponse != null && enveuCommonResponse.data != null) {
                            val screenWidget = BaseCategory()
                            screenWidget.layout = Layouts.HOR.name
                            screenWidget.contentImageType = ImageType.LDS.name
                            screenWidget.name = title
                            screenWidget.showHeader = true
                            val baseCategory = BaseCategory()
                            baseCategory.railCardType = RailCardType.IMAGE_ONLY.name
                            val railCommonData = RailCommonData(
                                enveuCommonResponse.data,
                                screenWidget,
                                false
                            )
                            if (count == 0) {
                                railCommonData.railType = HORIZONTAL_LDS_LANDSCAPE
                            } else {
                                railCommonData.railType = HORIZONTAL_PR_POTRAIT
                            }
                            railCommonDataList.add(railCommonData)

                            if (adapterDetailRail == null) {
                                RecyclerAnimator(activity).animate(binding.listRecyclerview)
                                adapterDetailRail = CommonAdapterNew(
                                    activity, railCommonDataList, this, this
                                )
                                binding.listRecyclerview.adapter = adapterDetailRail
                            } else {
                                synchronized(railCommonDataList) {
                                    adapterDetailRail?.notifyItemChanged(
                                        railCommonDataList.size - 1
                                    )
                                }
                            }
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


    override fun inflateBindingLayout(inflater: LayoutInflater): InternalPlaylistListFragmentBinding {
        return InternalPlaylistListFragmentBinding.inflate(inflater)
    }


    override fun railItemClick(item: RailCommonData?, position: Int) {
        val itemValue = item?.enveuVideoItemBeans?.get(position)!!
        if (AppCommonMethod.getCheckKEntryId(itemValue.getkEntryId())) {
            val getVideoId = itemValue.getkEntryId()
            PrintLogging.printLog("", "SearchAssetType-->>" + itemValue!!.assetType)
            AppCommonMethod.launchDetailScreen(
                context,
                getVideoId,
                itemValue.assetType,
                itemValue.id,
                "0",
                false,
                itemValue
            )
        } else {
            AppCommonMethod.launchDetailScreen(
                context,
                "",
                itemValue.assetType,
                itemValue.id,
                "0",
                false,
                itemValue
            )
        }
    }

    override fun moreRailClick(data: RailCommonData?, position: Int) {

    }
}