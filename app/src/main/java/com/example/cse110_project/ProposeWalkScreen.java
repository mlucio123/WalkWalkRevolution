package com.example.cse110_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse110_project.Firebase.ProposeWalkCollection;
import com.example.cse110_project.utils.AccessSharedPrefs;
import com.example.cse110_project.utils.Route;
import com.google.firebase.firestore.FieldValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProposeWalkScreen extends AppCompatActivity {

    private EditText walkName;
    private EditText timeHr;
    private EditText timeMin;

    private EditText dateMonth;
    private EditText dateDay;
    private EditText dateYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_walk_screen);

        Button submit = (Button) findViewById(R.id.submitBtn);
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        walkName = findViewById(R.id.walkName);
        timeHr = findViewById(R.id.hour);
        timeMin = findViewById(R.id.minute);

        dateMonth = findViewById(R.id.month);
        dateDay = findViewById(R.id.day);
        dateYear = findViewById(R.id.year);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String res = validate();

                if(res.equals("SUCCESS")) {

                    ProposeWalkCollection pwc = new ProposeWalkCollection();

                    Map<String, Object> info = new HashMap<String, Object>();

                    Intent intent = getIntent();
                    String routeID = intent.getStringExtra("routeID");
                    String routeStart = intent.getStringExtra("routeStart");

                    info.put("timestamp", FieldValue.serverTimestamp());
                    info.put("walkingName", walkName.getText().toString());
                    info.put("day", dateDay.getText().toString());
                    info.put("month", dateMonth.getText().toString());
                    info.put("year", dateYear.getText().toString());
                    info.put("hour", timeHr.getText().toString());
                    info.put("minute", timeMin.getText().toString());
                    info.put("isScheduled", false);
                    String[] empty = new String[0];
                    info.put("accept", Arrays.asList(deviceID));
                    info.put("declineBadTime", Arrays.asList());
                    info.put("declineBadRoute", Arrays.asList());
                    info.put("proposedBy", deviceID);
                    info.put("routeID", routeID);
                    info.put("startPosition", routeStart);

                    pwc.getTeamID(deviceID, info, "propose");

                    Toast.makeText(ProposeWalkScreen.this, "You proposed a walk!", Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(ProposeWalkScreen.this, TeamScreen.class);
                    startActivity(intent2);

                }  else {
                    Toast.makeText(ProposeWalkScreen.this, res, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private String validate() {

        try {
            String walkNameString = walkName.getText().toString();

            String timeHrString = timeHr.getText().toString();
            String timeMinString = timeMin.getText().toString();

            String dateMonthString = dateMonth.getText().toString();
            String dateDayString = dateDay.getText().toString();
            String dateYearString = dateYear.getText().toString();

            if (walkNameString.length() == 0) {
                return "Please Enter a walk name";
            }

            int hr = Integer.parseInt(timeHrString);
            if (hr < 0 || hr > 23) {
                return "Only valid hours are 0-23";
            }

            int min = Integer.parseInt(timeMinString);
            if (min < 0 || min > 59) {
                return "Only valid minutes are 0-59";
            }

            int day = Integer.parseInt(dateDayString);
            int month = Integer.parseInt(dateMonthString);
            int year = Integer.parseInt(dateYearString);

            if (day < 1 || day > 31) {
                return "Day should be between 1 to 31";
            }

            if (month < 1 || month > 12) {
                return "Month should be between 1 to 12";
            }

            if (year < 2020) {
                return "Cannot set a year that has passed";
            }


        } catch(Exception e) {
            return "Error with Form, make sure everything is filled";
        }

        return "SUCCESS";

    }



}
