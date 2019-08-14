package com.alphakiwi.projet_7.api;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alphakiwi.projet_7.MainActivity;
import com.alphakiwi.projet_7.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by Philippe on 30/01/2018.
 */

public class  UserHelper {

    public static final String COLLECTION_NAME = "users";
    static ArrayList<User> userList = new ArrayList<User>();

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, String resto, boolean notification) {
        // 1 - Create Obj
        User userToCreate = new User(uid, username, urlPicture, resto, notification);

        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {

        return UserHelper.getUsersCollection().document(uid).delete();
    }


    public static ArrayList<User> getAllUser() {



        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        userList = new ArrayList<>();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                userList.add(document.toObject(User.class));


                            }
                        }
                    }
                });
        return userList;
    }


}


