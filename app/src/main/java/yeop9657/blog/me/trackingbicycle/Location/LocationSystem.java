package yeop9657.blog.me.trackingbicycle.Location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 양창엽 on 2017-12-11.
 */

public class LocationSystem implements LocationListener
{
    /* POINT - : Double */
    private double dLatitude = 0.0;
    private double dLongitude = 0.0;

    /* POINT - : LocationManager */
    private LocationManager mLocationManager = null;

    /* POINT - : Long */
    private final static long MIN_DISTANCE_UPDATE = 10;
    private final static long MIN_TIME_UPDATE = 1000 * 60 * 1;

    /* MARK - : Activity */
    private Activity mActivity = null;

    /* POINT - : Location Creator */
    public LocationSystem(final Activity mActivity) {
        this.mActivity = mActivity;
        setLocationManager();
    }

    /* MARK - : User Custom Method */
    @SuppressLint("MissingPermission")
    private void setLocationManager()
    {
        try
        {
            mLocationManager = (LocationManager)mActivity.getSystemService(mActivity.LOCATION_SERVICE);
            final boolean bGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            final boolean bNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /* POINT - : Check GPS and Network Provider */
            if (!bGPSEnabled && !bNetworkEnabled)
            { new SweetAlertDialog(mActivity, SweetAlertDialog.ERROR_TYPE).setTitleText("Disable GPS System").setConfirmText("현재 장치에서 GPS System을 사용할 수가 없습니다.").show(); return; }

            /* POINT - : Import Network Provider */
            if (bNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

                if (mLocationManager != null) {
                    dLatitude = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                    dLongitude = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                }
            }

            /* POINT - : Import GPS Provider */
            if (bGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

                if (mLocationManager != null) {
                    dLatitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                    dLongitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                }
            }
        }
        catch (Exception error)
        {
            error.printStackTrace();
            Log.e("Location GPS Error!", error.getMessage());
            new SweetAlertDialog(mActivity, SweetAlertDialog.ERROR_TYPE).setTitleText("Disable GPS System").setConfirmText("현재 장치에서 GPS System을 사용할 수가 없습니다.").show();
        }
    }

    public final double getLatitude() { return this.dLatitude; }
    public final double getLongitude() { return this.dLongitude; }


    /* TODO - : Location Listener */
    @Override
    public void onLocationChanged(Location location) {

        if (mLocationManager != null) {
            dLatitude = location.getLatitude();
            dLongitude = location.getLongitude();

        }
    }

    @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
    @Override public void onProviderEnabled(String s) {}
    @Override public void onProviderDisabled(String s) {}
}
