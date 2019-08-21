package com.alphakiwi.projet_7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.base.BaseActivity;
import com.alphakiwi.projet_7.model.Restaurant;
import com.alphakiwi.projet_7.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;


public class PresentationActivity extends BaseActivity {

    private TextView text = null;
    private TextView lieuTel = null;
    private TextView loca = null;
    private TextView telephone = null;
    private TextView facebook = null;
    private ImageView image = null;
    private Button back = null;
    private Button like = null;

    private FloatingActionButton fab;
    private static final int UPDATE_RESTO = 40;
    private static final int UPDATE_RESTO2 = 50;
    private static final int UPDATE_RESTO_LIKE = 60;
    private static final int UPDATE_RESTO_LIKE2 = 70;


    Restaurant resto = new Restaurant();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        text = (TextView) findViewById(R.id.text);
        lieuTel = (TextView) findViewById(R.id.lieuTel);
        loca = (TextView) findViewById(R.id.loca);
        telephone = (TextView) findViewById(R.id.tel);
        facebook = (TextView) findViewById(R.id.facebook);
        image = (ImageView) findViewById(R.id.imageAvatar);

        Intent i = getIntent();
        resto = (Restaurant) i.getSerializableExtra("resto");


        text.setText(resto.getName());
        lieuTel.setText(resto.getName());
        loca.setText(resto.getAddress());

        telephone.setText("");
        facebook.setText("");



        /*

        Intent i = getIntent();
        String id = i.getStringExtra("id");

        PlacesClient placesClient = new PlacesClient() {
            @NonNull
            @Override
            public Task<FindAutocompletePredictionsResponse> findAutocompletePredictions(@NonNull FindAutocompletePredictionsRequest findAutocompletePredictionsRequest) {
                return null;
            }

            @NonNull
            @Override
            public Task<FetchPhotoResponse> fetchPhoto(@NonNull FetchPhotoRequest fetchPhotoRequest) {
                return null;
            }

            @NonNull
            @Override
            public Task<FetchPlaceResponse> fetchPlace(@NonNull FetchPlaceRequest fetchPlaceRequest) {
                return null;
            }

            @NonNull
            @Override
            public Task<FindCurrentPlaceResponse> findCurrentPlace(@NonNull FindCurrentPlaceRequest findCurrentPlaceRequest) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return null;
                }
                return null;
            }
        };


        // Define a Place ID.
        String placeId = id;

// Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();


            text.setText(place.getName());
            lieuTel.setText(place.getName() );
            presentation.setText(place.getUserRatingsTotal());
            loca.setText(place.getAddress());
            telephone.setText(place.getPhoneNumber() );
            facebook.setText(place.getWebsiteUri().toString());







        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
            }
        });


*/
        configureFab();
        configureLike();
        configureBack();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listCoworkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<User>  listUser = new ArrayList<User>();

        for(int j = 0; j < getAllUser().size(); j++){

            String restoUser = getAllUser().get(j).getResto().getName();


            int comparaison = restoUser.compareTo(resto.getName());



            if (comparaison == 0){

                listUser.add(getAllUser().get(j));
            }

        }

        MyAdapter adapter = new MyAdapter(this, listUser, getUserCurrent(), false);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_presentation;

    }


    private void configureBack() {
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void configureLike() {
        like = findViewById(R.id.like);


        if(getUserCurrent().getRestoLike().contains(resto.getName())){
            like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24px, 0, 0);
        }


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> newRestoLike = getUserCurrent().getRestoLike();

                if(getUserCurrent().getRestoLike().contains(resto.getName())){

                    like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_baseline_star_border_24px,0,0);
                    newRestoLike.remove(resto.getName());
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put("restoLike", newRestoLike);

                    if (getCurrentUser() != null) {
                        UserHelper.updateRestoLike(newRestoLike, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO_LIKE2));
                    }

                }else {


                    like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24px, 0, 0);
                    newRestoLike.add(resto.getName());
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put("restoLike", newRestoLike);

                    if (getCurrentUser() != null) {
                        UserHelper.updateRestoLike(newRestoLike, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO_LIKE));
                    }

                }

            }
        });

    }

    private void configureFab() {
        fab = findViewById(R.id.fab);

        final int[] comp = {getUserCurrent().getResto().getName().compareTo(resto.getName())};


        if (comp[0] == 0) {

            fab.setImageResource(R.drawable.ic_baseline_done_24px);

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comp[0] = getUserCurrent().getResto().getName().compareTo(resto.getName());


                if (comp[0] != 0) {


                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put("address", resto.address);
                    updateMap.put("name", resto.name);


                    fab.setImageResource(R.drawable.ic_baseline_done_24px);
                    if (getCurrentUser() != null) {
                        UserHelper.updateResto(updateMap, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO));
                    }

                }else{


                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put("address", "?");
                    updateMap.put("name", "Pas encore choisit");


                    fab.setImageResource(R.drawable.ic_baseline_done_outline_24px);
                    if (getCurrentUser() != null) {
                        UserHelper.updateResto(updateMap, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO2));
                    }

                }

            }
        });
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case UPDATE_RESTO:
                        Toast.makeText(PresentationActivity.this, "Vous avez choisi de manger à : " + resto.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case UPDATE_RESTO2:
                        Toast.makeText(PresentationActivity.this, "Vous avez choisi de ne plus manger à : " + resto.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case UPDATE_RESTO_LIKE:
                        Toast.makeText(PresentationActivity.this, "Vous aimez " + resto.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case UPDATE_RESTO_LIKE2:
                        Toast.makeText(PresentationActivity.this, "Vous n'aimez plus " + resto.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }



}
