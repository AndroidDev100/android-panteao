package com.make.baseCollection.baseCategoryServices

import com.make.callBacks.EnveuCallBacks
import com.make.networkRequestManager.RequestManager
import com.make.userManagement.callBacks.*
import com.make.watchHistory.callbacks.GetWatchHistoryCallBack
import com.google.gson.JsonObject

class BaseCategoryServices {

    companion object {
        val instance = BaseCategoryServices()
    }


    fun categoryService(screenId: String, enveuCallBacks: EnveuCallBacks) {
        RequestManager.instance.categoryCall(screenId, enveuCallBacks)
    }

    fun mypurchasesService(auth:String?,
        page: String?,
        size: String?,
        myPurchasesCallBack: MyPurchasesCallBack
    ) {
        RequestManager.instance.muPurchasesCall(auth,page, size, myPurchasesCallBack)
    }

    fun loginService(email: String, password: String, enveuCallBacks: LoginCallBack) {
        RequestManager.instance.loginCall(email, password, enveuCallBacks)
    }

    fun registerService(
        name: String,
        email: String,
        password: String,
        enveuCallBacks: LoginCallBack
    ) {
        RequestManager.instance.registerCall(name, email, password, enveuCallBacks)
    }

    fun logoutService(token: String, enveuCallBacks: LogoutCallBack) {
        RequestManager.instance.logoutCall(token, enveuCallBacks)
    }

    fun fbLoginService(params: JsonObject, enveuCallBacks: LoginCallBack) {
        RequestManager.instance.fbLoginCall(params, enveuCallBacks);
    }

    fun fbForceLoginService(params: JsonObject, enveuCallBacks: LoginCallBack) {
        RequestManager.instance.fbForceLoginCall(params, enveuCallBacks);
    }

    fun changePasswordService(params: JsonObject, token: String, enveuCallBacks: LoginCallBack) {
        RequestManager.instance.changePasswordCall(params, token, enveuCallBacks);
    }

    fun userProfileService(token: String, callBack: UserProfileCallBack) {
        RequestManager.instance.userProfileCall(callBack, token);
    }

    fun userUpdateProfileService(token: String, name: String, callBack: UserProfileCallBack) {
        RequestManager.instance.userUpdateProfileCall(callBack, token, name);
    }

    fun bookmarkService(
        token: String,
        assetId: Int,
        position: Int,
        bookmarkingCallback: BookmarkingCallback
    ) {
        RequestManager.instance.bookmarkVideo(token, assetId, position, bookmarkingCallback)
    }

    fun getBookmarkByVideoId(
        token: String,
        videoId: Int,
        getBookmarkCallback: GetBookmarkCallback
    ) {
        RequestManager.instance.getBookmarkByVideoId(token, videoId, getBookmarkCallback)
    }

    fun finishBookmark(token: String, assestId: Int, bookmarkingCallback: BookmarkingCallback) {
        RequestManager.instance.finishBookmark(token, assestId, bookmarkingCallback)

    }

    fun getContinueWatchingData(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        getBookmarkCallback: GetContinueWatchingCallback
    ) {
        RequestManager.instance.getContinueWatchingData(
            token,
            pageNumber,
            pageSize,
            getBookmarkCallback
        )
    }

    fun getWatchHistoryList(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        getWatchHistoryCallBack: GetWatchHistoryCallBack
    ) {
        RequestManager.instance.getWatchHistory(
            token,
            pageNumber,
            pageSize,
            getWatchHistoryCallBack
        )
    }

    fun addToWatchHistory(token: String, assestId: Int, bookmarkingCallback: BookmarkingCallback) {
        RequestManager.instance.addToWatchHistory(token, assestId, bookmarkingCallback);
    }

    fun deleteFromWatchHistory(
        token: String,
        assetId: Int,
        bookmarkingCallback: BookmarkingCallback
    ) {
        RequestManager.instance.deleteFromWatchHistory(token, assetId, bookmarkingCallback);

    }

    fun getWatchListData(
        token: String,
        pageNumber: Int,
        pageSize: Int,
        getWatchHistoryCallBack: GetWatchHistoryCallBack
    ) {
        RequestManager.instance.getWatchListData(
            token,
            pageNumber,
            pageSize,
            getWatchHistoryCallBack
        )

    }

    fun forgotPasswordService(screenId: String, enveuCallBacks: ForgotPasswordCallBack) {
        RequestManager.instance.forgotPasswordCall(screenId, enveuCallBacks)
    }
}