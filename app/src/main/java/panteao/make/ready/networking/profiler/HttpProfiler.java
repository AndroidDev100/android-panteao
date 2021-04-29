package panteao.make.ready.networking.profiler;

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor;

public class HttpProfiler {

    private static HttpProfiler mInstance;

    private HttpProfiler() {
    }

    public static HttpProfiler getInstance(){
        if(mInstance == null){
            mInstance = new HttpProfiler();
        }
        return mInstance;
    }

    public OkHttpProfilerInterceptor getOkHttpProfilerInterceptor(){
        return new OkHttpProfilerInterceptor();
    }

    /***
     * If you want to use this http profiler Api in android studio then
     * @return true
     */
    public boolean needHttpProfiler(){
        return false;
    }
}
