package com.vegansoft.vegemap.map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.vegansoft.vegemap.R;
import com.vegansoft.vegemap.model.Restaurant;

public class RestaurantInfoActivity extends Activity implements OnClickListener {
	private static final String TAG = "VegeMap";

	private static Restaurant restaurant;

	private TextView tvRestaurantName;
	private TextView tvAddress;
	private TextView tvRough_map_desc;
	private TextView tvGrade;
	private TextView tvType;
	private TextView tvBusiness_hours;
	private TextView tvHoliday;
	private TextView tvMenu;
	private TextView tvPrice;
	private TextView tvParking;

	private Button btHomepage;
	private Button btTel;
	private Button btGoogleMap;
	private Button btUpdate;

	private Button btDonate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_info);
		
	    getActionBar().setDisplayHomeAsUpEnabled(true); //상단 ActionBar 왼쪽에 Home 으로 가는 back 버튼 활성화

		initView();
		initEvent();
		initData();
	}

	private void initView() {
		tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
		tvAddress = (TextView) findViewById(R.id.tvAddressKo);
		tvRough_map_desc = (TextView) findViewById(R.id.tvRough_map_desc);
		tvGrade = (TextView) findViewById(R.id.tvGrade);
		tvType = (TextView) findViewById(R.id.tvType);
		tvBusiness_hours = (TextView) findViewById(R.id.tvBusiness_hours);
		tvHoliday = (TextView) findViewById(R.id.tvHoliday);
		tvMenu = (TextView) findViewById(R.id.tvMenu);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvParking = (TextView) findViewById(R.id.tvParking);

		btHomepage = (Button) findViewById(R.id.btHomepage);
		btTel = (Button) findViewById(R.id.btTel);
		btGoogleMap = (Button) findViewById(R.id.btGoogleMap);
		btUpdate = (Button) findViewById(R.id.btUpdate);
		
		btDonate = (Button) findViewById(R.id.btDonate);
	}

	private void initEvent() {
		btHomepage.setOnClickListener(this);
		btTel.setOnClickListener(this);
		btGoogleMap.setOnClickListener(this);
		btUpdate.setOnClickListener(this);
		btDonate.setOnClickListener(this);
	}

	private void initData() {
		if ((Restaurant) getIntent().getParcelableExtra("restaurant") != null) {
			restaurant = (Restaurant) getIntent().getParcelableExtra("restaurant");
		}
		
		if (restaurant == null)
			Log.i(TAG, "restaurant is null !!!!!!!!!!!!!!!!!!!!!!!!!");

		tvRestaurantName.setText(restaurant.getName());
		tvAddress.setText(restaurant.getDetail_addr());
		tvRough_map_desc.setText(restaurant.getRough_map_desc());
		tvGrade.setText(restaurant.getGrade());
		tvType.setText(restaurant.getType());
		tvBusiness_hours.setText(restaurant.getBusiness_hours());
		tvHoliday.setText(restaurant.getHoliday());
		tvMenu.setText(restaurant.getMenu());
		tvPrice.setText(restaurant.getPrice());
		tvParking.setText(restaurant.getParking());
		
		btHomepage.setText(restaurant.getHomepage());
		btTel.setText(restaurant.getTel());
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


	@Override
	public void onClick(View v) {
		Intent intent;
		Uri uri;

		switch (v.getId()) {
		case R.id.btHomepage:
			//WebView 에 URL 호출 방식...
//			intent=new Intent(this,WebViewActivity.class);
//			intent.putExtra("url", restaurant.getHomepage());
//			intent.putExtra("title", restaurant.getName());
//			startActivity(intent);
			
			//웹 브라우저 호출 방식...
			intent = new Intent(Intent.ACTION_VIEW);
			uri = Uri.parse(restaurant.getHomepage());
			intent.setData(uri);
			startActivity(intent);
			break;
		case R.id.btTel:
			String strUri = "tel:" + restaurant.getTel().trim();
			intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse(strUri));
			startActivity(intent);
			break;
		case R.id.btGoogleMap:
			//WebView 에 URL 호출 방식...
//			intent=new Intent(this,WebViewActivity.class);
//			intent.putExtra("url", "http://maps.google.com/maps?saddr=Current+Location&daddr="+restaurant.getLatitude()+","+restaurant.getLongitude());
//			intent.putExtra("title", "Transpotation");
//			startActivity(intent);
			
			//웹 브라우저 호출 방식...
			intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=Current+Location&daddr="+restaurant.getLatitude()+","+restaurant.getLongitude()));
			startActivity(intent);
			break;
			
		case R.id.btDonate:
			//WebView 에 URL 호출 방식...
//			intent=new Intent(this,WebViewActivity.class);
//			intent.putExtra("url", "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=BQ26FR5PWMZQN");
//			intent.putExtra("title", "Donation");
//			startActivity(intent);
			
			//웹 브라우저 호출 방식...
			intent = new Intent(Intent.ACTION_VIEW);
			uri = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=BQ26FR5PWMZQN");
			intent.setData(uri);
			startActivity(intent);
			break;
			
		case R.id.btUpdate:
			//웹 브라우저 호출 방식...
			intent = new Intent(Intent.ACTION_VIEW);
			uri = Uri.parse("http://cafe.naver.com/vegerestaurants");
			intent.setData(uri);
			startActivity(intent);
			break;
		default:
			break;
		}
	}


}
