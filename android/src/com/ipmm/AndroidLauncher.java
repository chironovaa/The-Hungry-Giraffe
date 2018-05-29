package com.ipmm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.widget.Toast;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ipmm.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AndroidLauncher extends AndroidApplication implements AndroidActivity{
	PersistentStorage settings;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings.init(this.getContext());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainActivity(this), config);
	}

	@Override
	public void addProperty(String s, String r) {
		settings.addProperty(s, r);
	}

	@Override
	public String getProperty(String s) {
		return settings.getProperty(s);
	}

	@Override
	public void showToast(String s) {
		Toast.makeText(this, s, 100).show();
	}
}
