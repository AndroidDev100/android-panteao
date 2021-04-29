package com.make.userManagement.callBacks

import com.google.gson.JsonObject
import retrofit2.Response

interface ForgotPasswordCallBack {

    fun success(status: Boolean, loginResponse : Response<JsonObject>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}