package com.mswiczar.dentsply;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;



public class XmlReader
{
    private URL rssUrl;
    public String str_Name;
    
 
    public void RssParserPull(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public List<HashMap<String,String>> parse()
    {
        List<HashMap<String,String>> noticias = null;
        XmlPullParser parser = Xml.newPullParser();
        try
        {
        	
        	Log.v("entro","List<HashMap<String,String>> parse()");


        	
            parser.setInput(this.getInputStream(), "ISO-8859-1");

        	Log.v("entro","parser.setInput(this.getInputStream(),");
 
            int evento = parser.getEventType();
 
            HashMap<String,String> noticiaActual = null;
 
            while (evento != XmlPullParser.END_DOCUMENT)
            {
                String etiqueta = null;
 
                switch (evento)
                {
                    case XmlPullParser.START_DOCUMENT:
 
                        noticias = new ArrayList<HashMap<String,String>>();
                        break;
 
                    case XmlPullParser.START_TAG:
 
                        etiqueta = parser.getName();
 
                        if (etiqueta.equals(str_Name))
                        {
                            noticiaActual = new HashMap<String,String>();
                        }
                        else if (noticiaActual != null)
                        {
                            noticiaActual.put(etiqueta,parser.nextText());
                        }
                        break;
 
                    case XmlPullParser.END_TAG:
 
                        etiqueta = parser.getName();
 
                        
                        
                        if (etiqueta.equals(str_Name) && noticiaActual != null)
                        {
                            noticias.add(noticiaActual);
                        }
                        break;
                }
 
                evento = parser.next();
            }
        }
        catch (Exception ex)
        {
        	
        	// Log.e("Exception found ",ex.getMessage());
            //throw new RuntimeException(ex);
        }
 
        return noticias;
    }
 
    private InputStream getInputStream()
    {
        try
        {
        	Log.v("poraca","NOerror");
        	HttpURLConnection urlConnection = (HttpURLConnection) rssUrl.openConnection();
        	InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        	
        	Log.v("poraca","retorno correcto astream<>null");
	
            return in;
        }
        catch (IOException e)
        {
        	
        	Log.v("poraca","error");

            throw new RuntimeException(e);
        }
    }
    
  public static String slurp (InputStream in) throws IOException {
    	
    	
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[1024];
        for (int n; (n = in.read(b)) != -1;) {
        	String data = new String(b, 0, n,"iso-8859-1");

        	out.append(data);
        }
        return out.toString();
    }

    public String getCVSData() throws IOException
    {
    	String adata;
    	adata = slurp (this.getInputStream());;
        Log.v("adata.length()"," " +adata.length());

    	return adata;
    }
    
}

