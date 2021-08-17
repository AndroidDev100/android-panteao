package panteao.make.ready.cardlayout.cardview


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.playListModelV2.Thumbnail
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.CustomLandscapeHeroViewBinding
import panteao.make.ready.enums.KalturaImageType
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.config.ImageLayer
import panteao.make.ready.utils.cropImage.helpers.Logger
import java.util.HashMap


@SuppressLint("ViewConstructor")
open class HeroCardView : BaseCardView {

    val TAG = "AssetCardView"

    private var customLandscapeHeroViewBinding: CustomLandscapeHeroViewBinding? = null
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
        customLandscapeHeroViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.custom_landscape_hero_view, this, true)
        val lp: ViewGroup.LayoutParams = customLandscapeHeroViewBinding?.infoField!!.layoutParams
        lp.width = (AppCommonMethod.ScreenWidth - AppCommonMethod.dptoPx(context, 120)).toInt()
        customLandscapeHeroViewBinding?.infoField!!.layoutParams = lp
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
        Logger.e("ASSET_DETAILS", Gson().toJson(enveuVideoItemBean))
        if (enveuVideoItemBean.images != null) {
            val crousalImages: HashMap<String, Thumbnail> = enveuVideoItemBean.images
            var imageType = KalturaImageType.LANDSCAPE
            if (widgetImageType.equals("9x16", ignoreCase = true)) {
                imageType = KalturaImageType.PORTRAIT
            }
            enveuVideoItemBean.posterURL =
                ImageLayer.getInstance().getFilteredImage(crousalImages, imageType, 1920, 1080)
        }
        customLandscapeHeroViewBinding?.playlistItem = enveuVideoItemBean
    }

}
