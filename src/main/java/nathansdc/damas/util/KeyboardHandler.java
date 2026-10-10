package main.java.nathansdc.damas.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.java.nathansdc.damas.App;

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
                App.isPlayModeChangeConfirmationDialogueActive = false;
                App.isRedirectConfirmationDialogueActive = false;
            } else if (App.alreadyStartedAGame && App.playerWon) {
                App.isPlayerWonDialogueActive = false;
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
