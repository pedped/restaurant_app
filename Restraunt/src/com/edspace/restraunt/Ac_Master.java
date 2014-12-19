package com.edspace.restraunt;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class Ac_Master extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private static final String TAG = "Ac_Master";
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_master);

		// check if the user is not logged in yet, hide the list
		if (sf.PerfernceManager_Read(Ac_Master.this, "userid").length() == 0) {

			Log.d(TAG, "user need to login");
			startActivity(new Intent(Ac_Master.this, AC_LoginWithEmail.class));

			// close the current actvity
			finish();
		} else {

			// check if user did not selected any city yet, force him to select
			// on
			if (sf.PerfernceManager_Read(Ac_Master.this, "cityid").length() == 0) {
				Log.d(TAG, "user need to choose city");
				startActivity(new Intent(Ac_Master.this, Ac_ChooseCity.class));
				// close the current actvity
				finish();
			}
		}

		// everything is good, we have to show the main lists of restraunt to
		// users
		fr_Restraunts fr_master = new fr_Restraunts();
		sf.setFragment(this, R.id.container, fr_master);

		// Set up the drawer.
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		onSectionAttached(position);

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			fr_ordershistory fr_history = new fr_ordershistory();
			sf.setFragment(this, R.id.container, fr_history);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			fr_RestrauntsVisit fr_visit = new fr_RestrauntsVisit();
			sf.setFragment(this, R.id.container, fr_visit);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.ac__master, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_contactus) {
			startActivity(new Intent(Ac_Master.this, AC_Contact.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
