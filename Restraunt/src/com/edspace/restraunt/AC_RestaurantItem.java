package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.edspace.restraunt.interfaces.OnResponseListener;

public class AC_RestaurantItem extends Activity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_restitem);

		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(5);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			CustomTextView ctv = new CustomTextView(AC_RestaurantItem.this);
			ctv.setText(mSectionsPagerAdapter.getPageTitle(i));
			ctv.setTextColor(Color.WHITE);
			ctv.setPadding(12, 12, 12, 12);
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this).setCustomView(ctv));
		}

		// send request to server about restaurant visit
		onRestaurantVisit(getIntent().getExtras().getInt("id"));

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	private void onRestaurantVisit(int id) {
		/*
		 * Create a web request on restaurant visit Sep 2, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "visit"));
		params.add(new BasicNameValuePair("type", "restaurant"));
		params.add(new BasicNameValuePair("id", id + ""));
		nc.WebRequest(this, params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(AC_RestaurantItem.this)
						.setTitle(R.string.ops)
						.setMessage(message)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								}).create().show();

			}

			@Override
			public void onSuccess(String result) {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ac__restaurant_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 0:
				fr_Restitem_Info fr_Restitem_Info = new fr_Restitem_Info();
				return fr_Restitem_Info;
			case 1:
				fr_Restitem_Menu fr_Restitem_menu = new fr_Restitem_Menu();
				return fr_Restitem_menu;
			case 2:
				fr_Restitem_Images fr_Restitem_Images = new fr_Restitem_Images();
				return fr_Restitem_Images;
			case 3:
				fr_Restitem_Opinions fr_Restitem_Opinions = new fr_Restitem_Opinions();
				return fr_Restitem_Opinions;
			case 4:
				fr_Restitem_Map fr_Restitem_Map = new fr_Restitem_Map();
				return fr_Restitem_Map;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
			case 0:
				return "اطلاعات";
			case 1:
				return "منو ها";
			case 2:
				return "تصاویر";
			case 3:
				return "نظرات";
			case 4:
				return "نقشه";
			}
			return null;
		}
	}

}
