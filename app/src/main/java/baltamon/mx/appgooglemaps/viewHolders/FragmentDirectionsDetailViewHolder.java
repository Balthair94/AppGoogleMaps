package baltamon.mx.appgooglemaps.viewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import baltamon.mx.appgooglemaps.R;

/**
 * Created by Baltazar Rodriguez on 03/05/2017.
 */

public class FragmentDirectionsDetailViewHolder {

    private TextView tv_origin;
    private TextView tv_destination;
    private TextView tv_distance;
    private TextView tv_time;
    private LinearLayout ll_origin;
    private LinearLayout ll_destination;

    public FragmentDirectionsDetailViewHolder(View view){
        tv_origin = (TextView) view.findViewById(R.id.tv_origin);
        tv_destination = (TextView) view.findViewById(R.id.tv_destination);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        ll_origin = (LinearLayout) view.findViewById(R.id.ll_origin);
        ll_destination = (LinearLayout) view.findViewById(R.id.ll_destination);
    }

    public TextView getTv_origin() {
        return tv_origin;
    }

    public TextView getTv_destination() {
        return tv_destination;
    }

    public TextView getTv_distance() {
        return tv_distance;
    }

    public TextView getTv_time() {
        return tv_time;
    }

    public LinearLayout getLl_origin() {
        return ll_origin;
    }

    public LinearLayout getLl_destination() {
        return ll_destination;
    }
}
