package panteao.make.ready.beanModel.facebook.userprofileresponse


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FacebookUserProfileResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("error")
    val error: Error? = null,

    @field:SerializedName("picture")
    val picture: Picture? = null,

    @field:SerializedName("first_name")
    val firstName: String? = null,

    @field:SerializedName("last_name")
    val lastName: String? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Serializable