package com.knwedu.ourschool.db;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.knwedu.college.db.CollegeDBAdapter;

//@ReportsCrashes(formUri = "http://61.12.80.165/crashlog/test.php", deleteOldUnsentReportsOnApplicationStart = true)
public class SchoolApplication extends Application {
	private DatabaseAdapter mDatabase;
	private CollegeDBAdapter mCollegeDatabase;

	@Override
	public void onCreate() {
		super.onCreate();
		mDatabase = new DatabaseAdapter(this);
		mCollegeDatabase = new CollegeDBAdapter(this);
		MultiDex.install(this);

//		ACRA.init(this);
	}

	public DatabaseAdapter getDatabase() {
		return mDatabase;
	}

	public CollegeDBAdapter getCollegeDatabase() {
		return mCollegeDatabase;
	}

	
	
}
