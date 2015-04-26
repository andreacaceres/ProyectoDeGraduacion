package ec.cacehure.classfinder;

import java.io.InputStream;
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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class pantalla_haciaDondeIr extends Activity{
	static IP dir_ip = new IP();
	static String url = dir_ip.getIp();
	Button btn_Yes, btn_No;
	WifiManager allwifi;
	WifiScanReceiver wifiReciever;
	private static String url_localizacion = url+"WebService/localizacion.php";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	private static final String TAG_VALUE3 = "value3";
	private static final String TAG_VALUE4 = "value4";
	private static final String TAG_VALUE5 = "value5";
	private static final String TAG_VALUE6 = "value6";
	private static final String TAG_VALUE7 = "value7";
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
	JSONArray courses = null;
	
	Vector p = new Vector();
	Vector indice = new Vector();
	private static final String TAG_AP_ONE = "ap";
	private static final String TAG_DESCRIPCION_ONE = "descripcion_one";
	private static final String TAG_PATH_IMAGEN_ONE = "path_imagen_one";
	JSONArray ap = null;
	ImageView imagen_one;
	private TextView lugar;
	private String descripcion;
	private String path_imagen_one;
	Button btnNext;
	Button ubi_espec;
	public String bssid= "";
	public String bssid_connected = "";
	public int bandera = 0;
	//private static final String TAG_X_FINAL = "x_final";
	//private static final String TAG_Y_FINAL = "y_final";
	public int x_coord = 0;
	public int y_coord = 0;
	ImageView imagen_inicial;
	Bitmap bitmap_inicial;
	
	//Contador cliente-servidor
	private static String counter = "0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togo);		
		ubi_espec = (Button)findViewById(R.id.buttonTrian);
		imagen_inicial = (ImageView)findViewById(R.id.image1);
		allwifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiReciever = new WifiScanReceiver();
		allwifi.startScan();
		btn_Yes = (Button)findViewById(R.id.btnYes);
		btn_Yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(pantalla_haciaDondeIr.this, cursos.class);
				intent.putExtra("id_image_single", "0");
				intent.putExtra("bssid_final", bssid_connected);
				startActivity(intent);
			}
		});
						
		btn_No = (Button)findViewById(R.id.btnNo);
		btn_No.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(pantalla_haciaDondeIr.this, lugares_conocidos.class);
				intent.putExtra("bssid_final", bssid);
				startActivity(intent);
			}
		});
		
		ubi_espec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent solo = new Intent(pantalla_haciaDondeIr.this, imagen_triangulada.class);
				solo.putExtra("coord_x", x_coord);
				solo.putExtra("coord_y", y_coord);
				startActivity(solo);*/
				new calculo_coordenadas().execute();
			}
		});
	}
	
	@Override
	protected void onPause() {
		unregisterReceiver(wifiReciever);
		super.onPause();
	}
		@Override
	protected void onResume() {
		registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}
    
    class WifiScanReceiver extends BroadcastReceiver{  	
		@Override
		public void onReceive(Context c, Intent intent) {
			WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
			String bssid_send = myWifiInfo.getBSSID();
			bssid_connected = bssid_send;
			
			List<ScanResult> wifiScanList = allwifi.getScanResults();
			final int n = wifiScanList.size(); //El tamaño de la lista
			Comparator<ScanResult> comparator = new Comparator<ScanResult>(){
				@Override
				public int compare(ScanResult arg0, ScanResult arg1) {
					return (arg0.level>arg1.level ? -1 : (arg0.level==arg1.level ? 0: 1));
				}
	        };       
	        Collections.sort(wifiScanList,comparator);
	        Log.v("ap detectados",wifiScanList.toString());
			for(int i=0;i<n;i++){
				if( (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-WIFI") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Claro_MOLINA0000029162") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Cidis_Lab")){
					p.add(wifiScanList.get(i));
					indice.add(i);
				}
			}
			bssid = wifiScanList.get((Integer) indice.get(0)).BSSID;
			int tam_vetor = p.size();
			//Log.v("ap detectados",""+p.toString());
			if(tam_vetor > 6){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), (p.elementAt(5).toString()), counter);
			}else if(tam_vetor == 5){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), "null", counter);
			}else if(tam_vetor == 4){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), "null", "null", counter);
			}else if(tam_vetor == 3){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), "null", "null", "null", counter);
			}else{
				new LoadWifiScan().execute((p.elementAt(0).toString()), "null", "null", "null", "null", "null", counter);
			}
		}		
    }
    
    class LoadWifiScan extends AsyncTask<String, String, Bitmap>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Capturando potencias...");
    		pDialog.show();
    	}
    	
		@Override
		protected Bitmap doInBackground(String... params) {
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1,params[0]));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2,params[1]));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE3,params[2]));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE4,params[3]));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE5,params[4]));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE6,params[5]));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE7, params[6]));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_localizacion, "POST", parametrosWifi);
			Log.v("Valor del success: ",jsonWifi.toString());
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					bandera = 1;
					ap = jsonWifi.getJSONArray(TAG_AP_ONE);		
					JSONObject c = ap.getJSONObject(0);
					lugar = (TextView)findViewById(R.id.textplace);
					descripcion = c.getString(TAG_DESCRIPCION_ONE);
					path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
					//x_coord = c.getInt(TAG_X_FINAL);
					//y_coord = c.getInt(TAG_Y_FINAL);
					try{
						bitmap_inicial = BitmapFactory.decodeStream((InputStream)new URL(path_imagen_one).getContent());
						lugar = (TextView)findViewById(R.id.textplace);
						lugar.setText(descripcion);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if (success == 2) {
					Toast.makeText(pantalla_haciaDondeIr.this, "Recalculando coordenadas.", Toast.LENGTH_SHORT).show();
				}else if(success == 4){
					p.clear();
					allwifi.startScan();
				}else if(success == 8){
					ap = jsonWifi.getJSONArray(TAG_AP_ONE);
					JSONObject c = ap.getJSONObject(0);
					lugar = (TextView)findViewById(R.id.textplace);
					descripcion = c.getString(TAG_DESCRIPCION_ONE);
					path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
					String contador_tmp = c.getString("contadorClienteServidor");
					counter = contador_tmp;
					Log.v("Valor del tmp contador cliente-servidor: ",contador_tmp);
					if(Integer.parseInt(contador_tmp) == 3){
						try{
							bitmap_inicial = BitmapFactory.decodeStream((InputStream)new URL(path_imagen_one).getContent());
							lugar = (TextView)findViewById(R.id.textplace);
							lugar.setText(descripcion);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						Log.v("Valor del contador cliente-servidor: ",counter);
						p.clear();
						allwifi.startScan();
					}
					/*try{
						bitmap_inicial = BitmapFactory.decodeStream((InputStream)new URL(path_imagen_one).getContent());
						lugar = (TextView)findViewById(R.id.textplace);
						lugar.setText(descripcion);
					}catch(Exception e){
						e.printStackTrace();
					}*/
				}else{
					Toast.makeText(pantalla_haciaDondeIr.this, "Recalculando coordenadas.", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return bitmap_inicial;
		}
		@Override
		protected void onPostExecute(Bitmap image){
			if(image != null){
				imagen_inicial.setImageBitmap(image);
				pDialog.dismiss();
				if(bandera == 1){//Volver a 1
					ubi_espec.setVisibility(1);
				}
			}else{
				pDialog.dismiss();
				Toast.makeText(pantalla_haciaDondeIr.this, "Recalculando coordenadas.", Toast.LENGTH_SHORT).show();
			}
		}
    }
    
    class calculo_coordenadas extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected void onPostExecute(String image){
			pDialog.dismiss();
			Intent solo = new Intent(pantalla_haciaDondeIr.this, imagen_triangulada.class);
			startActivity(solo);
		}
    }
}
