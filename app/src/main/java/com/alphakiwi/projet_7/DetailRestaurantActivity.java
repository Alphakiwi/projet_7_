package com.alphakiwi.projet_7;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alphakiwi.projet_7.adapter.MyAdapter;
import com.alphakiwi.projet_7.api.UserHelper;
import com.alphakiwi.projet_7.base.BaseActivity;
import com.alphakiwi.projet_7.model.Restaurant;
import com.alphakiwi.projet_7.model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alphakiwi.projet_7.BuildConfig.API_KEY;
import static com.alphakiwi.projet_7.HungryActivity.RESTAURANT;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUser;
import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;

public class DetailRestaurantActivity extends BaseActivity {

    private TextView text = null;
    private TextView lieuTel = null;
    private TextView loca = null;
    private TextView telephone = null;
    private TextView web = null;
    private ImageView image = null;
    private Button back = null;
    private Button like = null;

    private FloatingActionButton fab;
    private static final int UPDATE_RESTO = 40;
    private static final int UPDATE_RESTO2 = 50;
    private static final int UPDATE_RESTO_LIKE = 60;
    private static final int UPDATE_RESTO_LIKE2 = 70;

    public static final String RESTOLIKE = "restoLike";
    private static final String ID = "id";
    private static final String ADDRESS = "address";
    private static final String NAME = "name";

    private Restaurant resto = new Restaurant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        text = (TextView) findViewById(R.id.text);
        lieuTel = (TextView) findViewById(R.id.lieuTel);
        loca = (TextView) findViewById(R.id.loca);
        telephone = (TextView) findViewById(R.id.tel);
        web = (TextView) findViewById(R.id.web);
        image = (ImageView) findViewById(R.id.imageAvatar);

        Intent i = getIntent();
        resto = (Restaurant) i.getSerializableExtra(RESTAURANT);

        // Define a Place ID.
        String placeId = resto.getId();

// Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.PHOTO_METADATAS, Place.Field.RATING, Place.Field.OPENING_HOURS);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

// Initialize Places.
        Places.initialize(getApplicationContext(), API_KEY);

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            text.setText(place.getName() );
            lieuTel.setText(place.getName() + " (" + place.getRating() + "/5)"  );
            loca.setText(place.getAddress());

            if (place.getPhoneNumber()!= null) { telephone.setText(place.getPhoneNumber()); }
            if (place.getWebsiteUri()!= null) { web.setText(place.getWebsiteUri().toString()); }

            configureCall("tel:" + place.getPhoneNumber());
            configureWebsite(place.getWebsiteUri());

            if (place.getPhotoMetadatas()!= null) {

                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                String attributions = photoMetadata.getAttributions();
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).setMaxHeight(200).build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    Glide.with(this)
                            .load(bitmap)
                            .centerCrop()
                            .into(image);

                });
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Toast.makeText(this, getString(R.string.error_get_resto), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        configureFab();
        configureLike();
        configureBack();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listCoworkers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<User>  listUser = new ArrayList<User>();

        for(int j = 0; j < getAllUser().size(); j++){

            String restoUser = getAllUser().get(j).getResto().getId();
            int comparison = restoUser.compareTo(resto.getId()
            );

            if (comparison == 0){ listUser.add(getAllUser().get(j)); }
        }

        MyAdapter adapter = new MyAdapter(this, listUser, getUserCurrent(), false);

        recyclerView.setAdapter(adapter);



    }

    @Override
    public int getFragmentLayout() { return R.layout.activity_detail_restaurant; }

    private void configureBack() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void configureCall(String num) {
        Button call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse(num));
                startActivity(appel);

            }
        });

    }

    private void configureWebsite(Uri url) {
        Button website = findViewById(R.id.website);

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_VIEW,  url  );
                startActivity(intent);

            }
        });
    }

    private void configureLike() {
        like = findViewById(R.id.like);


        if(getUserCurrent().getRestoLike().contains(resto.getId())){
            like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24px, 0, 0);
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> newRestoLike = getUserCurrent().getRestoLike();

                if(getUserCurrent().getRestoLike().contains(resto.getId())){

                    like.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_baseline_star_border_24px,0,0);
                    newRestoLike.remove(resto.getId());
                    @SuppressWarnings("unchecked")
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put(RESTOLIKE, newRestoLike);

                    if (getCurrentUser() != null) {
                        UserHelper.updateRestoLike(newRestoLike, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO_LIKE2));
                    }

                }else {

                    like.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_star_24px, 0, 0);
                    newRestoLike.add(resto.getId());
                    @SuppressWarnings("unchecked")
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put(RESTOLIKE, newRestoLike);

                    if (getCurrentUser() != null) {
                        UserHelper.updateRestoLike(newRestoLike, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO_LIKE));
                    }
                }
            }
        });
    }

    private void configureFab() {
        fab = findViewById(R.id.fab);

        final int[] comp = {getUserCurrent().getResto().getId().compareTo(resto.getId())};
        if (comp[0] == 0) { fab.setImageResource(R.drawable.ic_baseline_done_24px); }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comp[0] = getUserCurrent().getResto().getId().compareTo(resto.getId());
                if (comp[0] != 0) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put(ADDRESS, resto.address);
                    updateMap.put(NAME, resto.name);
                    updateMap.put(ID, resto.id);

                    fab.setImageResource(R.drawable.ic_baseline_done_24px);
                    if (getCurrentUser() != null) {
                        UserHelper.updateResto(updateMap, getCurrentUser().getUid()).addOnFailureListener(onFailureListener()).addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_RESTO));
                    }

                }else{
                    @SuppressWarnings("unchecked")
                    Map<String, Object> updateMap = new HashMap();
                    updateMap.put(ADDRESS, "?");
                    updateMap.put(NAME, getString(R.string.no_choice));
                    updateMap.put(ID, "?");

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
                    case UPDATE_RESTO: Toast.makeText(DetailRestaurantActivity.this, getString(R.string.choice_eat) + " " + resto.getName(), Toast.LENGTH_SHORT).show();break;
                    case UPDATE_RESTO2: Toast.makeText(DetailRestaurantActivity.this, getString(R.string.choice_not_eat) + " " + resto.getName(), Toast.LENGTH_SHORT).show();break;
                    case UPDATE_RESTO_LIKE:Toast.makeText(DetailRestaurantActivity.this, getString(R.string.like) + " " + resto.getName(), Toast.LENGTH_SHORT).show();break;
                    case UPDATE_RESTO_LIKE2: Toast.makeText(DetailRestaurantActivity.this, getString(R.string.like_not) + " " + resto.getName(), Toast.LENGTH_SHORT).show();break;
                    default: break;
                }
            }
        };
    }
}
