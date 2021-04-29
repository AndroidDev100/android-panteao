package panteao.make.ready.utils.helpers;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor
        implements Interceptor {


    @Override
    public Response intercept(@NonNull Chain chain)
            throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("x-platform", "android")
                .addHeader("x-auth-token",
                        "")
                /*    .removeHeader("User-Agent")*/
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
                .build();
        return chain.proceed(request);
    }
}