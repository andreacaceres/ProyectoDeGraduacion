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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleLugaresConocidos extends Activity{
	private ProgressDialog pDialog;
	Button yes,no;
	private static final String url_imagen_single = "http://200.126.19.93/WebService/lugares_conocidos_single.php";
//	private static final String url_imagen_single = "http://192.168.0.6/WebService/lugares_conocidos_single.php";
	private static final String TAG_ID_IMAGEN = "id_imagen";
	private static final String TAG_IMAGEN = "imagen";
	JSONParser JParser = new JSONParser();
	private ImageView imgSingle;
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PATH_IMAGEN = "ruta_imagen";
	JSONArray path_receive = null;
	private String path_imagen;
	TextView Text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlelugares);
		
		Bundle bundle = getIntent().getExtras();
		final String id_imagen = bundle.getString("id");
		String text_receive = bundle.getString("descripcion");
		
		LoadImagen receive = new LoadImagen();
		receive.execute(id_imagen);
		
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
				startActivity(intent);
			}
		});
	}
	
	class LoadImagen extends AsyncTask<String, String, String>{
		@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(SingleLugaresConocidos.this);
    		pDialog.setMessage("Cargando la imagen");
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();

    	}
		
		@Override
		protected String doInBackground(String... params) {
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
							imgSingle = (ImageView)findViewById(R.id.imageViewSingle);
							URL url = new URL(path_imagen);
							Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
							imgSingle.setImageBitmap(bitmap);
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
		@Override
		protected void onPostExecute(String file_url){
			pDialog.dismiss();
		}
		
	}

}
