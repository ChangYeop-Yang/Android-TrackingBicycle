package yeop9657.blog.me.trackingbicycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;
import yeop9657.blog.me.trackingbicycle.Network.WeatherParsing;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class HomeFrame extends Fragment implements View.OnClickListener {

    private LocationSystem mLocationSystem = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View mView = inflater.inflate(R.layout.frame_home, container, false);
        /* POINT - : GPS Location Information */
        mLocationSystem = new LocationSystem(mView, true);
        new WeatherParsing(getActivity(),mView, mLocationSystem.dLatitude, mLocationSystem.dLongitude).execute();

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /* MARK - : OnClickListener */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case (R.id.bt_MainCall) : { break; }
            case (R.id.bt_MainMessage) : { break; }
            case (R.id.bt_MainRefresh) : { break; }
        }
    }
}
