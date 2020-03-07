package com.example.cse110_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.cse110_project.Firebase.ProposeWalkCollection;

import java.util.HashMap;
import java.util.Map;

public class ProposeWalkScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.propose_walk_screen);

        Button submit = (Button) findViewById(R.id.submitBtn);
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProposeWalkCollection pwc = new ProposeWalkCollection();

                Map<String, Object> info = new HashMap<String, Object>();

                pwc.getTeamID(deviceID, info);

//                finish();
            }
        });

    }
}
