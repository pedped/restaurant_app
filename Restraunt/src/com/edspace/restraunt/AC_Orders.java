package com.edspace.restraunt;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.edspace.restraunt.classess.Restaurant;

public class AC_Orders extends Activity implements ActionBar.TabListener {

	RestaurantsOrderPageAdapter pagerAdapter;
	ViewPager mViewPager;
	public List<Restaurant> Retaurants;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_orders);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Load Restraunts
		db db = new db(this);
		db.open();
		Retaurants = db.Orders_Restaurants();
		db.close();

		// we have to create new tab based on each item
		pagerAdapter = new RestaurantsOrderPageAdapter(getFragmentManager(),
				Retaurants);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < Retaurants.size(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(Retaurants.get(i).title).setTabListener(this));
		}

		// On Send Click
		Button btn_send = (Button) findViewById(R.id.ac_order_btn_approve);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// open send order activity
				startActivity(new Intent(AC_Orders.this, AC_SendOrder.class));
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ac__orders, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class RestaurantsOrderPageAdapter extends FragmentPagerAdapter {

		private List<Restaurant> restaurants;

		public RestaurantsOrderPageAdapter(FragmentManager fm,
				List<Restaurant> restraunts) {
			super(fm);
			this.restaurants = restraunts;
		}

		@Override
		public Fragment getItem(int position) {

			fr_OrderRestaurant fr = new fr_OrderRestaurant();
			Bundle bundle = new Bundle();
			bundle.putInt("id", restaurants.get(position).id);
			fr.setArguments(bundle);
			return fr;

		}

		@Override
		public int getCount() {
			return restaurants.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return restaurants.get(position).title;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fr_acorder, container,
					false);
			return rootView;
		}
	}

}
