package yeop9657.blog.me.trackingbicycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;
import yeop9657.blog.me.trackingbicycle.Network.WeatherParsing;
import yeop9657.blog.me.trackingbicycle.Sensor.Sensor;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class HomeFrame extends Fragment implements View.OnClickListener {

    /* MARK - : LocationSystem */
    private LocationSystem mLocationSystem = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.frame_home, container, false);

        /* POINT - : GPS Location Information */
        mLocationSystem = new LocationSystem(mView, true);

        /* POINT - : Weather Information */
        new WeatherParsing(mView.getContext(), mView, mLocationSystem.dLatitude, mLocationSystem.dLongitude).execute();
    
        /* accelation Sensor info */
        new Sensor(mView);

        return mView;
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
