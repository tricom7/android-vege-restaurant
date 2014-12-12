package com.vegansoft.vegemap.config;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.vegansoft.vegemap.R;
import com.vegansoft.vegemap.VegeMapApplication;
import com.vegansoft.vegemap.db.DbAdapter;
import com.vegansoft.vegemap.network.CheckRemoteNewDB;

public class ConfigActivity extends Activity {
	ListView listview;
	
	CheckBox chkVegan;
	CheckBox chkLacto;
	CheckBox chkOvo;
	CheckBox chkLactoOvo;
	CheckBox chkMostly;
	CheckBox chkPartly;
	
	CheckBox chkRestaurant;
	CheckBox chkBuffet;
	CheckBox chkBakery;
	CheckBox chkCafe;
	
	CheckBox chkBon;
	CheckBox chkThuckdam;
	CheckBox chkSoDelicious;
	CheckBox chkBizun;
	CheckBox chkGanga;
	CheckBox chkSubway;
	CheckBox chkCoffeeBean;
	
	Button btCheckDB;
	
	Bundle extra;
	Intent intent;
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		context = this;
		
		getActionBar().setDisplayHomeAsUpEnabled(true); //상단 ActionBar 왼쪽에 Home 으로 가는 back 버튼 활성화
		
		initView();
		initEvent();
		initData();
		
		extra = new Bundle();
		intent = new Intent(); //초기화 깜빡 했다간 NullPointerException이라는 짜증나는 놈이랑 대면하게 된다.
		extra.putInt("data", 1);
		intent.putExtras(extra);
		this.setResult(RESULT_OK, intent); // 성공했다는 결과값을 보내면서 데이터 꾸러미를 지고 있는 intent를 함께 전달한다.
	}
	
	
	private void initView() {
		chkVegan = (CheckBox)findViewById(R.id.chkVegan);
		chkLacto = (CheckBox)findViewById(R.id.chkLacto);
		chkOvo = (CheckBox)findViewById(R.id.chkOvo);
		chkLactoOvo = (CheckBox)findViewById(R.id.chkLactoOvo);
		chkMostly = (CheckBox)findViewById(R.id.chkMostly);
		chkPartly = (CheckBox)findViewById(R.id.chkPartly);
			
		chkRestaurant = (CheckBox)findViewById(R.id.chkRestaurant);
		chkBuffet = (CheckBox)findViewById(R.id.chkBuffet);
		chkBakery = (CheckBox)findViewById(R.id.chkBakery);
		chkCafe = (CheckBox)findViewById(R.id.chkCafe);
			
		chkBon = (CheckBox)findViewById(R.id.chkBon);
		chkThuckdam = (CheckBox)findViewById(R.id.chkThuckdam);
		chkSoDelicious = (CheckBox)findViewById(R.id.chkSoDelicious);
		chkBizun = (CheckBox)findViewById(R.id.chkBizun);
		chkGanga = (CheckBox)findViewById(R.id.chkGanga);
		chkSubway = (CheckBox)findViewById(R.id.chkSubway);
		chkCoffeeBean = (CheckBox)findViewById(R.id.chkCoffeeBean);	
		
		btCheckDB = (Button)findViewById(R.id.btCheckDB);
	}

	private void initEvent() {
		btCheckDB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckRemoteNewDB.CheckNewDB(context, null); // 새로운 DB가 서버에 있는지 체크
			}
		});
	}

	private void initData() {
		if (VegeMapApplication.isVegan) chkVegan.setChecked(true);
		if (VegeMapApplication.isLacto) chkLacto.setChecked(true);
		if (VegeMapApplication.isOvo) chkOvo.setChecked(true);
		if (VegeMapApplication.isLactoOvo) chkLactoOvo.setChecked(true);
		if (VegeMapApplication.isMostly) chkMostly.setChecked(true);
		if (VegeMapApplication.isPartly) chkPartly.setChecked(true);
			
		if (VegeMapApplication.isRestaurant) chkRestaurant.setChecked(true);
		if (VegeMapApplication.isBuffet) chkBuffet.setChecked(true);
		if (VegeMapApplication.isBakery) chkBakery.setChecked(true);
		if (VegeMapApplication.isCafe) chkCafe.setChecked(true);
			
		if (VegeMapApplication.isBon) chkBon.setChecked(true);
		if (VegeMapApplication.isThuckdam) chkThuckdam.setChecked(true);
		if (VegeMapApplication.isSoDelicious) chkSoDelicious.setChecked(true);
		if (VegeMapApplication.isBizun) chkBizun.setChecked(true);
		if (VegeMapApplication.isGanga) chkGanga.setChecked(true);
		if (VegeMapApplication.isSubway) chkSubway.setChecked(true);
		if (VegeMapApplication.isCoffeeBean) chkCoffeeBean.setChecked(true);
	}
	
	@Override
	public void onBackPressed() {
		setGlobalConfig();
		
		super.onBackPressed();
	}

	//상단 ActionBar 왼쪽에 Home 으로 가는 back 버튼 활성화
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            //NavUtils.navigateUpFromSameTask(this);
        	setGlobalConfig();
            this.finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
	private void setGlobalConfig() {
		VegeMapApplication.isVegan = chkVegan.isChecked();
		VegeMapApplication.isLacto = chkLacto.isChecked();
		VegeMapApplication.isOvo = chkOvo.isChecked();
		VegeMapApplication.isLactoOvo = chkLactoOvo.isChecked();
		VegeMapApplication.isMostly = chkMostly.isChecked();
		VegeMapApplication.isPartly = chkPartly.isChecked();
			
		VegeMapApplication.isRestaurant = chkRestaurant.isChecked();
		VegeMapApplication.isBuffet = chkBuffet.isChecked();
		VegeMapApplication.isBakery = chkBakery.isChecked();
		VegeMapApplication.isCafe = chkCafe.isChecked();
			
		VegeMapApplication.isBon = chkBon.isChecked();
		VegeMapApplication.isThuckdam = chkThuckdam.isChecked();
		VegeMapApplication.isSoDelicious = chkSoDelicious.isChecked();
		VegeMapApplication.isBizun = chkBizun.isChecked();
		VegeMapApplication.isGanga = chkGanga.isChecked();
		VegeMapApplication.isSubway = chkSubway.isChecked();
		VegeMapApplication.isCoffeeBean = chkCoffeeBean.isChecked();
		
		saveGlobalConfigToDB();
	}
	
	
	private void saveGlobalConfigToDB() {
		/* 1. DB에 보기 설정 저장하기 */
		DbAdapter mDbAdapter = new DbAdapter(getApplicationContext());
		mDbAdapter.createDatabase();
		mDbAdapter.open();
		mDbAdapter.updateConfig();
		mDbAdapter.close();
	}

	
}
