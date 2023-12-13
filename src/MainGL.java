import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

import GraphicalObject.Cube;
import GraphicalObject.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

public class MainGL extends GLCanvas
        implements GLEventListener, KeyListener
{
    private ArrayList<GraphicalObject> enemies;

    private ArrayList<GraphicalObject> enemiesRemove;
    private ArrayList<GraphicalObject> bulletList;
    private ArrayList<GraphicalObject> removeBullet;
    private Cube ship;
    private boolean leftArrow, rightArrow, shootBullet;
    private boolean move = true;


    public static void main(String[] args)
    {
        GLCanvas canvas = new MainGL();
        canvas.setPreferredSize(new Dimension(800, 600));
        final JFrame frame = new JFrame();
        frame.getContentPane().add(canvas);
        frame.setTitle("Space invaders");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Animator animator = new Animator(canvas);
        animator.start();
        frame.requestFocusInWindow();
    }

    public MainGL() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
        this.enemies = new ArrayList<GraphicalObject>();
        this.enemiesRemove = new ArrayList<GraphicalObject>();
        this.bulletList = new ArrayList<GraphicalObject>();
        this.removeBullet = new ArrayList<GraphicalObject>();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -15.0f);
        gl.glPushMatrix();
        for (GraphicalObject cube: enemies){
            cube.display(gl);
        }
        gl.glPopMatrix();

        //Move enemies
        if (move){
            for (GraphicalObject cube : enemies) {
                cube.translate(-0.001f, -0.001f, 0);
            }
            move = false;
        }

        if (!move){
            for (GraphicalObject cube : enemies) {
                cube.translate(+0.001f, -0.001f, 0);
            }
            move = true;
        }

        //Move ship
        this.ship.display(gl);
        if(leftArrow){
            float moveShipX =0;
            moveShipX-=0.1f;
            ship.translate(moveShipX, 0, 0);
        }
        if(rightArrow){
            float moveShipX =0;
            moveShipX+=0.1f;
            ship.translate(moveShipX, 0, 0);
        }

        //Shoot bullets
        if(shootBullet){
            Cube bullets = new Cube(ship.getPosX(), ship.getPosY(), ship.getPosZ(), 0,0,0,0.1f,0f,1f,1f);
            bulletList.add(bullets);
            shootBullet = false;
        }

        for (GraphicalObject bullets : bulletList){
            bullets.translate(0f,0.1f,0f);
            bullets.display(gl);
        }

        //Collision
        for (GraphicalObject bullet : bulletList){
                for (GraphicalObject enemy: enemies){
                    float distance = enemy.distance(enemy, bullet);
                    if(distance >= 0 && distance <= 1){
                        if (distance >= 0 && distance <= 1) {
                            enemiesRemove.add(enemy);
                            removeBullet.add(bullet);
                        }
                    }
                }
        }


        //Erase enemies / bullets
        if (enemiesRemove != null){
            enemies.removeAll(enemiesRemove);
        }
        if (removeBullet != null){
            bulletList.removeAll(removeBullet);
        }



        //End
        //Distance ship/bullets
        for (GraphicalObject enemis : enemies){
            float distance = ship.distance(enemis, ship);
            if (distance >= 0 && distance <= 1){
                drawable.destroy();
                JFrame endGame = new JFrame("END");
                JLabel congrat = new JLabel("YOU LOSE");
                congrat.setHorizontalAlignment(SwingConstants.CENTER);
                endGame.add(congrat);
                endGame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
                endGame.setSize(250,100);
                endGame.setLocationRelativeTo(null);
                endGame.setVisible(true);
            }
        }
        if (enemies.isEmpty()){
            drawable.destroy();
            JFrame endGame = new JFrame("END");
            JLabel congrat = new JLabel("CONGRATULATION");
            congrat.setHorizontalAlignment(SwingConstants.CENTER);
            endGame.add(congrat);
            endGame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
            endGame.setSize(250,100);
            endGame.setLocationRelativeTo(null);
            endGame.setVisible(true);
        }
        gl.glPopMatrix();

    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        // Color background
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        // Initialize all graphical objects
        float positionX, positionY;
        positionX = -10;
        positionY = 7;
        for(int i = 17; i >= 0; i--){
            Cube createdCube = new Cube(positionX, positionY, -10, 0, 0, 0, 1f, 0.5f, 0, 0.5f);
            this.enemies.add(createdCube);
            positionX += 4;
            if (i%6 == 0){
                positionX = -10;
                positionY -= 3;
            }
        }
        this.ship = new Cube(0, -7, -10, 0,0,0,1f,0f,1f,1f);
    }

    @Override
    public void reshape(GLAutoDrawable drawable,
                        int x, int y, int width, int height) {
        // TODO Auto-generated method stub
        GL2 gl = drawable.getGL().getGL2();
        // Set the view area
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        glu.gluPerspective(45.0, (float)width/height,
                0.1, 100.0);
        // Enable the model view
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                leftArrow = true;
                break;
            case KeyEvent.VK_RIGHT:
                rightArrow = true;
                break;
            case KeyEvent.VK_SPACE:
                shootBullet = true;
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                leftArrow = false;
                break;
            case KeyEvent.VK_RIGHT:
                rightArrow = false;
                break;
            case KeyEvent.VK_SPACE:
                shootBullet = false;

                break;

        }
    }
}