package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.edspace.restraunt.interfaces.OnResponseListener;

public class Ac_ChooseCity extends Activity {

	private Spinner spinner_State;
	private Spinner spinner_City;
	private String selectedCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_choosecity);

		// when this activity started, it should load the current state and city
		// from server and show to user
		// user will select a city
		spinner_State = (Spinner) findViewById(R.id.choosecity_sp_state);
		spinner_City = (Spinner) findViewById(R.id.choosecity_sp_city);

		Button btn_select = (Button) findViewById(R.id.choosecity_btn_select);
		btn_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Integer.valueOf(selectedCity) > 0) {

					// we have to send the server user city location
					saveUserCityInDatabase();

				} else {
					Toast.makeText(getApplicationContext(),
							"شما میبایست یک شهر انتخاب نمایید",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		// load states
		this.loadStates();
	}

	protected void saveUserCityInDatabase() {
		/*
		 * Create a web request Save User City Aug 20, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "user_setcity"));
		params.add(new BasicNameValuePair("cityid", selectedCity));
		nc.WebRequest(Ac_ChooseCity.this, params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(Ac_ChooseCity.this)
						.setTitle(R.string.ops)
						.setMessage(message)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								}).create().show();

			}

			@Override
			public void onSuccess(String result) {
				try {
					// user city saved in database, we have to show the user
					// home page
					sf.PerfernceManager_Write(Ac_ChooseCity.this, "cityid",
							selectedCity);

					// load the main activty
					startActivity(new Intent(Ac_ChooseCity.this,
							Ac_Master.class));

					// close the current activity
					finish();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}

	private void loadStates() {
		/*
		 * Create a web request Load Cities Aug 20, 2014
		 */

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "state_list"));
		nc.WebRequest(Ac_ChooseCity.this, params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(Ac_ChooseCity.this)
						.setTitle(R.string.ops)
						.setMessage(message)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								}).create().show();

			}

			@Override
			public void onSuccess(String result) {
				try {

					// states loaded, we have to use for spinner
					List<String> list = new ArrayList<String>();
					final Hashtable<String, String> states = new Hashtable<String, String>();
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonobject = jsonArray.getJSONObject(i);

						// load the item
						list.add(jsonobject.getString("Name"));
						states.put(i + "", jsonobject.getString("ID"));
					}

					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							Ac_ChooseCity.this,
							android.R.layout.simple_spinner_item, list);

					dataAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					spinner_State
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View view, int pos, long id) {

									// get selected state
									String state = states.get(pos + "");

									// load city based on state
									loadCities(state);

									Toast.makeText(getApplicationContext(),
											states.get(pos + ""),
											Toast.LENGTH_SHORT).show();

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}
							});

					spinner_State.setAdapter(dataAdapter);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}

	private void loadCities(String stateID) {

		/*
		 * Create a web request requestTitle Aug 20, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "cities_list"));
		params.add(new BasicNameValuePair("stateid", stateID));
		nc.WebRequest(Ac_ChooseCity.this, params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(Ac_ChooseCity.this)
						.setTitle(R.string.ops)
						.setMessage(message)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								}).create().show();

			}

			@Override
			public void onSuccess(String result) {
				try {

					// states loaded, we have to use for spinner
					List<String> list = new ArrayList<String>();
					final Hashtable<String, String> cities = new Hashtable<String, String>();
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonobject = jsonArray.getJSONObject(i);

						// load the item
						list.add(jsonobject.getString("Name"));
						cities.put(i + "", jsonobject.getString("ID"));
					}

					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							Ac_ChooseCity.this,
							android.R.layout.simple_spinner_item, list);

					dataAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					spinner_City
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View view, int pos, long id) {

									// get selected state
									String cityid = cities.get(pos + "");

									selectedCity = cityid;

									Toast.makeText(getApplicationContext(),
											cities.get(pos + ""),
											Toast.LENGTH_SHORT).show();

								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}
							});

					spinner_City.setAdapter(dataAdapter);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ac__master, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
