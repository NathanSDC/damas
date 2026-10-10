package main.java.nathansdc.damas;

import java.awt.Desktop;
import java.net.URI;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

import main.java.nathansdc.damas.ia.AI;
import main.java.nathansdc.damas.jogo.Board;
import main.java.nathansdc.damas.ui.Scene;
import main.java.nathansdc.damas.ui.Window;

public class App implements Runnable {

    public static boolean running = true;
    public static boolean isGameActive = false;
    public static boolean alreadyStartedAGame = false;

    // misc booleans
    public static boolean isMenuActive = true;
    public static boolean isPlayModeChangeConfirmationDialogueActive = false;
    public static boolean isRedirectConfirmationDialogueActive = false;
    public static boolean isPlayerWonDialogueActive = false;

    public static AI ai = new AI();

    public static boolean playerWon = false;
    public static boolean player1Won = false;
    public static boolean player2Won = false;

    public int player1score = 0;
    public int player2score = 0;

    /**
     * <pre>
     * false = player/player
     * true = player/ai
     */
    public static boolean playingAgainstAI = false;

    public static int offsetX = 15;
    public static int offsetXL = 15;
    public static int offsetY = 39;

    public static String appTitle = "Damas";

    public static int squareSize = 100;

    /**
     * PieceSize é uma constante relativa ao valor de SquareSize e possui sempre 80%
     * do valor total do mesmo
     */
    public static int pieceSize = (int) (squareSize * 0.8);

    public static Board board = new Board();
    public static Scene scene = new Scene();

    public static int windowWidth = Board.size * squareSize + offsetX;
    public static int windowHeight = Board.size * squareSize + offsetY;

    private static Random random = new Random();
    private static Timer t = new Timer(random.nextInt(1000) + 500, (a) -> {
        App.ai.execute(App.ai.choseMovement(App.board));
    });
    public static void update() {
        try {
            if ((board.getPlayer1PieceCount() == 0 || board.getPlayer2PieceCount() == 0) && !playerWon) {
                t.stop();
                
                if (board.getPlayer1PieceCount() == 0) {
                    player2Won = true;
                } else if (board.getPlayer2PieceCount() == 0) {
                    player1Won = true;
                }
                
                isMenuActive = false;
                isGameActive = false;
                playerWon = true;
                isPlayerWonDialogueActive = true;
            }

            pieceSize = (int) (squareSize * 0.8);
            scene.update();

            if (App.playingAgainstAI && App.board.currentTurn == 2 && !App.playerWon) {
                t.setRepeats(false);
                t.start();
                t.setDelay(random.nextInt(1000) + 500);// (0 ~ 1000) + (500)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
