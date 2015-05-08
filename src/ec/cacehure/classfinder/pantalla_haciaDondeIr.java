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
import android.os.Handler;
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
	private static String url_formula = url+"WebService/formulas.php";
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
	private static final String TAG_COORDENADAS = "coordenadas";
	private static final String TAG_X_FINAL = "x_final";
	private static final String TAG_Y_FINAL = "y_final";
	private static final String TAG_DESCRIPCION_ONE = "descripcion_one";
	private static final String TAG_PATH_IMAGEN_ONE = "path_imagen_one";
	JSONArray ap = null;
	JSONArray coordenadas = null;
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
	
	public int x_calculado, y_calculado;
	
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
				startActivity(solo);
				finish();*/
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
			List<ScanResult> tmp = null;
			final int n = wifiScanList.size(); //El tamaño de la lista
			Comparator<ScanResult> comparator = new Comparator<ScanResult>(){//Ordenando de menor potencia a mayor
				@Override
				public int compare(ScanResult arg0, ScanResult arg1) {
					return (arg0.level>arg1.level ? -1 : (arg0.level==arg1.level ? 0: 1));
				}
	        };       
	        Collections.sort(wifiScanList,comparator);
			for(int i=0;i<n;i++){
				if( (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:a1:05:61") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:a1:05:60") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:be:6c:51") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:be:6c:50") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("64:9e:f3:87:43:2f") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("66:9e:f3:87:43:2f") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:07:7d:14:a4:95") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:6a:ad:f0") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("02:07:7d:14:ce:21") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:07:7d:14:ce:21") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:6a:43:b1") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:6a:43:b0") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:6a:43:60") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("00:11:88:6a:ce:60") ||  (wifiScanList.get(i).SSID).equalsIgnoreCase("Cidis_Lab") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("c0:f8:da:ac:be:0a") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("bc:67:1c:e4:ca:10") || (wifiScanList.get(i).BSSID).equalsIgnoreCase("bc:67:1c:e4:ca:11") ||(wifiScanList.get(i).BSSID).equalsIgnoreCase("bc:67:1c:e4:a2:80") ||(wifiScanList.get(i).BSSID).equalsIgnoreCase("bc:67:1c:e8:cd:60") ||(wifiScanList.get(i).BSSID).equalsIgnoreCase("bc:67:1c:e8:cd:61") ||(wifiScanList.get(i).BSSID).equalsIgnoreCase("bc:67:1c:e4:a2:81")){
					p.add(wifiScanList.get(i));
					indice.add(i);
				}
			}
			//TEST
			
			//END
			bssid = wifiScanList.get((Integer) indice.get(0)).BSSID;
			int tam_vetor = p.size();
			Log.v("ap detectados filtrados",""+p.toString());
			Log.v("Tamanio del vector es: ",""+tam_vetor);
			
			if(tam_vetor == 8){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), (p.elementAt(5).toString()), counter);
				Log.v("Valor es mayor a 7",".");
			}else if(tam_vetor == 7){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), (p.elementAt(5).toString()), counter);
				Log.v("Valor es mayor a 7",".");
			}else if(tam_vetor == 6){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), (p.elementAt(5).toString()), counter);
				Log.v("Valor es mayor a 6",".");
			}else if(tam_vetor == 5){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), (p.elementAt(4).toString()), "null", counter);
				Log.v("Valor es 5",".");
			}else if(tam_vetor == 4){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), (p.elementAt(3).toString()), "null", "null", counter);
				Log.v("Valor es 4",".");
			}else if(tam_vetor == 3){
				new LoadWifiScan().execute((p.elementAt(0).toString()), (p.elementAt(1).toString()), (p.elementAt(2).toString()), "null", "null", "null", counter);
				Log.v("Valor es 3",".");
			}else{
				new LoadWifiScan().execute((p.elementAt(0).toString()), "null", "null", "null", "null", "null", counter);
				Log.v("Valor es 1",".");
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
					String contador_tmp = c.getString("contadorClienteServidor");
					counter = contador_tmp;
					Log.v("Valor del tmp contador cliente-servidor: ",contador_tmp);
					if(Integer.parseInt(contador_tmp) == 3){
						counter = "0";
						try{
							bitmap_inicial = BitmapFactory.decodeStream((InputStream)new URL(path_imagen_one).getContent());
							lugar = (TextView)findViewById(R.id.textplace);
							lugar.setText(descripcion);
						}catch(Exception e){
							e.printStackTrace();
						}
					}else{
						Log.v("Valor del contador cliente-servidor en el else: ",counter);
						p.clear();
						allwifi.startScan();
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
					Toast.makeText(pantalla_haciaDondeIr.this, "Error en la toma de datos...Espere por favor.", Toast.LENGTH_SHORT).show();
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
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			JSONObject json = JParser.makeHttpRequest(url_formula, "POST", parametros);
			Log.v("Formulasssssssssssssss", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					coordenadas = json.getJSONArray(TAG_COORDENADAS);		
					JSONObject c = coordenadas.getJSONObject(0);
					x_calculado = c.getInt(TAG_X_FINAL);
					y_calculado = c.getInt(TAG_Y_FINAL);
					Log.v("x_calculado es: ",""+x_calculado);
					Log.v("y_calculado es: ",""+y_calculado);
				}else{
					Handler handler =  new Handler(pantalla_haciaDondeIr.this.getMainLooper());
				    handler.post( new Runnable(){
				        @Override
						public void run(){
							Toast.makeText(pantalla_haciaDondeIr.this, "Se regreso a esta pantalla debido a que los valores de las coordenadas no son validas.", Toast.LENGTH_LONG).show();
				        	//showAlertDialog(imagen_triangulada.this, "Error", "Volviendo a la pantalla anterior para obtener nuevos datos.", false);
				        }
				    });
					Intent solo = new Intent(pantalla_haciaDondeIr.this, MainActivity.class);
					startActivity(solo);
					finish();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		@Override
		protected void onPostExecute(String image){
			pDialog.dismiss();
			Intent solo = new Intent(pantalla_haciaDondeIr.this, imagen_triangulada.class);
			solo.putExtra("x_calculado", x_calculado);
			solo.putExtra("y_calculado", y_calculado);
			startActivity(solo);
		}
    }
}
