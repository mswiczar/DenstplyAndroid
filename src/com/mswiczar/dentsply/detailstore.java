package com.mswiczar.dentsply;





import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class detailstore extends Activity {
	
	private String idRep="0";
	
	private String Telefono;
	private String ApellidoNombre;
	private String Specialiy;
	private String Division;
	private String Email;
	private String Education;
	private String Promotions;
	private boolean isFavoritesView=false;
	
	private ArrayAdapter<String> listEmail;
	private ArrayList<String> listEmailString;


	
	private void addToFavorites()
	{
		DentsplyApp app = (DentsplyApp) getApplication();

        Cursor acur = app.myDbHelper.createCursorFavoriteExists(idRep);
        acur.moveToNext();
        
        Log.e("DB", acur.getString(0));
        int val = acur.getInt(0);
        
        if (val!=0)
        {
			  AlertDialog.Builder dialog = new AlertDialog.Builder(detailstore.this);
			  dialog.setTitle("Favorites");
			  dialog.setMessage(ApellidoNombre + " Exists in Favorites");
			  
		      dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		      {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		        	   dialog.cancel();
		           }
		       });
			   dialog.show();
        }
        else
        {
        	app.myDbHelper.insertIntoFavorites(idRep);
			
			  AlertDialog.Builder dialog = new AlertDialog.Builder(detailstore.this);
			  dialog.setTitle("Favorites");
			  dialog.setMessage(ApellidoNombre + " added to Favorites");
			  
		      dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		      {
		           public void onClick(DialogInterface dialog, int id) 
		           {
		        	   dialog.cancel();
		           }
		       });
			   dialog.show();
        }
	}
	
	
	
	
	private void call(String numero) {
	    try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:"+numero));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	        Log.e("iGPS", "Call failed", e);
	    }
	}


	private void SendSMS(String numero) 
	{
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + numero)));	
	}

	private void SendEmail(String stringEmailContents) 
	{
    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { Email } );
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, stringEmailContents);
        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}
	
	
	

	
	
	private void addToAddressBook()
	{
	
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

		intent.putExtra(ContactsContract.Intents.Insert.NAME, ApellidoNombre);
		intent.putExtra(ContactsContract.Intents.Insert.PHONE, Telefono);
		
		//intent.putExtra(ContactsContract.Intents.Insert.POSTAL, dire);

		startActivity(intent);
		
	
	}
	
	
	/*
	private void showInWWW(String url)
	{
	
	try {
        final Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(myIntent);
		} 
	catch (ActivityNotFoundException e) 
		{
        	Log.e("iGPS", "Map failed", e);
		}
	
	}
	*/
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
    	listEmailString = new ArrayList<String>();
    	listEmailString.add("D4Me Service Request");
    	listEmailString.add("D4Me Support Request");
    	listEmailString.add("D4Me Co-Travel Request");
    	listEmailString.add("D4Me Sales Lead Request");
    	
        Bundle extras = getIntent().getExtras(); 
        if(extras !=null)
        {
        	
        	idRep =  extras.getString("empid");
        	String fav =extras.getString("isfav"); 
        	isFavoritesView = fav.equals("1");
        	
        	if (isFavoritesView)
        	{
        		setContentView(R.layout.detailstorefavorites);
        	}
        	else
        	{
        		setContentView(R.layout.detailstore);
        		
        	}
        	
        	
        	
    		DentsplyApp app = (DentsplyApp) getApplication();

            Cursor acur = app.myDbHelper.CursorgetEmpid(idRep);
            acur.moveToFirst();
            
            ApellidoNombre = acur.getString(1);
        	Email          = acur.getString(3);
        	Telefono       = acur.getString(4);
        	Division       = acur.getString(7);
        			
        	Specialiy      = acur.getString(8);
        	Promotions     = acur.getString(9);
        	Education      = acur.getString(12);
        	
        	
        	
        	/*
        	
        	final ImageView imagePhoto = (ImageView) findViewById(R.id.imagePicture);
            String urlPicture  = "http://d4meadmin.com/dentsply/images/"+ idRep +".png";
            
            Bitmap abit = app.urlToBitmap.get(urlPicture);
      	    if(abit==null)
      	    {
      		   abit = app.DownloadImage(urlPicture);
      		   if (abit!=null)
      		   {
      			  app.urlToBitmap.put(urlPicture,abit);
            	   imagePhoto.setImageBitmap(abit);
      		   }
      	    }
      	    else
      	    {
          	   imagePhoto.setImageBitmap(abit);
      	    }
      	    
      	    
      	    */
        	final ImageView imagePhoto = (ImageView) findViewById(R.id.imagePicture);
        	

            Bitmap abit = app.getBitmapFromAsset( detailstore.this , "pictures/"+idRep +".png");
            if (abit != null)
            {
         	   Log.v("Encontro "+ idRep +".png","Si");
         	  imagePhoto.setImageBitmap(abit);
            }
            else
            {
         	   Log.v("Encontro "+idRep +".png","NO");

		                   String urlPicture  = "http://d4meadmin.com/dentsply/images/"+ idRep +".png";
		                   
		                    abit = app.urlToBitmap.get(urlPicture);
	                 	   if(abit==null)
	                 	   {
	                 		   abit = app.DownloadImage(urlPicture);
	                 		   if (abit!=null)
	                 		   {
	                 			  app.urlToBitmap.put(urlPicture,abit);
	                 			 imagePhoto.setImageBitmap(abit);
	                 			  
	                 		   }
	                 	   }
	                 	   else
	                 	   {
	                 		  imagePhoto.setImageBitmap(abit);
	                 		   
	                 	   }
            }
        	

        	
            final TextView textName = (TextView) findViewById(R.id.textName);
            textName.setText(ApellidoNombre);
            
            final TextView textSpecialist = (TextView) findViewById(R.id.textSpecialist);
            textSpecialist.setText(Specialiy);
            
            
            final TextView textDivision = (TextView) findViewById(R.id.textDivision);
            textDivision.setText(Division);
        	
            
            
        	final ImageView imageCallTo = (ImageView) findViewById(R.id.imageCall);
            imageCallTo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	call(Telefono);
                }
            });        	
            final TextView textCallTo = (TextView) findViewById(R.id.textCall);
            textCallTo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	call(Telefono);
                }
            });        	
            

        	final ImageView imageEmail = (ImageView) findViewById(R.id.imageEmail);
        	imageEmail.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	
                	AlertDialog.Builder builder = new AlertDialog.Builder(detailstore.this);
                	builder.setTitle("Please select subject.");
                	listEmail = new ArrayAdapter<String>(detailstore.this, android.R.layout.select_dialog_item, listEmailString);

                	
                	builder.setAdapter(listEmail, new DialogInterface.OnClickListener() {
                	    public void onClick(DialogInterface dialog, int item) {
                	    	String subject = listEmailString.get(item);
                	    	SendEmail(subject);
                	    }});
                	builder.show();
                	
                	
                }
            });    
        	
        	
            final TextView textEmail = (TextView) findViewById(R.id.textEmail);
            textEmail.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                 	AlertDialog.Builder builder = new AlertDialog.Builder(detailstore.this);
                	builder.setTitle("Please select subject.");
                	listEmail = new ArrayAdapter<String>(detailstore.this, android.R.layout.select_dialog_item, listEmailString);

                	
                	builder.setAdapter(listEmail, new DialogInterface.OnClickListener() {
                	    public void onClick(DialogInterface dialog, int item) {
                	    	String subject = listEmailString.get(item);
                	    	SendEmail(subject);
                	    }});
                	builder.show();
                	                }
            });        	

            
            
        	final ImageView imageSMS = (ImageView) findViewById(R.id.imageText);
        	imageSMS.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	SendSMS(Telefono);
                }
            });        	
            final TextView textSMS = (TextView) findViewById(R.id.textText);
            textSMS.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	SendSMS(Telefono);
                }
            });        	

            
            


            final TextView textWWW = (TextView) findViewById(R.id.textEducation);
            textWWW.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	
        			Log.v("Education", Education);

                	
            		DentsplyApp app = (DentsplyApp) getApplication();
                	
            		if (app.canDisplayPdf( detailstore.this))
                	{
            			Log.v("Candisplay", "YES");
            			app.openPDF(Education);
                    	app.aupdateMain.sendUsageData(app.storedVars,"3");
                	}
            		else
            		{
                		AlertDialog alertDialog;
                		alertDialog  = new AlertDialog.Builder(detailstore.this).create();
                		alertDialog.setTitle("Dentsply");
                		alertDialog.setMessage("PDF viewer not available");
                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int which) {

                		} }); 
                		alertDialog.show();
            		}                	
                	
                }
            });        	
            final ImageView imageWWW = (ImageView) findViewById(R.id.imageEducation);
            imageWWW.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	
        			Log.v("Education", Education);
                	
            		DentsplyApp app = (DentsplyApp) getApplication();
                	
            		if (app.canDisplayPdf( detailstore.this))
                	{
            			Log.v("Candisplay", "YES");
            			app.openPDF(Education);
                    	app.aupdateMain.sendUsageData(app.storedVars,"3");
            			
                	}
            		else
            		{
            			
                		AlertDialog alertDialog;
                		alertDialog  = new AlertDialog.Builder(detailstore.this).create();
                		alertDialog.setTitle("Dentsply");
                		alertDialog.setMessage("PDF viewer not available");
                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int which) {

                		} }); 
                		alertDialog.show();

                		
            			
            		}                	
                	
                	
                	
                }
            }); 
            
            
            final TextView textWWW2 = (TextView) findViewById(R.id.textPromotions);
            textWWW2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
        			Log.v("Promotions", Promotions);

                	DentsplyApp app = (DentsplyApp) getApplication();
                	
            		if (app.canDisplayPdf( detailstore.this))
                	{
            			Log.v("Candisplay", "YES promotions");
            			
            			
            			app.openPDF(Promotions);
                		app.aupdateMain.sendUsageData(app.storedVars,"2");

                	}
            		else
            		{
            			
                		AlertDialog alertDialog;
                		alertDialog  = new AlertDialog.Builder(detailstore.this).create();
                		alertDialog.setTitle("Dentsply");
                		alertDialog.setMessage("PDF viewer not available");
                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int which) {

                		} }); 
                		alertDialog.show();

            			
            		}

                	
                }
            });        	
            final ImageView imageWWW2 = (ImageView) findViewById(R.id.imagePromotions);
            imageWWW2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
        			Log.v("Promotions", Promotions);
                	
                	DentsplyApp app = (DentsplyApp) getApplication();
                	
            		if (app.canDisplayPdf( detailstore.this))
                	{
            			Log.v("Candisplay", "YES");
            			
            			app.openPDF(Promotions);
                		app.aupdateMain.sendUsageData(app.storedVars,"2");
            			
                	}
            		else
            		{
            			
                		AlertDialog alertDialog;
                		alertDialog  = new AlertDialog.Builder(detailstore.this).create();
                		alertDialog.setTitle("Dentsply");
                		alertDialog.setMessage("PDF viewer not available");
                		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                		public void onClick(DialogInterface dialog, int which) {

                		} }); 
                		alertDialog.show();

            			
            		}
                
                
                }
            });              
                       
            
            final TextView textADDC = (TextView) findViewById(R.id.textAddressBook);
            textADDC.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	addToAddressBook();
                }
            });        	
            final ImageView imageADDC = (ImageView) findViewById(R.id.imageAddressBook);
            imageADDC.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) 
                {
                	addToAddressBook();
                }
            });              
            
            
      
        	if (!isFavoritesView)
        	{
                final TextView textFAV = (TextView) findViewById(R.id.textFavorites);
                textFAV.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) 
                    {
                    	addToFavorites();
                    }
                });        	


                final ImageView imageFAV = (ImageView) findViewById(R.id.imageFavorites);
                imageFAV.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) 
                    {
                    	addToFavorites();
                    }
                });              
        	}
            

            
            
            
            
        }        
        
    }

}
