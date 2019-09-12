package com.alphakiwi.projet_7;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserListResto;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;
import static com.google.android.libraries.places.api.model.TypeFilter.ESTABLISHMENT;

public class HungryActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView imageViewProfile;
    private TextView textUsername;
    private TextView textViewEmail;

    private View mLayout;

    private static final int PERMISSION_REQUEST_LOCATION = 0;
    public static final String RESTAURANT = "resto";
    private static final String API_KEY = BuildConfig.API_KEY;

    private FragmentManager fragmentManager = null;
    private FirstFragment firstFragment = new FirstFragment();
    private SecondFragment secondFragment = new SecondFragment();
    private ThirdFragment thirdFragment = new ThirdFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.navigation_mapView: fragmentManager.beginTransaction().replace(R.id.content_frame,firstFragment ).commit();
                    return true;
                case R.id.navigation_listView: fragmentManager.beginTransaction().replace(R.id.content_frame, secondFragment).commit();
                    return true;
                case R.id.navigation_workmates: fragmentManager.beginTransaction().replace(R.id.content_frame, thirdFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLayout = findViewById(R.id.mlayout);

        getAllUser();getAllUserListResto();getAllUserWithoutMyself();

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


        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        //no bindview because of the header
        imageViewProfile = (ImageView) header.findViewById(R.id.profile_activity_imageview_profile2);
        textUsername = (TextView) header.findViewById(R.id.profile_activity_edit_text_username2);
        textViewEmail = (TextView) header.findViewById(R.id.profile_activity_text_view_email2);


        updateUIWhenCreating();

        Places.initialize(getApplicationContext(), API_KEY);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint(getString(R.string.search));
        autocompleteFragment.setTypeFilter(ESTABLISHMENT);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                if (place.getTypes()!= null) {
                    if (place.getTypes().contains(Place.Type.RESTAURANT)) {

                        Intent i = new Intent(HungryActivity.this, DetailRestaurantActivity.class);
                        i.putExtra(RESTAURANT, new Restaurant(place.getName(), place.getAddress(), place.getId()));
                        startActivity(i);

                    } else { Toast.makeText(HungryActivity.this, getString(R.string.not_a_resto), Toast.LENGTH_SHORT).show(); }

                }else{
                    Intent i = new Intent(HungryActivity.this, DetailRestaurantActivity.class);
                    i.putExtra(RESTAURANT, new Restaurant(place.getName(), place.getAddress(), place.getId()));
                    startActivity(i);
                }
            }
            @Override
            public void onError(Status status) { }
        });
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_hungry_menu;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { drawer.closeDrawer(GravityCompat.START);
        } else { super.onBackPressed(); }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_my_resto) {

            int comp = getUserCurrent().getResto().getName().compareTo(getString(R.string.no_choice));
            if (comp== 0) {
                Toast.makeText(this, getString(R.string.no_choice_resto), Toast.LENGTH_SHORT).show();;
            }else {
                Intent i = new Intent(this, DetailRestaurantActivity.class);
                i.putExtra(RESTAURANT, getUserCurrent().getResto());
                startActivity(i);
            }
        } else if (id == R.id.nav_profil) {

            Intent newPage = new Intent(this, ProfilActivity.class);
            startActivity(newPage);
            this.finish();

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance().signOut(this).addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(){
        return new OnSuccessListener<Void>() {@Override  public void onSuccess(Void aVoid) { finish(); }};
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
                    if (currentUser!=null) {
                        String username = TextUtils.isEmpty(currentUser.getUsername()) ? getString(R.string.info_no_username_found) : currentUser.getUsername();
                        textUsername.setText(username);
                }   }
            });
        }
    }

    public void showRestaurants(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, show restaurants
            Snackbar.make(mLayout,
                    getString(R.string.location_permission), Snackbar.LENGTH_SHORT).show();
        } else {// Permission is missing and must be requested.
            requestLocationPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            // Request for location permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start preview Activity.
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();

                Snackbar.make(mLayout, getString(R.string.location_permission_granted), Snackbar.LENGTH_SHORT).show();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, getString(R.string.location_permission_denied), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Display a SnackBar with a button to request the missing permission.
            Snackbar.make(mLayout, getString(R.string.location_permission_required),
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(HungryActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                }
            }).show();
        } else {
            Snackbar.make(mLayout, getString(R.string.location_permission_request), Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }
}
