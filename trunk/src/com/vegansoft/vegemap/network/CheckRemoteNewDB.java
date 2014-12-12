package com.vegansoft.vegemap.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.vegansoft.vegemap.VegeMapApplication;
import com.vegansoft.vegemap.constant.Constants;
import com.vegansoft.vegemap.db.DbAdapter;

public class CheckRemoteNewDB {

	private static String TAG = CheckRemoteNewDB.class.getName();

	private static String REMOTE_DB_VERSION = "";

	private static Context context;

	private static Class nextActivity;

	/**
	 * 새로운 버전의 DB가 서버에 있으면 얻데이트 하기
	 */
	public static void CheckNewDB(Context argContext, Class argNextActivity) {
		context = argContext;
		nextActivity = argNextActivity;

		Log.d(TAG, "Local DB_VERSION = " + VegeMapApplication.DB_VERSION);

		if (!CheckNetwork.isNetworkAvailable(context)) { // 네트워크 연결이 불가능한 상태이면
															// 바로 지도페이지로 넘어가기..
			goMapPage();
		}

		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				try {
					// 새로운 DB 버전 확인하기
					URL u = new URL(Constants.DB_VERSION_URL);
					Scanner s = new Scanner(u.openStream());
					REMOTE_DB_VERSION = s.next();
					Log.d(TAG, "Remote DB_VERSION = " + REMOTE_DB_VERSION);

					// 로컬 DB의 버전과 새로운 DB의 버전 비교(DB 업데이트 필요성 확인)
					if (VegeMapApplication.DB_VERSION.equals(REMOTE_DB_VERSION)) {
						Log.d(TAG, "최신 버전입니다.");

						if (nextActivity == null) {
							Handler mHandler = new Handler(Looper.getMainLooper());
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									AlertDialog.Builder alert = new AlertDialog.Builder(context);
									alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
									    @Override
									    public void onClick(DialogInterface dialog, int which) {
									    dialog.dismiss();     //닫기
									    }
									});
									alert.setMessage("서버에 변경된 채식 식당 정보가 없습니다.");
									alert.show();
									
//									Toast toast = Toast.makeText(context, "서버에 변경된 채식 식당 정보가 없습니다.", Toast.LENGTH_LONG);
//									toast.setGravity(Gravity.TOP, 0, 0);
//									toast.show();
								}
							}, 0);
						}

						Log.d(TAG, "goMapPage() 호출 ...");
						goMapPage(); // 맵으로 이동..
					} else {
						Log.d(TAG, "새로운 DB 버전이 있습니다.");

						Handler mHandler = new Handler(Looper.getMainLooper());
						mHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								// 내용
								AlertDialog.Builder builder = new AlertDialog.Builder(context);
								builder.setMessage("새로운 DB 버전이 있습니다. 지금 다운로드 하시겠습니까?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
							}
						}, 0);

					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, e.getMessage());
				}
			}
		});
		thread1.start();
	}

	static DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				// Yes button clicked
				updateDB(); // 최신 DB로 갱신하기
				break;

			case DialogInterface.BUTTON_NEGATIVE:
				// No button clicked
				goMapPage(); // 맵으로 이동..
				break;
			}
		}
	};

	/**
	 * 최신 채식 DB로 갱신하기
	 */
	private static void updateDB() {
		Log.d(TAG, "updateDB() ...");

		TestConnectionNew t = new TestConnectionNew();
		t.execute(Constants.DB_URL);
	}

	private static void goMapPage() {
		Log.d(TAG, "goMapPage() ...");

		if (nextActivity != null) {
			Intent intent = new Intent(context, nextActivity); // 템플스테이용
			context.startActivity(intent);
		}

	}

	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;

	// Progress Dialog
	private static ProgressDialog pDialog;

	static class TestConnectionNew extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {

			int count;
			try {
				URL url = new URL(f_url[0]); // you can write here any link

				long startTime = System.currentTimeMillis();
				Log.d(TAG, "download begining");
				Log.d(TAG, "download url:" + url);
				Log.d(TAG, "downloaded file name:" + VegeMapApplication.DB_PATH + Constants.DB_NAME);

				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();
				Log.d(TAG, "lenghtOfFile: " + lenghtOfFile);

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(), 8192);

				File file = new File(VegeMapApplication.DB_PATH + Constants.DB_NAME);

				// Output stream to write file
				OutputStream output = new FileOutputStream(file);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					// Log.d(TAG, ""+(int)((total*100)/lenghtOfFile)+" %");

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

				Log.d(TAG, "download ready in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
			} catch (Exception e) {
				Log.d(TAG, "Error: " + e);
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();

			updateDbVersion(); // 가져온 DB 버전을 localDB에 기록해 둠(추후 같은 버전이면 다시 갱신하지
								// 않도록..)

			goMapPage(); // 맵 화면으로 이동...
		}

	}

	private static void updateDbVersion() {
		VegeMapApplication.DB_VERSION = REMOTE_DB_VERSION;

		/* 1. DB에 가져온 식당 DB 버전 기록하기 */
		DbAdapter mDbAdapter = new DbAdapter(context);
		mDbAdapter.createDatabase();
		mDbAdapter.open();
		mDbAdapter.updateVersion();
		mDbAdapter.close();
	}

}
