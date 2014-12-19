package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import com.edspace.restraunt.classess.Category;
import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.classess.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class classess {

	public static class RestrauntOpinion {

		public int id;
		public String message;
		public int rate;
		public long date;
		public User user;

		public String getDate() {
			// return sf.GetDateFromUnix(this.date, 0);
			return "Formated Date";
		}

	}

	public static class RestrauntImage {

		public int id;
		public String Title;
		public String ImageLink;
		public String Description;

	}

	public static class Menu {

		public int id;
		public String title;
		public String imagelink;
		public int price;
		public int servecount;
		public String description;
		public Category category;

	}

	public static class Category {

		public int id;
		public String title;
		public String imagelink;
		public int menucount;
		public List<Menu> menus;
		public Restaurant restaurant;

		public Category() {
			this.menus = new ArrayList<classess.Menu>();
		}

	}

	public static class User {

		public String firstName;
		public String lastName;
		public String userid;
		public String level;
		public String userName;

		public String getFullName() {
			// TODO Auto-generated method stub
			return firstName + " " + lastName;
		}

	}

	public static class Restaurant {

		public String title;
		public String address;
		public String imageLink;
		public int id;
		public String publicPhone;
		public double Lath;
		public double Long;
		protected boolean SaveState;
		public String MessageNumber;
		public String Email;
		public int MenuCount;
		public long Rating;
		public String Description;

		public Restaurant() {
			super();
		}

		public Restaurant(int id, String title, String imageLink) {
			super();
			this.title = title;
			this.imageLink = imageLink;
			this.id = id;
		}

		/**
		 * Start restaurant activity
		 * 
		 * @param context
		 */
		public void Launch(Context context) {
			Intent intent = new Intent(context, AC_RestaurantItem.class);
			intent.putExtra("id", this.id);
			((Activity) context).startActivity(intent);

		}

	}
}
