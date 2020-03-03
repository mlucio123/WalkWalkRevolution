const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

<<<<<<< HEAD
exports.addTimeStamp = functions.firestore
   .document('chats/{chatId}/messages/{messageId}')
   .onCreate((snap, context) => {
     if (snap) {
       return snap.ref.update({
                   timestamp: admin.firestore.FieldValue.serverTimestamp()
               });
     }

     return "snap was null or empty";
   });


exports.sendChatNotifications = functions.firestore
   .document('chats/{chatId}/messages/{messageId}')
=======
exports.sendInvite = functions.firestore
   .document('users/{userID}/invitations/{invitationID}')
>>>>>>> notificationsUI
   .onCreate((snap, context) => {
     // Get an object with the current document value.
     // If the document does not exist, it has been deleted.
     const document = snap.exists ? snap.data() : null;

     if (document) {
       var message = {
         notification: {
           title: document.from + ' sent you a message',
           body: document.text
         },
<<<<<<< HEAD
         topic: context.params.chatId
=======
         topic: context.params.userId
>>>>>>> notificationsUI
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

<<<<<<< HEAD
=======
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
>>>>>>> notificationsUI
