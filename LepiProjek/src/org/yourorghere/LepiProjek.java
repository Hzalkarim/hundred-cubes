package org.yourorghere;



import com.sun.opengl.util.FPSAnimator;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.WindowAdapter;

import java.awt.event.WindowEvent;

import javax.media.opengl.GL;

import javax.media.opengl.GLAutoDrawable;

import javax.media.opengl.GLCanvas;

import javax.media.opengl.GLEventListener;

import javax.media.opengl.glu.GLU;

import javax.swing.JFrame;

import javax.swing.SwingUtilities;



@SuppressWarnings("serial")

public class LepiProjek extends GLCanvas implements GLEventListener, KeyListener {

    // Define constants for the top-level container



    private static String TITLE = "Buat Laptop";

    private static final int CANVAS_WIDTH = 320 * 4;  // width of the drawable

    private static final int CANVAS_HEIGHT = 240 * 4; // height of the drawable

    private static final int FPS = 50; // animator's target frames per second

    // Setup OpenGL Graphics Renderer

    private GLU glu;  // for the GL Utility

    private float anglePyramid = 0.0f;  // rotational angle in degree for pyramid

    private float angleCube = 0.0f;     // rotational angle in degree for cube

    private float speedPyramid = 2.0f;  // rotational speed for pyramid

    private float speedCube = -1.5f;    // rotational speed for cube

    private boolean useLight = false;

    /** The entry main() method to setup the top-level container and animator */

