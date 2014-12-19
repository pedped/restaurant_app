package com.edspace.restraunt;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edspace.restraunt.classess.User;

public class AC_LoginWithEmail extends Activity {

	public static final String TAG = "ac_LoginWithEmail";
	EditText et_Email;
	EditText et_Password;
	Button btn_login;
	TextView txt_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the animation
		setContentView(R.layout.ac_loginwithemail);

		// Android Layout Items
		et_Email = (EditText) findViewById(R.id.et_Email);
		et_Password = (EditText) findViewById(R.id.et_Password);
		btn_login = (Button) findViewById(R.id.btn_LunchClassroom);
		txt_result = (TextView) findViewById(R.id.txt_result);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// valid inputs
				if (!validInputs()) {
					return;
				}

				// Set loading
				new LoginAsyncTask().execute("");
			}
		});

		final Button btn_ForgetPassword = (Button) findViewById(R.id.aclogin_btn_ForgetPassword);
		btn_ForgetPassword.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Intent intent = new Intent(ac_LoginWithEmail.this,
				// ac_ForgetPassword.class);
				// if (et_Email.getText().length() > 0)
				// intent.putExtra("email", et_Email.getText().toString());
				// startActivity(intent);

			}
		});

	}

	protected boolean validInputs() {

		if (et_Email.getText().length() < 4) {
			Toast.makeText(
					this,
					"First Name can not be empty and should have at least four characters",
					Toast.LENGTH_LONG).show();
			et_Email.requestFocus();
			return false;
		}

		if (et_Password.getText().length() < 8) {
			Toast.makeText(
					this,
					"Password can not be empty and should have at least 8 characters",
					Toast.LENGTH_LONG).show();
			et_Password.requestFocus();
			return false;
		}

		return true;
	}

	class LoginAsyncTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {

			txt_result.setText("");
			sf.Actvity_SetLoading(AC_LoginWithEmail.this);
			super.onPreExecute();
		}

		@Override
		protected void onCancelled() {
			sf.Actvity_SetLoading(AC_LoginWithEmail.this, false);
			super.onCancelled();
		}

		@Override
		protected void onCancelled(String result) {
			sf.Actvity_SetLoading(AC_LoginWithEmail.this, false);
			super.onCancelled(result);
		}

		protected String doInBackground(String... urls) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("apprequest", "login"));
			params.add(new BasicNameValuePair("username", et_Email.getText()
					.toString()));
			params.add(new BasicNameValuePair("password", et_Password.getText()
					.toString()));
			params.add(new BasicNameValuePair("deviceid", sf
					.getDeviceID(AC_LoginWithEmail.this)));
			try {
				HttpClient client = new DefaultHttpClient();
				String postURL = nc.website_request_url;
				HttpPost post = new HttpPost(postURL);
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ent);
				// post.setHeader("Cache-Control", "no-cache, no-store");
				HttpResponse responsePOST = client.execute(post);
				HttpEntity resEntity = responsePOST.getEntity();
				if (resEntity != null) {
					String val = URLDecoder.decode(
							EntityUtils.toString(resEntity, "UTF-8"), "UTF-8");
					Log.d(TAG, "result is " + val);
					return val;

				}
				return "";

			} catch (HttpHostConnectException e) {
				e.printStackTrace();
				return "";
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}

		protected void onPostExecute(String result) {
			try {

				// we get the json object about login here , we have
				// to
				// unjosn that
				JSONObject jo = new JSONObject(result);
				int resultcode = jo.getInt("statuscode");
				String resultText = jo.getString("statustext");
				if (resultcode == 1) {

					// success login , get the tokan

					String resultstr = jo.getString("result");

					JSONObject resultJO = new JSONObject(resultstr);

					String tokan = resultJO.getString("token");
					User user = parser.User_Parse(resultJO
							.getJSONObject("User"));

					sf.User_Save(AC_LoginWithEmail.this, user, tokan);

					// start home page activity
					startActivity(new Intent(AC_LoginWithEmail.this,
							Ac_Master.class));

					// finish the login
					finish();

				} else {
					txt_result.setText(resultText);
					new AlertDialog.Builder(AC_LoginWithEmail.this)
							.setTitle("Oops")
							.setMessage(resultText)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {

										}
									}).create().show();
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(AC_LoginWithEmail.this,
						"There is a problem in connection to nokebook",
						Toast.LENGTH_LONG).show();
			}

			sf.Actvity_SetLoading(AC_LoginWithEmail.this, false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu., menu);
		return true;
	}
}
