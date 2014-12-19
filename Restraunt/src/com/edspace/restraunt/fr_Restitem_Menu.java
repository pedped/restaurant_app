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
import android.widget.ExpandableListView;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * 
 * @author ataalla
 * 
 */
public class fr_Restitem_Menu extends Fragment {

	private int ID;
	private PullToRefreshExpandableListView expandableListView;
	private adapter_Restraunat_Menus adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_restitem_menu, container,
				false);
		// load the id
		ID = getActivity().getIntent().getExtras().getInt("id");

		// Load View
		expandableListView = (PullToRefreshExpandableListView) rootView
				.findViewById(R.id.fr_restmenu_expandablelist);
		adapter = new adapter_Restraunat_Menus(getActivity());
		expandableListView.getRefreshableView().setAdapter(adapter);

		// load menus
		loadMenus(0, 20);

		return rootView;
	}

	private void loadMenus(int start, int limit) {
		/*
		 * Create a web request Load menus Aug 22, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "rest_getfullmenus"));
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
					JSONObject json = new JSONObject(result);
					Restaurant restaurant = parser.Restraunt_Parse(json
							.getJSONObject("Restaurant"));

					// load restaurant menues
					JSONArray jsonArray = json.getJSONArray("Menus");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonobject = jsonArray.getJSONObject(i);

						// parse categories
						Category category = parser.Category_Parse(jsonobject,
								restaurant);

						// add the category to adapter
						adapter.Add(category);

					}

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
