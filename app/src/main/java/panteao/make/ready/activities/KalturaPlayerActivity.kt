package panteao.make.ready.activities

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.kaltura.playkit.PKLog
import com.kaltura.playkit.PlayerEvent
import com.kaltura.playkit.PlayerState
import com.kaltura.playkit.providers.ovp.OVPMediaAsset
import com.kaltura.tvplayer.KalturaOvpPlayer
import com.kaltura.tvplayer.KalturaPlayer
import com.kaltura.tvplayer.OVPMediaOptions
import com.kaltura.tvplayer.PlayerInitOptions
import kotlinx.android.synthetic.main.activity_kaltura_player.*
import panteao.make.ready.R

class KalturaPlayerActivity : FragmentActivity() {
    private val log = PKLog.get("MainActivity")
    private val START_POSITION = 0L // position for start playback in msec.
    private var player: KalturaPlayer? = null
    private var playerState: PlayerState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kaltura_player)
        loadPlaykitPlayer()
    }

    private fun addPlayerStateListener() {
        player!!.addListener(this, PlayerEvent.stateChanged) { event ->
            playerState = event.newState
        }

    }

    override fun onResume() {
        super.onResume()
        player?.let {
            it.onApplicationResumed()
            it.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.destroy();
    }

    override fun onPause() {
        super.onPause()
        player?.onApplicationPaused()
    }

    fun loadPlaykitPlayer() {

        val playerInitOptions = PlayerInitOptions(PARTNER_ID)
        playerInitOptions.setAutoPlay(true)

        player = KalturaOvpPlayer.create(this, playerInitOptions)

        player?.setPlayerView(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val container = player_root
        container.addView(player?.playerView)

        val ovpMediaOptions = buildOvpMediaOptions()
        player?.loadMedia(ovpMediaOptions) { entry, loadError ->
            if (loadError != null) {
                Toast.makeText(this, loadError.message, Toast.LENGTH_LONG).show()
            } else {
                log.d("OVPMedia onEntryLoadComplete  entry = " + entry.id)
            }
        }

        addPlayerStateListener()
    }

    private fun buildOvpMediaOptions(): OVPMediaOptions {
        val ovpMediaAsset = OVPMediaAsset()
        ovpMediaAsset.entryId = intent.getStringExtra("EntryId")
        ovpMediaAsset.ks = null
        ovpMediaAsset.redirectFromEntryId = true
        val ovpMediaOptions = OVPMediaOptions(ovpMediaAsset)
        ovpMediaOptions.startPosition = START_POSITION

        return ovpMediaOptions
    }

    companion object {
        val PARTNER_ID = 802792
        val SERVER_URL = "https://cdnapisec.kaltura.com"
        val ENTRY_ID = "1_7pg14mbg"
    }
}