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
import android.widget.Toast;

public class cursos extends ListActivity{
	static IP dir_ip = new IP();
	static String url = dir_ip.getIp();
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ListView all_courses;
	ArrayList<HashMap<String, String>> courseList;
	private static String url_all_courses = url+"WebService/get_courses.php";
	private static String url_validate = url+"WebService/validate.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_COURSES = "courses";
	private static final String TAG_CODE = "codigo";
	private static final String TAG_DESCRIPCION = "descripcion";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	private static final String TAG_VALUE3 = "value3";
	private static final String TAG_VALUE4 = "value4";
	JSONArray courses = null;
	JSONArray coordenadas = null;
	private static final String TAG_COORDENADAS = "coordenadas";
	private static final String TAG_X_1 = "coord_x_1";
	private static final String TAG_Y_1 = "coord_y_1";
	JSONArray coordenadas_aulas = null;
	private static final String TAG_AULAS = "coordenadas_aula";
	private static final String TAG_X_2 = "coord_x_2";
	private static final String TAG_Y_2 = "coord_y_2";
	JSONArray ubicacion = null;
	private static final String TAG_UBICACION = "ubicacion";
	private static final String TAG_UBICACION_INICIAL = "inicial";
	private static final String TAG_UBICACION_FINAL = "final";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cusos);
		courseList = new ArrayList<HashMap<String,String>>();
		Bundle bundle = getIntent().getExtras();
		final String id_imagen = bundle.getString("id_image_single");
		final String bssid_final = bundle.getString("bssid_final");
		
		new LoadAllCourses().execute();
		all_courses = getListView();
		all_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String>map =(HashMap<String, String>)all_courses.getItemAtPosition(position);
				final String code = map.get(TAG_CODE);
				final String description = map.get(TAG_DESCRIPCION);
				new Validate().execute(bssid_final, id_imagen, code, description);
			}
		});
	}

    class LoadAllCourses extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(cursos.this);
    		pDialog.setMessage("Cargando el listado de cursos. Por favor espere...");
    		pDialog.show();
    	}    	
		@Override
		protected String doInBackground(String... params) {
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
					Toast.makeText(cursos.this, "Error del lado del servidor.", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}

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
    
    class Validate extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(cursos.this);
    		pDialog.setMessage("Por favor espere...");
    		pDialog.show();
    	} 
		
    	@Override
		protected String doInBackground(String... params) {
    		List<NameValuePair> parametros = new ArrayList<NameValuePair>();
    		parametros.add(new BasicNameValuePair(TAG_VALUE1, params[0])); //BSSID
    		parametros.add(new BasicNameValuePair(TAG_VALUE2, params[1])); //ID_IMAGEN
    		parametros.add(new BasicNameValuePair(TAG_VALUE3, params[2])); //CODE
    		parametros.add(new BasicNameValuePair(TAG_VALUE4, params[3])); //DESCRIPCION
    		JSONObject json = JParser.makeHttpRequest(url_validate, "POST", parametros);
    		Log.v("Cursos",json.toString());
    		try {
				int success = json.getInt(TAG_SUCCESS);
				if (success == 1){
//					Log.v("Misma facultad",".");
//					Intent intent = new Intent(cursos.this, InPlace.class);
//					intent.putExtra("bssid", params[0]);
//					intent.putExtra("code", params[2]);
//					intent.putExtra("descripcion", params[3]);
//					startActivity(intent);
					/*Intent intent = new Intent(cursos.this, InPlace_2.class);
					intent.putExtra("code", params[2]);
					intent.putExtra("descripcion", params[3]);
					startActivity(intent);*/
					
					//Nuevo
					Log.v("Misma facultad",".");
					Intent mapa_fiec = new Intent(cursos.this, OutPlace.class);
					coordenadas = json.getJSONArray(TAG_COORDENADAS);
					//mapa_fiec.putExtra("bandera",0);
					for (int i = 0; i< coordenadas.length(); i++){
						JSONObject coord = coordenadas.getJSONObject(i);
						int x1 = coord.getInt(TAG_X_1);
						int y1 = coord.getInt(TAG_Y_1);
						mapa_fiec.putExtra("x1", x1);
						mapa_fiec.putExtra("y1", y1);
					}
					startActivity(mapa_fiec);
				}else if(success == 2){
					Log.v("No misma facultad",".");
					Intent mapa_fiec = new Intent(cursos.this, OutPlace.class);
					coordenadas = json.getJSONArray(TAG_COORDENADAS);
					coordenadas_aulas = json.getJSONArray(TAG_AULAS);
					ubicacion = json.getJSONArray(TAG_UBICACION);
					
					//mapa_fiec.putExtra("bandera",1);
					for (int i = 0; i< coordenadas.length(); i++){
						JSONObject coord = coordenadas.getJSONObject(i);
						int x1 = coord.getInt(TAG_X_1);
						int y1 = coord.getInt(TAG_Y_1);
						mapa_fiec.putExtra("x1", x1);
						mapa_fiec.putExtra("y1", y1);
					}
					for (int i = 0; i< coordenadas_aulas.length(); i++){
						JSONObject coord_aulas = coordenadas_aulas.getJSONObject(i);
						int x2 = coord_aulas.getInt(TAG_X_2);
						int y2 = coord_aulas.getInt(TAG_Y_2);
						mapa_fiec.putExtra("x2", x2);
						mapa_fiec.putExtra("y2", y2);
					}					
					for (int i = 0; i< ubicacion.length(); i++){
						JSONObject ubicaciones = ubicacion.getJSONObject(i);
						int ub_inicial = ubicaciones.getInt(TAG_UBICACION_INICIAL);
						int ub_final = ubicaciones.getInt(TAG_UBICACION_FINAL);
						mapa_fiec.putExtra("ubicacion_inicial", ub_inicial);
						mapa_fiec.putExtra("ubicacion_final", ub_final);
					}
					mapa_fiec.putExtra("descripcion", params[3]);
					startActivity(mapa_fiec);			
				}else{
					Toast.makeText(cursos.this, "No se encotró registros.", Toast.LENGTH_SHORT).show();
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