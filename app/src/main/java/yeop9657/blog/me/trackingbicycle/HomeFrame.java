package yeop9657.blog.me.trackingbicycle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;
import yeop9657.blog.me.trackingbicycle.Network.WeatherParsing;
import yeop9657.blog.me.trackingbicycle.Sensor.Sensor;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class HomeFrame extends Fragment implements View.OnClickListener {

    /* MARK - : LocationSystem */
    private LocationSystem mLocationSystem = null;

    /* MARK - : View */
    private View mView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /* POINT - : View */
        mView = inflater.inflate(R.layout.frame_home, container, false);

        /* POINT - : GPS Location Information */
        mLocationSystem = new LocationSystem(mView, true);

        /* POINT - : Weather Information */
        new WeatherParsing(mView.getContext(), mView, mLocationSystem.dLatitude, mLocationSystem.dLongitude).execute();
    
        /* accelation Sensor info */
        new Sensor(mView);

        /* POINT - : Button */
        final Button mButton[] = new Button[]{(Button) mView.findViewById(R.id.bt_MainCall), (Button) mView.findViewById(R.id.bt_MainMessage), (Button) mView.findViewById(R.id.bt_MainRefresh)};
        for (final Button mBtn : mButton) { mBtn.setOnClickListener(this); }

        return mView;
    }

    /* MARK - : OnClickListener */
    @Override
    public void onClick(View view) {

        /* POINT - : SharedPreference */
        final SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mView.getContext());
        final String mTelNumber = mSharedPreferences.getString("key_emergency_number", null);

        /* POINT - : Vibrator */
        final Vibrator mVibrator = (Vibrator)view.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        mVibrator.vibrate(200);

        /* POINT - : Check Null String */
        if (mTelNumber == null) {
            new SweetAlertDialog(mView.getContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("ERROR PHONE NUMBER").setContentText("입력 된 휴대폰 번호가 없습니다.").setConfirmText("확인").show();
            return;
        }

        /* POINT - : Button */
        switch (view.getId()) {

            case (R.id.bt_MainCall) : {
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s", mTelNumber))));
                break;
            }

            case (R.id.bt_MainMessage) : {
                final String mContents = String.format("Lat: %f, Long: %f\n%s", mLocationSystem.dLatitude, mLocationSystem.dLongitude, mLocationSystem.convertGEOAddress(mLocationSystem.dLatitude, mLocationSystem.dLongitude));
                SmsManager.getDefault().sendTextMessage(mTelNumber, null, mContents, null, null);

                /* POINT - : SweetDialog */
                new SweetAlertDialog(mView.getContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("SUCCESS SEND MESSAGE").setContentText("성공적으로 문자를 전송하였습니다.").setConfirmText("확인").show();
                break;
            }

            case (R.id.bt_MainRefresh) : {
                /* POINT - : Weather Information */
                new WeatherParsing(mView.getContext(), mView, mLocationSystem.dLatitude, mLocationSystem.dLongitude).execute();
                break;
            }
        }
    }
}
