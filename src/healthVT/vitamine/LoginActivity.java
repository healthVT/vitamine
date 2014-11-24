package healthVT.vitamine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import util.TitleFunction;
import util.User;
import util.tools;

/**
 * Created by Jay on 11/9/2014.
 */
public class LoginActivity extends TitleBarActivity implements Animation.AnimationListener {

    TextView emailField, passwordField, startButton, errorMessage;
    Animation flipRight, flipLeft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        new TitleFunction(this, false);

        emailField = (TextView) findViewById(R.id.loginEmailField);
        passwordField = (TextView) findViewById(R.id.loginPasswordField);
        startButton = (TextView) findViewById(R.id.startButton);
        errorMessage = (TextView) findViewById(R.id.loginErrorMessage);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorMessage.setText("");
                if(startButton.isEnabled()){
                    startButton.setEnabled(false);
                    flipAnim();
                    login();
                }

            }
        });
    }

    public void flipAnim(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flipRight = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.flip_right_in);
                flipRight.setAnimationListener(LoginActivity.this);

                flipLeft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.from_middle);
                flipLeft.setAnimationListener(LoginActivity.this);

                startButton.startAnimation(flipRight);
            }
        });


    }

    public void login(){
        final String email = emailField.getText().toString();
        final String password = passwordField.getText().toString();

        if(checkInput(email, password)){
            final User user = new User(this);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(user.login(email, password)){
                        Intent dailyIntent = new Intent(LoginActivity.this, DailyActivity.class);
                        startActivity(dailyIntent);
                        finish();
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startButton.setEnabled(true);
                                flipAnim();
                                errorMessage.setText("Wrong Email or Password.");
                            }
                        });

                    }
                }
            }).start();


        }else{
            startButton.setEnabled(true);
            flipAnim();
        }

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

    @Override
    public void onAnimationEnd(Animation anim){
        if (anim == flipRight) {

            if (!startButton.isEnabled()) {

                startButton.setText("Connecting");

            } else {

                startButton.setText("Sign-in");

            }

            startButton.clearAnimation();

            startButton.setAnimation(flipLeft);

            startButton.startAnimation(flipLeft);

        } else {
            startButton.setEnabled(true);
        }
    }

    @Override
    public void onAnimationRepeat(Animation anim){

    }

    @Override
    public void onAnimationStart(Animation anim){

    }
}
