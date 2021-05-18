package panteao.make.ready.cardlayout.cardpresenter

import android.animation.LayoutTransition
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.Presenter
import panteao.make.ready.R
import panteao.make.ready.cardlayout.cardview.PotraitCardView


/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
class PotraitCardPresenter(var contentType: Int, activity: FragmentActivity?) : Presenter() {


    private var mDefaultCardImage: Drawable? = null
    private var fragmentActivity: FragmentActivity? = null
    private var brightcoveId = "6081939248001"

    init {
        fragmentActivity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {

        sDefaultBackgroundDrawable =
            parent.resources.getDrawable(R.drawable.button_default_background)
        sSelectedBackgroundDrawable = parent.resources.getDrawable(R.drawable.button_background)

        mDefaultCardImage =
            ContextCompat.getDrawable(parent.context, R.drawable.placeholder_landscape)
        val textView = TextView(parent.context)
        val cardView = object : PotraitCardView(parent.context, contentType) {
            override fun setSelected(selected: Boolean) {
                if (selected)
                    this.background = sSelectedBackgroundDrawable
                else
                    this.background = sDefaultBackgroundDrawable

                super.setSelected(selected)
            }
        }
        view = cardView
        textView.setTextColor(Color.WHITE)

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        /*updateCardBackgroundColor(
            cardView,
            false
        )*/
        return ViewHolder(cardView)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        if (item is panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean) {
            val cardView = viewHolder.view as PotraitCardView
            brightcoveId = item.brightcoveVideoId!!
            cardView.setRailCommonDataModel(item)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as PotraitCardView
        // Remove references to images so that the garbage collector can free up memory

    }

    fun setBrightcoveId(brightcoveId: String) {
        this.brightcoveId = brightcoveId
    }

    fun stopPlayer(
        view: PotraitCardView
    ) {
        var card = view.findViewById<ConstraintLayout>(R.id.main_lay)
        var image = card.getViewById(R.id.imgViewCard)
        //  var fragmentTransaction = context?.supportFragmentManager?.beginTransaction()
//        card.layoutTransition = transition
        var playerFragment = view.findViewById<FrameLayout>(R.id.player_fragme)

//        brightcovePlayerFragment?.stopPlayback()
        //  fragmentTransaction = null
//        brightcovePlayerFragment = null
        image.visibility = View.VISIBLE
        //  image1.visibility = View.GONE
        playerFragment.removeAllViews()
        playerFragment?.visibility = View.GONE
    }

    fun startPlayer(
        view: PotraitCardView,
        context: FragmentActivity?,
        brightcoveId: String
    ) {
        var card = view.findViewById<ConstraintLayout>(R.id.main_lay)
        var image = card.getViewById(R.id.imgViewCard)
        var playerFragment = view.findViewById<FrameLayout>(R.id.player_fragme)
//        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null)
//        transition.setAnimator(LayoutTransition.APPEARING, null)
//        transition.setDuration(300)
//        card.layoutTransition = transition
        image?.visibility = View.INVISIBLE
        playerFragment.visibility = View.VISIBLE
//        brightcovePlayerFragment = BrightcoveExoPlayerVideoView(context)
        // PictureInPictureManager.getInstance().registerActivity(mActivity, baseVideoView);
//        brightcovePlayerFragment!!.mediaController = null
//        brightcovePlayerFragment?.layoutParams?.width =
//            ViewGroup.LayoutParams.MATCH_PARENT
//        playerFragment.addView(brightcovePlayerFragment)
//        brightcovePlayerFragment?.finishInitialization()
//        var eventEmitter = EventEmitterImpl()
//        eventEmitter.on(EventType.ENTER_FULL_SCREEN) {
//            //You can set listeners on each Video View
//        }
//        var catalog = Catalog(eventEmitter, ACCOUNT_ID, POLICY_KEY)
//        catalog.findVideoByID(brightcoveId, object : VideoListener() {
//            // Add the video found to the queue with add().
//            // Start playback of the video with start().
//            override fun onVideo(video: Video) {
//                //Log.e("player_player", video.toString() + "")
//                brightcovePlayerFragment?.clear()
//                brightcovePlayerFragment?.add(video)
//                brightcovePlayerFragment?.start()
//            }
//
//            override fun onError(errors: MutableList<CatalogError>) {
//                //Log.e("Error", Gson().toJson(errors));
//                super.onError(errors)
//
//            }
//        })
    }

    companion object {
        private val TAG = "CardPresenter"
        private var sSelectedBackgroundDrawable: Drawable? = null
        private var sDefaultBackgroundDrawable: Drawable? = null
        private var handler: android.os.Handler? = null
        private var handler1: Handler? = null
        var transition = LayoutTransition()
        private val ACCOUNT_ID = "5854923532001"
        private val POLICY_KEY =
            "BCpkADawqM1eQgw1AYFQOUXoNSPw_rzWhyBPlA-s-FXA5HLM5PxfY7B5JA-fgES-HUj3a4WafkLDIiQYmRZMrpe_oOfuY_KGj1EGRz9e-v7a8LI6sKhZmd3s3CAY1VlLNP9eCQz1OAMHAfUi"
//        var brightcovePlayerFragment: BrightcoveExoPlayerVideoView? = null
        private lateinit var view: PotraitCardView

    }
}
