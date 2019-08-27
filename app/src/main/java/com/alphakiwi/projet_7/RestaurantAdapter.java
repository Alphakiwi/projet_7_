package com.alphakiwi.projet_7;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphakiwi.projet_7.model.Restaurant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    Context context;
    ArrayList<Restaurant> list;
    LatLng here;

    public RestaurantAdapter(Context c, ArrayList<Restaurant> l, LatLng h){
        context = c;
        list = l;
        here=h;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_resto,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Define a Place ID.
        String placeId = "ChIJz3kEPbrXwkcRGr5hiPD8568";

// Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.RATING, Place.Field.OPENING_HOURS, Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);


// Initialize Places.
        Places.initialize(context, "AIzaSyBO7_U7r1oST2upR26wkjwLQfYSMbAogQ4");

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(context);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            //Log.i(TAG, "Place found: " + place.getName());

            //holder.opening.setText(place.getOpeningHours().toString());
            holder.note.setText(" (" + place.getRating() + "/5)" );
            double dist = CalculationByDistance(here,place.getLatLng())/10 + CalculationByDistance(here,place.getLatLng())%10;
            BigDecimal bd = new BigDecimal(dist);
            bd= bd.setScale(2,BigDecimal.ROUND_DOWN);
            dist = bd.doubleValue();
            holder.distance.setText(dist + " km");




            PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
            String attributions = photoMetadata.getAttributions();
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).setMaxHeight(200).build();
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                Glide.with(context)
                        .load(bitmap)
                        .centerCrop()
                        .into(holder.image);


            });




        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
               // Toast.makeText(context, "Problème pour récupérer les informations d'au moins un restaurant.", Toast.LENGTH_SHORT).show();

            }
        });




        holder.name.setText(list.get(position).getName());
        holder.descript.setText(list.get(position).getAddress());

        int compteur = 0;

        for(int j = 0; j < getAllUserWithoutMyself().size(); j++){

            String restoUser = getAllUserWithoutMyself().get(j).getResto().getName();


            int comparaison = restoUser.compareTo(list.get(position).getName());



            if (comparaison == 0){

                compteur += 1;
            }

        }

        holder.nbWorkmate.setText(" " + compteur);




            holder.descript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(context, PresentationActivity.class);
                i.putExtra("resto", list.get(position));

                context.startActivity(i);


            }
        });



    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, descript, note, distance, nbWorkmate, opening ;
        ImageView image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_title);
            descript =  itemView.findViewById(R.id.list_desc);
            image = itemView.findViewById(R.id.list_image);
            note = itemView.findViewById(R.id.list_note);
            distance =  itemView.findViewById(R.id.list_distance);
            nbWorkmate = itemView.findViewById(R.id.list_number_workmate);
            opening =  itemView.findViewById(R.id.list_ouverture);
        }
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
