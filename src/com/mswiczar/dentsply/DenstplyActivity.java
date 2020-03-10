package com.mswiczar.dentsply;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class DenstplyActivity extends Activity {
    /** Called when the activity is first created. */
	dbUpdate aupdate = new dbUpdate();
	
	Thread thread;
	ProgressDialog dialog;
	private Runnable runUpdate;
	private boolean showMenu=false;
	
    private Runnable runEndUpdate = new Runnable() 
    {
        public void run() 
        {
            DentsplyApp app = (DentsplyApp) getApplication();
            app.updated=true;
        	dialog.dismiss();
        }
    };
	
    @Override
    protected void onStop(){
    	if (thread!=null)
    	{
    		//thread.stop();
    	}
    	if (dialog!= null)
    	{
    		dialog.dismiss();
    	}
        super.onStop();
    }
	
	private void getUpdates()
	{
        try
        {
            DentsplyApp app = (DentsplyApp) getApplication();
        	aupdate.updateDb(app.myDbHelper ,app.storedVars);
        	app.getNews(app.storedVars.url_news);
        	app.getAllPDF();
   	        runOnUiThread(runEndUpdate);
        } 
        catch (Exception e) 
        { 
   	        runOnUiThread(runEndUpdate);
           Log.e("BACKGROUND_PROC", e.getMessage());
        }
	}        

    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	if (showMenu)
    	{
	    	MenuInflater inflater = getMenuInflater();
	    	inflater.inflate(R.menu.menumain, menu);
	    	return true;
    	}
    	else
    	{
    		return false;
    	}
    	
	 
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        DentsplyApp app = (DentsplyApp) getApplication();
    	
        switch (item.getItemId()) 
        {
		    	case R.id.menuSettings:
		    		Intent intentConfig = new Intent(DenstplyActivity.this, config.class);
		 			startActivity(intentConfig);
		    		return true;
		
		    	case R.id.menuNews:
		    		
            		if (app.canDisplayPdf( DenstplyActivity.this))
                	{
            			Log.v("Candisplay", "menuNews YES");
            			
            			Log.v("app.storedVars.url_news", app.storedVars.url_news);
            			app.openPDF(app.storedVars.url_news);
            			
                	}
            		else
            		{
            			
                		AlertDialog alertDialog;
                		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
                		alertDialog.setTitle("Dentsply");
                		alertDialog.setMessage("PDF viewer not available");
                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int which) {

                		} }); 
                		alertDialog.show();

            			
            		} 
		    		
