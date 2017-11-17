package com.knwedu.college.utils;

// sunit:11/02/2015
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.knwedu.comschoolapp.R;

public class CollegeLoadImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {
	private ProgressDialog dialog;
	private Context context;
	private ImageView imageView;
	private String imageFileName;
	private String base_url;
	private boolean showProgress = false;

	public CollegeLoadImageAsyncTask(Context context, ImageView imageView, String base_url,
			String imageFileName, boolean showProgress) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.imageView = imageView;
		this.base_url = base_url;
		this.imageFileName = imageFileName;
		this.showProgress = showProgress;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (showProgress) {
			/*dialog = new ProgressDialog(context);
			dialog.setTitle(context.getResources().getString(
					R.string.loading_image));
			dialog.setMessage(context.getResources().getString(
					R.string.please_wait));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();*/
		}
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		Bitmap mIcon11 = null;
		if (imageFileName.length() > 0) {

				try {
					InputStream in = new java.net.URL(
							base_url + imageFileName).openStream();
					mIcon11 = BitmapFactory.decodeStream(in);
					
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
			}
		return mIcon11;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		/*if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}*/
//		imageView.setImageBitmap(null);
		if(result!=null){
//			imageView.setImageBitmap(result);
		} else {
			result = BitmapFactory.decodeResource(context.getResources(), R.drawable.damy);
		}
			Bitmap circleBitmap = Bitmap.createBitmap(result.getWidth(), result.getHeight(), Bitmap.Config.ARGB_8888);

			BitmapShader shader = new BitmapShader (result,  TileMode.CLAMP, TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(shader);

			Canvas c = new Canvas(circleBitmap);
			c.drawCircle((float) (result.getWidth()/2.0), (float) (result.getHeight()/2.0), (float) (result.getHeight()/2.7), paint);

			
//			BitmapDrawable ob = new BitmapDrawable(context.getResources(), circleBitmap);
			imageView.setImageBitmap(circleBitmap);
			
		
	}
}
