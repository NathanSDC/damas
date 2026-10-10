package main.java.nathansdc.damas.ui;

import java.awt.Component;
import java.awt.Dimension;

import main.java.nathansdc.damas.util.MouseHandler;

public class GhostButton extends Component {

    public enum type {
        NEW,
        CONTINUE,
        LINK,
        EXIT,
        PLAY_AGAINST_AI_MODE,
        ACCEPT_PLAY_MODE_CHANGE,
        DECLINE_PLAY_MODE_CHANGE,
        ACCEPT_REDIRECT,
        DECLINE_REDIRECT,
        PLAYER_WON_NEW_GAME,
        PLAYER_WON_EXIT,
        PLAYER_WON_MENU
    }

    private type t;

    public GhostButton(){}

    public GhostButton(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setSize(getPreferredSize());
    }
    
    {
        addMouseListener(new MouseHandler(this));
    }

    public void setT(type t) {
        this.t = t;
    }

    public type getT() {
        return t;
    }
    
}
