package com.make.userManagement.callBacks

import com.make.bookmarking.bean.GetBookmarkResponse
import retrofit2.Response

interface GetBookmarkCallback {

    fun success(status: Boolean, loginResponse: Response<GetBookmarkResponse>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}