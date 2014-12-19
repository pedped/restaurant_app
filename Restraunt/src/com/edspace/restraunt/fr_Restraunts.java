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
import android.test.PerformanceTestCase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;

import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/*
 * Fragment Description
 */
public class fr_Restraunts extends Fragment {

	private adapter_Restaurants adapter;
	private EditText et_Search;
	protected String searchText = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fr_restaurants, container,
				false);

		// create adapter
		adapter = new adapter_Restaurants(getActivity());
		PullToRefreshListView listview = (PullToRefreshListView) rootView
				.findViewById(R.id.fr_master_ptr_master);
		listview.getRefreshableView().setAdapter(adapter);

		// setup pull to refresh events
		loadReastrants(0, config.getMasterListLimit());

		// Search Box
		et_Search = (EditText) rootView
				.findViewById(R.id.fr_restaurant_et_search);

		// create on text change event listner
		et_Search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable text) {
				searchText = text.toString();

				// Clear old results
				adapter.clear();

				// fetch results
				loadReastrants(0, 20);

			}
		});

		return rootView;
	}

	/**
	 * Load restraunts based on user city
	 * 
	 * @param startID
	 * @param limit
	 */
	private void loadReastrants(int startID, int limit) {

		/* 
		 * Create a web request Load Restrants Aug 20, 2014
		 */
		String userCityID = sf.PerfernceManager_Read(getActivity(), "cityid");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "rest_listbycity"));
		params.add(new BasicNameValuePair("cityid", userCityID));
		params.add(new BasicNameValuePair("startid", startID + ""));
		params.add(new BasicNameValuePair("limit", limit + "")); 
		if (searchText.length() > 0) {
			params.add(new BasicNameValuePair("searchtext", searchText + ""));
		}
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

					// check if we have any items
					if (jsonArray.length() > 0) {
						// show the results
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonobject = jsonArray.getJSONObject(i);

							// load the item
							Restaurant rest = parser
									.Restraunt_Parse(jsonobject);
							adapter.Add(rest);
						}
					}
					// refresh new items
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
