package com.alphakiwi.projet_7;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.base.BaseActivity;
import com.alphakiwi.projet_7.model.Restaurant;
import com.alphakiwi.projet_7.notification.NotificationReceiver;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.AlarmManager.INTERVAL_DAY;
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
    private static final String FIRST = "FirstTime";


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
            sharedpref();
            this.startHungryActivity();
        } else {
            this.showSnackBar(this.coordinatorLayout, getString(R.string.error_not_connected));
        }
    }

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


    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void updateUIWhenResuming(){
        this.buttonLogin.setText(this.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }


    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                this.createUserInFirestore();
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
            }
        }
    }


    public void sharedpref(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(FIRST, false)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,12);
            calendar.set(Calendar.MINUTE,00);
            if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.DAY_OF_MONTH, 1);
            Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), INTERVAL_DAY,pendingIntent);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(FIRST, true);
            editor.apply();
        }
    }


}