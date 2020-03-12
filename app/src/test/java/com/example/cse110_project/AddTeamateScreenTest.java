package com.example.cse110_project;

import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;

import static android.os.Looper.getMainLooper;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;
import static org.robolectric.Shadows.shadowOf;

import org.robolectric.annotation.LooperMode;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LooperMode(PAUSED)

public class AddTeamateScreenTest {
    @Rule
    public ActivityScenarioRule<AddTeamateScreen> scenarioRule = new ActivityScenarioRule<>(AddTeamateScreen.class);

    private ActivityScenario<AddTeamateScreen> scenario;

    private Button createTeamBtn;
    private Button submitBtn;

    private EditText teamMateName;
    private EditText teamMateEmail;

    private TextView pageTitle;
    private TextView inviteeName;
    private TextView inviteeEmail;

    @Before
    public void setup() {
        scenario = scenarioRule.getScenario();
    }

    private void init(AddTeamateScreen addTeamateScreen) {

        addTeamateScreen.isProductionMode(false);

        createTeamBtn = addTeamateScreen.findViewById(R.id.createTeamBtn);
        submitBtn = addTeamateScreen.findViewById(R.id.submitBtn);

        teamMateName = addTeamateScreen.findViewById(R.id.inviteeName);
        teamMateEmail = addTeamateScreen.findViewById(R.id.inviteeEmail);

        pageTitle = addTeamateScreen.findViewById(R.id.teamTitle);
        inviteeName = addTeamateScreen.findViewById(R.id.textView3);
        inviteeEmail = addTeamateScreen.findViewById(R.id.textView4);

    }

    @Test
    public void testSynchronizationUnit() {
        List<String> events = new ArrayList<>();
        shadowOf(getMainLooper()).idle();
        events.add("before");
        shadowOf(getMainLooper()).idle();
        new Handler(Looper.getMainLooper()).post(() -> events.add("after"));
        events.add("between");
        assertThat(events).containsExactly("before", "between").inOrder();
        shadowOf(getMainLooper()).idle();
        assertThat(events).containsExactly("before", "between", "after").inOrder();
    }

    @Test
    public void testTextViewUnit() {
        scenario.onActivity(addTeamateScreen -> {
            init(addTeamateScreen);
            assertEquals("Invitee Name: ", inviteeName.getText().toString());
            assertEquals("Invitee Email: ", inviteeEmail.getText().toString());
            assertEquals("Add Your Teamate", pageTitle.getText().toString());
        });
    }

    @Test
    public void testEditTextUnit() {
        scenario.onActivity(addTeamateScreen -> {
            init(addTeamateScreen);
            teamMateName.setText("cse110");
            teamMateEmail.setText("cse110@gmail.com");
            assertEquals("cse110", teamMateName.getText().toString());
            assertEquals("cse110@gmail.com", teamMateEmail.getText().toString());
        });
    }

    @Test
    public void testButtonUnit() {
        scenario.onActivity(addTeamateScreen -> {
            init(addTeamateScreen);
            String create = createTeamBtn.getText().toString();
            String add = submitBtn.getText().toString();
            assertEquals("CREATE TEAM", create);
            assertEquals("ADD TEAMATE", "ADD TEAMATE");
        });

    }


}
