package panteao.make.ready.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.kaltura.playkit.PKLog
import com.kaltura.playkit.PlayerState
import com.kaltura.tvplayer.KalturaPlayer
import panteao.make.ready.R
import panteao.make.ready.SDKConfig
import panteao.make.ready.player.kalturaPlayer.KalturaFragment
import panteao.make.ready.utils.constants.AppConstants

class KalturaPlayerActivity : FragmentActivity(), KalturaFragment.OnPlayerInteractionListener {
    private val log = PKLog.get("MainActivity")
    private val START_POSITION = 0L // position for start playback in msec.
    private var player: KalturaPlayer? = null
    private var playerState: PlayerState? = null
    var fromBingWatch = false
    private lateinit var playerFragment: KalturaFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kaltura_player)
        val args = Bundle()
        args.putString(AppConstants.ENTRY_ID, intent.getStringExtra("EntryId"))
        args.putBoolean("binge_watch", SDKConfig.getInstance().bingeWatchingEnabled)
        args.putInt("binge_watch_timer", SDKConfig.getInstance().timer)
        args.putBoolean("from_binge", fromBingWatch)
        val transaction = supportFragmentManager.beginTransaction()
        playerFragment = KalturaFragment()
        playerFragment.arguments = args
        transaction.replace(R.id.player_root, playerFragment)
        transaction.addToBackStack(null)
        transaction.commit()
//        loadPlaykitPlayer()
    }

    companion object {
        val PARTNER_ID = 802792
        val SERVER_URL = "https://cdnapisec.kaltura.com"
        val ENTRY_ID = "1_7pg14mbg"
    }

    override fun onPlayerStart() {

    }

    override fun onBookmarkCall() {

    }

    override fun onBookmarkFinish() {

    }

    override fun onCurrentPosition() {
        TODO("Not yet implemented")
    }

    override fun bingeWatchCall() {
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                playerFragment.onKeyDown(KeyEvent.KEYCODE_BACK)
                return true
            } else {
                playerFragment.onKeyDown(event.keyCode)
            }
        }
        return super.dispatchKeyEvent(event)
    }
}