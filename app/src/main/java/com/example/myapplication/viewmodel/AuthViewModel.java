package com.example.myapplication.viewmodel;

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

    // LiveData לניהול מצב הטעינה (Loading state)
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    // LiveData להודעות שגיאה או הצלחה (Success/Error messages)
    private final MutableLiveData<String> authMessage = new MutableLiveData<>();

    // LiveData לשמירת פרטי המשתמש המחובר (Current User Profile)
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getAuthMessage() { return authMessage; }
    public LiveData<User> getCurrentUser() { return currentUser; }

    /**
     * פונקציית התחברות (Login via Firebase Auth).
     */
    public void login(String email, String password) {
        isLoading.setValue(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    fetchUserData(authResult.getUser().getUid());
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("שגיאה בהתחברות: " + e.getMessage()); // Login Error
                });
    }

    /**
     * פונקציית הרשמה (Register via Firebase Auth -> Save profile to Firestore).
     */
    public void register(String id, String name, String email, String phone, String birthday, String password) {
        isLoading.setValue(true);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Auth success! Now save the rest of the data to Firestore
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        User newUser = new User(id, name, email, phone, birthday);

                        // Save document with the Auth UID as the document ID
                        db.collection("users").document(firebaseUser.getUid())
                                .set(newUser)
                                .addOnSuccessListener(aVoid -> {
                                    isLoading.setValue(false);
                                    authMessage.setValue("ההרשמה בוצעה בהצלחה!"); // Registration Successful
                                    currentUser.setValue(newUser);
                                })
                                .addOnFailureListener(e -> {
                                    isLoading.setValue(false);
                                    authMessage.setValue("שגיאה בשמירת הנתונים: " + e.getMessage());
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("שגיאה בהרשמה: " + e.getMessage());
                });
    }

    /**
     * מושך את נתוני המשתמש מ-Firestore לפי ה-UID של Auth.
     * Fetches user profile from Firestore.
     */
    public void fetchUserData(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    isLoading.setValue(false);
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        currentUser.setValue(user);
                        authMessage.setValue("התחברת בהצלחה!"); // Login Successful
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    authMessage.setValue("שגיאה בשליפת נתונים."); // Error fetching data
                });
    }

    /**
     * בודק אם משתמש כבר מחובר כשהאפליקציה נדלקת.
     * Check if user is already logged in on app startup.
     */
    public void checkSession() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            fetchUserData(user.getUid());
        }
    }
}
