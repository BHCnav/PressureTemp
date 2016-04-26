package com.bhcnav.pressuretemp;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView tv_temperature;
	private TextView tv_pressure;

	private TextView tv_humitity;

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private Sensor mSensor_Temperature;// zsj1130

	private volatile int isCalibrating = 0;
	private volatile float pressureCalDelata = (float) 0.0; // Calibration
	private volatile double temperatureCalDelata = 0.0; // Calibration

	private volatile float currentPressure;
	private volatile double currentTemp;
	static final int GET_CAL_DATA = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initData();
		initView();
		initEvent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		// menu.add(0, Menu.FIRST, 0, R.string.calibrate);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_calibrate:
			isCalibrating = 1;
			Intent intent = new Intent(MainActivity.this,
					CalibrateActivity.class);
			intent.putExtra("currentPressure", currentPressure);
			intent.putExtra("pressureCalDelata", pressureCalDelata);
			Log.d("Temp", "currentTemp:" + currentTemp);
			intent.putExtra("currentTemp", currentTemp);
			intent.putExtra("temperatureCalDelata", temperatureCalDelata);

			this.startActivityForResult(intent, GET_CAL_DATA);
			// Toast.makeText(this, "Calibarate", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		if (mSensorManager == null) {

			Log.e("Pressure", "mSensorManager is null");
		} else {
			Log.e("Pressure", "mSensorManager is :" + mSensorManager);
		}
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

		if (mSensor == null) {

			Log.e("Pressure", "mSensor is null");
		} else {
			Log.e("Pressure", "mSensor is :" + mSensor);
		}

		mSensor_Temperature = mSensorManager
				.getDefaultSensor(Sensor.TYPE_TEMPERATURE);// zsj1130

		if (mSensor_Temperature == null) {

			Log.e("Pressure", "	mSensor_Temperature is null");
		} else {
			Log.e("Pressure", "mSensor_Temperature is :" + mSensor_Temperature);
		}
		mSensorManager.registerListener(sensorEventListener, mSensor,
				1000000);
		mSensorManager.registerListener(sensorEventListener_temp,
				mSensor_Temperature, 1000000);
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		tv_pressure = (TextView) findViewById(R.id.tv_pressure);
		tv_humitity = (TextView) findViewById(R.id.tv_humidity);
	}

	private void initData() {
		// TODO Auto-generated method stub
		// Intent intent = new Intent();

		// / intent.setClass(A.this,B.class);

		// intent.putExtra("pressure",);
		// intent.putExtra("userpwd1"userpwd);

		// this.startActivity(intent);
	}

	private void tempValueFormat(double tempValue) {
		BigDecimal bd = new BigDecimal(tempValue);
		double temperature = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
		// Log.i("Sensor", "sensor changed==>" + millibars_of_pressure);
		tv_temperature.setText(Double.toString(temperature));
		// currentTemp = temperature;
	}

	private void pressureValueFromat(float pressureValue) {
		tv_pressure.setText(Float.toString(pressureValue));
		// currentPressure = pressureValue;
		// float sPV = event.values[0];
		DecimalFormat df = new DecimalFormat("0.00");
		df.getRoundingMode();
		//
		double height = 44330000 * (1 - (Math.pow((Double.parseDouble(df
				.format(pressureValue + pressureCalDelata)) / 101325),
				(float) 1.0 / 5255.0)));
		tv_humitity.setText(df.format(height));
	}

	private final SensorEventListener sensorEventListener_temp = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			System.out.println("zsj_Barometertest" + "onAccuracyChanged");
		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			if (isCalibrating == 0) {
				System.out.println("zsj_Barometertest:"
						+ "temp_onSensorChanged:" + event.values[0]
						+ event.values[1] + event.values[2]
						+ "temperatureCalDelata" + temperatureCalDelata);
				float temperatureValue = event.values[0];

				tempValueFormat(temperatureValue + temperatureCalDelata);
				BigDecimal bd = new BigDecimal(temperatureValue);
				double temperature = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();

				currentTemp = temperature;
			}

		}

	};

	private final SensorEventListener sensorEventListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			if (isCalibrating == 0) {
				// Log.e("onSensorChanged", "onSensorChanged:" +
				// event.values[0]);
				float value = event.values[0];
				// String promt = String.valueOf(value);
				// int length = promt.split("\\.")[0].length();
				// if (length == 4) {
				// promt += "HPa";
				// value = (float) (value * 100.0);
				// } else if (length == 3) {
				// promt += "KPa";
				// value = (float) (value * 1000.0);
				// } else if (length > 4) {
				// promt += "Pa";
				// }
				currentPressure = value * 100;
				pressureValueFromat(value * 100 + pressureCalDelata);

			}

		}
	};

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(sensorEventListener);
		mSensorManager.unregisterListener(sensorEventListener_temp);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);

		 Log.e("onActivityResult", "requestCode" + requestCode + ":"
		 + "resultCode" + resultCode + ":");
		switch (resultCode) {
		case GET_CAL_DATA:
			Bundle bundle = data.getExtras();
			pressureCalDelata = bundle.getFloat("PressureCalDelata");
			temperatureCalDelata = bundle.getDouble("TemperatureCalDelata");
			// Log.e("onActivityResult", "temperatureCalDelata:"
			// + temperatureCalDelata + "currentTemp:" + currentTemp);

			// Log.e("onActivityResult", "pressureCalDelata:" +
			// pressureCalDelata
			// + "currentPressure:" + currentPressure);

			tempValueFormat(currentTemp + temperatureCalDelata);
			pressureValueFromat(currentPressure + pressureCalDelata);

			isCalibrating = 0;
			
		case RESULT_CANCELED:
			isCalibrating = 0;
			break;

		default:
			break;
		}

	}
}
