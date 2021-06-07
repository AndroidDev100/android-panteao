package panteao.make.ready.activities.detailspage.fragment


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import panteao.make.ready.R
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import panteao.make.ready.activities.detailspage.repository.VideoDetailRepository
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.VideoDetailsFragmentBinding
import panteao.make.ready.utils.constants.AppConstants


class VODVideoDetailFragment : Fragment(), View.OnClickListener {

    val TAG = this.javaClass.name

    private lateinit var videoDetailsFragmentBinding: VideoDetailsFragmentBinding
    private var bundle: Bundle? = null
    private var videoDetail: EnveuVideoItemBean? = null
    private var isAddedToWatchList: Boolean? = false
    private var isAddedToLike: Boolean? = false
    private var isLogin: Boolean? = false
    private var idFromAssetWatchlist: Int? = null
    private var accessToken: String? = null
    private var brightCoveVideoId: String? = null

    companion object {
        fun newInstance(): VODVideoDetailFragment {
            return VODVideoDetailFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videoDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.video_details_fragment, container, false)
        videoDetailsFragmentBinding.buttonPlay.requestFocus()
        bundle = this.arguments
        return videoDetailsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brightCoveVideoId = bundle?.getString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE)
        videoDetail = bundle?.getSerializable(AppConstants.VIDEO_DETAIL) as EnveuVideoItemBean

        moreButton()
        videoDetailsFragmentBinding.contentsItem = videoDetail
        videoDetailsFragmentBinding.buttonPlay.setOnClickListener(this)
        videoDetailsFragmentBinding.buttonWatchlist.setOnClickListener(this)
        videoDetailsFragmentBinding.buttonFollow.setOnClickListener(this)

