package yeop9657.blog.me.trackingbicycle;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.Date;
import java.util.HashSet;

import io.realm.Realm;
import yeop9657.blog.me.trackingbicycle.Database.LocationAdaptor;
import yeop9657.blog.me.trackingbicycle.Database.PathAdapter;
import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class MapFrame extends Fragment implements OnMapReadyCallback{

    /* MARK - : GoogleMap */
    private GoogleMap mGoogleMap = null;

    /* MARK - : LocationSystem */
    private LocationSystem mLocationSystem = null;

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

        return mView;
    }

    @Override
    public void onStop() {
        super.onStop();

        /* POINT - : SweetAlertDialog */
        new SweetAlertDialog(mView.getContext(), SweetAlertDialog.NORMAL_TYPE).setTitleText("Save MyLocation Path").setContentText(mView.getContext().getResources().getString(R.string.ALERT_PATH_SAVE))
                .setConfirmText("저장").setCancelText("취소").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                /* POINT - : Realm */
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        /* POINT - : LocationAdaptor */
                        final LocationAdaptor mLocationAdaptor = realm.createObject(LocationAdaptor.class);
                        mLocationAdaptor.setSaveDate(new Date());       mLocationAdaptor.setAddress(mLocationSystem.convertGEOAddress(mLocationSystem.dLatitude, mLocationSystem.dLongitude));
                        mLocationAdaptor.setBikeName("ALTON");

                        /* POINT - : PathAdapter For-Each MyPath */
                        for (final PathAdapter mPath : cMyPath) { mLocationAdaptor.setmMyPath(mPath); }
                    }
                });

                sweetAlertDialog.cancel();
            }
        }).show();
    }

    /* TODO - : Drawing Path Method */

    /* TODO - : OnMapReadyCallback */
    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocationSystem.dLatitude, mLocationSystem.dLongitude), 17));
    }

    /* TODO - : setOnMyLocationChangeListener */
    private GoogleMap.OnMyLocationChangeListener mOnMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {

        @Override
        public void onMyLocationChange(Location location) {

            Log.e(TAG, String.format("%f, %f", location.getLatitude(), location.getLongitude()));

            /* POINT - : LocationSystem */
            final PathAdapter mPathAdapter = new PathAdapter();
            mPathAdapter.setLatitude(location.getLatitude());   mPathAdapter.setLongitude(location.getLongitude());
            mPathAdapter.setCurrentDate(new Date());            mPathAdapter.setfSpeed(location.getSpeed());
            cMyPath.add(mPathAdapter);
        }
    };
}
