package baltamon.mx.appgooglemaps.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import baltamon.mx.appgooglemaps.R;
import baltamon.mx.appgooglemaps.listeners.PlacesListener;
import baltamon.mx.appgooglemaps.models.Route;
import baltamon.mx.appgooglemaps.viewHolders.FragmentDirectionsDetailViewHolder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Baltazar Rodriguez on 03/05/2017.
 */

public class DirectionsDetailFragment extends Fragment implements PlacesListener{

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 2;

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
        holder.getLl_origin().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPlace();
            }
        });
        route = getArguments().getParcelable(MY_OBJECT_KEY);
        showDetails();
        return viewFragment;
    }

    public void showDetails(){
        if (route.getStartAddress() != null){
            holder.getTv_origin().setText(route.getStartAddress());
            holder.getTv_destination().setText(route.getEndAddress());
        }
        holder.getTv_distance().setText(route.getDistance().getText());
        holder.getTv_time().setText(route.getDuration().getText());
    }

    @Override
    public void onLocationsChange(Route routeObject) {
        route = routeObject;
        showDetails();
    }

    public void findPlace(){
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                    showToast(place.getAddress().toString());
                    Log.i("TAG", "Place: " + place.getName());
                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                    // TODO: Handle the error.
                    Log.i("TAG", status.getStatusMessage());
                    showToast(status.getStatusMessage());
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                    showToast("Operation canceled");
                }
                break;
        }
    }

    public void showToast(String string){
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }
}
