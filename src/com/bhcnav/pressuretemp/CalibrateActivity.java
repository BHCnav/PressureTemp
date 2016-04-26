package com.bhcnav.pressuretemp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CalibrateActivity extends Activity implements OnClickListener {

	private float mPressureCalDelata = (float) 0.0; // Calibration
	private double mTemperatureCalDelata = 0.0; // Calibration

	private float oldPressuredata = (float) 0.0; // Calibration
	private double oldTempData = 0.0; // Calibration

	private EditText et_pressure;
	private EditText et_temp;
	private Button btn_calibrate;
	static final int GET_CAL_DATA = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calibrate);

		Bundle extras = getIntent().getExtras();
		oldPressuredata = extras.getFloat("currentPressure");
		oldTempData = extras.getDouble("currentTemp");
		et_pressure = (EditText) findViewById(R.id.et_pressure);

		et_pressure.setText(Float.toString(oldPressuredata));
		et_temp = (EditText) findViewById(R.id.et_temp);
		et_temp.setText(Double.toString(oldTempData));
		btn_calibrate = (Button) findViewById(R.id.btn_calibrate);

		btn_calibrate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 数据是使用Intent返回
		double setedTempValue = Double
				.parseDouble(et_temp.getText().toString());
		if ((setedTempValue < (-273)) || (setedTempValue > 273)) {

			Toast.makeText(CalibrateActivity.this, R.string.TempValueAlert,
					Toast.LENGTH_SHORT).show();

			return;
		}

		float setedPressureValue = Float.parseFloat(et_pressure.getText()
				.toString());

		if ((setedPressureValue < 0) || (setedPressureValue > 190000)) {
			Toast.makeText(CalibrateActivity.this, R.string.PressureValueAlert,
					Toast.LENGTH_SHORT).show();

			return;
		}

		Intent intent = new Intent();
		mPressureCalDelata = setedPressureValue - oldPressuredata;
		mTemperatureCalDelata = setedTempValue - oldTempData;
		// 把返回数据存入Intent
		intent.putExtra("PressureCalDelata", mPressureCalDelata);
		intent.putExtra("TemperatureCalDelata", mTemperatureCalDelata);
		// 设置返回数据
		CalibrateActivity.this.setResult(GET_CAL_DATA, intent);
		// 关闭Activity
		CalibrateActivity.this.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
