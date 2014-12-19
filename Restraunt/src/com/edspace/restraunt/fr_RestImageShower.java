package com.edspace.restraunt;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author ataalla Sep 6, 2014
 */
public class fr_RestImageShower extends Fragment {

	private int ID;
	private ImageLoader imageLoader;
	private ImageView img_Main;
	private TextView txt_Title;
	private TextView txt_Description;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_restimageshower,
				container, false);

		// load the id
		ID = getActivity().getIntent().getExtras().getInt("id");

		// get title , imagelink , description
		String title = getArguments().getString("title");
		String description = getArguments().getString("description");
		String imagelink = getArguments().getString("imagelink");

		// Image Loader
		imageLoader = new ImageLoader(getActivity());

		// ImageView
		img_Main = (ImageView) rootView
				.findViewById(R.id.fr_restimageshower_img_MenuItem);
		txt_Title = (TextView) rootView
				.findViewById(R.id.fr_restimageshower__txt_Title);
		txt_Description = (TextView) rootView
				.findViewById(R.id.fr_restimageshower__txt_Description);

		// Load Image
		imageLoader.DisplayImage(nc.websiteurl + imagelink, img_Main);
		txt_Title.setText(title);
		txt_Description.setText(description);

		return rootView;
	}
}
