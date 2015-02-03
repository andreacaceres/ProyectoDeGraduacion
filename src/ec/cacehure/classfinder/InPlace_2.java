package ec.cacehure.classfinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class InPlace_2 extends Activity{
	private ProgressDialog pDialog;
	public static String url = new String ("http://192.168.0.6/");
	private static final String url_imagen_destino = url+"WebService/imagen_destino.php";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	JSONParser JParser = new JSONParser();
	private ImageView img1;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AULA_IMAGEN_RIGHT = "aula_imagen";
	private static final String TAG_PATH_IMAGEN_AULA = "path_imagen";
	JSONArray path_receive = null;
	private String path_imagen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destiny);
		Bundle bundle = getIntent().getExtras();
		final String code = bundle.getString("code");
		final String descripcion = bundle.getString("descripcion");
		new LoadImagen().execute(code,descripcion);
	}
	
	class LoadImagen extends AsyncTask<String, String, String>{
		
		@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(InPlace_2.this);
    		pDialog.setMessage("Cargando el aula de clases seleccionado.....");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, params[1] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_imagen_destino, "POST", parametrosWifi);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					path_receive = jsonWifi.getJSONArray(TAG_AULA_IMAGEN_RIGHT);
					for (int i = 0; i< path_receive.length(); i++){
						JSONObject c = path_receive.getJSONObject(i);
						path_imagen = c.getString(TAG_PATH_IMAGEN_AULA);
						Log.v("BSSID ruta imagen InPlace CLASS",path_imagen);					
						try{
							img1 = (ImageView)findViewById(R.id.imageDestiny);
							URL url = new URL(path_imagen);
							Log.v("URL imagen", url.toString());
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							img1.setImageBitmap(bitmap);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}else{
					//Hubo error
					Log.v("En el else",".");
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
		
	}

}
