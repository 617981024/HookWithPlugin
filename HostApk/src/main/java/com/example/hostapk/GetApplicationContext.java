package com.example.hostapk;

import android.app.Application;
import android.content.Context;

public class GetApplicationContext extends Application {

	private static Context sContext;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		sContext = base;
	}

	public static Context getContext() {
		return sContext;
	}
}
