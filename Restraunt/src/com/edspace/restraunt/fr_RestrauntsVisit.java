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

import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/*
 * Fragment Description
 */
public class fr_RestrauntsVisit extends Fragment {

	private adapter_Restaurants adapter;

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

		return rootView;
	}

	/**
	 * Load restraunts based on user city
	 * 
	 * @param start
	 * @param limit
	 */
	private void loadReastrants(int start, int limit) {

		/*
		 * Create a web request Load Restrants Aug 20, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "visit_fetch"));
		params.add(new BasicNameValuePair("type", "restaurant"));
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

						// load the item
						Restaurant rest = parser.Restraunt_Parse(jsonobject);
						adapter.Add(rest);
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
