package io.github.project_travel_mate.destinations;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import io.github.project_travel_mate.R;

public class Mapdisp extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient mFlc;
    double lat;
    double lng;
    String origin;
    private MarkerOptions mPlace1, mPlace2;
    Button mGetDirection;
    private Polyline mCurrentPolyline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mapdisp );
        mGetDirection = findViewById(R.id.btnGetDirection);
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
                        } else
                            Toast.makeText( Mapdisp.this, "null", Toast.LENGTH_SHORT ).show();
                    }
                });
        mPlace1 = new MarkerOptions().position(new LatLng(17.3850, 78.4867)).title("Location 1");
        mPlace2 = new MarkerOptions().position(new LatLng(18.5204, 73.8567)).title("Location 2");
        //String url = getUrl(mPlace1.getPosition(), mPlace2.getPosition(), "driving");
        mGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlace1 = new MarkerOptions().position(new LatLng(lat, lng)).title("Location 1");
                new FetchURL(Mapdisp.this).execute(getUrl(mPlace1.getPosition(),
                        mPlace2.getPosition(), "driving"), "driving");
            }
        });


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync( this );
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String strorigin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String strdest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = strorigin + "&" + strdest + "&" + mode;
        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyDwk_fmLyeA06V4WuBqqxu9ugOx8mJB8gI";
        return url;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(mPlace1);
        mMap.addMarker(mPlace2);
    }

    @Override
    public void onTaskDone(Object... values) {
        if (mCurrentPolyline != null)
            mCurrentPolyline.remove();
        mCurrentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
