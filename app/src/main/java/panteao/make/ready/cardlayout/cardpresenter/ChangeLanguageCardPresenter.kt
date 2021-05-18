package panteao.make.ready.cardlayout.cardpresenter

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import panteao.make.ready.R
import panteao.make.ready.cardlayout.cardview.ChangeLanguageCardView
import panteao.make.ready.fragments.changelanguage.LanguageModel
import panteao.make.ready.fragments.streamingSettings.VideoQualityModel


class ChangeLanguageCardPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        sDefaultBackgroundDrawable = parent.resources.getDrawable(R.drawable.button_default_background)
        sSelectedBackgroundDrawable = parent.resources.getDrawable(R.drawable.button_default_background)
//        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.placeholder_landscape)

        val cardView = object : ChangeLanguageCardView(parent.context) {
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
        if (item is LanguageModel) {
            val cardView = viewHolder.view as ChangeLanguageCardView
            cardView.setLanguageModel(item)
        }else if (item is VideoQualityModel){
            val cardView = viewHolder.view as ChangeLanguageCardView
            cardView.setVideoQualityModel(item)
        }else if(item is String){
            val cardView = viewHolder.view as ChangeLanguageCardView
            cardView.setRecentSearches(item)
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        viewHolder.view as ChangeLanguageCardView
    }

    companion object {
        private var sSelectedBackgroundDrawable: Drawable? = null
        private var sDefaultBackgroundDrawable: Drawable? = null

        private fun updateCardBackgroundColor(view: ChangeLanguageCardView, selected: Boolean) {
            val backgroundDrawable = if (selected) sSelectedBackgroundDrawable else sDefaultBackgroundDrawable
            view.background = backgroundDrawable
        }
    }
}
