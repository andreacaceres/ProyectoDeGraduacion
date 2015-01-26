package ec.cacehure.classfinder;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	private ProgressDialog pDialog;
	private static final String url_path_imagen = "http://200.126.19.93/WebService/imagen_right.php";
	private static final String TAG_VALUE = "value";
	JSONParser JParser = new JSONParser();
	private ImageView img2;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AULA_IMAGEN_RIGHT = "aula_imagen_right";
	private static final String TAG_PATH_IMAGEN = "path_imagen";
	JSONArray path_receive = null;
	private String path_imagen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
		
		final ImageView img1 = (ImageView)findViewById(R.id.imageCry);
		
		//Lo que viene del activity
		Bundle bundle = getIntent().getExtras();
		String codigo_aula = bundle.getString("codigo");
		
		Log.v("Codigo","Escogido: "+codigo_aula);
		
		LoadImagen receive = new LoadImagen();
		receive.execute(codigo_aula);	
	}
	
	//Cargando la imagen de la derecha
	class LoadImagen extends AsyncTask<String, String, String>{
    	
    	//Antes de comenzar el hilo background le muestra un mensajito =P
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
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE, Arrays.toString(params) ));
			
			Log.v("======>PATH_IMAGEN_CODE", Arrays.toString(params));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_path_imagen, "POST", parametrosWifi);
			Log.v("======>Lo que paso al otro lado PATH_IMAGEN", jsonWifi.toString());
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					path_receive = jsonWifi.getJSONArray(TAG_AULA_IMAGEN_RIGHT);
					Log.v("======>Lo que paso al otro lado PATH_IMAGEN","molestoso"+path_receive);
					for (int i = 0; i< path_receive.length(); i++){
						JSONObject c = path_receive.getJSONObject(i);
						path_imagen = c.getString(TAG_PATH_IMAGEN);
						Log.v("=====>Dentro del for",path_imagen);
						try{
							img2 = (ImageView)findViewById(R.id.image2);
							URL url = new URL(path_imagen);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							img2.setImageBitmap(bitmap);
						}catch(Exception e){
							
						}
					}
				}else{
					//Hubo error
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
