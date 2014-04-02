package ec.cacehure.classfinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}
	
	/*
	 * Descripción: Esta función de tipo boolean
	 * 				me devuelve un valor de True
	 * 				si es que el dispositivo esta conectado
	 * 				a una red Wi-Fi.
	 * 				Devolverá falso en caso de no estar conectado
	 * 				a una red Wi-Fi.
	 * */
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity != null){
			NetworkInfo[]info = connectivity.getAllNetworkInfo();
			if(info != null){
				for(int i=0; i<info.length;i++){
					if(info[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		}
		return false;
	}
}
