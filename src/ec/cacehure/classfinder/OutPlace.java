package ec.cacehure.classfinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class OutPlace extends Activity{
	// Constantes
	public int Ancho_real = 26400;
	public int Alto_real = 24300;
	public float Ancho_digital = (float) 71.42;
	public float Alto_digital = (float)62.53;
	public int w_digital = 2699;
	public int h_digital = 2363;
	public float W_real = (float)997670.1204;
	public float Y_real = (float)918293.6191;
	public int width_device = 0;
	public int height_device = 0;
	int x1,y1,x2,y2;
	public int x_final = 0;
	public int y_final = 0;
	public int x_final_destino = 0;
	public int y_final_destino = 0;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_image_map);
		final ImageView image = (ImageView)findViewById(R.id.thumbImage);
		Display display = getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		int displayHeight = display.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.mapa, options);
		int width = options.outWidth;
		if (width > displayWidth){
			int widthRatio = Math.round((float)width/(float)displayWidth);
			options.inSampleSize = widthRatio;
		}
		options.inJustDecodeBounds = false;
		final Bitmap scaledBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa, options);
		final Bitmap prueba = scaleToActualAspectRatio(scaledBitmap, displayWidth, displayHeight);
//		image.setImageBitmap(scaledBitmap);
		image.setImageBitmap(prueba);
		Log.v("Ancho de la image: ","es: "+prueba.getWidth());
		Log.v("Alto de la image: ","es: "+prueba.getHeight());
		width_device = prueba.getWidth();
		height_device = prueba.getHeight();
		
		Bundle bundle = getIntent().getExtras();
		x1 = bundle.getInt("x1");
		y1 = bundle.getInt("y1");
		x2 = bundle.getInt("x2");
		y2 = bundle.getInt("y2");
		
		int x_calculado =  x1;
		int y_calculado = y1;
		int x_destino = x2;
		int y_destino = y2;
		
		x_final = convertidor_x(x_calculado, W_real, Ancho_real, w_digital, width_device);
		y_final = convertidor_y(y_calculado, Y_real, Alto_real, h_digital, height_device);
		x_final_destino = convertidor_x(x_destino, W_real, Ancho_real, w_digital, width_device);
		y_final_destino = convertidor_y(y_destino, Y_real, Alto_real, h_digital, height_device);
		
		Log.v("x1 final: ","es: "+x_final);
		Log.v("y1 final: ","es: "+y_final);
		Log.v("x2 final destino: ","es: "+x_final_destino);
		Log.v("y2 final destino: ","es: "+y_final_destino);
		
		Bitmap imageBitmap = prueba.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(imageBitmap);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.marcador);
		canvas.drawBitmap(bm, (x_final-33), (y_final-98), null);
		canvas.drawBitmap(bm, (x_final_destino-33), (y_final_destino-98), null);
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
	
	public int convertidor_x(int x_calculado, float W_real, int Ancho_real, int w_digital, int width_device){
		x_calculado = x_calculado*100;
		float conv_x_1 = ((x_calculado*W_real)/Ancho_real);
		float conv_x_2 = ((w_digital*conv_x_1)/W_real);
		float conv_x_3 = ((width_device*conv_x_2)/w_digital);
		int x_final = (int) conv_x_3;
		return x_final;
	}
	public int convertidor_y(int y_calculado, float Y_real, int Alto_real, int h_digital, int height_device){
		y_calculado = y_calculado*100;
		float conv_y_1 = ((y_calculado*Y_real)/Alto_real);
		float conv_y_2 = ((h_digital*conv_y_1)/Y_real);
		float conv_y_3 = ((height_device*conv_y_2)/h_digital);
		int y_final = (int) conv_y_3;
		return y_final;
	}
}
