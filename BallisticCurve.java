package ballisticcurve;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class BallisticCurve extends JFrame {

    private static final double G = 9.8; //positive because Y axis is positive going down
    private int animationSpeed = 5; //millis. The smaller the faster
    private static int size = 900, ballDiameter = 10;
    private double startX, startY, ballX, ballY;
    private double xSpeed, ySpeed, lastPointX, lastPointY;
    private double time, deltaTime = 0.01 ; //in seconds
    private List<Point2D> curvePoints= new ArrayList<>();
    private Timer timer;

    BallisticCurve(){

        super("Balistic Curve");
        DrawBoard board  = new DrawBoard();
        add(board, BorderLayout.CENTER);

        ballX= lastPointX = startX = 50;
        ballY = lastPointY = startY = size - 100;
        getUserInput();

        timer = new Timer(animationSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                board.moveBall();
                board.repaint();
                if(! inBounds()) {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void getUserInput() {

        double angle = 45;//todo replace with user input + verification
        double speed = 100;
        xSpeed = speed * Math.cos(angle * (Math.PI / 180));
        ySpeed = speed * Math.sin(angle * (Math.PI / 180));
    }

    private boolean inBounds() {

        //ignore if ball exceeds height
        if((ballX < 0) || (ballX > (getWidth()))
                || ( ballY  > (getHeight() - ballDiameter) ) ) {
            return false;
        }

        return true;
    }

    class DrawBoard extends JPanel {

        public DrawBoard() {
            setPreferredSize(new Dimension(size, size));
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.RED);
            g2d.fillOval((int)ballX,(int)ballY,ballDiameter,ballDiameter);

            if((Math.abs(lastPointX - ballX)>=1) && (Math.abs(lastPointY - ballY)>=1) ) {
                curvePoints.add(new Point2D.Double(ballX, ballY));
                lastPointX = ballX; lastPointY = ballY;
            }

            drawCurve(g2d);
        }

        private void drawCurve(Graphics2D g2d) {

            g2d.setColor(Color.BLUE);
            for(int i=0; i < (curvePoints.size()-1); i++) {

                Point2D from = curvePoints.get(i);
                Point2D to = curvePoints.get(i+1);
                g2d.drawLine((int)from.getX(),(int)from.getY(), (int)to.getX(), (int)to.getY());
            }
        }

        private void moveBall() {

            ballX = startX + (xSpeed * time);
            ballY = startY - ((ySpeed *time)-(0.5 *G * Math.pow(time, 2))) ;
            time += deltaTime;
        }
    }

    public static void main(String[] args) {

        new BallisticCurve();
    }
}