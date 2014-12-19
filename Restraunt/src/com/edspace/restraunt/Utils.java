package com.edspace.restraunt;

import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
	
	// Google Play configuration
	static String GPlayURL = "https://play.google.com/store/apps/details?id=your.package.com";
	
	// API URL configuration
	static String AdminPageURL = "http://www.yourwebsite.com/YOUR_FOLDER/";
	static String CategoryAPI = "http://www.yourwebsite.com/YOUR_FOLDER/api/get-all-category-data.php";
	static String MenuAPI = "http://www.yourwebsite.com/YOUR_FOLDER/api/get-menu-data-by-category-id.php";
	static String TaxCurrencyAPI = "http://www.yourwebsite.com/YOUR_FOLDER/api/get-tax-and-currency.php";
	static String MenuDetailAPI = "http://www.yourwebsite.com/YOUR_FOLDER/api/get-menu-detail.php";
	static String SendDataAPI = "http://www.yourwebsite.com/YOUR_FOLDER/api/add-reservation.php";
	static String MenuSharingAPI = "http://www.yourwebsite.com/YOUR_FOLDER/menu-sharing.php";
	
	// change this access similar with accesskey in admin panel for security reason
	static String AccessKey = "12345";
	
	// database path configuration
	static String DBPath = "/data/data/com.the.restaurant/databases/";
	
	// method to check internet connection
	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// method to handle images from server
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
