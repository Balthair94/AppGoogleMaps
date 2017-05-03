package baltamon.mx.appgooglemaps.utilities;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;

import baltamon.mx.appgooglemaps.listeners.DirectionFinderListener;

/**
 * Created by Baltazar Rodriguez on 19/04/2017.
 */

public class DirectionFinder {

    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyADvszYNk-hzPf1Bsiozyjn1TvONqY337k";
    private DirectionFinderListener listener;
    private LatLng origin;
    private LatLng destination;

    public DirectionFinder(DirectionFinderListener listener, LatLng origin, LatLng destination){
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData(listener).execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {

        String latOrigin = String.valueOf(origin.latitude);
        String lngOrigin = String.valueOf(origin.longitude);

        String latDestination = String.valueOf(destination.latitude);
        String lngDestination = String.valueOf(destination.longitude);

        return DIRECTION_URL_API + "origin=" + latOrigin + "," + lngOrigin + "&destination=" +
                latDestination + "," + lngDestination + "&key=" + GOOGLE_API_KEY + "&mode=driving";
    }

}
