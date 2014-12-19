package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.Menu;
import com.edspace.restraunt.db.OrderItem;
import com.edspace.restraunt.interfaces.onMenuResponse;

public class adapter_Restraunat_Menus extends BaseExpandableListAdapter {
	private Context context;
	private List<Category> lists = new ArrayList<classess.Category>();
	private ImageLoader imageLoader;

	public adapter_Restraunat_Menus(Context context) {
		this.context = context;
		this.imageLoader = new ImageLoader(context);
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final Menu menu = getChild(groupPosition, childPosition);
		final LayoutInflater infalInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {

			convertView = infalInflater.inflate(R.layout.lvi_restmenu_menuitem,
					null);
		}

		// Title
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.lvi_restmenu_menu_Title);
		txt_title.setText(menu.title);

		// Price
		TextView txt_price = (TextView) convertView
				.findViewById(R.id.lvi_restmenu_menu_Price);
		txt_price.setText(menu.price + " تومان");

		// Description
		TextView txt_description = (TextView) convertView
				.findViewById(R.id.lvi_restmenu_menu_Description);
		txt_description.setText(menu.description);

		// Image
		ImageView img_main = (ImageView) convertView
				.findViewById(R.id.lvi_restmenu_menu_Image);
		imageLoader.DisplayImage(nc.websiteurl + menu.imagelink, img_main);

		// Add Button
		Button btn_AddToOrder = (Button) convertView
				.findViewById(R.id.lvi_restmenu_menu_Add);
		btn_AddToOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				sf.Menu_RequestOrder(context , menu);

			}
		});

		// create event listner on items
		img_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, AC_MenuItem.class);
				intent.putExtra("id", menu.id);
				((Activity) context).startActivity(intent);
			}
		});
		txt_title.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, AC_MenuItem.class);
				intent.putExtra("id", menu.id);
				((Activity) context).startActivity(intent);
			}
		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.lists.get(groupPosition).menus.size();
	}

	@Override
	public Category getGroup(int groupPosition) {
		return this.lists.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.lists.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Category category = getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.lvi_restmenu_category,
					null);
		}

		// Title
		TextView txt_title = (TextView) convertView
				.findViewById(R.id.lvi_restmenu_category_Title);
		txt_title.setText(category.title);

		// Menu Count
		TextView txt_menucount = (TextView) convertView
				.findViewById(R.id.lvi_restmenu_category_MenuCount);
		txt_menucount.setText(category.menucount + "");

		// Image
		ImageView img_main = (ImageView) convertView
				.findViewById(R.id.lvi_restmenu_category_img_Image);
		imageLoader.DisplayImage(nc.websiteurl + category.imagelink, img_main);

		return convertView;
	}

	@Override
	public Menu getChild(int groupPosition, int childPosititon) {
		return this.lists.get(groupPosition).menus.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return this.lists.get(groupPosition).menus.get(childPosition).id;
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
		this.lists.clear();
	}

	public void Add(Category category) {
		this.lists.add(category);
	}

}
