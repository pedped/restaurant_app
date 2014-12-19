package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.edspace.restraunt.classess.RestrauntImage;

public class AC_RestImagesShower extends Activity {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_restimage_shower);

		// get items
		String items = getIntent().getExtras().getString("items");
		int position = getIntent().getExtras().getInt("position");

		// we have to parse the items
		try {
			// Create a list for restaurant images
			List<RestrauntImage> images = new ArrayList<classess.RestrauntImage>();

			// load the data
			JSONArray jsonArray = new JSONArray(items);
			for (int i = 0; i < jsonArray.length(); i++) {

				// load json item
				JSONObject jsonobject = jsonArray.getJSONObject(i);

				// parse categories
				RestrauntImage image = parser.RestrauntImage_Parse(jsonobject);

				// add the image to list
				images.add(image);

			}

			//
			// Create the adapter that will return a fragment for each of the
			// three
			// primary sections of the activity.
			mSectionsPagerAdapter = new SectionsPagerAdapter(
					getFragmentManager(), images);

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mSectionsPagerAdapter);
			
			mViewPager.setCurrentItem(position);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ac__rest_images_shower, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		private List<RestrauntImage> images;

		public SectionsPagerAdapter(FragmentManager fm,
				List<RestrauntImage> images) {
			super(fm);
			this.images = images;
		}

		@Override
		public Fragment getItem(int position) {

			fr_RestImageShower item = new fr_RestImageShower();
			Bundle bundle = new Bundle();
			bundle.putString("title", images.get(position).Title);
			bundle.putString("imagelink", images.get(position).ImageLink);
			bundle.putString("description", images.get(position).Description);
			item.setArguments(bundle);
			return item;

		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return images.get(position).Title;
		}
	}

}
