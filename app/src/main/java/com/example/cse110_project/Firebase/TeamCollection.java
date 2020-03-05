package com.example.cse110_project.Firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_project.utils.Route;
import com.example.cse110_project.utils.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamCollection {

    FirebaseFirestore db;
    private final String TAG = "Firebase Team";


    /* Initialize firebase instance */
    public TeamCollection() {
        db = FirebaseFirestore.getInstance();
        if (this.db == null) {
            Log.d(TAG, "Unsuccessful firebase instance");
        } else {
            Log.d(TAG, "Success");
        }
    }


    /* Initialize Firebase App */
    public static void initFirebase(Context context) {
        FirebaseApp.initializeApp(context);
    }


    public void addTeamIdToUser(String teamID, String userID) {
        Map<String, Object> tmID = new HashMap<>();
        tmID.put("teamID", teamID);

        db.collection("users")
                .document(userID)
                .update(tmID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Added " + teamID + " in to the user " + userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });;
    }

    public void makeTeam(String deviceID) {
        Map<String, Object> teamMap = new HashMap<>();
        ArrayList<String> userIDs = new ArrayList<String>();
        userIDs.add(deviceID);
        teamMap.put("listOfUserIDs", userIDs );

        db.collection("teams")
                .add(teamMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        String teamID = documentReference.getId();

                        addTeamIdToUser(teamID, deviceID);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    public String getTeamID(String deviceID) {
        DocumentReference docRef = db.collection("teams").document(deviceID);
        return docRef.getId();

    }

    public void sendInvitation(String userId, String teamId, String fromUserID){
        Map<String,Object> invitationMap = new HashMap<>();
        invitationMap.put("teamId", teamId);
        invitationMap.put("fromUserID", fromUserID);

        db.collection("users")
                .document(teamId)
                .collection("invitations")
                .add(invitationMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.d(TAG, "Send an invite to " + userId + " from team " + teamId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        //add invited user to list of pending invitations
        Map<String, Object> teamMap = new HashMap<>();
        ArrayList<String> userIDs = new ArrayList<String>();
        userIDs.add(userId);
        teamMap.put("listOfUserIDs", userIDs );

        db.collection("teams")
                .document(teamId)
                .collection("listOfPendingUserIds")
                .add(invitationMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.d(TAG, "Send an invite to " + userId + " from team " + teamId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }



    public void addToTeamPendingList(String teamID, String newUserID) {

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userID", newUserID);


        db.collection("teams")
                .document(teamID)
                .collection("listOfPendingUserIds")
                .add(userMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, newUserID + " is added to " + teamID + "'s pending list!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }



    public void addToUserInvitationCollection(String toUserID, String fromUserID, String teamID) {


        Map<String,Object> invitationMap = new HashMap<>();
        invitationMap.put("teamId", teamID);
        invitationMap.put("fromUserID", fromUserID);

        db.collection("users")
                .document(toUserID)
                .collection("invitations")
                .add(invitationMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.d(TAG, "Send an invite to " + toUserID + " from team " + teamID + " by " + fromUserID);
                        addToTeamPendingList(teamID, toUserID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }



    public void findInviterTeam(String toUserId, String fromUserId) {

        db.collection("users")
                .document(fromUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Finding inviter's teamID: " + document.getData());
                                Log.d(TAG, "FOUND INVITER'S TeamID: " + document.getData().get("teamID"));
                                addToUserInvitationCollection(toUserId, fromUserId, document.getData().get("teamID").toString());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }


    public void sendInviteToEmail(String email, String fromUserID) {

        db.collection("users")
                .whereEqualTo("gmail", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String toUserID = document.getId();

                                findInviterTeam(toUserID, fromUserID);

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
