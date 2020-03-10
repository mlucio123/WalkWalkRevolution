const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendInvite = functions.firestore
   .document('users/{userID}/invitations/{invitationID}')
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: 'You have a Team Invitation',
           body: document.fromUserID + ' from Team: ' + document.teamId + ' has sent you a team Invite'
         },
         topic: context.params.userID
       };

       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";
   });

exports.sendInviteResponse = functions.firestore
	.document('teams/{teamID}/responses/{responseID}')
	.onCreate((snap, context) => {

     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.deviceID + ' has responded to your Team Invite',
           body: document.deviceID + ' has ' + document.action + ' your Team Invite'
         },
         topic: context.params.teamID
       };
	
       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";

});

exports.sendProposeWalkResponse = functions.firestore
	.document('teams/{teamID}/responsesToWalk/{responseID}')
	.onCreate((snap, context) => {

     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.deviceID + ' has responded to your team\'s Proposed Walk',
           body: document.deviceID + '\'s response to the Proposed Walk is: ' + document.response
         },
         topic: context.params.teamID
       };
	
       return admin.messaging().send(message)
         .then((response) => {
           // Response is a message ID string.
           console.log('Successfully sent message:', response);
           return response;
         })
         .catch((error) => {
           console.log('Error sending message:', error);
           return error;
         });
     }

     return "document was null or emtpy";

});


// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
