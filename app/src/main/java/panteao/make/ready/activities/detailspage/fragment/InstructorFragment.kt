package panteao.make.ready.activities.detailspage.fragment


import android.os.Bundle
import android.util.Log
import androidx.leanback.app.VerticalGridSupportFragment
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import com.google.gson.Gson
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.cardlayout.cardpresenter.EpisodeCardPresenter
import panteao.make.ready.cardlayout.cardpresenter.InstructorCardPresenter
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.RailInjectionHelper


class InstructorFragment : VerticalGridSupportFragment(), OnItemViewClickedListener,
    OnItemViewSelectedListener {
    private lateinit var railInjectionHelper: RailInjectionHelper
    private lateinit var seasonEpisodeData: ArrayList<EnveuVideoItemBean>
    private lateinit var railCommonData: RailCommonData
    private var pageNumber: Int = 0
    private var totalPages: Int = 0
    private lateinit var mAdapter: ArrayObjectAdapter
    private var seriesId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        railCommonData =
            arguments?.getParcelable(AppConstants.BUNDLE_SELECTED_SEASON)!!
        Logger.e("RAIL_COMMON_DATA", Gson().toJson(railCommonData))
        pageNumber = railCommonData.pageNumber
        totalPages = railCommonData.pageTotal
        seasonEpisodeData = (railCommonData.enveuVideoItemBeans as ArrayList<EnveuVideoItemBean>?)!!
        setupFragment()
    }

    private fun setupFragment() {
        onItemViewClickedListener = this
        railInjectionHelper = RailInjectionHelper(requireActivity().application)
        setOnItemViewSelectedListener(this)
        val gridPresenter = VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false)
        gridPresenter.numberOfColumns = NUM_COLUMNS
        setGridPresenter(gridPresenter)
        mAdapter = ArrayObjectAdapter(InstructorCardPresenter(0))
        mAdapter.addAll(0, seasonEpisodeData)
        adapter = mAdapter
    }

    override fun onResume() {
        super.onResume()
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
        private val NUM_COLUMNS = 3
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