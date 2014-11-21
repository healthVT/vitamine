package healthVT.vitamine;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import org.json.JSONObject;
import util.User;
import util.vitamineServer;

import java.io.IOException;

public class MyActivity extends TitleBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {
    TextView loginAsGoogle, loginAsEmail, signupButton;

    /** Google+ Sign-in Flow **/
    /* Track whether the sign-in button has been clicked so that we know to resolve
        * all issues preventing sign-in without waiting.
        */
    private boolean mSignInClicked, mIntentInProgress;
    private static final int RC_SIGN_IN = 0;

    /* Store the connection result from onConnectionFailed callbacks so that we can
          * resolve them when the user clicks sign-in.
        */
    private ConnectionResult mConnectionResult;
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loginAsGoogle = (TextView) findViewById(R.id.loginAsGoogle);
        loginAsEmail = (TextView) findViewById(R.id.loginAsEmail);
        signupButton = (TextView) findViewById(R.id.signupButton);

        loginAsGoogle.setOnClickListener(this);
        loginAsEmail.setOnClickListener(this);
        signupButton.setOnClickListener(this);

        User user = new User(this);
        if(user.getReturnUser()){
            if(user.login()){
                Intent dailyIntent = new Intent(MyActivity.this, DailyActivity.class);
                startActivity(dailyIntent);
                finish();
            }
        }


    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.loginAsGoogle:
                mSignInClicked = true;
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
                mGoogleApiClient.connect();
                signInWithGplus();
                break;

