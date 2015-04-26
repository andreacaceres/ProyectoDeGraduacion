package ec.cacehure.classfinder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InPlace extends Activity{
	// Constantes
	public int Ancho_real = 26400;
	public int Alto_real = 24300;
	public float Ancho_digital = (float) 71.42;
	public float Alto_digital = (float)62.53;
	public int w_digital = 2712;//2699;
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
	
	int x_icono1 = 71;
	int y_icono1 = 88;
	int x_icono2 = 83;
	int y_icono2 = 138;
	int x_icono3 = 152;
	int y_icono3 = 125;
	int x_icono4 = 123;
	int y_icono4 = 182;
	int x_icono5 = 235;
	int y_icono5 = 185;
	int x_icono6 = 204;
	int y_icono6 = 124;
	int x_icono7 = 63;
	int y_icono7 = 118;
	int x_icono8 = 115;
	int y_icono8 = 113;
	
	Button especifico;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_image_map);
		especifico = (Button)findViewById(R.id.btn_referente);
		final ImageView image = (ImageView)findViewById(R.id.thumbImage);
		Display display = getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		int displayHeight = display.getHeight();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(getResources(), R.drawable.mapa_completo, options);//mapa
		int width = options.outWidth;
		if (width > displayWidth){
			int widthRatio = Math.round((float)width/(float)displayWidth);
			options.inSampleSize = widthRatio;
		}
		options.inJustDecodeBounds = false;
		final Bitmap scaledBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapa_completo, options);//mapa
		final Bitmap prueba = scaleToActualAspectRatio(scaledBitmap, displayWidth, displayHeight);
		image.setImageBitmap(prueba);
		width_device = prueba.getWidth();
		height_device = prueba.getHeight();
		
		image.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent ev) {
				LayoutInflater inflater = getLayoutInflater();
				View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
				ImageView image = (ImageView) layout.findViewById(R.id.imageViewLugares);
				image.setImageResource(R.drawable.temp_img);
				TextView text = (TextView) layout.findViewById(R.id.textViewLugares);
				text.setText("OntouchEvent!");
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				if((int)ev.getX() >= (x_icono1-33) && (int)ev.getX() < (x_icono1 + 33) && (int)ev.getY() >= (y_icono1-33) && ev.getY() < (y_icono1 + 33)){
					image.setImageResource(R.drawable.fiec_nueva);
					text.setText("FIEC NUEVA EDIFICIO 15");
					toast.show();
				}else if((int)ev.getX() >= (x_icono2-33) && (int)ev.getX() < (x_icono2 + 33) && (int)ev.getY() >= (y_icono2-50) && ev.getY() < (y_icono2 + 33)){
					image.setImageResource(R.drawable.fiec_vieja);
					text.setText("FIEC LABORATORIO COMPUTACION EDIFICIO 16C");
					toast.show();
				}else if((int)ev.getX() >= (x_icono3-43) && (int)ev.getX() < (x_icono3 + 33) && (int)ev.getY() >= (y_icono3-50) && ev.getY() < (y_icono3 + 33)){
					image.setImageResource(R.drawable.fiec_antiguo_decanato);
					text.setText("FIEC EDIFICIO 15");
					toast.show();
				}else if((int)ev.getX() >= (x_icono4-43) && (int)ev.getX() < (x_icono4 + 33) && (int)ev.getY() >= (y_icono4-70) && ev.getY() < (y_icono4)){
					image.setImageResource(R.drawable.fiec_vieja_lab_electronica);
					text.setText("FIEC LABORATORIO ELECTRONICA EDIFICIO 16AB");
					toast.show();
				}else if((int)ev.getX() >= (x_icono5-90) && (int)ev.getX() < (x_icono5) && (int)ev.getY() >= (y_icono5-90) && ev.getY() < (y_icono5 + 33)){
					image.setImageResource(R.drawable.fiec_vieja);
					text.setText("FIEC VIEJA EDIFICIO 24AB");
					toast.show();
				}else if((int)ev.getX() >= (x_icono6-90) && (int)ev.getX() < (x_icono6 + 33) && (int)ev.getY() >= (y_icono6-90) && ev.getY() < (y_icono6 + 33)){
					image.setImageResource(R.drawable.fiec_comedor);
					text.setText("COMEDOR DE LA FIEC");
					toast.show();
				}else if((int)ev.getX() >= (x_icono7-90) && (int)ev.getX() < (x_icono7 + 33) && (int)ev.getY() >= (y_icono7-90) && ev.getY() < (y_icono7 + 33)){
					image.setImageResource(R.drawable.fiec_parqueadero_profesore);
					text.setText("FIEC PARQUEADERO H2");
					toast.show();
				}else if((int)ev.getX() >= (x_icono8-90) && (int)ev.getX() < (x_icono8 + 33) && (int)ev.getY() >= (y_icono8-90) && ev.getY() < (y_icono8 + 33)){
					image.setImageResource(R.drawable.fiec_parqueadero_profesores);
					text.setText("FIEC PARQUEADERO H");
					toast.show();
				}
				return true;
			}
		});
		
		Bundle bundle = getIntent().getExtras();
		
		x1 = bundle.getInt("x1");
		y1 = bundle.getInt("y1");
		int x_calculado =  x1;
		int y_calculado = y1;
		x_final = convertidor_x(x_calculado, W_real, Ancho_real, w_digital, width_device);
		y_final = convertidor_y(y_calculado, Y_real, Alto_real, h_digital, height_device);
		
		Bitmap imageBitmap = prueba.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(imageBitmap);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.marcador_3);
		canvas.drawBitmap(bm, (x_final-33), (y_final-98), null);
		
		Bitmap icono_1 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_2 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_3 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_4 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_5 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_6 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_7 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		Bitmap icono_8 = BitmapFactory.decodeResource(getResources(), R.drawable.icono_info);
		
		x_icono1 = convertidor_x(x_icono1, W_real, Ancho_real, w_digital, width_device);
		y_icono1 = convertidor_y(y_icono1, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_1, (x_icono1-16), (y_icono1-16), null);
		
		x_icono2 = convertidor_x(x_icono2, W_real, Ancho_real, w_digital, width_device);
		y_icono2 = convertidor_y(y_icono2, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_2, (x_icono2-16), (y_icono2-16), null);
		
		x_icono3 = convertidor_x(x_icono3, W_real, Ancho_real, w_digital, width_device);
		y_icono3 = convertidor_y(y_icono3, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_3, (x_icono3-16), (y_icono3-16), null);
		
		x_icono4 = convertidor_x(x_icono4, W_real, Ancho_real, w_digital, width_device);
		y_icono4 = convertidor_y(y_icono4, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_4, (x_icono4-16), (y_icono4-16), null);
		
		x_icono5 = convertidor_x(x_icono5, W_real, Ancho_real, w_digital, width_device);
		y_icono5 = convertidor_y(y_icono5, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_5, (x_icono5-16), (y_icono5-16), null);
		
		x_icono6 = convertidor_x(x_icono6, W_real, Ancho_real, w_digital, width_device);
		y_icono6 = convertidor_y(y_icono6, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_6, (x_icono6-16), (y_icono6-16), null);
		
		x_icono7 = convertidor_x(x_icono7, W_real, Ancho_real, w_digital, width_device);
		y_icono7 = convertidor_y(y_icono7, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_7, (x_icono7-16), (y_icono7-16), null);
		
		x_icono8 = convertidor_x(x_icono8, W_real, Ancho_real, w_digital, width_device);
		y_icono8 = convertidor_y(y_icono8, Y_real, Alto_real, h_digital, height_device);
		canvas.drawBitmap(icono_8, (x_icono8-16), (y_icono8-16), null);
		
		
		especifico.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Bandera que indique si es el mismo lugar o dos lugares diferentes.
				
				/*Intent especifico = new Intent(OutPlace.this, lugares_especificos.class);
				especifico.putExtra("ubicacion_inicial", ubicacion_inicial);
				especifico.putExtra("ubicacion_final", ubicacion_final);
				startActivity(especifico);*/
			}
		});
	}

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