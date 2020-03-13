package com.example.cse110_project.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_project.Firebase.InvitationCallback;
import com.example.cse110_project.Firebase.TeamCollection;
import com.example.cse110_project.utils.Route;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteNotification implements Notification {
    private Notification.NotifType type;
    private String fromName;
    private String teamID;
    private String deviceID;
    private String result;
    private boolean isCreator;
    FirebaseFirestore db;

    private String TAG = "INVITATION NOTIFICATION : ";

    public InviteNotification(String deviceID, String fromName, String teamID, boolean isCreator, String result) {
        this.type = NotifType.InviteNotification;
        this.fromName = fromName;
        this.teamID = teamID;
        this.deviceID = deviceID;
        this.isCreator = isCreator;
        this.result = result;
        db = FirebaseFirestore.getInstance();
    }

    public InviteNotification(String deviceID) {
        this.deviceID = deviceID;
        db = FirebaseFirestore.getInstance();
    }


    @Override
    public Notification.NotifType getType(){
        return type;
    }

    @Override
    public String getFromName(){
        return this.fromName;
    }

    @Override
    public String getResult(){
        return this.result;
    }

    @Override
    public boolean getIsCreator(){
        return this.isCreator;
    }



    public void getUserRepsonses(String deviceID, InvitationCallback callback) {

        //first layer for querying for teamID

        db.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "FOUND Device'S TeamID: " + document.get("teamID"));

                                if(document.getData().get("teamID") != null) {
                                    String teamID = document.get("teamID").toString();

                                    // second layer for searching responses
                                    db.collection("teams")
                                            .document(teamID)
                                            .collection("responses")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("TAG", "LOGGING RESPONSES FOR TEAM " + teamID);
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                                            String deviceID = document.getData().get("deviceID").toString();
                                                            String action = document.getData().get("action").toString();

                                                            //Third layer

                                                            db.collection("users")
                                                                    .document(deviceID)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot document = task.getResult();
                                                                                if (document.exists()) {
                                                                                    Log.d(TAG, "Finding inviter's teamID: " + document.getData());
                                                                                    Log.d(TAG, "FOUND INVITER'S TeamID: " + document.getData().get("teamID"));


                                                                                    callback.getUsers(document.getData().get("initial").toString(), action);


                                                                                } else {
                                                                                    Log.d(TAG, "No such document");
                                                                                }
                                                                            } else {
                                                                                Log.d(TAG, "get failed with ", task.getException());
                                                                            }
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w(TAG, "Error at findInviterTeam" , e);
                                                                        }
                                                                    });




                                                        }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });



                                }


                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at getTeamRoutesFromDevice" , e);
                    }
                });


    }



    public void addUserToResponse(String deviceID, String teamID, String action) {

        Map<String, Object> mp = new HashMap<>();
        mp.put("deviceID", deviceID);
        mp.put("action", action);

        db.collection("teams")
                .document(teamID)
                .collection("responses")
                .add(mp)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "User " + deviceID + " " + action + " to team " + teamID);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }


    public void addUserToTeamList(String deviceID, String teamID) {

        Map<String, Object> mp = new HashMap<>();
        mp.put("DUMB", "FIELDS");


        db.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                if(document.getData().get("teamID") != null) {

                                    String oldTeamID = document.getData().get("teamID").toString();

                                    Log.d(TAG, "USER has previous team with " + oldTeamID);

                                    db.collection("teams")
                                            .document(oldTeamID)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                    DocumentSnapshot innerDoc = task.getResult();

                                                    Log.d(TAG, "TEAM INFO FOR " + oldTeamID + " with data: " + innerDoc.getData().toString());
                                                    Log.d(TAG, "MEMBERS: " + innerDoc.getData().get("listOfUserIDs").toString());


                                                    ArrayList<String> userIDs = (ArrayList<String>) innerDoc.get("listOfUserIDs");

                                                    Log.d(TAG, "RETREIEVED : " + userIDs.size() + "people in the old team");

                                                    Map<String, Object> tmMap = new HashMap<>();
                                                    tmMap.put("teamID", teamID);

                                                    // TODO : FIXING MERGE TEAM HERE


                                                    for(String uID : userIDs) {


                                                        db.collection("users")
                                                                .document(uID)
                                                                .update(tmMap)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        Log.d(TAG, "Getting old team member : " + uID + " to new team " + teamID);
                                                                        db.collection("teams")
                                                                                .document(teamID)
                                                                                .update("listOfUserIDs", FieldValue.arrayUnion(uID))
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        Log.d(TAG, "APPENDING " + uID + " to the new team's list " + teamID);
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.w(TAG, "Error writing document", e);
                                                                                    }
                                                                                });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w(TAG, "Error writing document", e);
                                                                    }
                                                                });

                                                    }






                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });




                                } else {

                                    db.collection("teams")
                                            .document(teamID)
                                            .update("listOfUserIDs", FieldValue.arrayUnion(deviceID))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "Successfully append " + deviceID + " to " + teamID + "'s list.");

                                                    addUserToResponse(deviceID, teamID, "accepted");

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                }


                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

    }


    //@Override
    public void deleteNotification(String device, String fromDeviceID, String teamID, String elID, String action){
        db.collection("teams")
                .document(teamID)
                .collection("listOfPendingUserIds")
                .whereEqualTo("userID", device).addSnapshotListener((newSnapShot, err) -> {
            for(DocumentSnapshot doc : newSnapShot.getDocuments()) {
                doc.getReference().delete();
                addUserToResponse(device, teamID, action);
            }
        });




        db.collection("users")
                .document(device)
                .collection("invitations")
                .document(elID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully deleted " + device + " from " + elID + "'s list.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void acceptInvitation(String teamID, String deviceID){


    }
}
