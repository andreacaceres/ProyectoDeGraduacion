package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.Toast;

public class imagen_triangulada extends Activity{
	static IP dir_ip = new IP();
	static String url = dir_ip.getIp();
	
	public int H = 91;
	public int W = 98;
	public float h = (float)57.58;
	public float w = (float)62.45;
	public int Hpx = 2176;
	public int Wpx = 2360;
	public int width_imagen = 0;
	public int height_imagen = 0;
	public int w_marcador = 250;
	public int h_marcador = 180;
	
	private static String url_formula = url+"WebService/formulas.php";
	private static final String TAG_COORDENADAS = "coordenadas";
	private static final String TAG_X_FINAL = "x_final";
	private static final String TAG_Y_FINAL = "y_final";
	JSONArray coordenadas = null;
	private ProgressDialog pDialog;
	JSONParser JParser = new JSONParser();
	private static final String TAG_SUCCESS = "success";
	
//	public int x_calculado = 65;
//	public int y_calculado = 165;
	
	public String code;
	public String descripcion;
	
	public int x_resultado, y_resultado, xfinal, yfinal = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.triangulacion_imagen);
		Bundle bundle = getIntent().getExtras();
		int x_calculado = bundle.getInt("x_calculado");
		int y_calculado = bundle.getInt("y_calculado");
		
		//new calculo_coordenadas().execute();
		
		final ImageView image = (ImageView)findViewById(R.id.imageTriangulada);
		Display display = getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		int displayHeight = display.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.imagen_triangulada_horizontal, options);
		int width = options.outWidth;
		if (width > displayWidth){
			int widthRatio = Math.round((float)width/(float)displayWidth);
			options.inSampleSize = widthRatio;
		}
		options.inJustDecodeBounds = false;
		final Bitmap scaledBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagen_triangulada_horizontal, options);
		final Bitmap prueba = scaleToActualAspectRatio(scaledBitmap, displayWidth, displayHeight);
		image.setImageBitmap(prueba);
		width_imagen = prueba.getWidth();
		height_imagen = prueba.getHeight();
		Bitmap imageBitmap = prueba.copy(Bitmap.Config.ARGB_8888, true);
		Log.v("W en imagen test: ",":"+prueba.getWidth());
		Log.v("H en imagen test: ",":"+prueba.getHeight());
		Canvas canvas = new Canvas(imageBitmap);
		
		//calculado para poner el marcador:
		int x_1 = ((243-y_calculado)-36);
		int y_1 = (91-x_calculado);
				
		x_resultado = convertidor_x(x_1, w, W, Wpx, width_imagen);
//		xfinal = x_resultado-125;
		xfinal = x_resultado;
		
		y_resultado = convertidor_y(y_1, h, H, Hpx, height_imagen);
//		yfinal = y_resultado-90;
		yfinal = y_resultado-10;
		
		Log.v("X final en pixeles: ",""+x_resultado);
		Log.v("Y final en pixeles: ",""+y_resultado);
		
		//Marcador
//		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.aproximado);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.tag);
		canvas.drawBitmap(bm, xfinal, yfinal, null);
		image.setImageBitmap(imageBitmap);
	}
	
	// Function to resize the final image
	public Bitmap scaleToActualAspectRatio(Bitmap bitmap, int deviceWidth, int deviceHeight) {
		if (bitmap != null) {
			boolean flag = true;
			int bitmapHeight = bitmap.getHeight();
			int bitmapWidth = bitmap.getWidth(); 
			if (bitmapWidth > deviceWidth) {
				flag = false;
				int scaledWidth = deviceWidth;
				int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;
				try {
					if (scaledHeight > deviceHeight)
						scaledHeight = deviceHeight;
					bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,scaledHeight, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
	 		}
	 		if (flag) {
	 			if (bitmapHeight > deviceHeight) {
	 				int scaledHeight = deviceHeight;
	 				int scaledWidth = (scaledHeight * bitmapWidth)/ bitmapHeight;
	 				try {
	 					if (scaledWidth > deviceWidth)
	 						scaledWidth = deviceWidth;
	 					bitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,scaledHeight, true);
	 				} catch (Exception e) {
	 					e.printStackTrace();
	 				}
	 			}
	 		}
	 	}
	 	return bitmap;
	 }
	
	public int convertidor_x(int x2, float w, int W, int Wpx, int width_imagen){
		float x_cm, x_px = 0;
		int x_final;
		x_cm = (x2*w)/W;
		Log.v("En centimetros X", ""+x_cm);
		x_px = (x_cm*Wpx)/w;
		Log.v("En Pixeles X", ""+x_px);
		x_final = (int)((width_imagen*x_px)/Wpx);
		return x_final;
	}
	
	public int convertidor_y(int y2, float h, int H, int Hpx, int height_imagen){
		float y_cm, y_px = 0;
		int y_final;
		y_cm = (y2*h)/H;
		Log.v("En centimetros Y", ""+y_cm);
		y_px = (y_cm*Hpx)/h;
		Log.v("En Pixeles Y", ""+y_px);
		y_final = (int)((height_imagen*y_px)/Hpx);
		return y_final;
	}
	
	/*class calculo_coordenadas extends AsyncTask<String, String, String>{
    	@Override
		protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(imagen_triangulada.this);
    		pDialog.setMessage("Calculando coordenadas...");
    		pDialog.show();
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			JSONObject json = JParser.makeHttpRequest(url_formula, "POST", param);
			Log.v("Formulas", json.toString());
			try {
				int success = json.getInt(TAG_SUCCESS);
				if(success == 1){
					coordenadas = json.getJSONArray(TAG_COORDENADAS);		
					JSONObject c = coordenadas.getJSONObject(0);
					x_calculado = c.getInt(TAG_X_FINAL);
					y_calculado = c.getInt(TAG_Y_FINAL);
					Log.v("x_calculado es: ",""+x_calculado);
					Log.v("y_calculado es: ",""+y_calculado);
				}else{
					//Toast.makeText(imagen_triangulada.this, "Volviendo a la pantalla anterior.", Toast.LENGTH_LONG).show();
					Handler handler =  new Handler(imagen_triangulada.this.getMainLooper());
				    handler.post( new Runnable(){
				        @Override
						public void run(){
							Toast.makeText(imagen_triangulada.this, "Se regreso a esta pantalla debido a que los valores de las coordenadas no son validas.", Toast.LENGTH_LONG).show();
				        	//showAlertDialog(imagen_triangulada.this, "Error", "Volviendo a la pantalla anterior para obtener nuevos datos.", false);
				        }
				    });
					Intent solo = new Intent(imagen_triangulada.this, pantalla_haciaDondeIr.class);
					startActivity(solo);
					finish();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		@Override
		protected void onPostExecute(String image){
			pDialog.dismiss();
		}
    }
	
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog.show();
    }*/
}
