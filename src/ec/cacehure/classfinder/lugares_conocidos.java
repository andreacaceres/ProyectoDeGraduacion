package ec.cacehure.classfinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class lugares_conocidos extends ListActivity{

	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	ListView lugares_conocidos;
	ArrayList<HashMap<String, String>> lugaresconocidos_List;
	private static String url_lugares_conocidos = "http://192.168.0.6/WebService/lugares_conocidos.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PLACES = "places";
	private static final String TAG_DESCRIPCION = "descripcion";
	private static final String TAG_RUTA = "ruta";
	private static final String TAG_IMAGE = "imagen";
	private ImageView img;
	JSONArray places = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lugares_conocidos);
		lugaresconocidos_List = new ArrayList<HashMap<String,String>>();
//		new load_lugares_conocidos().execute();
//		lugares_conocidos = getListView();
		ImageView[] imageViewArray = new ImageView[2];

	    for (int i = 0; i < 2; i++ ) {
	        imageViewArray[i] = new ImageView(this);
	        imageViewArray[i].setImageResource(R.drawable.ic_imagen);
//	        imageViewArray[i].setSomething(somethingelse); // unique property for every imageView
	    }
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
			Log.v("======>Todos los cursos", json.toString());
			try{
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					places = json.getJSONArray(TAG_PLACES);
					for (int i = 0; i< places.length(); i++){
						JSONObject c = places.getJSONObject(i);
						String descripcion = c.getString(TAG_DESCRIPCION);
						String ruta_imagen = c.getString(TAG_RUTA);
//						try{
//							img = (ImageView)findViewById(R.id.imageThumbnail);
//							URL url = new URL(ruta_imagen);
//							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//							img.setImageBitmap(bitmap);
//						}catch(Exception e){
//							Log.v("=====>Dentro del for","Error");
//						}
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_DESCRIPCION, descripcion);
						map.put(TAG_RUTA, ruta_imagen);
						lugaresconocidos_List.add(map);
					}
				}else{
					Log.v("=====>ERROR","Error");
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
					ListAdapter adapter = new SimpleAdapter(lugares_conocidos.this, lugaresconocidos_List, R.layout.listview_thumbnail, new String[]{TAG_DESCRIPCION, TAG_RUTA}, new int[]{R.id.descripcion, R.id.texturl});
					setListAdapter(adapter);
				}
			});
		}
	}
}
