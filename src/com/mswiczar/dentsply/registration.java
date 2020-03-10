package com.mswiczar.dentsply;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class registration extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
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
    
    
    public void sendEmailRegistration(View button) {  
    	final EditText editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);  
    	  
    	final EditText editTextLastName = (EditText) findViewById(R.id.editTextLastName);  
    	  
    	final EditText editTextPhone = (EditText) findViewById(R.id.editTextPhone);  
    	
    	final EditText editTextZip = (EditText) findViewById(R.id.editTextZip);  
    	
    	final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);  
    	
    	final Spinner spinnerCompany = (Spinner) findViewById(R.id.spinnerCompany);  
    	
    	String name="";
    	String lastName="";
    	String phone="";
    	String zip ="";
    	String email = "";
    	String company="";
    	

    	
		try {
	    	 name = URLEncoder.encode(editTextFirstName.getText().toString(), "UTF-8");  
	    	 lastName =URLEncoder.encode( editTextLastName.getText().toString(), "UTF-8");  
	    	 phone = URLEncoder.encode(editTextPhone.getText().toString(), "UTF-8");    	
	    	 zip = URLEncoder.encode(editTextZip.getText().toString(), "UTF-8");    	
	    	 email =URLEncoder.encode( editTextEmail.getText().toString(), "UTF-8");    	
	    	 company = URLEncoder.encode(spinnerCompany.getSelectedItem().toString(), "UTF-8");  
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
    			/// save registration
    		}
    		else
    		{
    			// error
    		}
    	}
    	else
    	{
    		//error
    	}    	
    }  
    
}