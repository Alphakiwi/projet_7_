package com.alphakiwi.projet_7.get_nearby_place_data;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alphakiwi.projet_7.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import static com.alphakiwi.projet_7.api.UserHelper.getUserCurrent;


public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;
    List<String> coworkersResto;
    Context context;

    public GetNearbyPlacesData ( List<String> coworkersResto, Context c ) {
        this.coworkersResto = coworkersResto;
        this.context = c;
    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
            Toast.makeText(context, context.getString(R.string.error_get_data), Toast.LENGTH_SHORT).show();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        if (nearbyPlacesList.size() == 0){
            Toast.makeText(context, context.getString(R.string.error_get_data), Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));


            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");

            String id_place =  googlePlace.get("reference");



            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.snippet(vicinity + " \n :" + id_place);
            markerOptions.title(placeName );


            if (coworkersResto.contains(id_place)) {
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else{

                if (getUserCurrent().getRestoLike().contains(id_place)){
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                 }else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                }
            }

            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        }
    }
}
