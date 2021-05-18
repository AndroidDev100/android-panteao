package panteao.make.ready.cardlayout.cardview


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.databinding.CustomLandscapeHeroViewBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod


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
        lp.width = (AppCommonMethod.ScreenWidth - AppCommonMethod.dptoPx(context,120)).toInt()
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

    fun setRailCommonDataModel(enveuVideoItemBean: panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean) {
        customLandscapeHeroViewBinding?.playlistItem = enveuVideoItemBean
    }

}
