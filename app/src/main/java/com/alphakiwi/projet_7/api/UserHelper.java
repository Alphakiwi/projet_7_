package com.alphakiwi.projet_7.api;

import androidx.annotation.NonNull;

import com.alphakiwi.projet_7.model.Restaurant;
import com.alphakiwi.projet_7.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import static com.alphakiwi.projet_7.DetailRestaurantActivity.RESTOLIKE;
import static com.alphakiwi.projet_7.HungryActivity.RESTAURANT;


public class  UserHelper {

    public static final String COLLECTION_NAME = "users";
    public static final String NOTIFICATION = "notification";
    public static final String USERNAME = "username";

    static ArrayList<User> userListWithoutMyself = new ArrayList<User>();
    static ArrayList<User> userList = new ArrayList<User>();
    static ArrayList<String> restoList = new ArrayList<String>();


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture, Restaurant resto, boolean notification, ArrayList<String> restoLike) {
        // 1 - Create Obj
        User userToCreate = new User(uid, username, urlPicture, resto, notification, restoLike);

        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update(USERNAME, username);
    }

    public static Task<Void> updateResto(Map<String, Object> resto, String uid) {
        return UserHelper.getUsersCollection().document(uid).update(RESTAURANT, resto);
    }

    public static Task<Void> updateNotif(boolean bool, String uid) {
        return UserHelper.getUsersCollection().document(uid).update(NOTIFICATION, bool);
    }

    public static Task<Void> updateRestoLike(ArrayList<String> restoLike, String uid) {
        return UserHelper.getUsersCollection().document(uid).update(RESTOLIKE, restoLike);
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {

        return UserHelper.getUsersCollection().document(uid).delete();
    }


    public static User getUserCurrent() {

         User currentUser =new User();


        for(int j = 0; j < getAllUser().size(); j++){

            String UserID = getAllUser().get(j).getUid();


            int comparaison = UserID.compareTo( FirebaseAuth.getInstance().getCurrentUser().getUid());


            if (comparaison == 0){

                currentUser = getAllUser().get(j);
            }

        }


        return currentUser;
    }


    public static ArrayList<User> getAllUserWithoutMyself() {



        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        userListWithoutMyself = new ArrayList<>();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                int comparateur = document.toObject(User.class).getUid().compareTo( FirebaseAuth.getInstance().getCurrentUser().getUid());

                                if (comparateur!=0) {

                                    userListWithoutMyself.add(document.toObject(User.class));
                                }


                            }
                        }
                    }
                });
        return userListWithoutMyself;
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

    public static ArrayList<String> getAllUserListResto() {



        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        restoList = new ArrayList<>();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                restoList.add(document.toObject(User.class).getResto().getId());


                            }
                        }
                    }
                });
        return restoList;


    }


}


