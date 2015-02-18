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
	
	public static String url = new String ("http://200.126.19.93/");
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
	
	//Mostrar imagen
	ImageView imagen_one;
	private TextView lugar;
	private String descripcion;
	private String path_imagen_one;
	
	//test maps
	Button btnNext;
	
	public String bssid= "";
	public String bssid_connected = "";
	
	public int bandera = 0;
	private static final String TAG_X_FINAL = "x_final";
	private static final String TAG_Y_FINAL = "y_final";
	public int x_coord = 0;
	public int y_coord = 0;
	
	ImageView imagen_inicial;
	Bitmap bitmap_inicial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togo);		
		imagen_inicial = (ImageView)findViewById(R.id.image1);
		
		allwifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiReciever = new WifiScanReceiver();
		allwifi.startScan();
		
		btn_Yes = (Button)findViewById(R.id.btnYes);
		btn_Yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(pantalla_haciaDondeIr.this, cursos.class);
				intent.putExtra("id_image_single", "0");
				intent.putExtra("bssid_final", bssid_connected);
				//Mandar aviso de que se detecto las coordenadas
				intent.putExtra("bandera", bandera);
				intent.putExtra("x_coord", x_coord);
				intent.putExtra("y_coord", y_coord);
				startActivity(intent);
			}
		});
						
		btn_No = (Button)findViewById(R.id.btnNo);
		btn_No.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(pantalla_haciaDondeIr.this, lugares_conocidos.class);
				intent.putExtra("bssid_final", bssid);
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
			//Getting the BSSID that is connected
			WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
			String bssid_send = myWifiInfo.getBSSID();
			bssid_connected = bssid_send;
			
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
			for(int i=0;i<n;i++){
				if( (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-WIFI") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Claro_MOLINA0000029162") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-EVENTOS") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC-CONSEJO") || (wifiScanList.get(i).SSID).equalsIgnoreCase("FIEC_MET") || (wifiScanList.get(i).SSID).equalsIgnoreCase("Cidis_Lab")){
					p.add(wifiScanList.get(i));
					indice.add(i);
				}
			}
			bssid = wifiScanList.get((Integer) indice.get(0)).BSSID;
			int tam_vetor = p.size();
			Log.v("Vector p:", p.toString());
			if(tam_vetor > 6){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), (p.elementAt(5).toString()));
			}else if(tam_vetor == 5){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), "null");
			}else if(tam_vetor == 4){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), "null", "null");
			}else if(tam_vetor == 3){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), "null", "null", "null");
			}else {
				new LoadWifiScan().execute((p.elementAt(0).toString()), "null", "null", "null", "null", "null");
			}
		}		
    }
    
    class LoadWifiScan extends AsyncTask<String, String, Bitmap>{

    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(pantalla_haciaDondeIr.this);
    		pDialog.setMessage("Calculando coordenadas...");
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
					x_coord = c.getInt(TAG_X_FINAL);
					y_coord = c.getInt(TAG_Y_FINAL);
					try{
						bitmap_inicial = BitmapFactory.decodeStream((InputStream)new URL(path_imagen_one).getContent());
						lugar = (TextView)findViewById(R.id.textplace);
						lugar.setText(descripcion);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else if (success == 2) {
					Toast.makeText(pantalla_haciaDondeIr.this, "Error del lado del servidor.", Toast.LENGTH_SHORT).show();
				}else if(success == 4){
					p.clear();
					allwifi.startScan();
				}else if(success == 8){
					ap = jsonWifi.getJSONArray(TAG_AP_ONE);				
					JSONObject c = ap.getJSONObject(0);
					lugar = (TextView)findViewById(R.id.textplace);
					descripcion = c.getString(TAG_DESCRIPCION_ONE);
					path_imagen_one = c.getString(TAG_PATH_IMAGEN_ONE);
					try{
						bitmap_inicial = BitmapFactory.decodeStream((InputStream)new URL(path_imagen_one).getContent());
						lugar = (TextView)findViewById(R.id.textplace);
						lugar.setText(descripcion);
					}catch(Exception e){
						e.printStackTrace();
					}
				}else{
					Toast.makeText(pantalla_haciaDondeIr.this, "Error del lado del servidor.", Toast.LENGTH_SHORT).show();
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
			}else{
				pDialog.dismiss();
				Toast.makeText(pantalla_haciaDondeIr.this, "Error en la url", Toast.LENGTH_SHORT).show();
			}
		}
    }
}
