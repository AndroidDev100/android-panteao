package panteao.make.ready.activities.detailspage.fragment


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import panteao.make.ready.R
import panteao.make.ready.utils.helpers.database.preferences.UserPreference
import panteao.make.ready.activities.detailspage.listeners.SeriesDetailsListener
import panteao.make.ready.activities.detailspage.repository.VideoDetailRepository
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.SeriesDetailsFragmentBinding
import panteao.make.ready.utils.constants.AppConstants


class SeriesDetailFragment : Fragment(), View.OnClickListener {

    private lateinit var seasonDetailsFragment: LatestSeasonsDetailsFragment
    val TAG = this.javaClass.name

    private lateinit var seriesDetailsFragmentBinding: SeriesDetailsFragmentBinding
    private var bundle: Bundle? = null
    private var isAddedToWatchList: Boolean? = false
    private var isAddedToLike: Boolean? = false
    private var idFromAssetWatchlist: Int? = null
    private var videoDetail: EnveuVideoItemBean? = null
    private var mListener: SeriesDetailsListener? = null
    private var accessToken: String? = null


    companion object {
        fun newInstance(): SeriesDetailFragment {
            return SeriesDetailFragment()
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as SeriesDetailsListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity().supportFragmentManager.findFragmentByTag(AppConstants.SEASON_DETAIL) == null) {
            seasonDetailsFragment =
                LatestSeasonsDetailsFragment();
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.persist,
                    seasonDetailsFragment,
                    AppConstants.SEASON_DETAIL
                ).commit()
            if (seasonDetailsFragment.view != null)
                seasonDetailsFragment.view?.requestFocus()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        seriesDetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.series_details_fragment, container, false)
        bundle = this.arguments
        return seriesDetailsFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesDetailsFragmentBinding.buttonPlay.requestFocus()

        videoDetail =
            bundle?.getSerializable(AppConstants.SELECTED_SERIES) as EnveuVideoItemBean
        seriesDetailsFragmentBinding.contentsItem = videoDetail
        if (videoDetail?.assetCast == null || videoDetail?.assetGenres == null) {
            seriesDetailsFragmentBinding.lnCastHeadline.visibility = View.GONE
            seriesDetailsFragmentBinding.lnDirectorHeadline.visibility = View.GONE
        } else {
            seriesDetailsFragmentBinding.lnCastHeadline.visibility = View.VISIBLE
            seriesDetailsFragmentBinding.lnDirectorHeadline.visibility = View.VISIBLE
        }

        seriesDetailsFragmentBinding.buttonPlay.setOnClickListener(this)
        seriesDetailsFragmentBinding.buttonWatchlist.setOnClickListener(this)
        seriesDetailsFragmentBinding.buttonFollow.setOnClickListener(this)
        seriesDetailsFragmentBinding.moreInfo.setOnClickListener(this)
        seriesDetailsFragmentBinding.buttonEpisodes.setOnClickListener(this)

