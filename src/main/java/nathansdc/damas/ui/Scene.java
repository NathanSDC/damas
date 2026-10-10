package main.java.nathansdc.damas.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;

import javax.swing.JPanel;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.Board;
import main.java.nathansdc.damas.jogo.Board.PieceType;

public class Scene extends JPanel {

    private static Board startBoard = App.board;

    private Font audiowide20;
    private Font audiowide30;
    private Font audiowide45;
    {
        try {
            File fontStream = new File(
                    App.class.getClassLoader().getResource("main/resources/Audiowide/Audiowide.ttf").getFile());
            audiowide20 = Font.createFont(Font.TRUETYPE_FONT, fontStream);

            // Pré derivações da fonte
            audiowide20 = audiowide20.deriveFont(20.0f);
            audiowide30 = audiowide20.deriveFont(30.0f);
            audiowide45 = audiowide20.deriveFont(45.0f);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 0 - impossivel alcançar / aciona desmarcamento
     * 1 - casa livre para a qual pode se mover
     * 2 - peça inimiga marcada para possivel captura
     * 3 - peça atualmente selecionada
     * </pre>
     */
    public Integer[][] auxBoard = new Integer[Board.size][Board.size];

    public void clearAuxBoard() {
        for (int i = 0; i < auxBoard.length; i++) {
            for (int j = 0; j < auxBoard.length; j++) {
                auxBoard[i][j] = 0;
            }
        }
    }

    {
        clearAuxBoard();
    }

    private boolean hasAlreadyAddMenuButtons = false;

    public boolean nb = false, cb = false, lb = false, eb = false, ab = false;
    private GhostButton newButton = new GhostButton();
    private GhostButton conButton = new GhostButton();
    private GhostButton lnkButton = new GhostButton();
    private GhostButton extButton = new GhostButton();
    private GhostButton paiButton = new GhostButton();
    {

        conButton.setT(GhostButton.type.CONTINUE);
        newButton.setT(GhostButton.type.NEW);
        paiButton.setT(GhostButton.type.PLAY_AGAINST_AI_MODE);
        lnkButton.setT(GhostButton.type.LINK);
        extButton.setT(GhostButton.type.EXIT);
    }

    /**
     * has Already Added Play Mode Change Confirmation Buttons
     */
    private boolean hasAAPMCCButtons = false;

    public boolean acb = false, db = false;
    private GhostButton acceptButton = new GhostButton();
    private GhostButton declineButton = new GhostButton();
    {
        acceptButton.setT(GhostButton.type.ACCEPT_PLAY_MODE_CHANGE);
        declineButton.setT(GhostButton.type.DECLINE_PLAY_MODE_CHANGE);
    }

    /**
     * has Already Added Redirect Confirmation Buttons
     */
    private boolean hasAARCButtons = false;

    public boolean arb = false, drb = false;
    private GhostButton acceptRedButton = new GhostButton();
    private GhostButton declineRedButton = new GhostButton();
    {
        acceptRedButton.setT(GhostButton.type.ACCEPT_REDIRECT);
        declineRedButton.setT(GhostButton.type.DECLINE_REDIRECT);
    }

    /**
     * has already added player won buttons
     */
    private boolean hasAAPWButtons = false;

    public boolean pnb = false, peb = false, pmb = false;
    private GhostButton wonNewButton = new GhostButton();
    private GhostButton wonExtButton = new GhostButton();
    private GhostButton wonMenButton = new GhostButton();
    {
        wonNewButton.setT(GhostButton.type.PLAYER_WON_NEW_GAME);
        wonExtButton.setT(GhostButton.type.PLAYER_WON_EXIT);
        wonMenButton.setT(GhostButton.type.PLAYER_WON_MENU);
    }

    public Scene() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // Garante que as boradas dos circulos e linhas sejam suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Fonte padrão do programa
        g2d.setFont(audiowide20);

        // Desenha o fundo
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                if (auxBoard[i][j] == 1) { // Pode se mover para >
                    g.setColor(Color.decode("#699279"));
                } else if (auxBoard[i][j] == 2) { // Peça inimiga marcada como possivel captura
                    g.setColor(Color.decode("#cf7f8d"));
                } else if (auxBoard[i][j] == 3) { // Peça atual selecionada
                    g.setColor(Color.decode("#72bbc5"));
                } else {
                    if ((i + j) % 2 == 0) {
                        g.setColor(Color.decode("#c08b60"));
                    } else {
                        g.setColor(Color.decode("#dfc399"));
                    }
                }
                g.fillRect(i * 100, j * 100, 100, 100);
            }
        }

