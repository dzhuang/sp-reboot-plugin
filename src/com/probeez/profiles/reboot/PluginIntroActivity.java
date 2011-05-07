package com.probeez.profiles.reboot;

import static com.probeez.profiles.reboot.RebootHelper.ACTION_REBOOT;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class PluginIntroActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		Button button = (Button) findViewById(R.id.reboot_button);
		button.setOnClickListener(this);
		CheckBox checkbox = (CheckBox) findViewById(R.id.use_embedded_cmd);
		checkbox.setChecked(RebootHelper.isEmbeddedCmdUsed(this));
		checkbox.setOnClickListener(this);
	}

	@Override
  public void onClick(View v) {
		switch(v.getId()) {
			case R.id.reboot_button:
				PluginController.sendRebootIntent(this, ACTION_REBOOT);
				break;
			case R.id.use_embedded_cmd:
				boolean enabled = ((CheckBox)v).isChecked();
				RebootHelper.setEmbeddedCmdUsed(this, enabled);
				break;
		}
  }

}
