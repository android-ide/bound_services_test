package com.appfour.boundservicestest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final int SERVICE_COUNT = 20;

	private Map<ComponentName, Messenger> services = new HashMap<ComponentName, Messenger>();
	private List<ServiceConnection> serviceConnections = new ArrayList<ServiceConnection>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ServiceConnection connection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder binder) {
				Log.d(Constants.TAG, "onServiceConnected(): " + className);
				services.put(className, new Messenger(binder));
				updateLabel();
			}

			public void onServiceDisconnected(ComponentName className) {
				Log.d(Constants.TAG, "onServiceDisconnected(): " + className);
				services.remove(className);
				updateLabel();
			}
		};

		serviceConnections.add(connection);
		for (int i = 1; i <= SERVICE_COUNT; i++) {
			try {
				Class<?> cl = Class
						.forName("com.appfour.boundservicestest.Service" + i);
				bindService(new Intent(this, cl), connection,
						Context.BIND_AUTO_CREATE);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void updateLabel() {
		if (services.size() == SERVICE_COUNT) {
			getLabel().setText("All services connected");
			getLabel().setTextColor(Color.GREEN);
		} else {
			getLabel().setText(
					"Only " + services.size() + " of " + SERVICE_COUNT
							+ " services connected");
			getLabel().setTextColor(Color.RED);
		}
	}

	private TextView getLabel() {
		return (TextView) findViewById(R.id.label);
	}
}
