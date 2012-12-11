package com.gmail.yougetit;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Clinic extends Activity
{
	TextView dateText; // 看診日期
	ListView clinicListView; // 歷次看診記錄
	Spinner spinner1; // 看診時段
	String PAGETAG = "ConnectMySQL";

	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.clinic_activity);

		Button btnDate = (Button) findViewById(R.id.clinicDateTextBtn);
		spinner1 = (Spinner) findViewById(R.id.clinicTimeSelecter);
		Button btnEnter = (Button) findViewById(R.id.clinicEnterBtn);

		btnEnter.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					Class.forName("com.mysql.jdbc.Driver");
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				EditText clinicDateTextBox = (EditText) findViewById(R.id.clinicDateTextBox);
				String clinicDate = clinicDateTextBox.getText().toString(); // 看診時間
				String clinicTime = spinner1.getSelectedItem().toString();
				String MYSQL_IP = "59.126.210.3";
				String MYSQL_DBNAME = "opd";
				String MYSQL_USERNAME = "root";
				String MYSQL_PASSWORD = "1999";
				String path = "jdbc:mysql://" + MYSQL_IP + "/" + MYSQL_DBNAME
						+ "?" + "user=" + MYSQL_USERNAME + "&password="
						+ MYSQL_PASSWORD;

				String PAGETAG = "ConnectMySQL";
				Log.e(PAGETAG, path);

				Connection connect = null;
				Statement statement = null;
				ResultSet resultSet = null;
				clinicListView = (ListView) findViewById(R.id.clinicListView);
				String[] Contentitem = new String[]
				{ "chartno", "opddate", "regtime", "doctor", "icdno1",
						"icdno2", "icdno3" };
				int[] TextviewID = new int[]
				{ R.id.textView26, R.id.textView14, R.id.textView16,
						R.id.textView18, R.id.textView20, R.id.textView22,
						R.id.textView24 };
				List<HashMap<String, Object>> value = new ArrayList<HashMap<String, Object>>();

				try
				{
					String script2 = "SELECT chartno, opddate, regtime, doctor, icdno1, icdno2, icdno3 from homecare where opddate="
							+ "'"
							+ clinicDate
							+ "' and regtime='"
							+ clinicTime
							+ "'";
					connect = (Connection) DriverManager.getConnection(path);
					statement = (Statement) connect.createStatement();
					resultSet = (ResultSet) statement.executeQuery(script2);

					Log.e(PAGETAG, script2);
					while (resultSet.next())
					{
						HashMap<String, Object> item = new HashMap<String, Object>();
						item.put("chartno", resultSet.getString(1));
						item.put("opddate", resultSet.getString(2));
						item.put("regtime", resultSet.getString(3));
						item.put("doctor", resultSet.getString(4));
						item.put("icdno1", resultSet.getString(5));
						item.put("icdno2", resultSet.getString(6));
						item.put("icdno3", resultSet.getString(7));
						value.add(item);
					}

					SimpleAdapter simpleAdapter = new SimpleAdapter(
							Clinic.this, value, R.layout.clinic_listview,
							Contentitem, TextviewID);
					clinicListView.setAdapter(simpleAdapter);
					// 加上事件
					clinicListView
							.setOnItemClickListener(new OnItemClickListener()
							{

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3)
								{
									// 獲取選中項的HashMap對象
									HashMap<String, Object> map = (HashMap<String, Object>) clinicListView
											.getItemAtPosition(arg2);
									String chartno = map.get("chartno")
											.toString();
									String opddate = map.get("opddate")
											.toString();
									String regtime = map.get("regtime")
											.toString();

									Intent it = new Intent();
									it.setClass(Clinic.this, Doctor.class);
									startActivity(it);
									/* 彈出視窗
									Toast.makeText(
											getApplicationContext(),
											"arg2:" + arg2 + " opddate:"
													+ opddate + " chartno:"
													+ chartno + " regtime:"
													+ regtime,
											Toast.LENGTH_LONG).show();
											*/
								}
							});
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if (resultSet != null)
						{
							resultSet.close();
						}
						if (statement != null)
						{
							statement.close();
						}
						if (connect != null)
						{
							connect.close();
						}
					}
					catch (Exception e)
					{
					}
				}
				Toast.makeText(Clinic.this, "查詢完成！", Toast.LENGTH_LONG).show();
			}

		});

		btnDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showDateDialog();
			}
		});

		// 畫面啟動時獲得當前的日期：
		final Calendar currentDate = Calendar.getInstance();
		mYear = currentDate.get(Calendar.YEAR);
		mMonth = currentDate.get(Calendar.MONTH);
		mDay = currentDate.get(Calendar.DAY_OF_MONTH);

		dateText = (TextView) findViewById(R.id.clinicDateTextBox);
		dateText.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-")// 得到的月份+1，因為從0開始
				.append(mDay));

	}

	public void showDateDialog()
	{
		// 根據傳進的參數來實例化DialogFragment.
		// MyDialogFragment newDialog =
		// MyDialogFragment.newInstance(MyDialogFragment.ALTER_DIALOG);
		MyDialogFragment newDialog = MyDialogFragment
				.newInstance(MyDialogFragment.DATE_PICKER_DIALOG);
		// MyDialogFragment newDialog =
		// MyDialogFragment.newInstance(MyDialogFragment.TIME_PICKER_DiALOG);
		newDialog.setCallBack(mDateSetListener);
		newDialog.show(getFragmentManager(), "alert msg");
	}

	// 需要定義彈出的DatePicker對話框的事件監聽器：

	DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			// 設置文本的內容：
			dateText.setText(new StringBuilder().append(mYear).append("-")
					.append(mMonth + 1).append("-")// 得到的月份+1，因為從0開始
					.append(mDay));
		}
	};

}
