package healthVT.vitamine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import org.json.JSONObject;
import util.NavigationActivityListener;
import util.User;
import util.vitamineServer;

/**
 * Created by Jay on 11/8/2014.
 */
public class SelfInfoActivity extends TitleBarActivity {

    EditText emailEdit, passwordEdit, nameEdit, ageEdit, heightEdit, weightEdit, ethnicityEdit;
    RadioButton maleRadioButton, femaleRadioButton;
    TextView saveButton, logoutButton;
    User user;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self);

        //All of edit text
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        heightEdit = (EditText) findViewById(R.id.heightEdit);
        weightEdit = (EditText) findViewById(R.id.weightEdit);
        ethnicityEdit = (EditText) findViewById(R.id.ethnicityEdit);
        maleRadioButton = (RadioButton) findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton) findViewById(R.id.femaleRadioButton);
        logoutButton = (TextView) findViewById(R.id.logoutButton);

        //Save Button
        LinearLayout saveButtonLayout = (LinearLayout) findViewById(R.id.nextLayout);
        saveButton = (TextView) findViewById(R.id.nextButton);
        saveButton.setText("Save");
        saveButtonLayout.setVisibility(View.VISIBLE);

        user = new User(this);
        try {

            JSONObject userInfo = user.getUserInfo().getJSONObject("user");

            if (userInfo.getString("email") != null) {
                emailEdit.setText(userInfo.getString("email"));
            }
            if (userInfo.getString("name") != null) {
                nameEdit.setText(userInfo.getString("name"));
            }
            if (userInfo.getInt("age") != 0) {
                ageEdit.setText(userInfo.getString("age"));
            }
            if (userInfo.getInt("height") != 0) {
                heightEdit.setText(userInfo.getString("height"));
            }
            if (userInfo.getInt("weight") != 0) {
                weightEdit.setText(userInfo.getString("weight"));
            }
            if (userInfo.getString("ethnicity") != null) {
                ethnicityEdit.setText(userInfo.getString("ethnicity"));
            }
            if (userInfo.getString("gender") != null) {
                if (userInfo.getString("gender").equals("MALE")) {
                    maleRadioButton.setChecked(true);
                } else {
                    femaleRadioButton.setChecked(true);
                }
            }

        } catch (Exception e) {
            Log.e("Getting error on User information", e.toString());
        }

        attachEvent();

        new NavigationActivityListener().listener(this, "icon_profile");
    }

    private void attachEvent() {

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blink(saveButton);
                saveInfo();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.logout();
            }
        });
    }

    private void blink(final View button) {
        processStatusChangeAnimation("Updating");
    }

    private void saveInfo() {
        saveButton.setClickable(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String gender = null;
                if (maleRadioButton.isChecked()) {
                    gender = "MALE";
                } else {
                    gender = "FEMALE";
                }
                boolean result = user.updateUserInfo(emailEdit.getText().toString(), nameEdit.getText().toString(),
                        ageEdit.getText().toString(), gender, heightEdit.getText().toString(),
                        weightEdit.getText().toString(), ethnicityEdit.getText().toString());

                if (!result) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelfInfoActivity.this);
                            builder.setMessage("Update failed. Please try again later.")
                                    .setTitle("Error");

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    });

                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            processStatusChangeAnimation("Complete");
                            Thread.sleep(1000);
                            processStatusChangeAnimation("Save");
                            saveButton.setClickable(true);
                        } catch (Exception e) {

                        }

                    }
                }).start();


            }
        }).start();
    }

    public void processStatusChangeAnimation(final String textChangeTo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation fadeOut = getBlinkAnim(200, 0, 0);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }

                    @Override
                    public void onAnimationEnd(Animation arg0) {
                        saveButton.setText(textChangeTo);
                        Animation fadeIn = getBlinkAnim(200, 0, 1);
                        saveButton.startAnimation(fadeIn);
                    }
                });

                saveButton.startAnimation(fadeOut);
            }
        });
    }

    public Animation getBlinkAnim(long duration, int time, int model) {
        final Animation animation;
        if (model == 0) {
            animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        } else {
            animation = new AlphaAnimation(0, 1); // Change alpha from fully visible to invisible
        }

        animation.setDuration(duration); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setFillAfter(true);
        if (time > 0) {
            animation.setRepeatCount(time); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        }


        return animation;
    }
}
