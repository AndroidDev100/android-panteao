package panteao.make.ready.cardlayout.cardview


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.databinding.SeriesCardViewBinding


@SuppressLint("ViewConstructor")
open class SeriesCardView : BaseCardView {
    private var customMenuCardViewBinding: SeriesCardViewBinding? = null
    private var mAttachedToWindow: Boolean = false
    private var mContentType: String = ""

    constructor(context: Context) : super(context) {
        buildImageCardView()
    }

    private fun buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true

        val inflater = LayoutInflater.from(context)
        customMenuCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.series_card_view, this, true)
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
        customMenuCardViewBinding?.let {
            it.episodeTitle.text = itemsItem.title
        }

    }


    fun setTitleVisbility(focus: Boolean) {
        if (focus) {
            customMenuCardViewBinding?.episodeTitle?.visibility = View.VISIBLE
        } else {
            customMenuCardViewBinding?.episodeTitle?.visibility = View.GONE
        }
    }
}
