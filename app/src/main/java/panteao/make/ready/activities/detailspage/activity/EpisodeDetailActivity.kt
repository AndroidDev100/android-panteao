package panteao.make.ready.activities.detailspage.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.SeasonSelectionManager
import panteao.make.ready.activities.detailspage.fragment.EpisodeDetailFragment
import panteao.make.ready.activities.detailspage.fragment.EpisodeListFragment
import panteao.make.ready.activities.detailspage.listeners.BottomFragmentListener
import panteao.make.ready.activities.detailspage.listeners.OnFocusListener
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.ActivityEpisodeDetailBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class EpisodeDetailActivity : TvBaseBindingActivity<ActivityEpisodeDetailBinding>(),
    OnFocusListener, BottomFragmentListener, NoInternetFragment.OnFragmentInteractionListener {
    val TAG = this.javaClass.name
    private var id: Int? = null
    private var type: String? = null
    private var seriesId: String? = null
    private var season: String? = null
    private lateinit var episodeDetailFragment: EpisodeDetailFragment
    private lateinit var episodeListFragment: EpisodeListFragment
    private lateinit var  railInjectionHelper:RailInjectionHelper
    private var noInternetFragment = NoInternetFragment()
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityEpisodeDetailBinding {
        return ActivityEpisodeDetailBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialization()
        railInjectionHelper= RailInjectionHelper(this.application)
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
            binding.progressBar.visibility = View.VISIBLE
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
        seriesId = intent.getSerializableExtra(AppConstants.SELECTED_SERIES_ID) as String
        season = intent.getSerializableExtra(AppConstants.SELECTED_SEASON_NO) as String
        callDetailPageApi(id, type)
    }

    private fun callSeriesDetailApi(seriesId: String?, season: String?) {
        seriesId?.toInt()?.let {
            season?.toInt()?.let { it1 ->
//                railInjectionHelper.getEpisodeNoSeasonV2(it, 0, 10, it1).observe(this, Observer {
//                    if (it != null && it.getEnveuVideoItemBeans()!!.isNotEmpty()) {
//                        setVerticalEpisodeGridFragment(it.getEnveuVideoItemBeans() as ArrayList<EnveuVideoItemBean>)
//
//                    } else {
//                        Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show()
//                    }
//                })
            }
        }


    }


    private fun callDetailPageApi(id: Int?, contentType: String?) {
        val assetId = id
//        railInjectionHelper.getAssetDetailsV2(assetId.toString()).observe(this, Observer {
//            if (it != null && it.getEnveuVideoItemBeans()!!.size > 0) {
//                if (it.getEnveuVideoItemBeans()!!.get(0).responseCode == AppConstants.RESPONSE_CODE_SUCCESS
//                ) {
//                    binding?.progressBar?.visibility = View.GONE
//
//                    var videoDetail = it.getEnveuVideoItemBeans()!!.get(0)
//                    if (videoDetail.seriesId != null && videoDetail.seriesId != "") {
//                        seriesId = videoDetail.seriesId
//                    } else {
//
//                    }
//                    if (videoDetail.seasonNumber != null && videoDetail.seasonNumber != "") {
//                        callSeriesDetailApi(seriesId, videoDetail.seasonNumber)
//                    } else {
//                        callSeriesDetailApi(seriesId, videoDetail.seasonNumber)
//
//                    }
//                    setUI(videoDetail)
//                    episodeDetailFragment = EpisodeDetailFragment.newInstance()
//                    val bundle = Bundle()
//                    bundle.putSerializable(
//                        AppConstants.EPISODE_DETAIL,
//                        videoDetail
//                    )
//                    episodeDetailFragment.arguments = bundle
//                    addFragment(
//                        episodeDetailFragment,
//                        R.id.video_details_fragment_frame,
//                        false,
//                        "VODDetailPage"
//                    )
//
//                } else {
////                    Toast.makeText(this,"Some thing went wrong", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })

    }

    private fun setVerticalEpisodeGridFragment(enveuVideoItemBeansList: ArrayList<EnveuVideoItemBean>) {
        if (SeasonSelectionManager.getInstance().seasonEpisodeList != null) {
            SeasonSelectionManager.getInstance().seasonEpisodeList.clear()
        }
        SeasonSelectionManager.getInstance().seasonEpisodeList = enveuVideoItemBeansList
        episodeListFragment = EpisodeListFragment.newInstance()
//        addFragment(episodeListFragment, R.id.series_season_data, false, "VODDetailPage")
    }

    private fun setUI(videoDetail: EnveuVideoItemBean) {
        binding?.content = videoDetail
    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (::episodeListFragment.isInitialized)
            episodeListFragment.onKeypadClicked(keyCode)

        return super.onKeyUp(keyCode, event)

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


    override fun showFragment(show: Boolean) {
        if (show) {
            episodeDetailFragment.setFocus(show)
        }
    }


    override fun onShowBottomFragment(show: Boolean) {
        if (show) {
            binding?.seriesSeasonData?.visibility = View.VISIBLE
        } else {
            binding?.seriesSeasonData?.visibility = View.GONE
        }
    }

    override fun onEpisodeClicked(episodeId: Int) {
        if (id != episodeId) {
            id = episodeId
            callDetailPageApi(id, type)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SeasonSelectionManager.getInstance().clearVideoDetailsCache()
    }

}