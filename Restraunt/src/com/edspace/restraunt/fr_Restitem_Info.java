package com.edspace.restraunt;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edspace.restraunt.classess.Restaurant;
import com.edspace.restraunt.interfaces.OnResponseListener;

/**
 * 
 * @author ataalla
 * 
 */
public class fr_Restitem_Info extends Fragment {

	private int ID;
	protected Restaurant restrauntInfo;
	private ImageView view_Img_Main;
	private ImageLoader imageLoader;
	private TextView view_Txt_Title;
	private TextView view_Txt_Address;
	private ImageButton btn_Save;
	private ImageButton btn_Unsave;
	private Button btn_Call;
	private Button btn_Email;
	private Button btn_Message;
	private ProgressBar prg_Save;
	private boolean flagSaveUnsave = false;
	private TextView view_Txt_Deliver;
	private TextView view_Txt_Rate;
	private TextView view_Txt_MenuCount;
	private TextView view_Txt_Description;
	private RatingBar view_rb_Rate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fr_restitem_info, container,
				false);
		// load the id
		ID = getActivity().getIntent().getExtras().getInt("id");

		// Image Loader
		imageLoader = new ImageLoader(getActivity());

		// Views
		view_Img_Main = (ImageView) rootView
				.findViewById(R.id.fr_restinfo_img_main);
		view_Txt_Title = (TextView) rootView
				.findViewById(R.id.fr_restinfo_txt_Title);
		view_Txt_Address = (TextView) rootView
				.findViewById(R.id.fr_restinfo_txt_Address);
		view_Txt_Deliver = (TextView) rootView
				.findViewById(R.id.fr_restinfo_txt_deliver);
		view_Txt_Rate = (TextView) rootView
				.findViewById(R.id.fr_restinfo_txt_rate);
		view_Txt_MenuCount = (TextView) rootView
				.findViewById(R.id.fr_restinfo_txt_menucount);
		view_Txt_Description = (TextView) rootView
				.findViewById(R.id.fr_restinfo_txt_Description);
		view_rb_Rate = (RatingBar) rootView
				.findViewById(R.id.fr_restinfo_rb_Rate);

		// Save , Unsave Button
		btn_Save = (ImageButton) rootView
				.findViewById(R.id.fr_restinfo_btn_Save);
		prg_Save = (ProgressBar) rootView
				.findViewById(R.id.fr_restinfo_prg_savestatus);
		btn_Unsave = (ImageButton) rootView
				.findViewById(R.id.fr_restinfo_btn_Unsave);
		btn_Call = (Button) rootView.findViewById(R.id.fr_restinfo_btn_Call);
		btn_Email = (Button) rootView.findViewById(R.id.fr_restinfo_btn_Email);
		btn_Message = (Button) rootView
				.findViewById(R.id.fr_restinfo_btn_Message);

		// we have to hide the progress bar
		prg_Save.setVisibility(View.GONE);

		// Create Saveunsave Button
		btn_Save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestSaveRestaurant(getActivity(), ID);
			}
		});

		btn_Unsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestUnsaveRestaurant(getActivity(), ID);

			}
		});

		btn_Call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestCall(getActivity(), ID);
			}
		});

		btn_Message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestMessage(getActivity(), ID);
			}
		});

		btn_Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				requestEmail(getActivity(), ID);
			}
		});

		// load the restaurant info
		loadRestrauntInfo();

		return rootView;
	}

	protected void requestEmail(Activity activity, int iD2) {
		sf.sendEmail(activity, restrauntInfo.Email, "به " + restrauntInfo.title);
	}

	protected void requestMessage(Activity activity, int iD2) {
		sf.sendTextMessage(activity, restrauntInfo.MessageNumber);
	}

	protected void requestCall(Activity activity, int iD2) {
		sf.call(activity, restrauntInfo.publicPhone);
	}

	protected void requestUnsaveRestaurant(Activity activity, int iD2) {

		// we have to request server to save the restaurant
		if (!this.flagSaveUnsave) {

			// show the progress bar
			prg_Save.setVisibility(View.VISIBLE);
			btn_Save.setVisibility(View.GONE);
			btn_Unsave.setVisibility(View.GONE);

			// set flag
			flagSaveUnsave = true;

			// request server to save this restaurant
			/*
			 * Create a web request Request To Save Sep 3, 2014
			 */
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request",
					"restaurant_saveunsave"));
			params.add(new BasicNameValuePair("id", ID + ""));
			nc.WebRequest(getActivity(), params, new OnResponseListener() {

				@Override
				public void onUnSuccess(String message) {
					new AlertDialog.Builder(getActivity())
							.setTitle(R.string.ops)
							.setMessage(message)
							.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {

										}
									}).create().show();

					flagSaveUnsave = false;
					btn_Save.setVisibility(View.GONE);
					btn_Unsave.setVisibility(View.VISIBLE);
					prg_Save.setVisibility(View.GONE);

				}

				@Override
				public void onSuccess(String result) {
					try {
						// get result
						if (Integer.valueOf(result) == 1) {
							// it is on save state now
							btn_Unsave.setVisibility(View.VISIBLE);
							btn_Save.setVisibility(View.GONE);
							restrauntInfo.SaveState = true;

							Toast.makeText(getActivity(),
									"رستوران با موفقیت ذخیره گردید",
									Toast.LENGTH_LONG).show();
						} else if (Integer.valueOf(result) == 2) {
							// it is on unsave state now
							btn_Unsave.setVisibility(View.GONE);
							btn_Save.setVisibility(View.VISIBLE);
							restrauntInfo.SaveState = false;
							Toast.makeText(
									getActivity(),
									"رستوران با موفقیت از لیست ذخیره ها حذف گردید",
									Toast.LENGTH_LONG).show();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					flagSaveUnsave = false;
					prg_Save.setVisibility(View.GONE);

				}

				@Override
				public void onError() {
					flagSaveUnsave = false;
					btn_Save.setVisibility(View.GONE);
					btn_Unsave.setVisibility(View.VISIBLE);
					prg_Save.setVisibility(View.GONE);
				}
			});
		}

	}

	protected void requestSaveRestaurant(Activity activity, int iD2) {

		// we have to request server to save the restaurant
		if (!this.flagSaveUnsave) {

			// show the progress bar
			prg_Save.setVisibility(View.VISIBLE);
			btn_Save.setVisibility(View.GONE);
			btn_Unsave.setVisibility(View.GONE);

			// set flag
			flagSaveUnsave = true;

			// request server to save this restaurant
			/*
			 * Create a web request Request To Save Sep 3, 2014
			 */
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("request",
					"restaurant_saveunsave"));
			params.add(new BasicNameValuePair("id", ID + ""));
			nc.WebRequest(getActivity(), params, new OnResponseListener() {

				@Override
				public void onUnSuccess(String message) {
					new AlertDialog.Builder(getActivity())
							.setTitle(R.string.ops)
							.setMessage(message)
							.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {

										}
									}).create().show();

					flagSaveUnsave = false;
					btn_Save.setVisibility(View.VISIBLE);
					btn_Unsave.setVisibility(View.GONE);
					prg_Save.setVisibility(View.GONE);

				}

				@Override
				public void onSuccess(String result) {
					try {
						// get result
						if (Integer.valueOf(result) == 1) {
							// it is on save state now
							btn_Unsave.setVisibility(View.VISIBLE);
							btn_Save.setVisibility(View.GONE);
							restrauntInfo.SaveState = true;

							Toast.makeText(getActivity(),
									"رستوران با موفقیت ذخیره گردید",
									Toast.LENGTH_LONG).show();
						} else if (Integer.valueOf(result) == 2) {
							// it is on unsave state now
							btn_Unsave.setVisibility(View.GONE);
							btn_Save.setVisibility(View.VISIBLE);
							restrauntInfo.SaveState = false;

							Toast.makeText(
									getActivity(),
									"رستوران با موفقیت از لیست ذخیره ها حذف گردید",
									Toast.LENGTH_LONG).show();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					flagSaveUnsave = false;
					prg_Save.setVisibility(View.GONE);

				}

				@Override
				public void onError() {
					flagSaveUnsave = false;
					btn_Save.setVisibility(View.VISIBLE);
					btn_Unsave.setVisibility(View.GONE);
					prg_Save.setVisibility(View.GONE);
				}
			});
		}
	}

	private void loadRestrauntInfo() {

		// Create Loading for fragment
		sf.Actvity_SetLoading(getActivity());

		/*
		 * Create a web request Load Restaurant Info Aug 22, 2014
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("request", "rest_getinfo"));
		params.add(new BasicNameValuePair("id", ID + ""));
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
				sf.Actvity_SetLoading(getActivity(), false, true);
			}

			@Override
			public void onSuccess(String result) {
				try {

					// load the restaurant info
					restrauntInfo = parser.Restraunt_Parse(new JSONObject(
							result));
					afterInfoLoaded();
				} catch (Exception e) {
					e.printStackTrace();
				}
				sf.Actvity_SetLoading(getActivity(), false, true);

			}

			@Override
			public void onError() {
				sf.Actvity_SetLoading(getActivity(), false, true);
			}
		});

	}

	/**
	 * this function calls when info about the restaurant loaded from server
	 */
	protected void afterInfoLoaded() {

		// load the restaurant image
		imageLoader.DisplayImage(nc.websiteurl + restrauntInfo.imageLink,
				view_Img_Main);

		// load the title
		view_Txt_Title.setText(restrauntInfo.title);

		// load the address
		view_Txt_Address.setText(restrauntInfo.address);

		// check for save state
		if (restrauntInfo.SaveState) {
			// it is saved
			btn_Save.setVisibility(View.GONE);
			btn_Unsave.setVisibility(View.VISIBLE);
		} else {
			// it is not saved
			btn_Save.setVisibility(View.VISIBLE);
			btn_Unsave.setVisibility(View.GONE);
		}

		view_Txt_MenuCount.setText(restrauntInfo.MenuCount + " منو");

		view_Txt_Rate.setText(restrauntInfo.Rating + "");

		getActivity().setTitle(restrauntInfo.title);

		view_Txt_Description.setText(Html.fromHtml(restrauntInfo.Description));

		view_rb_Rate.setRating(restrauntInfo.Rating);
	}
}
