package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edspace.restraunt.classess.Restaurant;

public class adapter_Restaurants extends BaseAdapter {

	private Context context;
	private Activity ac;
	private ImageLoader imageLoader;

	public adapter_Restaurants(Context context) {

		this.context = context;
		this.ac = (Activity) context;
		imageLoader = new ImageLoader(context);
	}

	private List<Restaurant> items = new ArrayList<Restaurant>();

	public int getCount() {
		return items.size();
	}

	public Restaurant getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int position, View arg1, ViewGroup arg2) {

		LayoutInflater inflater = ac.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.lvi_restraunt, null);

		// get the Restraunt object
		final Restaurant Restraunt = items.get(position);

		// get the title
		TextView txt_Title = (TextView) rowView
				.findViewById(R.id.lvI_restraunt_title);
		txt_Title.setText(Restraunt.title);

		// get the Restraunt teacherName
		TextView txt_TeacherName = (TextView) rowView
				.findViewById(R.id.lvI_restraunt_address);
		txt_TeacherName.setText(Restraunt.address);

		final ImageView imageClass = (ImageView) rowView
				.findViewById(R.id.lvI_restraunt_image);
		imageLoader.DisplayImage(nc.websiteurl + Restraunt.imageLink,
				imageClass);

		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// we have to request restrannt to be launched
				Restraunt.Launch(context);

			}
		});

		Log.d("hell", "yes");

		return rowView;
	}

	public void Add(Restaurant Restraunt) {
		items.add(Restraunt);
	}

	public void Add(List<Restaurant> _items) {
		for (Restaurant Restraunt : _items) {
			items.add(Restraunt);
		}
	}

	public void Refresh() {
		this.notifyDataSetChanged();
	}

	public void clear() {
		items.clear();

	}

}
