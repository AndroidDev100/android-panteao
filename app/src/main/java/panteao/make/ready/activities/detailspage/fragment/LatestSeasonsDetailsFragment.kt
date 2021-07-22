package panteao.make.ready.activities.detailspage.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.SeasonSelectionManager
import panteao.make.ready.activities.detailspage.SeriesDetailManager
import panteao.make.ready.activities.detailspage.listeners.OnKeyEventListener
import panteao.make.ready.activities.detailspage.listeners.SeasonsClickListener
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModel.selectedSeason.SelectedSeasonModel
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.FragmentLatestSeasonDetailsBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.apistatus.APIStatus
import panteao.make.ready.tvBaseModels.basemodels.TVBaseActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class LatestSeasonsDetailsFragment : Fragment(), SeasonsClickListener, OnKeyEventListener {

    val TAG = "SeasonsDetailsFragment"

    private lateinit var fragmentSeasonDetailsBinding: FragmentLatestSeasonDetailsBinding
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
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_latest_season_details,
                container,
                false
            )
        railInjectionHelper = RailInjectionHelper(requireActivity().application)
        return fragmentSeasonDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedSeries = SeriesDetailManager.getInstance().selectedSeries
        seasonCount = selectedSeries.seasonCount
        if (seasonCount > 0) {
            seasonList = ArrayList()
            selectedSeasonId = selectedSeries.seasons[selectedSeries.seasons.size - 1] as Int?
            prepareSeasonList()
        } else {
            seasonList = ArrayList()
            selectedSeasonId = 1
            seasonList?.add(SelectedSeasonModel(getString(R.string.all_episode), 0, true))
            seasonList?.let { showSeasonList(it, selectedSeasonId) }
        }
    }

    private fun prepareSeasonList() {
        if (seasonList != null)
            seasonList?.clear()
        selectedSeries.seasons.forEachIndexed { index,
                                                seasonNumber ->
            if (selectedSeasonId == index)
                seasonList?.add(SelectedSeasonModel("Season $index", index, true))
            else
                seasonList?.add(SelectedSeasonModel("Season $index", index, false))
        }
        seasonList?.let { showSeasonList(it, selectedSeasonId) }
    }


    private fun showSeasonList(seasonList: ArrayList<SelectedSeasonModel>, selectedSeasonId: Int?) {
//        val listAdapter = SeasonsAdapter(seasonList, this)
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
                getAllEpisodes()
            }
        } else {
            val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(android.R.id.content, NoInternetFragment())?.commit()
        }
    }


    private fun getSeasonEpisodes() {
        fragmentSeasonDetailsBinding.progressBar.visibility = View.VISIBLE
        activity?.findViewById<Button>(R.id.button_play)?.text = getString(R.string.loading)
        selectedSeasonId?.let { it ->
            railInjectionHelper.getEpisodeNoSeasonV2(
                selectedSeries.id,
                0,
                AppConstants.PAGE_SIZE,
                it
            )
                .observe(requireActivity(), Observer {
                    when (it.status) {
                        APIStatus.SUCCESS.name -> {
                            val railCommonData = it.baseCategory as RailCommonData;
                            if (it != null && railCommonData.enveuVideoItemBeans!!.size > 0) {
                                activity?.findViewById<Button>(R.id.button_play)?.text =
                                    getString(R.string.play) + " S$selectedSeasonId: EP. 1"
                                setVerticalEpisodeGridFragment(
                                    requireActivity() as TVBaseActivity,
                                    railCommonData,
                                    railCommonData.enveuVideoItemBeans as ArrayList<EnveuVideoItemBean>
                                )
                            } else {
                                activity?.findViewById<Button>(R.id.button_play)
                                    ?.setCompoundDrawables(null, null, null, null)
                                activity?.findViewById<Button>(R.id.button_play)?.text =
                                    getString(R.string.new_episodes_added_soon)
                                activity?.findViewById<Button>(R.id.button_episodes)?.visibility =
                                    View.GONE
                                fragmentSeasonDetailsBinding.progressBar.visibility = View.GONE
                            }
                        }
                        APIStatus.ERROR.name, APIStatus.FAILURE.name
                        -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        connectionObserver()
    }

    private fun setVerticalEpisodeGridFragment(
        activity: TVBaseActivity,
        response: RailCommonData,
        enveuVideoItemBeansList: ArrayList<EnveuVideoItemBean>
    ) {

        if (SeasonSelectionManager.getInstance().seasonEpisodeList != null) {
            SeasonSelectionManager.getInstance().seasonEpisodeList.clear()
        }

        SeasonSelectionManager.getInstance().seasonEpisodeList = enveuVideoItemBeansList
        val verticalGridFragment = EpisodesFragment()
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.BUNDLE_SELECTED_SEASON, response)
        bundle.putInt(AppConstants.BUNDLE_SERIES_ID, selectedSeries.id)
        verticalGridFragment.arguments = bundle
        activity.addFragment(
            verticalGridFragment,
            fragmentSeasonDetailsBinding.persist.id,
            false,
            "VerticalEpisodeFragment"
        )
        fragmentSeasonDetailsBinding.progressBar.visibility = View.GONE

    }

    private fun getAllEpisodes() {
        fragmentSeasonDetailsBinding.progressBar.visibility = View.VISIBLE
        activity?.findViewById<Button>(R.id.button_play)?.text = getString(R.string.loading)
        railInjectionHelper.getEpisodeNoSeasonV2(
            selectedSeries.id,
            0,
            AppConstants.PAGE_SIZE,
            -1
        )
            .observe(requireActivity(), Observer {
                when (it.status) {
                    APIStatus.SUCCESS.name -> {
                        val railCommonData = it.baseCategory as RailCommonData;
                        if (it != null && railCommonData.enveuVideoItemBeans!!.size > 0) {
                            activity?.findViewById<Button>(R.id.button_play)?.text =
                                getString(R.string.play) + " S$selectedSeasonId: EP. 1"
                            setVerticalEpisodeGridFragment(
                                requireActivity() as TVBaseActivity,
                                railCommonData,
                                railCommonData.enveuVideoItemBeans as ArrayList<EnveuVideoItemBean>
                            )
                        } else {
                            activity?.findViewById<Button>(R.id.button_play)
                                ?.setCompoundDrawables(null, null, null, null)
                            activity?.findViewById<Button>(R.id.button_play)?.text =
                                getString(R.string.new_episodes_added_soon)
                            activity?.findViewById<Button>(R.id.button_episodes)?.visibility =
                                View.GONE
                            fragmentSeasonDetailsBinding.progressBar.visibility = View.GONE
                        }
                    }
                    APIStatus.ERROR.name, APIStatus.FAILURE.name
                    -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
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
        fun newInstance() = LatestSeasonsDetailsFragment()
    }


    fun getPremimumMark(menuModel: RailCommonData) {

    }

    override fun onSeasonClick(position: Int) {
        selectedSeasonId = position
        connectionObserver()
    }
}
