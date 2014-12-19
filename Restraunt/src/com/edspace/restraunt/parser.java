package com.edspace.restraunt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.Menu;
import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.classess.RestrauntImage;
import com.edspace.restraunt.classess.RestrauntOpinion;
import com.edspace.restraunt.classess.User;

public class parser {

	public static User User_Parse(JSONObject jsonObject) throws JSONException {

		User user = new User();
		user.firstName = jsonObject.getString("FirstName");
		user.lastName = jsonObject.getString("LastName");
		user.userid = jsonObject.getString("UserID");
		user.level = jsonObject.getString("Level");
		user.userName = jsonObject.getString("UserName");
		return user;
	}

	public static Restaurant Restraunt_Parse(JSONObject jsonobject)
			throws JSONException {

		Restaurant rest = new Restaurant();
		rest.id = jsonobject.getInt("ID");
		rest.title = jsonobject.getString("Title");
		rest.address = jsonobject.getString("Address");
		rest.imageLink = jsonobject.getString("ImageLink");
		rest.publicPhone = jsonobject.getString("PublicPhone");
		rest.Lath = jsonobject.getDouble("Lath");
		rest.Long = jsonobject.getDouble("Long");
		rest.MessageNumber = jsonobject.getString("MessageNumber");
		rest.Email = jsonobject.getString("Email");
		rest.MenuCount = jsonobject.getInt("MenuCount");
		rest.Rating = jsonobject.getLong("Rating");
		rest.Description = jsonobject.getString("Description");
		rest.SaveState = jsonobject.getInt("SaveState") == 1 ? true : false;
		return rest;
	}

	public static Category Category_Parse(JSONObject jsonobject)
			throws JSONException {

		Category category = new Category();
		category.id = jsonobject.getInt("ID");
		category.menucount = jsonobject.getInt("MenuCounts");
		category.title = jsonobject.getString("Title");
		category.imagelink = jsonobject.getString("ImageLink");

		// check if menu included with category
		if (!jsonobject.isNull("Menus")) {
			JSONArray items = jsonobject.getJSONArray("Menus");
			for (int i = 0; i < items.length(); i++) {
				JSONObject menuItem = items.getJSONObject(i);
				Menu menu = parser.Menu_Parse(menuItem);
				// load the parent category for this menu
				menu.category = category;
				category.menus.add(menu);
			}
		}

		return category;

	}

	public static Menu Menu_Parse(JSONObject jsonobject) throws JSONException {
		JSONObject menuItem = jsonobject;
		Menu menu = new Menu();
		menu.id = menuItem.getInt("ID");
		menu.title = menuItem.getString("Title");
		menu.description = menuItem.getString("Description");
		menu.imagelink = menuItem.getString("ImageLink");
		menu.servecount = menuItem.getInt("ServeCount");
		menu.price = menuItem.getInt("Price");

		return menu;
	}

	public static Category Category_Parse(JSONObject jsonobject,
			Restaurant restaurant) throws JSONException {

		Category result = Category_Parse(jsonobject);
		result.restaurant = restaurant;
		return result;

	}

	public static RestrauntImage RestrauntImage_Parse(JSONObject jsonobject)
			throws JSONException {
		RestrauntImage rest = new RestrauntImage();
		rest.id = jsonobject.getInt("ID");
		rest.Title = jsonobject.getString("Title");
		rest.Description = jsonobject.getString("Description");
		rest.ImageLink = jsonobject.getString("ImageLink");
		return rest;
	}

	public static RestrauntOpinion RestrauntOpinion_Parse(JSONObject jsonobject)
			throws JSONException {

		RestrauntOpinion rest = new RestrauntOpinion();
		rest.id = jsonobject.getInt("ID");
		rest.message = jsonobject.getString("Message");
		rest.date = jsonobject.getLong("Date");
		rest.rate = jsonobject.getInt("Rate");
		rest.user = parser.User_Parse(jsonobject.getJSONObject("User"));
		return rest;
	}
}
