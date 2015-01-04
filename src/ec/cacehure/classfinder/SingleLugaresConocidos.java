package ec.cacehure.classfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SingleLugaresConocidos extends Activity{
	
	Button yes,no;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.singlelugares);
		
		Bundle bundle = getIntent().getExtras();
		String id_imagen = bundle.getString("id");
		
		Log.v("Id recibido","es: "+id_imagen);
		
		yes = (Button)findViewById(R.id.btnYesSingle);
		yes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SingleLugaresConocidos.this, cursos.class);
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

}
