package panteao.make.ready.cardlayout.cardview


import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.LayoutEpisodeItemBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod


open class EpisodeCardView : BaseCardView {
    private var customMenuCardViewBinding: LayoutEpisodeItemBinding? = null
    private var mAttachedToWindow: Boolean = false

    constructor(context: Context) : super(context) {
        buildImageCardView()
    }

    private fun buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true

        val inflater = LayoutInflater.from(context)
        customMenuCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_episode_item, this, true)
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

    fun setMenuModel(enveuVideoItemBean: EnveuVideoItemBean) {

        customMenuCardViewBinding?.playlistItem = enveuVideoItemBean
        customMenuCardViewBinding?.episodeDuration?.text =
            AppCommonMethod.calculateTime(enveuVideoItemBean.duration)
    }

}
/**
 * Sets the image drawable with fade-in animation.
 */
