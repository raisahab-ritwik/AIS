package com.knwedu.college.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.knwedu.college.vo.CollegeBlogBean;
import com.knwedu.college.vo.CollegeBlogCommentsBean;
import com.knwedu.ourschool.utils.ServiceHandler;

public class CollegeJsonParser {

	// constructor
	public CollegeJsonParser() {

	}
	public JSONObject getJSONFromUrlnew(List<NameValuePair> namevaluepair,
			String urladd) {

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		String url = CollegeUrls.base_url;
		if(url == null){
			return jObj;
		}
		// Making HTTP request
		String HUrl = url + urladd;
		ServiceHandler sh = new ServiceHandler();
		json = sh.makeServiceCall(HUrl, ServiceHandler.POST,namevaluepair);


		try {
			jObj = null;
			if(json!=null)
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;

	}
	

	public JSONObject getJSONFromUrl(String url) {


		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		ServiceHandler sh = new ServiceHandler();
		json = sh.makeServiceCall(url, ServiceHandler.POST,null);

		try {
			jObj = null;
			if(json!=null)
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}



		return jObj;

	}

	/*public JSONObject getJSONFromUrl(HttpPost httpPost) {

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 500000);
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
					500000);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// sb.append(line + "n");
				sb.append(line);
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}*/

	public ArrayList<Object> getBlogData(String _jSONString) {
		JSONObject jSONObject;
		ArrayList<Object> blogList = new ArrayList<Object>();
		blogList.clear();

		try {
			jSONObject = new JSONObject(_jSONString);
			String result = jSONObject.getString("result");

			if (result.equalsIgnoreCase("1"))
				;
			{
				JSONArray jArrayData = null;
				try {
					jArrayData = jSONObject.getJSONArray("data");
				} catch (Exception ex) {
					jArrayData = new JSONArray();
				}
				for (int i = 0; i < jArrayData.length(); i++) {
					JSONObject jBlog = null;
					try {
						jBlog = jArrayData.getJSONObject(i);
					} catch (Exception ex) {
					}

					CollegeBlogBean blogBean = new CollegeBlogBean();
					String blog_id = "";
					String title = "";
					String description = "";
					String createdBy = "";
					String file_name = "";
					String comment_status = "";
					String createdAt = "";
					String no_comments = "";
					String image = "";

					ArrayList<CollegeBlogCommentsBean> commentList = new ArrayList<CollegeBlogCommentsBean>();
					commentList.clear();

					try {
						blog_id = jBlog.getString("id");
					} catch (Exception ex) {
					}
					try {
						file_name = jBlog.getString("file_name");
					} catch (Exception ex) {
					}
					try {
						comment_status = jBlog.getString("comment_status");
					} catch (Exception ex) {
					}
					try {
						title = jBlog.getString("title");
					} catch (Exception ex) {
					}
					try {
						description = jBlog.getString("description");
					} catch (Exception ex) {
					}
					try {
						createdBy = jBlog.getString("name");
					} catch (Exception ex) {
					}
					try {
						createdAt = jBlog.getString("created_at");
					} catch (Exception ex) {
					}
					try {
						no_comments = jBlog.getString("total");
					} catch (Exception ex) {
					}
					try {
						image = jBlog.getString("image");
					} catch (Exception ex) {
					}

					blogBean.setBlogId(blog_id);
					blogBean.setCreatedAt(createdAt);
					blogBean.setCreatedBy(createdBy);
					blogBean.setDescription(description);
					blogBean.setcommentstatus(comment_status);
					blogBean.setTitle(title);
					blogBean.setfilename(file_name);
					blogBean.setNo_comments(no_comments);
					blogBean.setImage(image);

					JSONArray jArrayComments = null;
					try {
						jArrayComments = jBlog.getJSONArray("comment");
					} catch (Exception ex) {
						jArrayComments = new JSONArray();
					}
					for (int j = 0; j < jArrayComments.length(); j++) {
						JSONObject jBlogComment = null;
						try {
							jBlogComment = jArrayComments.getJSONObject(j);
						} catch (Exception ex) {
						}

						CollegeBlogCommentsBean blogComments = new CollegeBlogCommentsBean();
						String comment_id = "";
						String name = "";
						String commentCreatedAt = "";
						String comment = "";
						String is_delete = "" ;
						try {
							comment_id = jBlogComment.getString("comment_id");

						} catch (Exception ex) {
						}
						try {
							name = jBlogComment.getString("name");
						} catch (Exception ex) {
						}
						try {
							is_delete = jBlogComment.getString("is_delete");
						} catch (Exception ex) {
						}
						try {
							comment = jBlogComment.getString("comment");
						} catch (Exception ex) {
						}
						try {
							image = jBlogComment.getString("image");
						} catch (Exception ex) {
						}
						blogComments.setComment_id(comment_id);
						blogComments.setComment(comment);
						blogComments.setCreatedAt(commentCreatedAt);
						blogComments.setName(name);
						blogComments.setIs_delete(is_delete);
						commentList.add(blogComments);
					}
					blogBean.setCommentList(commentList);
					blogList.add(blogBean);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return blogList;

	}

}
