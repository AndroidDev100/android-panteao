package panteao.make.ready.networking.apiendpoints

import io.reactivex.Observable
import panteao.make.ready.beanModel.facebook.loginresponse.FacebookLoginResponse
import panteao.make.ready.beanModel.facebook.loginstatusresponse.LoginStatusResponse
import panteao.make.ready.beanModel.facebook.userprofileresponse.FacebookUserProfileResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterfaceFacebook {

    @Headers("x-platform: android")
    @POST("v2.6/device/login")
    fun facebookLoginApi(@Query("access_token") accessToken: String, @Query("scope") scope: String): Observable<FacebookLoginResponse>

    @Headers("x-platform: android")
    @POST("v2.6/device/login_status")
    fun facebookLoginStatus(@Query("access_token") accessToken: String, @Query("code") code: String): Observable<LoginStatusResponse>

    @Headers("x-platform: android")
    @GET("v2.3/me")
    fun getFacebookUserProfile(@Query("fields") fields: String, @Query("access_token") accessToken: String): Observable<FacebookUserProfileResponse>


}