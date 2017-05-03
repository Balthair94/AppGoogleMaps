package baltamon.mx.appgooglemaps.viewHolders;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import baltamon.mx.appgooglemaps.R;

/**
 * Created by Baltazar Rodriguez on 03/05/2017.
 */

public class FragmentDirectionsDetailViewHolder {

    private EditText et_origin;
    private EditText et_destination;
    private TextView tv_distance;
    private TextView tv_time;

    public FragmentDirectionsDetailViewHolder(View view){
        et_origin = (EditText) view.findViewById(R.id.et_origin);
        et_destination = (EditText) view.findViewById(R.id.et_destination);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
    }

    public EditText getEt_origin() {
        return et_origin;
    }

    public EditText getEt_destination() {
        return et_destination;
    }

    public TextView getTv_distance() {
        return tv_distance;
    }

    public TextView getTv_time() {
        return tv_time;
    }
}
