package com.gmail.yougetit;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class MyDialogFragment extends DialogFragment {
	public static final int DATE_PICKER_DIALOG = 1;
	public static final int ALTER_DIALOG = 2;
	public static final int TIME_PICKER_DiALOG = 3;

	OnDateSetListener ondateSet;


public void setCallBack(OnDateSetListener ondate){
	
	ondateSet = ondate;
	
	}

	public static MyDialogFragment newInstance(int title) {
		MyDialogFragment myDialogFragment = new MyDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("title", title);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		int args = getArguments().getInt("title");
		// 獲得當前的日期：
		final Calendar currentDate = Calendar.getInstance();
		int mYear;
		int mMonth;
		int mDay;
		mYear = currentDate.get(Calendar.YEAR);
		mMonth = currentDate.get(Calendar.MONTH);
		mDay = currentDate.get(Calendar.DAY_OF_MONTH);
		//根据传进来的参数选择创建哪种Dialog
		switch (args) {
		case DATE_PICKER_DIALOG:
			return new DatePickerDialog(getActivity(), ondateSet, mYear, mMonth, mDay);
		case ALTER_DIALOG:
			return new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_launcher)
				.setTitle(getTag())
				.setPositiveButton("ok",
						new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//点击ok触发的事件
					System.out.println("click ok!");
				}
				})
			.setNegativeButton("cancle",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//点击cancle触发的时间
					System.out.println("click cancle");
				}
				})
				.create();
		case TIME_PICKER_DiALOG:
			return new TimePickerDialog(getActivity(),new OnTimeSetListener() {
				
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					System.out.println("hour-->"+hourOfDay+"  minute-->"+ minute);
				}
			}, 13, 23, true);
		}
		return null;
	}

}