        accessToken = UserPreference.instance.userAuthToken
        if (UserPreference.instance.isLogin) {
            callCheckLike()
        }
    }

    override fun onPause() {
        super.onPause()
        view?.isFocusableInTouchMode = false
        view?.isFocusable = false

    }

    override fun onResume() {
        seriesDetailsFragmentBinding.buttonPlay.requestFocus()
        super.onResume()
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_play -> {
                val verticalEpisodeFragment =
                    fragmentManager?.findFragmentByTag("VerticalEpisodeFragment") as EpisodesFragment?
                if (verticalEpisodeFragment != null && verticalEpisodeFragment.isAdded) {
                    verticalEpisodeFragment.playFirstEpisode();
                }
            }
            R.id.button_watchlist -> {

            }
            R.id.button_follow -> {
//                if (UserPreference.instance.isLogin) {
//                    callRemoveLikeApi()
//                } else {
//                    val loginActivity =
//                        Intent(activity?.applicationContext, LoginActivity::class.java)
//                    startActivity(loginActivity)
//                }

            }
            R.id.more_info -> {
//                val fragmentManager = activity?.supportFragmentManager
//                val bundle1 = Bundle()
//                bundle1.putSerializable(AppConstants.SELECTED_ITEM, videoDetail)
//                val fullDescriptionDialog = FullDescriptionDialogFragment.newInstance()
//                fullDescriptionDialog.arguments = bundle1
//                fragmentManager?.let {
//                    fullDescriptionDialog.show(it, AppConstants.TAG_FRAGMENT_ALERT)
//                }
            }

            R.id.button_episodes -> {
                showEpisodeAndMoreFragment()

            }
        }

    }

    private fun showEpisodeAndMoreFragment() {
        mListener?.onEpisodeClicked()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    private fun callCheckWatchListApi() {
//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiIsToWatchList(requireActivity(), accessToken, id)
//                    .observe(this, Observer {
//                        if (it.isSuccessful) {
//                            isAddedToWatchList = true
//                            idFromAssetWatchlist = it.data?.assetId
////                            seriesDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_added_playlist
////                                ), null, null, null
////                            )
//                            seriesDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
////                            seriesDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_add_playlist
////                                ), null, null, null
////                            )
//                            seriesDetailsFragmentBinding.buttonWatchlist.setTextColor(
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

        accessToken?.let { accessToken ->
            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiIsLike(requireActivity(), accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it != null && it.isSuccessful == true) {
//                            isAddedToLike = true
//                            seriesDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_liked
//                                ), null, null, null
//                            )
//                            seriesDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
//                            isAddedToLike = false
//                            seriesDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_like
//                                ), null, null, null
//                            )
//                            seriesDetailsFragmentBinding.buttonFollow.setTextColor(
//                                resources.getColor(
//                                    R.color.white
//                                )
//                            )
//                        }
//                    })
            }
        }


    }

    private fun callRemoveLikeApi() {

        if (isAddedToLike == true) {

            accessToken?.let { accessToken ->
                videoDetail?.id?.let { id ->
//                    VideoDetailRepository.instance.hitApiDeleteLike(requireActivity(), accessToken, id)
//                        .observe(this, Observer {
//
//                            if (it.isSuccessful) {
//                                isAddedToLike = false
//                                seriesDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                    resources.getDrawable(
//                                        R.drawable.ic_like
//                                    ), null, null, null
//                                )
//                                seriesDetailsFragmentBinding.buttonFollow.setTextColor(
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
                }
            }

        } else if (isAddedToLike == false) {
            callAddLikeApi()
        }

    }

    private fun callAddLikeApi() {

        accessToken?.let { accessToken ->
            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiAddLike(requireActivity(), accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it.isSuccessful) {
//                            isAddedToLike = true
//                            seriesDetailsFragmentBinding.buttonFollow.setCompoundDrawablesWithIntrinsicBounds(
//                                resources.getDrawable(
//                                    R.drawable.ic_liked
//                                ), null, null, null
//                            )
//                            seriesDetailsFragmentBinding.buttonFollow.setTextColor(
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
            }
        }

    }

    private fun callRemoveFromWatchListApi() {

        if (isAddedToWatchList == true) {
            accessToken?.let { accessToken ->
                videoDetail?.id?.let { id ->
//                    VideoDetailRepository.instance.hitRemoveWatchlist(requireActivity(), accessToken, id)
//                        .observe(this, Observer {
//                            if (it.isSuccessful) {
//                                isAddedToWatchList = false
//                                seriesDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
//                                    resources.getDrawable(R.drawable.ic_add_playlist),
//                                    null,
//                                    null,
//                                    null
//                                )
//                                seriesDetailsFragmentBinding.buttonWatchlist.setTextColor(
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
                }
            }


        } else {
            callAddToWatchListApi()
        }
    }

    private fun callAddToWatchListApi() {

//        accessToken?.let { accessToken ->
//            videoDetail?.id?.let { id ->
//                VideoDetailRepository.instance.hitApiAddToWatchList(requireActivity(), accessToken, id)
//                    .observe(this, Observer {
//
//                        if (it.isSuccessful) {
//                            isAddedToWatchList = true
//                            //                    idFromAssetWatchlist = it.id
////                            seriesDetailsFragmentBinding.buttonWatchlist.setCompoundDrawablesWithIntrinsicBounds(
////                                resources.getDrawable(
////                                    R.drawable.ic_added_playlist
////                                ), null, null, null
////                            )
//                            seriesDetailsFragmentBinding.buttonWatchlist.setTextColor(
//                                resources.getColor(
//                                    R.color.dialog_green_color
//                                )
//                            )
//                        } else {
////                            if (it.responseCode == 4302) {
////                                val loginActivity =
////                                    Intent(context, LoginActivity::class.java)
////                                context?.startActivity(loginActivity)
////                                (context as FragmentActivity).finish()
////                            }
//                        }
//                    })
//            }
//        }

    }

}
