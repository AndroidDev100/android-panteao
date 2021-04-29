package com.make.userManagement.callBacks

import com.make.bookmarking.bean.continuewatching.GetContinueWatchingBean
import retrofit2.Response

interface GetContinueWatchingCallback {

    fun success(status: Boolean, loginResponse: Response<GetContinueWatchingBean>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}