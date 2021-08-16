package panteao.make.ready.beanModel.facebook.userprofileresponse

import com.google.gson.annotations.SerializedName
import panteao.make.ready.beanModel.facebook.userprofileresponse.Data
import java.io.Serializable

data class Picture(

    @field:SerializedName("data")
    val data: Data? = null
) : Serializable