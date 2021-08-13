package panteao.make.ready.utils.helpers.downloads.offlinePlayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.PowerManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kaltura.android.exoplayer2.ui.TimeBar
import com.kaltura.android.exoplayer2.ui.TimeBar.OnScrubListener
import com.kaltura.playkit.PKEvent
import com.kaltura.playkit.PlayerEvent
import com.kaltura.tvplayer.KalturaBasicPlayer
import com.kaltura.tvplayer.OfflineManager
import com.kaltura.tvplayer.PlayerInitOptions
import kotlinx.android.synthetic.main.offline_player_fragment.*
import panteao.make.ready.R
import panteao.make.ready.SDKConfig
import panteao.make.ready.baseModels.BaseBindingFragment
import panteao.make.ready.callbacks.commonCallbacks.PhoneListenerCallBack
import panteao.make.ready.databinding.OfflinePlayerFragmentBinding
import panteao.make.ready.utils.helpers.PhoneStateListenerHelper
import java.util.*


class OfflinePlayerFragment : BaseBindingFragment<OfflinePlayerFragmentBinding>() ,
    PhoneListenerCallBack {
    var entryID:String?=""
    var player: KalturaBasicPlayer?=null
    var powerManager:PowerManager?=null
    var wakeLock:PowerManager.WakeLock?=null
    override fun inflateBindingLayout(inflater: LayoutInflater): OfflinePlayerFragmentBinding {
        return OfflinePlayerFragmentBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wakeScreenOn();
    }

    private fun wakeScreenOn() {
        powerManager = requireActivity()!!.getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager?.newWakeLock(
            PowerManager.FULL_WAKE_LOCK,
            "OfflinePlayerFragment::WakelockTag"
        )
    }


    fun setEntryId(entryID: String?) {
        this.entryID=entryID
    }

    fun initPlayer() {
        uiHandling();
        var options=PlayerInitOptions(SDKConfig.PARTNER_ID).setAutoPlay(true).setAllowCrossProtocolEnabled(true)
        Log.w("optionss",options.toString())
        player=KalturaBasicPlayer.create(requireActivity(),options)
        var offlineManger=OfflineManager.getInstance(requireActivity())

       var pkMediaEntry =  offlineManger.getLocalPlaybackEntry(entryID!!)
        Log.w("optionss",pkMediaEntry.toString())
        player?.setMedia(pkMediaEntry)

        player?.setPlayerView(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        binding.rl.addView(player?.playerView)
        playerListeners(player)

    }

    private var playDrawable: Drawable? = null
    private var pauseDrawable: Drawable? = null
    private fun uiHandling() {
        playDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_play_arrow_24)
        pauseDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_pause_24)

    }

    private fun playerListeners(player: KalturaBasicPlayer?) {
        player!!.addListener(this,PlayerEvent.playing){
            event:PKEvent?->
            updatePlayPauseButton(true)
        }

        binding.playButton.setOnClickListener(View.OnClickListener {
            togglePlayPause()
        })

        player!!.addListener(this,PlayerEvent.playheadUpdated){
                event:PKEvent?->
            if (player!==null){
                binding.seekBar.setDuration(player!!.duration)
                binding.seekBar.setPosition(player!!.currentPosition)
                binding.currentTime.setText(stringForTime(player!!.currentPosition))
                binding.totalDuration.setText(stringForTime(player!!.duration))
            }
        }

        binding.cancel.setOnClickListener(View.OnClickListener {
            requireActivity().onBackPressed()
        })

        binding.backward.setOnClickListener(View.OnClickListener {
          backwardFunction()
        })

        binding.forward.setOnClickListener(View.OnClickListener {
            forwardFunction()
        })

        binding.quality.visibility = View.INVISIBLE
        seekBarListener(player)

    }

    private fun forwardFunction() {
        if (player != null) {
            player!!.seekTo(player!!.currentPosition + 10000)
        }
    }

    private fun backwardFunction() {
        if (player != null) {
            player!!.seekTo(player!!.currentPosition - 10000)
        }
    }

    private fun seekBarListener(player: KalturaBasicPlayer) {
        binding.seekBar.addListener(object : OnScrubListener {
            override fun onScrubStart(timeBar: TimeBar, position: Long) {

            }

            override fun onScrubMove(timeBar: TimeBar, position: Long) {
                binding.seekBar.setPosition(position)
                binding.currentTime.setText(stringForTime(position))
            }

            override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
                    binding.seekBar.setPosition(position)
                    player!!.seekTo(position)
            }
        })

    }

    private fun togglePlayPause() {
        val playing = player!!.isPlaying
        if (playing) {
            player!!.pause()
        } else {
            player!!.play()
        }
        updatePlayPauseButton(!playing)
    }


    private fun updatePlayPauseButton(isPlaying: Boolean) {
        val next: Drawable

        if (isPlaying) {
            binding.playButton.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_pause_24))
        } else {
            binding.playButton.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24))
        }

        //binding.playButton.setImageDrawable(next)
    }


    fun destroyPlayer() {
        if (player!=null){
            player?.removeListener { null }
            player?.stop()
            player?.destroy()
        }
    }

    fun stringForTime(timeMs: Long): String? {
        val formatBuilder = StringBuilder()
        val formatter = Formatter(formatBuilder, Locale.getDefault())
        val totalSeconds = (timeMs + 500) / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        formatBuilder.setLength(0)
        return if (hours > 0) formatter.format("%d:%02d:%02d", hours, minutes, seconds)
            .toString() else formatter.format("%02d:%02d", minutes, seconds).toString()
    }

    private val headsetRecicer: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == Intent.ACTION_HEADSET_PLUG) {
                val headsetState = intent.extras!!.getInt("state")
                if (headsetState == 0) {
                    if (player !== null) {
                        player!!.pause()
                        binding.playButton.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24))
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            val receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
            requireActivity()!!.registerReceiver(headsetRecicer, receiverFilter)
        } catch (ignored: Exception) {
        }
    }

    override fun onStop() {
        super.onStop()
        if (powerManager!==null && wakeLock!==null){
            if (wakeLock!!.isHeld()){
                wakeLock!!.release()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (player!=null){
            player?.pause()
            binding.playButton.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24))
        }
    }

    override fun onCallStateRinging() {
        if(player!==null){
            player!!.pause()
            binding.playButton.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_baseline_play_arrow_24))
        }
    }

    override fun onCallStateIdle(state: Int) {

    }

    override fun onStart() {
        super.onStart()
        try {
            if (wakeLock != null) {
                wakeLock!!.acquire()
            }
            val mgr = requireActivity()!!.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            mgr?.listen(
                PhoneStateListenerHelper.getInstance(requireActivity()),
                PhoneStateListener.LISTEN_CALL_STATE
            )
        } catch (ignored: java.lang.Exception) {
        }
    }

}