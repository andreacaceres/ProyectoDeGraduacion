package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class cursos extends ListActivity{
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ListView all_courses;
	ArrayList<HashMap<String, String>> courseList;
	private static String url_all_courses = "http://200.126.19.93/WebService/get_courses.php";
	private static String url_validate = "http://200.126.19.93/WebService/validate.php";
	private static String url_validate_2 = "http://200.126.19.93/WebService/validate_single.php";
//	private static String url_all_courses = "http://192.168.0.6/WebService/get_courses.php";
//	private static String url_validate = "http://192.168.0.6/WebService/validate.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_COURSES = "courses";
	private static final String TAG_CODE = "codigo";
	private static final String TAG_DESCRIPCION = "descripcion";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	private static final String TAG_VALUE3= "value3";
	JSONArray courses = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cusos);
		courseList = new ArrayList<HashMap<String,String>>();
		// BSSID de la otra clase
		Bundle bundle = getIntent().getExtras();
		final String bssid = bundle.getString("bssid");
		
		//ID_IMAGEN de la otra clase
		Bundle bundle_2 = getIntent().getExtras();
		final String id_imagen = bundle_2.getString("id_image_single");
		
		new LoadAllCourses().execute();
		all_courses = getListView();
		all_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, String>map =(HashMap<String, String>)all_courses.getItemAtPosition(position);
				final String code = map.get(TAG_CODE);
				final String description = map.get(TAG_DESCRIPCION);
				if(bssid==null){
					new Validate_2().execute(id_imagen, code, description);
				}else{
					new Validate().execute(bssid, code, description);
				}
			}
		});
	}
	 //Cargando desde el background todos los cursos almacenados en la base de datos
    class LoadAllCourses extends AsyncTask<String, String, String>{
    	//Antes de que comience el activity
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(cursos.this);
    		pDialog.setMessage("Cargando el listado de cursos. Por favor espere...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(false);
    		pDialog.show();
    	}    	
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			JSONObject json = JParser.makeHttpRequest(url_all_courses, "POST", parametros);
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					courses = json.getJSONArray(TAG_COURSES);
					for (int i = 0; i< courses.length(); i++){
						JSONObject c = courses.getJSONObject(i);
						String codigo = c.getString(TAG_CODE);
						String descripcion = c.getString(TAG_DESCRIPCION);
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_CODE, codigo);
						map.put(TAG_DESCRIPCION, descripcion);
						courseList.add(map);
					}
				}else{
					//Hubo error
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		//Despues..
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
			runOnUiThread(new Runnable(){
				@Override
				public void run(){
					ListAdapter adapter = new SimpleAdapter(cursos.this, courseList, R.layout.list_course, new String[]{TAG_CODE, TAG_DESCRIPCION}, new int[]{R.id.textCourse, R.id.textdescription});
					setListAdapter(adapter);
				}
			});
		}
    }
    
    // Validando si el curso pertence al lugar que se realizo la localizacion
    class Validate extends AsyncTask<String, String, String>{
    	
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(cursos.this);
    		pDialog.setMessage("Por favor espere...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	} 
		
    	@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
    		// BSSID, CODE, DESCRIPTION
    		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
    		parametros.add(new BasicNameValuePair(TAG_VALUE1, params[0]));
    		parametros.add(new BasicNameValuePair(TAG_VALUE2, params[1]));
    		parametros.add(new BasicNameValuePair(TAG_VALUE3, params[2]));
    		JSONObject json = JParser.makeHttpRequest(url_validate, "POST", parametros);
    		try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1){
					// solo figuras ya que esta en la misma facultad
					Log.v("Muestra figuras",".");
				}else if(success == 2){
					// Mapa completo
					Log.v("Muestra mapa completo",".");
					Intent outPlace = new Intent(cursos.this, OutPlace.class);
					startActivity(outPlace);
				}else{
					// No hay datos encontrados
					Log.v("Error",".");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
    	
    	@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
    }
    
 // Validando si el curso pertence al lugar que se realizo la localizacion
    class Validate_2 extends AsyncTask<String, String, String>{
    	
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(cursos.this);
    		pDialog.setMessage("Por favor espere...");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	} 
		
    	@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
    		// KNOW PLACE, CODE, DESCRIPTION
    		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
    		parametros.add(new BasicNameValuePair(TAG_VALUE1, params[0]));
    		parametros.add(new BasicNameValuePair(TAG_VALUE2, params[1]));
    		parametros.add(new BasicNameValuePair(TAG_VALUE3, params[2]));
    		JSONObject json = JParser.makeHttpRequest(url_validate_2, "POST", parametros);
    		try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1){
					// solo figuras ya que esta en la misma facultad
					Log.v("En validate 2 solo figuras",".");
				}else if(success == 2){
					// Mapa completo
					Log.v("En validate 2 solo mapa",".");
					Intent outPlace = new Intent(cursos.this, OutPlace.class);
					startActivity(outPlace);
				}else{
					// No hay datos encontrados
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
    	
    	@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
    }
}
