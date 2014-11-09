package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import healthVT.vitamine.MyActivity;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Jay on 4/29/14.
 */
public class vitamineServer extends AsyncTask<String, Integer, JSONObject> {
    private SharedPreferences sharedData;
    private Context context;

//    protected final String host = "http://www.midawn.com/";
    protected final String host = "http://10.0.2.2:8080/projectVTServer/";

    public vitamineServer(Context context){
        this.context = context;
        sharedData = context.getSharedPreferences("Foodmula", Context.MODE_PRIVATE);
    }

    protected JSONObject doInBackground(String... urls){
        JSONObject json = null;
        try{
            String token = sharedData.getString("token", null);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost request = new HttpPost();
            Log.d("URL", host + urls[0]);
            URI serverAPI = new URI(host + urls[0]);
            request.addHeader("Accept", "application/json");
            if(token != null){
                Log.d("setting header", token);
                request.addHeader("X-Auth-Token", token);
            }

            request.setURI(serverAPI);
            HttpResponse response = httpclient.execute(request);

            if(response.getStatusLine().getStatusCode() == 403){
                Intent intent = new Intent(context, MyActivity.class);
                context.startActivity(intent);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String result = in.readLine();
            if(result == null || result.equals("")){
                JSONObject obj = new JSONObject();
                obj.put("success", false);
                sharedData.edit().putString("token", null).commit();
                return obj;
            }


            json = new JSONObject(result);

        }catch(Exception e){

            Log.e("Http Request ", "Exception", e);
        }

        return json;
    }

}
