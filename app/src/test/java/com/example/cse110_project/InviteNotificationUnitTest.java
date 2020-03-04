package com.example.cse110_project;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

@LargeTest
public class InviteNotificationUnitTest {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
   /*
    @Test
    public void testInviteNotificationDelete(){
        HashMap

        db.collection("users")
                .document("testUser")
                .collection("invitations")
                .document("testInvitation")
                .set();
    }

    */
}
