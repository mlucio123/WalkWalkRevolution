package com.example.cse110_project;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Distribution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class TeamNotificationScreen extends AppCompatActivity {

    private LinearLayout notifLayout;
    private LinearLayout noWalkLayout;
    private LinearLayout proposedWalkLayout;
    private Button acceptWalk;
    private Button badTImeDecline;
    private Button badRouteDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_notification_screen);

        noWalkLayout = findViewById(R.id.noWalkMessage);
        notifLayout = findViewById(R.id.notifcationLayout);
        proposedWalkLayout = findViewById(R.id.proposedWalkLayout);

        noWalkLayout.setVisibility(View.GONE);
        proposedWalkLayout.setVisibility(View.GONE);
        notifLayout.setVisibility(View.GONE);

        //check for proposed walk
        //if proposed walk exists, show proposed walk layout

    }

}
