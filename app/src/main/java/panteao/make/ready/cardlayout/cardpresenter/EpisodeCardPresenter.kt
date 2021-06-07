package panteao.make.ready.cardlayout.cardpresenter

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter
import panteao.make.ready.R
import panteao.make.ready.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import panteao.make.ready.cardlayout.cardview.EpisodeCardView

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
class EpisodeCardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        sDefaultBackgroundDrawable =
            parent.resources.getDrawable(R.drawable.button_default_background)
        sSelectedBackgroundDrawable =
            parent.resources.getDrawable(R.drawable.button_default_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.placeholder_landscape)

        val cardView = object : EpisodeCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
//                EpisodeCardPresenter.updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
//        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        if (item is EnveuVideoItemBean) {
            val cardView = viewHolder.view as EpisodeCardView
            cardView.setMenuModel(item)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as EpisodeCardView
        // Remove references to images so that the garbage collector can free up memory
    }

    companion object {
        private var sSelectedBackgroundDrawable: Drawable? = null
        private var sDefaultBackgroundDrawable: Drawable? = null

        private fun updateCardBackgroundColor(view: EpisodeCardView, selected: Boolean) {
            val backgroundDrawable =
                if (selected) sSelectedBackgroundDrawable else sDefaultBackgroundDrawable
            view.background = backgroundDrawable
        }
    }
}