    public static void main(String[] args) {

        // Run the GUI codes in the event-dispatching thread for thread safety

        SwingUtilities.invokeLater(new Runnable() {



            @Override

            public void run() {

                // Create the OpenGL rendering canvas

                GLCanvas canvas = new LepiProjek();

                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));



                // Create a animator that drives canvas' display() at the specified FPS.

                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);



                // Create the top-level container

                final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame

                frame.getContentPane().add(canvas);

                frame.addWindowListener(new WindowAdapter() {



                    @Override

                    public void windowClosing(WindowEvent e) {

                        // Use a dedicate thread to run the stop() to ensure that the

                        // animator stops before program exits.

                        new Thread() {



                            @Override

                            public void run() {

                                if (animator.isAnimating()) {

                                    animator.stop();

                                }

                                System.exit(0);

                            }

                        }.start();

                    }

                });

                frame.setTitle(TITLE);

                frame.pack();

                frame.setVisible(true);

                animator.start(); // start the animation loop

            }

        });

    }



    /** Constructor to setup the GUI for this Component */

    public LepiProjek() {

        this.addGLEventListener(this);

    }



    // ------ Implement methods declared in GLEventListener ------

    /**

     * Called back immediately after the OpenGL context is initialized. Can be used

     * to perform one-time initialization. Run only once.

     */

    @Override

    public void init(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();   // get the OpenGL graphics context

        glu = new GLU();                         // get GL Utilities

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color

        gl.glClearDepth(1.0f);      // set clear depth value to farthest

        gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing

        gl.glDepthFunc(GL.GL_LEQUAL);  // the type of depth test to do

        gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction

        gl.glShadeModel(GL.GL_SMOOTH); // blends colors nicely, and smoothes out lighting
        
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);  
        gl.glEnable(GL.GL_NORMALIZE);
        
        
        float[] specularLight = {.2f, 0f, 0f,0f };    
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specularLight, 0); 
        
        float[] ambientLight = { 0f, 0f, 1f,0f };
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); 

        float[] diffuseLight = { 1f,0f,0f,0f };
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);

    }



    /**

     * Call-back handler for window re-size event. Also called when the drawable is

     * first set to visible.

     */

    @Override

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL gl = drawable.getGL();  // get the OpenGL 2 graphics context



        if (height == 0) {

            height = 1;   // prevent divide by zero

        }

        float aspect = (float) width / height;



        // Set the view port (display area) to cover the entire window

        gl.glViewport(0, 0, width, height);



        // Setup perspective projection, with aspect ratio matches viewport

        gl.glMatrixMode(GL.GL_PROJECTION);  // choose projection matrix

        gl.glLoadIdentity();             // reset projection matrix

        glu.gluPerspective(60.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar



        // Enable the model-view transform

        gl.glMatrixMode(GL.GL_MODELVIEW);

        gl.glLoadIdentity(); // reset

    }



    /**

     * Called back by the animator to perform rendering.

     */

    @Override

    public void display(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();  // get the OpenGL 2 graphics context
        
        
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glLoadIdentity();  // reset the model-view matrix

        /**for (float i = -10f; i<=10f; i += 1.5f){
            autoCube(gl, .5f + (0.6f * Math.abs(i) / 10), angleCube * (i/10), 0, i+1f, -15f, 0.8f);
        }
        
        for (float i = -10f; i<=10f; i += 1.5f){
            autoCube(gl, .5f + (0.6f * Math.abs(i) / 10), angleCube * (i/10), i+1f, 0, -15f, 0.9f);
        }**/
        float rr = 20f;
        for (float f = rr; f >= -rr; f -= .7f){
            autoRingOfCube(20f - Math.abs(f), gl, .5f - 0.3f*(f/rr), angleCube * (1 - 0.8f*(f/rr)), 0, (float)Math.sqrt(rr*rr-(f-rr)*(f-rr)) - 13f, -50f, 0.3f + 0.6f*(f/rr));
        }
        
        float rrr = 7f;
        for (float f = rrr; f >= -rrr; f -= .7f){
            autoRingOfCube(8f - Math.abs(f), gl, .3f - 0.2f*(f/rrr), angleCube * (1 - 0.9f*(f/rrr)), 0, (float)Math.sqrt(rrr*rrr+(f)*(f))*1.5f-3f, -50f, 0.7f- 0.25f*(f/rrr));
            
            autoCube(gl, 0.1f + 0.95f*(f/rrr), angleCube * (1 - 0.9f*(f/rrr)), 0, (float)Math.sqrt(rrr*rrr+(f)*(f))*1.5f+7f, -50f, 0.7f+ 0.25f*(f/rrr));
        }
        
        for (float f = 0; f >= -20f; f-=1){
            autoRingOfCube(17f, gl, 0.5f, angleCube * (1 - 0.7f*(f/-20)), 0, f-13f, -50f, 0.5f-0.5f*(f/-10));
        }
        
        for (float f = 0; f < 40f; f += 1){
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), -24f, f-17f, -50f, 0.6f+0.4f*(f/40));
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), -26f, f-17f, -48f, 0.6f+0.3f*(f/40));
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), -28f, f-17f, -50f, 0.6f+0.4f*(f/40));
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), -26f, f-17f, -52f, 0.6f+0.3f*(f/40));
            
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), 24f, f-17f, -50f, 0.6f+0.4f*(f/40));
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), 26f, f-17f, -48f, 0.6f+0.3f*(f/40));
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), 28f, f-17f, -50f, 0.6f+0.4f*(f/40));
            autoCube(gl, 0.4f, angleCube * (1 - 0.7f*(f/30)), 26f, f-17f, -52f, 0.6f+0.2f*(f/40));
        }
        
        
        angleCube += speedCube;
    }
    
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_Z:
                useLight = true;
                break;
            case KeyEvent.VK_X:
                useLight = false;
                break;
            case KeyEvent.VK_UP:
                speedCube += 15f;
                
                break;
            case KeyEvent.VK_DOWN:
                speedCube -= 15f;
                break;
        }
        repaint();
    }
    
    public void autoRingOfCube(float radius, GL gl, float s, float r, float tx, float ty, float tz, float c){
        for (float x = -radius; x <= radius; x+=1.5f){
            double y = Math.sqrt(radius*radius - (x-tx)*(x-tx)) + ty;
            autoCube(gl, s, r, x+tx, ty, (float)y+tz, c);
        }
    }
    
    public void autoCube(GL gl, float s, float r, float tx, float ty, float tz, float c){
        gl.glLoadIdentity(); // reset the current model-view matrix
        gl.glTranslatef(tx, ty, tz); // translate right and into the screen
        gl.glRotatef(r, 0, 1.0f, 0); // rotate about the x, y and z-axes

        gl.glBegin(GL.GL_QUADS); // of the color cube

        // Top-face
        gl.glColor3f(c, c, c); // green
        gl.glVertex3f(s, s, -s);
        gl.glVertex3f(-s, s, -s);
        gl.glVertex3f(-s, s, s);
        gl.glVertex3f(s, s, s);

        // Bottom-face
        gl.glVertex3f(s, -s, s);
        gl.glVertex3f(-s, -s, s);
        gl.glVertex3f(-s, -s, -s);
        gl.glVertex3f(s, -s, -s);

        // Front-face
        gl.glVertex3f(s, s, s);
        gl.glVertex3f(-s, s, s);
        gl.glVertex3f(-s, -s, s);
        gl.glVertex3f(s, -s, s);

        // Back-face
        gl.glVertex3f(s, -s, -s);
        gl.glVertex3f(-s, -s, -s);
        gl.glVertex3f(-s, s, -s);
        gl.glVertex3f(s, s, -s);

        // Left-face
        gl.glVertex3f(-s, s, s);
        gl.glVertex3f(-s, s, -s);
        gl.glVertex3f(-s, -s, -s);
        gl.glVertex3f(-s, -s, s);

        // Right-face
        gl.glVertex3f(s, s, -s);
        gl.glVertex3f(s, s, s);
        gl.glVertex3f(s, -s, s);
        gl.glVertex3f(s, -s, -s);

        gl.glEnd(); // of the color cube
    }



    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {

        throw new UnsupportedOperationException("Not supported yet.");

    }

    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void keyReleased(KeyEvent e) {
        if (e.isActionKey()){
            return;
        }
    }

}


