package com.example.myapplication.viewmodel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthViewModel extends ViewModel{
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> authMessage = new MutableLiveData<>();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getAuthMessage() { return authMessage; }
    public LiveData<User> getCurrentUser() { return currentUser; }

    public void login(String email, String password) {
        isLoading.setValue(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    fetchUserData(authResult.getUser().getUid());
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("שגיאה בהתחברות: " + e.getMessage());
                });
    }

    public void register(String id, String name, String email, String phone, String birthday, String password) {
        isLoading.setValue(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        User newUser = new User(id, name, email, phone, birthday);

                        db.collection("users").document(firebaseUser.getUid())
                                .set(newUser)
                                .addOnSuccessListener(aVoid -> {
                                    isLoading.setValue(false);
                                    authMessage.setValue("Success!");
                                    currentUser.setValue(newUser);
                                })
                                .addOnFailureListener(e -> {
                                    isLoading.setValue(false);
                                    authMessage.setValue("Error during registration: " + e.getMessage());
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Error during registration: " + e.getMessage());
                });
    }

    public void fetchUserData(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    isLoading.setValue(false);
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        currentUser.setValue(user);
                        authMessage.setValue("Login Successful");
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Error fetching data");
                });
    }

    public void checkSession() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            fetchUserData(user.getUid());
        }
    }

    public void saveBuildToProfile(String buildRefId, String buildName) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) return;

        isLoading.setValue(true);
        com.example.myapplication.model.SavedBuild savedBuild =
                new com.example.myapplication.model.SavedBuild(buildRefId, buildName, System.currentTimeMillis());

        db.collection("users").document(firebaseUser.getUid())
                .update("savedBuilds", com.google.firebase.firestore.FieldValue.arrayUnion(savedBuild))
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Build saved to your profile successfully!");
                    fetchUserData(firebaseUser.getUid());
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Error saving to profile: " + e.getMessage());
                });
    }

    public void renameSavedBuild(String buildRefId, String newName) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        User user = currentUser.getValue();
        if (firebaseUser == null || user == null || user.getSavedBuilds() == null) return;

        java.util.List<com.example.myapplication.model.SavedBuild> updatedBuilds = new java.util.ArrayList<>();

        for (com.example.myapplication.model.SavedBuild oldBuild : user.getSavedBuilds()) {
            if (oldBuild.getBuildRef().equals(buildRefId)) {
                updatedBuilds.add(new com.example.myapplication.model.SavedBuild(
                        oldBuild.getBuildRef(),
                        newName,
                        oldBuild.getSavedAt()
                ));
            }
            else {
                updatedBuilds.add(oldBuild);
            }
        }

        isLoading.setValue(true);
        db.collection("users").document(firebaseUser.getUid())
                .update("savedBuilds", updatedBuilds)
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Build renamed successfully!");
                    fetchUserData(firebaseUser.getUid());
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Error renaming build: " + e.getMessage());
                });
    }

    public void deleteSavedBuild(String buildRefId) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        User user = currentUser.getValue();
        if (firebaseUser == null || user == null || user.getSavedBuilds() == null) return;

        java.util.List<com.example.myapplication.model.SavedBuild> updatedBuilds = new java.util.ArrayList<>();
        for (com.example.myapplication.model.SavedBuild b : user.getSavedBuilds()) {
            if (!b.getBuildRef().equals(buildRefId)) {
                updatedBuilds.add(b);
            }
        }

        isLoading.setValue(true);
        db.collection("users").document(firebaseUser.getUid())
                .update("savedBuilds", updatedBuilds)
                .addOnSuccessListener(aVoid -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Build deleted successfully!");
                    fetchUserData(firebaseUser.getUid());
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("Error deleting build: " + e.getMessage());
                });
    }

    public void signOut() {
        auth.signOut();
        currentUser.setValue(null);
    }

    public void clearMessage() {
        authMessage.setValue(null);
    }
}
