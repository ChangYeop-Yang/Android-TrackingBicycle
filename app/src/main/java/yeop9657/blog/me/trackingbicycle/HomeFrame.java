package yeop9657.blog.me.trackingbicycle;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import yeop9657.blog.me.trackingbicycle.Location.LocationSystem;
import yeop9657.blog.me.trackingbicycle.Sensor.Sensor;

/**
 * Created by 양창엽 on 2017-12-09.
 */

public class HomeFrame extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mView = inflater.inflate(R.layout.frame_home, container, false);
        /* POINT - : GPS Location Information */
        new LocationSystem(mView, true);
    
        /* accelation Sensor info */
        new Sensor(mView);
//        Sensor sensor = new Sensor(mView);
    
        Button btn_Call = mView.findViewById(R.id.bt_MainCall);
        Button btn_Message = mView.findViewById(R.id.bt_MainMessage);
        
        btn_Call.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_CALL);
                
                intent.setData(Uri.parse("tel:01099991231"));
                try {
                    startActivity(intent);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        /*
        btn_Message.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v){
                SmsManager mSmsManager = SmsManager.getDefault();
                mSmsManager.sendTextMessage("01092900333", null, "위기에빠짐~ 위치 어디", null, null);
            }
        });
        */
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
    
    public void sendSMS(String smsNumber, String smsText){
    }
    
}
