package ec.cacehure.classfinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class pantalla_haciaDondeIr extends Activity{

	Button btn_Yes, btn_No;
	WifiManager allwifi;
	WifiScanReceiver wifiReciever;
	private static String url_localizacion_1_2 = "http://200.126.19.93/WebService/localizacion_1_2.php";
	private static String url_localizacion_3 = "http://200.126.19.93/WebService/localizacion_3.php";
	private static String url_localizacion_4 = "http://200.126.19.93/WebService/localizacion_4.php";
	private static String url_localizacion_5 = "http://200.126.19.93/WebService/localizacion_5.php";
	private static String url_localizacion_6 = "http://200.126.19.93/WebService/localizacion_6.php";
	
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	private static final String TAG_VALUE3 = "value3";
	private static final String TAG_VALUE4 = "value4";
	private static final String TAG_VALUE5 = "value5";
	private static final String TAG_VALUE6 = "value6";
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
	JSONArray courses = null;
	
	Vector p = new Vector();
	private static final String TAG_VALUE = "value";
	private static final String TAG_AP_ONE = "ap";
	private static final String TAG_DESCRIPCION_ONE = "descripcion_one";
	private static final String TAG_PATH_IMAGEN_ONE = "path_imagen_one";
	JSONArray ap = null;
	
	//Mostrar imagen
	ImageView imagen_one;
	private TextView lugar;
	private String descripcion;
	private String path_imagen_one;
	
	//test maps
	Button btnNext;
	
	public String bssid= "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togo);
				
		allwifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiReciever = new WifiScanReceiver();
		allwifi.startScan();
		
		btn_Yes = (Button)findViewById(R.id.btnYes);
		btn_Yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(pantalla_haciaDondeIr.this, cursos.class);
				intent.putExtra("bssid", bssid);
				startActivity(intent);
			}
		});
						
		btn_No = (Button)findViewById(R.id.btnNo);
		btn_No.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(pantalla_haciaDondeIr.this, lugares_conocidos.class);
				startActivity(intent);
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
			//Ordena la lista de menos level a mayor level
			Comparator<ScanResult> comparator = new Comparator<ScanResult>(){
				@Override
				public int compare(ScanResult arg0, ScanResult arg1) {
					// TODO Auto-generated method stub
					return (arg0.level>arg1.level ? -1 : (arg0.level==arg1.level ? 0: 1));
				}
	        };       
	        Collections.sort(wifiScanList,comparator);
			//Imprime la lista que destecto y ordenada
			Log.v("=============>DETECTA", "AP's: "+ wifiScanList);
			for(int i=0;i<n;i++){
				if( (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-WIFI") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Claro_MOLINA0000029162") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-EVENTOS") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC_CONSEJO") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC_MET") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Cidis_Lab")){
					p.add(wifiScanList.get(i));
				}
			}
			Log.v("=============> Android BSSID", "AP DISPONIBLES VECTOR: "+ p.toString());
			int tam_vetor = p.size();
			Log.v("=============> Android BSSID", "TAMAÑO VECTOR: "+ tam_vetor);
			if(tam_vetor > 6){
				LoadWifiScan_6 six = new LoadWifiScan_6();
				bssid = wifiScanList.get(0).BSSID;
				six.execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), (p.elementAt(5).toString()));
			}else if(tam_vetor == 5){
				LoadWifiScan_5 five = new LoadWifiScan_5();
				bssid = wifiScanList.get(0).BSSID;
				five.execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()));
			}else if(tam_vetor == 4){
				LoadWifiScan_4 four = new LoadWifiScan_4();
				bssid = wifiScanList.get(0).BSSID;
				four.execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()));
			}else if(tam_vetor == 3){
				LoadWifiScan_3 three = new LoadWifiScan_3();
				bssid = wifiScanList.get(0).BSSID;
				three.execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()));
			}else {
				LoadWifiScan_1_2 one_two = new LoadWifiScan_1_2();
				bssid = wifiScanList.get(0).BSSID;
				one_two.execute((p.elementAt(0).toString()));
			}
		}		
    }
    
    // Clases anónimas
    class LoadWifiScan_1_2 extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE, params[0] ));
			
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion_1_2, "POST", parametrosWifi);
//			Log.v("======>Lo que paso al otro lado ONE_TWO", jsonWifi.toString());
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					ap = jsonWifi.getJSONArray(TAG_AP_ONE);
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
//						Log.v("=====>Dentro del for",descripcion);
//						Log.v("=====>Dentro del for",path_imagen_one);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		//Despues
		@Override
		protected void onPostExecute(String file_url){
			lugar = (TextView)findViewById(R.id.textplace);
			lugar.setText(descripcion);
			pDialog.dismiss();
		}
    	
    }
    
    class LoadWifiScan_3 extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, params[1] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE3, params[2] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion_3, "POST", parametrosWifi);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				ap = jsonWifi.getJSONArray(TAG_AP_ONE);
				if(success == 0){
					//Error
				}else if(success == 1){
					//Paso todo sin problemas
				}else if(success == 2){
					//Realizar segunda toma de datos porq capto un level=0
					p.clear();
					allwifi.startScan();
				}else if(success == 3){
					// Hay un ap de los que se envio que no esta en el mismo piso
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}else{
					// No hay tres ap para la localizacion
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String file_url){
			lugar = (TextView)findViewById(R.id.textplace);
			lugar.setText(descripcion);
			pDialog.dismiss();
		}
    	
    }
    
    class LoadWifiScan_4 extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, params[1] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE3, params[2] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE4, params[3] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion_4, "POST", parametrosWifi);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				ap = jsonWifi.getJSONArray(TAG_AP_ONE);
				if(success == 0){
					//Error
				}else if(success == 1){
					//Paso todo sin problemas
				}else if(success == 2){
					//Realizar segunda toma de datos porq capto un level=0
					p.clear();
					allwifi.startScan();
				}else if(success == 3){
					// Hay un ap de los que se envio que no esta en el mismo piso
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}else{
					// No hay tres ap para la localizacion
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(String file_url){
			lugar = (TextView)findViewById(R.id.textplace);
			lugar.setText(descripcion);
			pDialog.dismiss();
		}
    }
    
    class LoadWifiScan_5 extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, params[1] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE3, params[2] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE4, params[3] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE5, params[4] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion_5, "POST", parametrosWifi);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				ap = jsonWifi.getJSONArray(TAG_AP_ONE);
				if(success == 0){
					//Error
				}else if(success == 1){
					//Paso todo sin problemas
				}else if(success == 2){
					//Realizar segunda toma de datos porq capto un level=0
					p.clear();
					allwifi.startScan();
				}else if(success == 3){
					// Hay un ap de los que se envio que no esta en el mismo piso
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}else{
					// No hay tres ap para la localizacion
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(String file_url){
			lugar = (TextView)findViewById(R.id.textplace);
			lugar.setText(descripcion);
			pDialog.dismiss();
		}
    }
    
    class LoadWifiScan_6 extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, params[1] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE3, params[2] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE4, params[3] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE5, params[4] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE6, params[5] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion_6, "POST", parametrosWifi);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				ap = jsonWifi.getJSONArray(TAG_AP_ONE);
				if(success == 0){
					//Error
				}else if(success == 1){
					//Paso todo sin problemas
				}else if(success == 2){
					//Realizar segunda toma de datos porq capto un level=0
					p.clear();
					allwifi.startScan();
				}else if(success == 3){
					// Hay un ap de los que se envio que no esta en el mismo piso
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}else{
					// No hay tres ap para la localizacion
					for (int i = 0; i< ap.length(); i++){
						JSONObject c = ap.getJSONObject(i);
						lugar = (TextView)findViewById(R.id.textplace);
						descripcion = c.getString(TAG_DESCRIPCION_ONE);
						path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
						try{
							imagen_one = (ImageView)findViewById(R.id.image1);
							URL url = new URL(path_imagen_one);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imagen_one.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
    	
		@Override
		protected void onPostExecute(String file_url){
			lugar = (TextView)findViewById(R.id.textplace);
			lugar.setText(descripcion);
			pDialog.dismiss();
		}
    }
}
