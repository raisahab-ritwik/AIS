package com.knwedu.ourschool.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.AisAlertPointView;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.DataStructureFramwork.Transport;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TransportFragment extends Fragment {
	private TextView tvTransportCode;
	private TextView tvRouteName;
	private TextView tvRouteDesc;
	private TextView tvPickPoint;
	private TextView tvDropPoint;
	private TextView new_route_no, new_set_alert_point;
	private TextView pr_tr_code, pr_route_name, pr_pick_point, pr_drop_point, pr_description;
	private View view;
	private ProgressDialog dialog;
	private ArrayList<Transport> transports,tr;
	private int type;
	private RelativeLayout mainLayout;
	private LatLng latlongval;
	private GoogleMap googleMap;
	private String stringLatitude, stringLongitude, startLat, startLon, busLat, busLon, endLat,endLon;
	private Marker markerStart,markerEnd,markerBus;
	private static final int TIME_INTERVAL = 15000;
	private boolean flag = false;
	private LatLng bus;
	private Context mContext;

	private boolean test = false;
	CountDownTimer t;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.transport_view, container, false);
		return view;
	}

	private void autoRefreshMap() {
		t = new CountDownTimer(Long.MAX_VALUE, TIME_INTERVAL) {

			// This is called every interval. (Every 30 seconds in this example)
			public void onTick(long millisUntilFinished) {
				System.out.println("autoRefreshMap");
				fecthLocationDetailsFromServer();
				System.out.println(busLat + "----bus----" + busLon);
				/**
				 * Animate bus marker in every 30 seconds
				 */
				animateMarker(markerBus, bus, false);
			}

			public void onFinish() {
				Log.d("test", "Timer last tick");
				Toast.makeText(getActivity(), "Auto refresh is not responding, please try with better connection",
						Toast.LENGTH_LONG).show();
				start();
			}
		}.start();


	}

	public void animateMarker(final Marker marker, final LatLng toPosition,
							  final boolean hideMarker) {
		Log.d("animate",toPosition.toString());
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = googleMap.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = TIME_INTERVAL;

		final Interpolator interpolator = new LinearInterpolator();
		long elapsed = SystemClock.uptimeMillis() - start;
		float t = interpolator.getInterpolation((float) elapsed
				/ duration);
		double lng = t * toPosition.longitude + (1 - t)
				* startLatLng.longitude;
		double lat = t * toPosition.latitude + (1 - t)
				* startLatLng.latitude;
		marker.setPosition(toPosition);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 15));
		//googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1500, null);
		Log.d("animate_revised",marker.getPosition().toString());
//		if (t < 1.0) {
//			// Post again 16ms later.
//		//	handler.postDelayed(this, 16);
//
//		} else {
//			if (hideMarker) {
//				marker.setVisible(false);
//			} else {
//				marker.setVisible(true);
//			}
//		}


