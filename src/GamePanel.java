import java.awt.*;

import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class GamePanel extends JPanel implements ActionListener
{
	
	static final int WIDTH = 900;
	static final int HEIGHT = 630;
	static final int UNIT_SIZE = 30;
	static final int GAME_UNITS = (WIDTH*HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 100;
	
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	
	int bodyparts = 3;
	int appleX, appleY;
	int appleseaten = 0;
	int PoisonTime;
	int PoisonAppleX, PoisonAppleY;
	int counter1 = 0;
	int GodTime;
	int GodAppleX, GodAppleY;
	int counter2 = 0;
	
	Random random;
	Timer timer;
	char direction = 'R';
	
	boolean running = false;
	boolean PoisonApplePresent =false;
	boolean GodApplePresent = false;
	
	GamePanel()
	{
		random = new Random();
		
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.black);
		this.setBorder(new MatteBorder(2,2,2,2,new Color(0,120,212)));
		this.setFocusable(true);
		this.addKeyListener(new newKeyAdapter());
		
		startGame();
	}
	
	public void startGame()
	{
		newApple();
		newPoisonApple();
		newGodApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g)
	{
		if(running)
		{
			g.setColor(Color.yellow);
			g.setFont(new Font(null,Font.PLAIN,30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("SCORE: "+appleseaten, (WIDTH-metrics.stringWidth("SCORE: "+appleseaten))/2, g.getFont().getSize());
			
			g.setColor(Color.red);
			g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);
			
			if(PoisonTime == appleseaten%10)
			{
				PoisonApplePresent = true;
				counter1 = 0;
			}
			
			if(PoisonApplePresent)
			{
				g.setColor(new Color(150,0,150));
				g.fillOval(PoisonAppleX,PoisonAppleY,UNIT_SIZE,UNIT_SIZE);
			}
			
			if(GodTime == appleseaten%20)
			{
				GodApplePresent = true;
				counter2 = 0;
			}
			
			if(GodApplePresent)
			{
				g.setColor(new Color(255,255,255));
				g.fillOval(GodAppleX,GodAppleY,UNIT_SIZE,UNIT_SIZE);
			}
			
			g.setColor(Color.blue);
			for(int i=0;i<bodyparts;i++)
			{
				if(i==0)
				{
					g.setColor(Color.blue);
				}
				else
				{
					g.setColor(new Color(100,100,255));
				}
				g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
			}
		}
		else
		{
			gameOver(g);
		}
	}
	
	public void newApple()
	{
		appleX = random.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE;
		appleY = random.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void newPoisonApple()
	{
		PoisonTime = random.nextInt(10);
		PoisonAppleX = random.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE;
		PoisonAppleY = random.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void newGodApple()
	{
		GodTime = random.nextInt(20);
		GodAppleX = random.nextInt(WIDTH/UNIT_SIZE)*UNIT_SIZE;
		GodAppleY = random.nextInt(HEIGHT/UNIT_SIZE)*UNIT_SIZE;
	}
	
	public void move()
	{
		for(int i=bodyparts;i>0;i--)
		{
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		if(direction=='R')
			x[0]=x[0]+UNIT_SIZE;
		if(direction=='L')
			x[0]=x[0]-UNIT_SIZE;
		if(direction=='U')
			y[0]=y[0]-UNIT_SIZE;
		if(direction=='D')
			y[0]=y[0]+UNIT_SIZE;
	}
	
	public void checkApple()
	{
		if((x[0]==appleX) && y[0]==appleY)
		{
			appleseaten++;
			bodyparts++;
			newApple();
		}
	}
	
	public void checkPoisonApple()
	{
		if((x[0]==PoisonAppleX) && y[0]==PoisonAppleY)
		{
			PoisonApplePresent = false;
			appleseaten--;
			appleseaten--;
			bodyparts++;
			newPoisonApple();
		}
		else
		{
			if(counter1 > 20)
			{
				PoisonApplePresent = false;
				newPoisonApple();
			}
		}
	}
	
	public void checkGodApple()
	{
		if((x[0]==GodAppleX) && y[0]==GodAppleY)
		{
			GodApplePresent = false;
			appleseaten = appleseaten+5;
			newGodApple();
		}
		else
		{
			if(counter2 > 15)
			{
				GodApplePresent = false;
				newGodApple();
			}
		}
	}
	
	public void checkCollision()
	{
		for(int i=1;i<bodyparts;i++)
		{
			if((x[0]==x[i]) && (y[0]==y[i]))
			{
				running = false;
			}
		}

		if(x[0]>WIDTH)
			running = false;
		if(x[0]<0)
			running = false;
		if(y[0]>HEIGHT)
			running = false;
		if(y[0]<0)
			running = false;
		if(!running)
		{
			timer.stop();
		}
	}
	
	public void gameOver(Graphics g)
	{
		g.setColor(Color.yellow);
		g.setFont(new Font(null,Font.PLAIN,50));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("SCORE: "+appleseaten, (WIDTH-metrics1.stringWidth("SCORE: "+appleseaten))/2, HEIGHT*3/4);
		
		g.setColor(Color.red);
		g.setFont(new Font(null,Font.PLAIN,70));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (WIDTH-metrics2.stringWidth("GAME OVER"))/2, HEIGHT*2/5);
	}
	
	class newKeyAdapter extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			int x = e.getKeyCode();
			if(x==KeyEvent.VK_LEFT)
			{
				if(direction != 'R')
				{
					direction = 'L';
				}
			}
			if(x==KeyEvent.VK_RIGHT)
			{
				if(direction != 'L')
				{
					direction = 'R';
				}
			}
			if(x==KeyEvent.VK_UP)
			{
				if(direction != 'D')
				{
					direction = 'U';
				}
			}
			if(x==KeyEvent.VK_DOWN)
			{
				if(direction != 'U')
				{
					direction = 'D';
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(running)
		{	
			move();
			checkApple();
			checkCollision();
			if(PoisonApplePresent)
			{
				checkPoisonApple();
				counter1 ++ ;
			}
			if(GodApplePresent)
			{
				checkGodApple();
				counter2 ++ ;
			}
		}
		repaint();
	}
}
