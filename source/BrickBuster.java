package source;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class BrickBuster extends JFrame implements KeyListener{
	
	private static final long serialVersionUID = 1L;
	
	int MAX_HEIGHT=500;			//Height and width of the window
	int MAX_WIDTH=500;			
	int ballx,bally,ball_radius,speedx,speedy;			// Ball Parameters
	int pedalx,pedaly,pedal_width,pedal_height,pedal_speed,pedal_oldx,pedal_oldy;	// Pedal parameters
	int brick_width,brick_height;		// Brick Parameters
	int[][] x;		// Brick X coordinates
	int[] y;		// Brick Y Coordinates
	int key1=37,key2=39;	// key codes for values left and right arrow keys 
	int i=0;
	boolean game_over=false;	// to know whether game is over or not
	Graphics out;
	int score=0;
	int life;
	
	/*
	 * 	speedx -> represents the direction of the ball along the x axis, 
	 * 	if it is +ve ball moves right if -ve it moves left
	 * 
	 * speedy -> represents the direction of the ball along the y axis;
	 * if it is +ve the ball is moving down else it is moving up
	 * 
	 * ballx,bally -> are x,y coordinate of the ball
	 * 
	 * pedalx,pedaly -> are xy coordinates of the pedal
	 * 
	 * pedal_speed is the speed at which the pedal moves
	 * 
	 * XXX_height, XXX_width ->	are heights and widths of the name XXX
	 * */
	
	
	BrickBuster()
	{
		setSize(MAX_WIDTH,MAX_HEIGHT);
		setVisible(true);
		setResizable(false);
		setName("Brick Buster");
		addKeyListener(this);
		x=new int[4][];;
		y=new int[4];;
		initialize();
	}
	
	// Initializing all parameters to there default values
	
	public void initialize()
	{
		ballx=200;
		bally=425;
		ball_radius=10;
		speedx=ball_radius/2;
		speedy=-ball_radius/2;
		pedalx=180;
		pedaly=451;
		pedal_width=80;
		pedal_height=20;
		pedal_speed=14;
		pedal_oldy=451;
		i=0;
		brick_width=30;
		brick_height=10;
		life=4;
		
		for(int i=0;i<4;i++)			// i represents the row of the bricks						
		{
			y[i]=100+(brick_height+5)*i; 		// the y coordinate in a particular row of bricks remains constant					
			if(i%4==0)		// first row
			{
				x[i]=new int[2];
				int left=MAX_HEIGHT/2-brick_width-5;			//initializing the brick's x coordinates.
				for(int j=0;j<2;j++)							
				{
					x[i][j]=left+(brick_width+5)*j;
				}
			}
			else if(i%4==1)		//second row
			{
				x[i]=new int[6];
			
				int left=MAX_HEIGHT/2-3*(brick_width+5);
				for(int j=0;j<6;j++)
				{
					x[i][j]=left+(brick_width+5)*j;
				}
			}
			else if(i%4==2)		// third row
			{
				x[i]=new int[10];
				int left=MAX_HEIGHT/2-5*(brick_width+5);
				for(int j=0;j<10;j++)
				{
					x[i][j]=left+(brick_width+5)*j;
				}
			}
			else if(i%4==3)			// fourth row
			{
				x[i]=new int[12];
				int left=MAX_HEIGHT/2-6*(brick_width+5);
				for(int j=0;j<12;j++)
				{
					x[i][j]=left+(brick_width+5)*j;
				}
			}
		}
	}
	
	public void paint(Graphics g)
	{
		if(i==0)
		{
			g.setColor(Color.BLACK);			// make the background of the window black
			g.fillRect(0, 0, 500, 500);
			i=1;
			g.setColor(Color.YELLOW);
			g.fillRoundRect(pedalx, pedaly, pedal_width, pedal_height, 10, 10);	// draw the pedal for the first time
			for(int i=0;i<life;i++)
			{
				out.fillOval(40+i*(ball_radius+3), 475, ball_radius, ball_radius);
			}
			g.setColor(Color.RED);
			g.drawString("Life - ",10,485);
			for(int i=0;i<x.length;i++)
			{
				for(int j=0;j<x[i].length;j++)		// drawing all the bricks
				{
					g.fillRect(x[i][j], y[i], brick_width, brick_height);	
				}
			}
			g.setColor(Color.GREEN);	
			g.drawString("Score - "+score, 400, 485);
		}
	}
	
	public void eraseBrick(int i,int j)
	{
		out=getGraphics();
		out.setColor(Color.BLACK);			// erasing the brick at ith row jth column
		out.fillRect(x[i][j], y[i], brick_width, brick_height);
		score++;
		out.fillRect(380,455,100,30);
		out.setColor(Color.GREEN);
		out.drawString("Score - "+score, 400, 485);
		int k=0;
		boolean flag=true;
		for(k=0;k<x.length && flag;k++)
		{
		for(int l=0;l<x[k].length;l++)
		{
		if(x[k][l]!=0)
			{flag=false;
			break;}
		}
		}
		System.out.print(k+","+x.length);
		if(k==x.length)
		{
			game_over=true;
			out.setColor(Color.RED);
			out.drawString("YOU WON!!!", 200, 250);
		}
			
	}
	
	
	public void moveBall()
	{
		out=getGraphics();
		if(!game_over)
		{
		ballx += speedx;
		bally += speedy;		// move the ball to the next position		
		collision();			// check for any collisions
		out.setColor(Color.YELLOW);
		out.fillOval(ballx, bally, ball_radius, ball_radius);	// draw the ball in the new location
		try{
			Thread.sleep(20);		// this is to create some delay in the ball motion
		}
		catch(Exception e)
		{
			System.out.println("Exception in moving ball");
		}
		out.setColor(Color.BLACK);			//erase the ball
		out.fillOval(ballx, bally, ball_radius, ball_radius);
		out.setColor(Color.YELLOW);
		out.fillRoundRect(pedalx, pedaly, pedal_width, pedal_height, 10, 10);	
		}
		else if(life<0)
		{
			out.setColor(Color.YELLOW);
			out.fillOval(ballx, bally, ball_radius, ball_radius);
			out.setColor(Color.RED);
			out.drawString("YOU LOST!!", 200, 250);
		}
		
	}
	public void collision()
	{
		if(ballx<(ball_radius/2) || ballx>(MAX_WIDTH-ball_radius))	// check if ball hits the vertical boundary
		{
			speedx=-speedx;		// change the balls direction
		}
		if(bally<(ball_radius/2))			// check if ball hits the top of the window
		{
			speedy=-speedy; // change the ball direction
		}
		if( bally>(pedaly-ball_radius))		 // check if ball is going to hit the pedal
		{
			if(ballx>=(pedalx-ball_radius) && ballx<=(pedalx+pedal_width))	// check if ball is with in the region of the pedal 
			speedy=-speedy;
			else
				{
					if(!decreaseLife())
						game_over=true;
					else
					{
						resetBall();
					}
				}
		}
		int dist;
		for(int i=x.length-1,collision=0;i>=0;i--)	// check if the ball hits any brick
		{
			if(collision==1)
				break;			// if any collision is detected break the loop
			if(speedy<0)		// if ball is moving up i.e.
			dist=(bally-(y[i]+brick_height+3));                       		
			else
				dist=(bally-y[i]);	// if ball is moving down
			
			/* here we find the perpendicular distance from the ball to any row of bricks 
			 * 
			 * if ax+by+c=0 is the line and (x1,y1) is a point then
			 * perpendicular distance is 
			 * 
			 * 	ax1+by1+c/sqrt(square(a)+square(b))
			 * 
			 * In our case the line equation is 'y=c'  where c= the y-cord of any row of bricks 
			 * and the point is (ballx,bally)
			 * 
			*/
			if(dist<5 && dist>-5)	// check if the perpendicular distance is below a threshold value  
			{
				int distx1,distx2;
			for (int j=0,center=ballx+ball_radius/2; j<x[i].length; j++)	
			{
				
				if(x[i][j]>0 && center>=(x[i][j]) && center<=(x[i][j]+brick_width))		// check if the ball is with in a bricks region
				{
			//		System.out.println(ballx+":"+x[i][j]+":"+dist);		
					eraseBrick(i,j);		//erase the brick
					x[i][j]=0;				// neutralize its X-cord to stop further processing of this brick
					speedy=-speedy;			// change the direction
					collision=1;			// marks that the collision has occured
					break;
				}			
			}
			}
		}
		
	}
	public boolean decreaseLife()
	{
		life--;
		out=getGraphics();
		out.setColor(Color.BLACK);
		out.fillRect(40, 475, 100, 10);
		out.setColor(Color.YELLOW);
		for(int i=0;i<life;i++)
		{
			out.fillOval(40+i*(ball_radius+3), 475, ball_radius, ball_radius);
		}
		return life<0?false:true;
	}
	public void resetBall()
	{
		ballx=200;
		bally=425;
		speedx=-speedx;
		speedy=-speedy;
		out=getGraphics();
		out.setColor(Color.BLACK);
		out.fillRoundRect(pedalx, pedaly, pedal_width, pedal_height, 10, 10);	
		pedalx=180;
		pedaly=451;
		pedal_speed=10;
		pedal_oldy=451;	
	}
	public void keyPressed(KeyEvent e) {
		if(!game_over)
		{
		if(e.getKeyCode()==key1)	// check if it is left arrow key
		{
			pedal_oldx=pedalx;
			pedalx=pedalx-pedal_speed;		//move the pedal to a new location
			if(pedalx<0)
				pedalx=0;
		}	
		else if(e.getKeyCode()==key2)	// check if it is right arrow key
		{
			pedal_oldx=pedalx;
			if((pedalx+pedal_width)<MAX_WIDTH)
				pedalx=pedalx+pedal_speed;			//move the pedal to a new location
		}
		else if(e.getKeyCode()==38)
		{
			pedal_speed=pedal_speed+2;			// increaser the pedal speed
		}
		else if(e.getKeyCode()==40)
		{
			if(pedal_speed> 5)
			pedal_speed=pedal_speed-2;			// decrease the pedal speed
		}
		out=getGraphics();
		out.fillRoundRect(pedal_oldx, pedal_oldy, pedal_width, pedal_height, 10, 10);		// erase the pedal at old location
		out.setColor(Color.YELLOW);
		out.fillRoundRect(pedalx, pedaly, pedal_width, pedal_height, 10, 10);		// draw the pedal in the new location
		}
}
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	public static void main(String[] args) {
		
		BrickBuster buster=new BrickBuster();		
		
		buster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	try{
			while(true)
			{	
				buster.moveBall();		// to move the ball continuously
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}
