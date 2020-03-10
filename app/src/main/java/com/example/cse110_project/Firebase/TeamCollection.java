package com.example.cse110_project.Firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110_project.utils.Route;
import com.example.cse110_project.utils.Team;
import com.example.cse110_project.utils.User;
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
import java.util.List;
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
                });
    }

    public void makeTeam(String deviceID) {
        Log.i(TAG, "MAKING TEAM for: " + deviceID);
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
        Log.i(TAG, docRef.getId().toString());
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


    public void addToTeamPendingList(String deviceID, String inviteeName, String inviteeEmail, PendingTeammatesListListener myListener){
        Log.d(TAG, "DEVICE ID: " + deviceID);
        Log.d(TAG, inviteeEmail);

        DocumentReference userRef = db.collection("users").document(deviceID);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object teamIDObj = document.get("teamID");

                        // make team fi teamID field not found
                        if (teamIDObj == null){
                            makeTeam(deviceID);
                        }

                        // get pending list
                        String teamID = teamIDObj.toString();
                        Log.d(TAG, "TEAM ID: " + teamID);

                        // check if user exists
                        db.collection("users")
                                .whereEqualTo("gmail", inviteeEmail)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            // add existing user to listOfPendingUserIds
                                            // TODO: Check Duplicates
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d(TAG, document.getId() + " => " + document.getData());
                                                String userID = document.getId();
                                                Map<String,Object> pendingTeammate = new HashMap<>();
                                                pendingTeammate.put("userID", userID);
                                                db.collection("teams")
                                                        .document(teamID)
                                                        .collection("listOfPendingUserIds")
                                                        .add(pendingTeammate)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                sendInviteToEmail(inviteeEmail, deviceID);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding document", e);
                                                            }
                                                        });

                                            }

                                            // call callback function to perform intent
                                            myListener.onSuccess();
                                        } else {
                                            Log.w(TAG, "Error getting documents.", task.getException());
                                        }
                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
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
//                        addToTeamPendingList(teamID, toUserID);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at findInviterTeam" , e);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error at sendInviteToEmail" , e);
                    }
                });

    }




    public void getTeamRoutes(List<String> userIds, String currDeviceID, final MyCallback myCallback) {

        ArrayList<String> completedRoutesID = new ArrayList<>();
        HashMap<String, QueryDocumentSnapshot> statsMap = new HashMap<>();

        db.collection("users")
                .document(currDeviceID)
                .collection("completedRoutes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Completed Teammate Routes: ");


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Completed this teammate's route " + document.getId() + " by " + currDeviceID);
                                Log.d(TAG, "with stats as " + document.getData());
                                completedRoutesID.add(document.getId());
                                statsMap.put(document.getId(), document);
                            }

                            Log.d(TAG, "FOUND " + completedRoutesID.size() + " completed team routes");

                            for (String teammateDeviceID : userIds) {

                                db.collection("routes")
                                        .whereEqualTo("deviceID", teammateDeviceID)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    ArrayList<Route> routesSimpleList = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        try {

                                                            if(completedRoutesID.contains(document.getId())) {
                                                                QueryDocumentSnapshot statsDoc = statsMap.get(document.getId());
                                                                routesSimpleList.add(makeRoute(document, currDeviceID, true, statsDoc));
                                                            } else {
                                                                routesSimpleList.add(makeRoute(document, currDeviceID, false, null));
                                                            }

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Log.d(TAG, "THIS ROUTE CAN't be ADDED");
                                                        }

                                                        Log.d(TAG, "TEAMMATE ROUTE ID: " + document.getId() + " => " + document.getData());
                                                    }
                                                    myCallback.getRoutes(routesSimpleList);
                                                    Log.d(TAG, "CURRENT LIST OF TEAM ROUTES: " + routesSimpleList.size());

                                                } else {
                                                    Log.w(TAG, "Error getting team routes.", task.getException());
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error at getTeamRoutes", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }



    public void getTeamUsers(String teamID, String currDeviceID, final MyCallback myCallback) {

        db.collection("teams")
                .document(teamID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.i(TAG, teamID);
                            if (document.exists()) {
                                Log.d(TAG, "FOUND USERS ON THIS TEAM: " + document.getData());

                                List<String> userIds = (List<String>) document.get("listOfUserIDs");

                                getTeamRoutes(userIds, currDeviceID, myCallback);

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
                        Log.w(TAG, "Error at getTeamUsers" , e);
                    }
                });



    }


    /* Get routes for current device */
    public void getTeamRoutesFromDevice(String deviceID, final MyCallback myCallback){


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

                                    getTeamUsers(teamID, deviceID, myCallback);
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

    public void addTeamID(String teamID, String deviceID) {
        db.collection("users").document(deviceID).update("teamID", teamID);
    }


    public void getWalkingResponses(String deviceID, InvitationCallback callback) {

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
                                            .collection("responsesToWalk")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("TAG", "LOGGING RESPONSES FOR TEAM " + teamID);
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                                            String deviceID = document.getData().get("deviceID").toString();
                                                            String action = document.getData().get("response").toString();

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

    public void setUserResponseToWalk(String deviceID, String response) {

        db.collection("users")
                .document(deviceID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Finding teamID: " + document.getData());

                                String teamID = document.getData().get("teamID").toString();

                                Map<String, Object> res = new HashMap<>();
                                res.put("deviceID", deviceID);
                                res.put("response", response);

                                db.collection("teams")
                                        .document(teamID)
                                        .collection("responsesToWalk")
                                        .document(deviceID)
                                        .set(res)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, deviceID + " has responded " + response);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });









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



    /* This method makes Route object from returning query document snapshot */
    private Route makeRoute(QueryDocumentSnapshot qry, String deviceID, boolean hasWalked, QueryDocumentSnapshot statsMap){

        try {
            Route newRoute;
            String id = qry.getId();

            String title = "";

            if(qry.getData().get("title") != null) {
                title = qry.getData().get("title").toString();
            }

            String start_position = "";

            if(qry.getData().get("start_position") != null){
                start_position = qry.getData().get("start_position").toString();
            }


            String notes = "";

            if(qry.getData().get("notes") != null){
                notes = qry.getData().get("notes").toString();
            }


            boolean out = false;
            boolean loop= false;
            boolean flat= false;
            boolean hills= false;
            boolean even= false;
            boolean rough= false;
            boolean street= false;
            boolean trail= false;
            boolean easy= false;
            boolean medium= false;
            boolean hard= false;
            boolean favorite= false;

            String initial = "";

            if(qry.getData().get("createdBy") != null) {
                initial = qry.getData().get("createdBy").toString();
            }

            if(qry.getData().get("out") != null){
                out = Boolean.parseBoolean(qry.getData().get("out").toString());
            }
            if(qry.getData().get("loop") != null){
                loop = Boolean.parseBoolean(qry.getData().get("loop").toString());
            }
            if(qry.getData().get("flat") != null){
                flat = Boolean.parseBoolean(qry.getData().get("flat").toString());
            }
            if(qry.getData().get("hills") != null){
                hills = Boolean.parseBoolean(qry.getData().get("hills").toString());
            }
            if(qry.getData().get("even") != null){
                even  = Boolean.parseBoolean(qry.getData().get("even").toString());
            }
            if(qry.getData().get("rough") != null){
                rough = Boolean.parseBoolean(qry.getData().get("rough").toString());
            }
            if(qry.getData().get("street") != null){
                street = Boolean.parseBoolean(qry.getData().get("street").toString());
            }
            if(qry.getData().get("trail") != null){
                trail = Boolean.parseBoolean(qry.getData().get("trail").toString());
            }
            if(qry.getData().get("easy") != null){
                easy = Boolean.parseBoolean(qry.getData().get("easy").toString());
            }
            if(qry.getData().get("medium") != null){
                medium = Boolean.parseBoolean(qry.getData().get("medium").toString());
            }
            if(qry.getData().get("hard") != null){
                hard = Boolean.parseBoolean(qry.getData().get("hard").toString());
            }
            if(qry.getData().get("favorite") != null){
                favorite = Boolean.parseBoolean(qry.getData().get("favorite").toString());
            }

            boolean[] tags ={out, loop, flat, hills, even, rough, street, trail, easy, medium, hard, favorite};
            newRoute = new Route(title, start_position, tags, notes);

            newRoute.setNotes(notes);
            newRoute.setId(qry.getId().toString());

            newRoute.setFavorite(favorite);

            newRoute.setCreatedBy(initial);
            Log.d(TAG, "ROUTE IS CREATED BY : " + initial + " and got " + newRoute.getCreatedBy());

            String time;
            String steps;
            String distance;

            int[] colors = new int[3];

            Log.d(TAG, "ROUTE RED IS " + qry.getData().get("red".toString()));

            if(qry.getData().get("red") != null){
                colors[0] = Integer.parseInt(qry.getData().get("red").toString());
            }


            if(qry.getData().get("green") != null){
                colors[1] = Integer.parseInt(qry.getData().get("green").toString());
            }


            if(qry.getData().get("blue") != null){
                colors[2] = Integer.parseInt(qry.getData().get("blue").toString());
            }


            if(qry.getData().get("lastCompletedTime") != null ){
                time = qry.getData().get("lastCompletedTime").toString();
                if(time != null){
                    newRoute.setLastCompletedTime(time);
                }
            }

            if(qry.getData().get("lastCompletedSteps") != null){
                steps = qry.getData().get("lastCompletedSteps").toString();
                if(steps != null){
                    newRoute.setLastCompletedSteps(steps);
                }
            }

            if(qry.getData().get("lastCompletedDistance") != null){
                distance = qry.getData().get("lastCompletedDistance").toString();
                if(distance != null){
                    newRoute.setLastCompletedDistance(distance);
                }
            }

            Log.d(TAG, "GOT COLORS: " + colors[0] +", " + colors[1] + ", " + colors[2]);
            newRoute.setColors(colors);

            Log.d(TAG, "checking prev walked from make route");

            if(hasWalked) {
                Log.d(TAG, "THIS USER WALKED ON THIS ROUTE: " + id);
                Log.d(TAG, statsMap.getData().toString());
                newRoute.setLastCompletedDistance(statsMap.getData().get("distance").toString());
                newRoute.setLastCompletedSteps(statsMap.getData().get("steps").toString());
                newRoute.setLastCompletedTime(statsMap.getData().get("time").toString());
                newRoute.setPrevWalked(true);
            }

            newRoute.setId(id);
            return newRoute;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "PROBLEMS WITH GETTING BACK ROUTES");
            return null;
        }

    }


}
