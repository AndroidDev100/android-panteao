package panteao.make.ready.repository.bookmarking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.make.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.make.bookmarking.bean.BookmarkingResponse;
import com.make.bookmarking.bean.GetBookmarkResponse;
import com.make.bookmarking.bean.continuewatching.GetContinueWatchingBean;
import com.make.userManagement.callBacks.BookmarkingCallback;
import com.make.userManagement.callBacks.GetBookmarkCallback;
import com.make.userManagement.callBacks.GetContinueWatchingCallback;
import com.make.watchHistory.beans.ResponseWatchHistoryAssetList;
import com.make.watchHistory.callbacks.GetWatchHistoryCallBack;
import panteao.make.ready.utils.constants.AppConstants;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import org.json.JSONObject;

import panteao.make.ready.utils.cropImage.helpers.Logger;
import retrofit2.Response;

public class BookmarkingRepository {


    private static BookmarkingRepository instance;

    private BookmarkingRepository() {
    }

    public static BookmarkingRepository getInstance() {
        if (instance == null) {
            instance = new BookmarkingRepository();
        }
        return (instance);
    }

    public LiveData<BookmarkingResponse> bookmarkVideo(String token, int assetId, int position) {
        final MutableLiveData<BookmarkingResponse> bookmarkingResponseMutableLiveData;
        bookmarkingResponseMutableLiveData = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().bookmarkService(token, assetId, position, new BookmarkingCallback() {
            @Override
            public void success(boolean status, Response<BookmarkingResponse> bookmarkingResponse) {
                BookmarkingResponse bookmarkingResponseModel = new BookmarkingResponse();
                if (bookmarkingResponse != null) {
                    bookmarkingResponseModel.setResponseCode((long) bookmarkingResponse.code());
                    switch (bookmarkingResponse.code()) {
                        case 401:
                        case 404:
                        case 403: {
                            String debugMessage = "";
                            try {
                                JSONObject jObjError = new JSONObject(bookmarkingResponse.errorBody().string());
                                debugMessage = jObjError.getString("debugMessage");
                            } catch (Exception e) {
                                Logger.e("RegistrationLoginRepo", "" + e.toString());
                            }
                            bookmarkingResponseModel.setDebugMessage(debugMessage);
                        }
                        break;
                        default: {
                            if (bookmarkingResponse.body() != null && bookmarkingResponse.body().getResponseCode() == 2001) {
                                bookmarkingResponseModel.setData(bookmarkingResponse);
                            }
                        }
                        bookmarkingResponseMutableLiveData.postValue(bookmarkingResponseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {

            }
        });

        return bookmarkingResponseMutableLiveData;
    }

    public LiveData<GetBookmarkResponse> getBookmarkByVideoId(String token, int videoId) {
        MutableLiveData<GetBookmarkResponse> bookmarkingResponseMutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().getBookmarkByVideoId(token, videoId, new GetBookmarkCallback() {

            @Override
            public void success(boolean status, Response<GetBookmarkResponse> getBookmarkResponse) {
                bookmarkingResponseMutableLiveData.postValue(getBookmarkResponse.body());
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {
                bookmarkingResponseMutableLiveData.postValue(null);

            }
        });
        return bookmarkingResponseMutableLiveData;
    }

    public MutableLiveData<GetBookmarkResponse> finishBookmark(String token, int assestId) {
        MutableLiveData<GetBookmarkResponse> bookmarkingResponseMutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().finishBookmark(token, assestId, new BookmarkingCallback() {
            @Override
            public void success(boolean status, Response<BookmarkingResponse> loginResponse) {
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {

            }
        });
        return bookmarkingResponseMutableLiveData;
    }

    public MutableLiveData<GetContinueWatchingBean> getContinueWatchingData(String token, int pageNumber, int pageSize) {
        MutableLiveData<GetContinueWatchingBean> bookmarkingResponseMutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().getContinueWatchingData(token,pageNumber,pageSize, new GetContinueWatchingCallback() {
            @Override
            public void success(boolean status, Response<GetContinueWatchingBean> getBookmarkResponse) {
                bookmarkingResponseMutableLiveData.postValue(getBookmarkResponse.body());
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {

                bookmarkingResponseMutableLiveData.postValue(null);

            }
        });
        return bookmarkingResponseMutableLiveData;
    }



    public LiveData<ResponseWatchHistoryAssetList> hitApiGetWatchHistoryList(String token, int page, int size) {
        MutableLiveData<ResponseWatchHistoryAssetList> responseData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().getWatchHistoryList(token, page, size, new GetWatchHistoryCallBack() {
            @Override
            public void success(boolean status, Response<ResponseWatchHistoryAssetList> loginResponse) {
               try {
                   if (loginResponse.body()==null){
                       if (loginResponse.errorBody()!=null){
                           try {
                               JSONObject errorObject = new JSONObject(loginResponse.errorBody().string());
                               if (errorObject.getInt("responseCode") != 0) {
                                   if (errorObject.getInt("responseCode") == 4302) {
                                       ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                                       empty.setStatus(false);
                                       empty.setResponseCode(4302);
                                       responseData.postValue(empty);
                                   }else {
                                       ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                                       empty.setStatus(false);
                                       empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                                       responseData.postValue(empty);
                                   }

                               }else {
                                   ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                                   empty.setStatus(false);
                                   empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                                   responseData.postValue(empty);
                               }
                           }catch (Exception ignored){

                           }


                       }else {
                           ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                           empty.setStatus(false);
                           empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                           responseData.postValue(empty);
                       }

                   }else {
                       ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                       if (loginResponse.body().getData()!=null){
                           empty.setStatus(true);
                           empty.setData(loginResponse.body().getData());
                           responseData.postValue(empty);
                       }else {
                           empty.setStatus(false);
                           responseData.postValue(empty);
                       }

                   }
               }catch (Exception e){
                   ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                   empty.setStatus(false);
                   empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                   responseData.postValue(empty);
               }



            }

            @Override
            public void failure(boolean status, int errorCode, String message) {
                ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                empty.setStatus(false);
                empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                responseData.postValue(empty);
            }
        });
        return responseData;
    }

    public void addToWatchHistory(String token, int assestId) {
        BaseCategoryServices.Companion.getInstance().addToWatchHistory(token,assestId,new BookmarkingCallback(){

            @Override
            public void failure(boolean status, int errorCode, String message) {

            }

            @Override
            public void success(boolean status, Response<BookmarkingResponse> loginResponse) {

            }
        });
    }

    public LiveData<BookmarkingResponse> deleteFromWatchHistory(String token, int assetId) {
        MutableLiveData<BookmarkingResponse> bookmarkingResponseMutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().deleteFromWatchHistory(token,assetId,new BookmarkingCallback(){
            @Override
            public void failure(boolean status, int errorCode, String message) {
                bookmarkingResponseMutableLiveData.postValue(null);
            }

            @Override
            public void success(boolean status, Response<BookmarkingResponse> loginResponse) {
                if(status)
                    bookmarkingResponseMutableLiveData.postValue(loginResponse.body());
                else
                    bookmarkingResponseMutableLiveData.postValue(null);
            }
        });
        return bookmarkingResponseMutableLiveData;
    }

    public LiveData<ResponseWatchHistoryAssetList> getMyWatchListData(String token, int page, int size) {
        MutableLiveData<ResponseWatchHistoryAssetList> responseData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().getWatchListData(token, page, size, new GetWatchHistoryCallBack() {
            @Override
            public void success(boolean status, Response<ResponseWatchHistoryAssetList> loginResponse) {
                try {
                    if (loginResponse.body()==null){
                        if (loginResponse.errorBody()!=null){
                            try {
                                JSONObject errorObject = new JSONObject(loginResponse.errorBody().string());
                                if (errorObject.getInt("responseCode") != 0) {
                                    if (errorObject.getInt("responseCode") == 4302) {
                                        ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                                        empty.setStatus(false);
                                        empty.setResponseCode(4302);
                                        responseData.postValue(empty);
                                    }else {
                                        ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                                        empty.setStatus(false);
                                        empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                                        responseData.postValue(empty);
                                    }

                                }else {
                                    ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                                    empty.setStatus(false);
                                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                                    responseData.postValue(empty);
                                }
                            }catch (Exception ignored){

                            }


                        }else {
                            ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                            empty.setStatus(false);
                            empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                            responseData.postValue(empty);
                        }

                    }else {
                        ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                        if (loginResponse.body().getData()!=null){
                            empty.setStatus(true);
                            empty.setData(loginResponse.body().getData());
                            responseData.postValue(empty);
                        }else {
                            empty.setStatus(false);
                            responseData.postValue(empty);
                        }
                    }
                }catch (Exception ignored){

                }
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {
                ResponseWatchHistoryAssetList empty = new ResponseWatchHistoryAssetList();
                empty.setStatus(false);
                empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                responseData.postValue(empty);
            }
        });
        return responseData;
    }

}
