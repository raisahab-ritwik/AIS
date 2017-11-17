package com.knwedu.ourschool;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.knwedu.comschoolapp.R;
import com.knwedu.ourschool.utils.Constants;
import com.knwedu.ourschool.utils.OrgSet;
import com.knwedu.ourschool.utils.SchoolAppUtils;
import com.knwedu.ourschool.utils.Urls;

public class SpinnerDialog extends Dialog {
	private List<OrgSet> orgItems;
    private Context mContext;
    private Spinner mSpinner;
    private ArrayList<String> mList;
   public interface DialogListener {
        public void ready(int n);
        public void cancelled();
    }

    private DialogListener mReadyListener;
    
    public SpinnerDialog(Context context, List<OrgSet> movieItems, DialogListener readyListener) {
        super(context);
        mReadyListener = readyListener;
        mContext = context;
        this.orgItems = movieItems;
        this.setTitle("Please Select Organization");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.spinner);
        mSpinner = (Spinner) findViewById (R.id.dialog_spinner);
       
        mList= new ArrayList<String>();
       
        for(int i=0;i<orgItems.size();i++)
        {
        	 OrgSet m = orgItems.get(i);
             mList.add(m.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String> (mContext, android.R.layout.simple_spinner_dropdown_item, mList);
        mSpinner.setAdapter(adapter);

        Button buttonOK = (Button) findViewById(R.id.dialogOK);
        Button buttonCancel = (Button) findViewById(R.id.dialogCancel);
        buttonOK.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                int n = mSpinner.getSelectedItemPosition();
                mReadyListener.ready(n);
                OrgSet m = orgItems.get(n);
                SchoolAppUtils.SetSharedParameter(mContext, Constants.UORGANIZATIONID, m.getorgid());
                SchoolAppUtils.SetSharedParameter(mContext, Constants.PUSH_TITLE, m.getTitle());
                SchoolAppUtils.SetSharedParameter(mContext, Constants.COMMON_URL, m.getbaseurl());
                
                Log.d("URL.....", m.getorgid() + "|" + m.getTitle() + "|" + m.getbaseurl());
                new Urls(SchoolAppUtils.GetSharedParameter(mContext, Constants.COMMON_URL));
        		
                SpinnerDialog.this.dismiss();
                Constants.IMAGE_NAME=m.getThumbnailUrl();
               /* mContext.startActivity(new Intent(mContext,
           		SigninActivity.class));*/ 
                
                
            }
        });
        buttonCancel.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v) {
                mReadyListener.cancelled();
                SpinnerDialog.this.dismiss();
                return;
            }
        });
    }
    
}