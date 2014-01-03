package com.appfour.boundservicestest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

public abstract class BaseService extends Service {

	public static final int MSG_SAY_HELLO = 1;

	private class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SAY_HELLO:
				Toast.makeText(getApplicationContext(), "hello!",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	final Messenger messenger = new Messenger(new IncomingHandler());

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(Constants.TAG, "onBind(): " + getClass().getName());
		return messenger.getBinder();
	}

	@Override
	public void onCreate() {
		Log.d(Constants.TAG, "onCreate(): " + getClass().getName());
	}

	@Override
	public void onDestroy() {
		Log.d(Constants.TAG, "onDestroy(): " + getClass().getName());
	}

	@Override
	public void onLowMemory() {
		Log.d(Constants.TAG, "onLowMemory(): " + getClass().getName());
	}
}
