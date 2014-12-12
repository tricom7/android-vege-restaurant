package com.vegansoft.vegemap.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetwork {
	public static boolean isNetworkAvailable(Context context) {
		boolean available = false;
		
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo info = manager.getActiveNetworkInfo();
		
		if (info != null && info.isAvailable()) {
			available = true;
		}
		
		return available;
	}
}
