package com.example.cse110_project;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110_project.Firebase.UserCollection;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.example.cse110_project.utils.User;


public class FirstLoadScreen extends AppCompatActivity {

    private Button getStartedBtn;
    private EditText firstName;
    private EditText lastName;
    private EditText heightFt;
    private EditText heightInch;
    private EditText gmail;

    // Error msg for form validation
    public static final String FORM_VALIDATION_SUCCESSFUL = "SUCCESS";
    public static final String FORM_VALIDATION_INPUT_NULL = "Please fill out all fields.";
    public static final String FORM_VALIDATION_FN_BLANK = "First name can't be blank";
    public static final String FORM_VALIDATION_LN_BLANK = "Last name can't be blank";
    public static final String FORM_VALIDATION_FT_OUT_OF_RANGE = "Feet needs to be a number between 0-8";
    public static final String FORM_VALIDATION_INCH_OUT_OF_RANGE = "Inch needs to be a number between 0-11";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.first_load_form);

        getStartedBtn = (Button) findViewById(R.id.getStartedBtn);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = (EditText) findViewById(R.id.userFirstName);
                lastName = (EditText) findViewById(R.id.userLastName);
                heightFt = (EditText) findViewById(R.id.userHeightFt);
                heightInch = (EditText) findViewById(R.id.userHeightInch);
                gmail = (EditText) findViewById(R.id.emailEntry);
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                String res = validateFormInput(firstName, lastName, heightFt, heightInch);

                // TODO: More informative toast message
                if (res == "SUCCESS") {
                    User user = new User(deviceID, gmail.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                    UserCollection uc = new UserCollection();
                    user.setHeightFt(heightFt.getText().toString());
                    user.setHeightInch(heightInch.getText().toString());
                    uc.addUser(user);
                    Toast toast = Toast.makeText(getApplicationContext(), "Successfully post to firebase", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = res;
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });


    }


    /*
     * Function: validateFormInput
     * Description: Validate Form Input, return error messages
     */
    private String validateFormInput(EditText firstName, EditText lastName, EditText heightFt, EditText heightInch){

        if(firstName == null || lastName == null || heightFt == null || heightInch == null) {
            return FORM_VALIDATION_INPUT_NULL;
        }

        String fName;
        String lName;
        int ft;
        int inch;

        try {

            fName = firstName.getText().toString();
            lName = lastName.getText().toString();

            ft = Integer.parseInt(heightFt.getText().toString());
            inch = Integer.parseInt(heightInch.getText().toString());

            if (fName.length() == 0) { return FORM_VALIDATION_FN_BLANK; }
            if (lName.length() == 0) { return FORM_VALIDATION_LN_BLANK; }

            if (ft <= 0 || ft > 8) { return FORM_VALIDATION_FT_OUT_OF_RANGE; }
            if (inch < 0 || inch > 11 ) { return FORM_VALIDATION_INCH_OUT_OF_RANGE; }


        } catch (Exception e){
            return FORM_VALIDATION_INPUT_NULL;
        }

        AccessSharedPrefs.setUserInfo(this, fName, lName, ft, inch);

        Toast.makeText(FirstLoadScreen.this, "Saved", Toast.LENGTH_SHORT).show();

        return FORM_VALIDATION_SUCCESSFUL;

    }

}
