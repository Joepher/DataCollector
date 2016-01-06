package com.mapfinger.joepher.datacollector.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapfinger.joepher.datacollector.R;
import com.mapfinger.joepher.datacollector.bdlocation.BDLocationActivity;

/**
 * Created by Joepher on 2015-12-13.
 */
public class SettingUserIDActivity extends Activity implements View.OnClickListener {
	private TextView tv_userid;
	private Button btn_seeting;
	public static String userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_setting);

		tv_userid = (TextView) findViewById(R.id.tv_userid);
		btn_seeting = (Button) findViewById(R.id.btn_seeting);
		btn_seeting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_seeting:
				if (authCheck()) {
					doLocation();
				}
				break;
		}
	}

	private boolean authCheck() {
		getUserId();

		if (userId != null && !"".equals(userId)) {
			return true;
		} else {
			Toast.makeText(SettingUserIDActivity.this, "请正确设置用户ID", Toast.LENGTH_SHORT).show();

			return false;
		}
	}

	private void getUserId() {
		userId = tv_userid.getText().toString().trim();
	}

	private void doLocation() {
		Intent intent = new Intent(SettingUserIDActivity.this, LocatingActivity.class);
		startActivity(intent);
	}
}
