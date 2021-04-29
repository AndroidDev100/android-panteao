package panteao.make.ready.networking.apiendpoints;

import panteao.make.ready.utils.cropImage.helpers.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import panteao.make.ready.utils.cropImage.helpers.Logger;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestConfigContinue {
    private static Retrofit retrofit2 = null;

    public static Retrofit getClientForContinue(String token) {

        String baseUrl = "http://5cf64c7e46583900149cb61b.mockapi.io/api/";
        Logger.e("TOKEN", token);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(interceptor);


        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-auth-token", token);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit2 = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit2;
    }

}
