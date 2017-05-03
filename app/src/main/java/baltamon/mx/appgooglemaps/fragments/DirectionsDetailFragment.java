package baltamon.mx.appgooglemaps.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import baltamon.mx.appgooglemaps.R;
import baltamon.mx.appgooglemaps.listeners.PlacesListener;
import baltamon.mx.appgooglemaps.models.Route;
import baltamon.mx.appgooglemaps.viewHolders.FragmentDirectionsDetailViewHolder;

/**
 * Created by Baltazar Rodriguez on 03/05/2017.
 */

public class DirectionsDetailFragment extends Fragment implements PlacesListener{

    private static final String MY_OBJECT_KEY = "taxi_service";
    private Route route;
    private FragmentDirectionsDetailViewHolder holder;

    public static DirectionsDetailFragment newInstance(Route routeObject){
        DirectionsDetailFragment fragment = new DirectionsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MY_OBJECT_KEY, routeObject);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewFragment = inflater.inflate(R.layout.fragment_directions_detail, container, false);
        holder = new FragmentDirectionsDetailViewHolder(viewFragment);
        route = getArguments().getParcelable(MY_OBJECT_KEY);
        showDetails();
        return viewFragment;
    }

    public void showDetails(){
        if (route.getStartAddress() != null){
            holder.getEt_origin().setText(route.getStartAddress());
            holder.getEt_destination().setText(route.getEndAddress());
        }
        holder.getTv_distance().setText(route.getDistance().getText());
        holder.getTv_time().setText(route.getDuration().getText());
    }

    @Override
    public void onLocationsChange(Route routeObject) {
        route = routeObject;
        showDetails();
    }
}
