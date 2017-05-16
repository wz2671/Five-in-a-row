package com.example.androidfivechess;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/*
 * ����Ϊ1
 * ����Ϊ-1
 * �հ״�Ϊ0
 * ��������
*/

public class GameDate
{
	private int[][] chessMap;
	private int[][] valueMap;
	private int dir, depth;
	private Point selected;
	private boolean black, over, moved;
	private Stack<Point> used = new Stack<Point>();
	private List<Point> value = new ArrayList<Point>();

	public GameDate()
	{
		chessMap = new int[15][15];
		valueMap = new int[15][15];
		black = true;
		over = false;
		moved = false;
	}

	public boolean move(int x, int y)
	{
		if (chessMap[x][y] != 0)
			return false;
		if (black)
			chessMap[x][y] = 1;
		else
			chessMap[x][y] = -1;
		used.push(new Point(x, y));
		black = !black;
		if(!moved) moved = true;
		return true;
	}

	public int getChess(int i, int j)
	{
		return chessMap[i][j];
	}

	public void cancelLastMove()
	{
		if (used.isEmpty())
			return;
		if (over)
			over = false;
		Point p = used.pop();
		chessMap[p.x][p.y] = 0;
		black = !black;
	}

	public boolean isBlack()
	{
		return black;
	}

	public boolean isOver()
	{
		return over;
	}
	
	public boolean isMoved()
	{
		return moved;
	}

	public void reset()
	{
		for (int i = 0; i < 15; i++)
		{
			for (int j = 0; j < 15; j++)
			{
				chessMap[i][j] = 0;
				valueMap[i][j] = 0;
			}
		}
		black = true;
		over = false;
		moved = false;
		used.clear();
	}

	//����Ƿ���Ϸ����
	public boolean checkOver()
	{
		int cntX = 0, cntY = 0;
		// ����к�������5�ŵ�
		for (int i = 0; i < 15; i++)
		{
			cntX = cntY = 0;
			for (int j = 1; j < 15; j++)
			{
				if (chessMap[i][j] != 0)
				{
					if (chessMap[i][j - 1] == chessMap[i][j])
						cntY++;
					else
						cntY = 0;
				}
				if (chessMap[j][i] != 0)
				{
					if (chessMap[j - 1][i] == chessMap[j][i])
						cntX++;
					else
						cntX = 0;
				}
				if (cntX == 4 || cntY == 4)
				{
					over = true;
					return true;
				}
			}
		}
		// ������Խ�������5�ŵ�
		for (int i = 0; i < 11; i++)
		{
			cntX = cntY = 0;
			for (int j = 1; i + j < 15; j++)
			{
				if (chessMap[i + j][j] != 0)
				{
					if (chessMap[i + j - 1][j - 1] == chessMap[i + j][j])
						cntX++;
					// System.out.println("i: "+i+" j: "+j + "
					// "+chessMap[i+j][i]);
					else
						cntX = 0;
				}
				if (chessMap[j][i + j] != 0)
				{
					if (chessMap[j - 1][i + j - 1] == chessMap[j][i + j])
						cntY++;
					else
						cntY = 0;
				}
				if (cntX == 4 || cntY == 4)
				{
					over = true;
					return true;
				}
			}
		}
		// ��һ���Խ���
		for (int i = 0; i < 11; i++)
		{
			cntX = cntY = 0;
			for (int j = 1; i + j < 15; j++)
			{
				if (chessMap[14 - j][i + j] != 0)
				{
					if (chessMap[14 - j + 1][i + j - 1] == chessMap[14 - j][j + i])
						cntX++;
					else
						cntX = 0;
				}
				if (chessMap[14 - i - j][j] != 0)
				{
					if (chessMap[14 - i - j + 1][j - 1] == chessMap[14 - i - j][j])
						cntY++;
					else
						cntY = 0;
				}
				if (cntX == 4 || cntY == 4)
				{
					over = true;
					return true;
				}
			}
		}
		return false;
	}
	
	private Point AI()
	{
		int v = 0;
		value.clear();
		int currentColor = black?1:-1;
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<15; j++)
			{
				if(chessMap[i][j]==0)
				valueMap[i][j] = getValue(i, j, currentColor);
			}
		}
