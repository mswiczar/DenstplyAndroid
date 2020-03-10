package com.mswiczar.dentsply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.mswiczar.dentsply/databases/";
    private static String DB_NAME = "dentsply8.sql";
    public SQLiteDatabase myDataBase; 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    
    public DataBaseHelper(Context context) {
 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		
  //  		Log.v("DB","dbExistes");
    		//do nothing - database already exist
    	}else{
 
 //   		Log.v("DB","NO EXISTE");

    		//By calling this method and empty database will be created into the default system path
              //of your application so we are gonna be able to overwrite that database with our database.
        	//this.getReadableDatabase();
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		
    		
    	//	Log.v("DB",myPath);
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    	//	Log.v("DB","Close DB ==NULL");
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);

    	
    	//Log.v("DB",DB_PATH);
    	
    	
    	(new File(DB_PATH)).mkdir();

    	
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
        try
        {
        	
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}catch(SQLiteException e){
		 
		//database does't exist yet.

	}
    	
    }
 
    
    
    @Override
	public synchronized void close() 
    {
    	if(myDataBase != null)
    	{
    		    myDataBase.close();
    	}
    	super.close();
 
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
 	
	/*
		sqlite> .schema
		CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US');
		CREATE TABLE datacvs (id integer primary key autoincrement, position varchar(255) , zip varchar(64) , region varchar(32) , name varchar(255) , cell varchar(64) , office varchar(64) , email varchar(64));
		CREATE TABLE datos (fecha_division integer , fecha_reps integer, url_news varchar(255) , user_email varchar(64) , user_zip integer, installstatus integer, issuper integer);
		CREATE TABLE divisions (div_id integer, div_name varchar(128) , div_priority integer, div_brands text, div_promotions text , div_promotion_start_date integer , div_promotion_end_date integer , div_ce_text text , last_mod_date integer);
		CREATE TABLE favorites (empid integer);
		CREATE TABLE reps (empid integer, emp_name varchar(128), division_id integer , email varchar(128) , phone varchar(64) , zip text, imageurl varchar(255));
		CREATE TABLE zipResults (STATE varchar(2) ,Zip_from varchar(3) ,Zip_to varchar(3) ,DIST_MILES DECIMAL(10,5));
		sqlite> 
	 */
	
	
	
    public variablesPersistentes getDatosFromDB () 
    {
    	variablesPersistentes salida = new variablesPersistentes();
    	
    	Cursor datos =  myDataBase.rawQuery("select fecha_division  , fecha_reps , url_news  , user_email  , user_zip, installstatus, issuper  from datos", null);
    	datos.moveToFirst();

 		if  (!datos.isAfterLast())
 		{
        	salida.fecha_division =datos.getLong(0);
        	salida.fecha_reps=datos.getLong(1);
        	salida.url_news=datos.getString(2);
        	salida.user_email=datos.getString(3);
        	salida.user_zip=datos.getInt(4);
        	salida.status=datos.getInt(5);
        	salida.isSuperUser=datos.getInt(6);
 		}    	
    	return salida;
    	
    }
	
    public void setDatosToDB (variablesPersistentes thevar)
    {
		String sql = "delete from datos";
		myDataBase.execSQL(sql);

		sql = "insert into datos ( fecha_division  , fecha_reps , url_news  , user_email  , user_zip, installstatus, issuper)  ";
		sql = sql + " values (";
		sql = sql + thevar.fecha_division+",";
		sql = sql + thevar.fecha_reps+",";
		sql = sql + "'"+thevar.url_news+"',";
		sql = sql + "'"+thevar.user_email+"',";
		sql = sql +thevar.user_zip+" , ";
		sql = sql +thevar.status+" , ";
		sql = sql +thevar.isSuperUser;
		sql = sql + ")";
		Log.v("sql",sql);
		
		myDataBase.execSQL(sql);

    }
    
    
    
    
	
	
    public Cursor createCursorFavorites () 
    {
    	return 	myDataBase.rawQuery("select favorites.empid , emp_name ,division_id , email ,phone, zip,imageurl,div_name,div_brands,div_promotions,div_promotion_start_date,div_promotion_end_date,div_ce_text FROM reps , divisions,favorites where favorites.empid =reps.empid and reps.division_id=divisions.div_id ", null);
    }
    
    public Cursor createCursorFavoriteExists (String empid) 
    {
        Log.e("DB","select count(*) as cantidad from favorites where empid = "+empid);
    	return 	myDataBase.rawQuery("select count(*) as cantidad from favorites where empid = "+empid, null);
    }
	
    
    public int insertIntoFavorites (String empid)
    {
		String sql = "insert into favorites (empid) values('"+empid+"')";
		myDataBase.execSQL(sql);
		return 1;
    }

	public void deleteAllDataCVS()
	{
		String sql = "delete from datacvs";
		myDataBase.execSQL(sql);
	}
	
	
	
	public int getCount()
	{
		int salida =0;
		Cursor acursor;
		acursor =  	myDataBase.rawQuery("SELECT  count(*) as cantidad from datacvs", null);
		acursor.moveToFirst();
		salida= acursor.getInt(0);
		acursor.close();
		return salida;
	}
	
	
	
////////////divisions
	
	
	public  void deleteAllDataDivision()
	{
		String sql = "delete from divisions";
		myDataBase.execSQL(sql);
	};

	
	
	
	public  void insertRowDivisions( HashMap<String,String> theDictRow )
	{
		try
		{
		    
			SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 

			String str =  "INSERT INTO divisions (div_id, div_name , div_priority, div_brands ,div_promotions ,div_promotion_start_date ,div_promotion_end_date,div_ce_text,last_mod_date )";
			str = str + " values ( ";
			str = str +  theDictRow.get("DivisionId").replaceAll("'", " ")            + ",";
			str = str +  "'"+ theDictRow.get("DivisionName").replaceAll("'", " ")            + "',";
			str = str +  theDictRow.get("Priority").replaceAll("'", " ")            + ",";
			str = str +  "'"+theDictRow.get("Brands").replaceAll("'", " ")            + "',";
			str = str +  "'"+theDictRow.get("PromotionText").replaceAll("'", " ")            + "',";
			
			Date dateObj;
			SimpleDateFormat curFormater2 = new SimpleDateFormat("yyyyMMdd"); 

		    if (theDictRow.get("PromotionStartDate").equals(""))
		    {
				str = str    + "0,";
		    }
		    else
		    {
				dateObj = curFormater.parse(theDictRow.get("PromotionStartDate"));
				String newDateStr = curFormater2.format(dateObj); 
				str = str +        newDateStr   + ",";

			 //   Log.v("PromotionStartDate",theDictRow.get("PromotionStartDate"));
		    }

			
		    
			
		    if (theDictRow.get("PromotionEndDate").equals(""))
		    {
				str = str    + "0,";

		    }
		    else
		    {
			    dateObj = curFormater.parse(theDictRow.get("PromotionEndDate")); 
			    String	newDateStr = curFormater2.format(dateObj); 
				str = str +        newDateStr   + ",";
		    }

			
			
			str = str +  "'"+ theDictRow.get("CEtext").replaceAll("'", " ")            + "',";
			
		    if (theDictRow.get("LastModifiedDate").equals(""))
		    {
				str = str    + "0";

		    }
		    else
		    {
			    dateObj = curFormater.parse(theDictRow.get("LastModifiedDate")); 
			    String newDateStr = curFormater2.format(dateObj); 
				str = str +        newDateStr  ;
		    }
 
			
			
			str = str + ")";
			
		   // Log.v("DB",str);
			myDataBase.execSQL(str);
		}
		 catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return;
	};
	
	
	public  void deleteAllDataRep()
	{
			String sql = "delete from reps";
			myDataBase.execSQL(sql);
			
		
	}

	
	
	public void insertRowRep (HashMap<String,String> theDictRow )
	{
		
		String str =  "INSERT INTO reps (empid, emp_name , division_id, email ,phone ,zip ,imageurl )";
		str = str + " values ( ";
		str = str +  theDictRow.get("Employee_Id")  + ",";
		str = str +  "'"+ theDictRow.get("Employee_Name").replaceAll("'", " ")            + "',";
		str = str +  theDictRow.get("Division_Id")  + ",";
		str = str +  "'"+ theDictRow.get("Email").replaceAll("'", " ")            + "',";
		str = str +  "'"+ theDictRow.get("Phone").replaceAll("'", " ")            + "',";
		str = str +  "'"+ theDictRow.get("ZipCodes").replaceAll("'", " ")            + "',";
		str = str +  "'"+ theDictRow.get("imageurl").replaceAll("'", " ")            + "'";
		str = str + " )";
		//Log.v("insertRowRep",str);
		myDataBase.execSQL(str);

	};
	
	
	
    public Cursor getDivisions () 
    {
    	return 	myDataBase.rawQuery("SELECT  div_id , div_name ,div_priority , div_brands , div_promotions ,div_promotion_start_date, div_promotion_end_date,div_ce_text FROM divisions order by div_priority ", null);
    }

    public Cursor createCursorDivisions () 
    {
    	return 	myDataBase.rawQuery("SELECT  div_id , div_name ,div_priority , div_brands , div_promotions ,div_promotion_start_date, div_promotion_end_date,div_ce_text FROM divisions order by div_priority ", null);
    }

	
	
    public Cursor getDivisionsCEAndPromotions () 
    {
    	return 	myDataBase.rawQuery("SELECT  div_id ,  div_promotions ,div_ce_text  FROM divisions order by div_priority ", null);
    }
	
	




public Cursor CursorgetEmpid(String empid)
{
 return 	myDataBase.rawQuery("SELECT  empid , emp_name ,division_id , email ,phone, zip,imageurl,div_name,div_brands,div_promotions,div_promotion_start_date,div_promotion_end_date,div_ce_text FROM reps , divisions where reps.division_id=divisions.div_id and empid="+empid, null);

};



public ArrayList<HashMap<String,String> > getResultsFromSearch(String theZip)
{
	// check the zip
	
	
    ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;
    Cursor cursorFavorites = myDataBase.rawQuery("SELECT  empid , emp_name ,division_id , email ,phone, zip,imageurl,div_name,div_brands,div_promotions,div_promotion_start_date,div_promotion_end_date,div_ce_text FROM reps , divisions where reps.division_id=divisions.div_id order by div_priority", null);
   	cursorFavorites.moveToFirst();
   	HashMap<String,String> o;
   	listItems.clear();
       
   	while (!cursorFavorites.isAfterLast())
		{
   		
   		  String  theZipArrayString =  cursorFavorites.getString(5);
   		  
		   o = new  HashMap<String,String>();
           o.put("empid",cursorFavorites.getString(0));
           o.put("emp_name",cursorFavorites.getString(1));
           o.put("division_id",cursorFavorites.getString(2));
           o.put("email",cursorFavorites.getString(3));
           o.put("phone",cursorFavorites.getString(4));
           o.put("zip",cursorFavorites.getString(5));
           o.put("imageurl",cursorFavorites.getString(6));
           o.put("div_name",cursorFavorites.getString(7));
           o.put("div_brands",cursorFavorites.getString(8));
           o.put("div_promotions",cursorFavorites.getString(9));
           o.put("div_promotion_start_date",cursorFavorites.getString(10));
           o.put("div_promotion_end_date",cursorFavorites.getString(11));
           o.put("div_ce_text",cursorFavorites.getString(12));
           
           
           
          if ( theZipArrayString.indexOf(theZip) != -1)
          {
              listItems.add(o);
          }  
           
		   cursorFavorites.moveToNext();
		}
		cursorFavorites.close();	
	   
	   return listItems;
	   
	

};




public int getDistance(String strSearch ,String strTo  )
{
	
	if (strSearch.length()>3)
	{
		strSearch = strSearch.substring(0,3);
	}
	
	if (strTo.length()>3)
	{
		strTo = strTo.substring(0,3);
	}
	
	
	int salida =-1;
	Cursor acursor;
	
	acursor =  	myDataBase.rawQuery("select  DIST_MILES from zipResults where Zip3_from_ZIP3 ="+strSearch+" and  Zip3_to_ZIP3=" +strTo , null);
	//Log.v("sql","select  DIST_MILES from zipResults where Zip_from ="+strSearch+" and  Zip_to=" +strTo);
	if (acursor.moveToFirst())
	{
		salida= acursor.getInt(0);
	}
	else
	{
		salida=1;
	}
	acursor.close();
	//Log.v("result",""+salida );

	return salida;
};


	

	
	

	
	public String[]  getCompany ()
	{
		 String	salida[] = {
				 "Arnold Dental Supply Company",
				 "Atlanta Dental Supply Co.",
				 "Benco Dental Supply Co.",
				 "Burkhart Dental Supply",
				 "Darby Dental Supply, LLC",
				 "Dental Health Products, Inc.",
				 "Dentsply",
				 "Direct Dental Supply",
				 "Goetze Dental Co.",
				 "Henry Schein, Inc.",
				 "Holt Dental Supply Inc.",
				 "Iowa Dental Supply Co.",
				 "Midwest Dental Equipment and Supply",
				 "Nashville Dental, Inc.",
				 "Newark Dental Corporation",
				 "Orange County",
				 "Patterson Dental Co.",
				 "Pearson Dental Supply Co.",
				 "Smart Practice",
				 "Tri-State Dental Supplies",
				 "Ultimate Dental",
				 "Other"
		};
		return salida;
	}
	
	public boolean isAdmin(String theEmail)
	{
		String[] separated = theEmail.split("@");
		 //separated[0]; // this will contain "dest"
		 //separated[1]; // this will contain " host"
		 if (separated.length==2)
		 {
			 String emailHost = separated[1];
			  if (emailHost.equals("dentsply.com"))
			  {
				  return true;
			  }
		 }
		 return false;
	}
	
	
	public boolean ispreferredEmail(String theEmail)
	{
		 String	thearray[] = {
				 "arnold-dental.com",
				 "atlantadental.com",
				 "benco.com",
				 "burkhartdental.com",
				 "darbydentalsupply.com",
				 "dentsply.com",
				 "dhpi.net",
				 "directdentalsupply.com",
				 "goetzedental.com",
				 "henryschein.com",
				 "holtdental.com",
				 "iowadentalsupply.com",
				 "mwdental.com",
				 "nashvilledental.com",
				 "orangecounty.com",				 
				 "pemconewarkdental.com",
				 "patterson.com",
				 "pearsondental.com",
				 "smartpractice.com",
				 "tristatedental.com",
				 "endoco.com",
				 "newarkdental-pemco.com"
		 		};
		 
		 String[] separated = theEmail.split("@");
		 //separated[0]; // this will contain "dest"
		 //separated[1]; // this will contain " host"
		 int iii;
		 if (separated.length==2)
		 {
			 String emailHost = separated[1];
			 for (iii=0;iii<thearray.length;iii++) 
			 {
				  String host = thearray[iii];
				  if (host.equals(emailHost))
				  {
					  return true;
				  }
				}	
		 }
		 return false;
	}

	
	
	
 
	
 
}