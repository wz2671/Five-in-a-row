package com.example.androidfivechess;

import com.example.androidfiveinarow.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity
{
	private Button backButton, regreateButton, restartButton;
	private TextView playerTextView;
	private FiveChess mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		mapView = (FiveChess) findViewById(R.id.mapView);
		playerTextView = (TextView) findViewById(R.id.playerTextView);
		restartButton = (Button) findViewById(R.id.restartButton);
		regreateButton = (Button) findViewById(R.id.regretButton);
		backButton = (Button) findViewById(R.id.backButton);

		backButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent();
				intent.setClass(GameActivity.this, MainActivity.class);
				GameActivity.this.startActivity(intent);
				GameActivity.this.finish();
			}
		});

		regreateButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				mapView.cancelMove();
				if (mapView.isBlack())
					playerTextView.setText("黑方执棋");
				else
					playerTextView.setText("白方执棋");
			}
		});

		restartButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				AlertDialog.Builder warningDialog = new AlertDialog.Builder(GameActivity.this);
				warningDialog.setTitle("Warning");
				warningDialog.setMessage("确定重新开始？");
				warningDialog.setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						mapView.restart();
						playerTextView.setText("黑方执棋");
					}
				});
				warningDialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface arg0, int arg1)
					{
						
					}
				});
				warningDialog.show();
			}
		});
		
		ModeDialog dialog = new ModeDialog(this, mapView, R.style.dialog);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_mode, null);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mapView.isBlack())
			playerTextView.setText("黑方执棋");
		else
			playerTextView.setText("白方执棋");
		return true;
	}
	
}
