package com.make.userManagement.callBacks

import panteao.make.ready.beanModel.responseModels.MyPurchasesResponseModel
import retrofit2.Response

interface MyPurchasesCallBack {
    fun success(status: Boolean, loginResponse : Response<MyPurchasesResponseModel>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}