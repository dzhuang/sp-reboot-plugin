package com.probeez.profiles.reboot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import static com.probeez.profiles.reboot.FileUtils.REBOOT_CMD;
import static com.probeez.profiles.reboot.FileUtils.SU_CMD;
import static com.probeez.profiles.reboot.PluginController.TAG;

public class FileUtils {
	public final static String SU_CMD = "su";
	public final static String REBOOT_CMD = "reboot";
	private final static String[] CMD_PATHS = {
		"/system/bin/", "/system/xbin/"
	};

  public static String findRebootCommand(String su, Context context, boolean useEmbedded) {
		String cmd;
		// check if embedded or system command exists
		if (useEmbedded) {
			cmd = getEmbeddedCommandPath(context, REBOOT_CMD);
		} else {
			cmd = getSystemCommandPath(REBOOT_CMD);
			if (cmd!=null) {
				return cmd;
			}
			cmd = getEmbeddedCommandPath(context, REBOOT_CMD);
		}
		if (cmd!=null) {
			return cmd;
		}
		// activate embedded command
		try {
			copyStream(
				context.getAssets().open(REBOOT_CMD), 
				context.openFileOutput(REBOOT_CMD, Context.MODE_WORLD_READABLE)
			);
			cmd = context.getFileStreamPath(REBOOT_CMD).getAbsolutePath();
	    Runtime.getRuntime().exec(new String[] {
		    	su, "-c", "chmod 755 "+cmd
		    });
			Log.i(TAG, "Reboot command placed to:"+cmd);
			return cmd;
		} catch (IOException e) {
			Log.e(TAG, "Cannot find reboot command", e);
			return null;
		}
	}

	public static String getSystemCommandPath(String name) {
		try {
			for (String path : CMD_PATHS) {
				String cmd = path+name;
				File file = new File(cmd);
				if (file.exists()) {
					Log.i(TAG, "Command "+name+" found:"+cmd);
					return cmd;
				}
			}
		} catch (Throwable e) {
			Log.e(TAG, name+" command not found", e);
		}
		return null;
  }

	private static String getEmbeddedCommandPath(Context context, String name) {
		try {
			// locate at ./files/*
			File file = context.getFileStreamPath(name);
			if (file.exists()) {
				String cmd = file.getAbsolutePath();
				Log.i(TAG, "Command "+name+" found:"+cmd);
				return cmd;
			}
		} catch (Throwable e) {
			Log.e(TAG, name+" command not found", e);
		}
		return null;
	}
	
  private static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
  	try {
	    try {
	      byte[] buffer=new byte[4096];
	      int bytesRead;
	      while ((bytesRead=inputStream.read(buffer))>=0) {
	      	outputStream.write(buffer, 0, bytesRead);
	      }
		  } finally {
		  	outputStream.close();
		  }
  	} finally {
  		inputStream.close();
  	}
  }

}
