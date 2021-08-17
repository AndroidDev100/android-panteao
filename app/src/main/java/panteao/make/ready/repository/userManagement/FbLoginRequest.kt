package panteao.make.ready.repository.userManagement

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FbLoginRequest(

    @field:SerializedName("fbMail")
    var fbMail: Boolean? = null,

    @field:SerializedName("profilePicUrl")
    var profilePicUrl: String? = null,

    @field:SerializedName("fbId")
    var fbId: String? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("emailId")
    var emailId: String? = null,

    @field:SerializedName("accessToken")
    var accessToken: String? = null
) : Serializable