//		handler.post(new Runnable() {
//			@Override
//			public void run() {
//				long elapsed = SystemClock.uptimeMillis() - start;
//				float t = interpolator.getInterpolation((float) elapsed
//						/ duration);
//				double lng = t * toPosition.longitude + (1 - t)
//						* startLatLng.longitude;
//				double lat = t * toPosition.latitude + (1 - t)
//						* startLatLng.latitude;
//				marker.setPosition(new LatLng(lat, lng));
//
//				if (t < 1.0) {
//					// Post again 16ms later.
//					handler.postDelayed(this, 16);
//				} else {
//					if (hideMarker) {
//						marker.setVisible(false);
//					} else {
//						marker.setVisible(true);
//					}
//				}
//			}
//		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
		flag = true;
		if (isOnline()) {
			//test = true;
			retrieveTransportDetails();
			fecthLocationDetailsFromServer();

		}else{
			Toast.makeText(getActivity(), "You're offline",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		try {
			SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
					.findFragmentById(R.id.map);
			if (fragment != null)
				getFragmentManager().beginTransaction().remove(fragment).commit();

		} catch (IllegalStateException e) {
			// handle this situation because you are necessary will get
			// an exception here :-(
		}
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}
	@Override
	public void onPause() {
		super.onPause();
		SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
				.findFragmentById(R.id.map);
		if (fragment != null)
			getFragmentManager().beginTransaction().remove(fragment).commit();
		if(t!=null) {
			t.cancel();
		}

	}

	public TransportFragment(int type) {
		super();
		this.type = type;
	}

	private void initialize() {
		//tvTransportCode = (TextView) view.findViewById(R.id.txt_transport_code);
		//tvRouteName = (TextView) view.findViewById(R.id.txt_route_name);
		//tvRouteDesc = (TextView) view.findViewById(R.id.txt_transport_desc);
		//tvPickPoint = (TextView) view.findViewById(R.id.txt_pick_point);
		//tvDropPoint = (TextView) view.findViewById(R.id.txt_drop_point);
		new_route_no = (TextView) view.findViewById(R.id.txt_rout_no) ;
		new_set_alert_point = (TextView) view.findViewById(R.id.btn_set_alert);
		mainLayout = (RelativeLayout) view.findViewById(R.id.main_layout);


		new_set_alert_point.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent int_set_alert = new Intent(mContext, AisAlertPointView.class);
				int_set_alert.putExtra("rout_id", transports.get(0).transport_code);
				int_set_alert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(int_set_alert);
			}
		});



		System.out.println( "googleMap" + googleMap);

		try {
			if (googleMap == null) {

				googleMap = ((SupportMapFragment) getChildFragmentManager()
						.findFragmentById(R.id.map)).getMap();

				//initializeMap();

			}
			else {
				//initializeMap();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeMap(LatLng start,LatLng end, LatLng bus) {
		//test = false;
		if (null != googleMap) {
			if(null != markerStart && null != markerEnd && null != markerBus){
				markerStart.remove();
				markerEnd.remove();
				markerBus.remove();
			}
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			System.out.println(stringLatitude + "Rajhrita" + stringLongitude);
			//latlongval = new LatLng(Double.parseDouble("22.591648"), Double.parseDouble("88.408829"));
			// Move the camera instantly to zoom of 20.
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bus, 20));

			// Zoom in, animating the camera.
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1500, null);

			/**
			 * Placing marker for Bus
			 */
			markerBus = googleMap.addMarker(new MarkerOptions().position(bus).title("Current Bus Position")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
			/**
			 * Placing marker for Start Point
			 */
			markerStart = googleMap.addMarker(new MarkerOptions().position(start).title("Start Position")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
			/**
			 * Placing marker for End Point
			 */
			markerEnd = googleMap.addMarker(new MarkerOptions().position(end).title("End Position")
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));


		}
	}

	private boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		initialize();

	}

	private void retrieveTransportDetails() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(
				new BasicNameValuePair("user_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		nameValuePairs.add(new BasicNameValuePair("user_type_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERTYPEID)));
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		if (type == 1) {
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		}
		new GetTransportAsyntask().execute(nameValuePairs);
	};

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@SuppressLint("NewApi")
	@Override
	public void onStop() {
		super.onStop();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}
		}
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	private class GetTransportAsyntask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
		String error = "";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle(getActivity().getTitle().toString());
			dialog.setMessage(getResources().getString(R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_transport);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);
			transports = new ArrayList<Transport>();

			transports.clear();
			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_transport);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {
						JSONArray array;
						try {
							array = json.getJSONArray("data");
						} catch (Exception e) {
							error = "Error in Data";
							return false;
						}
						for (int i = 0; i < array.length(); i++) {
							Transport transport = new Transport(array.getJSONObject(i));
							transports.add(transport);
						}
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (JSONException e) {

			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			if (result) {
				for (int i = 0; i < transports.size(); i++) {
					Transport transport = transports.get(i);
					//tvTransportCode.setText(transport.transport_code);
					//tvRouteName.setText(transport.transport_route_name);
					//tvRouteDesc.setText(transport.transport_desc);
					//tvPickPoint.setText(transport.transport_pick_point);
					//tvDropPoint.setText(transport.transport_drop_point);
					new_route_no.setText("Route No : "+transport.transport_route_name);
					// getFromServer();
				}

				new_route_no.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
						View promptView = layoutInflater.inflate(R.layout.prompt_aims_transport_view, null);
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
						alertDialogBuilder.setView(promptView);
						pr_tr_code = (TextView) promptView.findViewById(R.id.tr_code);
						pr_route_name = (TextView) promptView.findViewById(R.id.route_name);
						pr_pick_point = (TextView) promptView.findViewById(R.id.pick_point);
						pr_drop_point = (TextView) promptView.findViewById(R.id.drop_point);
						pr_description = (TextView) promptView.findViewById(R.id.description);

						pr_tr_code.setText(transports.get(0).transport_code);
						pr_route_name.setText(transports.get(0).transport_route_name);
						pr_pick_point.setText(transports.get(0).transport_pick_point);
						pr_drop_point.setText(transports.get(0).transport_drop_point);
						pr_description.setText(transports.get(0).transport_desc);

						final AlertDialog alertD = alertDialogBuilder.create();
						alertD.show();



					}
				});
			} else {
				if (error.length() > 0) {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(), error);
				} else {
					SchoolAppUtils.showDialog(getActivity(), getActivity().getTitle().toString(),
							getResources().getString(R.string.error));
				}
				mainLayout.setVisibility(View.GONE);
			}
		}

	}

