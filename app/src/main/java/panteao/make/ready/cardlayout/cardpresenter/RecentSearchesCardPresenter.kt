package panteao.make.ready.cardlayout.cardpresenter

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import panteao.make.ready.R
import panteao.make.ready.cardlayout.cardview.RecentSearchesCardView


class RecentSearchesCardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        sDefaultBackgroundDrawable = parent.resources.getDrawable(R.drawable.button_default_background)
        sSelectedBackgroundDrawable = parent.resources.getDrawable(R.drawable.button_default_background)
//        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.placeholder_landscape)

        val cardView = object : RecentSearchesCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(
                    this,
                    selected
                )
                super.setSelected(selected)
//                setTitleVisbility(selected)
            }

        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true

        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        if (item is String) {
            val cardView = viewHolder.view as RecentSearchesCardView
            cardView.setRecentSearches(item)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        viewHolder.view as RecentSearchesCardView
    }

    companion object {
        private var sSelectedBackgroundDrawable: Drawable? = null
        private var sDefaultBackgroundDrawable: Drawable? = null

        private fun updateCardBackgroundColor(view: RecentSearchesCardView, selected: Boolean) {
            val backgroundDrawable = if (selected) sSelectedBackgroundDrawable else sDefaultBackgroundDrawable
            view.background = backgroundDrawable
        }
    }
}
