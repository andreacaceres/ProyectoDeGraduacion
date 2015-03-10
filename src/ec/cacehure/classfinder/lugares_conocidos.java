package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

//public class lugares_conocidos extends ListActivity{
public class lugares_conocidos extends Activity{
	static IP dir_ip = new IP();
	static String url = dir_ip.getIp();
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ListView lugares_conocidos;
	ArrayList<HashMap<String, String>> lugaresconocidos_List;
	static String url_lugares_conocidos = url+"WebService/lugares_conocidos.php";
	static String TAG_SUCCESS = "success";
	static String TAG_ID = "id";
	static String TAG_PLACES = "places";
	static String TAG_DESCRIPCION = "descripcion";
	static String TAG_RUTA = "ruta";
	static String TAG_BBSID_VALUE = "bssid_value";
	public static String TAG_BSSID_FINAL = "bssid_final";
	JSONArray places = null;
	ListViewAdapter adapter;
	Button refresh;
	
	int bandera = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares_conocidos);
		lugaresconocidos_List = new ArrayList<HashMap<String,String>>();
		
		Bundle bundle = getIntent().getExtras();
		final String bssid_low = bundle.getString("bssid_final");		
		new load_lugares_conocidos().execute(bssid_low);
	}
	
	class load_lugares_conocidos extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(lugares_conocidos.this);
    		pDialog.setMessage("Cargando el listado de lugares conocidos. Por favor espere...");
    		pDialog.show();
    	} 
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub			
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair(TAG_BBSID_VALUE, params[0]));
			JSONObject json = JParser.makeHttpRequest(url_lugares_conocidos, "POST", parametros);
			Log.v("Lugares conocidos", json.toString());
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					places = json.getJSONArray(TAG_PLACES);
					for (int i = 0; i< places.length(); i++){
						JSONObject c = places.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", c.getString("id"));
						map.put("descripcion", c.getString("descripcion"));
						map.put("ruta", c.getString("ruta"));
						map.put("bssid_final", params[0]);
						lugaresconocidos_List.add(map);
					}
					bandera = 1;
				}else{
					Log.v("========> ERROR", "BSSID: ");
					//Toast.makeText(lugares_conocidos.this, "No se encontraron lugares conocidos al AP que se encuentra usted conectado", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String file_url){
			lugares_conocidos = (ListView) findViewById(R.id.listview);
			adapter = new ListViewAdapter(lugares_conocidos.this, lugaresconocidos_List);
			lugares_conocidos.setAdapter(adapter);
			pDialog.dismiss();
			if(bandera == 0){
				Toast.makeText(lugares_conocidos.this, "No se encontraron lugares conocidos al AP que se encuentra usted conectado", Toast.LENGTH_LONG).show();
			}
		}
	}
}
