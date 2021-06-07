package panteao.make.ready.activities.detailspage.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import org.json.JSONObject
import panteao.make.ready.SDKConfig
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty
import panteao.make.ready.beanModel.enveuCommonRailData.RailCommonData
import panteao.make.ready.beanModel.isLike.ResponseIsLike
import panteao.make.ready.beanModelV2.videoDetailsV2.EnveuVideoDetailsBean
import panteao.make.ready.beanModelV3.playListModelV2.EnveuCommonResponse
import panteao.make.ready.networking.apiendpoints.ApiInterface
import panteao.make.ready.networking.apiendpoints.RequestConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VideoDetailRepository {
    private var endpoint: ApiInterface? =
        RequestConfig.getEnveuClient().create(ApiInterface::class.java)

    companion object {
        private var mInstance: VideoDetailRepository? = null
        val instance: VideoDetailRepository
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = VideoDetailRepository()
                }
                return mInstance as VideoDetailRepository
            }

    }

    fun getAssetDetails(assetId: String): LiveData<RailCommonData> {
        val responseMutableLiveData = MutableLiveData<RailCommonData>()
//        endpoint?.getVideoDetail(assetId, SDKConfig.getInstance().currentLanguageCode)
//            ?.enqueue(object : Callback<EnveuVideoDetailsBean> {
//                override fun onResponse(
//                    call: Call<EnveuVideoDetailsBean>,
//                    response: Response<EnveuVideoDetailsBean>
//                ) {
//                    val railCommonData = RailCommonData()
//                    railCommonData.setEnveuVideoItemBeans(ArrayList())
//                    if (response.body() != null && response.body()?.data != null) {
//                        val enveuVideoDetails = response.body()
//                        val itemBean = EnveuVideoItemBean(enveuVideoDetails)
//                        itemBean.responseCode = AppConstants.RESPONSE_CODE_SUCCESS
//                        railCommonData.getEnveuVideoItemBeans()?.add(itemBean)
//                        responseMutableLiveData.postValue(railCommonData)
//                    } else {
//                        val itemBean = EnveuVideoItemBean()
//                        itemBean.responseCode = response.code()
//                        railCommonData.getEnveuVideoItemBeans()?.add(itemBean)
//                        responseMutableLiveData.postValue(railCommonData)
//                    }
//                }
//
//                override fun onFailure(call: Call<EnveuVideoDetailsBean>, t: Throwable) {
//                    responseMutableLiveData.postValue(null)
//                }
//            })
        return responseMutableLiveData

    }

    fun getSeasonEpisodesV2(
        seriesId: Int, pageNumber: Int,
        size: Int, seasonNumber: Int
    ): LiveData<RailCommonData> {
        val responseMutableLiveData = MutableLiveData<RailCommonData>()

//        endpoint?.getRelatedContent(seriesId, seasonNumber, pageNumber, size)
//            ?.enqueue(object : Callback<EnveuCommonResponse> {
//                override fun onResponse(
//                    call: Call<EnveuCommonResponse>,
//                    response: Response<EnveuCommonResponse>
//                ) {
//                    parseResponseAsRailCommonData(response, responseMutableLiveData)
//                }
//
//                override fun onFailure(call: Call<EnveuCommonResponse>, t: Throwable) {
//                    responseMutableLiveData.postValue(null)
//                }
//            })
        return responseMutableLiveData
    }

    fun getAllEpisodeV2(seriesId: Int, pageNumber: Int, size: Int): LiveData<RailCommonData> {
        val responseMutableLiveData = MutableLiveData<RailCommonData>()

//        endpoint?.getRelatedContentWithoutSNo(seriesId, pageNumber, size)
//            ?.enqueue(object : Callback<EnveuCommonResponse> {
//                override fun onFailure(call: Call<EnveuCommonResponse>, t: Throwable) {
//                    responseMutableLiveData.postValue(null)
//                }
//
//                override fun onResponse(
//                    call: Call<EnveuCommonResponse>,
//                    response: Response<EnveuCommonResponse>
//                ) {
//                    parseResponseAsRailCommonData(response, responseMutableLiveData)
//                }
//
//            }
//            )
        return responseMutableLiveData
    }

    private fun parseResponseAsRailCommonData(
        response: Response<EnveuCommonResponse>,
        responseMutableLiveData: MutableLiveData<RailCommonData>
    ) {
        if (response.body() != null && response.body()!!.data != null) {
            val railCommonData = RailCommonData(response.body()!!.data!!)
            railCommonData.status = true
            responseMutableLiveData.postValue(railCommonData)
        } else {
            //  val errorModel = ApiErrorModel(response.code(), response.message())
        }

    }

    fun hitApiIsLike(context: Context, token: String, seriesId: Int): LiveData<ResponseIsLike> {
        val isLikeResponseLiveData = MutableLiveData<ResponseIsLike>()
        val isLikeResponseModel = ResponseIsLike()
//        ApiCallManager.instance.hitApiIsLike(
//            token,
//            seriesId,
//            object : CommonCallBack<ResponseIsLike> {
//                override fun success(status: Boolean, isLikeResponse: Response<ResponseIsLike>) {
//                    if (status) {
//                        if (isLikeResponse.code() == 500) {
//                            isLikeResponseModel.responseCode = isLikeResponse.code()
//                            isLikeResponseModel.isSuccessful = false
//                            isLikeResponseModel.debugMessage =
//                                context.getString(R.string.something_went_wrong)
//                            isLikeResponseLiveData.postValue(isLikeResponseModel)
//                        } else if (isLikeResponse.code() != 200) {
//                            val errorObject =
//                                JSONObject(isLikeResponse.errorBody()?.string())
//                            isLikeResponseModel.responseCode = errorObject.getInt("responseCode")
//                            var debugMessage = ""
//                            when (isLikeResponseModel.responseCode) {
//                                4302 -> {
//                                    debugMessage =
//                                        context.getString(R.string.you_are_logged_out)
//                                }
//                                else -> {
//                                    debugMessage = context.getString(R.string.something_went_wrong)
//                                }
//                            }
//                            isLikeResponseModel.isSuccessful = false
//                            isLikeResponseModel.debugMessage = debugMessage
//                            isLikeResponseLiveData.postValue(isLikeResponseModel)
//                        } else {
//                            val gson = Gson()
//                            val tmp = gson.toJson(isLikeResponse.body())
//                            val profileItemBean =
//                                gson.fromJson(tmp, ResponseIsLike::class.java)
//                            profileItemBean.isSuccessful = true
//                            isLikeResponseLiveData.postValue(profileItemBean)
//                        }
//                    } else {
//                        isLikeResponseModel.isSuccessful = false
//                        isLikeResponseModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        isLikeResponseLiveData.postValue(isLikeResponseModel)
//                    }
//                }
//
//                override fun failure(status: Boolean, errorCode: Int, message: String) {
//                    isLikeResponseModel.isSuccessful = false
//                    isLikeResponseModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    isLikeResponseLiveData.postValue(isLikeResponseModel)
//                }
//            })
        return isLikeResponseLiveData
    }

    fun hitApiDeleteLike(context: Context, token: String, seriesId: Int): LiveData<ResponseEmpty> {
        val emptyResponseLiveData = MutableLiveData<ResponseEmpty>()
        val responseEmptyModel = ResponseEmpty()
//        ApiCallManager.instance.deleteLike(token, seriesId, object : CommonCallBack<ResponseEmpty> {
//            override fun success(status: Boolean, baseResponse: Response<ResponseEmpty>) {
//                if (status) {
//                    if (baseResponse.code() == 500) {
//                        responseEmptyModel.responseCode = baseResponse.code()
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else if (baseResponse.code() != 200) {
//                        val errorObject =
//                            JSONObject(baseResponse.errorBody()?.string())
//                        responseEmptyModel.responseCode = errorObject.getInt("responseCode")
//                        var debugMessage = ""
//                        when (responseEmptyModel.responseCode) {
//                            4302 -> {
//                                debugMessage =
//                                    context.getString(R.string.you_are_logged_out)
//                            }
//                            else -> {
//                                debugMessage = context.getString(R.string.something_went_wrong)
//                            }
//                        }
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage = debugMessage
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else {
//                        val gson = Gson()
//                        val tmp = gson.toJson(baseResponse.body())
//                        val profileItemBean =
//                            gson.fromJson(tmp, ResponseEmpty::class.java)
//                        profileItemBean.isSuccessful = true
//                        emptyResponseLiveData.postValue(profileItemBean)
//                    }
//                } else {
//                    responseEmptyModel.isSuccessful = false
//                    responseEmptyModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    emptyResponseLiveData.postValue(responseEmptyModel)
//                }
//            }
//
//            override fun failure(status: Boolean, errorCode: Int, message: String) {
//                responseEmptyModel.isSuccessful = false
//                responseEmptyModel.debugMessage =
//                    context.getString(R.string.something_went_wrong)
//                emptyResponseLiveData.postValue(responseEmptyModel)
//            }
//        })
        return emptyResponseLiveData
    }

    fun hitApiAddLike(context: Context, token: String, seriesId: Int): LiveData<ResponseEmpty> {
        val emptyResponseLiveData = MutableLiveData<ResponseEmpty>()
        val responseEmptyModel = ResponseEmpty()
//        ApiCallManager.instance.addLike(token, seriesId, object : CommonCallBack<ResponseEmpty> {
//            override fun success(status: Boolean, baseResponse: Response<ResponseEmpty>) {
//                if (status) {
//                    if (baseResponse.code() == 500) {
//                        responseEmptyModel.responseCode = baseResponse.code()
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else if (baseResponse.code() != 200) {
//                        val errorObject =
//                            JSONObject(baseResponse.errorBody()?.string())
//                        responseEmptyModel.responseCode = errorObject.getInt("responseCode")
//                        var debugMessage = ""
//                        when (responseEmptyModel.responseCode) {
//                            4302 -> {
//                                debugMessage =
//                                    context.getString(R.string.you_are_logged_out)
//                            }
//                            else -> {
//                                debugMessage = context.getString(R.string.something_went_wrong)
//                            }
//                        }
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage = debugMessage
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else {
//                        val gson = Gson()
//                        val tmp = gson.toJson(baseResponse.body())
//                        val profileItemBean =
//                            gson.fromJson(tmp, ResponseEmpty::class.java)
//                        profileItemBean.isSuccessful = true
//                        emptyResponseLiveData.postValue(profileItemBean)
//                    }
//                } else {
//                    responseEmptyModel.isSuccessful = false
//                    responseEmptyModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    emptyResponseLiveData.postValue(responseEmptyModel)
//                }
//            }
//
//            override fun failure(status: Boolean, errorCode: Int, message: String) {
//                responseEmptyModel.isSuccessful = false
//                responseEmptyModel.debugMessage =
//                    context.getString(R.string.something_went_wrong)
//                emptyResponseLiveData.postValue(responseEmptyModel)
//            }
//        })
        return emptyResponseLiveData
    }

    fun hitRemoveWatchlist(context: Context,token: String, seriesId: Int): LiveData<ResponseEmpty> {
        val emptyResponseLiveData = MutableLiveData<ResponseEmpty>()
        val responseEmptyModel = ResponseEmpty()
//        ApiCallManager.instance.deleteLike(token, seriesId, object : CommonCallBack<ResponseEmpty> {
//            override fun success(status: Boolean, baseResponse: Response<ResponseEmpty>) {
//                if (status) {
//                    if (baseResponse.code() == 500) {
//                        responseEmptyModel.responseCode = baseResponse.code()
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else if (baseResponse.code() != 200) {
//                        val errorObject =
//                            JSONObject(baseResponse.errorBody()?.string())
//                        responseEmptyModel.responseCode = errorObject.getInt("responseCode")
//                        var debugMessage = ""
//                        when (responseEmptyModel.responseCode) {
//                            4302 -> {
//                                debugMessage =
//                                    context.getString(R.string.you_are_logged_out)
//                            }
//                            else -> {
//                                debugMessage = context.getString(R.string.something_went_wrong)
//                            }
//                        }
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage = debugMessage
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else {
//                        val gson = Gson()
//                        val tmp = gson.toJson(baseResponse.body())
//                        val profileItemBean =
//                            gson.fromJson(tmp, ResponseEmpty::class.java)
//                        profileItemBean.isSuccessful = true
//                        emptyResponseLiveData.postValue(profileItemBean)
//                    }
//                } else {
//                    responseEmptyModel.isSuccessful = false
//                    responseEmptyModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    emptyResponseLiveData.postValue(responseEmptyModel)
//                }
//            }
//
//            override fun failure(status: Boolean, errorCode: Int, message: String) {
//                responseEmptyModel.isSuccessful = false
//                responseEmptyModel.debugMessage =
//                    context.getString(R.string.something_went_wrong)
//                emptyResponseLiveData.postValue(responseEmptyModel)
//            }
//        })
        return emptyResponseLiveData
    }

    fun hitApiIsToWatchList(context: Context,token: String, seriesId: Int): LiveData<ResponseIsLike> {
        val isLikeResponseLiveData = MutableLiveData<ResponseIsLike>()
        val isLikeResponseModel = ResponseIsLike()
//        ApiCallManager.instance.hitApiIsLike(
//            token,
//            seriesId,
//            object : CommonCallBack<ResponseIsLike> {
//                override fun success(status: Boolean, isLikeResponse: Response<ResponseIsLike>) {
//                    if (status) {
//                        if (isLikeResponse.code() == 500) {
//                            isLikeResponseModel.responseCode = isLikeResponse.code()
//                            isLikeResponseModel.isSuccessful = false
//                            isLikeResponseModel.debugMessage =
//                                context.getString(R.string.something_went_wrong)
//                            isLikeResponseLiveData.postValue(isLikeResponseModel)
//                        } else if (isLikeResponse.code() != 200) {
//                            val errorObject =
//                                JSONObject(isLikeResponse.errorBody()?.string())
//                            isLikeResponseModel.responseCode = errorObject.getInt("responseCode")
//                            var debugMessage = ""
//                            when (isLikeResponseModel.responseCode) {
//                                4302 -> {
//                                    debugMessage =
//                                        context.getString(R.string.you_are_logged_out)
//                                }
//                                else -> {
//                                    debugMessage = context.getString(R.string.something_went_wrong)
//                                }
//                            }
//                            isLikeResponseModel.isSuccessful = false
//                            isLikeResponseModel.debugMessage = debugMessage
//                            isLikeResponseLiveData.postValue(isLikeResponseModel)
//                        } else {
//                            val gson = Gson()
//                            val tmp = gson.toJson(isLikeResponse.body())
//                            val profileItemBean =
//                                gson.fromJson(tmp, ResponseIsLike::class.java)
//                            profileItemBean.isSuccessful = true
//                            isLikeResponseLiveData.postValue(profileItemBean)
//                        }
//                    } else {
//                        isLikeResponseModel.isSuccessful = false
//                        isLikeResponseModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        isLikeResponseLiveData.postValue(isLikeResponseModel)
//                    }
//                }
//
//                override fun failure(status: Boolean, errorCode: Int, message: String) {
//                    isLikeResponseModel.isSuccessful = false
//                    isLikeResponseModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    isLikeResponseLiveData.postValue(isLikeResponseModel)
//                }
//            })
        return isLikeResponseLiveData
    }


    fun hitApiAddToWatchList(context: Context,token: String, seriesId: Int): LiveData<ResponseEmpty> {
        val emptyResponseLiveData = MutableLiveData<ResponseEmpty>()
        val responseEmptyModel = ResponseEmpty()
//        ApiCallManager.instance.addLike(token, seriesId, object : CommonCallBack<ResponseEmpty> {
//            override fun success(status: Boolean, baseResponse: Response<ResponseEmpty>) {
//                if (status) {
//                    if (baseResponse.code() == 500) {
//                        responseEmptyModel.responseCode = baseResponse.code()
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage =
//                            context.getString(R.string.something_went_wrong)
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else if (baseResponse.code() != 200) {
//                        val errorObject =
//                            JSONObject(baseResponse.errorBody()?.string())
//                        responseEmptyModel.responseCode = errorObject.getInt("responseCode")
//                        var debugMessage = ""
//                        when (responseEmptyModel.responseCode) {
//                            4302 -> {
//                                debugMessage =
//                                    context.getString(R.string.you_are_logged_out)
//                            }
//                            else -> {
//                                debugMessage = context.getString(R.string.something_went_wrong)
//                            }
//                        }
//                        responseEmptyModel.isSuccessful = false
//                        responseEmptyModel.debugMessage = debugMessage
//                        emptyResponseLiveData.postValue(responseEmptyModel)
//                    } else {
//                        val gson = Gson()
//                        val tmp = gson.toJson(baseResponse.body())
//                        val profileItemBean =
//                            gson.fromJson(tmp, ResponseEmpty::class.java)
//                        profileItemBean.isSuccessful = true
//                        emptyResponseLiveData.postValue(profileItemBean)
//                    }
//                } else {
//                    responseEmptyModel.isSuccessful = false
//                    responseEmptyModel.debugMessage =
//                        context.getString(R.string.something_went_wrong)
//                    emptyResponseLiveData.postValue(responseEmptyModel)
//                }
//            }
//
//            override fun failure(status: Boolean, errorCode: Int, message: String) {
//                responseEmptyModel.isSuccessful = false
//                responseEmptyModel.debugMessage =
//                    context.getString(R.string.something_went_wrong)
//                emptyResponseLiveData.postValue(responseEmptyModel)
//            }
//        })
        return emptyResponseLiveData
    }
}


