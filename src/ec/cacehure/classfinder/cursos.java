package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
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
	private static String url_all_courses = "http://192.168.0.6/WebService/get_courses.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_COURSES = "courses";
	private static final String TAG_CODE = "codigo";
	private static final String TAG_DESCRIPCION = "descripcion";
	JSONArray courses = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cusos);
		courseList = new ArrayList<HashMap<String,String>>();
		new LoadAllCourses().execute();
		all_courses = getListView();
		all_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				HashMap<String, String>map =(HashMap<String, String>)all_courses.getItemAtPosition(position);
				String code = map.get(TAG_CODE);
				String description = map.get(TAG_DESCRIPCION);
				Log.v("Codigo","es:"+code);
				Log.v("Descripcion","es:"+description);
//				Log.v("Descripcion Lugar","es:"+descripcion);
//				Intent intent = new Intent(cursos.this, Mapa.class);
//				intent.putExtra("codigo", code);
//				startActivity(intent);
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
			//reavisar como regresa el request
			Log.v("======>Todos los cursos", json.toString());
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
}
