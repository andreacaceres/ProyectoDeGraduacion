package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

//public class lugares_conocidos extends ListActivity{
public class lugares_conocidos extends Activity{

	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ListView lugares_conocidos;
	ArrayList<HashMap<String, String>> lugaresconocidos_List;
	static String url_lugares_conocidos = "http://192.168.0.6/WebService/lugares_conocidos.php";
	static String TAG_SUCCESS = "success";
	static String TAG_ID = "id";
	static String TAG_PLACES = "places";
	static String TAG_DESCRIPCION = "descripcion";
	static String TAG_RUTA = "ruta";
	JSONArray places = null;
	ListViewAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares_conocidos);
		lugaresconocidos_List = new ArrayList<HashMap<String,String>>();
		new load_lugares_conocidos().execute();
	}
	
	class load_lugares_conocidos extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(lugares_conocidos.this);
    		pDialog.setMessage("Cargando el listado de lugares conocidos. Por favor espere...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(false);
    		pDialog.show();
    	} 
    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			JSONObject json = JParser.makeHttpRequest(url_lugares_conocidos, "POST", parametros);
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
						lugaresconocidos_List.add(map);
					}
				}else{
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
		}
	}
}
