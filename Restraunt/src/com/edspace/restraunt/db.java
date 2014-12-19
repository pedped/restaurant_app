package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.Menu;
import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.edspace.restraunt.interfaces.onMenuResponse;

public class db {

	private Context context;

	private static final String DATABASE_NAME = "r_database";
	private static final int DATABASE_VER = 11;
	private SQLiteOpenHelper helper;
	private SQLiteDatabase database;

	public db(Context con) {
		this.context = con;
	}

	public static class OrderItem {
		/**
		 * Order Count
		 */
		public int Count;
		public Category category;
		public Menu menu;
		public Restaurant restraunt;
		public int MenuID;
		public int OrderID;
		public boolean Proceed;

		public OrderItem() {
			super();
		}

		public OrderItem(int count, Category category, Menu menu,
				Restaurant restraunt, int menuID) {
			super();
			Count = count;
			this.category = category;
			this.menu = menu;
			this.restraunt = restraunt;
			MenuID = menuID;
		}

		public OrderItem(int Count, Menu menu) {

			this.Count = Count;

			// set menu id
			this.MenuID = menu.id;
			this.menu = menu;

			// check if menu has category
			if (menu.category != null) {
				this.category = menu.category;
			}

			// check if the category has restraunt item
			if (menu.category.restaurant != null) {
				this.restraunt = menu.category.restaurant;
			}

		}

		public void validateBeforeOrder(final Context context,
				final onMenuResponse response) {
			if (!this.isValidForServe()) {
				// we will send request to server to know full serve info
				Log.d("ServeItem", "request serve info from server");

				// send request
				/*
				 * Create a web request Fetch Full Menu Item Aug 23, 2014
				 */
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("request", "menu_getfullinfo"));
				params.add(new BasicNameValuePair("id", MenuID + ""));
				nc.WebRequest(context, params, new OnResponseListener() {

					@Override
					public void onUnSuccess(String message) {
						new AlertDialog.Builder(context)
								.setTitle(R.string.ops)
								.setMessage(message)
								.setPositiveButton(R.string.ok,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {

											}
										}).create().show();

						response.OnProblem();

					}

					@Override
					public void onSuccess(String result) {
						try {

							response.onDone();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onError() {
						response.OnProblem();
					}
				});
			} else {
				// it is valid for request
				response.onDone();
			}
		}

		/**
		 * this function checks if of items are valid for sending to server or
		 * database
		 * 
		 * @return
		 */
		private boolean isValidForServe() {
			return this.Count > 0 && this.category != null && this.menu != null
					&& this.restraunt != null;
		}

		public String encode() throws JSONException {
			JSONObject json = new JSONObject();
			json.put("Count", this.Count);

			// menu info
			json.put("MenuID", this.MenuID);
			json.put("MenuPrice", this.menu.price);
			json.put("MenuServeCount", this.menu.servecount);
			json.put("MenuDescription", this.menu.description);
			json.put("MenuImageLink", this.menu.imagelink);
			json.put("MenuTitle", this.menu.title);

			// category info
			json.put("CategoryID", this.category.id);
			json.put("CategoryTitle", this.category.title);
			json.put("CategoryImageLink", this.category.imagelink);

			// restaurant info
			json.put("RestaurantID", this.restraunt.id);
			json.put("RestaurantTitle", this.restraunt.title);
			json.put("RestaurantImageLink", this.restraunt.imageLink);
			json.put("RestaurantPublicPhone", this.restraunt.publicPhone);
			json.put("RestaurantAddress", this.restraunt.address);

			Log.d("order info", json.toString());
			return json.toString();
		}

		public OrderItem decode(String encodedString) {
			try {

				JSONObject json = new JSONObject(encodedString);
				// menu info
				this.menu = new Menu();
				this.menu.id = json.getInt("MenuID");
				this.menu.price = json.getInt("MenuPrice");
				this.menu.servecount = json.getInt("MenuServeCount");
				this.menu.description = json.getString("MenuDescription");
				this.menu.imagelink = json.getString("MenuImageLink");
				this.menu.title = json.getString("MenuTitle");

				// category info
				this.category = new Category();
				this.category.id = json.getInt("CategoryID");
				this.category.title = json.getString("CategoryTitle");
				this.category.imagelink = json.getString("CategoryImageLink");

				// restaurant info
				this.restraunt = new Restaurant();
				this.restraunt.id = json.getInt("RestaurantID");
				this.restraunt.title = json.getString("RestaurantTitle");
				this.restraunt.imageLink = json
						.getString("RestaurantImageLink");
				this.restraunt.publicPhone = json
						.getString("RestaurantPublicPhone");
				this.restraunt.address = json.getString("RestaurantAddress");

				this.menu.category = category;
				this.category.restaurant = restraunt;
				this.Count = json.getInt("Count");

				return this;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}
	}

