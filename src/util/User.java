package util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import healthVT.vitamine.DailyActivity;
import org.json.JSONObject;

/**
 * Created by Jay on 8/9/14.
 */
public class User {

    private String email, password, token;
    private SharedPreferences sharedData;
    private String errorMessage;
    private boolean returnUser = false;
    private Context context;

    public User(Context context){
        sharedData = context.getSharedPreferences("Foodmula", Context.MODE_PRIVATE);
        this.context = context;
        if(sharedData.getString("email", null) != null){
            email = sharedData.getString("email", null);
            password = sharedData.getString("password", null);
            token = sharedData.getString("token", null);
            returnUser = true;
            Log.d("Returning User", "true");
        }
    }

    public boolean login(String email, String password){
        this.email = email;
        this.password = password;

        return login();
    }

    public boolean register(String email, String password, String name, String age, String gender, String height, String weight, String ethnicity){

        String params = "register=true&name=" + name + "&age=" + age + "&height=" + height + "&weight=" + weight + "&gender=" + gender + "&ethnicity=" + ethnicity + "&email=" + email + "&password=" + password;
        try{
            vitamineServer server = new vitamineServer(context);
            JSONObject resultJSON = server.execute("user/loginOrRegister/?" + params).get();

            if (resultJSON.get("success").toString().equals("true")) {

                sharedData.edit().putString("email", email).commit();
                sharedData.edit().putString("password", password).commit();

                return login();
            } else {
                return false;
            }

        }catch(Exception e){}

        return false;
    }

    public boolean login(){
        try{

            if(!hasToken()){
                vitamineServer server = new vitamineServer(context);
                JSONObject resultJSON = server.execute("login?" + "email=" + email + "&password=" + password).get();

                if (resultJSON.getString("token") != null) {

                    sharedData.edit().putString("token", resultJSON.getString("token")).commit();
                    sharedData.edit().putString("email", email).commit();
                    sharedData.edit().putString("password", password).commit();

                    Log.d("TOKEN", sharedData.getString("token", null));
                    return true;
                } else {
                    errorMessage = "Please check your email and password.";
                    return false;
                }
            }else{
                return true;
            }


        }catch(Exception e){
            Log.e("Login error", "Error on login", e);
            return false;
        }
    }

    public String getErrorMessage() {return errorMessage;}
    public Boolean getReturnUser() { return returnUser; }
    public Boolean hasToken(){ return token != null;}
}
