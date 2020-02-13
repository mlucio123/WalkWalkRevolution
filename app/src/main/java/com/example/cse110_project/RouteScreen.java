package com.example.cse110_project;
import android.app.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cse110_project.Firebase.MyCallback;
import com.example.cse110_project.Firebase.RouteCollection;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteScreen extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addRoute;
    private Button expandBtn;
    private RelativeLayout invis;

    private ArrayList<Route> currentRotes;

    private Route dummyRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_screen);

        currentRotes = new ArrayList<Route>();

        final Intent intent = new Intent(this, RouteFormScreen.class);

        addRoute = findViewById(R.id.addRouteBtn);
        addRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        RouteCollection rc = new RouteCollection();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        rc.getRoutes(deviceID, new MyCallback() {
            @Override
            public void getRoutes(ArrayList<Route> routes) {
                currentRotes = routes;
                Log.d("TAG", "SIZE IS = " + routes.size());

                LinearLayout l = (LinearLayout) findViewById(R.id.linear_layout_routes);


                for(int i = 0; i < routes.size(); i++){
                    Log.d("TAG", "ROUTE NAME: " + routes.get(i).getName());

                    // TODO : CALLS METHOD TAHT BUILDS THE ROUTE HERE

                    Button newButton = new Button(RouteScreen.this);
                    newButton.setText(routes.get(i).getName());
                    newButton.setBackgroundColor(0xFF99D6D6);
                    newButton.setTag(routes.get(i));

                    dummyRoute = routes.get(i);

                    newButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RouteScreen.this, WalkScreen.class);
                            Route dummy = (Route) view.getTag();
                            intent.putExtra("routeName", dummy.getName());
                            intent.putExtra("routeStart", dummy.getStartingPoint());
                            intent.putExtra("routeNotes", dummy.getNotes());
                            intent.putExtra("lastCompletedTime", dummy.getLastCompletedTime());
                            intent.putExtra("lastCompletedSteps", dummy.getLastCompletedSteps());
                            intent.putExtra("lastCompletedDistance", dummy.getLastCompletedDistance());
                            startActivity(intent);
                        }
                    });
                    //l.addView(newButton);

                }
                for (Route routeEntry : currentRotes ){
                    addElement(routeEntry);
                }

            }
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return false;
            }
        });
