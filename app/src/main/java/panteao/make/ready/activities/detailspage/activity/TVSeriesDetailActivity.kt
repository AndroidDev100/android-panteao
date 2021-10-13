package panteao.make.ready.activities.detailspage.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.SeriesDetailManager
import panteao.make.ready.activities.detailspage.fragment.LatestSeasonsDetailsFragment
import panteao.make.ready.activities.detailspage.fragment.SeasonsDetailsFragment
import panteao.make.ready.activities.detailspage.fragment.SeriesDetailFragment
import panteao.make.ready.activities.detailspage.listeners.OnKeyEventListener
import panteao.make.ready.activities.detailspage.listeners.SeriesDetailsListener
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.ActivityTvSeriesDetailsBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.apistatus.APIStatus
import panteao.make.ready.networking.responsehandler.ResponseModel
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper
import panteao.make.ready.utils.helpers.database.preferences.UserPreference


class TVSeriesDetailActivity : TvBaseBindingActivity<ActivityTvSeriesDetailsBinding>(),
    SeriesDetailsListener, NoInternetFragment.OnFragmentInteractionListener {
    val TAG = this.javaClass.name
    private var id: Int? = null
    private var type: String? = null
    private lateinit var onKeyEventListener: OnKeyEventListener
    private lateinit var seriesDetailFragment: SeriesDetailFragment
    private lateinit var seasonDetailsFragment: Fragment
    private var isSeasonFragment: Boolean? = false
    private lateinit var railInjectionHelper: RailInjectionHelper
    private var noInternetFragment = NoInternetFragment()


    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityTvSeriesDetailsBinding {
        return ActivityTvSeriesDetailsBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        railInjectionHelper = RailInjectionHelper(this.application)
        initialization()
        connectionObserver()
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    private fun connectionValidation(boolean: Boolean) {
        if (boolean) {
            binding?.progressBar?.visibility = View.VISIBLE
            callDetailPageApi(id, type)
        } else {
            addFragment(
                noInternetFragment,
                android.R.id.content,
                true,
                AppConstants.TAG_NO_INTERNET_FRAGMENT
            )
        }
    }

    override fun onFragmentInteraction() {
        if (isOnline(this)) {
            hideFragment(noInternetFragment, AppConstants.TAG_NO_INTERNET_FRAGMENT)
            connectionObserver()
        } else {
            Toast.makeText(this, getString(R.string.check_connection), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initialization() {
        id = intent.getSerializableExtra(AppConstants.SELECTED_ITEM) as Int
        callDetailPageApi(id, type)
    }

    private fun callDetailPageApi(id: Int?, contentType: String?) {
        val railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        railInjectionHelper.getSeriesDetailsV2(id.toString())
            .observe(this@TVSeriesDetailActivity,
                { response ->
                    if (response != null) {
                        if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                        } else if (response.status.equals(
                                APIStatus.SUCCESS.name,
                                ignoreCase = true
                            )
                        ) {
                            if (response.baseCategory != null) {
                                binding.progressBar.visibility = View.GONE
                                val enveuCommonResponse = response.baseCategory as RailCommonData
                                val selectedSeriesDetail =
                                    enveuCommonResponse.enveuVideoItemBeans?.get(0)
                                SeriesDetailManager.getInstance().selectedSeries =
                                    selectedSeriesDetail
                                setUI(selectedSeriesDetail!!)
                            }
                        } else if (response.status.equals(
                                APIStatus.ERROR.name,
                                ignoreCase = true
                            )
                        ) {
                            if (response.errorModel.errorCode != 0) {
                                if (response.errorModel.errorCode == AppConstants.RESPONSE_CODE_LOGOUT) {
                                    if (UserPreference.instance.isLogin) {
                                        //                                        hitApiLogout()
                                    }
                                    // showDialog(SeriesDetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.logged_out));
                                } else {
//                                    showDialog(
//                                        this@TVSeriesDetailActivity.getResources()
//                                            .getString(R.string.error),
//                                        resources.getString(R.string.something_went_wrong)
//                                    )
                                }
                            }
                        } else if (response.status.equals(
                                APIStatus.FAILURE.name,
                                ignoreCase = true
                            )
                        ) {
//                            showDialog(
//                                this@TVSeriesDetailActivity.getResources().getString(R.string.error),
//                                resources.getString(R.string.something_went_wrong)
//                            )
                        }
                    }
                })

    }

    private fun setUI(videoDetail: EnveuVideoItemBean) {
        binding.content = videoDetail
        seriesDetailFragment = SeriesDetailFragment.newInstance()
        val bundle = Bundle()
        bundle.putParcelable(AppConstants.SELECTED_SERIES, videoDetail)
//        SeriesDetailManager.getInstance().setBookmarks(continueWatchingBookmarkList)
        seriesDetailFragment.arguments = bundle
        addFragment(
            seriesDetailFragment,
            R.id.video_details_fragment_frame,
            false,
            TAG
        )
    }


    override fun onResume() {
        super.onResume()
        if (supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) != null) {
            if (supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) is LatestSeasonsDetailsFragment)
                seasonDetailsFragment =
                    supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) as LatestSeasonsDetailsFragment
            else
                seasonDetailsFragment =
                    supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) as SeasonsDetailsFragment

            if (seasonDetailsFragment.view != null)
                seasonDetailsFragment.view?.requestFocus()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null) {
            return
        }
        id = intent.getSerializableExtra(AppConstants.SELECTED_ITEM) as Int
        type = intent.getSerializableExtra(AppConstants.SELECTED_CONTENT_TYPE) as String
        connectionObserver()
    }

    override fun onEpisodeClicked() {
        val seasonsDetailsFragment = SeasonsDetailsFragment.newInstance()
        onKeyEventListener = seasonsDetailsFragment
        isSeasonFragment = true
        addFragment(
            seasonsDetailsFragment,
            R.id.video_details_fragment_frame,
            true,
            AppConstants.SEASON_DETAIL
        )
    }

    override fun showSeasonFragment(show: Boolean) {
    }

}