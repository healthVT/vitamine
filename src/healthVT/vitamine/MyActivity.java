package healthVT.vitamine;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import util.User;

public class MyActivity extends TitleBarActivity implements ConnectionCallbacks, OnConnectionFailedListener, View.OnClickListener {
    TextView loginAsGoogle, loginAsEmail;

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

        loginAsGoogle.setOnClickListener(this);
        loginAsEmail.setOnClickListener(this);

        User user = new User(this);
        if(user.getReturnUser()){
            if(user.login()){
                Intent dailyIntent = new Intent(MyActivity.this, DailyActivity.class);
                startActivity(dailyIntent);
                finish();
            }
        }


/*

        TextView loginText = (TextView) findViewById(R.id.loginText);
        Typeface demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        loginText.setTypeface(demiFont);


        continueText = (TextView) findViewById(R.id.continueLnk);
        informationExtensionLayout = (RelativeLayout) findViewById(R.id.informationExtensionLayout);
        startButtonLayout = (LinearLayout) findViewById(R.id.startButtonLayout);
        ethnicityField = (Spinner) findViewById(R.id.ethnicityField);
        //startText = (TextView) findViewById(R.id.startText);

        errorMessage = (TextView) findViewById(R.id.errorMessage);
        emailField = (EditText) findViewById(R.id.emailField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        //task = new RotationAwareTask(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ethnicityArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ethnicityField.setAdapter(adapter);
        attachListener();

        if (!isOnline()) {
            errorMessage.setText("Please connect to Internet first.");
        }


        //Circle Surface view
        startSurfaceView = (CircleAnimView) findViewById(R.id.surfaceView);
        startSurfaceView.setZOrderOnTop(true);
        SurfaceHolder holder = startSurfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);

        User user = new User(this);
        if(user.getReturnUser()){
            if(user.login()){
                login();
            }
        }
*/


    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            //mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.loginAsGoogle:
                mGoogleApiClient.connect();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_LOGIN).build();
                signInWithGplus();
                break;

            case R.id.loginAsEmail:
                Intent loginPage = new Intent(MyActivity.this, LoginActivity.class);
                startActivity(loginPage);
        }
    }

    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
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
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e("Main Activity", "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);


            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){
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


}

