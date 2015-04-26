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
import android.widget.ListView;
import android.widget.Toast;

public class lugares_especificos extends Activity{
	static IP dir_ip = new IP();
	static String url = dir_ip.getIp();
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ListView lugares_especificos;
	ArrayList<HashMap<String,String>> lugares_especificos_List;
	static String url_ruta_corta = url+"WebService/dijkstra.php";
	static String TAG_SUCCESS = "success";
	static String TAG_PATH = "path";
	static String TAG_DESCRIPCION = "descripcion";
	static String TAG_VALUE1 = "value1";
	static String TAG_VALUE2 = "value2";
	static String TAG_VALUE3 = "value3";
	static String TAG_RUTAS_IMAGENES = "rutas_imagenes";
	JSONArray path = null;
	ListViewAdapter_2 adapter;
	
	int bandera = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares_especificos);
		
		lugares_especificos_List = new ArrayList<HashMap<String,String>>();
		Bundle bundle = getIntent().getExtras();
		final int ubicacion_inicial = bundle.getInt("ubicacion_inicial");
		final int ubicacion_final = bundle.getInt("ubicacion_final");
		final String ubi_inicial = Integer.toString(ubicacion_inicial);
		final String ubi_final = Integer.toString(ubicacion_final);
		final String descripcion = bundle.getString("descripcion");
		new load_lugares_especificos().execute(ubi_inicial, ubi_final, descripcion);
	}
	
	class load_lugares_especificos extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(lugares_especificos.this);
    		pDialog.setMessage("Buscando el camino más corto. Por favor espere...");
    		pDialog.show();
    	}
		
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair(TAG_VALUE1, params[0]));
			parametros.add(new BasicNameValuePair(TAG_VALUE2, params[1]));
			parametros.add(new BasicNameValuePair(TAG_VALUE3, params[2]));
			JSONObject json = JParser.makeHttpRequest(url_ruta_corta, "POST", parametros);
			Log.v("Dijsktra: ", json.toString());
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					bandera = 1;
					path = json.getJSONArray(TAG_RUTAS_IMAGENES);
					for (int i = 0; i< path.length(); i++){
						JSONObject c = path.getJSONObject(i);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("ruta", c.getString("path"));
						map.put("descripcion", c.getString("descripcion"));
						lugares_especificos_List.add(map);
					}
				}else{
					//Toast.makeText(lugares_especificos.this, "Error del lado del servidor", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String file_url){
			lugares_especificos = (ListView) findViewById(R.id.listView1);
			adapter = new ListViewAdapter_2(lugares_especificos.this, lugares_especificos_List);
			lugares_especificos.setAdapter(adapter);
			pDialog.dismiss();
			if(bandera == 0){
				Toast.makeText(lugares_especificos.this, "Error del lado del servidor", Toast.LENGTH_SHORT).show();
			}
		}	
	}
}