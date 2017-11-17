package com.knwedu.ourschool.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aphidmobile.utils.AphidLog;
import com.aphidmobile.utils.IO;
import com.aphidmobile.utils.UI;
import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.SchoolAppUtils;

public class SlideAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	private int repeatCount = 1;

	private List<String> slidesData;

	public SlideAdapter(Context context) {
		inflater = LayoutInflater.from(context);

		slidesData = new ArrayList<String>();
		slidesData.clear();
		int total_files = SchoolAppUtils.GetSharedIntParameter(context,
				Constants.TUTORIAL_FILE_COUNT);
		
		
		for (int i = 1; i <= total_files; i++) {
			slidesData.add("tutorial_" + i + ".png");
		}

	}

	@Override
	public int getCount() {
		return slidesData.size() * repeatCount;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout = convertView;
		if (convertView == null) {
			layout = inflater.inflate(R.layout.complex1, null);
			AphidLog.d("created new view from adapter: %d", position);
		}

		final String data = slidesData.get(position % slidesData.size());
		File f = new File(inflater.getContext().getFilesDir(), data);

		Bitmap b = null;
		try {
			if (f.exists()) {
				b = BitmapFactory.decodeStream(new FileInputStream(f));
			} else {

				b = IO.readBitmap(inflater.getContext().getAssets(),
						"no_preview.png");
			}

			UI.<ImageView> findViewById(layout, R.id.photo).setImageBitmap(b);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return layout;
	}

	public void removeData(int index) {
		if (slidesData.size() > 1) {
			slidesData.remove(index);
		}
	}

	public void addData(String data) {
		slidesData.add(data);
	}
}
