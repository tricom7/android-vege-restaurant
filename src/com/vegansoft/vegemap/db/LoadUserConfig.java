package com.vegansoft.vegemap.db;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.vegansoft.vegemap.VegeMapApplication;


public class LoadUserConfig {

	private static String TAG = LoadUserConfig.class.getName();
	
	public static void LoadUserConfigFromDB(Context context) {
		Log.d(TAG, "LoadUserConfigFromDB() ...");
		/* 1. DB에서 지리 정보 가져오기 */
		DbAdapter mDbAdapter = new DbAdapter(context);
		mDbAdapter.createDatabase();
		mDbAdapter.open();

		Cursor cursor = mDbAdapter.getAllSelectCursor("CONFIG");
		
		if (cursor != null)
			cursor.moveToFirst();

		VegeMapApplication.isVegan = (cursor.getInt(0) == 1) ? true : false; 
		VegeMapApplication.isLacto = (cursor.getInt(1) == 1) ? true : false; 
		VegeMapApplication.isOvo = (cursor.getInt(2) == 1) ? true : false; 
		VegeMapApplication.isLactoOvo = (cursor.getInt(3) == 1) ? true : false; 
		VegeMapApplication.isMostly = (cursor.getInt(4) == 1) ? true : false; 
		VegeMapApplication.isPartly = (cursor.getInt(5) == 1) ? true : false; 
		
		VegeMapApplication.isRestaurant = (cursor.getInt(6) == 1) ? true : false; 
		VegeMapApplication.isBuffet = (cursor.getInt(7) == 1) ? true : false; 
		VegeMapApplication.isBakery = (cursor.getInt(8) == 1) ? true : false; 
		VegeMapApplication.isCafe = (cursor.getInt(9) == 1) ? true : false; 
		
		VegeMapApplication.isBon = (cursor.getInt(10) == 1) ? true : false; 
		VegeMapApplication.isThuckdam = (cursor.getInt(11) == 1) ? true : false; 
		VegeMapApplication.isSoDelicious = (cursor.getInt(12) == 1) ? true : false; 
		VegeMapApplication.isBizun = (cursor.getInt(13) == 1) ? true : false; 
		VegeMapApplication.isGanga = (cursor.getInt(14) == 1) ? true : false; 
		VegeMapApplication.isSubway = (cursor.getInt(15) == 1) ? true : false; 
		VegeMapApplication.isCoffeeBean = (cursor.getInt(16) == 1) ? true : false; 
		
		VegeMapApplication.DB_VERSION = (cursor.getString(17) != null) ? cursor.getString(17) : "";
		
		mDbAdapter.close();
	}
}
