package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.edspace.restraunt.classess.RestrauntOpinion;
import com.edspace.restraunt.interfaces.OnResponseListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 
 * @author ataalla
 * 
 */
public class fr_Restitem_Opinions extends Fragment {

	private int ID;
	private PullToRefreshListView listView;
	private adapter_Restaurant_Opinion adapter;
	private EditText et_Message;
	private RatingBar rb_Rate;
	private Button btn_Send;
	private boolean sendingOpinion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_restitem_opinions,
				container, false);
		// load the id
		ID = getActivity().getIntent().getExtras().getInt("id");

		// load the view
		listView = (PullToRefreshListView) rootView
				.findViewById(R.id.fr_restopinion_list_Master);
		adapter = new adapter_Restaurant_Opinion(getActivity());
		listView.getRefreshableView().setAdapter(adapter);

		// Views
		et_Message = (EditText) rootView
				.findViewById(R.id.fr_restopinion_et_Message);
		rb_Rate = (RatingBar) rootView
				.findViewById(R.id.fr_restopinion_rb_Rate);
		btn_Send = (Button) rootView.findViewById(R.id.fr_restopinion_btn_Send);

		// Setup View Events
		setupViews();

		// load opinions
		loadOpinion(0, 20);

		return rootView;
	}

	private void setupViews() {

		btn_Send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// when user click on send button, it will send message to
				// server
				if (sendingOpinion == false) {

					// set sending flag enabled
					sendingOpinion = true;

					/*
					 * Create a web request Send Opinion Aug 23, 2014
					 */
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("request",
							"restaurantopinion_add"));
					params.add(new BasicNameValuePair("id", ID + ""));
					params.add(new BasicNameValuePair("message", et_Message
							.getText() + ""));
					params.add(new BasicNameValuePair("rate", (int) rb_Rate
							.getRating() + ""));
					nc.WebRequest(getActivity(), params,
							new OnResponseListener() {

								@Override
								public void onUnSuccess(String message) {
									new AlertDialog.Builder(getActivity())
											.setTitle(R.string.ops)
											.setMessage(message)
											.setPositiveButton(
													R.string.ok,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface arg0,
																int arg1) {

														}
													}).create().show();

									sendingOpinion = false;

								}

								@Override
								public void onSuccess(String result) {

									try {
										Toast.makeText(
												getActivity(),
												"نظر شما با موفقیت ارسال گردید",
												Toast.LENGTH_SHORT).show();

										loadOpinion(0, 5);

									} catch (Exception e) {
										e.printStackTrace();
									}

									sendingOpinion = false;
								}

								@Override
								public void onError() {
									sendingOpinion = false;
								}
							});
				}

			}
		});

	}

	private void loadOpinion(int start, int limit) {

		/*
		 * Create a web request Load Opinions Aug 23, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "restaurantopinion_fetch"));
		params.add(new BasicNameValuePair("id", ID + ""));
		params.add(new BasicNameValuePair("start", start + ""));
		params.add(new BasicNameValuePair("limit", limit + ""));
		nc.WebRequest(getActivity(), params, new OnResponseListener() {

			@Override
			public void onUnSuccess(String message) {
				new AlertDialog.Builder(getActivity())
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

					// load the data
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonobject = jsonArray.getJSONObject(i);

						// parse categories
						RestrauntOpinion image = parser
								.RestrauntOpinion_Parse(jsonobject);

						// add the category to adapter
						adapter.Add(image);

					}

					// refresh adapter
					adapter.Refresh();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError() {

			}
		});

	}
}
