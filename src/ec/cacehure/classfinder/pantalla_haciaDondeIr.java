package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class pantalla_haciaDondeIr extends ListActivity{

	Button btn_salir;
	ListView all_courses;
	//LOCALIZACION
	WifiManager allwifi;
	WifiScanReceiver wifiReciever;
	//25 de Mayo 2014
	private static final String url_localizacion = "http://192.168.0.6/WebService/localizacion.php";
	private static final String TAG_VALUE0 = "value0";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	
	//Domingo 4 de Mayo 2014
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ArrayList<HashMap<String, String>> courseList;
	private static String url_all_courses = "http://192.168.0.6/WebService/get_courses.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_COURSES = "courses";
	private static final String TAG_CODE = "codigo";
	private static final String TAG_DESCRIPCION = "descripcion";
	JSONArray courses = null;
	
	//Julio 4 de 2014
	Vector p = new Vector();
	private static String url_localizacionOne = "http://192.168.0.6/WebService/localizacion_one.php";
	private static String url_two = "http://192.168.0.6/WebService/dos.php";
	private static final String TAG_VALUE = "value";
	//End
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togo);
		
		//4 de Mayo de 2014
		//Muestra los cursos de la FIEC
		courseList = new ArrayList<HashMap<String,String>>();
		new LoadAllCourses().execute();
		all_courses = getListView();
		//END
		
		allwifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiReciever = new WifiScanReceiver();
		allwifi.startScan();
		
		//cambios 25 de mayo de 2014
		//new LoadWifiScan().execute();
		//end
						
		btn_salir = (Button)findViewById(R.id.exit);
		btn_salir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		unregisterReceiver(wifiReciever);
		super.onPause();
	}
		@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}
    
    //Clase WifiScanReceiver
    class WifiScanReceiver extends BroadcastReceiver{
    	
		@Override
		public void onReceive(Context c, Intent intent) {
			// TODO Auto-generated method stub
			//Estan todas las wifi detectadas
			List<ScanResult> wifiScanList = allwifi.getScanResults();
			final int n = wifiScanList.size(); //El tamaño de la lista
			
			for(int i=0;i<n;i++){
				if( (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-WIFI") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Claro_MOLINA0000029162") || (wifiScanList.get(i).SSID).equalsIgnoreCase("AutoAlvarez") ){
					p.add(wifiScanList.get(i));
				}
			}
			Log.v("=============> Android BSSID", "AP DISPONIBLES VECTOR: "+ p.toString());
			
			int tam_vetor = p.size();
			Log.v("=============> Android BSSID", "TAMAÑO VECTOR: "+ tam_vetor);
			
			if(tam_vetor == 2){
				//Se pasa los TRES primeros valores al servidor
				LoadWifiScan tres = new LoadWifiScan();
				//tres.execute( (p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()) );
				tres.execute( (p.elementAt(0).toString()), (p.elementAt(1).toString()) );
			}else{
				//Se pasa solo el PRIMER valor al servidor
				LoadOneWifi one = new LoadOneWifi();
				one.execute((p.elementAt(0).toString()));
			}
		}		
    }
    
    //Cargando desde el background todos los cursos almacenados en la base de datos
    class LoadAllCourses extends AsyncTask<String, String, String>{

    	//Antes de que comience el activity
    	protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Cargando el listado de cursos. Por favor espere...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(false);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			JSONObject json = JParser.makeHttpRequest(url_all_courses, "POST", parametros);
			//reavisar como regresa el request
			Log.v("======>Todos los cursos", json.toString());
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					courses = json.getJSONArray(TAG_COURSES);
					for (int i = 0; i< courses.length(); i++){
						JSONObject c = courses.getJSONObject(i);
						String codigo = c.getString(TAG_CODE);
						String descripcion = c.getString(TAG_DESCRIPCION);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_CODE, codigo);
						map.put(TAG_DESCRIPCION, descripcion);
						courseList.add(map);
					}
				}else{
					
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		//Despues..
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
			runOnUiThread(new Runnable(){
				public void run(){
					ListAdapter adapter = new SimpleAdapter(pantalla_haciaDondeIr.this, courseList, R.layout.list_course, new String[]{TAG_CODE, TAG_DESCRIPCION}, new int[]{R.id.textCourse, R.id.textdescription});
					setListAdapter(adapter);
				}
			});
		}
    	
    }
    
    class LoadWifiScan extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			//25 de Mayo del 2014
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			String uno = (String)params[0];
			String dos = (String)params[1];
			Log.v("======>UNO", uno);
			Log.v("======>DOS", dos);
			//String tres = (String)params[2];
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE0, uno ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, dos ));
			//parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, tres ));
			
			//JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion, "POST", parametrosWifi);
			JSONObject jsonWifi = JParser.makeHttpRequest(url_two, "POST", parametrosWifi);
			//reavisar como regresa el request
			//Log.v("======>Lo que paso al otro lado TRES", jsonWifi.toString());
			Log.v("======>Lo que paso al otro lado DOS", jsonWifi.toString());
						
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					//Paso todo
				}else{
					//Hubo error
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
    	
    }
    
    //Testing the async
    class LoadOneWifi extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			Log.v("======>PARAMS_TO_STRING", Arrays.toString(params));
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE, Arrays.toString(params) ));
			
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacionOne, "POST", parametrosWifi);
			//reavisar como regresa el request
			Log.v("======>Lo que paso al otro lado ONE", jsonWifi.toString());
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					//Paso todo
				}else{
					//Hubo error
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
    }
		
}
