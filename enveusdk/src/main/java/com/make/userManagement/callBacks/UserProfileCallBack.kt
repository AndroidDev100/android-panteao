package com.make.userManagement.callBacks

import com.make.userManagement.bean.UserProfile.UserProfileResponse
import retrofit2.Response

interface UserProfileCallBack {

    fun success(status: Boolean, loginResponse : Response<UserProfileResponse>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}