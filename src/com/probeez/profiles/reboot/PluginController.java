package com.probeez.profiles.reboot;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.probeez.profiles.plugin.PluginBroadcastReceiver;

/**
 * Class to listen for SP intent broadcasts.
 * It should be declared in <code>AndroidManifest.xml</code> with appropriate intent filter. 
 */
public class PluginController extends PluginBroadcastReceiver {

	static final String TAG = "SPReboot";
	static final String STATE_ACTION = "state_action";
	static final int ACTION_REBOOT = 0;
	static final int ACTION_SHUTDOWN = 1;

	@Override
  protected void onPerformAction(Context context, Bundle state) {
		int action = state.getInt(STATE_ACTION);
		try {
			String cmd = "reboot"+(action==ACTION_REBOOT? "": " -p");
	    Runtime.getRuntime().exec(new String[] {
	    	"su", "-c", cmd
	    });
		} catch (IOException e) {
			Log.e(TAG, "Cannot execute su", e);
    }
  }

}
