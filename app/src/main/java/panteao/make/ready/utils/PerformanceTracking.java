package panteao.make.ready.utils;

import android.os.AsyncTask;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.HttpMetric;
import panteao.make.ready.utils.cropImage.helpers.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class PerformanceTracking extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {

        try {
            manualNetworkTrace();
            Logger.w("streamValues  --","inn"+"");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.w("streamValues  --",e.toString()+"");
        }

        return null;
    }

    @Override
    protected void onPostExecute(String resultData) {
        super.onPostExecute(resultData);
        try {
            JSONObject obj = new JSONObject(resultData);
            String name= obj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void manualNetworkTrace() throws Exception {
        byte[] data = "badgerbadgerbadgerbadgerMUSHROOM!".getBytes();

        // [START perf_manual_network_trace]
        HttpMetric metric =
                FirebasePerformance.getInstance().newHttpMetric("https://app.uat.enveu.com/app/api/v2/playlist?playlistId=6082265778001&page=0&size=5",
                        FirebasePerformance.HttpMethod.GET);
        final URL url = new URL("https://app.uat.enveu.com/app/api/v2/playlist?playlistId=6082265778001&page=0&size=5");
        metric.start();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        try {
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(data);
        } catch (IOException ignored) {
        }
        metric.setRequestPayloadSize(data.length);
        metric.setHttpResponseCode(conn.getResponseCode());

        Logger.w("streamValues  --",conn.getInputStream()+"");

        conn.disconnect();
        metric.stop();
        // [END perf_manual_network_trace]
    }

}