//		            final Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://d4meadmin.com/dentsply/news/"+app.storedVars.url_news));
	//	            startActivity(myIntent);
		    		return true;
		
		        
		    	case R.id.menuPromo:
		    		Intent intentPromo = new Intent(DenstplyActivity.this, divisions.class);
		 			startActivity(intentPromo);
		    		return true;
		        
		        	case R.id.menuFavorites:
		        		Intent intentFavorites = new Intent(DenstplyActivity.this, favorites.class);
		     			startActivity(intentFavorites);
		        		return true;
		        	default:
		        		return super.onOptionsItemSelected(item);
        }
    } 
	
	
    
    public void normalUsage()
    {
        setContentView(R.layout.main);
        showMenu=true;
        
        DentsplyApp app = (DentsplyApp) getApplication();
        
        if (app.updated==false)
        {
	        runUpdate = new Runnable()
	        {
	            public void run() {
	                getUpdates();
	            }
	        };

	        thread =  new Thread(null, runUpdate, "MagentoBackground");
	        thread.start();
	        dialog = ProgressDialog.show(DenstplyActivity.this, "Updating database", 
	                "Loading data\nPlease wait...", true);
        }
        
    	final ImageView imageCallTo = (ImageView) findViewById(R.id.imageViewSearch);
        imageCallTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) 
            {
            	
            	EditText editText = (EditText) findViewById(R.id.editTextZIP);
            	String zip = editText.getText().toString();
            	 
            	 if (zip.equals(""))
            	 {
             		AlertDialog alertDialog;
            		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
            		alertDialog.setTitle("Dentsply");
            		alertDialog.setMessage("Please, insert a valid ZipCode");
            		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int which) {

            		} }); 
            		alertDialog.show();
            	 }
            	 else
            	 {
            		 try 
            		 {
            			  int myNum = Integer.parseInt(zip);
            		      DentsplyApp app = (DentsplyApp) getApplication();
            		      String localZip = ""+app.storedVars.user_zip;
            		      if (app.myDbHelper.isAdmin(app.storedVars.user_email))
            		      {
                       		  Intent intentResultSearch = new Intent(DenstplyActivity.this, resultsearch.class);
                     		  intentResultSearch.putExtra("zip",zip );
                     		  intentResultSearch.putExtra("zip1",myNum );
                 			  startActivity(intentResultSearch);
 
            		   	  }
            		      else
            		      {
	            			  int distance = app.myDbHelper.getDistance(localZip,zip );
	            			  if (distance ==-1)
	            			  {
	                        		AlertDialog alertDialog;
	                        		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
	                        		alertDialog.setTitle("Dentsply");
	                        		alertDialog.setMessage("Search not allowed");
	                        		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
	                        		public void onClick(DialogInterface dialog, int which) {
	
	                        		} }); 
	                        		alertDialog.show();
	            			  }
	            			  else
	            			  {
	                			  if (distance <= 250)
	                			  {
	                           		  Intent intentResultSearch = new Intent(DenstplyActivity.this, resultsearch.class);
	                         		  intentResultSearch.putExtra("zip",zip );
	                         		  intentResultSearch.putExtra("zip1",myNum );
	                     			  startActivity(intentResultSearch);
	                			  }
	                			  else
	                			  {
	                            		AlertDialog alertDialog;
	                            		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
	                            		alertDialog.setTitle("Dentsply");
	                            		alertDialog.setMessage("Search not allowed");
	                            		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
	                            		public void onClick(DialogInterface dialog, int which) {
	
	                            		} }); 
	                            		alertDialog.show();
	                			  }
	            			  }
            		      }

            		  } 
            		 catch (NumberFormatException nfe) 
            		 {
                  		AlertDialog alertDialog;
                		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
                		alertDialog.setTitle("Dentsply");
                		alertDialog.setMessage("Please, insert a valid ZipCode");
                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int which) {

                		} }); 
                		alertDialog.show();
            		} 
            		 
            	}

            }
        });        	
    }
    
    
    public void registeruser()
    {
        setContentView(R.layout.registration);

    }
    
    public void pinScreen()
    {
        setContentView(R.layout.pin);
    
    }
    
    
    
    
    
    public void sendEmailRegistration(View button) {  
    	try
    	{
    		

	    	
    		
    		if (aupdate.checkInternetConnection(this))
    		{
        		final EditText editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);  
        		final EditText editTextLastName  = (EditText) findViewById(R.id.editTextLastName);  
        		final EditText editTextPhone     = (EditText) findViewById(R.id.editTextPhone);  
        		final EditText editTextZip       = (EditText) findViewById(R.id.editTextZip);  
        		final EditText editTextEmail     = (EditText) findViewById(R.id.editTextEmail);  
        		final Spinner spinnerCompany     = (Spinner) findViewById(R.id.spinnerCompany);  
        	
        		String name="";
        		String lastName="";
        		String phone="";
        		String zip ="";
        		String email = "";
        		String company="";
        	
        		try 
        		{
        			name = URLEncoder.encode(editTextFirstName.getText().toString(), "UTF-8");  
        			lastName =URLEncoder.encode( editTextLastName.getText().toString(), "UTF-8");  
        			phone = URLEncoder.encode(editTextPhone.getText().toString(), "UTF-8");    	
        			zip = URLEncoder.encode(editTextZip.getText().toString(), "UTF-8");    	
        			email =URLEncoder.encode( editTextEmail.getText().toString(), "UTF-8");    	
        			company = URLEncoder.encode(spinnerCompany.getSelectedItem().toString(), "UTF-8");  
    			} 
        		catch (UnsupportedEncodingException e) 
        		{
        			e.printStackTrace();
        			
        		}
        		try
        		{
        			  int myNum = Integer.parseInt(zip);
              		  Log.v("theStrToCall",""+myNum);

        		}
        		catch (Exception e) 
        		{
            		AlertDialog alertDialog;
            		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
            		alertDialog.setTitle("Dentsply");
            		alertDialog.setMessage("Invalid ZIPCODE.");
            		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            		public void onClick(DialogInterface dialog, int which) {

            		} }); 
            		alertDialog.show();
            			return;
        		}

        		
        		
        		
        		String urlRegistration = "http://d4meadmin.com/dentsply/reigsterUserNoEmail.php?Email="+email+"&FirstName="+name+"&LastName="+lastName+"&UserZip="+zip+"&UserCompany="+company+"&phone="+phone;
        		Log.v("theStrToCall",urlRegistration);

    	    	XmlReader axmlreader =  new XmlReader();
    	    	ArrayList <HashMap<String,String>> theArray = new ArrayList<HashMap<String,String>>();
    	    	axmlreader.RssParserPull(urlRegistration);
    	    	axmlreader.str_Name ="USER";
    	    	theArray.addAll(axmlreader.parse());
    	    	
    	    	if (theArray.size() != 0)
    	    	{
    	    		String OK = theArray.get(0).get("OK");
    	    		if (OK.equals("1"))
    	    		{
    	    	        DentsplyApp app = (DentsplyApp) getApplication();
    	    	        String emailcomp =editTextEmail.getText().toString();
    	    			if (app.myDbHelper.ispreferredEmail(emailcomp))
    	    			{
    	        	        app.storedVars.isSuperUser=0;
    	        	        app.storedVars.user_email = email;
    	        	        int myNum = Integer.parseInt(zip);
    	        	        app.storedVars.user_zip = myNum;
    	        	        app.storedVars.status=2;
    	        	        app.myDbHelper.setDatosToDB(app.storedVars);
    	        	        normalUsage();
    	    			}
    	    			else
    	    			{
    	        	    	String stringEmailContents ="OK"+
    	        	    			"Name: "+name+"\nLastName: "+lastName+"\nPhone: "+phone+
    	        	    			"\nCompany: "+company +"\nemail: " +email +
    	        	    			"\nZip: "+zip ;
    	        	    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
    	        	        emailIntent.setType("plain/text");
    	        	        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "skumar@cybernetconsulting.com" } );
    	        	        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dentsply Registration Request");
    	        	        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, stringEmailContents);
    	        	        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    	        	        app.storedVars.isSuperUser=0;
    	        	        app.storedVars.user_email = email;
    	        	        int myNum = Integer.parseInt(zip);
    	        	        app.storedVars.user_zip = myNum;
    	        	        app.storedVars.status=1;
    	        	        app.myDbHelper.setDatosToDB(app.storedVars);
    	        	        pinScreen();
    	    			}
    	    			/// save registration
    	    		}
    	    		else
    	    		{
    	    			// error
    	        		Log.v("arraysize: ","<> 1");

    	    		}
    	    	}
    	    	else
    	    	{
	        		Log.v("arraysize: ","0");
    	    		//error
    	    	}  
    			
    			
    		}
    		else
    		{
        		AlertDialog alertDialog;
        		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
        		alertDialog.setTitle("Dentsply");
        		alertDialog.setMessage("No Internet Connection! Check your internet connection.");
        		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {

        		} }); 
        		alertDialog.show();

    		}
    	}catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }  

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        DentsplyApp app = (DentsplyApp) getApplication();
        app.aupdateMain = aupdate;

        
        if (app.myDbHelper==null)
        {
	    	app.myDbHelper = new DataBaseHelper(this);
	        try 
	        {
	        	app.myDbHelper.createDataBase();
	        } 
	        catch (IOException ioe) 
	        {
	        	throw new Error("Unable to create database");
	        }
	        app.myDbHelper.openDataBase();
	        app.storedVars = app.myDbHelper.getDatosFromDB();
        }
        if (app.storedVars.user_email.equals(""))
        {
        	registeruser();
        	
        }
        else
        {
        	if (app.storedVars.status==2)
        	{
        		normalUsage();
        	}
        	else
        	{
        		pinScreen();
        	}
        }
        
    }
    
    
    
    public void sendPIN(View button) 
    {
    	try
    	{
    	final EditText editTextPIN = (EditText) findViewById(R.id.editTextPIN);  
    	String thePIN = editTextPIN.getText().toString();
		String  theStrToCall;
		String theEmail="";
		try {
			theEmail = URLEncoder.encode("mio@mwdental.com", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		theStrToCall ="http://d4meadmin.com/dentsply/verifyPin.php?theEmail="+theEmail+"&thePIN="+thePIN ;
		Log.v("theStrToCall",theStrToCall);
    	XmlReader axmlreader =  new XmlReader();
    	ArrayList <HashMap<String,String>> theArray = new ArrayList<HashMap<String,String>>();
		axmlreader.RssParserPull(theStrToCall);

    	axmlreader.str_Name ="USER";
    	theArray.addAll(axmlreader.parse());
    	if (theArray.size() != 0)
    	{
    		String OK = theArray.get(0).get("OK");
    		if (OK.equals("1"))
    		{
    			/// save registration
        		AlertDialog alertDialog;
        		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
        		alertDialog.setTitle("Dentsply");
        		alertDialog.setMessage("Registration Success!");
        		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {

        		} }); 
        		alertDialog.show();
                DentsplyApp app = (DentsplyApp) getApplication();
    	        app.storedVars.status=2;
    	        app.myDbHelper.setDatosToDB(app.storedVars);

        		normalUsage();

    		}
    		else
    		{
        		AlertDialog alertDialog;
        		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
        		alertDialog.setTitle("Dentsply");
        		alertDialog.setMessage("Problem with PIN Code!");
        		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {

        		} }); 
        		alertDialog.show();
    			// error
    		}
    	}
    	else
    	{
    		
    		
    		AlertDialog alertDialog;
    		alertDialog  = new AlertDialog.Builder(DenstplyActivity.this).create();
    		alertDialog.setTitle("Dentsply");
    		alertDialog.setMessage("Problem with PIN Code!");
    		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {

    		} }); 
    		alertDialog.show();

    		
    		//error
    	}
    } catch (Exception e)
    {
    }
    	
    }  
    
}