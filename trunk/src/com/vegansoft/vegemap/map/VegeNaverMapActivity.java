package com.vegansoft.vegemap.map;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.vegansoft.vegemap.R;
import com.vegansoft.vegemap.config.ConfigActivity;
import com.vegansoft.vegemap.db.DbAdapter;
import com.vegansoft.vegemap.model.Restaurant;
import com.vegansoft.vegemap.network.CheckNetwork;
import com.vegansoft.vegemap.nmap.NMapPOIflagType;
import com.vegansoft.vegemap.nmap.NMapViewerResourceProvider;

public class VegeNaverMapActivity extends NMapActivity {
	private String TAG = this.getClass().getName();

	private static final boolean DEBUG = true;

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume() ...");

		drawMap(-1); // 줌 레벨을 변경하지 않도록 인자로 -1을 줌

		super.onResume();
	}

	private static final String API_KEY = "062557525ff1138bff7d401e18c9fdc5";

	private MapContainerView mMapContainerView;

	private NMapView mMapView;

	private NMapMyLocationOverlay mMyLocationOverlay;
	private NMapLocationManager mMapLocationManager;
	private NMapCompassManager mMapCompassManager;

	private NMapViewerResourceProvider mMapViewerResourceProvider; // 오버레이 객체에 이미지 리소스 제공
	private NMapOverlayManager mOverlayManager; // 오버레이 객체 관리
	private NMapController mMapController;

	private NMapPOIdataOverlay mPoiDataOverlay;

	private NMapPOIdata poiData; // 여러 오버레이 아이템을 하나의 오버레이 객체에서 관리하기 위해 NMapPOIdata 객체 생성

	private HashMap<String, Restaurant> restaurantHash;

	private static int zoomLevel = 3; // 초기값

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate()...");

		drawMap(3);
	}

	private void drawMap(int zoomLevel) {
		/* 1. 지도 표시 */
		Log.d(TAG, "");
		mMapView = new NMapView(this);

		// create parent view to rotate map view
		mMapContainerView = new MapContainerView(this);
		mMapContainerView.setmMapView(mMapView);

		mMapContainerView.addView(mMapView);

		// set the activity content to the parent view
		setContentView(mMapContainerView);

		mMapView.setApiKey(API_KEY);
		// setContentView(mMapView);

		mMapView.setClickable(true);

		// 지도의 확대/축소, 중앙위치 지정에 필요한 NMapController 객체 생성
		mMapController = mMapView.getMapController();

		// 지도의 중앙에 표시할 경도, 위도 및 지도의 확대 수준을 지정 (확대 수준은 1~14 사이임 값이 클수록 크게 확대한 것이다.)
		if (zoomLevel != -1) { // -1로 인자가 전달되면 줌 레벤이나 현재 지도 위치를 변경 시키지 않음(식당 상제 정보에서 back 버튼으로 돌아올 경우)
			mMapController.setMapCenter(new NGeoPoint(127.862314, 36.350649), zoomLevel); // 경도, 위도, 확대비율
		}

		/* 2. 지도에 오버레이 표시 */
		// 오버레이 객체에 이미지 리소스를 제공하는 NMapViewerResourceProvider 객체 생성
		mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

		// 지도상에 표시되는 오버레이 객체를 관리하는 NMapOverlayManager 클래스의 객체 생성
		mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);

		makePoiData();

		// 여러 오버레이 아이템이 등록된 NMapPOIdata 객체를 갖고, 하나의 NMapPOIdataOverlay 객체 생성
		mPoiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null); // (인자: , 기본 마커를 지정하는 것으로 일반적으로 null 을 사용함)

		// 모든 오버레이가 화면에 표시되도록 지도의 중심과 확대 정도를 변경
		// mPoiDataOverlay.showAllPOIdata(3); // (인자: 줌 레벨)

		/* 3. 말풍선에 이벤트 연결하기 */
		// set event listener to the overlay
		mPoiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);

		/* 4. init Map for GPS */
		// location manager
		mMapLocationManager = new NMapLocationManager(this);
		mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);

		// compass manager
		mMapCompassManager = new NMapCompassManager(this);

		// create my location overlay
		mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);

		/* 5. 기타 */
		mMapContainerView.setmOverlayManager(mOverlayManager);

		if (!CheckNetwork.isNetworkAvailable(this)) {
			Toast toast = Toast.makeText(this, "현재 네트워크 사용이 불가능하여 지도 정보가 보여지지 않을 수 있습니다.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	@Override
	protected void onStop() {
		stopMyLocation();
		super.onStop();
	}

	/* MyLocation Listener */
	private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {

		@Override
		public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
			if (mMapController != null) {
				mMapController.animateTo(myLocation);
			}
			return true;
		}

		@Override
		public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
			Toast.makeText(VegeNaverMapActivity.this, "Your current location is temporarily unavailable.", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
			Toast.makeText(VegeNaverMapActivity.this, "Your current location is unavailable area.", Toast.LENGTH_LONG).show();
			stopMyLocation();
		}
	};

	/* POI data State Change Listener */
	private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {

		@Override
		public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				Log.i(TAG, "Clicked: title=" + item.getTitle());
			}

			// [[TEMP]] handle a click event of the callout
			Intent intent = new Intent(getApplicationContext(), RestaurantInfoActivity.class); // 템플스테이용
			Restaurant restaurant = restaurantHash.get(String.valueOf(item.getId()));
			if (DEBUG) {
				if (restaurant == null)
					Log.i(TAG, "restaurant 이 null 임");
				else
					Log.i(TAG, "restaurant 이 null 이 아님");
			}

			intent.putExtra("restaurant", restaurant);
			startActivity(intent);
		}

		@Override
		public void onFocusChanged(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem item) {
			if (DEBUG) {
				if (item != null) {
					Log.i(TAG, "onFocusChanged: " + item.toString());
				} else {
					Log.i(TAG, "onFocusChanged: ");
				}
			}
		}
	};

	private void makePoiData() {
		Log.d(TAG, "makePoiData() ...");

		/* 1. DB에서 지리 정보 가져오기 */
		DbAdapter mDbAdapter = new DbAdapter(getApplicationContext());
		mDbAdapter.createDatabase();
		mDbAdapter.open();

		restaurantHash = new HashMap<String, Restaurant>();
		restaurantHash = mDbAdapter.getAllVegeHash();
		Log.d(TAG, "restaurantHash.size(): " + restaurantHash.size());
		mDbAdapter.close();

		/* 2. POI data 생성 */
		// int markerId = NMapPOIflagType.PIN; //pin 아이콘 지정
		int markerId;

		// 여러 오버레이 아이템을 하나의 오버레이 객체에서 관리하기 위해 NMapPOIdata 객체 생성
		poiData = new NMapPOIdata(1, mMapViewerResourceProvider); // (인자: 등록할 오버레이의 수, )

		poiData.removeAllPOIdata();

		poiData.beginPOIdata(1); // 아이템 등록 시작 (인자: 오버레이의 개수)

		// 오버레이 아이템의 수만큼 addPOIitem() 메소드를 사용해서 오버레이 아이템을 등록
		for (String currentKey : restaurantHash.keySet()) {
			Restaurant restaurant = restaurantHash.get(currentKey);
			markerId = getPinIcon(restaurant.getGrade());
			// Log.d(TAG, restaurant.getName() + ": " + restaurant.getLatitude()
			// + "," + restaurant.getLongitude());
			NMapPOIitem item = poiData.addPOIitem(restaurant.getLongitude(), restaurant.getLatitude(), restaurant.getName(), markerId, restaurant.getId()); // 아이템을 등록
			item.setMarker(getResources().getDrawable(R.drawable.ic_angle));
			// item.setMarkerId(arg0)
			item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW);
		}

		// (인자: 경도, 위도, 타이틀, 핀 아이콘, id)
		poiData.endPOIdata(); // 오버레이 아이템의 등록 종료

		// mMapViewerResourceProvider.notifyAll();//LSD
	}

	/**
	 * 채식 유형에 따른 핀 아이콘 id 전달
	 * 
	 * @param grade
	 *            채식유형
	 * @return markerId 핀 아이콘 id
	 */
	private int getPinIcon(String grade) {
		int markerId = NMapPOIflagType.PIN;

		if (grade.equals("비건")) {
			markerId = NMapPOIflagType.PIN_GREEN;
		} else if (grade.equals("락토 채식")) {
			markerId = NMapPOIflagType.PIN_GREEN_MILKY;
		} else if (grade.equals("락토 오보")) {
			markerId = NMapPOIflagType.PIN_GREEN_YELLOW_MILKY;
		} else if (grade.equals("유란 채식")) {
			markerId = NMapPOIflagType.PIN_GREEN_YELLOW;
		} else if (grade.equals("대부분 채식")) {
			markerId = NMapPOIflagType.PIN_MOSTLY;
		} else if (grade.equals("일부 채식")) {
			markerId = NMapPOIflagType.PIN_PARTLY;
		}

		return markerId;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		case R.id.item_gps:
			startMyLocation();
			break;

		case R.id.item_gps_zoom:
			startMyLocation();
			mPoiDataOverlay.showAllPOIdata(11); // (인자: 줌 레벨)
			break;

		case R.id.item_all:
			zoomLevel = 3;
			mMapController.setMapCenter(new NGeoPoint(127.862314, 36.350649), zoomLevel);
			break;

		case R.id.item_satellite:
			mMapController.setMapViewMode(NMapView.VIEW_MODE_HYBRID);
			break;

		case R.id.item_map:
			mMapController.setMapViewMode(NMapView.VIEW_MODE_VECTOR);
			break;

		case R.id.item_setting:
			// Intent intent = new Intent(getApplicationContext(),
			// ConfigActivity.class); //설정
			// startActivityForResult(intent, B_ACTIVITY);

			Intent intent = new Intent(getApplicationContext(), ConfigActivity.class); // 설정
			startActivity(intent);
			break;

		default:
			return false;
		}

		return true;
	}

	private static final int B_ACTIVITY = 0;// requestCode

	// startActivityForResult의 콜백(안드로이드 표준)
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		switch (requestCode) {
		case B_ACTIVITY: // requestCode가 B_ACTIVITY인 케이스
			if (resultCode == RESULT_OK) { // B_ACTIVITY에서 넘겨진 resultCode가 OK일때만
											// 실행
				intent.getExtras().getInt("data"); // 등과 같이 사용할 수 있는데, 여기서 getXXX()안에 들어있는 파라메터는 꾸러미 속 데이터의 이름표라고 보면된다.

				Log.d(TAG, "data: " + intent.getExtras().getInt("data"));

				// 맵 새로 그리기
				// Bundle tempBundle = new Bundle();
				// onCreate(tempBundle);

				Intent i = new Intent(this, VegeNaverMapActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				this.finish();
				this.startActivity(i);

				// finish();
				// startActivity(new Intent(this, VegeNaverMapActivity.class));

				// drawMap();

			}
		}
	}

	private void startMyLocation() {
		Log.d(TAG, "startMyLocation() ...");

		if (mMyLocationOverlay != null) {
			Log.d(TAG, "mMyLocationOverlay != null");

			if (!mOverlayManager.hasOverlay(mMyLocationOverlay)) {
				mOverlayManager.addOverlay(mMyLocationOverlay);
			}

			if (mMapLocationManager.isMyLocationEnabled()) {

				if (!mMapView.isAutoRotateEnabled()) {
					mMyLocationOverlay.setCompassHeadingVisible(true);

					mMapCompassManager.enableCompass();

					mMapView.setAutoRotateEnabled(true, false);

					mMapContainerView.requestLayout();
				} else {
					stopMyLocation();
				}

				mMapView.postInvalidate();
			} else {
				boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
				if (!isMyLocationEnabled) {
					Toast.makeText(VegeNaverMapActivity.this, "Please enable a My Location source in system settings", Toast.LENGTH_LONG).show();

					Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(goToSettings);

					return;
				}
			}
		} else {
			Log.d(TAG, "mMyLocationOverlay == null");
		}
	}

	private void stopMyLocation() {
		if (mMyLocationOverlay != null) {
			mMapLocationManager.disableMyLocation();

			if (mMapView.isAutoRotateEnabled()) {
				mMyLocationOverlay.setCompassHeadingVisible(false);

				mMapCompassManager.disableCompass();

				mMapView.setAutoRotateEnabled(false, false);

				mMapContainerView.requestLayout();
			}
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("앱을 종료하시겠습니까?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
	}

	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// Yes button clicked
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startMain.putExtra("EXIT", true);
				startActivity(startMain);
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// No button clicked
				break;
			}
		}
	};

}
