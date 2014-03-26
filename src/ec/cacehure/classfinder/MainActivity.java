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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Ejemplo para ver si esta o no conectado
        Button btnStatus = (Button)findViewById(R.id.begin);
        
        cd = new ConnectionDetector(getApplicationContext());
        btnStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests
					flag = 1;
                    showAlertDialog(MainActivity.this, "Internet Connection", "You have internet connection", true);
                } else {
                    // Internet connection is not present
                    // Ask user to connect to Internet
                	flag = 0;
                    showAlertDialog(MainActivity.this, "No Internet Connection", "You don't have internet connection.", false);
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
        //END
	}
	
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
          
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if(flag == 1){
            		//Go to another activity
                    Intent intent = new Intent(MainActivity.this, pantalla_haciaDondeIr.class);
                    startActivity(intent);
            	}else{
            		finish();
            	}
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
