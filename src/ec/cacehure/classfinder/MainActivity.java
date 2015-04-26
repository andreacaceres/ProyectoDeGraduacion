package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	static IP dir_ip = new IP();
	static String url = dir_ip.getIp();
	Boolean isInternetPresent = false;
    ConnectionDetector cd;
    public int flag = 0;
    Button btn_salir;
    Button btnStatus;
    ImageButton btnSalir2;
    ImageButton btnIniciar2;  
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_SSID = "ssid_send";
    JSONParser jsonParser = new JSONParser();
    private static final String url_filtro =  url+"WebService/filtro.php";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		 cd = new ConnectionDetector(getApplicationContext());
		 btnIniciar2 = (ImageButton)findViewById(R.id.imageButton1);	 
		 btnIniciar2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
					new validate().execute();
                } else {
                    showAlertDialog(MainActivity.this, "No conexión a Internet", "Usted no está conectado a Internet.", false);
                	//showAlertDialog(MainActivity.this, "ERROR", "Usted no está conectado con la red de la FIEC.", false);
                }
			}
		});
		 btnSalir2 = (ImageButton)findViewById(R.id.imageButton2);
		 btnSalir2.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog.show();
    }
    
    class validate extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(MainActivity.this);
    		pDialog.setMessage("Validando el dominio...");
    		pDialog.show();
    	}

		@Override
		protected String doInBackground(String... params) {
			WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
			String ssid_send = myWifiInfo.getSSID();
			Log.v("Clase MainActivity SSID:",ssid_send);
			Log.v("Clase MainActivity BSSID:",myWifiInfo.getBSSID());
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair(TAG_SSID,ssid_send));		
			JSONObject json = jsonParser.makeHttpRequest(url_filtro, "POST", parametros);
			Log.v("Clase MainActivity SUCCESS:",json.toString());
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					Intent intent = new Intent(MainActivity.this, pantalla_haciaDondeIr.class);
					startActivity(intent);
				}else{
					//showAlertDialog(MainActivity.this, "ERROR", "Usted no está conectado con la red de la FIEC.", false);
					Handler handler =  new Handler(MainActivity.this.getMainLooper());
				    handler.post( new Runnable(){
				        @Override
						public void run(){
				        	showAlertDialog(MainActivity.this, "ERROR", "Usted no está conectado con la red de la FIEC.", false);
				        }
				    });
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}   	

        @Override
		protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}