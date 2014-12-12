package com.vegansoft.vegemap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.vegansoft.vegemap.db.LoadUserConfig;
import com.vegansoft.vegemap.map.VegeNaverMapActivity;
import com.vegansoft.vegemap.network.CheckRemoteNewDB;

public class MainActivity extends Activity {

	private String TAG = this.getClass().getName();
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		context = this;

		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		}

		setContentView(R.layout.activity_main);

		VegeMapApplication.DB_PATH = getDbPath(); // DB 파일의 경로 설정
		LoadUserConfig.LoadUserConfigFromDB(this); // DB에서 사용자 지도 보기 설정 및 local
													// DB의 버전 가져오기
		CheckRemoteNewDB.CheckNewDB(context, VegeNaverMapActivity.class); // 새로운 DB가 서버에 있는지 체크
	}

	private String getDbPath() {
		String DB_PATH = "";

		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + getPackageName() + "/databases/";
		}

		Log.d(TAG, "DB_PATH: " + DB_PATH);
		
		return DB_PATH;
	}
	
}
