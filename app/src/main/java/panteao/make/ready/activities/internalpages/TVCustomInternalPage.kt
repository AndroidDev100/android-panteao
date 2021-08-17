package panteao.make.ready.activities.internalpages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProviders
import panteao.make.ready.R
import panteao.make.ready.activities.internalpages.fragments.InternalPlayListGridFragment
import panteao.make.ready.activities.internalpages.fragments.InternalPlayListGridTVFragment
import panteao.make.ready.activities.internalpages.fragments.InternalPlaylistListingFragment
import panteao.make.ready.activities.internalpages.fragments.InternalPlaylistListingTVFragment
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.OnKeywordSearchFragmentListener
import panteao.make.ready.databinding.ActivityCustomInternalPageBinding
import panteao.make.ready.databinding.ActivityTvCustomInternalPageBinding
import panteao.make.ready.fragments.dialog.AlertDialogFragment
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment
import panteao.make.ready.networking.apistatus.APIStatus
import panteao.make.ready.networking.responsehandler.ResponseModel
import panteao.make.ready.tvBaseModels.basemodels.TvBaseBindingActivity
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.RailInjectionHelper

class TVCustomInternalPage : TvBaseBindingActivity<ActivityTvCustomInternalPageBinding>(),
    AlertDialogFragment.AlertDialogListener, OnKeywordSearchFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoItem = intent.getParcelableExtra("asset") as EnveuVideoItemBean?
        val strings = videoItem?.posterURL?.split(AppConstants.FILTER_PLAYER_BANNER + "/")
        videoItem?.posterURL = strings?.get(1)
        binding.asset = videoItem
        if(videoItem?.description.isNullOrEmpty()){
            binding.description.visibility=View.GONE
        }else{
            binding.description.visibility=View.VISIBLE
        }
        getAssetDetails(videoItem!!.id)
    }

    private fun getAssetDetails(assestId: Int) {
        val railInjectionHelper = ViewModelProviders.of(this)[RailInjectionHelper::class.java]
        railInjectionHelper.getAssetDetailsV2(assestId.toString()).observe(this@TVCustomInternalPage,
            { assetResponse: ResponseModel<*>? ->
                if (assetResponse != null) {
                    if (assetResponse.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (assetResponse.status
                            .equals(APIStatus.SUCCESS.name, ignoreCase = true)
                    ) {
                        val enveuCommonResponse =
                            assetResponse.baseCategory as RailCommonData
                        val strings =
                            enveuCommonResponse.enveuVideoItemBeans[0].customLinkDetails.split(",")
                        if (strings.size > 1) {
                            binding.title.visibility = View.GONE
                            val internalPlaylistListingFragment =
                                InternalPlaylistListingTVFragment();

                            val bundle = Bundle()
                            bundle.putString(AppConstants.PLAYLIST_CONTENT, enveuCommonResponse.enveuVideoItemBeans[0].customLinkDetails)
                            internalPlaylistListingFragment.arguments = bundle

                            supportFragmentManager.beginTransaction()
                                .replace(
                                    binding.frameInteralPlaylists.id,
                                    internalPlaylistListingFragment,
                                    null
                                ).commit()
                            if (internalPlaylistListingFragment.view != null)
                                internalPlaylistListingFragment.view?.requestFocus()
                        } else {
                            val internalPlayListGridFragment =
                                InternalPlayListGridTVFragment()
                            val bundle = Bundle()
                            bundle.putString(AppConstants.PLAYLIST_CONTENT, strings[0])
                            internalPlayListGridFragment.arguments = bundle
                            binding.title.visibility = View.VISIBLE
                            binding.title.text = strings[0].split("|")[1].replace("\"", "").trim()
                            supportFragmentManager.beginTransaction()
                                .replace(
                                    binding.frameInteralPlaylists.id,
                                    internalPlayListGridFragment,
                                    null
                                ).commit()
                            if (internalPlayListGridFragment.view != null)
                                internalPlayListGridFragment.view?.requestFocus()
                        }
                    } else if (assetResponse.status
                            .equals(APIStatus.ERROR.name, ignoreCase = true)
                    ) {
                        if (assetResponse.errorModel != null && assetResponse.errorModel
                                .errorCode != 0
                        ) {
                            showDialog(
                                this.getResources().getString(R.string.error),
                                resources.getString(R.string.something_went_wrong)
                            )
                        }
                    } else if (assetResponse.status
                            .equals(APIStatus.FAILURE.name, ignoreCase = true)
                    ) {
                        showDialog(
                            this.getResources().getString(R.string.error),
                            resources.getString(R.string.something_went_wrong)
                        )
                    }
                }
            })
    }

    private fun showDialog(title: String, message: String) {
        val fm = supportFragmentManager
        val alertDialog = AlertDialogSingleButtonFragment.newInstance(
            title,
            message,
            resources.getString(R.string.ok)
        )
        alertDialog.isCancelable = false
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityTvCustomInternalPageBinding {
        return ActivityTvCustomInternalPageBinding.inflate(inflater)
    }

    override fun onFinishDialog() {

    }

    override fun showNoDataFoundView(show: Boolean, msg: String) {

    }

    override fun showProgressBarView(show: Boolean) {
    }

    override fun searchResultsFound(searchKeyword: String) {
    }

}