package ec.cacehure.classfinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class imagen_triangulada extends Activity{
	
	public int H = 98;
	public int W = 91;
	public float h = (float)62.45;
	public float w = (float)57.58;
	public int Hpx = 2360;
	public int Wpx = 2176;
	public int width_imagen = 0;
	public int height_imagen = 0;
	public int w_marcador = 250;
	public int h_marcador = 180;
	
	public int x_calculado;
	public int y_calculado;
	
	public int x_resultado, y_resultado, xfinal, yfinal = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.triangulacion_imagen);
		Bundle bundle = getIntent().getExtras();
		x_calculado = Integer.valueOf(bundle.getString("coord_x"));
		y_calculado = Integer.valueOf(bundle.getString("coord_y"));
		
		final ImageView image = (ImageView)findViewById(R.id.imageTriangulada);
		Display display = getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		int displayHeight = display.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.fiec, options);
		int width = options.outWidth;
		if (width > displayWidth){
			int widthRatio = Math.round((float)width/(float)displayWidth);
			options.inSampleSize = widthRatio;
		}
		options.inJustDecodeBounds = false;
		final Bitmap scaledBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fiec, options);
		final Bitmap prueba = scaleToActualAspectRatio(scaledBitmap, displayWidth, displayHeight);
		image.setImageBitmap(prueba);
		width_imagen = prueba.getWidth();
		height_imagen = prueba.getHeight();
		Bitmap imageBitmap = prueba.copy(Bitmap.Config.ARGB_8888, true);
		Log.v("W en imagen test: ",":"+prueba.getWidth());
		Log.v("H en imagen test: ",":"+prueba.getHeight());
		Canvas canvas = new Canvas(imageBitmap);
		
		//calculado para poner el marcador:
		int x_1 = x_calculado;
		int y_1 = ((243-y_calculado)-36);
		
		x_resultado = convertidor_x(x_1, w, W, Wpx, width_imagen);
		xfinal = x_resultado-125;
		
		y_resultado = convertidor_y(y_1, h, H, Hpx, height_imagen);
		yfinal = y_resultado-90;
		
		Log.v("X final en pixeles: ",""+x_resultado);
		Log.v("Y final en pixeles: ",""+y_resultado);
		
		//Marcador
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.aproximado);
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

}
