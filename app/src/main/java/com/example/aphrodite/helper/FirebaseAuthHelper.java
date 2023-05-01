package com.example.aphrodite.helper;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseAuthHelper {

    private static FirebaseAuthHelper instance = null;
    private static FirebaseAuth firebaseAuth;

    private final FirebaseFirestore firestore;

    private FirebaseAuthHelper() {
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public static FirebaseAuthHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthHelper();
        }
        return instance;
    }

    public static FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }

    public String getCurrentUserId() {
        FirebaseUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        } else {
            return null;
        }
    }

    public void sendEmailVerification(@NonNull OnCompleteListener<Void> listener) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(listener);
        }
    }

    public void signUp(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }


    public boolean isUserLoggedIn() {
        FirebaseUser currentUser = getCurrentUser();
        return currentUser != null;
    }

    public void updateProfile(String displayName, String photoUrl, OnCompleteListener<Void> listener) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(photoUrl == null ? null : Uri.parse(photoUrl))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(listener);
        }
    }


    public void reauthenticate(String email, String password, OnCompleteListener<Void> listener) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);
            user.reauthenticate(credential)
                    .addOnCompleteListener(listener);
        }
    }

    public void updateUserEmail(String email, OnCompleteListener<Void> listener) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            user.updateEmail(email)
                    .addOnCompleteListener(listener);
        }
    }

    public void updateUserPassword(String password, OnCompleteListener<Void> listener) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            user.updatePassword(password)
                    .addOnCompleteListener(listener);
        }
    }

    public void deleteUser(OnCompleteListener<Void> listener) {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(listener);
        }
    }

    public void getUserName(OnCompleteListener<DocumentSnapshot> listener) {
        String userId = getCurrentUserId();
        if (userId != null) {
            firestore.collection("contacts_accounts")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(listener);
        }
    }


    public void signIn(String email, String password, OnCompleteListener<AuthResult> onCompleteListener) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public void signOut() {
        firebaseAuth.signOut();
    }

    public String getCurrentUserUid() {
        return firebaseAuth.getCurrentUser() != null ? firebaseAuth.getCurrentUser().getUid() : null;
    }


}
