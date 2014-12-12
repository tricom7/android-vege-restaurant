package com.vegansoft.vegemap.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vegansoft.vegemap.VegeMapApplication;
import com.vegansoft.vegemap.model.Restaurant;

public class DbAdapter {

	private String TAG = this.getClass().getName();

	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	public DbAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public DbAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + " UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return this;
	}

	public DbAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor getAllSelectCursor(String TABLE_NAME) {
		try {
			String sql = "SELECT * FROM " + TABLE_NAME;

			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				mCur.moveToNext();
			}
			return mCur;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	// Restaurants table name
	private static final String TABLE_VEGE = "vege_restaurant";

	// Restaurants Table Columns names
	private static final String KEY_ID_NO = "id_no";
	private static final String KEY_NAME = "name";
	private static final String KEY_PROVINCE = "province";
	private static final String KEY_TEL = "tel";
	private static final String KEY_ADDR = "addr";
	private static final String KEY_HOMEPAGE = "homepage";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_ADDR_EN = "addr_en";

	String[] COLUMNS = { KEY_ID_NO, KEY_NAME, KEY_PROVINCE, KEY_TEL, KEY_ADDR,
			KEY_HOMEPAGE, KEY_LATITUDE, KEY_LONGITUDE, KEY_ADDR_EN };

	/**
	 * CRUD
	 */

	public void addRestaurant(Restaurant restaurant) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, restaurant.getName()); // Restaurant Name
		values.put(KEY_TEL, restaurant.getTel()); // Restaurant Tel
		// .....

		// Inserting Row
		db.insert(TABLE_VEGE, null, values);
		db.close(); // Closing database connection
	}

	public Restaurant getRestaurant(int id) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		Cursor cursor = db.query(TABLE_VEGE, COLUMNS, KEY_ID_NO + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Restaurant restaurant = new Restaurant();
		restaurant.setId(cursor.getInt(0));
		restaurant.setName(cursor.getString(2));
		restaurant.setProvince(cursor.getString(3));
		restaurant.setCity(cursor.getString(4));
		restaurant.setDetail_addr(cursor.getString(5));
		restaurant.setTel(cursor.getString(6));
		restaurant.setBusiness_hours(cursor.getString(7));
		restaurant.setHoliday(cursor.getString(8));
		restaurant.setType(cursor.getString(9));
		restaurant.setGrade(cursor.getString(10));
		restaurant.setHomepage(cursor.getString(11));
		restaurant.setLatitude(cursor.getDouble(12));
		restaurant.setLongitude(cursor.getDouble(13));
		restaurant.setMenu(cursor.getString(14));
		restaurant.setZip(cursor.getString(15));
		restaurant.setParking(cursor.getString(16));
		restaurant.setTel2(cursor.getString(17));
		restaurant.setDistance(cursor.getDouble(18));
		restaurant.setRough_map_desc(cursor.getString(19));
		restaurant.setPrice(cursor.getString(20));

		return restaurant;
	}

	public List<Restaurant> getAllRestaurant() {
		List<Restaurant> restaurantList = new ArrayList<Restaurant>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_VEGE;

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(cursor.getInt(0));
				restaurant.setName(cursor.getString(2));
				restaurant.setProvince(cursor.getString(3));
				restaurant.setCity(cursor.getString(4));
				restaurant.setDetail_addr(cursor.getString(5));
				restaurant.setTel(cursor.getString(6));
				restaurant.setBusiness_hours(cursor.getString(7));
				restaurant.setHoliday(cursor.getString(8));
				restaurant.setType(cursor.getString(9));
				restaurant.setGrade(cursor.getString(10));
				restaurant.setHomepage(cursor.getString(11));
				restaurant.setLatitude(cursor.getDouble(12));
				restaurant.setLongitude(cursor.getDouble(13));
				restaurant.setMenu(cursor.getString(14));
				restaurant.setZip(cursor.getString(15));
				restaurant.setParking(cursor.getString(16));
				restaurant.setTel2(cursor.getString(17));
				restaurant.setDistance(cursor.getDouble(18));
				restaurant.setRough_map_desc(cursor.getString(19));
				restaurant.setPrice(cursor.getString(20));

				// Adding restaurant to list
				restaurantList.add(restaurant);
			} while (cursor.moveToNext());
		}

		return restaurantList;
	}
	
	public HashMap<String, Restaurant> getAllVegeHash() {
		Log.d(TAG, "getAllVegeHash() ...");
		
		HashMap<String, Restaurant> restaurantHash = new HashMap<String, Restaurant>();
		// Select All Query
		String selectQuery = getSQL();

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Restaurant restaurant = new Restaurant();
				restaurant.setId(cursor.getInt(0));
				restaurant.setName(cursor.getString(2));
				restaurant.setProvince(cursor.getString(3));
				restaurant.setCity(cursor.getString(4));
				restaurant.setDetail_addr(cursor.getString(5));
				restaurant.setTel(cursor.getString(6));
				restaurant.setBusiness_hours(cursor.getString(7));
				restaurant.setHoliday(cursor.getString(8));
				restaurant.setType(cursor.getString(9));
				restaurant.setGrade(cursor.getString(10));
				restaurant.setHomepage(cursor.getString(11));
				restaurant.setLatitude(cursor.getDouble(12));
				restaurant.setLongitude(cursor.getDouble(13));
				restaurant.setMenu(cursor.getString(14));
				restaurant.setZip(cursor.getString(15));
				restaurant.setParking(cursor.getString(16));
				restaurant.setTel2(cursor.getString(17));
				restaurant.setDistance(cursor.getDouble(18));
				restaurant.setRough_map_desc(cursor.getString(19));
				restaurant.setPrice(cursor.getString(20));

				// Adding restaurant to list
				restaurantHash.put(String.valueOf(cursor.getInt(0)), restaurant);
			} while (cursor.moveToNext());
		}

		return restaurantHash;
	}
	
	
	private String getSQL() {
		String sql="SELECT * FROM " + TABLE_VEGE;
		String str_grade = "grade in (";
		String str_type = "type in (";
		String str_name = "";
		
		//채식 유형
		if (VegeMapApplication.isVegan == true) {
			Log.d(TAG, "isVegan == true");
			str_grade += "'비건', ";
		}
		
		if (VegeMapApplication.isLacto == true) {
			Log.d(TAG, "isLacto == true");
			str_grade += "'락토 채식', ";
		}
		
		if (VegeMapApplication.isOvo == true) {
			Log.d(TAG, "isOvo == true");
			str_grade += "'유란 채식', ";
		}
		
		if (VegeMapApplication.isLactoOvo == true) {
			Log.d(TAG, "isLactoOvo == true");
			str_grade += "'락토 오보', ";
		}
		
		if (VegeMapApplication.isMostly == true) {
			Log.d(TAG, "isMostly == true");
			str_grade += "'대부분 채식', ";
		}
		
		if (VegeMapApplication.isPartly == true) {
			Log.d(TAG, "isPartly == true");
			str_grade += "'일부 채식', ";
		}
		
		if (str_grade.endsWith(", ")) {
			str_grade= str_grade.substring(0, str_grade.length()-2);
		}
		
		
		//식당 유형
		if (VegeMapApplication.isRestaurant == true) {
			str_type += "'주문식', ";
		}
		
		if (VegeMapApplication.isBuffet == true) {
			str_type += "'뷔페', ";
		}
		
		if (VegeMapApplication.isBakery == true) {
			str_type += "'빵집', '떡카페', ";
		}
		
		if (VegeMapApplication.isCafe == true) {
			str_type += "'카페', ";
		}
		
		if (str_type.endsWith(", ")) {
			str_type= str_type.substring(0, str_type.length()-2);
		}
		

		//프렌차이즈 필터링
		if (VegeMapApplication.isBon != true) {
			str_name += " and name not like '본비빔밥%'";
		}
		
		if (VegeMapApplication.isThuckdam != true) {
			str_name += " and name not like '떡담%'";
		}
		
		if (VegeMapApplication.isSoDelicious != true) {
			str_name += " and name not like '소딜리셔스%'";
		}
		
		if (VegeMapApplication.isBizun != true) {
			str_name += " and name not like '빚은%'";
		}
		
		if (VegeMapApplication.isGanga != true) {
			str_name += " and name not like '강가%'";
		}
		
		if (VegeMapApplication.isSubway != true) {
			str_name += " and name not like '서브웨이%'";
		}
		
		if (VegeMapApplication.isCoffeeBean != true) {
			str_name += " and name not like '커피빈%'";
		}
		

		sql += " where is_closed='false' and " + str_grade + ") and " + str_type + ")" + str_name;
		
//		Log.d(TAG, "sql = " + sql);
		
		return sql;
	}
	

	public int updateRestaurant(Restaurant restaurant) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, restaurant.getName());
		// ......

		// updating row
		return db.update(TABLE_VEGE, values, KEY_ID_NO + " = ?",
				new String[] { String.valueOf(restaurant.getId()) });
	}
	
	public void updateConfig() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		String sql = "update config set isVegan=?, isLacto=?, isOvo = ?, isLactoOvo = ?, isMostly = ?, isPartly = ?, isRestaurant = ?, isBuffet = ?, isBakery = ?, isCafe = ?, isBon = ?, isThuckdam = ?, isSoDelicious = ?, isBizun = ?, isGanga = ?, isSubway = ?, isCoffeeBean = ?";
		Object[] bindArgs = {VegeMapApplication.isVegan, VegeMapApplication.isLacto, VegeMapApplication.isOvo, VegeMapApplication.isLactoOvo, VegeMapApplication.isMostly, VegeMapApplication.isPartly, VegeMapApplication.isRestaurant, VegeMapApplication.isBuffet, VegeMapApplication.isBakery, VegeMapApplication.isCafe, VegeMapApplication.isBon, VegeMapApplication.isThuckdam, VegeMapApplication.isSoDelicious, VegeMapApplication.isBizun, VegeMapApplication.isGanga, VegeMapApplication.isSubway, VegeMapApplication.isCoffeeBean};
		
		db.execSQL(sql, bindArgs);
	}
	
	public void updateVersion() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		String sql = "update config set DB_VERSION = ?";
		Object[] bindArgs = {VegeMapApplication.DB_VERSION};
		
		db.execSQL(sql, bindArgs);
	}

	public void deleteRestaurant(Restaurant restaurant) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		db.delete(TABLE_VEGE, KEY_ID_NO + " = ?",
				new String[] { String.valueOf(restaurant.getId()) });
		db.close();
	}

	public int getRestaurantsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_VEGE;
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
