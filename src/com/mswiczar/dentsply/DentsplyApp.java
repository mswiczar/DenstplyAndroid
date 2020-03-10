package com.mswiczar.dentsply;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;


public class DentsplyApp extends Application 
{
	public boolean updated=false;
	public boolean simpleList=true;
	public variablesPersistentes storedVars;
	
	public HashMap<String,Bitmap> urlToBitmap;
	public double the_latitude;
	public double the_longitude;
    ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;
	DataBaseHelper myDbHelper;

    
	public ArrayList<HashMap<String,String>> listCategories = new ArrayList<HashMap<String,String>>() ;
	ArrayAdapter<String> theAdapterCategories;
	ArrayList<String>    thearrCategoriesStr = new ArrayList<String>();


	public ArrayList<HashMap<String,String>> listMenu = new ArrayList<HashMap<String,String>>() ;

	public dbUpdate aupdateMain=null;
	
	
	public Bitmap defaultBitMap=null;
	
	
	
	public InputStream OpenHttpConnection(String urlString) 
		    throws IOException
		    {
		        InputStream in = null;
		        int response = -1;
		               
		        URL url = new URL(urlString); 
		        URLConnection conn = url.openConnection();
		         
		        if (!(conn instanceof HttpURLConnection))                     
		            throw new IOException("Not an HTTP connection");
		        
		        try{
		            HttpURLConnection httpConn = (HttpURLConnection) conn;
		            httpConn.setAllowUserInteraction(false);
		            httpConn.setInstanceFollowRedirects(true);
		            httpConn.setRequestMethod("GET");
		            httpConn.connect(); 

		            response = httpConn.getResponseCode();                 
		            if (response == HttpURLConnection.HTTP_OK) {
		                in = httpConn.getInputStream();                                 
		            }                     
		        }
		        catch (Exception ex)
		        {
		            throw new IOException("Error connecting");            
		        }
		        return in;     
		    }
			
	public Bitmap DownloadImage(String URL)
    {        
        Bitmap bitmap = null;
        InputStream in = null;        
        try {
            in = OpenHttpConnection(URL);
            if (in != null)
            {
            	bitmap =  BitmapFactory.decodeStream(in);
            	in.close();
            }
        } catch (IOException e1) {
            
            e1.printStackTrace();
        }
        return bitmap;                
    }		

	
	
	
	
	
	public  Bitmap getBitmapFromAsset(Context context, String strName) {

	    Bitmap bitmap = null;
	    try {
	    	
	    	InputStream myInput = context.getAssets().open(strName);

	        bitmap = BitmapFactory.decodeStream(myInput);
	    } catch (IOException e) {
	        return null;
	    }

	    return bitmap;
	}
	

	
	
	
	public void DownloadPDF(String URL,String filename)
    {        
		String newstrpdffilename = filename.replace(":","");

		Log.v("Download URL","ACA");
		Log.v("Download URL - URL ",URL);
		Log.v("Download URL - filename ",newstrpdffilename);
		Log.v("Download URL","fin ACA");
		
		
		
		//String localFilePath = root.getPath() + "/"+filename;
		String localFilePath = "/sdcard/dentsply/"+newstrpdffilename;
		Log.v("localFilePath",localFilePath);
		File file = new File(localFilePath);
		if(file.exists())
		{
			Log.v("existe", " -> "+localFilePath );
			return;
		}
		
        InputStream in = null;        
        try {
            in = OpenHttpConnection(URL);
            if (in != null)
            {
            	
            	try {
            		Log.v ("entro","entro");
            			try
            			{
            				File wallpaperDirectory = new File("/sdcard/dentsply/");
            				wallpaperDirectory.mkdirs();
            				wallpaperDirectory = new File("/sdcard/dentsply/promotions/");
            				wallpaperDirectory.mkdirs();
            				
            				wallpaperDirectory = new File("/sdcard/dentsply/CE/");
            				wallpaperDirectory.mkdirs();
                    	} 
            			catch (Exception e) 
            			{
            			
            			}
            		InputStream is = in; 
            		
            		
            		 File f=new File(localFilePath);
            		
            		 OutputStream out=new FileOutputStream(f);
            		
            		 
            		 
            		byte buf[]=new byte[8192];
            		  int len;
            		  while((len=is.read(buf))>0)
            		  out.write(buf,0,len);
            		  out.close();
            		  is.close();
            		
            		
            	} catch (Exception e) {
            		e.printStackTrace();
            	}            	
            	//bitmap =  BitmapFactory.decodeStream(in);
            	in.close();
            }
        } catch (IOException e1) {
            
            e1.printStackTrace();
        }
    }		
	
	
	
