package com.example.cse110_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cse110_project.fitness.FitnessService;
import com.example.cse110_project.fitness.FitnessServiceFactory;
import com.example.cse110_project.fitness.GoogleFitAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class RouteFormScreen extends AppCompatActivity {

    private Button favBtn;
    private Button easyBtn;
    private Button moderateBtn;
    private Button hardBtn;
    private Button cancelBtn;
    private Button submitBtn;

    private EditText routeName;
    private EditText startPosition;

    private String fitnessServiceKey = "GOOGLE_FIT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_form);

        routeName = findViewById(R.id.routeName);

        cancelBtn = findViewById(R.id.cancelBtn);
        submitBtn = findViewById(R.id.submitBtn);

        easyBtn = findViewById(R.id.easyBtn);
        moderateBtn = findViewById(R.id.moderateBtn);
        hardBtn = findViewById(R.id.hardBtn);

        favBtn = findViewById(R.id.favBtn);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO : FIX LOGIC AND SEND FAV TO BACKEND OR ROUTE OBJECT
                // TODO : THIS CURRENTLY REMOVES THE BORDER IN THE FAVORITE BUTTON

                if ( favBtn.getBackgroundTintList() == ColorStateList.valueOf( 0xFF000000 )){
                    favBtn.setBackgroundTintList( ColorStateList.valueOf( 0xFFFF0000 ) );
                } else {
                    favBtn.setBackgroundTintList( ColorStateList.valueOf( 0xFF000000 ) );
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteFormScreen.this, HomeScreen.class);
                intent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                startActivity(intent);
//                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (routeName.getText().toString() == null) {
                    Toast.makeText(RouteFormScreen.this, "You did not fill in route name.", Toast.LENGTH_SHORT).show();
                } else {

                    // TODO CREATE OBJ OF CORRESPONDING MESSAGES AND SEND TO FIREBASE

                    Intent intent = new Intent(RouteFormScreen.this, HomeScreen.class);
                    intent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                    startActivity(intent);
//                    finish();
                }
            }
        });
    }

}
