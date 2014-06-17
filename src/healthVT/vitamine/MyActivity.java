package healthVT.vitamine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import org.json.JSONObject;
import util.vitamineServer;
import util.tools;

public class MyActivity extends Activity implements Animation.AnimationListener {
    /**
     * Called when the activity is first created.
     */

    private TextView continueText, errorMessage, startText;
    private RelativeLayout informationExtensionLayout;
    private LinearLayout startButtonLayout;
    private Spinner ethnicityField;
    private EditText emailField, passwordField;
    private Animation animRotate;
    private Handler m_handler;
    private Runnable m_handlerTask;
    //private RotationAwareTask task;

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
        startText = (TextView) findViewById(R.id.startText);

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

        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        animRotate.setAnimationListener(this);

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
                startButtonAnim(true);
                loginOrRegister();
            }
        });

    }

    private void loginOrRegister() {
        if (!checkInput()) {
            startButtonAnim(false);
            return;
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

        vitamineServer server = new vitamineServer();
        try {
            JSONObject resultJSON = server.execute("user/loginOrRegister/?" + params).get();
            Log.d("result", resultJSON.get("success").toString());
            if (resultJSON.get("success").toString().equals("true")) {
                Log.d("login", "true");
                login();

            } else {
                errorMessage.setText(resultJSON.get("message").toString());
            }

            startButtonAnim(false);

        } catch (Exception e) {
            Log.e("Server Error ", "Server Error", e);
        }


    }

    private void login() {
        Log.d("Login", "success");
        Intent intent = new Intent(MyActivity.this, DailyActivity.class);
        startActivityForResult(intent, 0);
    }

    private void startButtonAnim(boolean loading) {
        if (loading) {
            startAnim();
        } else {
            endAnim();
        }
    }

    private void startAnim() {
        Log.d("start anim", "true");
        //task.execute();

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startText.setText("Login");
                        startButtonLayout.startAnimation(animRotate);
                    }
                });
            }
        }).start();
    }

    private void endAnim() {
//        m_handlerTask = new Runnable() {
//            @Override
//            public void run() {
//                startText.setText("Start");
//                startButtonLayout.clearAnimation();
//            }
//        };
//
//        m_handlerTask.run();

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

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animRotate) {
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

//    static class RotationAwareTask extends AsyncTask<Void, Void, Void> {
//        MyActivity activity=null;
//
//        RotationAwareTask(MyActivity activity) {
//            attach(activity);
//        }
//
//        @Override
//        protected Void doInBackground(Void... unused) {
//            activity.startText.setText("Start");
//            activity.startButtonLayout.clearAnimation();
//
//            return(null);
//        }
//
//        void attach(MyActivity activity) {
//            this.activity=activity;
//        }
//
//    }
}

