package com.alphakiwi.projet_7;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.base.BaseActivity;
import com.alphakiwi.projet_7.fragment.FirstFragment;
import com.alphakiwi.projet_7.fragment.SecondFragment;
import com.alphakiwi.projet_7.fragment.ThirdFragment;
import com.alphakiwi.projet_7.model.Restaurant;
import com.alphakiwi.projet_7.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserListResto;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;
import static com.alphakiwi.projet_7.api.UserHelper.getUsersCollection;
import static com.google.android.libraries.places.api.model.TypeFilter.ESTABLISHMENT;

public class HungryActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView imageViewProfile;

    @BindView(R.id.content_frame)
    FrameLayout frame;

    //FOR DESIGN

    TextView textUsername;
    TextView textViewEmail;

    private View mLayout;
    private static final int PERMISSION_REQUEST_LOCATION = 0;

    FragmentManager fragmentManager = null;

    FirstFragment firstFragment = new FirstFragment();



    private static final String API_KEY = BuildConfig.API_KEY;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fragmentManager = getSupportFragmentManager();


            switch (item.getItemId()) {
                case R.id.navigation_mapView:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    ,firstFragment )
                            .commit();

                    return true;
                case R.id.navigation_listView:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new SecondFragment())
                            .commit();

                    return true;
                case R.id.navigation_workmates:
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , new ThirdFragment())
                            .commit();
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLayout = findViewById(R.id.mlayout);


        getAllUser();
        getAllUserListResto();
        getAllUserWithoutMyself();

        sharedpref();

        showRestaurants();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.button_update_account, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header = navigationView.getHeaderView(0);

        imageViewProfile = (ImageView) header.findViewById(R.id.profile_activity_imageview_profile2);
        textUsername = (TextView) header.findViewById(R.id.profile_activity_edit_text_username2);
        textViewEmail = (TextView) header.findViewById(R.id.profile_activity_text_view_email2);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame
                        , new FirstFragment())
                .commit();


        updateUIWhenCreating();

        Places.initialize(getApplicationContext(), API_KEY);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Search restaurants");
        autocompleteFragment.setTypeFilter(ESTABLISHMENT);


        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.

            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();


        autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(
                new LatLng(latitude - 0.05 ,longitude - 0.05),
                new LatLng(latitude + 0.05, longitude + 0.05)));


// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                if (place.getTypes()!= null) {

                    if (place.getTypes().contains(Place.Type.RESTAURANT)) {

                        Intent i = new Intent(HungryActivity.this, PresentationActivity.class);

                        i.putExtra("resto", new Restaurant(place.getName(), place.getAddress(), place.getId()));

                        startActivity(i);

                    } else {

                        Toast.makeText(HungryActivity.this, "Ce n'est pas un restaurant", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Intent i = new Intent(HungryActivity.this, PresentationActivity.class);

                    i.putExtra("resto", new Restaurant(place.getName(), place.getAddress(), place.getId()));

                    startActivity(i);

                }


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
               // Log.i(TAG, "An error occurred: " + status);
            }
        });


    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_hungry_menu;
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_first_layout) {


            int comp = getUserCurrent().getResto().getName().compareTo("Pas encore choisit");

            if (comp== 0) {

                Toast.makeText(this, "Vous n'avez pas encore choisit de restaurant.", Toast.LENGTH_SHORT).show();;

            }else {
                Intent i = new Intent(this, PresentationActivity.class);

                i.putExtra("resto", getUserCurrent().getResto());

                startActivity(i);

            }
        } else if (id == R.id.nav_second_layout) {
            Intent newPage = new Intent(this
                    , ProfileActivity.class);
            startActivity(newPage);

            this.finish();
        } else if (id == R.id.nav_third_layout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        };
    }

    private void updateUIWhenCreating(){



        if ( getCurrentUser() != null){

            //Get picture URL from Firebase
            if ( getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load( getCurrentUser().getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);
            }


            //Get email & username from Firebase
            String email = TextUtils.isEmpty( getCurrentUser().getEmail()) ? getString(R.string.info_no_email_found) :  getCurrentUser().getEmail();


            //Update views with data
            this.textViewEmail.setText(email );



            // 5 - Get additional data from Firestore
            UserHelper.getUser( getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User currentUser = documentSnapshot.toObject(User.class);
                    String username = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.info_no_username_found) : currentUser.getUsername();
                   textUsername.setText(username);
                }
            });
        }
    }


    public void sharedpref(){



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("FirstTime", false)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,12);
            calendar.set(Calendar.MINUTE,00);
            if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.DAY_OF_MONTH, 1);
            Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(this.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), INTERVAL_FIFTEEN_MINUTES,pendingIntent);
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FirstTime", true);
            editor.apply();
        }
    }


    public void showRestaurants(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, show restaurants
            Snackbar.make(mLayout,
                    "Location permission available. Show restaurants.",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            // Permission is missing and must be requested.
            requestLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for location permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start preview Activity.
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new FirstFragment())
                        .commit();

                Snackbar.make(mLayout, "Location permission granted. Showing restaurants.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Location permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, "Location access is required to display restaurants near you.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(HungryActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                }
            }).show();

        } else {
            Snackbar.make(mLayout,
                    "Permission is not available. Requesting location permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }

}
