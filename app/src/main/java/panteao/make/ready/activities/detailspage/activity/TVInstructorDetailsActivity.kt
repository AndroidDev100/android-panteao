package panteao.make.ready.activities.detailspage.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.fragment.InstructorFragment
import panteao.make.ready.activities.detailspage.fragment.LatestSeasonsDetailsFragment
import panteao.make.ready.activities.detailspage.fragment.SeasonsDetailsFragment
import panteao.make.ready.activities.detailspage.fragment.SeriesDetailFragment
import panteao.make.ready.activities.detailspage.listeners.OnKeyEventListener
import panteao.make.ready.activities.detailspage.listeners.SeriesDetailsListener
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.ActivityTvInstructorBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.networking.apistatus.APIStatus
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class TVInstructorDetailsActivity : TvBaseBindingActivity<ActivityTvInstructorBinding>(),
    SeriesDetailsListener, NoInternetFragment.OnFragmentInteractionListener {
    val TAG = this.javaClass.name
    private lateinit var instructorDetails: EnveuVideoItemBean
    private var type: String? = null
    private lateinit var onKeyEventListener: OnKeyEventListener
    private lateinit var seriesDetailFragment: SeriesDetailFragment
    private lateinit var seasonDetailsFragment: Fragment
    private var isSeasonFragment: Boolean? = false
    private lateinit var railInjectionHelper: RailInjectionHelper
    private var noInternetFragment = NoInternetFragment()


    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityTvInstructorBinding {
        return ActivityTvInstructorBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        railInjectionHelper = RailInjectionHelper(this.application)
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
            initialization()
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
        instructorDetails =
            intent.getSerializableExtra(AppConstants.SELECTED_ITEM) as EnveuVideoItemBean
        callDetailPageApi(instructorDetails.id, type)
    }

    private fun callDetailPageApi(id: Int, contentType: String?) {
        Logger.e("INSTRUCTOR_RESPONSE", Gson().toJson(instructorDetails));
        setUI(instructorDetails)
        val railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        railInjectionHelper.getInstructorRelatedContent(id, 0, AppConstants.PAGE_SIZE, -1)
            .observe(this,
                { response ->
                    if (response != null && response.status.equals(APIStatus.SUCCESS.name, true)) {
                        val instructorFragment = InstructorFragment()
                        val bundle = Bundle()
                        bundle.putParcelable(
                            AppConstants.BUNDLE_SELECTED_SEASON,
                            response.baseCategory as RailCommonData
                        )
                        instructorFragment.arguments = bundle
                        addFragment(
                            instructorFragment,
                            R.id.persist,
                            false,
                            AppConstants.SEASON_DETAIL
                        )
                        binding.progressBar.visibility = View.GONE
                        Logger.e("INSTRUCTOR_RESPONSE", Gson().toJson(response));
                    }
                })
    }

    private fun setUI(videoDetail: EnveuVideoItemBean) {
        binding.contentsItem = videoDetail
    }


    override fun onResume() {
        super.onResume()
        if (supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) != null) {
            seasonDetailsFragment =
                supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) as InstructorFragment

            if (seasonDetailsFragment.view != null)
                seasonDetailsFragment.view?.requestFocus()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null) {
            return
        }
        instructorDetails =
            intent.getSerializableExtra(AppConstants.SELECTED_ITEM) as EnveuVideoItemBean
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