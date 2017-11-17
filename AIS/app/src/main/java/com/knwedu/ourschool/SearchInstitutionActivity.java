//package com.knwedu.ourschool;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.app.SearchManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.MatrixCursor;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.BaseColumns;
//import android.support.v4.widget.CursorAdapter;
//import android.support.v4.widget.SimpleCursorAdapter;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//
//import com.knwedu.comschoolapp.R;
//import com.knwedu.ourschool.adapter.OrganizationAdapter;
//import com.knwedu.ourschool.db.DatabaseAdapter;
//import com.knwedu.ourschool.db.SchoolApplication;
//import com.knwedu.ourschool.utils.JsonParser;
//import com.knwedu.ourschool.utils.OrgSet;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.knwedu.ourschool.utils.Urls;
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import io.fabric.sdk.android.services.concurrency.AsyncTask;
//
//
//public class SearchInstitutionActivity extends AppCompatActivity  {
//
//    private SearchView searchView =null;
//    //private String[] strArrData = {"No Suggestions"};
//    private ListView searchResults;
//    private MenuItem mSearchAction;
//    public static final int CONNECTION_TIMEOUT = 10000;
//    public static final int READ_TIMEOUT = 15000;
//   // private RecyclerView mRVFish;
//    //private AdapterFish mAdapter;
//    private SimpleCursorAdapter myAdapter;
//    private DatabaseAdapter mDatabase;
//    private ImageView image;
//    SharedPreferences prefs=null;
//
//    View myFragmentView;
//   // SearchView search;
//    ImageButton buttonBarcode;
//    ImageButton buttonAudio;
//    Typeface type;
//   // ListView searchResults;
//    String found = "N";
//
//    ArrayList<OrgSet> orgSets = new ArrayList<OrgSet>();
//    //Based on the search string, only filtered products will be moved here from productResults
//    ArrayList<OrgSet> filteredOrgsetResults = new ArrayList<OrgSet>();
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState)
//    {
//        //get the context of the HomeScreen Activity
//        // final SearchInstitutionActivity activity = (SearchInstitutionActivity) getActivity();
//
//        //define a typeface for formatting text fields and listview.
//
//        //type= Typeface.createFromAsset(activity.getAssets(),"fonts/book.TTF");
//        myFragmentView = inflater.inflate(R.layout.activity_search_institution, container, false);
//
//        searchView = (SearchView) myFragmentView.findViewById(R.id.searchView);
//        searchView.setQueryHint("Start typing to search...");
//
//        searchResults = (ListView) myFragmentView.findViewById(R.id.listview_search);
//        buttonBarcode = (ImageButton) myFragmentView.findViewById(R.id.imageButton2);
//        buttonAudio = (ImageButton) myFragmentView.findViewById(R.id.imageButton1);
//
//
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//
//                //Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
//        {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//
//                //Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
//        {
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // TODO Auto-generated method stub
//
//                return false;
//            }
//        @Override
//        public boolean onQueryTextChange(String str) {
//
//        if (str.length() > 3)
//        {
//
//            searchResults.setVisibility(myFragmentView.VISIBLE);
//            myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(str);
//        }
//        else
//        {
//
//            searchResults.setVisibility(myFragmentView.INVISIBLE);
//        }
//
//        return false;
//    }
//
//    });
//    return myFragmentView;
//    }
//
//    public void filterProductArray(String newText)
//    {
//
//        String pName;
//
//        filteredOrgsetResults.clear();
//        for (int i = 0; i < orgSets.size(); i++)
//        {
//            pName = orgSets.get(i).getApp_type().toLowerCase();
//            if ( pName.contains(newText.toLowerCase()) ||
//                    orgSets.get(i).setApp_type().contains(newText))
//            {
//                filteredOrgsetResults.add(orgSets.get(i));
//
//            }
//        }
//
//    }
//
//    //in this myAsyncTask, we are fetching data from server for the search string entered by user.
//    class myAsyncTask extends AsyncTask<String, Void, String>
//    {
//        JsonParser jParser;
//        JSONArray productList;
//        String url=new String();
//        String textSearch;
//        ProgressDialog pd;
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            productList=new JSONArray();
//            jParser = new JsonParser();
//            pd= new ProgressDialog(SearchInstitutionActivity.this);
//            pd.setCancelable(false);
//            pd.setMessage("Searching...");
//            pd.getWindow().setGravity(Gravity.CENTER);
//            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... sText) {
//
//            url="http://lawgo.in/lawgo/products/user/1/search/"+sText[0];
//            String returnResult = getProductList(url);
//            this.textSearch = sText[0];
//            return returnResult;
//
//        }
//
//        public String getProductList(String url)
//        {
//
//            Product tempProduct = new Product();
//            String matchFound = "N";
//            //productResults is an arraylist with all product details for the search criteria
//            //productResults.clear();
//
//
//            try {
//
//
//                JSONObject json = jParser.getJSONFromUrl(url);
//
//                productList = json.getJSONArray("ProductList");
//
//                //parse date for dateList
//                for(int i=0;i<productList.length();i++)
//                {
//                    tempProduct = new Product();
//
//                    JSONObject obj=productList.getJSONObject(i);
//
//                    tempProduct.setProductCode(obj.getString("ProductCode"));
//                    tempProduct.setProductName(obj.getString("ProductName"));
//                    tempProduct.setProductGrammage(obj.getString("ProductGrammage"));
//                    tempProduct.setProductBarcode(obj.getString("ProductBarcode"));
//                    tempProduct.setProductDivision(obj.getString("ProductCatCode"));
//                    tempProduct.setProductDepartment(obj.getString("ProductSubCode"));
//                    tempProduct.setProductMRP(obj.getString("ProductMRP"));
//                    tempProduct.setProductBBPrice(obj.getString("ProductBBPrice"));
//
//                    //check if this product is already there in productResults, if yes, then don't add it again.
//                    matchFound = "N";
//
//                    for (int j=0; j < productResults.size();j++)
//                    {
//
//                        if (productResults.get(j).getProductCode().equals(tempProduct.getProductCode()))
//                        {
//                            matchFound = "Y";
//                        }
//                    }
//
//                    if (matchFound == "N")
//                    {
//                        productResults.add(tempProduct);
//                    }
//
//                }
//
//                return ("OK");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ("Exception Caught");
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            super.onPostExecute(result);
//
//            if(result.equalsIgnoreCase("Exception Caught"))
//            {
//                Toast.makeText(SearchInstitutionActivity.this, "Unable to connect to server,please try later", Toast.LENGTH_LONG).show();
//
//                pd.dismiss();
//            }
//            else
//            {
//
//
//                //calling this method to filter the search results from productResults and move them to
//                //filteredProductResults
//                filterProductArray(textSearch);
//                searchResults.setAdapter(new SearchResultsAdapter(SearchInstitutionActivity.this,filteredOrgsetResults));
//                pd.dismiss();
//            }
//        }
//
//    }
//}
//
//class SearchResultsAdapter extends BaseAdapter
//{
//    private LayoutInflater layoutInflater;
//
//    private ArrayList<OrgSet> productDetails=new ArrayList<OrgSet>();
//    int count;
//    Typeface type;
//    Context context;
//
//    //constructor method
//    public SearchResultsAdapter(Context context, ArrayList<OrgSet> product_details) {
//
//        layoutInflater = LayoutInflater.from(context);
//
//        this.productDetails=product_details;
//        this.count= product_details.size();
//        this.context = context;
//        type= Typeface.createFromAsset(context.getAssets(),"fonts/book.TTF");
//
//    }
//
//    @Override
//    public int getCount() {
//        return count;
//    }
//
//    @Override
//    public Object getItem(int arg0) {
//        return productDetails.get(arg0);
//    }
//
//    @Override
//    public long getItemId(int arg0) {
//        return arg0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//
//        ViewHolder holder;
//        Product tempProduct = productDetails.get(position);
//
//        if (convertView == null)
//        {
//            convertView = layoutInflater.inflate(R.layout.listtwo_searchresults, null);
//            holder = new ViewHolder();
//            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
//            holder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
//            holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_mrpvalue);
//            holder.product_bb = (TextView) convertView.findViewById(R.id.product_bb);
//            holder.product_bbvalue = (TextView) convertView.findViewById(R.id.product_bbvalue);
//            holder.addToCart = (Button) convertView.findViewById(R.id.add_cart);
//
//            convertView.setTag(holder);
//        }
//        else
//        {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//
//        holder.product_name.setText(tempProduct.getProductName());
//        holder.product_name.setTypeface(type);
//
//        holder.product_mrp.setTypeface(type);
//
//        holder.product_mrpvalue.setText(tempProduct.getProductMRP());
//        holder.product_mrpvalue.setTypeface(type);
//
//        holder.product_bb.setTypeface(type);
//
//        holder.product_bbvalue.setText(tempProduct.getProductBBPrice());
//        holder.product_bbvalue.setTypeface(type);
//
//        return convertView;
//    }
//
//    static class ViewHolder
//    {
//        TextView product_name;
//        TextView product_mrp;
//        TextView product_mrpvalue;
//        TextView product_bb;
//        TextView product_bbvalue;
//        TextView product_savings;
//        TextView product_savingsvalue;
//        TextView qty;
//        TextView product_value;
//        Button addToCart;
//
//    }
//
//}
//
//
//
//}
