package baltamon.mx.appgooglemaps.listeners;

import java.util.List;

import baltamon.mx.appgooglemaps.models.Route;

/**
 * Created by Baltazar Rodriguez on 19/04/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route, Route routeObject);
}
