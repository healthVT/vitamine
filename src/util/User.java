package util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import healthVT.vitamine.MyActivity;
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

                sharedData.edit().putString("email", email).apply();
                sharedData.edit().putString("password", password).apply();

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

                    sharedData.edit().putString("token", resultJSON.getString("token")).apply();
                    sharedData.edit().putString("email", email).apply();
                    sharedData.edit().putString("password", password).apply();

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

    public boolean logout(){
        try{
            vitamineServer server = new vitamineServer(context);
            server.execute("api/logout").get();


            sharedData.edit().putString("token", null).commit();
            sharedData.edit().putString("email", null).commit();
            sharedData.edit().putString("password", null).commit();

            Intent intent = new Intent(context, MyActivity.class);
            context.startActivity(intent);

            return true;

        }catch(Exception e){
            Log.e("Logout error", "Error on logout", e);
            return false;
        }
    }

    public boolean updateUserInfo(String email, String name, String age, String gender, String height, String weight, String ethnicity){
        String params = "name=" + name + "&age=" + age + "&height=" + height + "&weight=" + weight + "&gender=" + gender + "&ethnicity=" + ethnicity + "&email=" + email;
        try{
            vitamineServer server = new vitamineServer(context);
            JSONObject resultJSON = server.execute("user/updateUserInfo/?" + params).get();

            if (resultJSON.get("success").toString().equals("true")) {
                return true;
            } else {
                return false;
            }

        }catch(Exception e){
            Log.e("Error on update user info", e.toString());

            return false;
        }
    }

    public JSONObject getUserInfo(){
        JSONObject resultJSON = null;
        try{
            vitamineServer server = new vitamineServer(context);
            resultJSON = server.execute("user/getUserInfo").get();
        }catch(Exception e){
            Log.e("Error on Getting user information", e.toString());
        }

        return resultJSON;
    }

    public String getErrorMessage() {return errorMessage;}
    public Boolean getReturnUser() { return returnUser; }
    public Boolean hasToken(){ return token != null;}
}
