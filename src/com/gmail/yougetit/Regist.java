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

public class Regist extends Activity
{
	TextView dateText; // �ݶE���
	ListView listview; // �����ݶE�O��
	Spinner spinner1; // �ݶE�ɬq
	Spinner spinner2; // �ݶE��v
	String PAGETAG = "ConnectMySQL";

	// �ΨӫO�s�~���G
	private int mYear;
	private int mMonth;
	private int mDay;
	// �n���@�ӿW�@�L�G�����ѡA�ӧ@���n���DatePicker��Dialog��ID�G
	static final int DATE_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.permitAll().build());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.regist_activity);

		Button btnEnter = (Button) findViewById(R.id.Inquiry_Data);
		Button btnDate = (Button) findViewById(R.id.regdate_select);
		Button btnReg = (Button) findViewById(R.id.Inquiry_register);
		spinner1 = (Spinner) findViewById(R.id.Inquiry_Time_input);
		spinner2 = (Spinner) findViewById(R.id.Inquiry_doctor_input);

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
				EditText textChartno = (EditText) findViewById(R.id.Inquiry_CaseNo_input);
				String chartno = textChartno.getText().toString();
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
				try
				{
					String script1 = "SELECT name, sex, birthday, id FROM chart where chartno='"
							+ chartno + "'";
					connect = (Connection) DriverManager.getConnection(path);
					statement = (Statement) connect.createStatement();
					resultSet = (ResultSet) statement.executeQuery(script1);

					// �d�߯f�H�򥻸��(�m�W�B�ʧO�B�ͤ�B������)
					String name = null;
					String sex = null;
					String birthday = null;
					String id = null;
					if (resultSet.next() == false)
					{
						Log.e(PAGETAG, "null");
						Toast.makeText(Regist.this, "�d�L�f�����I", Toast.LENGTH_LONG)
								.show();
					}
					else
					{
						name = resultSet.getString(1);
						int sexno = resultSet.getInt(2);
						if (sexno == 1)
						{
							sex = "�k";
						}
						if (sexno == 2)
						{
							sex = "�k";
						}
						birthday = resultSet.getString(3);
						id = resultSet.getString(4);
						Log.e(PAGETAG, name + sex + birthday + id);

					}
					TextView textName = (TextView) findViewById(R.id.Inquiry_Name_input);
					TextView textSex = (TextView) findViewById(R.id.Inquiry_Sex_input);
					TextView textBirthday = (TextView) findViewById(R.id.Inquiry_year_input);
					TextView textID = (TextView) findViewById(R.id.Inquiry_IDcard_input);

					textName.setText(name);
					textSex.setText(sex);
					textBirthday.setText(birthday);
					textID.setText(id);

					// �d�߾����N�E�O��
					listview = (ListView) findViewById(R.id.listView1);
					String[] Contentitem = new String[]
					{ "opddate", "regtime", "doctor", "icdno1", "icdno2",
							"icdno3" };
					int[] TextviewID = new int[]
					{ R.id.textView2, R.id.textView4, R.id.textView6,
							R.id.textView8, R.id.textView10, R.id.textView12 };
					List<HashMap<String, Object>> value = new ArrayList<HashMap<String, Object>>();

					try
					{
						String script2 = "SELECT opddate, regtime, doctor, icdno1, icdno2, icdno3 from homecare where chartno="
								+ chartno;
						connect = (Connection) DriverManager
								.getConnection(path);
						statement = (Statement) connect.createStatement();
						resultSet = (ResultSet) statement.executeQuery(script2);

						while (resultSet.next())
						{
							HashMap<String, Object> item = new HashMap<String, Object>();
							item.put("opddate", resultSet.getString(1));
							item.put("regtime", resultSet.getString(2));
							item.put("doctor", resultSet.getString(3));
							item.put("icdno1", resultSet.getString(4));
							item.put("icdno2", resultSet.getString(5));
							item.put("icdno3", resultSet.getString(6));
							value.add(item);
						}

						SimpleAdapter simpleAdapter = new SimpleAdapter(
								Regist.this, value, R.layout.reg_listview,
								Contentitem, TextviewID);
						listview.setAdapter(simpleAdapter);
						/**
						 * ����~���h listview.setOnItemClickListener(new
						 * OnItemClickListener() {
						 * 
						 * @Override public void onItemClick(AdapterView<?>
						 *           arg0, View view, int arg2, long arg3) { //
						 *           TODO �۰ʲ��ͪ���k Stub Toast.makeText(
						 *           Regist.this, ((TextView) view
						 *           .findViewById(R.id.Inquiry_Name))
						 *           .getText(), Toast.LENGTH_SHORT) .show(); }
						 *           });
						 **/

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
					Toast.makeText(Regist.this, "�d�ߧ����I", Toast.LENGTH_LONG)
							.show();
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
			}
		});

		btnDate.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showDialog(DATE_DIALOG_ID);
			}
		});

		btnReg.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String chartno, opddate, doctor, regtime;

				try
				{
					Class.forName("com.mysql.jdbc.Driver");
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				EditText textChartno = (EditText) findViewById(R.id.Inquiry_CaseNo_input);
				dateText = (TextView) findViewById(R.id.regdate);
				spinner1 = (Spinner) findViewById(R.id.Inquiry_Time_input);
				spinner2 = (Spinner) findViewById(R.id.Inquiry_doctor_input);

				chartno = textChartno.getText().toString();
				opddate = dateText.getText().toString();
				doctor = spinner2.getSelectedItem().toString();
				Log.e(PAGETAG, "doctor:" + doctor);

				regtime = spinner1.getSelectedItem().toString();
				Log.e(PAGETAG, "regtime:" + regtime);
				String MYSQL_IP = "59.126.210.3";
				String MYSQL_DBNAME = "opd";
				String MYSQL_USERNAME = "root";
				String MYSQL_PASSWORD = "1999";
				String path = "jdbc:mysql://" + MYSQL_IP + "/" + MYSQL_DBNAME
						+ "?" + "user=" + MYSQL_USERNAME + "&password="
						+ MYSQL_PASSWORD;

				
				Log.e(PAGETAG, path);

				Connection connect = null;
				Statement statement = null;

				try
				{
					String script3 = "INSERT INTO homecare (`chartno`, `opddate`, `doctor`, `regtime`) VALUES ('"
							+ chartno
							+ "', '"
							+ opddate
							+ "', '"
							+ doctor
							+ "', '" + regtime + "')";
					connect = (Connection) DriverManager.getConnection(path);
					statement = (Statement) connect.createStatement();
					int result = statement.executeUpdate(script3);
					String resultString = Integer.toString(result);
					Log.e(PAGETAG, resultString);
					if (result == 1)
					{
						Toast.makeText(Regist.this, "���������I", Toast.LENGTH_LONG)
						.show();
					}
					else
					{
						Toast.makeText(Regist.this, "�������ѡI", Toast.LENGTH_LONG)
						.show();
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
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
				
			}
		});

		spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView adapterView, View view,
					int position, long id)
			{
				// Toast.makeText(MainActivity.this,
				// "�z���"+adapterView.getSelectedItem().toString(),
				// Toast.LENGTH_LONG).show();
			}

			public void onNothingSelected(AdapterView arg0)
			{
				// Toast.makeText(MainActivity.this, "�z�S����ܥ��󶵥�",
				// Toast.LENGTH_LONG).show();
			}
		});

		spinner2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView adapterView, View view,
					int position, long id)
			{
				// Toast.makeText(MainActivity.this,
				// "�z���"+adapterView.getSelectedItem().toString(),
				// Toast.LENGTH_LONG).show();
			}

			public void onNothingSelected(AdapterView arg0)
			{
				// Toast.makeText(MainActivity.this, "�z�S����ܥ��󶵥�",
				// Toast.LENGTH_LONG).show();
			}
		});
		// ��o��e������G
		final Calendar currentDate = Calendar.getInstance();
		mYear = currentDate.get(Calendar.YEAR);
		mMonth = currentDate.get(Calendar.MONTH);
		mDay = currentDate.get(Calendar.DAY_OF_MONTH);

		dateText = (TextView) findViewById(R.id.regdate);
		dateText.setText(new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-")// �o�쪺���+1�A�]���q0�}�l
				.append(mDay));
	}

	// �ݭn�w�q�u�X��DatePicker��ܮت��ƥ��ť���G
	DatePickerDialog.OnDateSetListener mDateSetListener = new OnDateSetListener()
	{
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			// �]�m�奻�����e�G
			dateText.setText(new StringBuilder().append(mYear).append("-")
					.append(mMonth + 1).append("-")// �o�쪺���+1�A�]���q0�}�l
					.append(mDay));
		}
	};

	/**
	 * ��Activity�ե�showDialog��Ʈɷ|Ĳ�o�Ө�ƪ��եΡG
	 */

	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener, mYear,
						mMonth, mDay);
		}
		return null;
	}

}