	public static class db_RestrauntItem {
		public static final String TABLE_NAME = "restaurant";
		public static final String KEY_ID = "r_id";
		public static final String KEY_TITLE = "r_title";
		public static final String KEY_IMAGELINK = "r_imagelink";
	}

	public static class db_OrderItem {
		public static final String TABLE_NAME = "orderitems";
		public static final String KEY_ID = "o_id";
		public static final String KEY_RESTAURANTID = "o_rid";
		public static final String KEY_MENUID = "o_mid";
		public static final String KEY_INFO = "o_info";
		public static final String KEY_COUNT = "o_count";
		public static final String KEY_PROCCEDD = "o_proceed";
	}

	public class DB_Helper extends SQLiteOpenHelper {

		public DB_Helper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VER);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			try {
				// ORDER ITEM
				db.execSQL("CREATE TABLE " + db_OrderItem.TABLE_NAME + " ("
						+ db_OrderItem.KEY_ID
						+ " INTEGER PRIMARY KEY AUTOINCREMENT , "
						+ db_OrderItem.KEY_RESTAURANTID + " INTEGER NOT NULL, "
						+ db_OrderItem.KEY_COUNT + " INTEGER NOT NULL, "
						+ db_OrderItem.KEY_PROCCEDD + " INTEGER NOT NULL, "
						+ db_OrderItem.KEY_MENUID
						+ " INTEGER NOT NULL UNIQUE, " + db_OrderItem.KEY_INFO
						+ " STRING NOT NULL );");

				// Restaurant ITEM
				db.execSQL("CREATE TABLE " + db_RestrauntItem.TABLE_NAME + " ("
						+ db_RestrauntItem.KEY_ID + " INTEGER PRIMARY KEY , "
						+ db_RestrauntItem.KEY_TITLE + " STRING NOT NULL, "
						+ db_RestrauntItem.KEY_IMAGELINK + " STRING NOT NULL);");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + db_OrderItem.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + db_RestrauntItem.TABLE_NAME);
			onCreate(db);

		}

	}

	public db open() {
		helper = new DB_Helper(this.context);
		database = helper.getWritableDatabase();
		return this;
	}

	public void close() {
		helper.close();
	}

	public long Restaurant_Add(Restaurant item) {
		ContentValues values = new ContentValues();
		values.put(db_RestrauntItem.KEY_ID, item.id);
		values.put(db_RestrauntItem.KEY_TITLE, item.title);
		values.put(db_RestrauntItem.KEY_IMAGELINK, item.imageLink);
		if (this.Restaurant_GetByID(item.id) == null) {
			// it is new item
			return database.insert(db_RestrauntItem.TABLE_NAME, null, values);
		} else {
			// its is old item
			return database.update(db_RestrauntItem.TABLE_NAME, values,
					db_RestrauntItem.KEY_ID + "='" + item.id + "'", null);
		}
	}

	public long Order_Add(OrderItem item) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(db_OrderItem.KEY_RESTAURANTID, item.restraunt.id);
		values.put(db_OrderItem.KEY_MENUID, item.MenuID);
		values.put(db_OrderItem.KEY_INFO, item.encode());
		values.put(db_OrderItem.KEY_COUNT, item.Count);
		values.put(db_OrderItem.KEY_PROCCEDD, false);

		// check if the menu was on the table before
		if (!this.Order_Exists(item.OrderID)) {
			return database.insert(db_OrderItem.TABLE_NAME, null, values);
		} else {
			this.Order_Remove(item.OrderID);
			return database.insert(db_OrderItem.TABLE_NAME, null, values);
		}

	}

	private boolean Order_Exists(int orderID) {
		Cursor cr = database.query(db_OrderItem.TABLE_NAME,
				new String[] { db_OrderItem.KEY_ID }, db_OrderItem.KEY_ID
						+ "='" + orderID + "'", null, null, null, null);

		return cr.getCount() > 0;
	}

	public Restaurant Restaurant_GetByID(int restaurantID) {
		Cursor cr = database.query(db_OrderItem.TABLE_NAME, new String[] {
				db_RestrauntItem.KEY_ID, db_RestrauntItem.KEY_TITLE,
				db_RestrauntItem.KEY_IMAGELINK }, db_RestrauntItem.KEY_ID
				+ "='" + restaurantID + "'", null, null, null, null);

		if (cr.moveToNext()) {
			return new Restaurant(cr.getInt(0), cr.getString(1),
					cr.getString(2));
		}

		return null;

	}

	public List<OrderItem> Orders_GetAllItems(int restaurantID,
			boolean includeProceedItems) {
		Cursor cr = database.query(db_OrderItem.TABLE_NAME, new String[] {
				db_OrderItem.KEY_ID, db_OrderItem.KEY_MENUID,
				db_OrderItem.KEY_RESTAURANTID, db_OrderItem.KEY_INFO,
				db_OrderItem.KEY_COUNT, db_OrderItem.KEY_PROCCEDD },
				db_OrderItem.KEY_RESTAURANTID + "='" + restaurantID + "' AND "
						+ db_OrderItem.KEY_PROCCEDD + "= '"
						+ (includeProceedItems ? 1 : 0) + "'", null, null,
				null, null);

		List<OrderItem> items = new ArrayList<OrderItem>();

		while (cr.moveToNext()) {
			OrderItem item = new OrderItem();
			item.OrderID = cr.getInt(cr.getInt(0));
			item.MenuID = cr.getInt(1);
			item.decode(cr.getString(3));
			item.Count = cr.getInt(4);
			item.Proceed = cr.getInt(5) == 1;
			items.add(item);
		}

		return items;

	}

	public List<OrderItem> Orders_GetAllItems(boolean includeProceedItems) {

		String includeFunction = (includeProceedItems == true ? db_OrderItem.KEY_PROCCEDD
				+ " = 1"
				: "true = true");

		Cursor cr = database.query(db_OrderItem.TABLE_NAME, new String[] {
				db_OrderItem.KEY_ID, db_OrderItem.KEY_MENUID,
				db_OrderItem.KEY_RESTAURANTID, db_OrderItem.KEY_INFO,
				db_OrderItem.KEY_COUNT, db_OrderItem.KEY_PROCCEDD },
				includeFunction, null, null, null, null);

		List<OrderItem> items = new ArrayList<OrderItem>();

		while (cr.moveToNext()) {
			OrderItem item = new OrderItem();
			item.OrderID = cr.getInt(cr.getInt(0));
			item.MenuID = cr.getInt(1);
			item.decode(cr.getString(3));
			item.Count = cr.getInt(4);
			item.Proceed = cr.getInt(5) == 1;
			items.add(item);
		}

		return items;

	}

	public List<Restaurant> Orders_Restaurants() {
		Cursor cr = database.query(db_OrderItem.TABLE_NAME, new String[] {
				db_OrderItem.KEY_RESTAURANTID, db_OrderItem.KEY_INFO }, null,
				null, db_OrderItem.KEY_RESTAURANTID, null, null);

		List<Restaurant> items = new ArrayList<Restaurant>();
		while (cr.moveToNext()) {
			OrderItem order = new OrderItem();
			items.add(order.decode(cr.getString(1)).restraunt);
		}
		return items;
	}

	public void Order_RemoveByRestaurantID(int restaurantID) {

		database.delete(db_OrderItem.TABLE_NAME, db_OrderItem.KEY_RESTAURANTID
				+ "=" + restaurantID, null);

	}

	public void Order_Remove(int OrderID) {

		database.delete(db_OrderItem.TABLE_NAME, db_OrderItem.KEY_ID + "="
				+ OrderID, null);

	}

	/**
	 * return non proceed order
	 * 
	 * @param menuID
	 * @return
	 */
	public OrderItem Order_GetByMenuID(int menuID) {
		Cursor cr = database.query(db_OrderItem.TABLE_NAME, new String[] {
				db_OrderItem.KEY_ID, db_OrderItem.KEY_MENUID,
				db_OrderItem.KEY_RESTAURANTID, db_OrderItem.KEY_INFO,
				db_OrderItem.KEY_COUNT, db_OrderItem.KEY_PROCCEDD },
				db_OrderItem.KEY_MENUID + " = '" + menuID + "' AND "
						+ db_OrderItem.KEY_PROCCEDD + " = '0'", null, null,
				null, null);

		if (cr.moveToNext()) {
			OrderItem item = new OrderItem();
			item.OrderID = cr.getInt(cr.getInt(0));
			item.MenuID = cr.getInt(1);
			item.decode(cr.getString(3));
			item.Count = cr.getInt(4);
			item.Proceed = cr.getInt(5) == 1;
			return item;
		}

		return null;
	}
}