        accessToken = UserPreference.instance.userAuthToken
        if (UserPreference.instance.isLogin) {
            callCheckWatchListApi()
            callCheckLike()
        }
    }

    override fun onPause() {
        super.onPause()
        view?.isFocusableInTouchMode = false
        view?.isFocusable = false

    }

    override fun onResume() {
        videoDetailsFragmentBinding.buttonPlay.requestFocus()
        super.onResume()
    }


    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.button_play -> {
//                AppCommonMethod.loadPlayer(videoDetail, activity!!, ArrayList(), 0)
//            }
//            R.id.button_watchlist -> {
//                if (UserPreference.instance.isLogin) {
//                    callRemoveFromWatchListApi()
//                } else {
//                    val loginActivity =
//                        Intent(activity?.applicationContext, LoginActivity::class.java)
//                    startActivity(loginActivity)
//                }
//            }
//            R.id.button_follow -> {
//                if (UserPreference.instance.isLogin) {
//                    callRemoveLikeApi()
//                } else {
//                    val loginActivity =
//                        Intent(activity?.applicationContext, LoginActivity::class.java)
//                    startActivity(loginActivity)
//                }
////                Toast.makeText(activity, "Follow", Toast.LENGTH_SHORT).show()
//
//            }
//            R.id.overview_dots -> {
//                val fragmentManager = activity?.supportFragmentManager
//                val bundle1 = Bundle()
//                bundle1.putSerializable(AppConstants.SELECTED_ITEM, videoDetail)
//                val fullDescriptionDialog = FullDescriptionDialogFragment.newInstance()
//                fullDescriptionDialog.arguments = bundle1
//                fragmentManager?.let {
//                    fullDescriptionDialog.show(it, AppConstants.TAG_FRAGMENT_ALERT)
//                }
//
//            }
//        }

    }

    private fun moreButton() {

//        Handler().postDelayed({
//
//            val lineCount = videoDetailsFragmentBinding.overview.lineCount
//            videoDetailsFragmentBinding.overview.maxLines = 3
//            if (lineCount > 3) {
//                videoDetailsFragmentBinding.overviewDots.visibility = View.VISIBLE
//            } else {
//                videoDetailsFragmentBinding.overviewDots.visibility = View.GONE
//            }
//        }, 200)

    }


    private fun callCheckWatchListApi() {
//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiIsToWatchList(activity!!,accessToken, id)
//                    .observe(this, Observer {
//                        if (it.isSuccessful) {
//                            isAddedToWatchList = true
//                            idFromAssetWatchlist = it.data?.assetId
//                            videoDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_added_playlist
//                                ), null, null, null
//                            )
//                            videoDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
//                            isAddedToWatchList = false
//                            videoDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_add_playlist
//                                ), null, null, null
//                            )
//                            videoDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.white
//                                )
//                            )
//
//                        }
//                    })
//            }
//        }

    }


    private fun callCheckLike() {
//
//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiIsLike(activity!!, accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it != null && it.isSuccessful == true) {
//                            isAddedToLike = true
//                            videoDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_liked
//                                ), null, null, null
//                            )
//                            videoDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
//                            isAddedToLike = false
//                            videoDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_like
//                                ), null, null, null
//                            )
//                            videoDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.white
//                                )
//                            )
//                        }
//                    })
//            }
//        }


    }

    private fun callRemoveLikeApi() {
//
//        if (isAddedToLike == true) {
//
//            accessToken?.let { accessToken ->
//                videoDetail?.id?.let { id ->
//                    VideoDetailRepository.instance.hitApiDeleteLike(activity!!, accessToken, id)
//                        .observe(this, Observer {
//
//                            if (it.isSuccessful) {
//                                isAddedToLike = false
//                                videoDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                    resources.getDrawable(
//                                        R.drawable.ic_like
//                                    ), null, null, null
//                                )
//                                videoDetailsFragmentBinding.buttonFollow.setTextColor(
//                                    resources.getColor(
//                                        R.color.white
//                                    )
//                                )
//                            } else {
//                                if (it.responseCode == 4302) {
//                                    val loginActivity =
//                                        Intent(context, LoginActivity::class.java)
//                                    context?.startActivity(loginActivity)
//                                    (context as FragmentActivity).finish()
//                                }
//                            }
//                        })
//                }
//            }
//
//        } else if (isAddedToLike == false) {
//            callAddLikeApi()
//        }

    }

    private fun callAddLikeApi() {
//
//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiAddLike(activity!!, accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it.isSuccessful) {
//                            isAddedToLike = true
//                            videoDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_liked
//                                ), null, null, null
//                            )
//                            videoDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
//                            if (it.responseCode == 4302) {
//                                val loginActivity =
//                                    Intent(context, LoginActivity::class.java)
//                                context?.startActivity(loginActivity)
//                                (context as FragmentActivity).finish()
//                            }
//                        }
//                    })
//            }
//        }

    }

    private fun callRemoveFromWatchListApi() {
//
//        if (isAddedToWatchList == true) {
//            accessToken?.let { accessToken ->
//                videoDetail?.id?.let { id ->
//                    VideoDetailRepository.instance.hitRemoveWatchlist(activity!!,accessToken, id)
//                        .observe(this, Observer {
//                            if (it.isSuccessful) {
//                                isAddedToWatchList = false
//                                videoDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
//                                    resources.getDrawable(R.drawable.ic_add_playlist),
//                                    null,
//                                    null,
//                                    null
//                                )
//                                videoDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                    resources.getColor(
//                                        R.color.white
//                                    )
//                                )
//                            }else {
//                                if (it.responseCode == 4302) {
//                                    val loginActivity =
//                                        Intent(context, LoginActivity::class.java)
//                                    context?.startActivity(loginActivity)
//                                    (context as FragmentActivity).finish()
//                                }
//                            }
//                        })
//                }
//            }
//
//
//        } else {
//            callAddToWatchListApi()
//        }
    }

    private fun callAddToWatchListApi() {

//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiAddToWatchList(activity!!,accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it.isSuccessful) {
//                            isAddedToWatchList = true
//                            // idFromAssetWatchlist = it.id
//                            videoDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(R.drawable.ic_added_playlist),
//                                null,
//                                null,
//                                null
//                            )
//                            videoDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        }else {
//                            if (it.responseCode == 4302) {
//                                val loginActivity =
//                                    Intent(context, LoginActivity::class.java)
//                                context?.startActivity(loginActivity)
//                                (context as FragmentActivity).finish()
//                            }
//                        }
//                    })
//            }
//        }

    }
}
