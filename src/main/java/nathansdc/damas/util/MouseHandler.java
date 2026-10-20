package main.java.nathansdc.damas.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Movement;
import main.java.nathansdc.damas.ui.InvisibleButton;

public class MouseHandler implements MouseListener, MouseMotionListener {

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
            if ((parent != null) && parent instanceof InvisibleButton) {
                ((InvisibleButton) parent).run();
            } else if ((parent != null) && parent instanceof BoardManager) {
                handleInBoardClick(e);
            }
        }

        App.update();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if ((parent != null) && parent instanceof InvisibleButton) {
            ((InvisibleButton) parent).onHover = true;
            ((InvisibleButton) parent).setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        App.update();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if ((parent != null) && parent instanceof InvisibleButton) {
            ((InvisibleButton) parent).onHover = false;
            ((InvisibleButton) parent).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        App.update();
    }

    private void handleInBoardClick(MouseEvent e) {
        BoardManager boardManager = (BoardManager) parent;

        clickX = ((int) e.getX() / App.getMetrics().getSquareSize());
        clickY = ((int) e.getY() / App.getMetrics().getSquareSize());

        if (boardManager.getBoard().isOutOfBounds(clickX, clickY)) {
            System.err.println("How long are ur arms to touch this far? " + clickX + "x" + clickY);
            return;
        }

        if (!App.isGameActive) {
            System.err.println("How the heck are u here?");
            return;
        }

        if (!App.playerWon && ((App.settings.isPlaying_against_ai() && boardManager.currentTurn == 1)
                || !App.settings.isPlaying_against_ai())) {
            if (!boardManager.getBoard().isThereEmpty(clickX, clickY)) {
                if (!hasSelectedPiece) {
                    App.scene.auxiliarBoard = App.boardManager.showavailableMovements(App.boardManager.getBoard(),
                            clickX, clickY);
                    selectedX = clickX;
                    selectedY = clickY;
                    hasSelectedPiece = true;
                } else if (App.scene.auxiliarBoard[clickX][clickY] == 2) {
                    // Movimento de captura clicando na peça do oponente
                    Movement m = new Movement(selectedX, selectedY, clickX, clickY, boardManager.getBoard());
                    List<Movement> moves = Movement.getMovements(boardManager.getBoard(), selectedX, selectedY,
                            boardManager.currentTurn);
                    for (Movement move : moves) {
                        if (move.equals(m)) {
                            Movement.execute(move, boardManager);
                            hasSelectedPiece = false;
                            selectedX = -1;
                            selectedY = -1;
                            break;
                        }
                    }
                    App.scene.clearAuxiliarBoard();
                } else {
                    // Quaisquer cliques fora de uma marcada ou na propria peça desativa a seleçao
                    // atual
                    hasSelectedPiece = false;
                    selectedX = -1;
                    selectedY = -1;
                    App.scene.clearAuxiliarBoard();
                }
            } else {
                if (hasSelectedPiece) {
                    if (App.scene.auxiliarBoard[clickX][clickY] == 1) {
                        Movement m = new Movement(selectedX, selectedY, clickX, clickY, boardManager.getBoard());
                        List<Movement> moves = Movement.getMovements(boardManager.getBoard(), selectedX, selectedY,
                                boardManager.currentTurn);
                        for (Movement move : moves) {
                            if (move.equals(m)) {
                                if (Movement.execute(move, boardManager)) {
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

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
