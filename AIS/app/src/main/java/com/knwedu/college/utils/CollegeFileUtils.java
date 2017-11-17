package com.knwedu.college.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

public class CollegeFileUtils {
	public static String getPath(Context context, Uri uri)
			throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection,
						null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDriveFileAbsolutePath(Activity context, Uri uri)
			throws IOException {
		if (uri == null)
			return null;
		ContentResolver resolver = context.getContentResolver();
		FileInputStream input = null;
		FileOutputStream output = null;

		String filename = "";
		final String[] projection = { MediaStore.MediaColumns.DISPLAY_NAME };
		ContentResolver cr = context.getApplicationContext()
				.getContentResolver();
		Cursor metaCursor = cr.query(uri, projection, null, null, null);
		if (metaCursor != null) {
			try {
				if (metaCursor.moveToFirst()) {
					filename = metaCursor.getString(0);
				}
			} finally {
				metaCursor.close();
			}
		}

		String outputFilePath = new File(context.getCacheDir(), filename)
				.getAbsolutePath();
		try {
			ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "r");
			FileDescriptor fd = pfd.getFileDescriptor();
			input = new FileInputStream(fd);
			output = new FileOutputStream(outputFilePath);
			int read = 0;
			byte[] bytes = new byte[4096];
			while ((read = input.read(bytes)) != -1) {
				output.write(bytes, 0, read);
			}
			return new File(outputFilePath).getAbsolutePath();
		} catch (IOException ignored) {
			// nothing we can do
		} finally {
			input.close();
			output.close();
		}
		return "";
	}
}
