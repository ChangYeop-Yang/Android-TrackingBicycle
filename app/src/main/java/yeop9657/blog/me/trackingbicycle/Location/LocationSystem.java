package yeop9657.blog.me.trackingbicycle.Location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.util.List;

import yeop9657.blog.me.trackingbicycle.R;

/**
 * Created by 양창엽 on 2017-12-11.
 */

public class LocationSystem
{
    /* MARK - : Double */
    public double dLatitude = 0.0;
    public double dLongitude = 0.0;

    /* MARK - : Long */
    private final static long MIN_DISTANCE_UPDATE = 10;
    private final static long MIN_TIME_UPDATE = 1000 * 60 * 1;

    /* MARK - : View */
    private View mView = null;

    /* MARK - : String */
    private final static String TAG = LocationSystem.class.getSimpleName();

    /* MARK - : Location Creator */
    public LocationSystem(final View mView, final boolean isChecked) {
        this.mView = mView;

        /* MARK - : Setting Location Manager */
        if (mView != null && isChecked) { setLocationManager(mHomeLocationListener); }
        else { setLocationManager(mMapLocationListener); }
    }

    /* MARK - : User Custom Method */
    @SuppressLint("MissingPermission")
    private void setLocationManager(final LocationListener mLocationListener)
    {
        /* POINT - : Resource */
        final Resources mResources = mView.getContext().getResources();

        try
        {
            final LocationManager mLocationManager = (LocationManager)mView.getContext().getSystemService(mView.getContext().LOCATION_SERVICE);
            final boolean bGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            final boolean bNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /* POINT - : Import Network Provider */
            if (bNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, mLocationListener);

                if (mLocationManager != null) {
                    dLatitude = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                    dLongitude = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                }
            }

            /* POINT - : Import GPS Provider */
            if (bGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, mLocationListener);

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
            new SweetAlertDialog(mView.getContext(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Disable GPS System").setContentText(mResources.getString(R.string.ERROR_LOCATION_ACCESS)).setConfirmText("확인").show();
        }
    }

    /* TODO - : Coordinate to GEO Address Method */
    public final String convertGEOAddress(final double Latitude, final double Longitude)
    {
        /* Google Geocoder 을 위한 객체 생성 */
        final Geocoder geocoder = new Geocoder(mView.getContext());

        try {
            final List<Address> mAddressList = geocoder.getFromLocation(Latitude, Longitude, 1);

             /* 해당 지역의 정보를 받은 뒤 작동이 되는 구문 */
            if( (mAddressList != null) && (mAddressList.size() != 0) ) {
                final Address address = mAddressList.get(0);

                final StringBuffer mStringBuffer = new StringBuffer();
                mStringBuffer.append(address.getPostalCode() == null ? "" : address.getPostalCode());       mStringBuffer.append(" ");
                mStringBuffer.append(address.getLocality() == null ? "" : address.getLocality());           mStringBuffer.append(" ");
                mStringBuffer.append(address.getSubLocality() == null ? "" : address.getSubLocality());     mStringBuffer.append(" ");
                mStringBuffer.append(address.getThoroughfare() == null ? "" : address.getThoroughfare());   mStringBuffer.append(" ");
                mStringBuffer.append(address.getFeatureName() == null ? "" : address.getFeatureName());

                return mStringBuffer.toString();
            }
        }
        catch (NumberFormatException e) { e.printStackTrace(); Log.e(TAG, e.getMessage()); }
        catch (IOException e) { e.printStackTrace(); Log.e(TAG, e.getMessage()); }

        return "Not Geo Address.";
    }

    /* TODO - : Location Listener */
    private LocationListener mHomeLocationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(final Location location) {

            if (location != null) {
                dLatitude = location.getLatitude();
                dLongitude = location.getLongitude();

            /* POINT - : Update UI GPS Information */
                ((Activity) mView.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final StringBuffer mStringBuffer = new StringBuffer();
                        mStringBuffer.append("※ Coordinate: "); mStringBuffer.append(String.format("%f, %f", location.getLatitude(), location.getLongitude())); mStringBuffer.append("\n\n");
                        mStringBuffer.append("※ Accuracy(정확도): ");   mStringBuffer.append(location.getAccuracy());   mStringBuffer.append("\n\n");
                        mStringBuffer.append("※ Speed(속도): ");        mStringBuffer.append(location.getSpeed());      mStringBuffer.append("\n\n");
                        mStringBuffer.append("※ Altitude(고도): ");     mStringBuffer.append(location.getAltitude());   mStringBuffer.append("\n\n");
                        mStringBuffer.append("※ Bearing(방향): ");      mStringBuffer.append(location.getBearing());    mStringBuffer.append("\n\n");
                        mStringBuffer.append("※ Address(주소): ");      mStringBuffer.append(convertGEOAddress(location.getLatitude(), location.getLongitude()));

                        final TextView mTextView = (TextView)mView.findViewById(R.id.tv_MainGPSInfo);
                        mTextView.setText(mStringBuffer.toString());
                    }
                });
            }
        }

        @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override public void onProviderEnabled(String s) {}
        @Override public void onProviderDisabled(String s) {}
    };

    private LocationListener mMapLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                dLatitude = location.getLatitude();
                dLongitude = location.getLongitude();
            }
        }

        @Override public void onStatusChanged(String s, int i, Bundle bundle) {}
        @Override public void onProviderEnabled(String s) {}
        @Override public void onProviderDisabled(String s) {}
    };
}
