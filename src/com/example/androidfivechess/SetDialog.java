package com.example.androidfivechess;

import com.example.androidfiveinarow.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetDialog extends Dialog
{

	public SetDialog(Context context)
	{
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setTitle("…Ë÷√");
		setContentView(R.layout.settingdialog);

		Button bt;
		bt = (Button) findViewById(R.id.setConfirmButton);
		bt.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				dismiss();
			}
		});

	}

}
