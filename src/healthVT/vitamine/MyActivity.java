package healthVT.vitamine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import org.json.JSONObject;
import util.CircleAnimView;
import util.vitamineServer;
import util.tools;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private TextView continueText, errorMessage, startText;
    private RelativeLayout informationExtensionLayout;
    private LinearLayout startButtonLayout;
    private Spinner ethnicityField;
    private EditText emailField, passwordField;
    private CircleAnimView startSurfaceView;
    private vitamineServer server;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        TextView titleText = (TextView) findViewById(R.id.titleBar);
        TextView loginText = (TextView) findViewById(R.id.loginText);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "Lobster.ttf");
        Typeface demiFont = Typeface.createFromAsset(getAssets(), "demi.ttf");
        titleText.setTypeface(titleFont);
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

    }

    private void attachListener() {
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
                Log.d("Click on", "Start");
                Intent dailyIntent = new Intent(MyActivity.this, DailyActivity.class);
                startActivity(dailyIntent);
//                startSurfaceView.setAnimStart();
//                if(!loginOrRegister()){
//                    startSurfaceView.stopAnim();
//
//                }
            }
        });

    }

    private boolean loginOrRegister() {
        if (!checkInput()) {
            return false;
        }

        Log.d("login or register", "after check input");

        String params = "";
        if (continueText.getVisibility() == View.GONE) {
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
            params = "register=true&name=" + name + "&age=" + age + "&height=" + height + "&weight=" + weight + "&gender=" + gender + "&ethnicity=" + ethnicity + "&";
        }

        params += "email=" + emailField.getText() + "&password=" + passwordField.getText();

        Log.d("login or register", "params: " + params);
        server = new vitamineServer();

        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try{
                        JSONObject resultJSON = server.execute("user/loginOrRegister/?" + "email=" + emailField.getText() + "&password=" + passwordField.getText()).get();
                        Log.d("result", resultJSON.get("success").toString());
                        startSurfaceView.stopAnim();
                        if (resultJSON.get("success").toString().equals("true")) {
                            Log.d("login", "true");
                            Intent dailyIntent = new Intent(MyActivity.this, DailyActivity.class);
                            startActivity(dailyIntent);
                            finish();
                        } else {
                            errorMessage.setText(resultJSON.get("message").toString());
                        }

                    }catch(Exception e){}
                }
            });

        } catch (Exception e) {
            Log.e("Server Error ", "Server Error", e);
        }

        return true;
    }

    private void login() {
        Log.d("Login", "success");
        Intent intent = new Intent(MyActivity.this, DailyActivity.class);
        startActivityForResult(intent, 0);
    }

    private boolean checkInput() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

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


}

