package panteao.make.ready.beanModel.facebook.userprofileresponse

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Error(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("fbtrace_id")
    val fbtraceId: String? = null
) : Serializable