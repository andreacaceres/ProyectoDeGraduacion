package ec.cacehure.classfinder;

import java.io.InputStream;
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
import android.widget.TextView;
import android.widget.Toast;

public class InPlace_2 extends Activity{
	private ProgressDialog pDialog;
	public static String url = new String ("http://192.168.0.5/");
	private static final String url_imagen_destino = url+"WebService/imagen_destino.php";
	private static final String TAG_VALUE1 = "value1";
	private static final String TAG_VALUE2 = "value2";
	JSONParser JParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AULA_IMAGEN_RIGHT = "aula_imagen";
	private static final String TAG_PATH_IMAGEN_AULA = "path_imagen";
	private static final String TAG_DESCRIPCION = "descripcion";
	JSONArray path_receive = null;
	private String path_imagen;
	private String text_descr;
	
	ImageView img;
	Bitmap bitmap;
	TextView descrip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destiny);
		Bundle bundle = getIntent().getExtras();
		final String code = bundle.getString("code");
		final String descripcion = bundle.getString("descripcion");
		img = (ImageView)findViewById(R.id.imageOrigin);
		descrip = (TextView)findViewById(R.id.text_desc);
		new LoadImagen().execute(code,descripcion);
	}
	
	class LoadImagen extends AsyncTask<String, String, Bitmap>{
		
		@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(InPlace_2.this);
    		pDialog.setMessage("Cargando el aula de clases seleccionado.....");
    		pDialog.show();
    	}
		
		@Override
		protected Bitmap doInBackground(String... params) {
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
						text_descr = c.getString(TAG_DESCRIPCION);
						Log.v("Descripcion",text_descr);
						try{
							bitmap = BitmapFactory.decodeStream((InputStream)new URL(path_imagen).getContent());
							descrip.setText(text_descr);
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
			return bitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap image){
			if(image != null){
				img.setImageBitmap(image);
				pDialog.dismiss();
			}else{
				pDialog.dismiss();
				Toast.makeText(InPlace_2.this, "Error en la url", Toast.LENGTH_SHORT).show();
			}
		}
		
	}

}
