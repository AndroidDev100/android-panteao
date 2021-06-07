package panteao.make.ready.activities.detailspage.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.SeasonSelectionManager
import panteao.make.ready.activities.detailspage.SeasonsAdapter
import panteao.make.ready.activities.detailspage.SeriesDetailManager
import panteao.make.ready.activities.detailspage.listeners.OnKeyEventListener
import panteao.make.ready.activities.detailspage.listeners.SeasonsClickListener
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModel.selectedSeason.SelectedSeasonModel
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.FragmentSeasonDetailsBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.tvBaseModels.basemodels.TVBaseActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper

class SeasonsDetailsFragment : Fragment(), SeasonsClickListener, OnKeyEventListener {

    val TAG = "SeasonsDetailsFragment"

    private lateinit var fragmentSeasonDetailsBinding: FragmentSeasonDetailsBinding
    //    private var selectedMovie: Asset? = null
//    private var railData: RailCommonData? = null

    private lateinit var selectedSeries: EnveuVideoItemBean
    private lateinit var railInjectionHelper: RailInjectionHelper
    private var selectedSeasonId: Int? = 0
    private var seasonCount: Int = 0
    private var seasonList: ArrayList<SelectedSeasonModel>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentSeasonDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_season_details, container, false)
        railInjectionHelper = RailInjectionHelper(requireActivity().application)
        return fragmentSeasonDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedSeries = SeriesDetailManager.getInstance().selectedSeries
        fragmentSeasonDetailsBinding.recyclerEpisodes.requestFocus()
        seasonCount = selectedSeries.seasonCount
        fragmentSeasonDetailsBinding.content = selectedSeries
        if (seasonCount > 0) {
            seasonList = ArrayList()
            selectedSeasonId = selectedSeries.seasons[0] as Int?
            prepareSeasonList()
        } else {
            seasonList = ArrayList()
            selectedSeasonId = 1
            seasonList?.add(SelectedSeasonModel(getString(R.string.all_episode), 0, true))
            seasonList?.let { showSeasonList(it, selectedSeasonId) }
        }
        connectionObserver()
    }

    private fun prepareSeasonList() {
        if (seasonList != null)
            seasonList?.clear()
        selectedSeries.seasons.forEachIndexed { index,
                                                seasonNumber ->
            if (selectedSeasonId == seasonNumber)
                seasonList?.add(SelectedSeasonModel(getString(R.string.season)+" "+seasonNumber, seasonNumber, true))
            else
                seasonList?.add(SelectedSeasonModel(getString(R.string.season)+" "+seasonNumber,
                    seasonNumber as Int, false))
        }
        seasonList?.let { showSeasonList(it, selectedSeasonId) }
    }

    private fun showSeasonList(seasonList: ArrayList<SelectedSeasonModel>, selectedSeasonId: Int?) {
        val listAdapter = SeasonsAdapter(seasonList, this)
        fragmentSeasonDetailsBinding.recyclerEpisodes.layoutManager = LinearLayoutManager(activity)
        fragmentSeasonDetailsBinding.recyclerEpisodes.setAdapter(listAdapter)
    }


    private fun connectionObserver() {
        activity?.applicationContext?.let { context ->
            if (NetworkConnectivity.isOnline(context)) {
                connectionValidation(true)
            } else {
                connectionValidation(false)
            }
        }

    }

    private fun connectionValidation(aBoolean: Boolean?) {
        if (aBoolean == true) {
            if (seasonCount > 0) {
                getSeasonEpisodes()
            } else {
                //here -1 indicates not to send seasonNumber key
                getAllEpisodes()
            }
        } else {
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(android.R.id.content, NoInternetFragment())?.commit()

        }
    }


    private fun setVerticalEpisodeGridFragment(
        response: RailCommonData,
        enveuVideoItemBeansList: ArrayList<EnveuVideoItemBean>
    ) {

        if (SeasonSelectionManager.getInstance().seasonEpisodeList != null) {
            SeasonSelectionManager.getInstance().seasonEpisodeList.clear()
        }

        SeasonSelectionManager.getInstance().seasonEpisodeList = enveuVideoItemBeansList
        val mActivity = activity as TVBaseActivity
        val verticalGridFragment = EpisodesFragment()
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.BUNDLE_SELECTED_SEASON, response)
        bundle.putInt(AppConstants.BUNDLE_SERIES_ID, selectedSeries.id)
        verticalGridFragment.arguments = bundle
        mActivity.addFragment(
            verticalGridFragment,
            fragmentSeasonDetailsBinding.persist.id,
            false,
            "VerticalEpisodeFragment"
        )
    }

    private fun getSeasonEpisodes() {
        selectedSeasonId?.let {
//            railInjectionHelper.getEpisodeNoSeason(selectedSeries.id, 0, 10, it)
//                .observe(this, Observer {
//                    if (it != null && it.getEnveuVideoItemBeans()!!.size > 0) {
//                        setVerticalEpisodeGridFragment(
//                            it,
//                            it.getEnveuVideoItemBeans() as ArrayList<EnveuVideoItemBean>
//                        )
//                        Log.d(TAG, " DATA FOUND" + selectedSeasonId)
//                    } else {
//                        Log.d(TAG, " NO DATA FOUND")
//                    }
//                })
        }
    }

    private fun getAllEpisodes() {
//        railInjectionHelper.getEpisodeNoSeason(selectedSeries.id, 0, AppConstants.PAGE_SIZE, -1)
//            .observe(activity!!, Observer<RailCommonData> { response ->
//                if (response != null) {
//                    if (response != null && response.getEnveuVideoItemBeans()!!.size > 0) {
//                        setVerticalEpisodeGridFragment(
//                            response,
//                            response.getEnveuVideoItemBeans() as ArrayList<EnveuVideoItemBean>
//                        )
//                        Log.d(TAG, " DATA FOUND" + selectedSeasonId)
//                    } else {
//                        Log.d(TAG, " NO DATA FOUND")
//                    }
//                }
//            })

    }


    override fun onKeyPress(keyCode: Int) {

    }

    override fun onBackPress() {

    }

    override fun onResume() {
        super.onResume()
//        fragmentSeasonDetailsBinding.persist.requestFocus()
    }

    companion object {
        fun newInstance() = SeasonsDetailsFragment()
    }


    fun getPremimumMark(menuModel: RailCommonData) {

    }

    override fun onSeasonClick(position: Int) {
        selectedSeasonId = position
        connectionObserver()
    }
}
