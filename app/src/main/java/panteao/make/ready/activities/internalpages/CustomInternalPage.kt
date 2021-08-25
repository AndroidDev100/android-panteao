package panteao.make.ready.activities.internalpages

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProviders
import panteao.make.ready.R
import panteao.make.ready.activities.internalpages.fragments.InternalPlayListGridFragment
import panteao.make.ready.activities.internalpages.fragments.InternalPlaylistListingFragment
import panteao.make.ready.baseModels.BaseBindingActivity
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.callbacks.commonCallbacks.OnKeywordSearchFragmentListener
import panteao.make.ready.databinding.ActivityCustomInternalPageBinding
import panteao.make.ready.fragments.dialog.AlertDialogFragment
import panteao.make.ready.fragments.dialog.AlertDialogSingleButtonFragment
import panteao.make.ready.networking.apistatus.APIStatus
import panteao.make.ready.networking.responsehandler.ResponseModel
import panteao.make.ready.utils.constants.AppConstants
import panteao.make.ready.utils.helpers.RailInjectionHelper

class CustomInternalPage : BaseBindingActivity<ActivityCustomInternalPageBinding>(),
    AlertDialogFragment.AlertDialogListener, OnKeywordSearchFragmentListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoItem = intent.getParcelableExtra("asset") as EnveuVideoItemBean?
        val assetId = intent.getIntExtra("asset_id", 0)
        binding.asset = videoItem
        if (videoItem?.description.isNullOrEmpty()) {
            binding.description.visibility = View.GONE
            binding.lessButton.visibility = View.GONE
        } else {
            binding.description.visibility = View.VISIBLE
            binding.lessButton.visibility = View.VISIBLE
            binding.description.setEllipsis("...")
            binding.description.setEllipsize(TextUtils.TruncateAt.END)
        }
        if (binding!==null){
            binding.backButton.setOnClickListener(View.OnClickListener {
                onBackPressed()
            })

            binding.lessButton.setOnClickListener(View.OnClickListener {
                expendable()
            })
        }
        if (assetId>0){
            getAssetDetails(assetId)
        }else{
            getAssetDetails(videoItem!!.id)
        }

    }

    private fun expendable() {
        binding.description.toggle()
        binding.description.setEllipsis("...")
        if (binding.description.isExpanded()) {
            binding.description.setEllipsize(null)
        } else {
            binding.description.setEllipsize(TextUtils.TruncateAt.END)
        }

        if (binding.description.isExpanded()) {
            binding.textExpandable.text = (resources.getString(R.string.less))
        } else {
            binding.textExpandable.text = (resources.getString(R.string.more))
        }
    }

    private fun getAssetDetails(assestId: Int) {
        val railInjectionHelper = ViewModelProviders.of(this)[RailInjectionHelper::class.java]
        railInjectionHelper.getAssetDetailsV2(assestId.toString()).observe(this@CustomInternalPage,
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
                                InternalPlaylistListingFragment();

                            val bundle = Bundle()
                            bundle.putString(
                                AppConstants.PLAYLIST_CONTENT,
                                enveuCommonResponse.enveuVideoItemBeans[0].customLinkDetails
                            )
                            internalPlaylistListingFragment.arguments = bundle

                            supportFragmentManager.beginTransaction()
                                .replace(
                                    binding.frameInteralPlaylists.id,
                                    internalPlaylistListingFragment,
                                    null
                                ).commit()
                        } else {
                            val internalPlayListGridFragment =
                                InternalPlayListGridFragment()
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

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityCustomInternalPageBinding {
        return ActivityCustomInternalPageBinding.inflate(inflater)
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