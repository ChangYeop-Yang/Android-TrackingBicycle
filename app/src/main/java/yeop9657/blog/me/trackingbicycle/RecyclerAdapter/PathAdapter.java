package yeop9657.blog.me.trackingbicycle.RecyclerAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.text.DateFormat;

import io.realm.Realm;
import io.realm.RealmResults;
import yeop9657.blog.me.trackingbicycle.Database.LocationAdaptor;
import yeop9657.blog.me.trackingbicycle.PublicData.MapData;
import yeop9657.blog.me.trackingbicycle.R;

/**
 * Created by 양창엽 on 2017-12-15.
 */

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.ViewHolder> {

    /* MARK - : ArrayList */
    private RealmResults<LocationAdaptor> mLocationAdaptorList = null;

    /* MARK - : PathAdapter */
    private PathAdapter mPathAdapter = this;

    /* MARK - : View */
    private View mView = null;

    /* TODO - : Creator PathAdapter */
    public PathAdapter(final View mView, RealmResults<LocationAdaptor> mLocationAdaptorList) {
        this.mView = mView;
        this.mLocationAdaptorList = mLocationAdaptorList;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        /* POINT - : DateFormat */
        final DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

        /* POINT - : TextView */
        holder.tv_BikeName.setText( String.format("%d - %s Type", position + 1, mLocationAdaptorList.get(position).getBikeName()) );
        holder.tv_Address.setText( mLocationAdaptorList.get(position).getAddress() );
        holder.tv_Date.setText( String.format("#%s", mDateFormat.format(mLocationAdaptorList.get(position).getSaveDate())) );

        /* POINT - : Integer */
        final int mPathLength = mLocationAdaptorList.get(position).getMyPath().size() - 1;

        /* POINT - : StringBuffer */
        final StringBuffer mURLBuffer = new StringBuffer();
        mURLBuffer.append("https://maps.googleapis.com/maps/api/staticmap?center=");
        mURLBuffer.append(mLocationAdaptorList.get(position).getMyPath().get(mPathLength / 2).getLatitude());   mURLBuffer.append(",");
        mURLBuffer.append(mLocationAdaptorList.get(position).getMyPath().get(mPathLength / 2).getLongitude());  mURLBuffer.append("&");
        /* POINT - : Start Marker */
        mURLBuffer.append("&markers=color:red|label:S|");
        mURLBuffer.append(mLocationAdaptorList.get(position).getMyPath().get(0).getLatitude());                 mURLBuffer.append(",");
        mURLBuffer.append(mLocationAdaptorList.get(position).getMyPath().get(0).getLongitude());                mURLBuffer.append("&");
        /* POINT - : End Marker */
        mURLBuffer.append("&markers=color:blue|label:E|");
        mURLBuffer.append(mLocationAdaptorList.get(position).getMyPath().get(mPathLength).getLatitude());                 mURLBuffer.append(",");
        mURLBuffer.append(mLocationAdaptorList.get(position).getMyPath().get(mPathLength).getLongitude());                mURLBuffer.append("&");
        /* POINT - : Path */
        mURLBuffer.append("path=color:0xff0000ff|weight:5");
        for (int mCount = 0; mCount <= mPathLength; mCount++) {
            mURLBuffer.append(String.format("|%f,%f", mLocationAdaptorList.get(position).getMyPath().get(mCount).getLatitude(), mLocationAdaptorList.get(position).getMyPath().get(mCount).getLongitude()));
        }
        mURLBuffer.append("&zoom=18&size=400x200&scale=2"); mURLBuffer.append(MapData.MAP_STATIC_API_KEY);

        /* POINT - : ImageView */
        Glide.with(mView).load(mURLBuffer.toString()).into(holder.iv_Path);

        /* POINT - : CardView */
        holder.cv_CardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                /* POINT - : Dialog */
                final Dialog mDialog = new Dialog(mView.getContext());
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.setContentView(R.layout.alert_map);
                mDialog.show();

                /* POINT - : TextView */
                final TextView mInfoTextView = (TextView) mDialog.findViewById(R.id.tv_AlertInfo);
                final StringBuffer mPathInfo = new StringBuffer();
                mPathInfo.append(String.format("※ Address: %s\n", mLocationAdaptorList.get(position).getAddress()));
                mPathInfo.append(String.format("\n※ Bicycle Type: %s\n", mLocationAdaptorList.get(position).getBikeName()));
                mPathInfo.append(String.format("\n※ Start Date: %s\n", DateFormat.getDateInstance(DateFormat.FULL).format(mLocationAdaptorList.get(position).getSaveDate())));
                mInfoTextView.setText(mPathInfo.toString());

                /* POINT - : syncGoogleMap Method */
                syncGoogleMap(mDialog, position);
            }
        });

        holder.cv_CardView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                /* POINT - : Vibrator */
                final Vibrator mVibrator = (Vibrator)view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrator.vibrate(200);

                /* POINT - : SweetDialog */
                new SweetAlertDialog(mView.getContext(), SweetAlertDialog.WARNING_TYPE).setTitleText("DELETE PATH ITEM").setConfirmText("확인").setCancelText("취소")
                        .setContentText("해당 아이템을 삭제하시겠습니까?").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(final SweetAlertDialog sweetAlertDialog) {

                        /* POINT - : Realm */
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                /* POINT - : Delete RealmResults */
                                mLocationAdaptorList.get(position).deleteFromRealm();

                                /* POINT - : Delete mPathAdapter */
                                mPathAdapter.notifyItemRemoved(position);
                                mPathAdapter.notifyDataSetChanged();

                                sweetAlertDialog.cancel();
                            }
                        });
                    }
                }).show();

                return false;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_path, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public int getItemCount() { return mLocationAdaptorList.size(); }

    /* TODO - : Sync Google Map Method */
    private void syncGoogleMap(final Dialog mDialog, final int mPosition) {

        /* POINT - : Button */
        final Button mConfirm = (Button) mDialog.findViewById(R.id.bt_AlertConfirm);

        /* POINT - : MapView */
        final MapView mMapView = (MapView) mDialog.findViewById(R.id.mv_MapView);
        MapsInitializer.initialize(mView.getContext());
        mMapView.onCreate(mDialog.onSaveInstanceState());
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                /* POINT - : LatLng */
                final int mLength = mLocationAdaptorList.get(mPosition).getMyPath().size() - 1;
                final LatLng mLatLng = new LatLng(mLocationAdaptorList.get(mPosition).getMyPath().get(mLength / 2).getLatitude(), mLocationAdaptorList.get(mPosition).getMyPath().get(mLength / 2).getLongitude());

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 18));
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                /* POINT - : Marker */
                googleMap.addMarker(new MarkerOptions().title(MapData.MARKER_START_NAME).snippet(mLocationAdaptorList.get(mPosition).getSaveDate().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(new LatLng(mLocationAdaptorList.get(mPosition).getMyPath().get(0).getLatitude(), mLocationAdaptorList.get(mPosition).getMyPath().get(0).getLongitude())));
                googleMap.addMarker(new MarkerOptions().title(MapData.MARKER_END_NAME).snippet(mLocationAdaptorList.get(mPosition).getSaveDate().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .position(new LatLng(mLocationAdaptorList.get(mPosition).getMyPath().get(mLength).getLatitude(), mLocationAdaptorList.get(mPosition).getMyPath().get(mLength).getLongitude())));

                /* POINT - : Path */
                final PolylineOptions mPolylineOptions = new PolylineOptions().color(Color.BLUE).width(15).geodesic(true);
                for (int mCount = 0; mCount <= mLength; mCount++) {
                    mPolylineOptions.add(new LatLng(mLocationAdaptorList.get(mPosition).getMyPath().get(mCount).getLatitude(), mLocationAdaptorList.get(mPosition).getMyPath().get(mCount).getLongitude()));
                }
                googleMap.addPolyline(mPolylineOptions);
            }
        });

        /* POINT - : Button Listener */
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* POINT - : Vibrator */
                final Vibrator mVibrator = (Vibrator)view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrator.vibrate(200);

                mDialog.dismiss();
                mMapView.onDestroy();
            }
        });
    }

    /* TODO - : ViewHolder class */
    public static class ViewHolder extends RecyclerView.ViewHolder {

       /* MARK - : TextView */
       private TextView tv_BikeName = null;
       private TextView tv_Date = null;
       private TextView tv_Address = null;

       /* MARK - : ImageView */
       private ImageView iv_Path = null;

       /* MARK - : CardView */
        private CardView cv_CardView = null;

        private ViewHolder(View itemView) {
            super(itemView);

            /* POINT - : TextView */
            tv_BikeName = (TextView) itemView.findViewById(R.id.tv_ItemBikeName);
            tv_Address = (TextView) itemView.findViewById(R.id.tv_ItemAddress);
            tv_Date = (TextView) itemView.findViewById(R.id.tv_ItemDate);

            /* POINT - : ImageView */
            iv_Path = (ImageView) itemView.findViewById(R.id.iv_ItemMap);

            /* POINT - : CardView */
            cv_CardView = (CardView) itemView.findViewById(R.id.cv_ItemCard);
        }
    }
}
