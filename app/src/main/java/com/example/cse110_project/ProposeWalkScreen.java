package com.example.cse110_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ProposeWalkScreen extends AppCompatActivity {

    private EditText routeName;
    private EditText hour;
    private EditText minute;
    private EditText month;
    private EditText day;

    public static final String FORM_VALIDATION_SUCCESSFUL = "SUCCESS";
    public static final String FORM_VALIDATION_INPUT_NULL = "Please fill out all fields.";
    public static final String FORM_VALIDATION_RN_BLANK = "Route name can't be blank";
    public static final String FORM_VALIDATION_HR_OUT_OF_RANGE = "Hour needs to be a number between 0-23";
    public static final String FORM_VALIDATION_MIN_OUT_OF_RANGE = "Minute needs to be a number between 0-59";
    public static final String FORM_VALIDATION_MON_OUT_OF_RANGE = "Month needs to be a number between 1-12";
    public static final String FORM_VALIDATION_DAY_OUT_OF_RANGE_ONE = "Day needs to be a number between 1-31";
    public static final String FORM_VALIDATION_DAY_OUT_OF_RANGE_TWO = "Day needs to be a number between 1-30";
    public static final String FORM_VALIDATION_DAY_OUT_OF_RANGE_THREE = "Day needs to be a number between 1-29";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_walk_screen);

        Button submit = (Button) findViewById(R.id.submitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                routeName = (EditText) findViewById(R.id.routeName);
                hour = (EditText) findViewById(R.id.hour);
                minute = (EditText) findViewById(R.id.minute);
                month = (EditText) findViewById(R.id.month);
                day = (EditText) findViewById(R.id.day);

                String res = validateFormInput(routeName, hour, minute, month, day);

                finish();
            }
        });

    }

    private String validateFormInput(EditText routeName, EditText hour, EditText minute, EditText month, EditText day) {
        if(routeName == null || hour == null || minute == null || month == null || day == null) {
            return FORM_VALIDATION_INPUT_NULL;
        }

        String rName;
        int hr;
        int min;
        int mon;
        int d;

        try {

            rName = routeName.getText().toString();
            hr = Integer.parseInt(hour.getText().toString());
            min = Integer.parseInt(minute.getText().toString());
            mon = Integer.parseInt(month.getText().toString());
            d = Integer.parseInt(day.getText().toString());

            if (rName.length() == 0) { return FORM_VALIDATION_RN_BLANK; }
            if (hr < 0 || hr >= 24) { return FORM_VALIDATION_HR_OUT_OF_RANGE; }
            if (min < 0 || min >= 60 ) { return FORM_VALIDATION_MIN_OUT_OF_RANGE; }
            if (mon < 1 || mon > 12) { return FORM_VALIDATION_MON_OUT_OF_RANGE; }
            if (mon == 2) {
                if (d < 1 || d > 29 ) { return FORM_VALIDATION_DAY_OUT_OF_RANGE_THREE; }
            }
            else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
                if (d < 1 || d > 30 ) { return FORM_VALIDATION_DAY_OUT_OF_RANGE_TWO; }
            }
            else {
                if (d < 1 || d >= 31) { return FORM_VALIDATION_DAY_OUT_OF_RANGE_ONE; }
            }

        } catch (Exception e){
            return FORM_VALIDATION_INPUT_NULL;
        }

        return FORM_VALIDATION_SUCCESSFUL;
    }
}
