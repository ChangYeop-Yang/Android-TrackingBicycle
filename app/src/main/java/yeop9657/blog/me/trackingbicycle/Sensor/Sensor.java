package yeop9657.blog.me.trackingbicycle.Sensor;

import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;

import yeop9657.blog.me.trackingbicycle.R;

/**
 * Created by jw565 on 2017-12-14.
 */

public class Sensor {
	TextView textview_sensor_info;
	View view = null;

	public Sensor(View v){
		view = v;
		textview_sensor_info= v.findViewById(R.id.tv_MainDeviceInfo);
		textview_sensor_info.setText("");
		
		int delay = SensorManager.SENSOR_DELAY_UI;
		SensorManager sensors = (SensorManager)v.getContext().getSystemService(Context.SENSOR_SERVICE);
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_ORIENTATION), delay);
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY), delay);
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER), delay);
		
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE), delay);
		
		//useless sensor
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION), delay);
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_GAME_ROTATION_VECTOR), delay);
		sensors.registerListener(mSensorListener,
				sensors.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD), delay);
	}
	
	SensorEventListener mSensorListener = new SensorEventListener() {
		public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
		}
		public void onSensorChanged(SensorEvent event) {
			if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
				return;
			
			StringBuffer totalString = new StringBuffer();
			String[] output = new String[10];
			float[] v = event.values;
			float accelation = 0.0f;
			
			//round
			for(int i=0; i<v.length; i++)
			{
				v[i] = Math.round(v[i]*100.0f)/100.0f;
			}
			
			for(int i=0; i<10; i++)
				output[i] = "";
			
			
			
			
			switch (event.sensor.getType()) {
				case android.hardware.Sensor.TYPE_ACCELEROMETER:
					output[0] +=
							"-X:" + v[0] +
									"\n -Y:" + v[1] +
									"\n -Z:" + v[2] + "\n";
					
					break;
				case android.hardware.Sensor.TYPE_GYROSCOPE:
					output[1] +=
							"-X:" + v[0] +
									"\n -Y:" + v[1] +
									"\n -Z:" + v[2] + "\n";
					
					
					accelation = (float)Math.sqrt(v[0]*v[0]+v[1]*v[1]);
					accelation = (float)Math.round(accelation*100)/100;
					
					totalString.append("※ Acceleration: "); totalString.append(accelation); totalString.append("\n");
					totalString.append(output[1]);
					textview_sensor_info.setText(totalString);
					textview_sensor_info.invalidate();
					break;
			}
		}
	};
}
