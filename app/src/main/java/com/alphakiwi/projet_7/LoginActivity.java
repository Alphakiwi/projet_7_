package com.alphakiwi.projet_7;

import android.content.Intent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.widget.Button;

import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.base.BaseActivity;
import com.alphakiwi.projet_7.model.Restaurant;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;

public class LoginActivity extends BaseActivity {

    //FOR DESIGN
    @BindView(R.id.main_activity_coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.main_activity_button_login)
    Button buttonLogin;

    //CallbackManager callbackManager;

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    @Override
    public int getFragmentLayout() { return R.layout.activity_login; }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // callbackManager.onActivityResult(requestCode, resultCode, data);

        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.updateUIWhenResuming();
    }


    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.main_activity_button_login)
    public void onClickLoginButton() {
        if (this.isCurrentUserLogged()){
            this.startProfileActivity();
        } else {
            this.startSignInActivity();
        }
    }

    @OnClick(R.id.main_activity_button_chat)
    public void onClickChatButton() {
        if (this.isCurrentUserLogged()){
            this.startHungryActivity();
        } else {
            this.showSnackBar(this.coordinatorLayout, getString(R.string.error_not_connected));
        }
    }

    // --------------------
    // REST REQUEST
    // --------------------

    private void createUserInFirestore(){

        if (this.getCurrentUser() != null){

            if (!getAllUser().contains(getUserCurrent())) {

                String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
                String username = this.getCurrentUser().getDisplayName();
                String uid = this.getCurrentUser().getUid();

                Restaurant resto = new Restaurant(getString(R.string.no_choice), "?", "?");
                boolean notification = true;

                ArrayList<String> restoLike = new ArrayList<String>();


                UserHelper.createUser(uid, username, urlPicture, resto, notification, restoLike ).addOnFailureListener(this.onFailureListener());
            }

        }
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), //EMAIL
                                        new AuthUI.IdpConfig.GoogleBuilder().build(), //GOOGLE
                                        new AuthUI.IdpConfig.FacebookBuilder().build())) // FACEBOOK
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo)
                        .build(),
                RC_SIGN_IN);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }





    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfilActivity.class);
        startActivity(intent);
    }



    private void startHungryActivity(){
        Intent intent = new Intent(this,HungryActivity.class);
        startActivity(intent);
    }

    // --------------------
    // UI
    // --------------------

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateUIWhenResuming(){
        this.buttonLogin.setText(this.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }

    // --------------------
    // UTILS
    // --------------------

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            }
        }
    }

}