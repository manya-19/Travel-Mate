package io.github.project_travel_mate.destinations;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.project_travel_mate.R;

public class Distances extends AppCompatActivity {
    private FusedLocationProviderClient mFlc;
    double lat;
    double lng;
    ListView lv;
    String urlstr;
    String origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {






        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_distances );
        lv = (ListView) findViewById( R.id.lvdist );
        List<String> yal = new ArrayList<String>();
        yal.add("Delhi");
        yal.add("Bangalore");
        yal.add("Mumbai");
        yal.add("Chennai");
        yal.add("Pune");
        yal.add("Kolkata");
        yal.add("Hyderabad");
        yal.add("Jaipur");
        yal.add("Panjim");
        yal.add("Hyderabad");

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                yal );
        mFlc = LocationServices.getFusedLocationProviderClient(this);
        mFlc.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            origin = lat + "," + lng;





                        }
                    }
                });
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String usr = yal.get( i );
                Intent inte = new Intent( getApplicationContext(), Mapdisp.class );
                startActivity( inte );
                //urlstr = "https://maps.googleapis.com/maps/api/directions/json?origin="
                        //+ origin + "&destination=" + usr +
                        //"&mode=bicycling&key=" + "AIzaSyDHgEkWH92HS0zgKq68sCfawqlNoUfi5Qs";
                //Toast.makeText( Distances.this, urlstr, Toast.LENGTH_SHORT ).show();
               // JSONObject jobj = getJSONObjectFromURL( urlstr );





            }
        } );






    }
    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }
}
