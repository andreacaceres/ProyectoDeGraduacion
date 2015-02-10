package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListViewAdapter extends BaseAdapter {
 
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
 
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
	}
 
	@Override
	public int getCount() {
		return data.size();
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView descripcion;
		TextView id;
		ImageView flag;
 
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View itemView = inflater.inflate(R.layout.listview_thumbnail, parent, false);
		// Get the position
		resultp = data.get(position);
 
		// Locate the TextViews in listview_item.xml
		id = (TextView)itemView.findViewById(R.id.textId);
		descripcion = (TextView) itemView.findViewById(R.id.descripcion);
 
		// Locate the ImageView in listview_item.xml
		flag = (ImageView) itemView.findViewById(R.id.imageThumbnail);
 
		// Capture position and set results to the TextViews
		id.setText(resultp.get(lugares_conocidos.TAG_ID));
		descripcion.setText(resultp.get(lugares_conocidos.TAG_DESCRIPCION));
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		imageLoader.DisplayImage(resultp.get(lugares_conocidos.TAG_RUTA), flag);
		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				// Get the position
				resultp = data.get(position);
				Log.v("Id","Enviada: "+resultp.get(lugares_conocidos.TAG_ID));
				Log.v("BSSID ADAPTER","Enviada: "+resultp.get(lugares_conocidos.TAG_BSSID_FINAL));
				Intent intent = new Intent(context, SingleLugaresConocidos.class);
				intent.putExtra("id", resultp.get(lugares_conocidos.TAG_ID));
				intent.putExtra("descripcion", resultp.get(lugares_conocidos.TAG_DESCRIPCION));
				intent.putExtra("bssid_final", resultp.get(lugares_conocidos.TAG_BSSID_FINAL));
				// Start SingleItemView Class
				context.startActivity(intent);
			}
		});
		return itemView;
	}
}
