package main.java.nathansdc.damas.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Movement;
import main.java.nathansdc.damas.ui.InvisibleButton;

public class MouseHandler implements MouseListener {

    private Component parent = null;

    public MouseHandler() {
    }

    public MouseHandler(Component parent) {
        this.parent = parent;
    }

    public static boolean hasSelectedPiece = false;

    private int clickX = 0;
    private int clickY = 0;

    private int selectedX = 0;
    private int selectedY = 0;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            if((parent!=null) && parent instanceof InvisibleButton){
                ((InvisibleButton)parent).run();
            } else {
                handleSceneClick(e);
            }
        }

        App.update();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if ((parent!=null) && parent instanceof InvisibleButton) {
            ((InvisibleButton)parent).onHover = true;
            ((InvisibleButton)parent).setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        App.update();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if ((parent!=null) && parent instanceof InvisibleButton) {
            ((InvisibleButton)parent).onHover = false;
            ((InvisibleButton)parent).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        App.update();
    }

    private void handleSceneClick(MouseEvent e) {
        double a = App.getMetrics().getSquareSize() + (App.getMetrics().getOffsetX() / BoardManager.size);

        clickX = (int) (Math.ceil(e.getX() / a)) - 1;
        clickY = (int) (Math.ceil((e.getY() - (App.getMetrics().getOffsetY() - App.getMetrics().getOffsetX())) / a)) - 1;

        if ((App.isGameActive && !App.playerWon) && !App.boardManager.getBoard().isOutOfBounds(clickX, clickY)
                && ((App.playingAgainstAI && App.boardManager.currentTurn == 1) || !App.playingAgainstAI)) {
            if (!App.boardManager.getBoard().isThereEmpty(clickX, clickY)) {
                if (!hasSelectedPiece) {
                    App.scene.auxiliarBoard = App.boardManager.showavailableMovements(App.boardManager.getBoard(), clickX, clickY);
                    selectedX = clickX;
                    selectedY = clickY;
                    hasSelectedPiece = true;
                } else {
                    if (App.scene.auxiliarBoard[clickX][clickY] == 2) {
                        // Movimento de captura clicando na peça do oponente
                        Movement m = new Movement(selectedX, selectedY, clickX, clickY, App.boardManager.getBoard());
                        List<Movement> moves = Movement.getMovements(App.boardManager.getBoard(), selectedX, selectedY, App.boardManager.currentTurn);
                        for (Movement move : moves) {
                            if (move.equals(m)) {
                                Movement.execute(move, App.boardManager);
                                hasSelectedPiece = false;
                                selectedX = 0;
                                selectedY = 0;
                                break;
                            }
                        }
                        App.scene.clearAuxiliarBoard();
                    } else {
                        // Quaisquer cliques fora de uma marcada ou na propria peça desativa a seleçao
                        // atual
                        hasSelectedPiece = false;
                        selectedX = 0;
                        selectedY = 0;
                        App.scene.clearAuxiliarBoard();
                    }
                }
            } else {
                if (hasSelectedPiece) {
                    if (App.scene.auxiliarBoard[clickX][clickY] == 1) {
                        Movement m = new Movement(selectedX, selectedY, clickX, clickY, App.boardManager.getBoard());
                        List<Movement> moves = Movement.getMovements(App.boardManager.getBoard(), selectedX, selectedY, App.boardManager.currentTurn);
                        for (Movement move : moves) {
                            if (move.equals(m)) {
                                if (Movement.execute(move, App.boardManager)) {
                                    hasSelectedPiece = false;
                                    selectedX = 0;
                                    selectedY = 0;
                                    App.scene.clearAuxiliarBoard();
                                    break;
                                }   
                            }
                        }
                    }
                }
            }
        }
    }
}
