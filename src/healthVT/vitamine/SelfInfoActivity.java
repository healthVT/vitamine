package healthVT.vitamine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
    TextView saveButton;
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


        //Save Button
        LinearLayout saveButtonLayout = (LinearLayout) findViewById(R.id.nextLayout);
        saveButton = (TextView) findViewById(R.id.nextButton);
        saveButton.setText("Save");
        saveButtonLayout.setVisibility(View.VISIBLE);

        user = new User(this);
        try{

            JSONObject userInfo = user.getUserInfo().getJSONObject("user");

            if(userInfo.getString("email") != null){
                emailEdit.setText(userInfo.getString("email"));
            }
            if(userInfo.getString("name") != null){
                nameEdit.setText(userInfo.getString("name"));
            }
            if(userInfo.getInt("age") != 0){
                ageEdit.setText(userInfo.getString("age"));
            }
            if(userInfo.getInt("height") != 0){
                heightEdit.setText(userInfo.getString("height"));
            }
            if(userInfo.getInt("weight") != 0){
                weightEdit.setText(userInfo.getString("weight"));
            }
            if(userInfo.getString("ethnicity") != null){
                ethnicityEdit.setText(userInfo.getString("ethnicity"));
            }
            if(userInfo.getString("gender") != null){
                if(userInfo.getString("gender").equals("MALE")){
                    maleRadioButton.setChecked(true);
                }else{
                    femaleRadioButton.setChecked(true);
                }
            }

        }catch(Exception e){
            Log.e("Getting error on User information", e.toString());
        }

        attachEvent();

        new NavigationActivityListener().listener(this, "icon_profile");
    }

    private void attachEvent(){

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveButton.setText("Updating");
                //blink(saveButton);
                saveInfo();

            }
        });
    }

    private void blink(final View button){

        SelfInfoActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(SelfInfoActivity.this, R.anim.fade_in);
                saveButton.startAnimation(fadeInAnimation);
            }
        });



    }

    private void saveInfo(){

        String gender = null;
        if(maleRadioButton.isChecked()){
            gender = "MALE";
        }else{
            gender = "FEMALE";
        }
        boolean result = user.updateUserInfo(emailEdit.getText().toString(), nameEdit.getText().toString(),
                ageEdit.getText().toString(), gender, heightEdit.getText().toString(),
                weightEdit.getText().toString(), ethnicityEdit.getText().toString());

        if(!result){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        saveButton.setText("Save");
    }
}
