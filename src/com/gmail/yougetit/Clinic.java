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

		btnDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showDateDialog();
			}
		});

		//畫面啟動時獲得當前的日期：		
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
		MyDialogFragment newDialog = MyDialogFragment.newInstance(MyDialogFragment.DATE_PICKER_DIALOG);
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
