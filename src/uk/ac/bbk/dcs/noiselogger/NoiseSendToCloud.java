package uk.ac.bbk.dcs.noiselogger;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by allanw on 15/04/2015.
 */
public class NoiseSendToCloud extends AsyncTask<String, Void, Integer> {

    private static final String LOG_TAG = "NoiseLogger";

    protected Integer doInBackground(String... urls) {
        String url = "https://api.thingspeak.com/update";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        List<NameValuePair> urlParams = new ArrayList<NameValuePair>();
        urlParams.add(new BasicNameValuePair("key", "WIX5V1FL27ITZJKX"));
        urlParams.add(new BasicNameValuePair("field3", "testing123"));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParams));
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "setEntity() failed");
        }

        try {
            HttpResponse response = client.execute(post);
            return response.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            Log.e(LOG_TAG, "client.execute() failed");
            return 0;
        } catch (IOException e) {
            Log.e(LOG_TAG, "client.execute() failed - IO Exception");
            return 0;
        }
    }
}
