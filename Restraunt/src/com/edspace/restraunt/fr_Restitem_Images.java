package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.RestrauntImage;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * 
 * @author ataalla
 * 
 */
public class fr_Restitem_Images extends Fragment {

	private int ID;
	private PullToRefreshGridView gridview;
	private adapter_Restaurant_Images adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_restitem_images,
				container, false);

		// load the id
		ID = getActivity().getIntent().getExtras().getInt("id");

		// Load View
		gridview = (PullToRefreshGridView) rootView
				.findViewById(R.id.fr_restimages_gridview);
		adapter = new adapter_Restaurant_Images(getActivity());
		gridview.getRefreshableView().setAdapter(adapter);

		// Load the images
		loadImages(0, 18);

		return rootView;
	}

	private void loadImages(int start, int limit) {
		/*
		 * Create a web request Load Restrant Images Aug 22, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "rest_getimages"));
		params.add(new BasicNameValuePair("id", ID + ""));
		params.add(new BasicNameValuePair("start", start + ""));
		params.add(new BasicNameValuePair("limit", limit + ""));
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
					// load the data
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonobject = jsonArray.getJSONObject(i);

						// parse categories
						RestrauntImage image = parser
								.RestrauntImage_Parse(jsonobject);

						// add the category to adapter
						adapter.Add(image);

					}

					adapter.setMasterImages(result);

					// refresh adapter
					adapter.Refresh();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}
}
