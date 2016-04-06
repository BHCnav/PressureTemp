package com.bhcnav.pressuretemp;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView tv_temperature;
	private TextView tv_pressure;
	private TextView tv_humitity;

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private Sensor mSensor_Temperature;// zsj1130

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
		initView();
		initEvent();
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

		mSensor_Temperature = mSensorManager
				.getDefaultSensor(Sensor.TYPE_TEMPERATURE);// zsj1130

		mSensorManager.registerListener(sensorEventListener, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(sensorEventListener_temp,
				mSensor_Temperature, SensorManager.SENSOR_DELAY_NORMAL);
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_pressure = (TextView) findViewById(R.id.tv_pressure);
		tv_humitity = (TextView) findViewById(R.id.tv_humidity);
	}

	private void initData() {
		// TODO Auto-generated method stub

	}

	private final SensorEventListener sensorEventListener_temp = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			System.out.println("zsj_Barometertest" + "onAccuracyChanged");
		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			System.out.println("zsj_Barometertest:" + "temp_onSensorChanged");
			float temperatureValue = event.values[0];
			BigDecimal bd = new BigDecimal(temperatureValue);
			double temperature = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
			// Log.i("Sensor", "sensor changed==>" + millibars_of_pressure);
			tv_temperature.setText(temperature + "°Ê");

		}

	};

	private final SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			float value = event.values[0];
			String promt = String.valueOf(value);
			int length = promt.split("\\.")[0].length();
			if (length == 4) {
				promt += "HPa";
			} else if (length == 3) {
				promt += "KPa";
			} else if (length > 4) {
				promt += "Pa";
			}
			tv_pressure.setText(promt);

			float sPV = event.values[0];
			DecimalFormat df = new DecimalFormat("0.00");
			df.getRoundingMode();
			// º∆À„∫£∞Œ
			double height = 44330000 * (1 - (Math.pow(
					(Double.parseDouble(df.format(sPV)) / 1013.25),
					(float) 1.0 / 5255.0)));
			tv_humitity.setText(df.format(height) + "m");
		}
	};

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(sensorEventListener);
		mSensorManager.unregisterListener(sensorEventListener_temp);
		super.finish();
	}

}
