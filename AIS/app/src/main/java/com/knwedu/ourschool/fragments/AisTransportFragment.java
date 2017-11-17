package com.knwedu.ourschool.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.knwedu.ourschool.AisBusListActivity;
import com.knwedu.ourschool.ParentMainActivity;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.JsonParser;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AisTransportFragment extends Fragment {

    ProgressBar progress;
    TextView subscribe, status,txt_rout_no,btn_set_alert,label;
    TextView pr_route_no, pr_begin_time, pr_em_ph, pr_driver, pr_desc;
    String rout_no, begin_time, em_con_no, driver_name,vstatus, description;
    int api_status;
    RelativeLayout map_layout;
    private LatLng latlongval;
    private GoogleMap googleMap;
    private String stringLatitude, stringLongitude, startLat, startLon, busLat, busLon, endLat,endLon;
    private Marker markerStart,markerEnd,markerBus ;
    private static final int TIME_INTERVAL = 15000;
    private boolean flag = false;
    private LatLng bus;
    private boolean test = false;
    CountDownTimer t;
    Context mContext;


    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ais_transport_view, container, false);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        initialize();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    private void autoRefreshMap() {

        t = new CountDownTimer(Long.MAX_VALUE, TIME_INTERVAL) {

            // This is called every interval. (Every 30 seconds in this example)
            public void onTick(long millisUntilFinished) {
                System.out.println("autoRefreshMap");
                fecthLocationDetailsFromServer("1");
                System.out.println(busLat + "----bus----" + busLon);
                /**
                 * Animate bus marker in every 30 seconds
                 */
                //animateMarker(markerBus, bus, false);
            }

            public void onFinish() {
                Log.d("test", "Timer last tick");
                Toast.makeText(mContext, "Auto refresh is not responding, please try with better connection",
                        Toast.LENGTH_LONG).show();
                start();
            }
        }.start();


    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {
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



    private void initialize() {
        subscribe = (TextView) view.findViewById(R.id.btn_subscribe);
        status = (TextView) view.findViewById(R.id.status);
        txt_rout_no = (TextView) view.findViewById(R.id.txt_rout_no);
        btn_set_alert = (TextView) view.findViewById(R.id.btn_set_alert);
        map_layout = (RelativeLayout) view.findViewById(R.id.map_layout);
        label = (TextView)view.findViewById(R.id.label);
        subscribe.setVisibility(View.GONE);
        status.setVisibility(View.GONE);
        label.setVisibility(View.GONE);
        Animation anim= new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(300); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        btn_set_alert.startAnimation(anim);
        Log.d("ddddddd","aistransport");

        rout_no = "";
        progress = (ProgressBar) view.findViewById(R.id.progress);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("user_type_id",
                SchoolAppUtils.GetSharedParameter(mContext,
                        Constants.USERTYPEID)));
        nameValuePairs.add(new BasicNameValuePair("user_id", SchoolAppUtils
                .GetSharedParameter(mContext, Constants.USERID)));
        nameValuePairs.add(new BasicNameValuePair("organization_id",
                SchoolAppUtils.GetSharedParameter(mContext,
                        Constants.UORGANIZATIONID)));
        nameValuePairs.add(new BasicNameValuePair("child_id", SchoolAppUtils
                .GetSharedParameter(mContext, Constants.CHILD_ID)));
        vstatus = "0";
        new GetVehicleStatus().execute(nameValuePairs);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bus_list = new Intent(mContext, AisBusListActivity.class);
                bus_list.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(bus_list);
            }
        });

        txt_rout_no.setPaintFlags(txt_rout_no.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        txt_rout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.prompt_transport, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(promptView);

                pr_route_no = (TextView) promptView.findViewById(R.id.route_no);
                pr_begin_time =(TextView) promptView.findViewById(R.id.begin_time);
                pr_em_ph =(TextView) promptView.findViewById(R.id.ph_no);
                pr_driver =(TextView) promptView.findViewById(R.id.driver_name);
                pr_desc =(TextView) promptView.findViewById(R.id.description);
                pr_route_no.setText(rout_no);
                pr_begin_time.setText(begin_time);
                pr_em_ph.setText(em_con_no);
                pr_driver.setText(driver_name);
                pr_desc.setText(description);


                final AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();

            }
        });


        btn_set_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int_set_alert = new Intent(mContext, AisAlertPointView.class);
                int_set_alert.putExtra("rout_id", rout_no);
                int_set_alert.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(int_set_alert);

            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                map_layout.setVisibility(View.VISIBLE);
                txt_rout_no.setText("Route No : "+rout_no);
                System.out.println( "googleMap>>>" + googleMap);

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

                fecthLocationDetailsFromServer("0");
            }
        });
    }
    private void fecthLocationDetailsFromServer(String callType) {
        List<NameValuePair> nvp = new ArrayList<>();
        nvp.add(new BasicNameValuePair("route_number",rout_no));
        new RetrieveDeviceDetailsAsynTask(callType).execute(nvp);
    }


    private class GetVehicleStatus extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];
            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            mContext, Constants.COMMON_URL)
                            + Urls.api_ais_vehicle_status);


            try {

                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {
                        JSONArray array;
                        array = json.getJSONArray("data");
                        rout_no =array.getJSONObject(0).getString("route_number");
                        begin_time = array.getJSONObject(0).getString("begin_time");
                        em_con_no = array.getJSONObject(0).getString("emergency_contact_number");
                        driver_name =array.getJSONObject(0).getString("operator_name");
                        description =array.getJSONObject(0).getString("description");
                        vstatus =array.getJSONObject(0).getString("status");
                        api_status= 1;
                        return true;


                    }else {
                        try {
                            error = json.getString("data");
                            rout_no = "NA";
                            vstatus = "";
                            api_status = 0;
                        } catch (Exception e) {
                        }
                        return true;
                    }
                }else {
                    error = getResources().getString(R.string.unknown_response);
                }

            } catch (JSONException e) {

            }




            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);

            if(true){
                if(api_status == 1){
                    if(vstatus.equals("1")) {
                        label.setVisibility(View.GONE);
                        subscribe.setVisibility(View.GONE);

                        status.setVisibility(View.VISIBLE);
                    }else{
                        status.setVisibility(View.GONE);
                        subscribe.setVisibility(View.GONE);
                        label.setVisibility(View.VISIBLE);
                        label.setText("Vehicle is not running");

                    }


                }else{
                    status.setVisibility(View.GONE);
                    label.setVisibility(View.VISIBLE);
                    String appType = SchoolAppUtils.GetSharedParameter(mContext,
                            Constants.APP_TYPE);
                    if(appType.equals(Constants.APP_TYPE_COMMON_STANDARD)) {
                        subscribe.setVisibility(View.VISIBLE);
                        label.setText("Kindly avail transport tracking by clicking 'Subscribe'");
                    }else{
                        label.setText("Kindly contact your school admin.");
                    }
                }
            }
        }



    }




    private class RetrieveDeviceDetailsAsynTask extends
            AsyncTask<List<NameValuePair>, Void, Boolean> {
        String error = "";
        String callType;

        public RetrieveDeviceDetailsAsynTask(String ct){
            this.callType = ct;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(List<NameValuePair>... params) {

            List<NameValuePair> namevaluepair = params[0];

            JsonParser jParser = new JsonParser();
            JSONObject json = jParser.getJSONFromUrlfrist(
                    namevaluepair,
                    SchoolAppUtils.GetSharedParameter(
                            mContext, Constants.COMMON_URL)
                            + Urls.api_ais_get_bus_location);
            try {
                JSONArray array;
                array = json.getJSONArray("data");
                if (json != null) {
                    if (json.getString("result").equalsIgnoreCase("1")) {

                        busLat = array.getJSONObject(0).getString("latitude");
                        busLon = array.getJSONObject(0).getString("longitude");
                        bus = new LatLng(Double.parseDouble(busLat), Double.parseDouble(busLon));
                        return true;
                    }else {
                        try {
                            error = json.getString("data");
                        } catch (Exception e) {
                        }
                        return false;
                    }
                }else {
                    error = getResources().getString(R.string.unknown_response);
                }

            } catch (JSONException e) {

            }

            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {

            super.onPostExecute(result);

            if (result) {
                if(callType.equals("0")) {
                    initializeMap(bus);
                    autoRefreshMap();
                    flag = false;
                }else{
                    animateMarker(markerBus, bus, false);
                }

            } else {
                if (error.length() > 0) {
                    Toast.makeText(mContext, "No Data Found", Toast.LENGTH_LONG).show();
                }
            }


        }



    }


    private void initializeMap( LatLng bus) {
        //test = false;
        if (null != googleMap) {
            if( null != markerBus){
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



        }
    }

}