/*
        RouteCollection rc = new RouteCollection();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        rc.getRoutes(deviceID, new MyCallback() {
            @Override
            public void getRoutes(ArrayList<Route> routes) {
                currentRotes = routes;
                Log.d("TAG", "SIZE IS = " + routes.size());

                for(int i = 0; i < routes.size(); i++){
                    Log.d("TAG", "ROUTE NAME: " + routes.get(i).getName());
                    // TODO : CALLS METHOD TAHT BUILDS THE ROUTE HERE
                }



            }
        });

 */


        invis = findViewById(R.id.invisibleRel);
        invis.setVisibility(View.GONE);
        expandBtn = findViewById(R.id.expandBtn);
        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle_contents(expandBtn, invis);
            }
        });
    }

    public void toggle_contents(Button exp, RelativeLayout hide){
        if( hide.isShown()) {
            hide.setVisibility(View.GONE);
            exp.setText("Expand");
        } else {
            hide.setVisibility(View.VISIBLE);
            exp.setText("Hide");
        }
    }

    public void addElement(Route routeEntry){
        int fontColor = Color.parseColor("#ffffffff");
        Drawable draw = getDrawable(R.drawable.rounded_edges);

        LinearLayout routeContain = findViewById(R.id.routeContain);
        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPaddingRelative(30, 25, 30, 25);
        container.setLayoutParams(containerParams);
        container.setBackground(draw);

        LinearLayout titleEntry = new LinearLayout(this);
        titleEntry.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));
        titleEntry.setOrientation(LinearLayout.HORIZONTAL);

        TextView title = new TextView(this);
        title.setText("Route Name:");
        title.setTextColor(fontColor);
        title.setTextSize(20);
        title.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f
        ));

        TextView titleDisplay = new TextView(this);
        titleDisplay.setText(routeEntry.getName());
        titleDisplay.setTextColor(fontColor);
        titleDisplay.setTextSize(20);
        titleDisplay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f
        ));


        LinearLayout startEntry = new LinearLayout(this);
        titleEntry.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));
        startEntry.setOrientation(LinearLayout.HORIZONTAL);

        TextView start = new TextView(this);
        start.setText("Start Position:");
        start.setTextColor(fontColor);
        start.setTextSize(20);
        start.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f
        ));

        TextView startDisplay = new TextView(this);
        startDisplay.setText(routeEntry.getStartingPoint());
        startDisplay.setTextColor(fontColor);
        startDisplay.setTextSize(20);
        startDisplay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f
        ));

        final LinearLayout hidden = new LinearLayout(this);
        hidden.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        hidden.setOrientation(LinearLayout.VERTICAL);
        hidden.setVisibility(View.GONE);

        LinearLayout tagHolder = new LinearLayout(this);
        tagHolder.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        tagHolder.setOrientation(LinearLayout.HORIZONTAL);
        TextView tags = new TextView(this);
        tags.setText("Tags:");
        tags.setTextSize(20);
        tags.setTextColor(fontColor);
        tags.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.6f
        ));


        TextView tagsDisplay = new TextView(this);
        Object[] tagString = parseTags(routeEntry.getTags());
        /*
        if (tagString.length > 0){
            for(int i = 0; i < tagString.length; i++){

            }
        }

         */

        tagsDisplay.setText(Arrays.toString(tagString));
        tagsDisplay.setTextSize(20);
        tagsDisplay.setTextColor(fontColor);
        tagsDisplay.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.4f
        ));


        final Button expand = new Button(this);
        expand.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( hidden.isShown()) {
                    hidden.setVisibility(View.GONE);
                    expand.setText("Expand");
                } else {
                    hidden.setVisibility(View.VISIBLE);
                    expand.setText("Hide");
                }
            }
        });
        Drawable buttonBack = getDrawable(R.drawable.btn_rounded);
        expand.setBackground(buttonBack);
        expand.setText("Expand");
        expand.setTextSize(14);
        int black = Color.parseColor("#ff000000");
        expand.setTextColor(black);
        expand.setVisibility(View.VISIBLE);
        LinearLayout btnHolder = new LinearLayout(this);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        btnParams.setMargins(150, 50, 150, 0);
        btnHolder.setLayoutParams(btnParams);

        btnHolder.addView(expand);
        tagHolder.addView(tags);
        tagHolder.addView(tagsDisplay);
        hidden.addView(tagHolder);
        startEntry.addView(start);
        startEntry.addView(startDisplay);
        titleEntry.addView(title);
        titleEntry.addView(titleDisplay);
        container.addView(titleEntry);
        container.addView(startEntry);
        container.addView(hidden);
        container.addView(btnHolder);
        routeContain.addView(container);

    }


    protected Object[] parseTags(boolean[] tags) {
        //boolean[] tags ={out, loop, flat, hills, even, rough, street, trail, easy, medium, hard};
        List<String> tagList = new ArrayList<String>();
        if (tags[0]) {
            tagList.add("Out&Back");
        } else if (tags[1]) {
            tagList.add("Loop");
        }

        if (tags[2]) {
            tagList.add("Flat");
        } else if (tags[3]){
            tagList.add("Hills");
        }

        if (tags[4]){
            tagList.add("Even");
        } else if (tags[5]){
            tagList.add("Rough");
        }

        if (tags[6]){
            tagList.add("Street");
        } else if (tags[7]){
            tagList.add("Trail");
        }

        if (tags[8]){
            tagList.add("Easy");
        } else if (tags[9]){
            tagList.add("Medium");
        } else if (tags[10]){
            tagList.add("Hard");
        }


        return tagList.toArray();
    }



    private void selectFragment(MenuItem item){

        Intent newIntent = new Intent(this, this.getClass());
        switch(item.getItemId()) {
            case R.id.navigation_home:
                newIntent = new Intent(this, HomeScreen.class);
                newIntent.putExtra(HomeScreen.FITNESS_SERVICE_KEY, fitnessServiceKey);
                startActivity(newIntent);
                break;
            case R.id.navigation_walk:
                newIntent = new Intent(this, WalkScreen.class);
                startActivity(newIntent);
                break;
            default:
                break;
        }
    }

}
