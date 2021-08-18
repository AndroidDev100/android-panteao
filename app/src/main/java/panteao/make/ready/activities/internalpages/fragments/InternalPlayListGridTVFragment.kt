package panteao.make.ready.activities.internalpages.fragments


import android.os.Bundle
import android.util.Log
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.LifecycleOwner
import com.google.gson.Gson
import com.make.baseCollection.baseCategoryModel.BaseCategory
import com.make.enums.ImageType
import com.make.enums.Layouts
import panteao.make.ready.R
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.cardlayout.cardpresenter.AssetCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.EpisodeCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.PopularSearchCardPresenter
import panteao.make.ready.cardlayout.cardview.PopularSearchCardView
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.servicelayer.APIServiceLayer
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class InternalPlayListGridTVFragment : VerticalGridSupportFragment(), OnItemViewClickedListener,
    OnItemViewSelectedListener {
    private lateinit var imageType: String
    private lateinit var playListId: String
    private lateinit var railInjectionHelper: RailInjectionHelper
    private lateinit var railCommonData: RailCommonData
    private lateinit var playListContent: String
    private var pageNumber: Int = 0
    private var totalPages: Int = 0
    private lateinit var mAdapter: ArrayObjectAdapter
    private var seriesId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playListContent =
            arguments?.getString(AppConstants.PLAYLIST_CONTENT)!!
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
        if(aBoolean){
            setupFragment()
        }else{
            noInternetFragment()
        }
    }
    fun noInternetFragment() {
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.content, NoInternetFragment())
        fragmentTransaction?.commit()
    }

    private fun setupFragment() {
        onItemViewClickedListener = this
        railInjectionHelper = RailInjectionHelper(requireActivity().application)
        setOnItemViewSelectedListener(this)
        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
        mAdapter = ArrayObjectAdapter(PopularSearchCardPresenter("16x9"))
        playListContent.split("|").forEachIndexed { i, s ->
            if (i == 0) {
                playListId = s.replace("\"", "").trim()
            } else if (i == 2) {
                imageType = s.replace("\"", "").trim()
            }
        }
        getPlayListDetails(playListId)

    }

    private fun getPlayListDetails(playListId: String) {
        APIServiceLayer.getInstance().getPlayListById(playListId, 0, 20)
            .observe(
                (activity as LifecycleOwner?)!!,
                { enveuCommonResponse: EnveuCommonResponse? ->
                    if (enveuCommonResponse != null && enveuCommonResponse.data != null) {
                        val screenWidget = BaseCategory()
                        screenWidget.layout = Layouts.HOR.name
                        if(imageType == "LANDSCAPE") {
                            screenWidget.contentImageType = ImageType.LDS.name
                        }else if(imageType == "PORTRAIT") {
                            screenWidget.contentImageType = ImageType.PR1.name
                        }else if(imageType == "PORTRAIT_2_3") {
                            screenWidget.contentImageType = ImageType.PR2.name
                        }else if(imageType == "SQUARE") {
                            screenWidget.contentImageType = ImageType.SQR.name
                        }else{
                            screenWidget.contentImageType = ImageType.LDS.name
                        }
                        val railCommonData =
                            RailCommonData(enveuCommonResponse.data, screenWidget, false)
                        mAdapter.addAll(0, railCommonData.enveuVideoItemBeans)
                        adapter = mAdapter
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "Resumed")
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


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView()")
    }

    companion object {

        private val TAG = "VerticalEpisodeFragment"
        private val NUM_COLUMNS = 5
    }

    override fun onItemSelected(
        itemViewHolder: Presenter.ViewHolder?,
        item: Any?,
        rowViewHolder: RowPresenter.ViewHolder?,
        row: Row?
    ) {
        if (item is EnveuVideoItemBean) {
            val position: Int = (adapter as ArrayObjectAdapter).indexOf(item)

            val cardRowAdapterSelected = adapter as ArrayObjectAdapter
            if ((position == cardRowAdapterSelected.size() - 3) && (pageNumber < totalPages)) {
                pageNumber += 1
//                railInjectionHelper.getEpisodeNoSeason(
//                    seriesId,
//                    pageNumber,
//                    AppConstants.PAGE_SIZE,
//                    -1
//                ).observe(activity!!, Observer { response ->
//                    if (response != null) {
//                        if (response != null && response.getEnveuVideoItemBeans()!!.size > 0) {
////                            setVerticalEpisodeGridFragment(response.getEnveuVideoItemBeans() as ArrayList<EnveuVideoItemBean>)
////                            Log.d(TAG, " DATA FOUND" + selectedSeasonId)
//                            mAdapter.addAll(mAdapter.size(), response.getEnveuVideoItemBeans())
//                            adapter.notifyItemRangeChanged(mAdapter.size(), response.totalCount)
//                        } else {
//                            Log.d(TAG, " NO DATA FOUND")
//                        }
//                    }
//
//                })

            }
        }
    }

    fun playFirstEpisode() {
//        AppCommonMethod.loadPlayer(seasonEpisodeData[0],activity!!,seasonEpisodeData,seriesId)
    }
}