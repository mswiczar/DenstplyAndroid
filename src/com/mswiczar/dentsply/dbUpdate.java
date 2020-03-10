package com.mswiczar.dentsply;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;


public class dbUpdate {

	ArrayList <HashMap<String,String>> theArrayDates = new ArrayList<HashMap<String,String>>();
	HashMap<String,String> theStorageConfig = new HashMap<String,String>();
	

	
	public  boolean checkInternetConnection(Context mContext) {
	    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    // test for connection
	    if (cm.getActiveNetworkInfo() != null
	            && cm.getActiveNetworkInfo().isAvailable()
	            && cm.getActiveNetworkInfo().isConnected()) {
	        return true;
	    } else {
	        return false;
	    }
	}	
	
	
	private boolean updateDivision (String theDate, DataBaseHelper theDB)
	{
    	Log.v("updateDivision","updateDivision start");

		String  theStrToCall;
		if (theDate==null)
		{
			theStrToCall ="http://d4meadmin.com/dentsply/divisionData.php";
		}
		else
		{
//			theStrToCall="http://d4meadmin.com/dentsply/divisionData.php?theDate="+theDate;
			theStrToCall ="http://d4meadmin.com/dentsply/divisionData.php";
			
		}
    	XmlReader axmlreader =  new XmlReader();

    	ArrayList <HashMap<String,String>> theArray = new ArrayList<HashMap<String,String>>();
    	axmlreader.RssParserPull(theStrToCall);
    	axmlreader.str_Name ="DIVISION";
    	theArray.addAll(axmlreader.parse());
    	if (theArray.size() != 0)
    	{
    		theDB.deleteAllDataDivision();
			for (int iii=0;iii< theArray.size() ; iii++)
			{
				theDB.insertRowDivisions( theArray.get(iii) );
				
				
			}
	    	Log.v("updateDivision","updateDivision end");
			return true;
    	}
    	else
    	{
	    	Log.v("updateDivision","updateDivision end");
    		return false;
    	}
	};
	
	
	private boolean updateRepresentative (String theDate,DataBaseHelper theDB)
	{
		
	/**
	 * <EMPS>
		<EMP>
			<Employee_Id>902735</Employee_Id>
			<Employee_Name>Tom Hunt</Employee_Name>
			<Division_Id>1</Division_Id>
			<Email>thunt@dentsply.com</Email>
			<Phone>(804) 338-0506</Phone>
			<ZipCodes>22400, 22401, 22402, 22403, 22404, 22405, 22406, 22407, 22408, 22409, 22410, 22411, 22412, 22413, 22414, 22415, 22416, 22417, 22418, 22419, 22420, 22421, 22422, 22423, 22424, 22425, 22426, 22427, 22428, 22429, 22430, 22431, 22432, 22433, 22434, 22435, 22436, 22437, 22438, 22439, 22440, 22441, 22442, 22443, 22444, 22445, 22446, 22447, 22448, 22449, 22450, 22451, 22452, 22453, 22454, 22455, 22456, 22457, 22458, 22459, 22460, 22461, 22462, 22463, 22464, 22465, 22466, 22467, 22468, 22469, 22470, 22471, 22472, 22473, 22474, 22475, 22476, 22477, 22478, 22479, 22480, 22481, 22482, 22483, 22484, 22485, 22486, 22487, 22488, 22489, 22490, 22491, 22492, 22493, 22494, 22495, 22496, 22497, 22498, 22499, 22500, 22501, 22502, 22503, 22504, 22505, 22506, 22507, 22508, 22509, 22510, 22511, 22512, 22513, 22514, 22515, 22516, 22517, 22518, 22519, 22520, 22521, 22522, 22523, 22524, 22525, 22526, 22527, 22528, 22529, 22530, 22531, 22532, 22533, 22534, 22535, 22536, 22537, 22538, 22539, 22540, 22541, 22542, 22543, 22544, 22545, 2</ZipCodes>
			<imageurl></imageurl>
	     </EMP>	
	 */
		String  theStrToCall;
    	Log.v("updateRepresentative","updateRepresentative start");

		if (theDate==null)
		{
			theStrToCall ="http://d4meadmin.com/dentsply/repData.php";

		}
		else
		{
			theStrToCall ="http://d4meadmin.com/dentsply/repData.php";
//			theStrToCall="http://d4meadmin.com/dentsply/repData.php?theDate="+theDate;
		}
    	XmlReader axmlreader =  new XmlReader();

    	ArrayList <HashMap<String,String>> theArray = new ArrayList<HashMap<String,String>>();
    	axmlreader.RssParserPull(theStrToCall);
    	axmlreader.str_Name ="EMP";
    	theArray.addAll(axmlreader.parse());
    	if (theArray.size() != 0)
    	{
    		theDB.deleteAllDataRep();
			for (int iii=0;iii< theArray.size() ; iii++)
			{
				theDB.insertRowRep( theArray.get(iii) );
			}
	    	Log.v("updateRepresentative","updateRepresentative end");
			
			return true;

    	}
    	else
    	{
	    	Log.v("updateRepresentative","updateRepresentative end");
    		
    		return false;
    	}
	};

	
	
	
	
