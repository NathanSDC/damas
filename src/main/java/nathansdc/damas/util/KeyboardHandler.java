package main.java.nathansdc.damas.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.ui.Scene.ActiveDialogue;

public class KeyboardHandler implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P) {
            if (App.alreadyStartedAGame && App.isGameActive && !App.playerWon) {
                App.isGameActive = false;
                App.isMenuActive = true;
            } else if(App.alreadyStartedAGame && !App.isGameActive && !App.playerWon){
                App.isGameActive = true;
                App.isMenuActive = false;
                App.activeDialogue = ActiveDialogue.MENU;
            } else if (!App.alreadyStartedAGame && !App.isGameActive && !App.playerWon) {
                App.activeDialogue = ActiveDialogue.MENU;
            } else if (App.alreadyStartedAGame && App.playerWon) {
                App.isMenuActive = true;
                App.isGameActive = false;
                App.alreadyStartedAGame = false;
            }
        }

        App.update();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
}
