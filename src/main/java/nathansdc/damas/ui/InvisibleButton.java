package main.java.nathansdc.damas.ui;

import java.awt.Component;
import java.awt.Dimension;

import main.java.nathansdc.damas.util.MouseHandler;

public class InvisibleButton extends Component {

    public boolean onHover;

    Runnable action;

    public InvisibleButton(Runnable action){
        this.action = action;
    }
    
    public InvisibleButton(int width, int height, Runnable action) {
        setPreferredSize(new Dimension(width, height));
        setSize(getPreferredSize());
    }

    {
        addMouseListener(new MouseHandler(this));
    }

    public void run(){
        action.run();
    }

    public boolean isOnHover() {
        return onHover;
    }

    public void setOnHover(boolean onHover) {
        this.onHover = onHover;
    }

}