        // Desenha as peças no tabuleiro
        for (int i = 0; i < Board.size; i++) {
            for (int j = 0; j < Board.size; j++) {
                drawPieceAt(g2d, i, j, startBoard.getPieceAt(i, j));
            }
        }

        if (!App.isGameActive) {
            // Pinta uma cortina semi-transparente sobre o tabuleiro
            g2d.setColor(new Color(0, 0, 0, 153));
            g2d.fillRect(0, 0, App.windowWidth, App.windowHeight);

            // TOD O: Debug Active
            // System.out.println("isMenuActive: " + App.isMenuActive
            //         + ";\nisPlayModeChangeConfirmationDialogueActive: " + App.isPlayModeChangeConfirmationDialogueActive
            //         + ";\nisRedirectConsfirmationDialogueActive: " + App.isRedirectConfirmationDialogueActive
            //         + ";\nisPlayerWonDialogueActive: " + App.isPlayerWonDialogueActive);

            if (App.isMenuActive && !App.isPlayModeChangeConfirmationDialogueActive
                    && !App.isRedirectConfirmationDialogueActive && !App.isPlayerWonDialogueActive) {

                if (hasAAPMCCButtons) {
                    acceptButton.setBounds(0, 0, 0, 0);
                    declineButton.setBounds(0, 0, 0, 0);
                }

                if (hasAARCButtons) {
                    acceptRedButton.setBounds(0, 0, 0, 0);
                    declineRedButton.setBounds(0, 0, 0, 0);
                }

                if (hasAAPWButtons) {
                    wonNewButton.setBounds(0, 0, 0, 0);
                    wonExtButton.setBounds(0, 0, 0, 0);
                    wonMenButton.setBounds(0, 0, 0, 0);
                }

                drawMenu(g2d);
            } else if (App.isMenuActive && App.isPlayModeChangeConfirmationDialogueActive
                    && !App.isRedirectConfirmationDialogueActive && !App.isPlayerWonDialogueActive) {

                if (hasAlreadyAddMenuButtons) {
                    newButton.setBounds(0, 0, 0, 0);
                    conButton.setBounds(0, 0, 0, 0);
                    paiButton.setBounds(0, 0, 0, 0);
                    lnkButton.setBounds(0, 0, 0, 0);
                    extButton.setBounds(0, 0, 0, 0);
                }

                if (hasAARCButtons) {
                    acceptRedButton.setBounds(0, 0, 0, 0);
                    declineRedButton.setBounds(0, 0, 0, 0);
                }

                if (hasAAPWButtons) {
                    wonNewButton.setBounds(0, 0, 0, 0);
                    wonExtButton.setBounds(0, 0, 0, 0);
                    wonMenButton.setBounds(0, 0, 0, 0);
                }

                drawConfirmationDiallogue1(g2d);
            } else if (App.isMenuActive && !App.isPlayModeChangeConfirmationDialogueActive
                    && App.isRedirectConfirmationDialogueActive && !App.isPlayerWonDialogueActive) {

                if (hasAlreadyAddMenuButtons) {
                    newButton.setBounds(0, 0, 0, 0);
                    conButton.setBounds(0, 0, 0, 0);
                    paiButton.setBounds(0, 0, 0, 0);
                    lnkButton.setBounds(0, 0, 0, 0);
                    extButton.setBounds(0, 0, 0, 0);
                }

                if (hasAAPMCCButtons) {
                    acceptButton.setBounds(0, 0, 0, 0);
                    declineButton.setBounds(0, 0, 0, 0);
                }

                if (hasAAPWButtons) {
                    wonNewButton.setBounds(0, 0, 0, 0);
                    wonExtButton.setBounds(0, 0, 0, 0);
                    wonMenButton.setBounds(0, 0, 0, 0);
                }

                drawConfirmationDiallogue2(g2d);
            } else if (!App.isMenuActive && !App.isPlayModeChangeConfirmationDialogueActive
                    && !App.isRedirectConfirmationDialogueActive && App.isPlayerWonDialogueActive) {

                if (hasAlreadyAddMenuButtons) {
                    newButton.setBounds(0, 0, 0, 0);
                    conButton.setBounds(0, 0, 0, 0);
                    paiButton.setBounds(0, 0, 0, 0);
                    lnkButton.setBounds(0, 0, 0, 0);
                    extButton.setBounds(0, 0, 0, 0);
                }

                if (hasAAPMCCButtons) {
                    acceptButton.setBounds(0, 0, 0, 0);
                    declineButton.setBounds(0, 0, 0, 0);
                }

                if (hasAARCButtons) {
                    acceptRedButton.setBounds(0, 0, 0, 0);
                    declineRedButton.setBounds(0, 0, 0, 0);
                }

                drawPlayerWonDialogue(g2d);
            }
        } else {
            if (hasAlreadyAddMenuButtons) {
                newButton.setBounds(0, 0, 0, 0);
                conButton.setBounds(0, 0, 0, 0);
                paiButton.setBounds(0, 0, 0, 0);
                lnkButton.setBounds(0, 0, 0, 0);
                extButton.setBounds(0, 0, 0, 0);
            }

            if (hasAAPMCCButtons) {
                acceptButton.setBounds(0, 0, 0, 0);
                declineButton.setBounds(0, 0, 0, 0);
            }

            if (hasAARCButtons) {
                acceptRedButton.setBounds(0, 0, 0, 0);
                declineRedButton.setBounds(0, 0, 0, 0);
            }

            if (hasAAPWButtons) {
                wonNewButton.setBounds(0, 0, 0, 0);
                wonExtButton.setBounds(0, 0, 0, 0);
                wonMenButton.setBounds(0, 0, 0, 0);
            }
        }

