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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class InPlace extends Activity{
	public static String url = new String ("http://192.168.0.6/");
	
	private ProgressDialog pDialog;
	private static final String url_imagen_ubicacion = url+"WebService/imagen_ubicacion.php";
	private static final String TAG_VALUE1 = "value1";
	JSONParser JParser = new JSONParser();
	private ImageView img1;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_AULA_IMAGEN_RIGHT = "aula_imagen";
	private static final String TAG_PATH_IMAGEN_BSSID = "url_imagen";
	JSONArray path_receive = null;
	private String url_imagen;
	Button siguiente;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);	
		Bundle bundle = getIntent().getExtras();
		String path_bssid = bundle.getString("bssid");
		final String code = bundle.getString("code");
		final String descripcion = bundle.getString("descripcion");
		
		new LoadImagen().execute(path_bssid);
		siguiente = (Button)findViewById(R.id.button_next);
		siguiente.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InPlace.this, InPlace_2.class);
				intent.putExtra("code", code);
				intent.putExtra("descripcion", descripcion);
				startActivity(intent);
			}
		});
		
	}
	
	class LoadImagen extends AsyncTask<String, String, String>{
    	
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
			parametrosWifi.add(new BasicNameValuePair(TAG_VALUE1, params[0] ));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_imagen_ubicacion, "POST", parametrosWifi);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					path_receive = jsonWifi.getJSONArray(TAG_AULA_IMAGEN_RIGHT);
					for (int i = 0; i< path_receive.length(); i++){
						JSONObject c = path_receive.getJSONObject(i);
						url_imagen = c.getString(TAG_PATH_IMAGEN_BSSID);
						Log.v("BSSID ruta imagen InPlace CLASS",url_imagen);					
						try{
							img1 = (ImageView)findViewById(R.id.imageDestiny);
							URL url = new URL(url_imagen);
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
		//Despues
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
    }
}
