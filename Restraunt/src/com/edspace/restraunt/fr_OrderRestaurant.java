package com.edspace.restraunt;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edspace.restraunt.db.OrderItem;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * 
 * @author ataalla Aug 29, 2014
 */
public class fr_OrderRestaurant extends Fragment {

	/**
	 * restaurant id
	 */
	private int ID;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_orderrestaurant,
				container, false);

		// load the id
		ID = getArguments().getInt("id");

		// Image Loader
		imageLoader = new ImageLoader(getActivity());

		// Views
		// Load orders
		db db = new db(getActivity());
		db.open();
		List<OrderItem> orders = db.Orders_GetAllItems(ID, false);
		db.close();

		// load list
		adapter_Orders adapterOrders = new adapter_Orders(getActivity());
		PullToRefreshExpandableListView list = (PullToRefreshExpandableListView) rootView
				.findViewById(R.id.fr_orderrest_list_Main);
		list.getRefreshableView().setAdapter(adapterOrders);

		// add the items to list
		for (OrderItem item : orders) {
			adapterOrders.Add(item);
		}

		// refresh the list
		adapterOrders.Refresh();

		return rootView;
	}
}
