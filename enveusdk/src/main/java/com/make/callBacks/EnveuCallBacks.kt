package com.make.callBacks

import com.make.baseCollection.baseCategoryModel.BaseCategory

interface EnveuCallBacks{

    fun success(status: Boolean, baseCategory : List <BaseCategory>){

    }
    fun failure(status: Boolean, errorCode : Int, message : String){

    }
}