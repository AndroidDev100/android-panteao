package panteao.make.ready.beanModel.facebook.loginresponse

import com.google.gson.annotations.SerializedName
import panteao.make.ready.beanModel.facebook.loginresponse.Error
import java.io.Serializable

data class FacebookLoginResponse(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("user_code")
    val userCode: String? = null,

    @field:SerializedName("interval")
    val interval: Int? = null,

    @field:SerializedName("verification_uri")
    val verificationUri: String? = null,

    @field:SerializedName("error")
    val error: Error? = null,

    @field:SerializedName("expires_in")
    val expiresIn: Int? = null
) : Serializable