package ec.cacehure.classfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Opciones extends Activity{
	
	Button general, especifico;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.opciones);
		
		Bundle bundle = getIntent().getExtras();
		final int x1 = bundle.getInt("x1");
		final int y1 = bundle.getInt("y1");
		final int x2 = bundle.getInt("x2");
		final int y2 = bundle.getInt("y2");
		final int ubicacion_inicial = bundle.getInt("ubicacion_inicial");
		final int ubicacion_final = bundle.getInt("ubicacion_final");
		
		general = (Button)findViewById(R.id.buttonGeneral);
		especifico = (Button)findViewById(R.id.buttonEspecifico);
		
		general.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent general = new Intent(Opciones.this, OutPlace.class);
				general.putExtra("x1", x1);
				general.putExtra("y1", y1);
				general.putExtra("x2", x2);
				general.putExtra("y2", y2);
				startActivity(general);
			}
		});
		
		especifico.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent especifico = new Intent(Opciones.this, lugares_especificos.class);
				especifico.putExtra("ubicacion_inicial", ubicacion_inicial);
				especifico.putExtra("ubicacion_final", ubicacion_final);
				startActivity(especifico);
			}
		});
	}

}
