package com.probeez.profiles.reboot;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

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
	static final int ACTION_REBOOT = 0;
	static final int ACTION_SHUTDOWN = 1;
	private static final String KEY_USE_EMBEDDED_CMD = "use_embedded_cmd";
	private static Boolean isEmbeddedCmdUsed; 
	
	@Override
  protected void onPerformAction(Context context, Bundle state, boolean triggered) {
		if (triggered) {
			int action = state.getInt(STATE_ACTION, ACTION_REBOOT);
			sendRebootIntent(context, action);
		}
  }

	static void sendRebootIntent(Context context, int action) {
		Intent intent = new Intent(context, RebootTimerActivity.class);
		intent.putExtra(STATE_ACTION, action);
		intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static synchronized boolean isEmbeddedCmdUsed(Context context) {
		if (isEmbeddedCmdUsed==null) {
			SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
			isEmbeddedCmdUsed = prefs.getBoolean(KEY_USE_EMBEDDED_CMD, false);
		}
	  return isEmbeddedCmdUsed;
  }
	
	public static synchronized void setEmbeddedCmdUsed(Context context, boolean enabled) {
		isEmbeddedCmdUsed = enabled;
		SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(KEY_USE_EMBEDDED_CMD, enabled);
		editor.commit();
	}
}
