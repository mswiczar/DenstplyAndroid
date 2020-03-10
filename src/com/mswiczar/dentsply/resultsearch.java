package com.mswiczar.dentsply;

import java.util.ArrayList;
import java.util.HashMap;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class resultsearch extends Activity {
	
	private String theZipSearch;
	Thread thread;
	ProgressDialog dialog;
	private Runnable runUpdate;

    ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;
	OrderAdapter m_adapter;

	   @Override
	    public void onDestroy() {
	      super.onDestroy();
	    }

	    private Runnable runEndUpdate = new Runnable() 
	    {
	        public void run() {
				m_adapter.notifyDataSetChanged();
	        	dialog.dismiss();
	        }
	    };
		
		private void getData()
		{
	        try
	        {
	            DentsplyApp app = (DentsplyApp) getApplication();
	            
	            
	            listItems.clear();
	            listItems.addAll(app.myDbHelper.getResultsFromSearch(theZipSearch));

	            app.aupdateMain.sendUsageData(app.storedVars);

	            runOnUiThread(runEndUpdate);
	        } 
	        catch (Exception e) 
	        { 
	            runOnUiThread(runEndUpdate);
	           Log.e("BACKGROUND_PROC", e.getMessage());
	        }
		}        

	   
	    @Override
	    protected void onStop(){
	    	if (thread!=null)
	    	{
	    	//	thread.stop();
	    	}
	    	if (dialog!= null)
	    	{
	    		dialog.dismiss();
	    	}
	        super.onStop();
	    }
			   
	   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     //   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
     //   StrictMode.setThreadPolicy(policy);

        
        setContentView(R.layout.resultsearch);
        
        
        final ListView listRows = (ListView) findViewById(R.id.list);
        listRows.setItemsCanFocus(false);
        
    	
    	
        Bundle extras = getIntent().getExtras();
        theZipSearch="";
        if(extras !=null)
        {
        	theZipSearch =  extras.getString("zip");
        }
    	
    	
        runUpdate = new Runnable()
        {
            public void run() {
                getData();
            }
        };

        thread =  new Thread(null, runUpdate, "MagentoBackground");
        thread.start();
        dialog = ProgressDialog.show(resultsearch.this, "Loading data", 
                "Please wait...", true);

        
        listRows.setOnItemClickListener(new OnItemClickListener() 
        {
    				public void onItemClick(AdapterView<?> arg0, View arg1,
    						int arg2, long arg3)
    				{
    					/*
    					 * 
    					 */
    		                HashMap<String,String> o = listItems.get(arg2);
    		                if (o != null) 
    		                {
    		                	Intent intent = new Intent(resultsearch.this, detailstore.class);
    		                		intent.putExtra("empid",o.get("empid") );
    		                		intent.putExtra("isfav","0" );
    		                	startActivity(intent);
    		                	
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
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);

	                HashMap<String,String> o = items.get(position);
	                
	               if (o != null) 
	               {
	                	
	                   ImageView imageRow = (ImageView) v.findViewById(R.id.Imageimage);	 
	                   TextView labelName = (TextView) v.findViewById(R.id.labelName);
	                   TextView labelDivision = (TextView) v.findViewById(R.id.labelDivision);
	                   TextView labelBrands = (TextView) v.findViewById(R.id.labelBrands);
	                   
	                   
	                   
	           
	                   
	                   DentsplyApp app = (DentsplyApp) getApplication();

	                   Bitmap abit = app.getBitmapFromAsset( resultsearch.this , "pictures/"+o.get("empid") +".png");
	                   if (abit != null)
	                   {
	                	   Log.v("Encontro "+ o.get("empid") +".png","Si");
	                   	   imageRow.setImageBitmap(abit);
	                   }
	                   else
	                   {
	                	   Log.v("Encontro "+o.get("empid") +".png","NO");

				                   String urlPicture  = "http://d4meadmin.com/dentsply/images/"+ o.get("empid") +".png";
				                   
				                    abit = app.urlToBitmap.get(urlPicture);
			                 	   if(abit==null)
			                 	   {
			                 		   abit = app.DownloadImage(urlPicture);
			                 		   if (abit!=null)
			                 		   {
			                 			  app.urlToBitmap.put(urlPicture,abit);
			                          	  imageRow.setImageBitmap(abit);
			                 			  
			                 		   }
			                 	   }
			                 	   else
			                 	   {
			                       	   imageRow.setImageBitmap(abit);
			                 		   
			                 	   }
	                   }
                 	   
	                   labelName.setText(o.get("emp_name"));
	                   labelDivision.setText(o.get("div_name") + "");
	                   labelBrands.setText(o.get("div_brands"));
	               }
	               return v;
	        }
	 }
	 
    
	
}
