package com.alphakiwi.projet_7;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;



import java.util.Arrays;
import java.util.List;


public class PresentationActivity extends AppCompatActivity {

    private TextView text = null;
    private TextView lieuTel = null;
    private TextView presentation = null;
    private TextView loca = null;
    private TextView telephone = null;
    private TextView facebook = null;
    private ImageView image = null;
    private Button back = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);


        text = (TextView) findViewById(R.id.text);
        lieuTel = (TextView) findViewById(R.id.lieuTel);
        presentation = (TextView) findViewById(R.id.description);
        loca = (TextView) findViewById(R.id.loca);
        telephone = (TextView) findViewById(R.id.tel);
        facebook = (TextView) findViewById(R.id.facebook);
        image = (ImageView) findViewById(R.id.imageAvatar);

        Intent i = getIntent();
        String lieu = i.getStringExtra("lieu");
        String nom = i.getStringExtra("nom");



        text.setText(nom);
        lieuTel.setText(nom );
        loca.setText(lieu);

        presentation.setText("");
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


        configureBack();




        presentation.setMovementMethod(new ScrollingMovementMethod());

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



}