	public void  updateNews(variablesPersistentes thevars)
	{
    	Log.v("updateNews","updateNews end");

    	
		String url2call = "http://d4meadmin.com/dentsply/getLastNews.php";
    	XmlReader axmlreader =  new XmlReader();
    	Log.v("url2call",url2call);

    	ArrayList <HashMap<String,String>> theArrayNews = new ArrayList<HashMap<String,String>>();
    	axmlreader.RssParserPull(url2call);
    	axmlreader.str_Name ="URL";
    	theArrayNews.addAll(axmlreader.parse());
    	if (theArrayNews.size()!=0)
    	{
    		thevars.url_news = theArrayNews.get(0).get("STRING");
    	}
    	
    	Log.v("thevars.url_news",thevars.url_news);
    	Log.v("updateNews","updateNews end");

    	
	}
	
	
	
	
	
	
	
	public void getLastDates(variablesPersistentes thevars)
	{
    	Log.v("getLastDates","getLastDates start");
		
		String url2call = "http://d4meadmin.com/dentsply/lastChangeDate.php";
    	Log.v("url2call",url2call);

    	XmlReader axmlreader =  new XmlReader();
    	axmlreader.RssParserPull(url2call);
    	axmlreader.str_Name ="DATE";
    	theArrayDates.addAll(axmlreader.parse());
    	Log.v("getLastDates","getLastDates end");
    	
    	
		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date dateObj;
		SimpleDateFormat curFormater2 = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String newDateStr="";
    	
    	
    	
		for (int iii=0;iii< theArrayDates.size() ; iii++)
		{
			if (theArrayDates.get(iii).get("Entity_Id").equals("1"))
			{
				   try 
				   {
					   dateObj = curFormater.parse(theArrayDates.get(iii).get("Last_Modified_Date"));
					   newDateStr = curFormater2.format(dateObj); 
					   thevars.fecha_division=Long.parseLong(newDateStr);
				   } 
				   catch (ParseException e) 
				   {
					   e.printStackTrace();
				   }
			}
			
			if (theArrayDates.get(iii).get("Entity_Id").equals("2"))
			{
				   try 
				   {
					   dateObj = curFormater.parse(theArrayDates.get(iii).get("Last_Modified_Date"));
					   newDateStr = curFormater2.format(dateObj); 
					   thevars.fecha_reps=Long.parseLong(newDateStr);
				   } 
				   catch (ParseException e) 
				   {
					   e.printStackTrace();
				   }
			}
			
	    	Log.v("updateRepresentative",theArrayDates.get(iii).get("Entity_Id") + " - " + theArrayDates.get(iii).get("Entity_Name") + " - "+ theArrayDates.get(iii).get("Last_Modified_Date") );
		}
		Log.v("fecha_reps", ""+thevars.fecha_reps);
		Log.v("fecha_division", ""+thevars.fecha_division);
    	
	}

	
	public void sendUsageData(variablesPersistentes avar)
	{
		String email    =avar.user_email;
		String entidad  ="1";
		String cantidad ="1";
		SimpleDateFormat curFormater2 = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObj = new Date();
		String eldate = curFormater2.format(dateObj); 
		
		String   url2call = "http://d4meadmin.com/dentsply/updateStatsUser.php?Email="+email +"&date="+eldate+"&entity="+entidad+"&count="+cantidad;
		
    	XmlReader axmlreader =  new XmlReader();

    	ArrayList <HashMap<String,String>> theArrayUsage = new ArrayList<HashMap<String,String>>();
    	axmlreader.RssParserPull(url2call);
    	axmlreader.str_Name ="USER";
    	theArrayUsage.addAll(axmlreader.parse());
    	if (theArrayUsage.size()!=0)
    	{
    		String OK = theArrayUsage.get(0).get("OK");
    		if (OK.equals("1"))
    		{
    			
    		}
    	}
	}
	
	
	public void sendUsageData(variablesPersistentes avar,String entidad)
	{
		
		try
		{
		String email    =avar.user_email;
		String cantidad ="1";
		SimpleDateFormat curFormater2 = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObj = new Date();
		String eldate = curFormater2.format(dateObj); 
		
		String   url2call = "http://d4meadmin.com/dentsply/updateStatsUser.php?Email="+email +"&date="+eldate+"&entity="+entidad+"&count="+cantidad;
		
    	XmlReader axmlreader =  new XmlReader();

    	ArrayList <HashMap<String,String>> theArrayUsage = new ArrayList<HashMap<String,String>>();
    	axmlreader.RssParserPull(url2call);
    	axmlreader.str_Name ="USER";
    	theArrayUsage.addAll(axmlreader.parse());
    	if (theArrayUsage.size()!=0)
    	{
    		String OK = theArrayUsage.get(0).get("OK");
    		if (OK.equals("1"))
    		{
    			
    		}
    	}
		}
		catch (Exception e)
		{
			
		}
	}
	
	
	
	
	public boolean   updateDb(DataBaseHelper theDB ,variablesPersistentes thevars )
	{
		
		this.updateNews(thevars);
        long fecha_division=thevars.fecha_division;
    	long fecha_reps=thevars.fecha_reps;

		this.getLastDates(thevars);
		
		if (fecha_division < thevars.fecha_division)
		{
			this.updateDivision(null,theDB);
		}
		
		if (fecha_reps < thevars.fecha_reps)
		{
			this.updateRepresentative(null,theDB);
		}
		
		Log.v("set setDatosToDB","start");
		theDB.setDatosToDB(thevars);
		Log.v("set setDatosToDB","end");
		
		return true;
	}
	
}





