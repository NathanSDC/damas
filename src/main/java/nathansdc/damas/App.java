package main.java.nathansdc.damas;

import java.awt.Desktop;
import java.net.URI;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import main.java.nathansdc.damas.ia.AI;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Piece;
import main.java.nathansdc.damas.ui.InterfaceMetrics;
import main.java.nathansdc.damas.ui.Scene;
import main.java.nathansdc.damas.ui.Window;
import main.java.nathansdc.damas.util.ExecutionListener;

public class App implements Runnable {

    
    public static boolean running = true;
    public static boolean isGameActive = false;
    public static boolean alreadyStartedAGame = false;
    
    // menu booleans
    public static boolean isMenuActive = true;
    public static boolean isPlayModeChangeConfirmationDialogueActive = false;
    public static boolean isRedirectConfirmationDialogueActive = false;
    public static boolean isPlayerWonDialogueActive = false;
    
    public static AI ai = new AI();

    // player won tests
    public static boolean playerWon = false;
    public static boolean player1Won = false;
    public static boolean player2Won = false;
    
    public static boolean isPlayer1OutOfMoves = false;
    public static boolean isPlayer2OutOfMoves = false;
    
    /**
     * <pre>
     * false = player/player
     * true = player/ai
    */
    public static boolean playingAgainstAI = false;
   
    public static int offsetX = 15;
    public static int offsetY = 39;

    public static String appTitle = "Damas";

    public static int squareSize = 100;

    public static InterfaceMetrics getMetrics() {
        return InterfaceMetrics.getInstance();
    }
    
    /**
     * PieceSize é uma constante relativa ao valor de SquareSize e possui sempre 80%
     * do valor total do mesmo
    */
   public static int pieceSize = (int) (squareSize * 0.8);
   
    public static BoardManager boardManager = new BoardManager();
    public static ExecutionListener executionListener = boardManager;
    public static Scene scene = new Scene();
    
    public static int windowWidth = BoardManager.size * squareSize + offsetX;
    public static int windowHeight = BoardManager.size * squareSize + offsetY;
    
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
                isPlayerWonDialogueActive = true;
            }

            pieceSize = (int) (squareSize * 0.8);
            scene.update();

            // Se ativo notifica a IA de que pode fazer sua jogada
            if (playingAgainstAI && boardManager.currentTurn == 2 && !playerWon && !isPlayer2OutOfMoves) {
                t.setRepeats(false);
                t.start();
                // Para maior sensação de realismo e fuildez das jogadas adiciona uma tempo de espera
                // aleatório entre uma jogada e outra
                t.setDelay(random.nextInt(1000) + 500);// 0.5s ~ 1.5s de espera entre jogadas
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startNewGame() {
        boardManager.reset();
        scene.clearAuxBoard();
        isGameActive = true;
        alreadyStartedAGame = true;
        playerWon = false;
        player1Won = false;
        player2Won = false;
        isPlayer1OutOfMoves = false;
        isPlayer2OutOfMoves = false;
        isMenuActive = false;
        isPlayerWonDialogueActive = false;
        isPlayModeChangeConfirmationDialogueActive = false;
        isRedirectConfirmationDialogueActive = false;
    }

    public static void main(String[] args) throws Exception {
        Window w = new Window(appTitle, windowWidth, windowHeight);
        // TODO: Adicionar mais responsividade a janela
        w.setResizable(false); // impede a janela de mudar seu tamanhho padrao

        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        w.add(scene);
        w.pack();
        w.setTitle(appTitle);
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
