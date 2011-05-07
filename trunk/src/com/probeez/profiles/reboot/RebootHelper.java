package com.probeez.profiles.reboot;

import static com.probeez.profiles.reboot.FileUtils.SU_CMD;
import static com.probeez.profiles.reboot.PluginController.*;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class RebootHelper {
	static final int ACTION_REBOOT = 0;
	static final int ACTION_SHUTDOWN = 1;
	private static final String KEY_USE_EMBEDDED_CMD = "use_embedded_cmd";
	private static Boolean isEmbeddedCmdUsed; 

	private String mSuCmd;
	private String mRebootCmd;

	public RebootHelper(Context context) {
		mSuCmd = FileUtils.getSystemCommandPath(SU_CMD);
		if (mSuCmd!=null) {
			mRebootCmd = FileUtils.findRebootCommand(mSuCmd, context, isEmbeddedCmdUsed(context));
		}
	}
	
	public String getSuCommand() {
		return mSuCmd;
	}
	
	public String getRebootCommand() {
		return mRebootCmd;
	}
	
	public void reboot(int action) {
		if (mSuCmd!=null && mRebootCmd!=null) {
			try {
		    Runtime.getRuntime().exec(new String[] {
		    	mSuCmd, "-c",
		    	(action==ACTION_REBOOT? mRebootCmd: mRebootCmd+" -p")
		    });
			} catch (IOException e) {
				Log.e(TAG, "Cannot execute "+mSuCmd, e);
	    }
		}
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
