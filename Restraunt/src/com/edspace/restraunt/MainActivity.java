package com.edspace.restraunt;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {

	public static FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// initialising the object of the FragmentManager. Here I'm passing
		// getSupportFragmentManager(). You can pass getFragmentManager() if you
		// are coding for Android 3.0 or above.
		fragmentManager = getFragmentManager();
	}
}