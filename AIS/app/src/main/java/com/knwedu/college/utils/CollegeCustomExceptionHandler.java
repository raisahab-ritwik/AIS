package com.knwedu.college.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.util.Date;

import android.os.Environment;

public class CollegeCustomExceptionHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler defaultUEH;

    /* 
     * if any of the parameters is null, the respective functionality 
     * will not be used 
     */
    public CollegeCustomExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
	public void uncaughtException(Thread t, Throwable e) 
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        
        DateFormat[] formats = new DateFormat[] 
         {        		  
        	   DateFormat.getDateTimeInstance(),        		   
         };
        String time = "";
    	 for (DateFormat df : formats) 
    	 {
    		   time += df.format(new Date(System.currentTimeMillis()));
    	 }
        	 
        stacktrace += " \nCrash Happen: At :"+time;

    	try {
			writeFile(stacktrace);
		} catch (IOException e1) {				
			e1.printStackTrace();
		}       
        defaultUEH.uncaughtException(t, e);
    }

    private void writeFile(String data) throws IOException
    {
    	File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/crashReport_AlWahdaSchoolApp.txt");
    	if(!myFile.exists())
        myFile.createNewFile();
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = 
                                new OutputStreamWriter(fOut);
        myOutWriter.append(data);
        myOutWriter.close();
        fOut.close();
    }
}

