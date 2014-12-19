package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.db.OrderItem;
import com.edspace.restraunt.interfaces.OnResponseListener;

public class AC_SendOrder extends Activity {

	private static final String TAG = "AC_SendOrder";
	private EditText et_name;
	private EditText et_phone;
	private EditText et_subscribecode;
	private EditText et_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_sendorder);

		// Order Info
		et_name = (EditText) findViewById(R.id.ac_sendorder_et_name);
		et_phone = (EditText) findViewById(R.id.ac_sendorder_et_phone);
		et_address = (EditText) findViewById(R.id.ac_sendorder_et_address);
		et_subscribecode = (EditText) findViewById(R.id.ac_sendorder_et_subscribecode);

		// Send Order Button
		Button btn_send = (Button) findViewById(R.id.ac_sendorder_btnsend);

		// Click Listener
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// validate infos
				if (!validateInfos()) {

					return;
				}

				// all items are valid, we have to send the request to server
				// fetch each restaurant id
				db db = new db(AC_SendOrder.this);
				db.open();
				List<Restaurant> items = db.Orders_Restaurants();
				db.close();

				for (Restaurant restaurant : items) {
					sendRequest(restaurant.id);
				}

			}

		});
	}

	private void sendRequest(final int restaurantID) {

		db db = new db(this);
		db.open();
		List<OrderItem> list = db.Orders_GetAllItems(restaurantID, false);
		db.close();

		// we have to create new string based on the list
		JSONArray result = new JSONArray();
		for (OrderItem orderItem : list) {
			try {
				result.put(orderItem.encode());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Show the result in logcat
		Log.d(TAG, result.toString());

		/*
		 * Create a web request Send Order Request to server Aug 30, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "order_send"));
		params.add(new BasicNameValuePair("restaurantid", restaurantID + ""));
		params.add(new BasicNameValuePair("infos", result.toString()));
		params.add(new BasicNameValuePair("name", et_name.getText().toString()));
		params.add(new BasicNameValuePair("phone", et_phone.getText()
				.toString()));
		params.add(new BasicNameValuePair("address", et_address.getText()
				.toString()));
		params.add(new BasicNameValuePair("subscribecode", et_subscribecode
				.getText().length() > 0 ? et_subscribecode.getText().toString()
				: "0"));

		nc.WebRequest(this, params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(AC_SendOrder.this)
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
					// we have to clear the database for that restaurant order
					db db = new db(AC_SendOrder.this);
					db.open();
					db.Order_RemoveByRestaurantID(restaurantID);
					db.close(); 

					Toast.makeText(getApplicationContext(),
							"درخواست شما ارسال گردید", Toast.LENGTH_LONG)
							.show();
					
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

	protected boolean validateInfos() {
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ac__send_order, menu);
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
