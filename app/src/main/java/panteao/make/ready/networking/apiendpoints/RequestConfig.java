package panteao.make.ready.networking.apiendpoints;

import panteao.make.ready.SDKConfig;
import panteao.make.ready.BuildConfig;
import panteao.make.ready.networking.profiler.HttpProfiler;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static panteao.make.ready.BuildConstants.BASE_URL;


public class RequestConfig {
    private static Retrofit retrofit = null;
    private static Retrofit enveuRetrofit = null;
    private static Retrofit subscriptionRetrofit = null;
    private static Retrofit searchRetrofit = null;
    private static Retrofit configRetrofit = null;
    private static Retrofit recoClickRetrofit = null;
    static String baseUrl;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getEnveuClient() {
        if (enveuRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            enveuRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getOVP_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return enveuRetrofit;
    }

    public static Retrofit getConfigClient() {
        if (configRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.API_KEY_MOB);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            configRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.CONFIG_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return configRetrofit;
    }

    public static Retrofit getRecoClickClient() {
        if (recoClickRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.API_KEY_MOB);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            recoClickRetrofit = new Retrofit.Builder()
                    .baseUrl("https://media-post.recosenselabs.com/v1.1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return recoClickRetrofit;
    }


    public static Retrofit getEnveuSubscriptionClient() {
        if (subscriptionRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            subscriptionRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return subscriptionRetrofit;
    }

    public static Retrofit getEnveuLogoutClient(String token) {
        if (subscriptionRetrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder().addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey()).addHeader("x-auth",token);
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            subscriptionRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return subscriptionRetrofit;
    }


    public static Retrofit getClientInterceptor(final String token) {
        Logger.e("TOKEN", ""+token);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }


        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(SDKConfig.getInstance().getOVP_BASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getUserInteration(final String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit redeemCoupon(final String token) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(SDKConfig.getInstance().getCoupon_BASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit paymentClient(final String token,String paymentURL) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json")
                    .addHeader("x-platform", " android")
                    .addHeader("x-auth", token)
                    .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(paymentURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


    public static Retrofit getClientSearch() {
        if (searchRetrofit==null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG)
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if(HttpProfiler.getInstance().needHttpProfiler()){
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
                }
            }

            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("x-api-key", SDKConfig.getInstance().getOvpApiKey());
                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
            httpClient.readTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .cache(null)
                    .addInterceptor(loggingInterceptor);

            OkHttpClient client = httpClient.build();

            searchRetrofit = new Retrofit.Builder()
                    .baseUrl(SDKConfig.getInstance().getSEARCH_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();

        }
        return searchRetrofit;

    }

    public static Retrofit getClientHeader() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if(HttpProfiler.getInstance().needHttpProfiler()){
            if (BuildConfig.DEBUG) {
                httpClient.addInterceptor(HttpProfiler.getInstance().getOkHttpProfilerInterceptor());
            }
        }

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", " application/json");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        OkHttpClient client = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


}
