package ec.cacehure.classfinder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter_2 extends BaseAdapter{
	// Declare Variables
		Context context;
		LayoutInflater inflater;
		ArrayList<HashMap<String, String>> data;
		ImageLoader imageLoader;
		HashMap<String, String> resultp = new HashMap<String, String>();
	 
		public ListViewAdapter_2(Context context,
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
			ImageView flag;
	 
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View itemView = inflater.inflate(R.layout.lugares_especificos_thumbnail, parent, false);
			// Get the position
			resultp = data.get(position);
			descripcion = (TextView) itemView.findViewById(R.id.textDescripcionThum);
			// Locate the ImageView in listview_item.xml
			flag = (ImageView) itemView.findViewById(R.id.imageThumbEspe);
			descripcion.setText(resultp.get(lugares_conocidos.TAG_DESCRIPCION));
	 
			// Capture position and set results to the ImageView
			// Passes flag images URL into ImageLoader.class
			imageLoader.DisplayImage(resultp.get(lugares_conocidos.TAG_RUTA), flag);
			// Capture ListView item click
			return itemView;
		}
}
