package com.probeez.profiles.reboot;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.PowerManager;
import static com.probeez.profiles.reboot.RebootHelper.*;

import com.probeez.profiles.plugin.PluginBroadcastReceiver;
/**
 * Class to listen for SP intent broadcasts.<br>
 * It should be declared in <code>AndroidManifest.xml</code> with the intent filter:
 * <pre>
 * &lt;intent-filter&gt;
 *    &lt;action android:name="com.probeez.profiles.plugin.PERFORM_ACTION" /&gt;
 *    &lt;action android:name="com.probeez.profiles.plugin.QUERY_PLUGIN_STATUS" /&gt;
 * &lt;/intent-filter&gt;
 * </pre>
 */
public class PluginController extends PluginBroadcastReceiver {

	// to see log messages run 'adb shell setprop log.tag.SPReboot DEBUG'
	static final String TAG = "SPReboot";
	static final String STATE_ACTION = "state_action";
	
	@Override
  protected void onPerformAction(Context context, Bundle state, boolean triggered) {
		if (triggered) {
			int action = state.getInt(STATE_ACTION, ACTION_REBOOT);
			PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
			boolean isScreenOn = pm.isScreenOn(); 		
			if (isScreenOn) {
				sendRebootIntent(context, action);
			} else {
				RebootHelper helper = new RebootHelper(context);
				helper.reboot(action);
			}
		}
  }

	static void sendRebootIntent(Context context, int action) {
		Intent intent = new Intent(context, RebootTimerActivity.class);
		intent.putExtra(STATE_ACTION, action);
		intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	
}
