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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	public static String url = new String ("http://192.168.0.5/");
	Boolean isInternetPresent = false;
    ConnectionDetector cd;
    public int flag = 0;
    Button btn_salir;
    Button btnStatus;
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
		 btnStatus = (Button)findViewById(R.id.begin);
		 
		 btnStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
                    // La conexión a internet esta presente
					new validate().execute();
                } else {
                    // La conexión a internet no esta presente
                    // Se le solicita al usuario que se conecte por medio Wi-Fi al Internet
                    showAlertDialog(MainActivity.this, "No conexión a Internet", "Usted no está conectado a Internet.", false);
                }
			}
		});
		 
		 btn_salir = (Button)findViewById(R.id.btnNo);
		 btn_salir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	//Muestra mensaje
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Titulo del mensaje
        alertDialog.setTitle(title);
 
        // Descripcion del mensaje
        alertDialog.setMessage(message);
          
        // Boton OK
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {}
        });
        // Mostrando mensaje de alerta
        alertDialog.show();
    }
    
    class validate extends AsyncTask<String, String, String>{
    	
    	//Antes de comenzar el hilo background le muestra un mensajito =P
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(MainActivity.this);
    		pDialog.setMessage("Validando el dominio...");
    		pDialog.show();
    	}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//Getting the SSID
			WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
			String ssid_send = myWifiInfo.getSSID();
			//Building parameters
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair(TAG_SSID,ssid_send));		
			//Sending data, POST
			JSONObject json = jsonParser.makeHttpRequest(url_filtro, "POST", parametros);
			
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					Intent intent = new Intent(MainActivity.this, pantalla_haciaDondeIr.class);
					startActivity(intent);
				}else{
					showAlertDialog(MainActivity.this, "No correcto Wifi", "Usted no está conectado con la red de la FIEC.", false);
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}   	

		//Cierra el pDialog
        @Override
		protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }
}

