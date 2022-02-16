package panteao.make.ready.activities.internalpages.fragments


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.make.enums.ListingLayoutType
import com.make.enums.RailCardType
import panteao.make.ready.R
import panteao.make.ready.activities.internalpages.InternalPageImageType
import panteao.make.ready.activities.listing.listui.ListActivity
import panteao.make.ready.activities.listing.ui.GridActivity
import panteao.make.ready.activities.watchList.ui.WatchListActivity
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
import panteao.make.ready.utils.constants.AppConstants.*
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.cropImage.helpers.PrintLogging
import panteao.make.ready.utils.cropImage.helpers.ShimmerDataModel
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper
import panteao.make.ready.utils.helpers.RecyclerAnimator
import panteao.make.ready.utils.helpers.intentlaunchers.ActivityLauncher


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
        Log.w("internalPage","list")
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
        playLists = playListContent.split(",") as ArrayList<String>
        setRecyclerProperties(binding.listRecyclerview)
    }

    fun setRecyclerProperties(recyclerView: RecyclerView) {
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(false)
        val mLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        //ViewCompat.setNestedScrollingEnabled(recyclerView, false);
        getPlayListDetails(requireActivity())
    }

    private fun callShimmer() {

        val shimmerAdapter = ShimmerAdapter(
            activity, ShimmerDataModel().getList(0)
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
                            screenWidget.contentID=playListId
                            screenWidget.showHeader = true
                            screenWidget.contentShowMoreButton = true
                            if (imageType.equals(InternalPageImageType.PORTRAIT_2_3.name)){
                               screenWidget.contentImageType= ImageType.PR2.name
                            }else if (imageType.equals(InternalPageImageType.LANDSCAPE.name)){
                                screenWidget.contentImageType= ImageType.LDS.name
                            }else{
                                screenWidget.contentImageType= ImageType.LDS.name
                            }
                            val baseCategory = BaseCategory()
                            baseCategory.railCardType = RailCardType.IMAGE_ONLY.name
                            val railCommonData = RailCommonData(
                                enveuCommonResponse.data,
                                screenWidget,
                                false
                            )
                            Log.w("imageType-->",imageType)
                            if (imageType.equals(InternalPageImageType.PORTRAIT_2_3.name)){
                                railCommonData.railType = HORIZONTAL_PR_POSTER
                            }else if (imageType.equals(InternalPageImageType.LANDSCAPE.name)){
                                railCommonData.railType = HORIZONTAL_LDS_LANDSCAPE
                            }
                            else if (imageType.equals(InternalPageImageType.PORTRAIT.name)){
                                railCommonData.railType = HORIZONTAL_PR_POTRAIT
                            }
                            else if (imageType.equals(InternalPageImageType.SQUARE.name)){
                                railCommonData.railType = HORIZONTAL_SQR_SQUARE
                            }
                            else{
                                railCommonData.railType = HORIZONTAL_LDS_LANDSCAPE
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
                        }else{
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
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun inflateBindingLayout(): InternalPlaylistListFragmentBinding {
        return InternalPlaylistListFragmentBinding.inflate(inflater)
    }


    override fun railItemClick() {
        val itemValue = item?.enveuVideoItemBeans?.get(position)!!
        if (AppCommonMethod.getCheckKEntryId(itemValue.getkEntryId())) {
            val getVideoId = itemValue.getkEntryId()
            PrintLogging.printLog("SearchAssetType-->>" + itemValue.assetType)
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

    var playListId: String? = null
    override fun moreRailClick() {
        try {
            if (data!!.screenWidget != null) {
                if (data!!.screenWidget.contentID != null) playListId =
                    data!!.screenWidget.contentID else playListId =
                    data!!.screenWidget.landingPagePlayListId
                if (data!!.screenWidget.name != null && data!!.screenWidget.referenceName != null && (data!!.screenWidget.referenceName.equals(
                        AppConstants.ContentType.CONTINUE_WATCHING.name,
                        ignoreCase = true
                    ) || data!!.screenWidget.referenceName.equals(
                        "special_playlist",
                        ignoreCase = true
                    ))
                ) {
                    ActivityLauncher(requireActivity()).portraitListing(
                        requireActivity(),
                        GridActivity::class.java,
                        playListId,
                        data!!.screenWidget.name.toString(),
                        0,
                        0,
                        data!!.screenWidget,
                        position
                    )
                } else if (data!!.screenWidget.name != null && data!!.screenWidget.referenceName != null && data!!.screenWidget.referenceName.equals(
                        AppConstants.ContentType.MY_WATCHLIST.name,
                        ignoreCase = true
                    )
                ) {
                    ActivityLauncher(requireActivity()).watchHistory(
                        requireActivity(),
                        WatchListActivity::class.java, data!!.screenWidget.name.toString(), false
                    )
                } else {
                    if (data!!.screenWidget.contentListinglayout != null && !data!!.screenWidget.contentListinglayout.equals(
                            "",
                            ignoreCase = true
                        ) && data!!.screenWidget.contentListinglayout.equals(
                            ListingLayoutType.LST.name,
                            ignoreCase = true
                        )
                    ) {
                        if (data!!.screenWidget.name != null) {
                            ActivityLauncher(requireActivity()).listActivity(
                                requireActivity(),
                                ListActivity::class.java,
                                playListId,
                                data!!.screenWidget.name.toString(),
                                0,
                                0,
                                data!!.screenWidget
                            )
                        } else {
                            ActivityLauncher(requireActivity()).listActivity(
                                requireActivity(),
                                ListActivity::class.java, playListId, "", 0, 0, data!!.screenWidget
                            )
                        }
                    } else if (data!!.screenWidget.contentListinglayout != null && !data!!.screenWidget.contentListinglayout.equals(
                            "",
                            ignoreCase = true
                        ) && data!!.screenWidget.contentListinglayout.equals(
                            ListingLayoutType.GRD.name,
                            ignoreCase = true
                        )
                    ) {
                        Logger.e("getRailData", "GRD")
                        if (data!!.screenWidget.name != null) {
                            ActivityLauncher(requireActivity()).portraitListing(
                                requireActivity(),
                                GridActivity::class.java,
                                playListId,
                                data!!.screenWidget.name.toString(),
                                0,
                                0,
                                data!!.screenWidget,
                                position
                            )
                        } else {
                            ActivityLauncher(requireActivity()).portraitListing(
                                requireActivity(),
                                GridActivity::class.java,
                                playListId,
                                "",
                                0,
                                0,
                                data!!.screenWidget,
                                position
                            )
                        }
                    } else {
                        Logger.e("getRailData", "PDF")
                        if (data!!.screenWidget.name != null) {
                            ActivityLauncher(requireActivity()).portraitListing(
                                requireActivity(),
                                GridActivity::class.java,
                                playListId,
                                data!!.screenWidget.name.toString(),
                                0,
                                0,
                                data!!.screenWidget,
                                position
                            )
                        } else {
                            ActivityLauncher(requireActivity()).portraitListing(
                                requireActivity(),
                                GridActivity::class.java,
                                playListId,
                                "",
                                0,
                                0,
                                data!!.screenWidget,
                                position
                            )
                        }
                    }
                }
            }
        }catch (exception : Exception){

        }
    }
}