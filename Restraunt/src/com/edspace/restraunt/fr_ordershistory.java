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

import com.edspace.restraunt.adapter_OrdersHistory.RestaurantOrderHistory;
import com.edspace.restraunt.db.OrderItem;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * 
 * @author ataalla Sep 2, 2014
 */
public class fr_ordershistory extends Fragment {

	private int ID;
	private ImageLoader imageLoader;
	private adapter_OrdersHistory adapterOrders;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_ordershistory, container,
				false);

		// Image Loader
		imageLoader = new ImageLoader(getActivity());

		// load list
		adapterOrders = new adapter_OrdersHistory(getActivity());
		PullToRefreshExpandableListView list = (PullToRefreshExpandableListView) rootView
				.findViewById(R.id.fr_orderhistory_list_Main);
		list.getRefreshableView().setAdapter(adapterOrders);

		// fetch the orders from list
		loadOrders(0, 10);

		return rootView;
	}

	private void loadOrders(int start, int limit) {
		/*
		 * Create a web request Load Orders History Sep 2, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "orders_fetchhistory"));
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

					JSONArray json = new JSONArray(result);
					for (int i = 0; i < json.length(); i++) {
						JSONObject object = json.getJSONObject(i);

						// we have to create new order based on infos
						RestaurantOrderHistory restaurantOrderHistory = new RestaurantOrderHistory();
						JSONArray ordersjson = object.getJSONArray("Info");
						for (int j = 0; j < ordersjson.length(); j++) {
							OrderItem order = new OrderItem();
							order = order.decode(ordersjson.getString(j));
							order.restraunt = parser.Restraunt_Parse(object
									.getJSONObject("Restaurant"));
							restaurantOrderHistory.orders.add(order);
							restaurantOrderHistory.restaurant = order.restraunt;
						}

						restaurantOrderHistory.State = object.getInt("State");
						restaurantOrderHistory.RejectTime = object
								.getString("RejectTime");
						restaurantOrderHistory.ProcceedDate = object
								.getString("ProcceedDate");
						restaurantOrderHistory.Date = object
								.getLong("Date");

						adapterOrders.Add(restaurantOrderHistory);
					}

					adapterOrders.Refresh();

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
