package panteao.make.ready.activities.detailspage.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.fragment.VODVideoDetailFragment
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.ActivityVideoDetailBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class VideoDetailActivity : TvBaseBindingActivity<ActivityVideoDetailBinding>(),
    NoInternetFragment.OnFragmentInteractionListener {


    val TAG = this.javaClass.name
    private var id: Int? = null
    private var type: String? = null
    private lateinit var vODVideoDetailFragment: VODVideoDetailFragment
    private lateinit var railInjectionHelper: RailInjectionHelper
    private var noInternetFragment = NoInternetFragment()


    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityVideoDetailBinding {
        return ActivityVideoDetailBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        railInjectionHelper == RailInjectionHelper(this.application)
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
        val assetId = id
//        railInjectionHelper.getAssetDetails(assetId.toString()).observe(this, Observer {
//            if (it != null && it.getEnveuVideoItemBeans()!!.size > 0) {
//                if (it.getEnveuVideoItemBeans()
//                        ?.get(0)?.responseCode == AppConstants.RESPONSE_CODE_SUCCESS
//                ) {
//                    var videoDetail = it.getEnveuVideoItemBeans()!!.get(0)
//                    binding?.progressBar?.visibility = View.GONE
//                    setUI(videoDetail)
//                    vODVideoDetailFragment = VODVideoDetailFragment.newInstance()
//                    val bundle = Bundle()
//                    bundle.putSerializable(
//                        AppConstants.VIDEO_DETAIL,
//                        videoDetail as EnveuVideoItemBean
//                    )
//                    bundle.putString(
//                        AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE,
//                        it.getEnveuVideoItemBeans()!![0].brightcoveVideoId.toString()
//                    )
//                    vODVideoDetailFragment.arguments = bundle
//                    addFragment(
//                        vODVideoDetailFragment,
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

    private fun setUI(videoDetail: EnveuVideoItemBean) {
        binding?.content = videoDetail
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


}