package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edspace.restraunt.classess.RestrauntImage;

public class adapter_Restaurant_Images extends BaseAdapter {

	private Context context;
	private Activity ac;
	private ImageLoader imageLoader;

	public adapter_Restaurant_Images(Context context) {

		this.context = context;
		this.ac = (Activity) context;
		imageLoader = new ImageLoader(context);
	}

	private List<RestrauntImage> items = new ArrayList<RestrauntImage>();
	private String jsonedImages;

	public int getCount() {
		return items.size();
	}

	public RestrauntImage getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int position, View arg1, ViewGroup arg2) {

		LayoutInflater inflater = ac.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.lvi_rest_image, null);

		// get the Restaurant object
		final RestrauntImage restImage = items.get(position);

		// image
		final ImageView imageClass = (ImageView) rowView
				.findViewById(R.id.lvi_restimage_Image);
		imageLoader.DisplayImage(nc.websiteurl + restImage.ImageLink,
				imageClass);

		// Text View
		final TextView txtTitle = (TextView) rowView
				.findViewById(R.id.lvi_restimage_txt_Title);
		txtTitle.setText(restImage.Title);

		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, AC_RestImagesShower.class);
				intent.putExtra("items", jsonedImages);
				intent.putExtra("position", position);
				((Activity) context).startActivity(intent);
			}
		});

		return rowView;
	}

	public void Add(RestrauntImage RestrauntImage) {
		items.add(RestrauntImage);
	}

	public void Add(List<RestrauntImage> _items) {
		for (RestrauntImage restImage : _items) {
			items.add(restImage);
		}
	}

	public void Refresh() {
		this.notifyDataSetChanged();
	}

	public void clear() {
		items.clear();

	}

	public void setMasterImages(String result) {
		this.jsonedImages = result;
	}

}
