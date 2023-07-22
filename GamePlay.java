package Brickbraking;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
public class GamePlay extends JPanel implements ActionListener,KeyListener{
    private boolean play=false;
    private int totalBricks=21;
    private Timer timer;
    private int score=0;
    private int delay=8;
    private int ballposX=100;
    private int ballposY=350;
    private int ballXdir=-3;
    private int ballYdir=-4;
    private int playerX=300;
    private MapGenarator map;
    public GamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        timer=new Timer(delay,this);
        timer.start();
        map=new MapGenarator(3,7);
    }
    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);
        //borders
        g.setColor(Color.cyan);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(0, 3, 3, 592);
        g.fillRect(683, 3, 3, 592);
        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 6);
        //bricks
        map.draw((Graphics2D)g);
        //ball
        g.setColor(Color.red);
        g.fillOval(ballposX, ballposY, 15, 15);
        //score
        g.setColor(Color.green);
        g.setFont(new Font("serif",Font.BOLD,20));
        g.drawString("Score :"+score, 580, 30);
        //game over
        if(ballposY>=570) {
            play=false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,40));
            g.drawString("GameOver!!  Score: "+score, 150, 300);
            g.setColor(Color.green);
            g.setFont(new Font("serif",Font.BOLD,40));
            g.drawString("Press Enter to Restart", 150, 350);
        }
        if(totalBricks==0) {
            play=false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.green);
            g.setFont(new Font("serif",Font.BOLD,40));
            g.drawString("You Won  Score: "+score, 150, 260);

            g.setFont(new Font("serif",Font.BOLD,40));
            g.drawString("Press Enter to Restart", 150, 310);
        }
    }
    private void moveLeft() {
        play=true;
        playerX-=40;
    }
    private void moveRight() {
        play=true;
        playerX+=40;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode()==KeyEvent.VK_LEFT) {
            if(playerX<=0) playerX=0;
            else moveLeft();
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
            if(playerX>=592) playerX=592;
            else moveRight();
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER) {
            if(!play) {
                score=0;
                totalBricks=21;
                ballposX=120;
                ballposY=300;
                ballXdir=-2;
                ballYdir=-3;
                playerX=300;

                map=new MapGenarator(3,7);
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(play) {
            if(ballposX<=0) {
                ballXdir=-ballXdir;
            }
            if(ballposX>=665) {
                ballXdir=-ballXdir;
            }
            if(ballposY<=0) {
                ballYdir=-ballYdir;
            }
//		if(ballposY>590) {
//
//		}
            Rectangle ballRect=new Rectangle(ballposX,ballposY,20,20);
            Rectangle paddleRect=new Rectangle(playerX,550,100,8);


            if(ballRect.intersects(paddleRect)) {
                ballYdir=-ballYdir;
            }

            A:for(int i=0;i<map.map.length;i++) {
                for(int j=0;j<map.map[0].length;j++) {
                    if(map.map[i][j]>0) {
                        int width=map.brickWidth;
                        int height=map.brickHeight;
                        int brickXpos=80+j*width;
                        int brickYpos=50+i*height;
                        Rectangle brickRect=new Rectangle(brickXpos,brickYpos,width,height);
                        if(ballRect.intersects(brickRect)) {
                            map.setBrick(0, i, j);
                            totalBricks=totalBricks-1;
                            score=score+10;
                            if(ballposX+19<=brickXpos || ballposX+1>=brickXpos+width) {
                                ballXdir=-ballXdir;
                            }
                            else {
                                ballYdir=-ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            ballposX+=ballXdir;
            ballposY+=ballYdir;
        }
        repaint();
    }

}

