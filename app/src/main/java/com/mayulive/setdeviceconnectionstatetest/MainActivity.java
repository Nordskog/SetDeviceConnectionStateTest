package com.mayulive.setdeviceconnectionstatetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity
{
	private static final String TAG = "AudioSystemTest";

	private static final String DEVICE_OUT_WIRED_HEADPHONE_NAME  = "DEVICE_OUT_WIRED_HEADPHONE";
	private static final int DEVICE_OUT_WIRED_HEADPHONE = 0x8;

	private static int minTestCase()
	{
		Log.i("AudioSystemTest", "Test case method called");

		int response = -2;

		try
		{
			Class audioSystemClass = Class.forName("android.media.AudioSystem");

			Method AudioSystemClass_setDeviceConnectionState =
					audioSystemClass.getMethod("setDeviceConnectionState",
							int.class, int.class, String.class, String.class);

			//Enable DEVICE_OUT_WIRED_HEADPHONE, forcing audio to headphones.
			response = (int)AudioSystemClass_setDeviceConnectionState.invoke(
					audioSystemClass,
					DEVICE_OUT_WIRED_HEADPHONE,
					1,
					"",
					DEVICE_OUT_WIRED_HEADPHONE_NAME);

			Log.i(TAG,"Ret value: "+response);
		}
		catch (Exception ex)
		{
			Log.e(TAG, ex.getMessage());
		}

		return response;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button = findViewById(R.id.button);
		final TextView resultView = findViewById(R.id.output);

		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int ret = minTestCase();
				resultView.setText(String.valueOf(ret));
			}
		});
	}
}
