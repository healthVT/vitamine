package util;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * Created by Jay on 4/29/14.
 */
public class vitamineServer extends AsyncTask<String, Integer, JSONObject> {
    protected final String host = "http://www.jjtemp.com/projectVTServer/";
    //protected final String host = "http://10.0.2.2:8080/projectVTServer/";

    protected JSONObject doInBackground(String... urls){
        JSONObject json = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI serverAPI = new URI(host + urls[0]);
            request.setURI(serverAPI);
            HttpResponse response = httpclient.execute(request);

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String result = in.readLine();
            json = new JSONObject(result);

        }catch(Exception e){

            Log.e("Http Request ", "Exception", e);
        }

        return json;
    }

}
