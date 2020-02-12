package com.example.cse110_project;
import android.app.Activity;

import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
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

public class RouteScreen extends AppCompatActivity {
    private String fitnessServiceKey = "GOOGLE_FIT";
    private BottomNavigationView bottomNavigationView;
    private Button addRoute;
    private Button expandBtn;

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
                    l.addView(newButton);
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

        expandBtn = findViewById(R.id.expandBtn);
        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
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
