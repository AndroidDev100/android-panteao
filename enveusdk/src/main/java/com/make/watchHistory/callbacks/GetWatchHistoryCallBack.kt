package com.make.watchHistory.callbacks

import com.make.watchHistory.beans.ResponseWatchHistoryAssetList
import retrofit2.Response

interface GetWatchHistoryCallBack {

    fun success(status: Boolean, loginResponse: Response<ResponseWatchHistoryAssetList>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}