package ec.cacehure.classfinder;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class pantalla_haciaDondeIr extends Activity{

	Button btn_salir, btn_actualizar;
	TextView textConnected, textSsid, textBssid, textRssi;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.togo);
		
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
			}
		});
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
}