//	private void fecthLocationDetailsFromServer() {
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//		nameValuePairs.add(new BasicNameValuePair("imei_no", "359547063152737"));
//		nameValuePairs.add(new BasicNameValuePair("organization_id","13"));
//
//		new RetrieveDeviceDetailsAsynTask().execute(nameValuePairs);
//	}

	private void fecthLocationDetailsFromServer() {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		if (type == 1) {
			nameValuePairs.add(new BasicNameValuePair("child_id",
					SchoolAppUtils.GetSharedParameter(getActivity(), Constants.CHILD_ID)));
		}else{
			nameValuePairs.add(
					new BasicNameValuePair("child_id", SchoolAppUtils.GetSharedParameter(getActivity(), Constants.USERID)));
		}
		nameValuePairs.add(new BasicNameValuePair("organization_id",
				SchoolAppUtils.GetSharedParameter(getActivity(), Constants.UORGANIZATIONID)));
		new RetrieveDeviceDetailsAsynTask().execute(nameValuePairs);
	}

//	protected class RetrieveDeviceDetailsAsynTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {
//
//		private String error = "";
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected Boolean doInBackground(List<NameValuePair>... params) {
//			List<NameValuePair> nameValuePairs = params[0];
//			// Log parameters:
//			Log.d("url extension: ", Urls.api_get_tracking_details);
//			String parameters = "";
//			for (NameValuePair nvp : nameValuePairs) {
//				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
//			}
//			Log.d("Parameters: ", parameters);
//
//			JsonParser jParser = new JsonParser();
//			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_get_tracking_details);
//			try {
//				if (json != null) {
//					if (json.getString("result").equalsIgnoreCase("1")) {
//						JSONArray array;
//						try {
//							array = json.getJSONArray("data");
//							for (int i = 0; i < array.length(); i++) {
//								JSONObject jsonObject = array.getJSONObject(i);
//								stringLatitude = jsonObject.optString("latitude").toString();
//								stringLongitude = jsonObject.optString("longitude").toString();
//							}
//
//						} catch (Exception e) {
//							error = json.getString("data");
//							return false;
//						}
//						return true;
//					} else {
//						try {
//							error = json.getString("data");
//						} catch (Exception e) {
//						}
//						return false;
//					}
//				} else {
//					error = getResources().getString(R.string.unknown_response);
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return false;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//
//			super.onPostExecute(result);
//			if (result) {
//				initializeMap();
//			} else {
//				if (error.length() > 0) {
//					Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
//				}
//			}
//		}
//
//	}

	protected class RetrieveDeviceDetailsAsynTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {

		private String error = "";
		private LatLng start,end;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_tracking_details);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_tracking_details);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						try {
							JSONObject obj = json.getJSONObject("data");
							JSONObject objStart = obj.getJSONObject("start");
							startLat = objStart.getString(Constants.LATITUDE);
							startLon = objStart.getString(Constants.LONGITUDE);
							start = new LatLng(Double.parseDouble(startLat), Double.parseDouble(startLon));

							System.out.println(startLat+"----start----"+startLon);

							JSONObject objEnd = obj.getJSONObject("end");
							endLat = objEnd.getString(Constants.LATITUDE);
							endLon = objEnd.getString(Constants.LONGITUDE);
							end = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLon));

							JSONObject objBus = obj.getJSONObject("bus");
							busLat = objBus.getString(Constants.LATITUDE);
							busLon = objBus.getString(Constants.LONGITUDE);
							bus = new LatLng(Double.parseDouble(busLat), Double.parseDouble(busLon));

