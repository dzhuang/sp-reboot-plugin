package com.probeez.profiles.reboot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;
import static com.probeez.profiles.reboot.PluginController.TAG;

public class FileUtils {
	public final static String SU_CMD = "su";
	public final static String REBOOT_CMD = "reboot";
	private final static String[] CMD_PATHS = {
		"/system/bin/", "/system/xbin/", "./" 
	};

  public static String findRebootCommand(String su, Context context) {
		String cmd = getCommandFullPath(REBOOT_CMD);
		if (cmd!=null) {
			return cmd;
		}
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

	public static String getCommandFullPath(String name) {
		try {
			for (String path : CMD_PATHS) {
				String cmd = path+name;
				if (new File(cmd).exists()) {
					Log.i(TAG, "Command "+name+" found:"+cmd);
					return cmd;
				}
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
