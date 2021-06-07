package panteao.make.ready.activities.detailspage.fragment


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import panteao.make.ready.R
import panteao.make.ready.activities.detailspage.repository.VideoDetailRepository
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.EpisodeDetailsFragmentBinding
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.database.preferences.UserPreference


class EpisodeDetailFragment : Fragment(), View.OnClickListener {


    val TAG = this.javaClass.name

    private lateinit var episodeDetailsFragmentBinding: EpisodeDetailsFragmentBinding
    private var bundle: Bundle? = null
    private var isAddedToWatchList: Boolean? = false
    private var isAddedToLike: Boolean? = false
    private var idFromAssetWatchlist: Int? = null
    private var idFromAssetLike: Int? = null
    private var endPoint: String? = null
    private var isLogin: Boolean? = false
    private var isEntitled: Boolean? = false
    private var videoDetail: EnveuVideoItemBean? = null
    private var accessToken: String? = null


    companion object {
        fun newInstance(): EpisodeDetailFragment {
            return EpisodeDetailFragment()
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
        episodeDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.episode_details_fragment, container, false)
        episodeDetailsFragmentBinding.buttonPlay.requestFocus()
        bundle = this.arguments
        return episodeDetailsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoDetail =
            bundle?.getSerializable(AppConstants.EPISODE_DETAIL) as EnveuVideoItemBean
        moreButton()
        episodeDetailsFragmentBinding.contentsItem = videoDetail
        episodeDetailsFragmentBinding.buttonPlay.setOnClickListener(this)
        episodeDetailsFragmentBinding.buttonWatchlist.setOnClickListener(this)
        episodeDetailsFragmentBinding.buttonFollow.setOnClickListener(this)
        episodeDetailsFragmentBinding.buttonEpisodes.setOnClickListener(this)

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
        episodeDetailsFragmentBinding.buttonPlay.requestFocus()
        super.onResume()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_play -> {
//                AppCommonMethod.loadPlayer(videoDetail, requireActivity(), ArrayList(), 0)
            }
            R.id.button_watchlist -> {
                if (UserPreference.instance.isLogin) {
                    callRemoveFromWatchListApi()
                } else {
//                    val loginActivity =
//                        Intent(activity?.applicationContext, LoginActivity::class.java)
//                    startActivity(loginActivity)
                }

            }
            R.id.button_follow -> {
                if (UserPreference.instance.isLogin) {
                    callRemoveLikeApi()
                } else {
//                    val loginActivity =
//                        Intent(activity?.applicationContext, LoginActivity::class.java)
//                    startActivity(loginActivity)
                }
//                Toast.makeText(activity, "Follow", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun moreButton() {

        Handler().postDelayed({

            val lineCount = episodeDetailsFragmentBinding.overview.lineCount
            episodeDetailsFragmentBinding.overview.maxLines = 3
        }, 200)

    }


    fun setFocus(focus: Boolean) {
        if (focus) {
            episodeDetailsFragmentBinding.buttonFollow.requestFocus()
        }
    }

    private fun callCheckWatchListApi() {
//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiIsToWatchList(requireActivity(),accessToken, id)
//                    .observe(requireActivity(), Observer {
//                        if (it.isSuccessful) {
//                            isAddedToWatchList = true
//                            idFromAssetWatchlist = it.data?.assetId
////                            episodeDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.add_to_watchlist_navy_blue
////                                ), null, null, null
////                            )
//                            episodeDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
////                            episodeDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_add_playlist
////                                ), null, null, null
////                            )
//                            episodeDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.white
//                                )
//                            )
//                            isAddedToWatchList = false
//                        }
//                    })
//            }
//        }

    }


    private fun callCheckLike() {

//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiIsLike(requireActivity(), accessToken, id)
//                    .observe(requireActivity(), Observer {
//                        if (it.isSuccessful) {
//                            isAddedToLike = true
////                            episodeDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_liked
////                                ), null, null, null
////                            )
//                            episodeDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
//                            isAddedToLike = false
////                            episodeDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_like
////                                ), null, null, null
////                            )
//                            episodeDetailsFragmentBinding.buttonFollow.setTextColor(
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

//        if (isAddedToLike == true) {
//
//            accessToken?.let { accessToken ->
//                videoDetail?.id?.let { id ->
//                    VideoDetailRepository.instance.hitApiDeleteLike(requireActivity(), accessToken, id)
//                        .observe(this, Observer {
//                            if (it.isSuccessful) {
//                                isAddedToLike = false
////                                episodeDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
////                                    resources.getDrawable(
////                                        R.drawable.ic_like
////                                    ), null, null, null
////                                )
//                                episodeDetailsFragmentBinding.buttonFollow.setTextColor(
//                                    resources.getColor(
//                                        R.color.white
//                                    )
//                                )
//                            } else {
//                                if (it.responseCode == 4302) {
////                                    val loginActivity =
////                                        Intent(context, LoginActivity::class.java)
////                                    context?.startActivity(loginActivity)
////                                    (context as FragmentActivity).finish()
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

//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiAddLike(requireActivity(), accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it.isSuccessful) {
//                            isAddedToLike = true
////                            episodeDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_liked
////                                ), null, null, null
////                            )
//                            episodeDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
//                                if (it.responseCode == 4302) {
////                                    val loginActivity =
////                                        Intent(context, LoginActivity::class.java)
////                                    context?.startActivity(loginActivity)
////                                    (context as FragmentActivity).finish()
//                                }
//                            }
//                    })
//            }
//        }

    }

    private fun callRemoveFromWatchListApi() {

//        if (isAddedToWatchList == true) {
//            accessToken?.let { accessToken ->
//                videoDetail?.id?.let { id ->
//                    VideoDetailRepository.instance.hitRemoveWatchlist(requireActivity(),accessToken, id)
//                        .observe(this, Observer {
//                            if (it.isSuccessful) {
//                                isAddedToWatchList = false
////                                episodeDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                    resources.getDrawable(R.drawable.ic_add_playlist),
////                                    null,
////                                    null,
////                                    null
////                                )
//                                episodeDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                    resources.getColor(
//                                        R.color.white
//                                    )
//                                )
//                            }else {
//                                if (it.responseCode == 4302) {
////                                    val loginActivity =
////                                        Intent(context, LoginActivity::class.java)
////                                    context?.startActivity(loginActivity)
////                                    (context as FragmentActivity).finish()
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
//                VideoDetailRepository.instance.hitApiAddToWatchList(requireActivity(),accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it.isSuccessful) {
//                            isAddedToWatchList = true
//                            //                    idFromAssetWatchlist = it.id
////                            episodeDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_added_playlist
////                                ), null, null, null
////                            )
//                            episodeDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        }else {
//                                if (it.responseCode == 4302) {
////                                    val loginActivity =
////                                        Intent(context, LoginActivity::class.java)
////                                    context?.startActivity(loginActivity)
////                                    (context as FragmentActivity).finish()
//                                }
//                            }
//                    })
//            }
//        }

    }

}