        g.dispose();
    }

    private void drawMenu(Graphics2D g2d) {
        // Desenha a caixa de opções/menu
        // 800 - 450 = 350 / 2 = 175
        // 800 - 600 = 200 / 2 = 100
        int menuW = 450;
        int menuH = 540;

        int menuX = ((App.windowWidth - menuW) / 2) - (App.offsetX / 2);
        int menuY = ((App.windowHeight - menuH) / 2) - (App.offsetY / 2);

        g2d.setColor(Color.decode("#e7da9f"));
        g2d.fillRect(menuX, menuY, menuW, menuH);

        // Reajuste de tamnho da fonte para o titulo

        g2d.setColor(Color.BLACK);
        g2d.setFont(audiowide45);

        if (!App.alreadyStartedAGame || App.playerWon) {
            int titleW = g2d.getFontMetrics().stringWidth(App.appTitle);
            int titleC = ((App.windowWidth - titleW) / 2) - (App.offsetX / 2);
            g2d.drawString(App.appTitle, titleC, menuY + 100);
        } else {
            int titleW = g2d.getFontMetrics().stringWidth("Pausado");
            int titleC = ((App.windowWidth - titleW) / 2) - (App.offsetX / 2);
            g2d.drawString("Pausado", titleC, menuY + 100);
        }

        int ghostButtonX = menuX + 75;
        int pad = 10;
        int ghostButtonWidth = menuW - 150;
        int ghostButtonHeight = 65;

        String[] ghostButtonStrings = new String[] { "Novo Jogo", (App.windowWidth + "x" + App.windowHeight),
                "Github", "Sair" };

        g2d.setFont(audiowide30);
        for (int i = 0; i < ghostButtonStrings.length; i++) {

            if (i == 0) {
                if (!App.alreadyStartedAGame || App.playerWon) {
                    ghostButtonStrings[i] = "Novo Jogo";
                    if (hasAlreadyAddMenuButtons) {
                        setComponentZOrder(conButton, 1);
                        setComponentZOrder(newButton, 0);
                    }
                } else {
                    ghostButtonStrings[i] = "Continue";
                    if (hasAlreadyAddMenuButtons) {
                        setComponentZOrder(conButton, 0);
                        setComponentZOrder(newButton, 1);
                    }
                }
            }

            if (i == 1) {
                if (App.playingAgainstAI) {
                    ghostButtonStrings[i] = "Contra IA";
                } else {
                    ghostButtonStrings[i] = "Contra Player";
                }
            }

            int baseHeight = ((menuY + 175) + (i * (ghostButtonHeight + pad)));

            if (((cb || nb) && i == 0) || (ab && i == 1) || (lb && i == 2) || (eb && i == 3)) {
                g2d.setColor(Color.decode("#c08b60"));
                drawHoverButton(g2d, ghostButtonStrings[i], ghostButtonX, baseHeight, ghostButtonWidth,
                        ghostButtonHeight);
            } else {
                g2d.setColor(Color.decode("#000000"));
                drawButton(g2d, ghostButtonStrings[i], ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
            }

            if (!hasAlreadyAddMenuButtons) {
                switch (i) {
                    case 0:
                        conButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(conButton);
                        newButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(newButton);
                        break;
                    case 1:
                        paiButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(paiButton);
                        break;
                    case 2:
                        lnkButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(lnkButton);
                        break;
                    case 3:
                        extButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(extButton);
                        break;
                    default:
                        break;
                }
            } else {
                if (i == 0) {
                    newButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                    conButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                } else if (i == 1) {
                    paiButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                } else if (i == 2) {
                    lnkButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                } else if (i == 3) {
                    extButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                }
            }
        }

        hasAlreadyAddMenuButtons = true;

        if (hasAlreadyAddMenuButtons) {
            if (!App.alreadyStartedAGame || App.playerWon) {
                setComponentZOrder(conButton, 1);
                setComponentZOrder(newButton, 0);
            } else {
                setComponentZOrder(conButton, 0);
                setComponentZOrder(newButton, 1);
            }
        }

        g2d.setStroke(new BasicStroke());

    }

    private void drawConfirmationDiallogue1(Graphics2D g) {
        int menuW = 500;
        int menuH = 200;

        int menuX = ((App.windowWidth - menuW) / 2) - (App.offsetX / 2);
        int menuY = ((App.windowHeight - menuH) / 2) - (App.offsetY / 2);

        g.setColor(Color.decode("#e7da9f"));
        g.fillRect(menuX, menuY, menuW, menuH);

        g.setColor(Color.BLACK);
        g.setFont(audiowide20);

        String ask = "Ao trocar o modo de jogo, a partida";
        String ask2 = "atual será resetada! Prosseguir?";

        g.drawString(ask, menuX + 50, menuY + 40);
        g.drawString(ask2, menuX + 70, menuY + 65);

        int w = (menuW - 70) / 2;
        int h = 65;

        int acceptX = menuX + 30;
        int acceptY = (menuY + menuH - 30) - 65;

        if (acb) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        }

        acceptButton.setBounds(acceptX, acceptY, w, h);

        int declineX = (menuX + menuW - 30) - w;
        int declineY = (menuY + menuH - 30) - h;

        if (db) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Não", declineX, declineY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Não", declineX, declineY, w, h);
        }

        declineButton.setBounds(declineX, declineY, w, h);

        if (!hasAAPMCCButtons) {
            add(acceptButton);
            add(declineButton);

            hasAAPMCCButtons = true;
        }
    }

    private void drawConfirmationDiallogue2(Graphics2D g) {
        int menuW = 500;
        int menuH = 200;

        int menuX = ((App.windowWidth - menuW) / 2) - (App.offsetX / 2);
        int menuY = ((App.windowHeight - menuH) / 2) - (App.offsetY / 2);

        g.setColor(Color.decode("#e7da9f"));
        g.fillRect(menuX, menuY, menuW, menuH);

        g.setColor(Color.BLACK);
        g.setFont(audiowide20);

        String ask = "Você será redirecionado para um site";
        String ask2 = "externo! Deseja prosseguir?";

        g.drawString(ask, menuX + 50, menuY + 40);
        g.drawString(ask2, menuX + 90, menuY + 65);

        int w = (menuW - 70) / 2;
        int h = 65;

        int acceptX = menuX + 30;
        int acceptY = (menuY + menuH - 30) - 65;

        if (arb) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        }

        acceptRedButton.setBounds(acceptX, acceptY, w, h);

        int declineX = (menuX + menuW - 30) - w;
        int declineY = (menuY + menuH - 30) - h;

        if (drb) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Não", declineX, declineY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Não", declineX, declineY, w, h);
        }

        declineRedButton.setBounds(declineX, declineY, w, h);

        if (!hasAARCButtons) {
            add(acceptRedButton);
            add(declineRedButton);

            hasAARCButtons = true;
        }
    }

    private void drawPlayerWonDialogue(Graphics2D g) {
        int menuW = 500;
        int menuH = 200;

        int menuX = ((App.windowWidth - menuW) / 2) - (App.offsetX / 2);
        int menuY = ((App.windowHeight - menuH) / 2) - (App.offsetY / 2);

        g.setColor(Color.decode("#e7da9f"));
        g.fillRect(menuX, menuY, menuW, menuH);

        g.setColor(Color.BLACK);
        g.setFont(audiowide20);

        String ask = "";
        if (App.playingAgainstAI) {
            if (App.player1Won) {
                ask = "Você ganhou!";
            } else if (App.player2Won) {
                ask = "Você perdeu!";
            }
            g.drawString(ask, menuX + ((menuW - g.getFontMetrics().stringWidth(ask)) / 2), menuY + 40);
        } else {
            if (App.player1Won) {
                ask = "Jogador 1 ganhou!";
            } else if (App.player2Won) {
                ask = "Jogador 2 ganhou!";
            }
            g.drawString(ask, menuX + ((menuW - g.getFontMetrics().stringWidth(ask)) / 2), menuY + 40);
        }
        String ask2 = "Deseja iniciar novo jogo?";
        g.drawString(ask2, menuX + ((menuW - g.getFontMetrics().stringWidth(ask2)) / 2), menuY + 65);


        int w = (menuW - 80) / 3;
        int h = 65;

        int acceptX = menuX + 30;
        int acceptY = (menuY + menuH - 30) - h;

        if (pnb) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        }

        wonNewButton.setBounds(acceptX, acceptY, w, h);

        int menuButtonX = (menuX + (menuW/2)) - (w / 2);
        int menuButtonY = (menuY + menuH - 30) - h;

        if (pmb) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Menu", menuButtonX, menuButtonY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Menu", menuButtonX, menuButtonY, w, h);
        }

        wonMenButton.setBounds(menuButtonX, menuButtonY, w, h);

        int declineX = (menuX + menuW - 30) - w;
        int declineY = (menuY + menuH - 30) - h;

        if (peb) {
            g.setColor(Color.decode("#c08b60"));
            drawButton(g, "Sair", declineX, declineY, w, h);
        } else {
            g.setColor(Color.decode("#000000"));
            drawButton(g, "Sair", declineX, declineY, w, h);
        }

        wonExtButton.setBounds(declineX, declineY, w, h);

        if (!hasAAPWButtons) {
            add(wonNewButton);
            add(wonExtButton);
            add(wonMenButton);

            hasAAPWButtons = true;
        }
    }

    private void drawButton(Graphics2D g, String text, int x, int y, int w, int h) {
        g.setStroke(new BasicStroke(1));
        g.drawRect(x, y, w, h);

        int gbsW = g.getFontMetrics().stringWidth(text);

        g.drawString(text, x + (w / 2) - (gbsW / 2), y + (int) (h / 1.5));
    }

    private void drawHoverButton(Graphics2D g, String text, int x, int y, int w, int h) {
        g.setStroke(new BasicStroke(1));
        g.fillRect(x, y, w, h);

        g.setColor(Color.decode("#e7da9f"));
        int gbsW = g.getFontMetrics().stringWidth(text);

        g.drawString(text, x + (w / 2) - (gbsW / 2), y + (int) (h / 1.5));
    }

    private void drawPieceAt(Graphics2D g, int x, int y, PieceType piece) {
        switch (piece) {
            case PLAYER1:
                g.setColor(Color.decode("#412b17"));
                g.fillOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(Color.decode("#332411"));
                g.drawOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);
                break;
            case PLAYER2:
                g.setColor(Color.decode("#d85757"));
                g.fillOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(Color.decode("#ad3e3e"));
                g.drawOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);
                break;
            case PLAYER1_KING:
                g.setColor(Color.decode("#412b17"));
                g.fillOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(Color.decode("#dfb11c"));
                g.drawOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);
                break;
            case PLAYER2_KING:
                g.setColor(Color.decode("#d85757"));
                g.fillOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(Color.decode("#dfb11c"));
                g.drawOval(x * 100 + 10, y * 100 + 10, App.pieceSize, App.pieceSize);
                break;

            default:
                break;
        }

    }

    public void update() {
        if (App.isGameActive)
            startBoard = App.board;
        repaint();
    }

}
