package com.alphakiwi.projet_7.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alphakiwi.projet_7.R;
import com.alphakiwi.projet_7.adapter.RestaurantAdapter;
import com.alphakiwi.projet_7.model.Restaurant;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import static com.alphakiwi.projet_7.BuildConfig.API_KEY;

public class SecondFragment extends Fragment {


    private static final String LOG_TAG = "ListRest";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private RecyclerView mListView;
    private View myView;
    private int counter = 0;
    private static ArrayList<Restaurant> resultList = null;
    private RestaurantAdapter adapter = new RestaurantAdapter();
    private Double lng;
    private Double lat;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.user_recycler, container, false);

        FloatingActionButton button = (FloatingActionButton ) myView.findViewById(R.id.fab);
        button.setImageResource(R.drawable.ic_filter_list);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resultList != null) {

                    switch(counter){
                        case 0:
                            Collections.sort(resultList, new Restaurant.RestaurantDistanceComparator());
                            Toast.makeText(mContext, getString(R.string.sort_distance), Toast.LENGTH_SHORT).show(); break;
                        case 1:
                            Collections.sort(resultList, new Restaurant.RestaurantMarksComparator());
                            Toast.makeText(mContext, getString(R.string.sort_marks), Toast.LENGTH_SHORT).show();        break;
                        case 2:
                            Collections.sort(resultList, new Restaurant.RestaurantAZComparator());
                            Toast.makeText(mContext, getString(R.string.sort_AZ), Toast.LENGTH_SHORT).show(); break;
                        case 3:
                            Collections.sort(resultList, new Restaurant.RestaurantZAComparator());
                            Toast.makeText(mContext, getString(R.string.sort_ZA), Toast.LENGTH_SHORT).show(); break;

                        default: //For all other cases, do this        break;
                    }

                }

                adapter.updateRestaurants();
                mListView.smoothScrollToPosition(0);

                if (counter>=3){
                    counter = 0;
                }else {
                    counter += 1;
                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        }

        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lng = location.getLongitude();
        lat = location.getLatitude();*/

        Bundle args = getArguments();
        lat = args.getDouble("lat");
        lng = args.getDouble("long");



        int radius = 10000;

        ArrayList<Restaurant> list = search(lat, lng, radius);

        if (list.size() == 0){
            Toast.makeText(mContext, getString(R.string.error_get_data), Toast.LENGTH_SHORT).show();
        }

        mListView = (RecyclerView) myView.findViewById(R.id.list);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RestaurantAdapter(getContext(), list, new LatLng(lat,lng) );

        mListView.setAdapter(adapter);


        mListView.smoothScrollToPosition(mListView.getAdapter().getItemCount() - 1);

        return myView;
    }

    public static ArrayList<Restaurant> search(double lat, double lng, int radius) {

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("location=" + lat + "," + lng);
            sb.append("&radius=" + radius);
            sb.append("&type=meal_takeaway");
            sb.append("&key=" + API_KEY);

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the descriptions from the results
            resultList = new ArrayList<Restaurant>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                Restaurant place = new Restaurant(predsJsonArray.getJSONObject(i).getString("name"),
                        predsJsonArray.getJSONObject(i).getString("vicinity"), predsJsonArray.getJSONObject(i).
                        getString("reference"));

                resultList.add(place);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }




}