            case R.id.loginAsEmail:
                Intent loginPage = new Intent(MyActivity.this, LoginActivity.class);
                startActivity(loginPage);
                break;
            case R.id.signupButton:
                Intent registerPage = new Intent(MyActivity.this, RegisterActivity.class);
                startActivity(registerPage);
                break;
        }
    }

    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            Log.d("Sign in with g", "is connecting false");

            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            Log.d("has result", "hhas result");
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            task.execute();

                        }catch(Exception e){
                            Log.e("error", "error", e);
                        }
                    }
                }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tokenReceived(final String token){
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person currentPerson = Plus.PeopleApi
                    .getCurrentPerson(mGoogleApiClient);

            final String personName;
            if(currentPerson.getName().getFormatted() == null){
                personName = currentPerson.getDisplayName();
            }else{
                personName = currentPerson.getName().getFormatted();
            }
            final String personPhotoUrl = currentPerson.getImage().getUrl();
            final String personGooglePlusProfile = currentPerson.getUrl();
            final String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            final String birthday = currentPerson.getBirthday();

            final String gender;
            switch(currentPerson.getGender()){
                case 0:
                    gender = "Male";
                    break;
                case 1:
                    gender = "Female";
                    break;
                case 2:
                    gender = "Other";
                    break;
                default:
                    gender = null;
                    break;
            }



            Log.e("Main Activity", "Name: " + personName + ", plusProfile: "
                    + personGooglePlusProfile + ", email: " + email
                    + ", Image: " + personPhotoUrl + ", Birthday: " + birthday + ", Gender: " + gender + ", Token: " + token);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    try{
                        vitamineServer server = new vitamineServer(MyActivity.this);
                        final JSONObject result = server.execute("socialMedia?socialToken=" + token + "email=" + email + "socialMedia=Google").get();
                        Log.d("result", result.toString());

                        if(result.isNull("username")){
                            Log.d("in here", "here");
                            Intent registerIntent = new Intent(MyActivity.this, RegisterActivity.class);

                            registerIntent.putExtra("email", email);
                            registerIntent.putExtra("name", personName);
                            registerIntent.putExtra("birthday", birthday);
                            registerIntent.putExtra("gender", gender);
                            registerIntent.putExtra("token", token);

                            startActivity(registerIntent);

                        }else{
                            //TODO Login
                        }
                    }catch(Exception e){
                        Log.e("Error ", "Server", e);
                    }

                }
            }).start();

        } else {
            Toast.makeText(getApplicationContext(),
                    "Person information is null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("failed", result.toString());
        if (!mIntentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks
            // 'sign-in'.
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg){
        Log.d("onConnected", "connected");
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }



    private void attachListener() {
/*
        continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueText.setVisibility(View.GONE);
                informationExtensionLayout.setVisibility(RelativeLayout.VISIBLE);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) startButtonLayout.getLayoutParams();
                params.topMargin = 10;
            }
        });

        startButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSurfaceView.setAnimStart();
                if(!loginOrRegister(emailField.getText().toString(), passwordField.getText().toString(), false)){
                    startSurfaceView.stopAnim();
                }
            }
        });
*/

    }

    private boolean loginOrRegister(final String email, final String password, boolean login) {
/*
        if (!checkInput(email, password)) {
            return false;
        }

        Log.d("login or register", "after check input");


        User user = new User(this);
        if (continueText.getVisibility() == View.GONE && !login) {
            String name = ((EditText) findViewById(R.id.nameField)).getText().toString();
            String age = ((EditText) findViewById(R.id.ageField)).getText().toString();
            String height = ((EditText) findViewById(R.id.heightField)).getText().toString();
            String weight = ((EditText) findViewById(R.id.weightField)).getText().toString();
            String ethnicity = ethnicityField.getSelectedItem().toString();
            String gender = null;

            RadioGroup genderRadio = (RadioGroup) findViewById(R.id.radioGroupGender);
            for (int i = 0; i < genderRadio.getChildCount(); i++) {
                RadioButton radio = (RadioButton) genderRadio.getChildAt(i);
                if (radio.isChecked()) {
                    gender = radio.getText().toString();
                }
            }
            boolean registered = user.register(email, password, name, age, gender, height, weight, ethnicity);
            if(registered){
                Log.d("REGISTERRRRRRR", "SUCCESS");

                login();
            }else{
                Log.d("REGISTERRRRRRR", "FAIL");

                errorMessage.setText(user.getErrorMessage());
            }

        }else{

            if(user.login(email, password)){
                Log.d("LOGIN", "SUCCESS");
                login();
            }else{
                Log.d("LOGIN", "FAIL");
                errorMessage.setText(user.getErrorMessage());
            }
        }
*/

        return true;
    }

/*
    private void login() {
        Log.d("Login", "success");
        Intent dailyIntent = new Intent(MyActivity.this, DailyActivity.class);
        startActivity(dailyIntent);
        finish();
    }

    private boolean checkInput(String email, String password) {
        if (email == null || email.length() < 5 || !tools.checkEmailFormat(email)) {
            errorMessage.setText("Please check your email format.");
            return false;
        }

        if (password == null || password.length() < 5) {
            errorMessage.setText("Please check your password, at least 5 length.");
            return false;
        }

        return true;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void toastMessage(Toast toast, String message) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
*/

    AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            String token = null;
            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

            try {

                final String scope = "oauth2:https://www.googleapis.com/auth/plus.login";
                token = GoogleAuthUtil.getToken(MyActivity.this,  email, scope);
                Log.d("TOken", token);
            } catch (IOException transientEx) {
                // Network or server error, try later
                Log.e("IOException", transientEx.toString());
            } catch (UserRecoverableAuthException e) {
                // Recover (with e.getIntent())
                Log.e("UserRecoverableAuthException", e.toString());

            } catch (GoogleAuthException authEx) {
                // The call is not ever expected to succeed
                // assuming you have already verified that
                // Google Play services is installed.
                Log.e("GoogleAuthException", authEx.toString());
            } catch(Exception e){
                Log.e("Exception", e.toString());
            }

            if(token != null){
                onPostExecute(token);
            }

            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            Log.d("Background", "Access token retrieved:" + token);
            if(token != null){
                tokenReceived(token);
            }


        }
    };

}



