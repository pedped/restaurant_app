package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.edspace.restraunt.classess.RestrauntOpinion;

public class adapter_Restaurant_Opinion extends BaseAdapter {

	private Context context;
	private Activity ac;
	private ImageLoader imageLoader;

	public adapter_Restaurant_Opinion(Context context) {

		this.context = context;
		this.ac = (Activity) context;
		imageLoader = new ImageLoader(context);
	}

	private List<RestrauntOpinion> items = new ArrayList<RestrauntOpinion>();

	public int getCount() {
		return items.size();
	}

	public RestrauntOpinion getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int position, View arg1, ViewGroup arg2) {

		LayoutInflater inflater = ac.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.lvi_restopinion, null);

		// get the Restraunt object
		final RestrauntOpinion item = items.get(position);

		// Message
		TextView txt_Message = (TextView) rowView
				.findViewById(R.id.lvi_restopinion_txt_Message);
		txt_Message.setText(item.message);

		// Date
		TextView txt_Date = (TextView) rowView
				.findViewById(R.id.lvi_restopinion_txt_Date);
		txt_Date.setText(item.getDate());

		// Rate
		RatingBar rb_Rate = (RatingBar) rowView
				.findViewById(R.id.lvi_restopinion_rb_Rate);
		rb_Rate.setRating(item.rate);

		// Set Name
		TextView txt_Name = (TextView) rowView
				.findViewById(R.id.lvi_restopinion_txt_Name);
		txt_Name.setText(item.user.getFullName());

		return rowView;
	}

	public void Add(RestrauntOpinion item) {
		items.add(item);
	}

	public void Add(List<RestrauntOpinion> _items) {
		for (RestrauntOpinion item : _items) {
			items.add(item);
		}
	}

	public void Refresh() {
		this.notifyDataSetChanged();
	}

	public void clear() {
		items.clear();

	}

}
