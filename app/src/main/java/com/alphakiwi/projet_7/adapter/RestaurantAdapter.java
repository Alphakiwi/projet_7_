package com.alphakiwi.projet_7.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphakiwi.projet_7.DetailRestaurantActivity;
import com.alphakiwi.projet_7.R;
import com.alphakiwi.projet_7.model.Restaurant;
import com.bumptech.glide.Glide;
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
import java.util.Calendar;
import java.util.List;

import static android.view.View.GONE;
import static com.alphakiwi.projet_7.BuildConfig.API_KEY;
import static com.alphakiwi.projet_7.HungryActivity.RESTAURANT;
import static com.alphakiwi.projet_7.api.UserHelper.getAllUserWithoutMyself;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    Context context;
    ArrayList<Restaurant> list;
    LatLng here;
    double notation;

    public RestaurantAdapter(Context c, ArrayList<Restaurant> l, LatLng h){
        context = c;
        list = l;
        here=h;

    }

    public RestaurantAdapter(){

    }

    public void updateRestaurants() {
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_resto,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Define a Place ID.
        String placeId = list.get(position).getId();

// Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.RATING, Place.Field.OPENING_HOURS, Place.Field.PHOTO_METADATAS, Place.Field.LAT_LNG);

// Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);


// Initialize Places.
        Places.initialize(context, API_KEY);

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(context);

        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();

            if (place.getRating()!= null) {

                notation = place.getRating();
                list.get(position).setNote((int)Math.round(notation*100));

            }

            if (notation>=4){
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
            }else if (notation>=3 && notation <4){
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(GONE);
            } else if (notation>=2 && notation <3){
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(GONE);
                holder.star3.setVisibility(GONE);
            }else if (notation<2){
                holder.star1.setVisibility(GONE);
                holder.star2.setVisibility(GONE);
                holder.star3.setVisibility(GONE);
            }


            double dist = CalculationByDistance(here,place.getLatLng())/10 + CalculationByDistance(here,place.getLatLng())%10;
            BigDecimal bd = new BigDecimal(dist);
            bd= bd.setScale(2,BigDecimal.ROUND_DOWN);
            dist = bd.doubleValue();
            holder.distance.setText(dist + " km");
            list.get(position).setDistance((int)Math.round(dist*1000));


            Calendar cal = Calendar.getInstance();
// De Sunday = 1 à Saturday = 7
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;

            if (dayOfWeek == -1){
                dayOfWeek = 6;
            }


            if (place.getOpeningHours()!= null)
            holder.opening.setText(place.getOpeningHours().getWeekdayText().get(dayOfWeek));


            if (place.getPhotoMetadatas()!=null) {


                PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).setMaxHeight(200).build();
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    Glide.with(context)
                            .load(bitmap)
                            .centerCrop()
                            .into(holder.image);


                });
            }

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {

            }
        });

        holder.name.setText(list.get(position).getName());
        holder.descript.setText(list.get(position).getAddress());

        int nb = 0;
        for(int j = 0; j < getAllUserWithoutMyself().size(); j++){

            String restoUser = getAllUserWithoutMyself().get(j).getResto().getId();
            int comparison3 = restoUser.compareTo(list.get(position).getId());
            if (comparison3 == 0){ nb += 1; }
        }

        holder.nbWorkmate.setText(" " + nb);

        holder.descript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DetailRestaurantActivity.class);
                i.putExtra(RESTAURANT, list.get(position));
                context.startActivity(i);

            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DetailRestaurantActivity.class);
                i.putExtra(RESTAURANT, list.get(position));

                context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, descript, distance, nbWorkmate, opening ;
        ImageView image, star1, star2, star3;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_title);
            descript =  itemView.findViewById(R.id.list_desc);
            image = itemView.findViewById(R.id.list_image);
            distance =  itemView.findViewById(R.id.list_distance);
            nbWorkmate = itemView.findViewById(R.id.list_number_workmate);
            opening =  itemView.findViewById(R.id.list_ouverture);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);

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
