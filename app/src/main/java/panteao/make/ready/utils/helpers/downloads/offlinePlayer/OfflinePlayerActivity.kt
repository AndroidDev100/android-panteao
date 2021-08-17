package panteao.make.ready.utils.helpers.downloads.offlinePlayer

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import panteao.make.ready.R
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.databinding.OfflinePlayerActivityBinding


class OfflinePlayerActivity : BaseBindingActivity<OfflinePlayerActivityBinding>() {
    private var fragment:OfflinePlayerFragment?=null
    private var entryId:String?=""
    override fun inflateBindingLayout(inflater: LayoutInflater): OfflinePlayerActivityBinding {
        return OfflinePlayerActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.w("optionss","oncreate")
        if (savedInstanceState==null){
            entryId=intent.getStringExtra("entry_id")
            addPlayerFragment()
        }

    }

    private fun addPlayerFragment() {
        var manager = supportFragmentManager
        fragment = manager.findFragmentById(R.id.player_fragment) as OfflinePlayerFragment?
        if (fragment!==null){
            Log.w("optionss",fragment.toString())
        }else{
            Log.w("optionss","null")
        }

        fragment?.setEntryId(entryId)
        fragment?.initPlayer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            val params2 = binding.playerLayout.layoutParams
            params2.width = ViewGroup.LayoutParams.MATCH_PARENT
            params2.height = ViewGroup.LayoutParams.MATCH_PARENT
            binding.playerLayout.requestLayout()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (android.provider.Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment!=null){
            fragment?.destroyPlayer()
        }
    }
}