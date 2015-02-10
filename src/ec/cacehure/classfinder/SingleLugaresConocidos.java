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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SingleLugaresConocidos extends Activity{
	public static String url = new String ("http://192.168.0.5/");
	
	private ProgressDialog pDialog;
	Button yes,no;
	private static final String url_imagen_single = url+"WebService/lugares_conocidos_single.php";
	private static final String TAG_ID_IMAGEN = "id_imagen";
	private static final String TAG_IMAGEN = "imagen";
	JSONParser JParser = new JSONParser();
	ImageView imgSingle;
	Bitmap bitmap_Single;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PATH_IMAGEN = "ruta_imagen";
	JSONArray path_receive = null;
	private String path_imagen;
	TextView Text;
	String variable_null = "null";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlelugares);
		
		Bundle bundle = getIntent().getExtras();
		final String id_imagen = bundle.getString("id");
		final String text_receive = bundle.getString("descripcion");
		final String bssid_final = bundle.getString("bssid_final");
		imgSingle = (ImageView)findViewById(R.id.imageViewSingle);
		new LoadImagen().execute(id_imagen);
		Text = (TextView)findViewById(R.id.textViewDescripcion);
		Text.setText(text_receive);	
		
		yes = (Button)findViewById(R.id.btnYesSingle);
		yes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SingleLugaresConocidos.this, cursos.class);
				intent.putExtra("id_image_single", id_imagen);
				startActivity(intent);
			}
		});
		
		no = (Button)findViewById(R.id.btnNoSingle);
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SingleLugaresConocidos.this, lugares_conocidos.class);
				intent.putExtra("bssid_final", bssid_final);
				startActivity(intent);
			}
		});
	}
	
	class LoadImagen extends AsyncTask<String, String, Bitmap>{
		@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(SingleLugaresConocidos.this);
    		pDialog.setMessage("Cargando la imagen");
    		pDialog.show();

    	}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub			
			String valor_imagen_id = params[0];
			List<NameValuePair> parametros = new ArrayList<NameValuePair>();
			parametros.add(new BasicNameValuePair(TAG_ID_IMAGEN, valor_imagen_id));
			JSONObject jsonWifi = JParser.makeHttpRequest(url_imagen_single, "POST", parametros);
			try{
				int success = jsonWifi.getInt(TAG_SUCCESS);
				if(success == 1){
					path_receive = jsonWifi.getJSONArray(TAG_IMAGEN);
					for (int i = 0; i< path_receive.length(); i++){
						JSONObject c = path_receive.getJSONObject(i);
						path_imagen = c.getString(TAG_PATH_IMAGEN);
						try{
							bitmap_Single = BitmapFactory.decodeStream((InputStream)new URL(path_imagen).getContent());
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}else{
					//Hubo error
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
			return bitmap_Single;
		}
		@Override
		protected void onPostExecute(Bitmap image){
			if(image != null){
				imgSingle.setImageBitmap(image);
				pDialog.dismiss();
			}else{
				pDialog.dismiss();
				Toast.makeText(SingleLugaresConocidos.this, "Error en la url", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
