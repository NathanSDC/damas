package main.java.nathansdc.damas.ui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import main.java.nathansdc.damas.util.KeyboardHandler;
import main.java.nathansdc.damas.util.MouseHandler;

public class Window extends JFrame {

    // Serial version UID for serialization
    public static final long serialVersionUID = 1L;

    // Remember previous window size when toggling fullscreen
    private int prevWidth = -1;
    private int prevHeight = -1;

    // Current fullscreen state
    private boolean isFullscreen = false;

    // Preferred window dimensions
    private int preferredHeight;
    private int preferredWidth;

    public Window(String title){
        super(title);
    }

    public Window(String title, int width, int height){
        super(title);
        this.preferredWidth = width;
        this.preferredHeight = height;

        setSize(new Dimension(width, height));
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));

        addMouseListener(new MouseHandler());
        addKeyListener(new KeyboardHandler());
    }
    
    /**
     * Toggle or set fullscreen on this JFrame.
     * Uses the GraphicsDevice full-screen API when available; otherwise falls back to maximized window.
     *
     * @param fullscreen true to enter fullscreen, false to exit
     */
    public void setFullscreen(boolean fullscreen) {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        if (this.isFullscreen == fullscreen) {
            // no-op
            return;
        }

        this.isFullscreen = fullscreen;

        if (fullscreen) {
            // save current size
            this.prevWidth = getWidth();
            this.prevHeight = getHeight();

            // To change undecorated we must dispose the frame first
            dispose();
            setUndecorated(true);

            if (gd.isFullScreenSupported()) {
                // enter exclusive full-screen mode
                gd.setFullScreenWindow(this);
            } else {
                // fallback: maximize and make visible
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setVisible(true);
            }
        } else {
            // exit full screen
            if (gd.getFullScreenWindow() == this) {
                gd.setFullScreenWindow(null);
            }

            // must dispose to change undecorated flag
            dispose();
            setUndecorated(false);

            // restore previous size if available, otherwise preferred
            int w = (this.prevWidth > 0) ? this.prevWidth : this.preferredWidth;
            int h = (this.prevHeight > 0) ? this.prevHeight : this.preferredHeight;
            setSize(w, h);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    /** Convenience toggle */
    public void toggleFullscreen() {
        setFullscreen(!this.isFullscreen);
    }

}
