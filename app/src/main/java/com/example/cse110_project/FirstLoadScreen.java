package com.example.cse110_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstLoadScreen extends AppCompatActivity {

    private Button getStartedBtn;
    private EditText firstName;
    private EditText lastName;
    private EditText heightFt;
    private EditText heightInch;
    private String fitnessServiceKey = "GOOGLE_FIT";



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


                boolean res = validateFormInput(firstName, lastName, heightFt, heightInch);

                // TODO: More informative toast message
                if (!res) {
                    Context context = getApplicationContext();
                    CharSequence text = "Invalid Form Input!";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    finish();
                }
            }
        });


    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    /*
     * Function: validateFormInput
     * Description: Validate Form Input, return error messages
     * TODO: return error messages
     */
    private boolean validateFormInput(EditText firstName, EditText lastName, EditText heightFt, EditText heightInch){

        if(firstName == null || lastName == null || heightFt == null || heightInch == null) {
            return false;
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

            if (fName.length() == 0) { return false; }
            if (lName.length() == 0) { return false; }

            if (ft <= 0 || ft > 8) { return false; }
            if (inch < 0 || inch > 11 ) { return false; }


        } catch (Exception e){
            return false;
        }

        AccessSharedPrefs.setUserInfo(this, fName, lName, ft, inch);

        Toast.makeText(FirstLoadScreen.this, "Saved", Toast.LENGTH_SHORT).show();

        return true;

    }

}
