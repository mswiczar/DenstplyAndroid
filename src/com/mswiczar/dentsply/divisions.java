package com.mswiczar.dentsply;

import java.util.ArrayList;
import java.util.HashMap;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class divisions extends Activity {

    ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;
	OrderAdapter m_adapter;

	   @Override
	    public void onDestroy() {
	      super.onDestroy();
	    }

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.divisions);
        
        
        final ListView listRows = (ListView) findViewById(R.id.list);
        listRows.setItemsCanFocus(false);
        
        DentsplyApp app = (DentsplyApp) getApplication();


        
    	Cursor cursorFavorites = app.myDbHelper.createCursorDivisions();
    	cursorFavorites.moveToFirst();
    	HashMap<String,String> o;
    	
        app.aupdateMain.sendUsageData(app.storedVars,"2");

    	
 		while (!cursorFavorites.isAfterLast())
 		{
 			
 			o = new  HashMap<String,String>();
            o.put("div_id",""+ cursorFavorites.getInt(0));
            o.put("div_name",cursorFavorites.getString(1));
            o.put("div_priority",""+cursorFavorites.getInt(2));
            o.put("div_brands",cursorFavorites.getString(3));
            o.put("div_promotions",cursorFavorites.getString(4));
            o.put("div_promotion_start_date",""+ cursorFavorites.getInt(5));
            o.put("div_promotion_end_date",""+ cursorFavorites.getInt(6));
            o.put("div_ce_text",cursorFavorites.getString(7));
 			listItems.add(o);
 		 	cursorFavorites.moveToNext();
 		}
 		cursorFavorites.close();
        
        listRows.setOnItemClickListener(new OnItemClickListener() 
        {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3)
    				{
    					/*
    					 * 
    					 */
    			           DentsplyApp app = (DentsplyApp) getApplication();

    		                HashMap<String,String> o = listItems.get(arg2);
    		                if (o != null) 
    		                {
    		            		if (app.canDisplayPdf( divisions.this))
    		                	{
    		            			Log.v("Candisplay", "menuNews YES");
    		            			
    		            			Log.v("o.get(div_promotions)",o.get("div_promotions"));
    		            			app.openPDF(o.get("div_promotions"));
    		            			
    		                	}
    		            		else
    		            		{
    		            			
    		                		AlertDialog alertDialog;
    		                		alertDialog  = new AlertDialog.Builder(divisions.this).create();
    		                		alertDialog.setTitle("Dentsply");
    		                		alertDialog.setMessage("PDF viewer not available");
    		                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
    		                		public void onClick(DialogInterface dialog, int which) {

    		                		} }); 
    		                		alertDialog.show();

    		            			
    		            		} 

    		                	
    		                	
    		                	/*
    		                	
    		                	String url = "http://d4meadmin.com/dentsply/"+ o.get("div_promotions");
    		                	Intent i = new Intent(Intent.ACTION_VIEW);
    		                	i.setData(Uri.parse(url));
    		                	startActivity(i);
    		                	
    		                	*/
    		                }
    		                
    				}
    	  });        
        m_adapter = new OrderAdapter(this, R.layout.row, listItems);
   		listRows.setAdapter(m_adapter);        
        
    }

	 private class OrderAdapter extends ArrayAdapter<HashMap<String,String> > 
	 {
		 private ArrayList<HashMap<String,String> > items;
	        public OrderAdapter(Context context, int textViewResourceId, ArrayList<HashMap<String,String> > items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }
	      
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) 
	        {
	        		View v = convertView;
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.rowpromotions, null);
	                HashMap<String,String> o = items.get(position);
	                if (o != null) 
	               {
	                   TextView labelDivision = (TextView) v.findViewById(R.id.labelDivision);
	                   TextView labelPromotions = (TextView) v.findViewById(R.id.labelBrands);

	                   labelDivision.setText(o.get("div_name"));
	                   labelPromotions.setText(o.get("div_brands"));
	               }
	               return v;
	        }
	 }
}
