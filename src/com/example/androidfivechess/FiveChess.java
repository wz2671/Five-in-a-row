package com.example.androidfivechess;

import com.example.androidfiveinarow.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FiveChess extends View
{
	private float width, height;
	private Point selected = new Point();
	private float offset;
	private boolean single, playerfirst;
	private Context context;
	private GameDate game = new GameDate();
	private Bitmap blackChess, whiteChess, cross;
	private SoundPool sp;
	private int music;

	@Override // ����view�Ĵ�С
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
	}

	public FiveChess(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		single = false;
		playerfirst = false;
		sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);//��һ������Ϊͬʱ���������������������ڶ����������ͣ�����Ϊ��������
		music = sp.load(context, R.raw.chessmedia, 1); //�������زķŵ�res/raw���2��������Ϊ��Դ�ļ�����3��Ϊ���ֵ����ȼ�
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		width = w / 15.0f;
		height = (h - w) / 2.0f;
		offset = width / 2;
		getChess();
		super.onSizeChanged(w, w, oldw, oldw);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		// ���ñ���ͼƬ
		/*
		 * Resources resources = getContext().getResources(); btnDrawable =
		 * resources.getDrawable(R.drawable.secondbackground);
		 * setBackground(btnDrawable);
		 */
		// ���ߵı�
		Paint linePaint = new Paint();
		linePaint.setColor(getResources().getColor(R.color.line));
		linePaint.setStrokeWidth(2);
		// ���̱����ı�
		Paint mapPaint = new Paint();
		mapPaint.setColor(getResources().getColor(R.color.map));
		// �����̱���
		canvas.drawRect(offset, height + offset, 14 * width + offset, height + 14 * width + offset, mapPaint);
		// System.out.println(offset + " " + height + " " + width);
		// ��������
		for (int i = 0; i < 15; i++)
		{
			// ����
			canvas.drawLine(offset, height + i * width + offset, 14 * width + offset, height + i * width + offset,
					linePaint);
			// ����
			canvas.drawLine(i * width + offset, height + offset, i * width + offset, height + 14 * width + offset,
					linePaint);
		}

		for (int i = 0; i < 15; i++)
		{
			for (int j = 0; j < 15; j++)
			{
				paintChess(i, j, canvas, game.getChess(i, j));
			}
		}
		
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() != MotionEvent.ACTION_DOWN || game.isOver())
			return super.onTouchEvent(event);

		int x = (int) (event.getX() / width);
		int y = (int) (event.getY() / width);
		boolean available = game.move(x, y);
		if(available) 
		{
			selected = new Point(x, y);
			sp.play(music, 1, 1, 0, 0, 1);
		/*��������
		 * play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate) ,
		 * ����leftVolume��rightVolume��ʾ����������
		 * priority��ʾ���ȼ�,
		 * loop��ʾѭ������,
		 * rate��ʾ���ʣ���  //�������0.5���Ϊ2��1���������ٶ� 
		 *  */
		}
		invalidate();
		if(game.checkOver())
		{
			showOverDialog();
		}
		if(single && !game.isOver() && available) 
		{
			computerMove();
			if(game.checkOver()) showOverDialog();
		}
		return true;
	}

	public void paintChess(int i, int j, Canvas canvas, int chessColor)
	{
		if (chessColor == -1)
		{
			canvas.drawBitmap(whiteChess, i * width + 1, j * width + 1, new Paint());
		}
		else if (chessColor == 1)
		{
			canvas.drawBitmap(blackChess, i * width + 1, j * width + 1, new Paint());
		}
		if(i==selected.x && j==selected.y && game.isMoved())
			canvas.drawBitmap(cross, i * width + 1, j * width + 1, new Paint());

	}

	public void getChess()
	{
		whiteChess = BitmapFactory.decodeResource(getResources(), R.drawable.white);
		blackChess = BitmapFactory.decodeResource(getResources(), R.drawable.black);
		cross = BitmapFactory.decodeResource(getResources(), R.drawable.cross);
		int chessWidth = blackChess.getWidth();
		int chessHeight = blackChess.getHeight();
		// ��С�ı���
		float scaleRate = (width - 2) / chessWidth;
		// System.out.println("width:"+width+" scaleRate: "+scaleRate+" height:
		// " + chessHeight + " width " + chessWidth);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleRate, scaleRate);
		// �õ��µ�ͼƬ
		blackChess = Bitmap.createBitmap(blackChess, 0, 0, chessWidth, chessHeight, matrix, true);
		whiteChess = Bitmap.createBitmap(whiteChess, 0, 0, chessWidth, chessHeight, matrix, true);
		cross = Bitmap.createBitmap(cross, 0, 0, chessWidth, chessHeight, matrix, true);

	}

	public void showOverDialog()
	{
		String overMessage = single?(playerfirst?(game.isBlack()?"���Ի�ʤ":"��һ�ʤ"):
			(game.isBlack()?"��һ�ʤ":"���Ի�ʤ")):
			(game.isBlack()?"�׷�ʤ��":"�ڷ�ʤ��");
		AlertDialog.Builder overDialog = new AlertDialog.Builder(context);
		overDialog.setTitle("GameOver");
		overDialog.setMessage(overMessage);
		overDialog.setNegativeButton("���¿�ʼ", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				restart();
			}
		});
		overDialog.setPositiveButton("ȡ��", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
			}
		});
		overDialog.show();
	}
	
	public void restart()
	{
		game.reset();
		invalidate();
	}

	public void cancelMove()
	{
		game.cancelLastMove();
		if(single && !game.isOver()) game.cancelLastMove();
		selected = game.getLast();
		invalidate();
	}
	
	public void computerMove()
	{
		game.aiMove();
		selected = game.getSelected();
		invalidate();
	}
	
	public void setMode(boolean b)
	{
		single = b;
	}
	
	public void setFirst(boolean b)
	{
		playerfirst = b;
	}
	
	public boolean computerFirst()
	{
		if(single && !playerfirst) return true;
		else return false;
	}
	
	public void firstMove()
	{
		game.move(7, 7);
		selected.x=7;
		selected.y=7;
		invalidate();
	}

	public boolean isBlack()
	{
		return game.isBlack();
	}
	
	public boolean isOver()
	{
		return game.isOver();
	}
}
