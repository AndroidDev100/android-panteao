package panteao.make.ready.cardlayout.cardview


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.CustomSquareCardViewBinding
import panteao.make.ready.enums.KalturaImageType
import panteao.make.ready.utils.config.ImageLayer
import java.util.HashMap


@SuppressLint("ViewConstructor")
open class SquareCardView : BaseCardView {

    val TAG = "AssetCardView"

    private var customMenuCardViewBinding: CustomSquareCardViewBinding? = null
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
        setBackgroundColor(context.resources.getColor(android.R.color.transparent))
        background = null
        customMenuCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.custom_square_card_view, this, true)
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

    fun setRailCommonDataModel(enveuVideoItemBean: EnveuVideoItemBean, widgetImageType: String) {

        Log.d(TAG, enveuVideoItemBean.toString())
        val crousalImages: HashMap<String, Thumbnail> = enveuVideoItemBean.getImages()
        var imageType = KalturaImageType.LANDSCAPE
        if (widgetImageType.equals("9x16", ignoreCase = true)) {
            imageType = KalturaImageType.PORTRAIT
        }
        enveuVideoItemBean.posterURL =
            ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 800, 450)

        customMenuCardViewBinding?.playlistItem = enveuVideoItemBean

        customMenuCardViewBinding?.let {
            it.txtTitle.text = enveuVideoItemBean.title
        }

    }

}
