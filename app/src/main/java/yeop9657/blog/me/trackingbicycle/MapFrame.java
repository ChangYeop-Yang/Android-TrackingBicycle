package yeop9657.blog.me.trackingbicycle;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.Date;
import java.util.HashSet;

import io.realm.Realm;
import yeop9657.blog.me.trackingbicycle.Database.LocationAdaptor;
import yeop9657.blog.me.trackingbicycle.Database.PathAdapter;
import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;
import yeop9657.blog.me.trackingbicycle.PublicData.MapData;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class MapFrame extends Fragment implements OnMapReadyCallback{

    /* MARK - : GoogleMap */
    private GoogleMap mGoogleMap = null;

    /* MARK - : LocationSystem */
    private LocationSystem mLocationSystem = null;
    private PolylineOptions mPolylineOptions = null;

    /* MARK - : View */
    private View mView = null;

    /* MARK - : String */
    private final static String TAG = MapFrame.class.getSimpleName();

    /* MARK - : LocationAdaptor */
    private static HashSet<PathAdapter> cMyPath = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* POINT - : View */
        mView = inflater.inflate(R.layout.frame_map, container, false);

        /* POINT - : SupportMapFragment */
        final SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mv_MapView);
        mMapFragment.getMapAsync(this);

        /* POINT - : LocationSystem */
        mLocationSystem = new LocationSystem(mView, false);

        /* POINT - : Toggle Button */
        final ToggleButton mToggleButton = (ToggleButton) mView.findViewById(R.id.tb_MyLocation);
        mToggleButton.setOnCheckedChangeListener(mOnCheckedChangeListener);

        return mView;
    }

    @Override
    public void onStop() {
        super.onStop();

        cMyPath.clear();
        mGoogleMap.clear();
    }

    /* TODO - : Create Marker Method */
    private void createMarker(final String mTitle, final LatLng mLatLng, final float mColor) {

        /* POINT - : MarkerOptions */
        final MarkerOptions mMarkerOptions = new MarkerOptions();
        mMarkerOptions.title(mTitle);       mMarkerOptions.snippet(new Date().toString());
        mMarkerOptions.draggable(false);    mMarkerOptions.position(mLatLng);
        mMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(mColor));

        mGoogleMap.addMarker(mMarkerOptions);
    }

    /* TODO - : OnCheckedChangeListener */
    private ToggleButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

            /* POINT - : createMyLocationPath Method */
            createMyLocationPath(isChecked);
        }
    };

    /* TODO - : Save/End Create Path Method */
    @SuppressLint("MissingPermission")
    private void createMyLocationPath(final boolean isChecked) {

        /* POINT - : Turn On */
        if (isChecked) {

            mGoogleMap.setMyLocationEnabled(true);

            /* POINT - : Create Maker Method */
            createMarker(MapData.MARKER_START_NAME, new LatLng(mLocationSystem.dLatitude, mLocationSystem.dLongitude), BitmapDescriptorFactory.HUE_RED);
        }
        /* POINT - : Turn On */
        else {

            mGoogleMap.setMyLocationEnabled(false);

            /* POINT - : Create Maker Method */
            createMarker(MapData.MARKER_END_NAME, new LatLng(mLocationSystem.dLatitude, mLocationSystem.dLongitude), BitmapDescriptorFactory.HUE_BLUE);

            /* POINT - : SweetAlertDialog */
            new SweetAlertDialog(mView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("Save MyLocation Path").setContentText(mView.getContext().getResources().getString(R.string.ALERT_PATH_SAVE))
                    .setConfirmText("저장").setCancelText("취소").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    /* POINT - : Realm */
                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {

                            /* POINT - : LocationAdaptor */
                            final LocationAdaptor mLocationAdaptor = realm.createObject(LocationAdaptor.class);
                            mLocationAdaptor.setSaveDate(new Date());       mLocationAdaptor.setAddress(mLocationSystem.convertGEOAddress(mLocationSystem.dLatitude, mLocationSystem.dLongitude));
                            mLocationAdaptor.setBikeName("ALTON");

                            /* POINT - : PathAdapter For-Each MyPath */
                            for (final PathAdapter mPath : cMyPath) { mLocationAdaptor.setmMyPath(mPath); }
                        }
                    }, new Realm.Transaction.OnSuccess() {

                        @Override
                        public void onSuccess() {
                            Toast.makeText(mView.getContext(), "성공적으로 경로를 저장하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, new Realm.Transaction.OnError() {

                        @Override
                        public void onError(Throwable error) {
                            Toast.makeText(mView.getContext(), "경로 저장을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });

                    sweetAlertDialog.cancel();
                }
            }).show();
        }
    }

    /* TODO - : OnMapReadyCallback */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        /* POINT - : GoogleMap  */
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationSystem.dLatitude, mLocationSystem.dLongitude), 17));
        googleMap.setOnMarkerClickListener(mOnMarkerClickListener);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);

        /* POINT - : PolylineOptions */
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.width(15);      mPolylineOptions.color(Color.BLUE);
        mPolylineOptions.clickable(true);       mPolylineOptions.geodesic(true);
    }

    /* TODO - : setOnMyLocationChangeListener */
    private GoogleMap.OnMyLocationChangeListener mOnMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {

            Log.e(TAG, String.format("%f, %f", location.getLatitude(), location.getLongitude()));

            /* POINT - : Camera Update */
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

            /* POINT - : LocationSystem */
            final PathAdapter mPathAdapter = new PathAdapter();
            mPathAdapter.setLatitude(location.getLatitude());   mPathAdapter.setLongitude(location.getLongitude());
            mPathAdapter.setCurrentDate(new Date());            mPathAdapter.setfSpeed(location.getSpeed());
            cMyPath.add(mPathAdapter);

            /* POINT - : PolylineOptions */
            mPolylineOptions.add(new LatLng(location.getLatitude(), location.getLongitude()));
            mGoogleMap.addPolyline(mPolylineOptions);
        }
    };

    /* TODO - : OnMarkerClickListener */
    private GoogleMap.OnMarkerClickListener mOnMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            /* POINT - : Snackbar */
            final String mString = String.format("※ Address: %s", mLocationSystem.convertGEOAddress(marker.getPosition().latitude, marker.getPosition().longitude));
            Snackbar.make(mView, mString, Snackbar.LENGTH_SHORT).show();
            return false;
        }
    };
}
