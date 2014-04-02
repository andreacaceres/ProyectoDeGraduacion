package ec.cacehure.classfinder;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class pantalla_haciaDondeIr extends Activity{

	Button btn_salir, btn_actualizar;
	TextView textConnected, textSsid, textBssid, textRssi;
	//LOCALIZACION
	WifiManager allwifi;
	WifiScanReceiver wifiReciever;
	String BSSIDValido[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togo);
		
		//Localizacion
		allwifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		wifiReciever = new WifiScanReceiver();
		allwifi.startScan();
		//END
		
		textConnected = (TextView)findViewById(R.id.textconnected);       
        textSsid = (TextView)findViewById(R.id.textssidvalue);
        textBssid = (TextView)findViewById(R.id.textbssidvalue);
        textRssi = (TextView)findViewById(R.id.textrssidvalue);
        
        DisplayWifiState();
		
		btn_salir = (Button)findViewById(R.id.exit);
		btn_salir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_actualizar = (Button)findViewById(R.id.refresh);
		btn_actualizar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Actualizar los datos
				DisplayWifiState();
				allwifi.startScan();
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
			registerReceiver(wifiReciever, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	//Obtiene los valores del AP
    private void DisplayWifiState(){
    	WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
		textConnected.setText("--- CONNECTED ---");	        
		textSsid.setText(myWifiInfo.getSSID());
		textBssid.setText(myWifiInfo.getBSSID());			
		textRssi.setText(String.valueOf(myWifiInfo.getRssi()));
    }
    
    //Clase WifiScanReceiver
    class WifiScanReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context c, Intent intent) {
			// TODO Auto-generated method stub
			//Estan todas las wifi detectadas
			List<ScanResult> wifiScanList = allwifi.getScanResults();
			final int n = wifiScanList.size(); //El tamaño de la lista
			BSSIDValido = new String[n];
			for(int i= 0; i<n; i++){
				if((wifiScanList.get(i).BSSID).equalsIgnoreCase("C0:F8:DA:AC:BE:0A")){
	        		 BSSIDValido[i] = ((wifiScanList.get(i)).toString());
	        		 Log.v("============= Android", "EL BBSID VALIDO ES: "+wifiScanList.get(i).BSSID);
	        		 Log.v("============= Android", "EL SSID  VALIDO ES: "+wifiScanList.get(i).SSID);
	        		 Log.v("============= Android", "EL RSSI  VALIDO ES: "+wifiScanList.get(i).level);
	        	}else{
	        		 Log.v("============= Android", "EL BBSID NO VALIDO ES: "+wifiScanList.get(i).BSSID);
	        		 Log.v("============= Android", "EL SSID  NO VALIDO ES: "+wifiScanList.get(i).SSID);
	        		 Log.v("============= Android", "EL RSSI  NO VALIDO ES: "+wifiScanList.get(i).level);
	        	}	
			}
		}
    	
    }
}
