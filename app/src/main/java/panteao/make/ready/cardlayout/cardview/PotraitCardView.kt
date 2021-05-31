package panteao.make.ready.cardlayout.cardview


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.leanback.widget.BaseCardView
import panteao.make.ready.R
import panteao.make.ready.databinding.CustomPotraitCardViewBinding


@SuppressLint("ViewConstructor")
open class PotraitCardView : BaseCardView {

    val TAG = "AssetCardView"

    private var customMenuCardViewBinding: CustomPotraitCardViewBinding? = null
    private var mAttachedToWindow: Boolean = false
    private var mContentType: Int = 0


    constructor(context: Context, contentType: Int) : super(context) {
        this.mContentType = contentType
        buildImageCardView()
    }

    private fun buildImageCardView() {
        // Make sure the ImageCardView is focusable.
        isFocusable = true
        isFocusableInTouchMode = true

        val inflater = LayoutInflater.from(context)
        customMenuCardViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.custom_potrait_card_view, this, true)
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


        Log.d(TAG, enveuVideoItemBean.toString())

        customMenuCardViewBinding?.playlistItem = enveuVideoItemBean
        customMenuCardViewBinding?.let {
           // it.txtTitle.text = enveuVideoItemBean.title
        }

    }

}