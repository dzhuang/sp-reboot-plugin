package com.probeez.profiles.reboot;

import static com.probeez.profiles.plugin.PluginIntent.ACTION_EDIT_CONDITION;
import static com.probeez.profiles.plugin.PluginIntent.EXTRA_STATE;
import static com.probeez.profiles.plugin.PluginIntent.EXTRA_SUMMARY;
import static com.probeez.profiles.reboot.PluginController.*;
import static com.probeez.profiles.reboot.RebootHelper.*;

import com.probeez.profiles.plugin.PluginIntent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class EditActionActivity extends Activity implements OnClickListener, OnCancelListener {

	private int mAction;
	private ArrayAdapter<CharSequence> mAdapter;

	/** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (!ACTION_EDIT_CONDITION.equals(intent.getAction())) {
			finish();
			return;
		}
		Bundle state = intent.getBundleExtra(EXTRA_STATE);
		mAction = state!=null? state.getInt(STATE_ACTION, ACTION_REBOOT): ACTION_REBOOT; 
	}

	@Override
	protected void onResume() {
		super.onResume();
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		mAdapter = ArrayAdapter.createFromResource(
				this, R.array.actions, android.R.layout.select_dialog_singlechoice);
    return new AlertDialog.Builder(this)
	    .setTitle(R.string.plugin_title)
	    .setSingleChoiceItems(mAdapter, mAction, this)
	    .setOnCancelListener(this)
	    .create();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int action) {
		PluginIntent data = new PluginIntent(this);
		data.putExtra(EXTRA_SUMMARY, mAdapter.getItem(action));
		Bundle state = data.getState();
		state.putInt(STATE_ACTION, action);
    setResult(RESULT_OK, data);
		finish();
	}
}