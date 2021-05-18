package panteao.make.ready.utils

import android.text.TextUtils
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.*
import panteao.make.ready.R
import kotlin.jvm.internal.Intrinsics

class CustomListRowPresenter : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE) {

    override fun createRowViewHolder(parent: ViewGroup): RowPresenter.ViewHolder {
//        headerPresenter = CustomRowHeaderPresenter()
        Intrinsics.checkParameterIsNotNull(parent, "parent")
        val viewHolder = super.createRowViewHolder(parent)
        val var10000 = viewHolder.view
        return if (var10000 == null) {
            throw TypeCastException("null cannot be cast to non-null type android.support.v17.leanback.widget.ListRowView")
        } else {
            val var3 = (var10000 as ListRowView).gridView
            var3.windowAlignment = BaseGridView.WINDOW_ALIGN_BOTH_EDGE
            var3.windowAlignmentOffsetPercent = 0.0f
            var3.windowAlignmentOffset = parent.resources
                .getDimensionPixelSize(R.dimen.lb_browse_padding_start)
            var3.itemAlignmentOffsetPercent = 0.0f
            shadowEnabled = false

            Intrinsics.checkExpressionValueIsNotNull(viewHolder, "viewHolder")
            viewHolder
        }
    }

    class CustomRowHeaderPresenter : RowHeaderPresenter() {

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder?, item: Any?) {
            super.onBindViewHolder(viewHolder, item)
//            val headerItem = if (item == null) null
//            else (item as Row).headerItem;
//            val vh = viewHolder as RowHeaderPresenter.ViewHolder;
//            val title = vh.view.findViewById<TextView>(R.id.row_header);
//            if (VipaApplication.IS_KIDS_MODE) {
//                val rainbow = title.context.resources.getIntArray(R.array.packages_colors)
//                val currentColor = rainbow[headerItem?.id?.toInt()!!.rem(rainbow.size)]
//                if (!TextUtils.isEmpty(headerItem?.getName())) {
//                    title.setText(headerItem?.getName());
//                    title.setTextColor(
//                        currentColor
//                    )
//                }
//            }
//                title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F);
        }
    }
}