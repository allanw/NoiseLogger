package uk.ac.bbk.dcs.noiselogger;

import android.media.MediaRecorder;
import android.os.Environment;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class NoiseRecorder extends Activity {
    private static final String LOG_TAG = "NoiseLogger";
    private static String mFileName = null;

    private Button startRecording;
    private boolean isRecording = true;

    private MediaRecorder mRecorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_recorder);
        startRecording = (Button)findViewById(R.id.record_button);
        startRecording.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isRecording) {
                    startRecording();
                    startRecording.setText("Stop recording");
                }
                else {
                    int max_amp = mRecorder.getMaxAmplitude();
                    System.out.println("Max amplitude");
                    System.out.println(max_amp);
                    stopRecording();

                    // Send to ThingSpeak
                    new NoiseSendToCloud().execute();

                    startRecording.setText("Record");
                }
                isRecording = !isRecording;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_noise_recorder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startRecording() {
        // code taken from http://developer.android.com/guide/topics/media/audio-capture.html
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        Log.wtf(LOG_TAG, mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    public NoiseRecorder() {
        // code taken from http://developer.android.com/guide/topics/media/audio-capture.html
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/noiselogger.3gp";
    }
}