//							for (int i = 0; i < array.length(); i++) {
//								JSONObject jsonObject = array.getJSONObject(i);
//								stringLatitude = jsonObject.optString("latitude").toString();
//								stringLongitude = jsonObject.optString("longitude").toString();
//							}

						} catch (Exception e) {
							error = json.getString("data");
							return false;
						}
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (result) {
				if(flag){
					initializeMap(start, end, bus);
					autoRefreshMap();
					flag = false;
				}
			} else {
				if (error.length() > 0) {
					Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
				}
			}
		}

	}

	protected class ReRetrieveDeviceDetailsAsynTask extends AsyncTask<List<NameValuePair>, Void, Boolean> {

		private String error = "";
		private LatLng start,end;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(List<NameValuePair>... params) {
			List<NameValuePair> nameValuePairs = params[0];
			// Log parameters:
			Log.d("url extension: ", Urls.api_tracking_details);
			String parameters = "";
			for (NameValuePair nvp : nameValuePairs) {
				parameters += nvp.getName() + "=" + nvp.getValue() + ",";
			}
			Log.d("Parameters: ", parameters);

			JsonParser jParser = new JsonParser();
			JSONObject json = jParser.getJSONFromUrlnew(nameValuePairs, Urls.api_tracking_details);
			try {
				if (json != null) {
					if (json.getString("result").equalsIgnoreCase("1")) {

						try {
							JSONObject obj = json.getJSONObject("data");
							JSONObject objStart = obj.getJSONObject("start");
							startLat = objStart.getString(Constants.LATITUDE);
							startLon = objStart.getString(Constants.LONGITUDE);
							start = new LatLng(Double.parseDouble(startLat), Double.parseDouble(startLon));

							System.out.println(startLat+"----start----"+startLon);

							JSONObject objEnd = obj.getJSONObject("end");
							endLat = objEnd.getString(Constants.LATITUDE);
							endLon = objEnd.getString(Constants.LONGITUDE);
							end = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLon));

							JSONObject objBus = obj.getJSONObject("bus");
							busLat = objBus.getString(Constants.LATITUDE);
							busLon = objBus.getString(Constants.LONGITUDE);
							bus = new LatLng(Double.parseDouble(busLat), Double.parseDouble(busLon));

//							for (int i = 0; i < array.length(); i++) {
//								JSONObject jsonObject = array.getJSONObject(i);
//								stringLatitude = jsonObject.optString("latitude").toString();
//								stringLongitude = jsonObject.optString("longitude").toString();
//							}

						} catch (Exception e) {
							error = json.getString("data");
							return false;
						}
						return true;
					} else {
						try {
							error = json.getString("data");
						} catch (Exception e) {
						}
						return false;
					}
				} else {
					error = getResources().getString(R.string.unknown_response);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			super.onPostExecute(result);
			if (result) {
				//autoRefreshMap();

			} else {
				if (error.length() > 0) {
					Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_LONG).show();
				}
			}
		}

	}

}
