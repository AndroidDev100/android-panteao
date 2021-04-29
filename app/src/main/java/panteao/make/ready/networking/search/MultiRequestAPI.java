package panteao.make.ready.networking.search;

import panteao.make.ready.beanModel.popularSearch.ResponsePopularSearch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MultiRequestAPI {

    @Headers("x-platform: android")
    @GET("search")
    io.reactivex.Observable<ResponseSearch> getSearch(@Query("query") String keyword, @Query("assetType") String type, @Query("size") int size, @Query("page") int page);

    @Headers("x-platform: android")
    @GET("searchSeries")
    Call<ResponsePopularSearch> getSearchSeries(@Query("query") String keyword, @Query("assetType") String type, @Query("size") int size, @Query("page") int page);

    @Headers("x-platform: android")
    @GET("search")
    Call<ResponseSearch> getCategorySearch(@Query("query") String keyword, @Query("assetType") String type, @Query("size") int size, @Query("page") int page);


}
