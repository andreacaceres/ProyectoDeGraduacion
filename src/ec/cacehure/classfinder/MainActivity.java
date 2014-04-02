package ec.cacehure.classfinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Boolean isInternetPresent = false;
    ConnectionDetector cd;
    public int flag = 0;
    Button btn_salir;
    Button btnStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        cd = new ConnectionDetector(getApplicationContext());
        
		btnStatus = (Button)findViewById(R.id.begin);
        btnStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
                    // La conexión a internet esta presente
                    // hace un HTTP Request
					//flag = 1;
					Intent intent = new Intent(MainActivity.this, pantalla_haciaDondeIr.class);
                    startActivity(intent);
                    //showAlertDialog(MainActivity.this, "Conexión a Internet", "Usted esta conectado a la red", true);
                } else {
                    // La conexión a internet no esta presente
                    // Se le solicita al usuario que se conecte por medio Wi-Fi al Internet
                	//flag = 0;
                    showAlertDialog(MainActivity.this, "No conexión a Internet", "Usted no está conectado a Internet.", false);
                }
			}
		});
        
        btn_salir = (Button)findViewById(R.id.exit);
        btn_salir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Titulo del mensaje
        alertDialog.setTitle(title);
 
        // Descripcion del mensaje
        alertDialog.setMessage(message);
          
        // Boton OK
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	/*if(flag == 1){
            		//Si es true, se va a otra pantalla
                    Intent intent = new Intent(MainActivity.this, pantalla_haciaDondeIr.class);
                    startActivity(intent);
            	}else{
            		finish();
            	}*/
            }
        });
        // Mostrando mensaje de alerta
        alertDialog.show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
