package panteao.make.ready.repository.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.make.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.make.userManagement.callBacks.LogoutCallBack;
import panteao.make.ready.beanModel.emptyResponse.ResponseEmpty;
import panteao.make.ready.networking.apiendpoints.ApiInterface;
import panteao.make.ready.networking.apiendpoints.RequestConfig;
import panteao.make.ready.utils.constants.AppConstants;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeRepository {

    private static HomeRepository instance;

    private HomeRepository() {
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return (instance);
    }


    public LiveData<JsonObject> hitApiLogout(boolean session, String token) {
        final MutableLiveData<JsonObject> responseApi;
        {
            responseApi = new MutableLiveData<>();
            BaseCategoryServices.Companion.getInstance().logoutService(token, new LogoutCallBack() {
                @Override
                public void failure(boolean status, int errorCode, String message) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, 500);
                    responseApi.postValue(jsonObject);
                }

                @Override
                public void success(boolean status, Response<JsonObject> response) {
                        if (status){
                            try {
                                if (response.code() == 404) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                }if (response.code() == 403) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                }
                                else if (response.code() == 200) {
                                    Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(response.body());
                                } else if (response.code() == 401) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                } else if (response.code() == 500) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                }
                            }catch (Exception e){
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                responseApi.postValue(jsonObject);
                            }

                        }
                }
            });

        }
        return responseApi;
    }


    public LiveData<ResponseEmpty> hitApiVerifyUser(String token) {
        final MutableLiveData<ResponseEmpty> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);


            Call<ResponseEmpty> call = endpoint.getVerifyUser();
            call.enqueue(new Callback<ResponseEmpty>() {
                @Override
                public void onResponse(@NonNull Call<ResponseEmpty> call, @NonNull Response<ResponseEmpty> response) {
                    if (response.code() == 200) {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setResponseCode(response.code());
                        empty.setStatus(true);
                        responseApi.postValue(empty);
                    } else {

                        ResponseEmpty empty = new ResponseEmpty();

                        empty.setResponseCode(response.code());
                        responseApi.postValue(empty);
                    }


                }

                @Override
                public void onFailure(@NonNull Call<ResponseEmpty> call, @NonNull Throwable t) {
                   /* try {
                        responseApi.postValue(call.execute().body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    ResponseEmpty responseEmpty = new ResponseEmpty();
                    responseEmpty.setStatus(false);
                    responseApi.postValue(responseEmpty);
                }
            });
        }

        return responseApi;
    }


}
