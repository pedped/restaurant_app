package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 
 * @author ataalla
 * 
 */
public class fr_Restitem_Map extends Fragment {

	private int ID;
	private GoogleMap map;
	private TextView txt_Address;
	private TextView txt_Phone;

	static final LatLng KIEL = new LatLng(53.551, 9.993);
	private Restaurant restrauntInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_restitem_map, container,
				false);

		// load the id
		ID = getActivity().getIntent().getExtras().getInt("id");

		// Load the View
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.fr_restmap_Map)).getMap();
		txt_Address = (TextView) rootView
				.findViewById(R.id.fr_restmap_txt_Address);
		txt_Phone = (TextView) rootView.findViewById(R.id.fr_restmap_txt_Phone);

		// Load Restarnt Info
		loadRestrauntInfo();

		return rootView;
	}

	private void loadRestrauntInfo() {

		/*
		 * Create a web request Load Reastnt Info Aug 22, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "rest_getinfo"));
		params.add(new BasicNameValuePair("id", ID + ""));
		nc.WebRequest(getActivity(), params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(getActivity())
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

					// load the restrant info
					restrauntInfo = parser.Restraunt_Parse(new JSONObject(
							result));
					afterInfoLoaded();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}

	protected void afterInfoLoaded() {
		txt_Address.setText(restrauntInfo.address);
		txt_Phone.setText(restrauntInfo.publicPhone);

		LatLng location = new LatLng(restrauntInfo.Lath, restrauntInfo.Long);
		Marker marker = map.addMarker(new MarkerOptions().position(location)
				.title(restrauntInfo.title));
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
	}
}
