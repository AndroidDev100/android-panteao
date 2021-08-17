package panteao.make.ready.beanModel.facebook.userprofileresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(

    @field:SerializedName("is_silhouette")
    val isSilhouette: Boolean? = null,

    @field:SerializedName("width")
    val width: Int? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("height")
    val height: Int? = null
) : Serializable