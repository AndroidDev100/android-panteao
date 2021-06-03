package panteao.make.ready.adapters.tv

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import panteao.make.ready.R
import panteao.make.ready.SDKConfig
import panteao.make.ready.beanModel.model.MenuModel
import panteao.make.ready.beanModel.responseModels.LoginResponse.UserData
import panteao.make.ready.databinding.MenuItemBinding
import panteao.make.ready.utils.commonMethods.AppCommonMethod
import panteao.make.ready.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import kotlin.math.roundToInt


class TvMenuAdapter(private val dataSet: ArrayList<MenuModel>, internal var mContext: Context) :
        ArrayAdapter<MenuModel>(mContext, R.layout.menu_item, dataSet) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        lateinit var menuModelBinding: MenuItemBinding
        val inflater = LayoutInflater.from(context)
        menuModelBinding = DataBindingUtil.inflate(inflater, R.layout.menu_item, parent, false)
        val menuModel = getItem(position) as MenuModel
        var userData = UserData()
        if (KsPreferenceKeys.getInstance().appPrefProfile != "")
            userData = Gson().fromJson<UserData>(KsPreferenceKeys.getInstance().appPrefProfile, UserData::class.java)
        if (position == 0) {
            menuModelBinding.labelText.setPaddingRelative(
                    0, 2, 0, 2
            )
            menuModelBinding.labelText.setImageDrawable(mContext.resources.getDrawable(R.drawable.makereadytv_logo))
            val params = menuModelBinding.labelText.layoutParams
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = AppCommonMethod.dptoPx(mContext, 40).roundToInt()
            menuModelBinding.labelText.layoutParams = params
            menuModelBinding.labelText.setBackgroundColor(mContext.resources.getColor(R.color.popular_search_color))
            menuModelBinding.label.visibility = View.GONE
        } else {
            menuModelBinding.labelText.setImageDrawable(menuModel.menuIcon)
            menuModelBinding.label.text = menuModel.menuName
        }
        if (convertView == null) {
            convertView = menuModelBinding.root
        } else {
            menuModelBinding =
                    DataBindingUtil.inflate(inflater, R.layout.menu_item, parent, false)
            menuModelBinding.menuModel = getItem(position) as MenuModel
        }
        return convertView
    }
}