package panteao.make.ready.cardlayout.cardview


import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.databinding.LayoutRecentSearchItemBinding


open class RecentSearchesCardView(context: Context) : BaseCardView(context) {
    private var customMenuCardViewBinding: LayoutRecentSearchItemBinding? = null
    private var mAttachedToWindow: Boolean = false

    init {
        buildImageCardView()
    }

    private fun buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true
        val inflater = LayoutInflater.from(context)
        customMenuCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_recent_search_item, this, true)
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

    fun setRecentSearches(recentSearchText: String) {
        customMenuCardViewBinding?.episodeTitle?.text = recentSearchText

    }
}
