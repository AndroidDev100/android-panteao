package panteao.make.ready.cardlayout.cardview


import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.LayoutPopularSearchItemBinding
import panteao.make.ready.utils.cropImage.helpers.Logger


open class PopularSearchCardView : BaseCardView {
    private var customMenuCardViewBinding: LayoutPopularSearchItemBinding? = null
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
            DataBindingUtil.inflate(inflater, R.layout.layout_popular_search_item, this, true)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
   Logger.e("ON_FOCUS_CHANGE", gainFocus.toString())
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

    fun setMenuModel(itemsItem: EnveuVideoItemBean) {

        customMenuCardViewBinding?.contentsItem = itemsItem

    }


    fun setTitleVisbility(focus: Boolean) {
        if (focus) {
            customMenuCardViewBinding?.episodeTitle?.visibility = View.VISIBLE
        } else {
            customMenuCardViewBinding?.episodeTitle?.visibility = View.GONE
        }
    }

}
/**
 * Sets the image drawable with fade-in animation.
 */
