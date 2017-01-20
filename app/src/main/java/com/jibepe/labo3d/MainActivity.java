package com.jibepe.labo3d;



import java.util.Observable;
import java.util.Observer;


import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;


public class MainActivity extends ActionBarActivity {
	
	private final String TAG = "MainActivity";
	

    /*********************************************/
    /***                PROCESS                 **/
    /*********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
		

		if (supportsEs2) 
		{
			// Create an OpenGL ES 2.0 context.
			theView = new GLES20View (getApplication());
			
			RelativeLayout outerView = 
	                (RelativeLayout)this.findViewById(R.id.lelayout);
	        View view1 =  this.findViewById(R.id.txt2glsurf);
	        outerView.removeView(view1);
	        outerView.addView(theView,0);
		}

		LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
			      new IntentFilter("custom-event-name")); 
    
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
      // Unregister since the activity is about to be closed.
      LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
      super.onDestroy();
    }    
  
    
    /*********************************************/
    /***                GUI                     **/
    /*********************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
    /*********************************************/
    /***         LOCAL MESSENGING               **/
    /*********************************************/
    
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
  	  @Override
  	  public void onReceive(Context context, Intent intent) {
  	    // Get extra data included in the Intent
    	String topic = intent.getStringExtra("topic");
  	    String message = intent.getStringExtra("message");
  	    Log.d(TAG, "Got message: ("+topic+ ") " + message);
  	    if (topic.compareTo("bzPosition") == 0)
  	    {
  	    	String[] strMsg = message.split(":");
  	    	String objName = strMsg[0];
  	    	String[] strPos = strMsg[1].split(",");
  	    	final float [] objPos = {Float.parseFloat(strPos[0]), Float.parseFloat(strPos[2]), -Float.parseFloat(strPos[1])}; 
  	    	theView.getRenderer().setObjPos(objName, objPos);
  	    } else if (topic.compareTo("bzOrientation") == 0)
  	    {
  	    	String[] strMsg = message.split(":");
  	    	String objName = strMsg[0];
  	    	String[] strRot = strMsg[1].split(",");
  	    	final float [] objRot = {Float.parseFloat(strRot[0]), Float.parseFloat(strRot[2]), -Float.parseFloat(strRot[1])}; 
  	    	theView.getRenderer().setObjRot(objName, objRot);
  	    }  	    
  	  }
  	};

  	
  	

	private GLES20View theView;


}
