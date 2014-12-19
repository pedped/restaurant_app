package com.edspace.restraunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AC_Contact extends Activity {

	private Button btn_sendmessage;
	private GoogleMap map;
	private TextView txt_Address;
	private TextView txt_Phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_contactus);

		// Load the View
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.ac_contact_Map)).getMap();
		txt_Address = (TextView) findViewById(R.id.ac_contact_txt_Address);
		txt_Phone = (TextView) findViewById(R.id.ac_contact_txt_Phone);

		btn_sendmessage = (Button) findViewById(R.id.ac_contact_btn_Sendmessage);
		btn_sendmessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendMessage();
			}
		});

		// Load Information
		LatLng location = new LatLng(29.62642087743759, 52.51815554051245);
		Marker marker = map.addMarker(new MarkerOptions().position(location)
				.title(getString(R.string.app_name)));
		// Marker kiel = map.addMarker(new MarkerOptions()
		// .position(KIEL)
		// .title("Kiel")
		// .snippet("Kiel is cool")
		// .icon(BitmapDescriptorFactory
		// .fromResource(R.drawable.ic_launcher)));

		// Move the camera instantly to hamburg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 5));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

		txt_Phone.setText("0711 2306071");
		txt_Address
				.setText("شیراز، خیابان ملاصدرا، روبروی فروشگاه گلبافت، جنب فروشگاه ال جی، طبقه دوم، واحد وبسایت");
	}

	protected void sendMessage() {

		final EditText et = new EditText(this);
		et.setHint("پیام خود را وارد نمایید");
		new AlertDialog.Builder(this)
				.setTitle("ارسال پیام")
				.setPositiveButton("ارسال",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								sf.sendTextMessage(AC_Contact.this, et
										.getText().toString());

							}
						}).setView(et).create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ac__contact, menu);
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
