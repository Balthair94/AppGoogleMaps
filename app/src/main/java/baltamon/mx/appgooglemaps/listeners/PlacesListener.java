package baltamon.mx.appgooglemaps.listeners;

import baltamon.mx.appgooglemaps.models.Route;

/**
 * Created by Baltazar Rodriguez on 03/05/2017.
 */

public interface PlacesListener {
    void onLocationsChange(Route routeObject);
}
