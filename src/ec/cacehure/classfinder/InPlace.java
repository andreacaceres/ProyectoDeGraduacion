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

public class InPlace extends Activity{
	public static String url = new String ("http://192.168.176.219/");
	
	private ProgressDialog pDialog;
//	private static final String url_path_imagen = url+"WebService/imagen_in_place.php";
	private static final String url_path_imagen_right = url+"WebService/imagen_in_place_right.php";
	private static final String url_path_imagen_left = url+"WebService/imagen_in_place_left.php";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	private static final String TAG_VALUE3 = "value3";
	JSONParser JParser = new JSONParser();
	private ImageView img1;
	private ImageView img2;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AULA_IMAGEN_RIGHT = "aula_imagen_right";
	private static final String TAG_PATH_IMAGEN_CODE = "path_imagen";
	private static final String TAG_PATH_IMAGEN_BSSID = "url_imagen";
	JSONArray path_receive = null;
	private String path_imagen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);	
		Bundle bundle = getIntent().getExtras();
		String path_bssid = bundle.getString("bssid");
		String code = bundle.getString("code");
		String descripcion = bundle.getString("descripcion");
		
		new LoadImagen_Right().execute(code,descripcion);
		new LoadImagen_Left().execute(path_bssid);
			
	}
	
	//Cargando la imagen de la derecha
	class LoadImagen_Right extends AsyncTask<String, String, String>{
    	
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(InPlace.this);
    		pDialog.setMessage("Dando direcciones");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			Log.v("CODE INPLACE", params[0]);
			Log.v("DESCRI INPLACE", params[1]);
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE2, params[0] ));
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE3, params[1] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_path_imagen_right, "POST", parametrosWifi);
			Log.v("CLASE INPLACE", jsonWifi.toString());
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					path_receive = jsonWifi.getJSONArray(TAG_AULA_IMAGEN_RIGHT);
					for (int i = 0; i< path_receive.length(); i++){
						JSONObject c = path_receive.getJSONObject(i);
						path_imagen = c.getString(TAG_PATH_IMAGEN_CODE);
						try{
							img2 = (ImageView)findViewById(R.id.image2);
							URL url = new URL(path_imagen);
							Log.v("La url de la imagen izquierda es:",url.toString());
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							img2.setImageBitmap(bitmap);
						}catch(Exception e){
							Log.v("En el catch del try","."+e.toString());
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
		//Despues
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
    }
	
	// Load left imagen
	class LoadImagen_Left extends AsyncTask<String, String, String>{
    	
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(InPlace.this);
    		pDialog.setMessage("Dando direcciones");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> parametrosWifi = new ArrayList<NameValuePair>();
			Log.v("BSSID INPLACE", params[0]);
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_path_imagen_left, "POST", parametrosWifi);
			Log.v("CLASE INPLACE", jsonWifi.toString());
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					path_receive = jsonWifi.getJSONArray(TAG_AULA_IMAGEN_RIGHT);
					for (int i = 0; i< path_receive.length(); i++){
						JSONObject c = path_receive.getJSONObject(i);
						path_imagen = c.getString(TAG_PATH_IMAGEN_BSSID);
						try{
							img1 = (ImageView)findViewById(R.id.imageCry);
							URL url = new URL(path_imagen);
							Log.v("La url de la imagen derecha es:",url.toString());
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							img1.setImageBitmap(bitmap);
						}catch(Exception e){
							Log.v("En el catch del try","."+e.toString());
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
		//Despues
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
    }
	
}
