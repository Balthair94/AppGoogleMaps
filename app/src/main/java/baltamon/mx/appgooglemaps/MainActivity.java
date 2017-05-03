package baltamon.mx.appgooglemaps;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import baltamon.mx.appgooglemaps.fragments.DirectionsDetailFragment;
import baltamon.mx.appgooglemaps.listeners.DirectionFinderListener;
import baltamon.mx.appgooglemaps.listeners.PlacesListener;
import baltamon.mx.appgooglemaps.models.Distance;
import baltamon.mx.appgooglemaps.models.Duration;
import baltamon.mx.appgooglemaps.models.Route;
import baltamon.mx.appgooglemaps.utilities.DirectionFinder;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private static final int MY_LOCATION_PERMISSIONS = 01;

    private GoogleMap googleMap;

    private ArrayList<Marker> markers = new ArrayList<>();

    private List<Polyline> polylinePaths;
    private ProgressDialog progressDialog;

    private PlacesListener placesListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpToolbar();
        setUpMap();
        showFragment();
    }

    public void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Google Maps");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (googleMap != null){
            showCurrentLocation();
            onMapActions();
        }
    }

    public void onMapActions(){
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (markers.size() == 0){
                    markers.add(googleMap.addMarker(new MarkerOptions().position(latLng).title("Origin")));
                } else if (markers.size() == 1){
                    markers.add(googleMap.addMarker(new MarkerOptions().position(latLng).title("Destination")));
                    sendRequest(markers.get(0).getPosition(), markers.get(1).getPosition());
                } else {
                    markers.get(1).setTitle("Origin");
                    markers.get(0).remove(); //Remove marker from the map
                    markers.remove(0); //Remove marker from the list
                    markers.add(googleMap.addMarker(new MarkerOptions().position(latLng).title("Destination")));
                    sendRequest(markers.get(0).getPosition(), markers.get(1).getPosition());
                }
            }
        });
    }

    public void showFragment(){
        Route route = new Route();
        route.setDuration(new Duration("00 min.", 0));
        route.setDistance(new Distance("00 km.", 0));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DirectionsDetailFragment fragment = DirectionsDetailFragment.newInstance(route);
        transaction.replace(R.id.fragment_layout, fragment);
        placesListener = fragment;
        transaction.commit();
    }

    public void sendRequest(LatLng origin, LatLng destination){
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //WE CALL THE DIALOGBOX TO REQUEST FOR THE PERMISSIONS
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSIONS);
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_LOCATION_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showCurrentLocation();
                break;
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes, Route routeObject) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();

        if (!routes.isEmpty()){
            for (Route route : routes) {
                PolylineOptions polylineOptions = new PolylineOptions().
                        color(Color.BLUE).
                        width(5);

                for (int i = 0; i < route.getPoints().size(); i++)
                    polylineOptions.add(route.getPoints().get(i));

                polylinePaths.add(googleMap.addPolyline(polylineOptions));

            }

            //It allows to know to the Fragment that there is a new Route
            placesListener.onLocationsChange(routeObject);
        } else
            Toast.makeText(this, "There is no results", Toast.LENGTH_SHORT).show();
    }

}