//		for(int i=0; i<15; i++)
//		{
//			for(int j=0; j<15; j++)
//			{
//				System.out.printf("%d ",valueMap[i][j]);
//			}
//			System.out.printf("\n");
//		}
		
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<15; j++)
			{
				if(chessMap[i][j]!=0 || v>valueMap[i][j]) continue;
				if(v<valueMap[i][j]) 
				{
					v = valueMap[i][j];
					value.clear();
					value.add(new Point(i,j));
//					System.out.println("x: "+i+" y: "+j +" value: "+valueMap[i][j]);
				}
				if(v==valueMap[i][j])
				{
					value.add(new Point(i,j));
//					System.out.println("x: "+i+" y: "+j +" value: "+valueMap[i][j]);
				}
			}
		}
//		System.out.println("x: "+point.x+" y: "+point.y +" value: "+valueMap[point.x][point.y]);
		return value.get((int)(Math.random()*value.size()));
	}
	
	private int getValue(int x, int y, int color)
	{
		int value=0; dir = -1; depth=4;
		int[] colorList = new int[8];
		//���ҷ���
		for(int i=0; i<8; i++) colorList[i]=-color;
		inRow(x, y, colorList, true);
//		if(x==2&&y==2) 
//		{
//			System.out.printf("���ҷ���\n");
//			for(int i=0; i<8; i++)
//			System.out.printf("%d ", colorList[i]);
//			System.out.printf("\n");
//		}
		value += calcValue(colorList, color, true);
		for(int i=0; i<8; i++) colorList[i]=color;
		inRow(x, y, colorList, true);
//		if(x==2&&y==2) 
//		{
//			System.out.printf("���ҷ���\n");
//			for(int i=0; i<8; i++)
//			System.out.printf("%d ", colorList[i]);
//			System.out.printf("\n");
//		}
		value += calcValue(colorList, -color, false);

		//���·���
		for(int i=0; i<8; i++) colorList[i]=-color;
		inCol(x, y, colorList, true);
		value += calcValue(colorList, color, true);
		for(int i=0; i<8; i++) colorList[i]=color;
		inCol(x, y, colorList, true);
		value += calcValue(colorList, -color, false);
		
//		if(x==2&&y==2) 
//		{
//			System.out.printf("���·���\n");
//			for(int i=0; i<8; i++)
//			System.out.printf("%d ", colorList[i]);
//			System.out.printf("\n");
//		}
		//��б����
		for(int i=0; i<8; i++) colorList[i]=-color;
//		if(x==2&&y==2) 
//		{
//			System.out.printf("��б����\n");
//		}
		inLeftSlant(x, y, colorList, true);
//		if(x==2&&y==2) 
//		{
//			System.out.printf("��б����\n");
//			for(int i=0; i<8; i++)
//			System.out.printf("%d ", colorList[i]);
//			System.out.printf("\n");
//		}
		value += calcValue(colorList, color, true);
		for(int i=0; i<8; i++) colorList[i]=color;
		inLeftSlant(x, y, colorList, true);
		value += calcValue(colorList, -color, false);
		

		//��б����
		for(int i=0; i<8; i++) colorList[i]=-color;
		inRightSlant(x, y, colorList, true);
		value += calcValue(colorList, color, true);
		for(int i=0; i<8; i++) colorList[i]=color;
		inRightSlant(x, y, colorList, true);
		value += calcValue(colorList, -color, false);
		
//		if(x==2&&y==2) 
//		{
//			System.out.printf("��б����\n");
//			for(int i=0; i<8; i++)
//			System.out.printf("%d ", colorList[i]);
//			System.out.printf("\n");
//		}
		return value;
	}
	
	private void inRow(int x, int y, int[] list, boolean f)
	{	
		if(dir==-1)
		{
			if(depth!=4) list[depth] = chessMap[x][y];
			if(depth==0 || x==0) {dir = 1; depth= 3; return ;}
			else 
			{
				depth--;
				inRow(x-1, y, list, false);
			}
		}
		
		if(dir==1 && f)
		{
			if(depth!=3)list[depth] = chessMap[x][y];
			if(depth==7 || x==14) {dir = -1; depth = 4; return;}
			else
			{
				depth++;
				inRow(x+1, y, list, true);
			}
		}
	}
	
	private void inCol(int x, int y, int[] list, boolean f)
	{	
		if(dir==-1)
		{
			if(depth!=4) list[depth] = chessMap[x][y];
			if(depth==0 || y==0) {dir = 1; depth= 3; return ;}
			else 
			{
				depth--;
				inCol(x, y-1, list, false);
			}
		}
		
		if(dir==1 && f)
		{
			if(depth!=3)list[depth] = chessMap[x][y];
			if(depth==7 || y==14) {dir = -1; depth=4; return;}
			else  
			{
				depth++;
				inCol(x, y+1, list, true);
			}
		}
	}
	
	private void inLeftSlant(int x, int y, int[] list, boolean f)
	{	
		if(dir==-1)
		{
			if(depth!=4) list[depth] = chessMap[x][y];
			if(depth==0 || x==0 || y==0) {dir = 1; depth= 3; return ;}
			else 
			{
				depth--;
				inLeftSlant(x-1, y-1, list, false);
			}
		}
		
		if(dir==1 && f)
		{
			if(depth!=3) list[depth] = chessMap[x][y];
			if(depth==7 || x==14 || y==14) {dir=-1; depth=4; return;}
			else  
			{
				depth++;
				inLeftSlant(x+1, y+1, list, true);
			}
		}
	}
	
	private void inRightSlant(int x, int y, int[] list, boolean f)
	{	
		if(dir==-1)
		{
			if(depth!=4) list[depth] = chessMap[x][y];
			if(depth==0 || x==0 || y==14) {dir = 1; depth= 3; return ;}
			else 
			{
				depth--;
				inRightSlant(x-1, y+1, list, false);
			}
		}
		
		if(dir==1 && f)
		{
			if(depth!=3)list[depth] = chessMap[x][y];
			if(depth==7 || x==14 || y==0) {dir =-1; depth=4; return;}
			else  
			{
				depth++;
				inRightSlant(x+1, y-1, list, true);
			}
		}
	}
	
	
	/*����	����		���
	0110	60		50
	01010	40		35
	x110	10		10
	x1010	10		10
	����		950		700
	�����	900		650
	����		100		100
	������	100		100
	����		6000	3500
	�����	5000	3000
	����		4000	800
	������	3600	750
	����		20000	15000
	�����	10000	3300
	����		20000	15000
	������	10000	3300
	����������������ģ��м�û�пո����˶�������һ���ո�
	���������Ӳ��������ģ��������м���һ���ո����˶�������һ���ո�
	�������������������ģ��м�û�пո񣬵���һ�˽����ŶԷ������ӻ���һ�����������̵ı߽硣
	�������������Ӳ��������ģ��������м���һ���ո񣬶���һ�˽����ŶԷ������ӻ���һ�����������̵ı߽硣
	*/
	private int calcValue(int[] l, int c, boolean f)
	{
		//����������>_<
		
		if(l[3]==-c)
		{
			if(l[4]==-c) return 0;
			if(l[4]==0)
			{
				if(l[5]==-c || l[5]==0) return 0;
				if(l[5]==c) 
				{
					if(l[6]==0 && l[7]==0) return 10;//������
					else if(l[6]==0 && l[7]==c) return 10;//������
					else if(l[6]==c && l[7]==0) return 100;//������
					else if(l[6]==c && l[7]==c) return f?3600:750;//������ 
					else return 0;
				}
			}
			if(l[4]==c)
			{
				if(l[5]==-c ||l[6]==-c || l[7]==-c) return 0;
				if(l[5]==0)
				{
					if(l[6]==0)
					{
						if(l[7]==c) return 10;
						if(l[7]==0) return 10;
					}
					if(l[6]==1)
					{
						if(l[7]==0) return 100;
						if(l[7]==1) return f?3600:750;
					}
				}
				if(l[5]==c)
				{
					if(l[6]==0)
					{
						if(l[7]==0) return 100;
						if(l[7]==c) return f?3600:750;
					}
					if(l[6]==c)
					{
						if(l[7]==0) return f?4000:800;
						if(l[7]==c) return f?20000:15000;
					}
				}
			}
		}
		if(l[3]==c)
		{
			if(l[2]==-c)
			{
				if(l[4]==-c) return 0;
				if(l[4]==0) 
				{
					if(l[6]==-c) return 0;
					if(l[6]==0)
					{
						if(l[5]==0) return 10;//����
						if(l[5]==c) return 100;//������
						if(l[5]==-c) return 0;
					}
					if(l[6]==1)
					{
						if(l[5]==c) return f?3600:750;//������
						if(l[5]==0) return 15;//����
						if(l[5]==-c) return 0;
					}
				}
				if(l[4]==c)
				{
					if(l[5]==-c) return 0;
					if(l[5]==0) 
					{
						if(l[6]==0) return 100; //����
						if(l[6]==c) return f?3600:750; //������
					}
					if(l[5]==c) 
					{
						if(l[6]==-c) return 0;
						if(l[6]==0) return f?4000:800;//����
						if(l[6]==c) return f?20000:15000; //����
					}
				}
			}
			if(l[2]==0)
			{
				if(l[1]==-c)
				{
					if(l[4]==-c || l[5]==-c) return 0;
					if(l[4]==c)
					{
						if(l[5]==0)
						{
							if(l[6]==-c) return f?100:50; //-101?10-1
							if(l[6]==0) return f?950:700;
							if(l[6]==1) 
							{
								if(l[7]==-c) return f?3600:750;
								if(l[7]==0 || l[7]==c) return f?5000:3000;
							}
						}
						if(l[5]==c)
						{
							if(l[6]==-c) return f?4000:800;
							if(l[6]==0) return f?6000:3500;
							if(l[6]==c) return f?20000:15000;
						}
					}
					if(l[4]==0)
					{
						if(l[5]==c)
						{
							if(l[6]==-c) return 100;
							if(l[6]==0) return f?900:650;
							if(l[6]==c) return f?5000:3000;
						}
						if(l[5]==0) 
						{
							if(l[6]==-c)return 5;
							if(l[6]==c) return 15;
							if(l[6]==0) return 10;
						}
					}
				}
				if(l[1]==0)
				{
					if(l[4]==-c) return 10;
					if(l[4]==0)
					{
						if(l[5]==0) return f?60:50;
						if(l[5]==-c) return f?50:40;
						if(l[5]==c)
						{
							if(l[6]==-c) return 100;
							if(l[6]==0) return f?900:650;
							if(l[6]==c) return f?5000:3000;
						}
					}
					if(l[4]==c)
					{
						if(l[5]==-c) return 100;
						if(l[5]==0) return f?950:700;
						if(l[5]==c)
						{
							if(l[6]==-c) return f?4000:800;
							if(l[6]==0) return f?6000:3500;
							if(l[6]==c) return f?20000:15000;
						}
					}
				}
				if(l[1]==c)
				{
					if(l[0]==-c) 
					{
						if(l[4]==-c) return 0;
						if(l[4]==0)
						{
							if(l[5]==0) return 100;
							if(l[5]==c) return 150;
							if(l[5]==-c) return 50;
						}
						if(l[4]==c)
						{
							if(l[5]==0) return f?5000:3000;
							if(l[5]==-c) return f?3600:750;
							if(l[5]==c)
							{
								if(l[6]==0) return f?6000:3500;
								if(l[6]==-c) return f?3600:750;
								if(l[6]==c) return f?20000:15000;
							}
						}
					}
					if(l[0]==0)
					{
						if(l[4]==-c) return 100;
						if(l[4]==0) 
						{
							if(l[5]==0 || l[5]==-c) return f?900:650;
							if(l[5]==c)
							{
								if(l[6]==-c ||l[6]==0) return f?900:650;
								if(l[6]==c) return f?5000:3000;
							}
						}
						if(l[4]==c)
						{
							if(l[5]==0) return f?5000:3000;
							if(l[5]==-c) return f?3600:750;
							if(l[5]==c) 
							{
								if(l[6]==0) return f?6000:3500;
								if(l[6]==c) return f?20000:15000;
								if(l[6]==-c) return f?4000:800;
							}
						}
					}
					if(l[0]==-c)
					{
						if(l[4]==-c) return 0;
						if(l[4]==0)
						{
							if(l[5]==-c || l[5]==0) return 100;
							if(l[5]==c)
							{
								if(l[6]==-c) return 100;
								if(l[6]==0) return f?900:650;
								if(l[6]==c) return f?3600:750;
							}
						}
						if(l[4]==c)
						{
							if(l[5]==-c) return f?3600:750;
							if(l[5]==0) return f?5000:3000;
							if(l[5]==c) 
							{
								if(l[6]==-c) return f?5000:3000;
								if(l[6]==0) return f?6000:3500;
								if(l[6]==c) return f?20000:15000;
							}
						}
					}
				}
			}
			if(l[2]==c)
			{
				if(l[1]==-c) 
				{
					if(l[4]==-c ||l[5]==-c) return 0;
					if(l[4]==0)
					{
						if(l[5]==0) return 100;
						if(l[5]==c) return f?3600:750;
					}
					if(l[4]==c)
					{
						if(l[5]==0) return f?4000:800;
						if(l[5]==c) return f?20000:15000;
					}
				}
				if(l[1]==0)
				{
					if(l[0]==c)
					{
						if(l[4]==c) return f?10000:3300;
						if(l[4]==-c) return f?3600:750;
						if(l[4]==0) return f?5000:3000;
					}
					if(l[0]==-c || l[0]==0)
					{
						if(l[4]==-c) return 0;
						if(l[4]==0)
						{
							if(l[5]==0) return f?950:700;
							if(l[5]==-c) return 100;
							if(l[5]==c)
							{
								if(l[6]==0 || l[6]==c) return f?5000:3000;
								if(l[6]==-c) return f?3600:750;
							}
						}
						if(l[4]==c)
						{
							if(l[5]==c) return f?20000:15000;
							if(l[5]==0) return f?6000:3500;
							if(l[5]==-c) return f?4000:800;
						}
					}
				}
				if(l[1]==c)
				{
					if(l[0]==c) return f?20000:15000;
					if(l[0]==0) 
					{
						if(l[4]==c) return f?20000:15000;
						if(l[4]==0) return f?6000:3500;
						if(l[4]==-c) return f?4000:800;
					}
					if(l[0]==-c)
					{
						if(l[4]==c) return f?20000:15000;
						if(l[4]==0) return f?4000:800;
						if(l[4]==-c) return 0;
					}
				}
			}
		}
		if(l[3]==0)
		{
			if(l[2]==-c || l[2]==0)
			{
				if(l[4]==-c || l[5]==-c ||l[6]==-c) return 0;
				if(l[4]==0)
				{
					if(l[5]==0) return 0;
					if(l[5]==c) 
					{
						if(l[6]==0) return 40;
						if(l[6]==c)
						{
							if(l[7]==0) return f?900:650;
							if(l[7]==-c) return 100;
							if(l[7]==c) return f?3600:750;
						}
					}
				}
				if(l[4]==c)
				{
					if(l[5]==0)
					{
						if(l[6]==0) 
						{
							if(l[7]==-c) return f?(l[2]==0?60:10):(l[2]==0?50:10);
							if(l[7]==0 ||l[7]==c) return f?60:50;
						}
						if(l[6]==c)
						{
							if(l[7]==0) return f?900:650;
							if(l[7]==-c) return 100;
							if(l[7]==c) return f?5000:3000;
						}
					}
					if(l[5]==c)
					{
						if(l[6]==0)
						{
							if(l[7]==-c) return f?(l[2]==0?950:100):(l[2]==0?700:100);
							if(l[7]==0) return f?950:700;
							if(l[7]==c) return f?5000:3000;
						}
						if(l[6]==c)
						{
							if(l[7]==0) return f?6000:3500;
							if(l[7]==-c) return f?4000:800;
							if(l[7]==c) return f?20000:15000;
						}
					}
				}
			}
			if(l[2]==c)
			{
				if(l[1]==-c)
				{
					if(l[4]==-c || l[5]==-c) return 0;
					if(l[4]==0)
					{
						if(l[5]==0) return 10;
						if(l[5]==c)
						{
							if(l[6]==0) return f?40:35;
							if(l[6]==-c) return 10;
							if(l[6]==c) 
							{
								if(l[7]==-c) return f?3600:750;
								if(l[7]==0 || l[7]==c) return f?5000:3000;
							}
						}
					}
					if(l[4]==c)
					{
						if(l[5]==0)
						{
							if(l[6]==-c || l[6]==0) return 100;
							if(l[6]==c) 
							{
								if(l[7]==-c) return 100;
								if(l[7]==0) return f?900:650;
								if(l[7]==c) return f?5000:3000;
							}
						}
						if(l[5]==c)
						{
							if(l[6]==0)
							{
								if(l[7]==0 || l[7]==c) return f?5000:3000;
								if(l[7]==-c) return f?3600:750;
							}
							if(l[6]==-c) return f?3600:750;
							if(l[6]==c)
							{
								if(l[7]==c) return f?20000:15000;
								if(l[7]==0) return f?6000:3500;
								if(l[7]==-c) return f?3600:750;
							}
						}
					}
				}
				if(l[1]==0)
				{
					if(l[4]==-c)
					{
						if(l[0]==-c) return 0;
						else return 10;
					}
					if(l[4]==0)
					{
						if(l[5]==-c || l[5]==0) return 10;
						if(l[5]==c)
						{
							if(l[6]==-c) return 10;
							if(l[6]==0) return 15;
							if(l[6]==c)
							{
								if(l[7]==0) return f?900:650;
								if(l[7]==-c) return 100;
								if(l[7]==c) return f?5000:3000;
							}
						}
					}
					if(l[4]==c)
					{
						if(l[5]==-c) return 100;
						if(l[5]==0) return f?900:650;
						if(l[5]==c) 
						{
							if(l[6]==0) return f?5000:3000;
							if(l[6]==-c) return f?3600:750;
							if(l[6]==c)
							{
								if(l[7]==-c) return f?4000:800;
								if(l[7]==0) return f?6000:3500;
								if(l[7]==c) return f?20000:15000;
							}
						}
					}
				}
				if(l[1]==c)
				{
					if(l[0]==-c) 
					{
						if(l[4]==-c) return 0;
						if(l[4]==0)
						{
							if(l[5]==0 || l[5]==-c) return 100;
							if(l[5]==c)
							{
								if(l[6]==-c) return 100;
								if(l[6]==0) return 110;
								if(l[6]==c)
								{
									if(l[7]==-c) return 100;
									if(l[7]==0) return f?900:650;
									if(l[7]==c) return f?5000:3000;
								}
							}
						}
						if(l[4]==c)
						{
							if(l[5]==c && l[6]==c)
							{
								if(l[7]==c) return f?20000:15000;
								if(l[7]==-c) return f?3600:750;
								if(l[7]==0) return f?6000:3500;
							}
							else return f?3600:750;
						}
					}
					if(l[0]==0)
					{
						if(l[4]==-c) return 100;
						if(l[4]==0) return f?900:650;
						if(l[4]==c)
						{
							if(l[5]==-c) return f?3600:750;
							if(l[5]==0) return f?5000:3000;
							if(l[5]==c)
							{
								if(l[6]==-c) return f?3600:750;
								if(l[6]==0) return f?5100:3100;
								if(l[6]==c)
								{
									if(l[7]==-c) return f?4000:800;
									if(l[7]==0) return f?6000:3500;
									if(l[7]==c) return f?20000:15000;
								}
							}
						}
					}
					if(l[0]==c)
					{
						if(l[4]==-c) return f?360:75;
						if(l[4]==0) return f?3600:750;
						if(l[4]==c)
						{
							if(l[5]==-c) return f?3600:750;
							if(l[5]==0) return f?3600:750;
							if(l[5]==c)
							{
								if(l[6]==-c) return f?3600:750;
								if(l[6]==0) return f?3600:750;
								if(l[6]==c)
								{
									if(l[7]==-c) return f?4000:800;
									if(l[7]==0) return f?6000:3500;
									if(l[7]==c) return f?20000:15000;
								}
							}
						}
					}
				}
			}
		}
		return 0;
	}
	
	public void aiMove()
	{
		selected = AI();
		move(selected.x, selected.y);
	}
	
	public Point getSelected()
	{
		return selected;
	}

	public Point getLast()
	{
		if(used.empty()) {moved = false; return new Point(0, 0);}
		else return used.peek();
	}
}


class Point
{
	public int x, y;

	public Point(){};
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}