package panteao.make.ready.beanModel.model

import android.graphics.drawable.Drawable

data class MenuIconModel(
    var menuName: String = "",
    var menuIcon: Drawable? = null,
    var menuId: Int = 0
)