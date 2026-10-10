package main.java.nathansdc.damas.util;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.Board;
import main.java.nathansdc.damas.ui.GhostButton;

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
            if (parent != null) {
                handleGhostButtonClick(e);
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
        if (parent != null) {
            if (parent instanceof GhostButton) {
                GhostButton p = (GhostButton) parent;
                switch (p.getT()) {
                    case NEW:
                        App.scene.nb = true;
                        break;
                    case CONTINUE:
                        App.scene.cb = true;
                        break;
                    case LINK:
                        App.scene.lb = true;
                        break;
                    case EXIT:
                        App.scene.eb = true;
                        break;
                    case PLAY_AGAINST_AI_MODE:
                        App.scene.ab = true;
                        break;
                    case ACCEPT_PLAY_MODE_CHANGE:
                        App.scene.acb = true;
                        break;
                    case DECLINE_PLAY_MODE_CHANGE:
                        App.scene.db = true;
                        break;
                    case ACCEPT_REDIRECT:
                        App.scene.arb = true;
                        break;
                    case DECLINE_REDIRECT:
                        App.scene.drb = true;
                        break;
                    case PLAYER_WON_NEW_GAME:
                        App.scene.pnb = true;
                        break;
                    case PLAYER_WON_EXIT:
                        App.scene.peb = true;
                        break;
                    case PLAYER_WON_MENU:
                        App.scene.pmb = true;
                        break;
                    default:
                        break;
                }
            }
        }

        App.update();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (parent != null) {
            if (parent instanceof GhostButton) {
                GhostButton p = (GhostButton) parent;
                switch (p.getT()) {
                    case NEW:
                        App.scene.nb = false;
                        break;
                    case CONTINUE:
                        App.scene.cb = false;
                        break;
                    case LINK:
                        App.scene.lb = false;
                        break;
                    case EXIT:
                        App.scene.eb = false;
                        break;
                    case PLAY_AGAINST_AI_MODE:
                        App.scene.ab = false;
                        break;
                    case ACCEPT_PLAY_MODE_CHANGE:
                        App.scene.acb = false;
                        break;
                    case DECLINE_PLAY_MODE_CHANGE:
                        App.scene.db = false;
                        break;
                    case ACCEPT_REDIRECT:
                        App.scene.arb = false;
                        break;
                    case DECLINE_REDIRECT:
                        App.scene.drb = false;
                        break;
                    case PLAYER_WON_NEW_GAME:
                        App.scene.pnb = false;
                        break;
                    case PLAYER_WON_EXIT:
                        App.scene.peb = false;
                        break;
                    case PLAYER_WON_MENU:
                        App.scene.pmb = false;
                        break;
                    default:
                        break;
                }
            }
        }

        App.update();
    }

    private void handleSceneClick(MouseEvent e) {
        double a = App.squareSize + (App.offsetX / Board.size);

        clickX = (int) (Math.ceil(e.getX() / a)) - 1;
        clickY = (int) (Math.ceil((e.getY() - (App.offsetY - App.offsetX)) / a)) - 1;

        if ((App.isGameActive && !App.playerWon) && (clickY < 9 && clickY >= 0)
                && ((App.playingAgainstAI && App.board.currentTurn == 1) || !App.playingAgainstAI)) {
            if (!App.board.isEmptyTarget(clickX, clickY)) {
                if (!hasSelectedPiece) {
                    App.scene.auxBoard = App.board.showavailableMovements(clickX, clickY);

                    selectedX = clickX;
                    selectedY = clickY;
                    hasSelectedPiece = true;
                } else {
                    if (App.scene.auxBoard[clickX][clickY] == 2) {
                        // Movimento de captura clicando na peça do oponente
                        int diffX = selectedX - clickX;
                        int diffY = selectedY - clickY;

                        App.board.movePiece(selectedX, selectedY, clickX + diffX, clickY + diffY);
                        App.scene.clearAuxBoard();
                    } else {
                        // Quaisquer cliques fora de uma marcada ou na propria peça desativa a seleçao
                        // atual
                        hasSelectedPiece = false;
                        selectedX = 0;
                        selectedY = 0;
                        App.scene.clearAuxBoard();
                    }
                }
            } else {
                if (hasSelectedPiece) {
                    if (App.scene.auxBoard[clickX][clickY] == 1) {
                        if (App.board.movePiece(selectedX, selectedY, clickX, clickY)) {
                            hasSelectedPiece = false;
                            selectedX = 0;
                            selectedY = 0;
                            App.scene.clearAuxBoard();
                        }
                    } else if (App.scene.auxBoard[clickX][clickY] == 2) {
                        // Movimento de captura clicando na peça do oponente
                        int diffX = selectedX - clickX;
                        int diffY = selectedY - clickY;

                        App.board.movePiece(selectedX, selectedY, clickX + diffX, clickY + diffY);
                    } else {
                        hasSelectedPiece = false;
                        selectedX = 0;
                        selectedY = 0;
                        App.scene.clearAuxBoard();
                    }
                }
            }
        }

    }

    private void handleGhostButtonClick(MouseEvent e) {
        if (parent != null) {
            if (parent instanceof GhostButton) {
                GhostButton p = (GhostButton) parent;
                switch (p.getT()) {
                    case NEW:
                        App.board.resetBoard();
                        App.scene.clearAuxBoard();
                        App.isGameActive = true;
                        App.alreadyStartedAGame = true;
                        App.playerWon = false;
                        App.player1Won = false;
                        App.player2Won = false;
                        App.isMenuActive = false;
                        break;
                    case CONTINUE:
                        App.isGameActive = true;
                        App.alreadyStartedAGame = true;
                        App.isMenuActive = false;
                        break;
                    case LINK:
                        App.isRedirectConfirmationDialogueActive = true;
                        break;
                    case EXIT:
                        System.exit(0);
                        break;
                    case PLAY_AGAINST_AI_MODE:
                        if (App.alreadyStartedAGame && !App.playerWon) {
                            App.isPlayModeChangeConfirmationDialogueActive = true;
                        } else {
                            App.playingAgainstAI = !App.playingAgainstAI;
                        }
                        break;
                    case ACCEPT_PLAY_MODE_CHANGE:
                        App.board.resetBoard();
                        App.scene.clearAuxBoard();
                        App.isGameActive = true;
                        App.alreadyStartedAGame = true;
                        App.playerWon = false;
                        App.player1Won = false;
                        App.player2Won = false;
                        App.isMenuActive = false;
                        App.isPlayModeChangeConfirmationDialogueActive = false;

                        App.playingAgainstAI = !App.playingAgainstAI;
                        break;
                    case DECLINE_PLAY_MODE_CHANGE:
                        App.isPlayModeChangeConfirmationDialogueActive = false;
                        break;
                    case ACCEPT_REDIRECT:
                        App.openBrowserLink();
                        App.isRedirectConfirmationDialogueActive = false;
                        break;
                    case DECLINE_REDIRECT:
                        App.isRedirectConfirmationDialogueActive = false;
                        break;
                    case PLAYER_WON_NEW_GAME:
                        App.board.resetBoard();
                        App.scene.clearAuxBoard();
                        App.isGameActive = true;
                        App.alreadyStartedAGame = true;
                        App.playerWon = false;
                        App.player1Won = false;
                        App.player2Won = false;
                        App.isMenuActive = false;
                        App.isPlayerWonDialogueActive = false;
                        break;
                    case PLAYER_WON_EXIT:
                        System.exit(0);
                        break;
                    case PLAYER_WON_MENU:
                        App.isGameActive = false;
                        App.isMenuActive = true;
                        App.playerWon = true;
                        App.player1Won = false;
                        App.player2Won = false;
                        App.isPlayerWonDialogueActive = false;
                        break;
                    default:
                        break;
                }
            }
        }

        App.update();
    }

}