	public DentsplyApp ()
	{
		super();
        urlToBitmap = new HashMap<String,Bitmap>();
	}
	
	public static final String MIME_TYPE_PDF = "application/pdf";
	
	public  boolean canDisplayPdf(Context context) {
	    PackageManager packageManager = context.getPackageManager();
	    Intent testIntent = new Intent(Intent.ACTION_VIEW);
	    testIntent.setType(MIME_TYPE_PDF);
	    if (packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public boolean checkStored(String imagename)
	{
		
		File file = new File("/sdcard/dentsply/"+imagename);
		if(file.exists())
		{
			Log.v("existe", " -> "+ ""+imagename );
			return true;
		}

		
		return false;
	}
	
	
	public void openPDF(String strpdffilename)
	{
		
		String newstrpdffilename = strpdffilename.replace(":","");
		
		 File file = new File("/sdcard/dentsply/"+ newstrpdffilename);
		 Log.v("openPDF(String strpdffilename) ", "/sdcard/dentsply/"+ newstrpdffilename);
		 
		 PackageManager packageManager = getPackageManager();
		 
		 
		 Intent testIntent = new Intent(Intent.ACTION_VIEW);
		 testIntent.setType("application/pdf");
		 List<ResolveInfo> list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

		 if (list.size() > 0 && file.isFile()) 
		 {
			 Log.v("if (list.size() > 0 && file.isFile())  ", "SI " );
		     Intent intent = new Intent();
		     intent.setAction(Intent.ACTION_VIEW);
		     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     
		     Uri uri = Uri.fromFile(file);
		     intent.setDataAndType(uri, "application/pdf");
		     startActivity(intent);
		 }
		 else
		 {
			 Log.v("if (list.size() > 0 && file.isFile())  ", "NO " );
		 }
	}
	
	
	
	
	public void  getNews(String filename)
	{
			String url ="http://d4meadmin.com/dentsply/news/"+filename;
			DownloadPDF(url,filename);
	}
	
	public void  getAllPDF()
	{
		Cursor adivision = myDbHelper.getDivisionsCEAndPromotions();
		adivision.moveToFirst();
			String url;
			String filename;
			
    	Log.v("getAllPDF","Start");
 		while (!adivision.isAfterLast())
 		{
 			
 			Log.v("div id",adivision.getString(0));
 			Log.v("div promotion",adivision.getString(1));
 			Log.v("div ce",adivision.getString(2));

 			url= "http://d4meadmin.com/dentsply/"+adivision.getString(1);
 			Log.v("url",url);

 			filename= adivision.getString(1);
 			Log.v("filename",filename);
 			
 			DownloadPDF(url,filename);
 			
 			url= "http://d4meadmin.com/dentsply/"+adivision.getString(2);
 			Log.v("este url",url);
 			
 			filename= adivision.getString(2);
 			Log.v("este filename",filename);
 			DownloadPDF(url,filename);
 			
 			// div id
 			// div promotion
 			// div ce
 			
 			
            adivision.moveToNext();
 		}
 		adivision.close();
		
	    //return 	myDataBase.rawQuery("SELECT  div_id ,  div_promotions ,div_ce_text  FROM divisions order by div_priority ", null);
	}
	
	
}
