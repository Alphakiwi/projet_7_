package com.alphakiwi.projet_7;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.base.BaseActivity;
import com.alphakiwi.projet_7.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;


import butterknife.BindView;
import butterknife.OnClick;

import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;

/**
 * Created by Philippe on 25/01/2018.
 */

public class ProfilActivity extends BaseActivity {

    //FOR DESIGN
    @BindView(R.id.profile_activity_imageview_profile)
    ImageView imageViewProfile;
    @BindView(R.id.profile_activity_edit_text_username)
    EditText textInputEditTextUsername;
    @BindView(R.id.profile_activity_text_view_email)
    TextView textViewEmail;
    @BindView(R.id.profile_activity_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.profile_activity_resto)
    TextView resto;
    @BindView(R.id.switch_notif)
    Switch notif;


    //FOR DATA
    private static final int UPDATE_Notif = 5;
    private static final int UPDATE_Notif2 = 8;
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;
    private static final int UPDATE_USERNAME = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.updateUIWhenCreating();

        if (getUserCurrent()!= null) {
            resto.setText(getUserCurrent().getResto().getName());

            if (getUserCurrent().getNotification() == false) {
                notif.setChecked(false);
            }
        }
        notif.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View v) {


                if (notif.isChecked()) {

                    if (getCurrentUser() != null){
                            UserHelper.updateNotif(true, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_Notif));
                    }


                } else {

                    if (getCurrentUser() != null){
                        UserHelper.updateNotif(false, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_Notif2));
                    }


                }

            }
        });

    }

    @Override
    public int getFragmentLayout() { return R.layout.activity_profile; }

    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.profile_activity_button_update)
    public void onClickUpdateButton() { this.updateUsernameInFirebase(); }

    @OnClick(R.id.profile_activity_button_sign_out)
    public void onClickSignOutButton() { this.signOutUserFromFirebase(); }

    @OnClick(R.id.profile_back)
    public void onClickBackButton() { this.finish(); }

    @OnClick(R.id.profile_activity_button_delete)
    public void onClickDeleteButton() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.popup_message_confirmation_delete_account)
            .setPositiveButton(R.string.popup_message_choice_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteUserFromFirebase();
                }
            })
            .setNegativeButton(R.string.popup_message_choice_no, null)
            .show();
    }
    // --------------------
    // REST REQUESTS
    // --------------------

    private void signOutUserFromFirebase(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    private void deleteUserFromFirebase(){
        if (this.getCurrentUser() != null) {

            //4 - We also delete user from firestore storage
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());

            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
        }
    }

    // 3 - Update User Username
    private void updateUsernameInFirebase(){

        this.progressBar.setVisibility(View.VISIBLE);
        String username = this.textInputEditTextUsername.getText().toString();

        if (this.getCurrentUser() != null){
            if (!username.isEmpty() &&  !username.equals(getString(R.string.info_no_username_found))){
                UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener()).addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_USERNAME));
            }
        }
    }


    // --------------------
    // UI
    // --------------------

    private void updateUIWhenCreating(){

        if (this.getCurrentUser() != null){

            //Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(this.getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }

            //Get email & username from Firebase
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) : this.getCurrentUser().getEmail();

            //Update views with data
            this.textViewEmail.setText(email);

            // 5 - Get additional data from Firestore
            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    String username = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.info_no_username_found) : currentUser.getUsername();
                    textInputEditTextUsername.setText(username);
                }
            });
        }
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case UPDATE_USERNAME:
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(ProfilActivity.this, getString(R.string.new_name) , Toast.LENGTH_SHORT).show();
                        break;
                    case SIGN_OUT_TASK:
                        finish();
                        finish();
                        break;
                    case DELETE_USER_TASK:
                        finish();
                        break;
                    case UPDATE_Notif:
                        Toast.makeText(ProfilActivity.this, getString(R.string.notif), Toast.LENGTH_SHORT).show();
                        break;
                    case UPDATE_Notif2:
                        Toast.makeText(ProfilActivity.this, getString(R.string.no_notif) , Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }




}
