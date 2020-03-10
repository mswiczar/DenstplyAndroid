package com.mswiczar.dentsply;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class config extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        DentsplyApp app = (DentsplyApp) getApplication();
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


    public void sendPIN(View button) 
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
        		alertDialog  = new AlertDialog.Builder(config.this).create();
        		alertDialog.setTitle("Dentsply");
        		alertDialog.setMessage("Registration Success!");
        		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int which) {

        		} }); 
        		alertDialog.show();

    		}
    		else
    		{
        		AlertDialog alertDialog;
        		alertDialog  = new AlertDialog.Builder(config.this).create();
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
    		alertDialog  = new AlertDialog.Builder(config.this).create();
    		alertDialog.setTitle("Dentsply");
    		alertDialog.setMessage("Problem with PIN Code!");
    		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {

    		} }); 
    		alertDialog.show();

    		
    		//error
    	}    	
    }  
   
}