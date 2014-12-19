package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.Menu;
import com.edspace.restraunt.db.OrderItem;

public class adapter_Orders extends BaseExpandableListAdapter {
	private Context context;
	private List<CategoryOrder> items = new ArrayList<CategoryOrder>();
	private ImageLoader imageLoader;

	private static class CategoryOrder {
		public Category category;
		public List<OrderItem> orders = new ArrayList<db.OrderItem>();
	}

	public adapter_Orders(Context context) {
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

			convertView = infalInflater.inflate(R.layout.lvi_order_menuitem,
					null);
		}

		// Title
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.lvi_order_menu_Title);
		txt_title.setText(menu.title);

		// Total Price
		TextView txt_totalprice = (TextView) convertView
				.findViewById(R.id.lvi_order_menu_TotalPrice);
		txt_totalprice.setText((menu.price * order.Count) + "");

		// Price
		TextView txt_price = (TextView) convertView
				.findViewById(R.id.lvi_order_menu_Price);
		txt_price.setText(order.Count + "*" + menu.price);

		// Description
		TextView txt_description = (TextView) convertView
				.findViewById(R.id.lvi_order_menu_Description);
		txt_description.setText(menu.description);

		// Image
		ImageView img_main = (ImageView) convertView
				.findViewById(R.id.lvi_order_menu_Image);
		imageLoader.DisplayImage(nc.websiteurl + menu.imagelink, img_main);

		// Add Button
		Button btn_Remove = (Button) convertView
				.findViewById(R.id.lvi_order_menu_Remove);
		btn_Remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.items.get(groupPosition).orders.size();
	}

	@Override
	public CategoryOrder getGroup(int groupPosition) {
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
		Category category = getGroup(groupPosition).category;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.lvi_order_category,
					null);
		}

		// Title
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.lvi_order_category_Title);
		txt_title.setText(category.title);

		// Menu Count
		TextView txt_menucount = (TextView) convertView
				.findViewById(R.id.lvi_order_category_MenuCount);
		txt_menucount.setText(getGroup(groupPosition).orders.size() + "");

		// Image
		ImageView img_main = (ImageView) convertView
				.findViewById(R.id.lvi_order_category_img_Image);
		imageLoader.DisplayImage(nc.websiteurl + category.imagelink, img_main);

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

	public void Add(OrderItem orderitem) {

		// check if order item is not exist, add that to the list
		for (CategoryOrder item : items) {
			if (orderitem.category.id == item.category.id) {
				item.orders.add(orderitem);
				return;
			}
		}

		// it is not exist
		// we have to create new category order
		CategoryOrder co = new CategoryOrder();
		co.category = orderitem.category;
		co.orders.add(orderitem);
		items.add(co);
	}
}
