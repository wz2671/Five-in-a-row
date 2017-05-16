package com.example.androidfivechess;

import com.example.androidfiveinarow.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ModeDialog extends Dialog
{
	private FiveChess mapview;
	private RadioGroup modeGroup, turnGroup;
	private RadioButton doubleButton, singleButton, playerFirstButton, computerFirstButton;
	private Button confirmButton;
	private RadioButtonListener radioButtonListener = new RadioButtonListener();
	private ButtonClickListener buttonListener = new ButtonClickListener();
	
	public ModeDialog(Context context, FiveChess mapview, int theme)
	{
		super(context, theme);
		this.mapview = mapview;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		findView();
		setListener();
	}
	
	private void findView()
	{
		modeGroup = (RadioGroup)findViewById(R.id.modeGroup);
		turnGroup = (RadioGroup)findViewById(R.id.turnGroup);
		doubleButton = (RadioButton)findViewById(R.id.doubleMode);
		singleButton = (RadioButton)findViewById(R.id.singleMode);
		playerFirstButton = (RadioButton)findViewById(R.id.playerFirst);
		computerFirstButton = (RadioButton)findViewById(R.id.computerFirst);
		confirmButton = (Button)findViewById(R.id.modeConfirmButton);
	}
	
	private void setListener()
	{
		modeGroup.setOnCheckedChangeListener(radioButtonListener);
		turnGroup.setOnCheckedChangeListener(radioButtonListener);
		confirmButton.setOnClickListener(buttonListener);
	}
	
	class RadioButtonListener implements android.widget.RadioGroup.OnCheckedChangeListener
	{
		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int id)
		{
			if(id == R.id.doubleMode) 
			{
				mapview.setMode(false);
				playerFirstButton.setEnabled(false);
				computerFirstButton.setEnabled(false);
			}
			if(id == R.id.singleMode)
			{
				mapview.setMode(true);
				playerFirstButton.setEnabled(true);
				computerFirstButton.setEnabled(true);
				if(!playerFirstButton.isChecked() && !computerFirstButton.isChecked())
					playerFirstButton.setChecked(true);
			}
			if(id == R.id.playerFirst) 
			{
				mapview.setFirst(true);
			}
			if(id == R.id.computerFirst)
			{
				mapview.setFirst(false);
			}
		}
	}
	
	class ButtonClickListener implements android.view.View.OnClickListener
	{
		@Override
		public void onClick(View arg0)
		{
			dismiss();
			if(mapview.computerFirst()) mapview.firstMove();
		}
	}
}
