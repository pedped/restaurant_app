package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edspace.restraunt.classess.Menu;
import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.db.OrderItem;

public class adapter_OrdersHistory extends BaseExpandableListAdapter {
	private Context context;
	private List<RestaurantOrderHistory> items = new ArrayList<RestaurantOrderHistory>();
	private ImageLoader imageLoader;

	public static class RestaurantOrderHistory {
		public Restaurant restaurant;
		public List<OrderItem> orders = new ArrayList<db.OrderItem>();
		protected int State;
		protected String RejectTime;
		protected String ProcceedDate;
		protected long Date;
	}

	public adapter_OrdersHistory(Context context) {
		this.context = context;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final OrderItem order = getChild(groupPosition, childPosition);
		final Menu menu = order.menu;
		final LayoutInflater infalInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {

			convertView = infalInflater.inflate(
					R.layout.lvi_orderhistory_menuitem, null);
		}

		// Title
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_menu_Title);
		txt_title.setText(menu.title);

		// Total Price
		TextView txt_totalprice = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_menu_TotalPrice);
		txt_totalprice.setText((menu.price * order.Count) + "");

		// Price
		TextView txt_price = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_menu_Price);
		txt_price.setText(order.Count + "*" + menu.price);

		// Description
		TextView txt_description = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_menu_Description);
		txt_description.setText(menu.description);

		// Image
		ImageView img_main = (ImageView) convertView
				.findViewById(R.id.lvi_orderhistory_menu_Image);
		imageLoader.DisplayImage(nc.websiteurl + menu.imagelink, img_main);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.items.get(groupPosition).orders.size();
	}

	@Override
	public RestaurantOrderHistory getGroup(int groupPosition) {
		return this.items.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.items.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Restaurant restaurant = getGroup(groupPosition).restaurant;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(
					R.layout.lvi_orderhistory_category, null);
		}

		// Title
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_category_RestaurantTitle);
		txt_title.setText(restaurant.title);

		// Status
		TextView txt_status = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_category_Status);
		switch (getGroup(groupPosition).State) {
		case -1:
			txt_status.setText("عدم تایید سفارش");
			break;
		case 0:
			txt_status.setText("در حال انتظار");
			break;
		case 1:
			txt_status.setText("تایید شده");
			break;

		default:
			break;
		}

		// Date
		TextView txt_date = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_category_Date);
		txt_date.setText("ارسال شده در "
				+ sf.getDate(getGroup(groupPosition).Date));

		// Menu Count
		TextView txt_menucount = (TextView) convertView
				.findViewById(R.id.lvi_orderhistory_category_MenuCount);
		txt_menucount.setText(getGroup(groupPosition).orders.size() + "");

		// Image
		ImageView img_main = (ImageView) convertView
				.findViewById(R.id.lvi_orderhistory_category_img_Image);
		imageLoader
				.DisplayImage(nc.websiteurl + restaurant.imageLink, img_main);

		return convertView;
	}

	@Override
	public OrderItem getChild(int groupPosition, int childPosititon) {
		return this.items.get(groupPosition).orders.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return this.items.get(groupPosition).orders.get(childPosition).MenuID;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void Refresh() {
		this.notifyDataSetChanged();
	}

	public void Clear() {
		this.items.clear();
	}

	public void Add(RestaurantOrderHistory oh) {
		items.add(oh);
	}
}
