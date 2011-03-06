package com.probeez.profiles.reboot;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import android.content.Context;
import android.content.Intent;
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

	static final String TAG = "SPReboot";
	static final String STATE_ACTION = "state_action";
	static final int ACTION_REBOOT = 0;
	static final int ACTION_SHUTDOWN = 1;

	@Override
  protected void onPerformAction(Context context, Bundle state, boolean triggered) {
		if (triggered) {
			Intent intent = new Intent(context, RebootTimerActivity.class);
			int action = state.getInt(STATE_ACTION, ACTION_REBOOT);
			intent.putExtra(STATE_ACTION, action);
			intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
  }

}
