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
		
		Bitmap imageBitmap = prueba.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(imageBitmap);
//		Paint p = new Paint();
//		p.setColor(Color.BLUE);
//		canvas.drawCircle(242, 296, 20, p);
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
	
	//Function to transforms the real coordenates
	public int convertidor(){
		return 1;
	}
}
