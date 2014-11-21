package healthVT.vitamine;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import org.json.JSONObject;
import util.SpinnerAdapter;
import util.User;
import util.vitamineServer;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Jay on 11/15/2014.
 */
public class RegisterActivity extends TitleBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText ethnicityEdit, emailEdit, nameEdit, ageEdit, errorMessage, passwordEdit, heightEdit, weightEdit;
    TextView registerButton;
    Spinner ethnicitySpinner;
    RadioGroup genderGroup;
    String email, token, password, name, socialMedia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        ethnicityEdit = (EditText) findViewById(R.id.ethnicityEdit);
        ethnicitySpinner = (Spinner) findViewById(R.id.ethnicitySpinner);
        registerButton = (TextView) findViewById(R.id.registerButton);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        ageEdit = (EditText) findViewById(R.id.ageEdit);
        genderGroup = (RadioGroup) findViewById(R.id.genderGroup);
        errorMessage = (EditText) findViewById(R.id.errorMessage);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        heightEdit = (EditText) findViewById(R.id.heightEdit);
        weightEdit = (EditText) findViewById(R.id.weightEdit);

        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_row, Arrays.asList(getResources().getStringArray(R.array.ethnicityArray)));
        ethnicitySpinner.setAdapter(adapter);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        if(email != null){ emailEdit.setText(intent.getStringExtra("email")); }
        if(intent.getStringExtra("name") != null){ nameEdit.setText(intent.getStringExtra("name")); }
        if(intent.getStringExtra("birthday") != null){ ageEdit.setText(intent.getStringExtra("birthday")); }

        if(intent.getStringExtra("gender") != null && intent.getStringExtra("gender").equals("Male")){
            genderGroup.check(R.id.maleRadioButton);
        }else if(intent.getStringExtra("gender") != null && intent.getStringExtra("gender").equals("Female")){
            genderGroup.check(R.id.femaleRadioButton);
        }

        token = intent.getStringExtra("token");
        socialMedia = intent.getStringExtra("socialMedia");

        ethnicitySpinner.setOnItemSelectedListener(this);
        registerButton.setOnClickListener(this);
        ethnicityEdit.setOnClickListener(this);
        ageEdit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.registerButton:
                if(checkInput()){
                    register();
                }

                break;

            case R.id.ethnicityEdit:
                ethnicitySpinner.performClick();
                break;
            case R.id.ageEdit:
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "datePicker");
        }
    }

    private boolean checkInput() {
        email = emailEdit.getText().toString();
        password = passwordEdit.getText().toString();
        name = nameEdit.getText().toString();
        if (email == null || email.length() < 5 || !util.tools.checkEmailFormat(email)) {
            errorMessage.setText("Please check your email format.");
            return false;
        }

        if (password == null || password.length() < 5) {
            errorMessage.setText("Please check your password, at least 5 length.");
            return false;
        }

        if(name == null || name.length() < 2){
            errorMessage.setText("Please check your name, at least 2 length.");
            return false;
        }

        return true;
    }

    public void register(){
        try{

            String birthday = ageEdit.getTag().toString();
            int genderId = genderGroup.getCheckedRadioButtonId();
            String gender = "";
            if(genderId == R.id.maleRadioButton){
                gender = "Male";
            }else if(genderId == R.id.femaleRadioButton){
                gender = "Female";
            }
            String height = heightEdit.getText().toString();
            String weight = weightEdit.getText().toString();
            String ethnicity = ethnicityEdit.getText().toString();
            if(height == null) { height = ""; }
            if(weight == null) { weight = ""; }

            vitamineServer server = new vitamineServer(this);

            String url = "user/register?email=" + email + "&password=" + password + "&name=" + name + "&birthday=" + birthday + "&gender=" +
                    gender + "&height=" + height + "&weight=" + weight + "&ethnicity=" + ethnicity + "&socialToken=" + token + "&socialMedia=" + socialMedia;

            JSONObject result = server.execute(url).get();
            Log.d("Result", result.toString());
            if(result.getBoolean("success")){
                User user = new User(this);
                user.login(email, password);

                Intent dailyIntent = new Intent(RegisterActivity.this, DailyActivity.class);
                startActivity(dailyIntent);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),
                        "Error on register, please try again later.", Toast.LENGTH_LONG).show();
            }

        }catch(Exception e){
            Log.e("Error on register", "register", e);
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        ethnicityEdit.setText(parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }




}
