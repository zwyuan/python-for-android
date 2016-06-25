package org.renpy.android;

import android.content.BroadcastReceiver;  
import android.content.Intent;  
import android.content.Context; 
import android.os.Environment; 
import android.util.Log;
import org.renpy.android.PythonActivity; 

import java.io.File;
import java.lang.StringBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;


/*
 * Boot MobileInsight on Startup
 */ 
public class MobileInsightBroadcastReceiver extends BroadcastReceiver {  

   private static final String TAG = "MobileInsightBroadcastReceiver"; 

   public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
   }
   public void onReceive(Context context, Intent intent) {   

   	     if(!isExternalStorageWritable()){
   	     	return;
   	     }

         //Check configuration
         File sdcard = Environment.getExternalStorageDirectory();
         File file = new File(sdcard,".mobileinsight.ini");

         if(!file.exists() || file.isDirectory()){

         	Log.i(TAG, "mobileinsight configuration not found");

         	return;

         }

         StringBuilder text = new StringBuilder();

         boolean bstart = false;

         try {
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    String line;

			    while ((line = br.readLine()) != null) {
			        if(line.contains("bstartup")){
			        	if(line.contains("1")){
			        		bstart = true;
			        	}
			        	break;
			        }
			    }
			    br.close();
		}
		catch (IOException e) {
			//Do nothing
		}

        Log.i(TAG, "bstart="+String.valueOf(bstart));
        if(bstart){
        	Intent ix = new Intent(context,PythonActivity.class);   
         	ix.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
         	context.startActivity(ix);  
        }  
    }      
}  