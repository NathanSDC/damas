package main.java.nathansdc.damas;

import java.awt.Desktop;
import java.net.URI;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import main.java.nathansdc.damas.ia.AI;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Piece;
import main.java.nathansdc.damas.ui.Scene.ActiveDialogue;
import main.java.nathansdc.damas.ui.ColorScheme;
import main.java.nathansdc.damas.ui.InterfaceMetrics;
import main.java.nathansdc.damas.ui.Scene;
import main.java.nathansdc.damas.ui.Window;
import main.java.nathansdc.damas.util.ExecutionListener;

public class App implements Runnable {

    // App game control
    public static boolean running = true;
    public static boolean isGameActive = false;
    public static boolean alreadyStartedAGame = false;

    // user settings
    public static Settings settings = Settings.getInstance();
    
    // menu booleans
    public static boolean isMenuActive = true;
    public static ActiveDialogue activeDialogue = ActiveDialogue.MENU;

    public static AI ai = new AI(settings.getDifficulty());
    
    public static ColorScheme colorScheme = ColorScheme.getInstance();
    
    // player won tests
    public static boolean playerWon = false;
    public static boolean player1Won = false;
    public static boolean player2Won = false;
    
    private static boolean isPlayer1OutOfMoves = false;
    private static boolean isPlayer2OutOfMoves = false;

    public static InterfaceMetrics getMetrics() {
        return InterfaceMetrics.getInstance();
    }
   
    public static BoardManager boardManager = new BoardManager();
    public static ExecutionListener executionListener = boardManager;

    public static Scene scene = new Scene();
    
    private static Random random = new Random();
    private static Timer t = new Timer(random.nextInt(1000) + 500, (a) -> {
        // Caso o jogador pause o jogo logo após fazer uma jogada, o algoritimo não
        // executará sua jogada até que o jogo tenha sido despausado.
        if(App.isGameActive)
            App.ai.execute(App.ai.choseMovement(App.boardManager));
    });

    public static void update() {
        try {
            // Verifica se um dos jogadores está atualmente sem movimentos possiveis
            if (boardManager.currentTurn == 1) {
                isPlayer1OutOfMoves = boardManager.getPlayablePieces().size() == 0;
            } else {
                isPlayer2OutOfMoves = boardManager.getPlayablePieces().size() == 0;
            }

            if (((boardManager.getBoard().getPieceCount(Piece.PLAYER1) == 0 || boardManager.getBoard().getPieceCount(Piece.PLAYER2) == 0 || isPlayer1OutOfMoves
                    || isPlayer2OutOfMoves)) && !playerWon) {
                t.stop();

                if (boardManager.getBoard().getPieceCount(Piece.PLAYER1) == 0 || isPlayer1OutOfMoves) {
                    player2Won = true;
                } else if (boardManager.getBoard().getPieceCount(Piece.PLAYER2) == 0 || isPlayer2OutOfMoves) {
                    player1Won = true;
                }

                isMenuActive = false;
                isGameActive = false;
                playerWon = true;
                App.activeDialogue = ActiveDialogue.SOMEONE_WON;
            }

            InterfaceMetrics.getInstance().update();
            scene.update();

            // Se ativo notifica a IA de que pode fazer sua jogada
            if (settings.isPlaying_against_ai() && boardManager.currentTurn == 2 && !playerWon && !isPlayer2OutOfMoves) {
                t.setRepeats(false);
                t.start();
                // Para maior sensação de realismo e fluidez das jogadas adiciona uma tempo de espera
                // aleatório entre uma jogada e outra
                t.setDelay(random.nextInt(1000) + 500);// 0.5s ~ 1.5s de espera entre jogadas
            }

            settings.save();
            App.colorScheme.setCurrentTheme(settings.getApp_theme());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startNewGame() {
        boardManager.reset();
        scene.clearAuxiliarBoard();
        isGameActive = true;
        alreadyStartedAGame = true;
        playerWon = false;
        player1Won = false;
        player2Won = false;
        isPlayer1OutOfMoves = false;
        isPlayer2OutOfMoves = false;
        isMenuActive = false;
        App.activeDialogue = ActiveDialogue.MENU;
    }

    public static void main(String[] args) throws Exception {

        colorScheme.setCurrentTheme(settings.getApp_theme());
        scene.backupTheme = colorScheme.getCurrentTheme();

        Window w = new Window(settings.getApp_name(), getMetrics().getWindowWidth(), getMetrics().getWindowHeight());
        w.setResizable(false); // impede a janela de mudar seu tamanhho padrao

        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        w.add(scene);
        w.pack();
        w.setTitle(settings.getApp_name());
        w.setVisible(true);
    }

    // Animação de movimentos e afins
    @Override
    public void run() {
        while (running) {
            try {

                Thread.sleep(1000 / 120); // 120 FPS
            } catch (Exception e) {

            }
        }
    }

    public static void openBrowserLink() {
        if (!Desktop.isDesktopSupported()) {
            // TODO: Adicionar janela de erro para o usuario
        }

        Desktop desktop = Desktop.getDesktop();

        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI("https://github.com/nathansdc/damas"));
            } catch (Exception e) {
                // TODO: Adicionar janela de erro para o usuario
                e.printStackTrace();
            }
        } else {
            // TODO: Adicionar janela de erro para o usuario
        }
    }
}
