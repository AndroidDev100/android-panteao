package panteao.make.ready.activities.detailspage.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.activities.KalturaPlayerActivity
import panteao.make.ready.activities.detailspage.fragment.VODVideoDetailFragment
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.ActivityVideoDetailBinding
import panteao.make.ready.fragments.common.NoInternetFragment
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.cropImage.helpers.Logger
import panteao.make.ready.utils.helpers.NetworkConnectivity
import panteao.make.ready.utils.helpers.RailInjectionHelper


class VideoDetailActivity : TvBaseBindingActivity<ActivityVideoDetailBinding>(),
    NoInternetFragment.OnFragmentInteractionListener, View.OnClickListener {


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
        railInjectionHelper = RailInjectionHelper(this.application)
        id = intent.getSerializableExtra(AppConstants.SELECTED_ITEM) as Int
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

    private fun callDetailPageApi(id: Int?, contentType: String?) {
        val assetId = id
        railInjectionHelper.getAssetDetailsV2(assetId.toString()).observe(this, Observer {
            if (it != null && it.baseCategory != null) {
                val commonData = it.baseCategory as RailCommonData
                binding.contentsItem = commonData.enveuVideoItemBeans[0]
                binding.progressBar.visibility = View.GONE
                binding.buttonPlay.requestFocus()
                setClicks()
            }
        })

    }

    private fun setClicks() {

        binding.buttonPlay.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button_play -> {
                val playerIntent = Intent(this, KalturaPlayerActivity::class.java)
                playerIntent.putExtra("EntryId", binding.contentsItem?.getkEntryId())
                startActivity(playerIntent)
            }
        }
    }


}