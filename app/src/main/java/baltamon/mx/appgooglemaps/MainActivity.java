package baltamon.mx.appgooglemaps;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import baltamon.mx.appgooglemaps.fragments.DirectionsDetailFragment;
import baltamon.mx.appgooglemaps.listeners.DirectionFinderListener;
import baltamon.mx.appgooglemaps.listeners.PlacesListener;
import baltamon.mx.appgooglemaps.models.Distance;
import baltamon.mx.appgooglemaps.models.Duration;
import baltamon.mx.appgooglemaps.models.Route;
import baltamon.mx.appgooglemaps.utilities.DirectionFinder;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {

    private static final int MY_LOCATION_PERMISSIONS = 1;

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
        if (googleMap != null) {
            showCurrentLocation();
            onMapActions();
        }
    }

    public void onMapActions() {
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (markers.isEmpty()) {
                    markers.add(googleMap.addMarker(new MarkerOptions().position(latLng).title("Origin")));
                } else if (markers.size() == 1) {
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

    public void callProgressDialog(){

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        else
            progressDialog  = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);
    }

    public void showFragment() {
        Route route = new Route();
        route.setDuration(new Duration("00 min.", 0));
        route.setDistance(new Distance("00 km.", 0));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DirectionsDetailFragment fragment = DirectionsDetailFragment.newInstance(route);
        transaction.replace(R.id.fragment_layout, fragment);
        placesListener = fragment;
        transaction.commit();
    }

    public void sendRequest(LatLng origin, LatLng destination) {
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

    //TO GET THE ADDRESS FROM A MARKER -- Use it if you need it
    /*public void getAddressMarker(Marker marker) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
            } else {
                Toast.makeText(this, "There is no address", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    showCurrentLocation();
                break;
        }
    }

    public void showToast(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDirectionFinderStart() {
        callProgressDialog();

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes, Route routeObject) {
        callProgressDialog();
        polylinePaths = new ArrayList<>();

        if (!routes.isEmpty()) {
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
