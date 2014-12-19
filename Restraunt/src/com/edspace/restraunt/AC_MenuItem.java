package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.edspace.restraunt.classess.Menu;
import com.edspace.restraunt.interfaces.OnResponseListener;

public class AC_MenuItem extends Activity {

	private Button btn_Order;
	private TextView txt_MenuTitle;
	private TextView txt_Price;
	private TextView txt_ServeCount;
	private int ID;
	private ImageLoader imageLoader;
	private ImageView img_Main;
	private TextView txt_Description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_menuitem);

		// fetch menu id
		ID = getIntent().getExtras().getInt("id");

		// create image loader
		imageLoader = new ImageLoader(this);

		btn_Order = (Button) findViewById(R.id.ac_menuitem_btn_Order);
		txt_MenuTitle = (TextView) findViewById(R.id.ac_menuitem_txt_MenuName);
		txt_Price = (TextView) findViewById(R.id.ac_menuitem_txt_Price);
		txt_ServeCount = (TextView) findViewById(R.id.ac_menuitem_txt_ServeCount);
		txt_Description = (TextView) findViewById(R.id.ac_menuitem_txt_Description);
		img_Main = (ImageView) findViewById(R.id.ac_menuitem_img_MenuItem);

		// set loading
		sf.Actvity_SetLoading(this);

		// fetch menu information from server
		fetchMenuInformation();
	}

	private void fetchMenuInformation() {

		/*
		 * Create a web request Fetch Menu Inoformation Sep 6, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "menu_getbyid"));
		params.add(new BasicNameValuePair("id", ID + ""));
		nc.WebRequest(this, params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(AC_MenuItem.this)
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
					// we have to parse the menu item
					final Menu menu = parser.Menu_Parse(new JSONObject(result));

					// show menu information
					txt_MenuTitle.setText(menu.title);
					txt_Price.setText("قیمت : " + menu.price + " تومان");
					txt_ServeCount.setText("تعداد سرو : " + menu.servecount);
					txt_Description.setText(menu.description);

					// load the image
					imageLoader.DisplayImage(nc.websiteurl + menu.imagelink,
							img_Main);

					// set activity title
					//getActionBar().setTitle(menu.title);

					// close the loading bar
					sf.Actvity_SetLoading(AC_MenuItem.this, false);

					// create button order item
					btn_Order.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							sf.Menu_RequestOrder(AC_MenuItem.this, menu);
						}
					});

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
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ac__menu_item, menu);
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
}
