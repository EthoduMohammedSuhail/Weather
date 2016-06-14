package com.example.weather;

import android.support.v7.app.ActionBarActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weather.R;

public class MainActivity extends ActionBarActivity {
	TextView resultTextView ;
	EditText cityNameEditText;
	
	public class DownloadTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... urls) 
		{
             String result = "";
             URL url;
             HttpURLConnection urlConnection= null;
            
        
             try 
             {
				url = new URL (urls[0]);
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in =urlConnection.getInputStream();
				InputStreamReader reader = new InputStreamReader(in);
				int data = reader.read();
				while(data!=-1)
				{
					char current =(char) data;
					result+=  current;
					data = reader.read();
					
				}

				return result;
				
			} 
             
             catch (Exception e){
            	 e.printStackTrace();
            	 resultTextView.setText("url exception");
             }
             return "Nothing found";
            
            
		}
		public void onPostExecute(String result)
		{
			 String message = "";
			super.onPostExecute(result);
			try
			{
				JSONObject jsonObject = new JSONObject(result);
				String wheatherInfo = jsonObject.getString("weather");
				JSONArray arr = new JSONArray(wheatherInfo);
				for(int i = 0;i<arr.length();i++)
				{
					JSONObject jsonPart = arr.getJSONObject(i);
					String main = "";
					String description = "";
					 main= jsonPart.getString("main");
					 description = jsonPart.getString("description");
					if(main != null && description!=null)
					{
						
			message += main + ":" + description + "\r\n";
			
					}
					if (message != null)
					{
						resultTextView.setText(message);
						
					}
					else
					{
						resultTextView.setText("Nothing found");
						
					}
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			         mgr.hideSoftInputFromInputMethod(cityNameEditText.getWindowToken(), 0);
				
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				resultTextView.setText("json exception");
				
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void findWeather(View view)
	
	{
		 
		String cityname = "";
		cityname = cityNameEditText.getText().toString();
		String encodedCityname = "";

		{
		
		try {
			encodedCityname = URLEncoder.encode(cityname, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			DownloadTask task = new DownloadTask();
			
			try
			{
				task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityname + "&APPID=91852c286d47739d85183be83d3427e5");
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			}
		
		}
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 cityNameEditText = (EditText) findViewById(R.id.cityNameEditText);
		 resultTextView = (TextView) findViewById(R.id.resultTextView);
			
		Toast.makeText(getApplicationContext(), "Developer: Mohammed suhail", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
