package com.probeez.profiles.reboot;

import static com.probeez.profiles.reboot.RebootHelper.*;
import static com.probeez.profiles.reboot.PluginController.*;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RebootTimerActivity extends Activity implements OnClickListener {


	private class TimerHandler extends Handler {
		private static final int MSG_COUNTDOWN = 0;
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_COUNTDOWN:
				int seconds = msg.arg1-1;
				mTimerText.setText(String.valueOf(seconds));
				if (seconds>0) {
					postCountdown(seconds);
					return;
				}
				finish();
				mHelper.reboot(mAction);
				break;
			}
		}
		public synchronized void postCountdown(int seconds) { 
			Message msg = mHandler.obtainMessage(MSG_COUNTDOWN, seconds, 0);
			mHandler.sendMessageDelayed(msg, 1000);
		}
		public synchronized void cancelCountdown() {
			mHandler.removeMessages(MSG_COUNTDOWN);
		}
	};

	private TimerHandler mHandler	= new TimerHandler();
	private TextView mTimerText;
	private int mAction;
	private String mSuCmd;
	private String mRebootCmd;
	private RebootHelper mHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mAction = intent!=null? intent.getIntExtra(STATE_ACTION, ACTION_REBOOT): ACTION_REBOOT;
		mHelper = new RebootHelper(this);
		mSuCmd = mHelper.getSuCommand();
		if (mSuCmd==null) {
			Toast.makeText(this, getString(R.string.no_cmd_found, FileUtils.SU_CMD), Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		mRebootCmd = mHelper.getRebootCommand();
		if (mRebootCmd==null) {
			Toast.makeText(this, getString(R.string.no_cmd_found, FileUtils.REBOOT_CMD), Toast.LENGTH_LONG).show();
			finish();
			return;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		showDialog(0);
		mHandler.postCountdown(5);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.countdown, null);
		Button button = (Button) view.findViewById(R.id.button);
		button.setOnClickListener(this);
		mTimerText = (TextView) view.findViewById(R.id.timer);
    return new AlertDialog.Builder(this)
	    .setView(view)
	    .create();
	}

	@Override
	public void onClick(View v) {
		mHandler.cancelCountdown();
		finish();
	}
	
}
