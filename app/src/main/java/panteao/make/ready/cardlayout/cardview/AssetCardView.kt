package panteao.make.ready.cardlayout.cardview


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.CustomAssetCardViewBinding
import panteao.make.ready.enums.KalturaImageType
import panteao.make.ready.utils.config.ImageLayer
import panteao.make.ready.utils.helpers.ImageHelper
import java.util.*


@SuppressLint("ViewConstructor")
open class AssetCardView : BaseCardView {

    val TAG = "AssetCardView"

    private var customMenuCardViewBinding: CustomAssetCardViewBinding? = null
    private var mAttachedToWindow: Boolean = false
    private var mContentType: Int = -1


    constructor(context: Context, contentType: Int) : super(context) {
        this.mContentType = contentType
        buildImageCardView()
    }

    private fun buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true

        val inflater = LayoutInflater.from(context)
        customMenuCardViewBinding =
                DataBindingUtil.inflate(inflater, R.layout.custom_asset_card_view, this, true)
    }

    override fun hasOverlappingRendering(): Boolean {
        return false
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAttachedToWindow = true

    }

    override fun onDetachedFromWindow() {
        mAttachedToWindow = false
        super.onDetachedFromWindow()
    }

    fun setRailCommonDataModel(enveuVideoItemBean: EnveuVideoItemBean,widgetImageType: String) {
        if (enveuVideoItemBean.isContinueWatching) {
            if (enveuVideoItemBean.videoPosition > 0) {
                customMenuCardViewBinding?.continueWatching?.visibility = View.VISIBLE
                customMenuCardViewBinding?.pbProcessing?.visibility = View.VISIBLE
                val totalDuration: Double = enveuVideoItemBean.duration.toDouble()
                val currentPosition: Double =
                        (enveuVideoItemBean.videoPosition * 1000).toDouble()
                val percentagePlayed = currentPosition / totalDuration * 100L
                customMenuCardViewBinding?.pbProcessing?.progress = percentagePlayed.toInt()
            } else {
                customMenuCardViewBinding?.continueWatching?.visibility = View.GONE
                customMenuCardViewBinding?.pbProcessing?.visibility = View.GONE
            }
        }

        val crousalImages: HashMap<String, Thumbnail> = enveuVideoItemBean.getImages()
        var imageType = KalturaImageType.LANDSCAPE
        if (widgetImageType.equals("9x16", ignoreCase = true)) {
            imageType = KalturaImageType.PORTRAIT
        }
        enveuVideoItemBean.posterURL =
            ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 640, 450)

        customMenuCardViewBinding?.playlistItem = enveuVideoItemBean
        customMenuCardViewBinding?.let {
            it.txtTitle.text = enveuVideoItemBean.title
        }
    }

}
