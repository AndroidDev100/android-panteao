package panteao.make.ready.beanModel.facebook.loginstatusresponse

import com.google.gson.annotations.SerializedName

data class Error(

    @field:SerializedName("error_user_title")
    val errorUserTitle: String? = null,

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("error_subcode")
    val errorSubcode: Int? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("is_transient")
    val isTransient: Boolean? = null,

    @field:SerializedName("fbtrace_id")
    val fbtraceId: String? = null,

    @field:SerializedName("error_user_msg")
    val errorUserMsg: String? = null
)