package main.java.nathansdc.damas.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import main.java.nathansdc.damas.App;
import main.java.nathansdc.damas.jogo.Board;
import main.java.nathansdc.damas.jogo.BoardManager;
import main.java.nathansdc.damas.jogo.Movement;
import main.java.nathansdc.damas.jogo.Piece;
import main.java.nathansdc.damas.ui.ColorScheme.Theme;
import main.java.nathansdc.damas.util.FontLoader;

public class Scene extends JPanel {

    public enum ActiveDialogue {
        MENU,
        SETTINGS,
        PLAYMODE_CHANGE_CONFIRMATION,
        REDIRECT_CONFIRMATION,
        SOMEONE_WON,
        THEME_CHOOSER_DIALOGUE,
        AI_DIFFICULTY_CHOOSER_DIALOGUE
    }

    private static BoardManager startBoard = App.boardManager;

    private Font audiowide = FontLoader.getFont("Audiowide").deriveFont(20.0f);

    /**
     * <pre>
     * 0 - impossivel alcançar / aciona desmarcamento
     * 1 - casa livre para a qual pode se mover
     * 2 - peça inimiga marcada para possivel captura
     * 3 - peça atualmente selecionada
     * </pre>
     */
    public Integer[][] auxiliarBoard = new Integer[Board.SIZE][Board.SIZE];

    public void clearAuxiliarBoard() {
        for (int i = 0; i < auxiliarBoard.length; i++) {
            for (int j = 0; j < auxiliarBoard.length; j++) {
                auxiliarBoard[i][j] = 0;
            }
        }
    }

    // -----------------------------------------------------------------------------
    // Botões do menu principal

    private boolean hasAlreadyAddMenuButtons = false;

    private InvisibleButton menuButton_new = new InvisibleButton(() -> {
        App.startNewGame();
    });
    private InvisibleButton menuButton_continue = new InvisibleButton(() -> {
        App.isGameActive = true;
        App.alreadyStartedAGame = true;
        App.isMenuActive = false;
    });
    private InvisibleButton menuButton_settings = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.SETTINGS;
    });
    private InvisibleButton menuButton_exit = new InvisibleButton(() -> {
        System.exit(0);
    });
    private InvisibleButton paiButton = new InvisibleButton(() -> {
        if (App.alreadyStartedAGame && !App.playerWon) {
            App.activeDialogue = ActiveDialogue.PLAYMODE_CHANGE_CONFIRMATION;
        } else {
            App.settings.setPlaying_against_ai(!App.settings.isPlaying_against_ai());
        }
    });

    private InvisibleButton gitLinkButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.REDIRECT_CONFIRMATION;
    });

    // -----------------------------------------------------------------------------
    // Botões de diálogo da confirmação de mudança de modo de jogo

    private boolean hasAAPMCCButtons = false;

    private InvisibleButton acceptButton = new InvisibleButton(() -> {
        App.boardManager.reset();
        App.scene.clearAuxiliarBoard();
        App.isGameActive = true;
        App.alreadyStartedAGame = true;
        App.playerWon = false;
        App.player1Won = false;
        App.player2Won = false;
        App.isMenuActive = false;
        App.activeDialogue = ActiveDialogue.MENU;

        App.settings.setPlaying_against_ai(!App.settings.isPlaying_against_ai());
    });
    private InvisibleButton declineButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.MENU;
    });

    // -----------------------------------------------------------------------------
    // Botões de diálogo da confirmação de redirecionamento para site externo

    private boolean hasAARCButtons = false;

    private InvisibleButton acceptRedButton = new InvisibleButton(() -> {
        App.openBrowserLink();
        App.activeDialogue = ActiveDialogue.MENU;
    });
    private InvisibleButton declineRedButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.MENU;
    });

    // -----------------------------------------------------------------------------
    // Botões de diálogo do player won

    private boolean hasAAPWButtons = false;
    private InvisibleButton wonNewButton = new InvisibleButton(() -> {
        App.startNewGame();
    });
    private InvisibleButton wonExtButton = new InvisibleButton(() -> {
        System.exit(0);
    });
    private InvisibleButton wonMenButton = new InvisibleButton(() -> {
        App.isGameActive = false;
        App.isMenuActive = true;
        App.playerWon = true;
        App.player1Won = false;
        App.player2Won = false;
        App.activeDialogue = ActiveDialogue.MENU;
    });

    // -----------------------------------------------------------------------------
    // Botões do painel de configuraççoes gerais

    private boolean hasSettingsButtons = false;

    private InvisibleButton saveSettingsButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.MENU;
    });
    private InvisibleButton discardSettingsButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.MENU;
    });

    private InvisibleButton themeSettingsButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.THEME_CHOOSER_DIALOGUE;
    });
    private InvisibleButton captureDirectionBothSettingsButton = new InvisibleButton(() -> {
        App.settings.setBoth_direction_capture(!App.settings.isBoth_direction_capture());
    });
    private InvisibleButton forcedCaptureSettingsButton = new InvisibleButton(() -> {
        App.settings.setForced_capture(!App.settings.isForced_capture());
    });
    private InvisibleButton difficultySettingsButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.AI_DIFFICULTY_CHOOSER_DIALOGUE;
    });
    private InvisibleButton forcedPromotionSettingsButton = new InvisibleButton(() -> {
        App.settings.setForced_promotion(!App.settings.isForced_promotion());
    });
    private InvisibleButton windowSizeSettingsButton = new InvisibleButton(() -> {
        // TODO: window size chooser dialogue
    });
    private InvisibleButton menuStyleSettingsButton = new InvisibleButton(() -> {
        // TODO: menu style chooser dialogue
    });

    // -----------------------------------------------------------------------------
    // Botões do diálogo do Theme chooser

    private boolean hasAATCButtons = false;

    public ColorScheme.Theme backupTheme = App.colorScheme.getCurrentTheme();

    private InvisibleButton classicThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(ColorScheme.Theme.CLASSIC);
    });
    private InvisibleButton darkThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(ColorScheme.Theme.DARK);
    });
    private InvisibleButton lightThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(ColorScheme.Theme.LIGHT);
    });
    private InvisibleButton blueThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(ColorScheme.Theme.BLUE);
    });
    private InvisibleButton pinkThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(ColorScheme.Theme.PINK);
    });
    private InvisibleButton greenThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(ColorScheme.Theme.GREEN);
    });
    private InvisibleButton saveThemeButton = new InvisibleButton(() -> {
        App.activeDialogue = ActiveDialogue.SETTINGS;
        backupTheme = App.colorScheme.getCurrentTheme();
    });
    private InvisibleButton discardThemeButton = new InvisibleButton(() -> {
        App.settings.setApp_theme(backupTheme);
        App.activeDialogue = ActiveDialogue.SETTINGS;
    });

    public Scene() {
        super();
        clearAuxiliarBoard();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // Garante que as boradas dos circulos e linhas sejam suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Fonte padrão do programa
        g2d.setFont(audiowide);

        // Desenha o fundo
        for (int i = 0; i < BoardManager.size; i++) {
            for (int j = 0; j < BoardManager.size; j++) {
                if (auxiliarBoard[i][j] == 1) { // Pode se mover para >
                    g.setColor(App.colorScheme.getAuxiliarBoardColor1());
                } else if (auxiliarBoard[i][j] == 2) { // Peça inimiga marcada como possivel captura
                    g.setColor(App.colorScheme.getAuxiliarBoardColor2());
                } else if (auxiliarBoard[i][j] == 3) { // Peça atual selecionada
                    g.setColor(App.colorScheme.getAuxiliarBoardColor3());
                } else {
                    if ((i + j) % 2 == 0) {
                        g.setColor(App.colorScheme.getBoardColor1());
                    } else {
                        g.setColor(App.colorScheme.getBoardColor2());
                    }
                }
                g.fillRect(i * 100, j * 100, 100, 100);
            }
        }

        // Desenha as peças no tabuleiro
        for (int i = 0; i < BoardManager.size; i++) {
            for (int j = 0; j < BoardManager.size; j++) {
                drawPieceAt(g2d, i, j, startBoard.getBoard().getPiece(i, j));
            }
        }

        if (!App.isGameActive) {
            // Pinta uma cortina semi-transparente sobre o tabuleiro
            g2d.setColor(new Color(0, 0, 0, 153));
            g.fillRect(0, 0, App.getMetrics().getWindowWidth(), App.getMetrics().getWindowHeight());
            // Reseta as hitboxes de todos os botões
            resetBoundary();

            if (App.isMenuActive && App.activeDialogue == ActiveDialogue.MENU) {
                drawMenu(g2d);
            } else if (App.isMenuActive && App.activeDialogue == ActiveDialogue.PLAYMODE_CHANGE_CONFIRMATION) {
                drawConfirmationDiallogue1(g2d);
            } else if (App.isMenuActive && App.activeDialogue == ActiveDialogue.REDIRECT_CONFIRMATION) {
                drawConfirmationDiallogue2(g2d);
            } else if (App.isMenuActive && (App.activeDialogue == ActiveDialogue.SETTINGS ||
                    App.activeDialogue == ActiveDialogue.AI_DIFFICULTY_CHOOSER_DIALOGUE)) {
                drawSettingsMenu(g2d);
            } else if (App.isMenuActive && App.activeDialogue == ActiveDialogue.THEME_CHOOSER_DIALOGUE) {
                drawThemeChooserDialogue(g2d);
            } else if (!App.isMenuActive && App.activeDialogue == ActiveDialogue.SOMEONE_WON) {
                drawPlayerWonDialogue(g2d);
            }
        } else {
            // Reseta as hitboxes de todos os botões
            resetBoundary();
        }

        g.dispose();
    }

    private void resetBoundary() {
        if (hasAlreadyAddMenuButtons) {
            menuButton_new.setBounds(0, 0, 0, 0);
            menuButton_continue.setBounds(0, 0, 0, 0);
            paiButton.setBounds(0, 0, 0, 0);
            menuButton_settings.setBounds(0, 0, 0, 0);
            menuButton_exit.setBounds(0, 0, 0, 0);
            gitLinkButton.setBounds(0, 0, 0, 0);
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

        if (hasSettingsButtons) {
            saveSettingsButton.setBounds(0, 0, 0, 0);
            discardSettingsButton.setBounds(0, 0, 0, 0);
            captureDirectionBothSettingsButton.setBounds(0, 0, 0, 0);
            forcedCaptureSettingsButton.setBounds(0, 0, 0, 0);
            forcedPromotionSettingsButton.setBounds(0, 0, 0, 0);
            themeSettingsButton.setBounds(0, 0, 0, 0);
            menuStyleSettingsButton.setBounds(0, 0, 0, 0);
            difficultySettingsButton.setBounds(0, 0, 0, 0);
            windowSizeSettingsButton.setBounds(0, 0, 0, 0);
        }

        if (hasAATCButtons) {
            classicThemeButton.setBounds(0, 0, 0, 0);
            darkThemeButton.setBounds(0, 0, 0, 0);
            lightThemeButton.setBounds(0, 0, 0, 0);
            blueThemeButton.setBounds(0, 0, 0, 0);
            pinkThemeButton.setBounds(0, 0, 0, 0);
            greenThemeButton.setBounds(0, 0, 0, 0);
            saveThemeButton.setBounds(0, 0, 0, 0);
            discardThemeButton.setBounds(0, 0, 0, 0);
        }
    }

    private void drawMenu(Graphics2D g2d) {
        // Desenha a caixa de opções/menu
        // 800 - 450 = 350 / 2 = 175
        // 800 - 600 = 200 / 2 = 100
        int menuW = 450;
        int menuH = 540;

        int menuX = ((App.getMetrics().getWindowWidth() - menuW) / 2) - (App.getMetrics().getOffsetX() / 2);
        int menuY = ((App.getMetrics().getWindowHeight() - menuH) / 2) - (App.getMetrics().getOffsetY() / 2);

        g2d.setColor(App.colorScheme.getMenuColor());
        g2d.fillRect(menuX, menuY, menuW, menuH);

        // Reajuste de tamnho da fonte para o titulo

        g2d.setColor(App.colorScheme.getTextColor());
        g2d.setFont(audiowide.deriveFont(45f));

        if (!App.alreadyStartedAGame || App.playerWon) {
            int titleW = g2d.getFontMetrics().stringWidth(App.settings.getApp_name());
            int titleC = ((App.getMetrics().getWindowWidth() - titleW) / 2) - (App.getMetrics().getOffsetX() / 2);
            g2d.drawString(App.settings.getApp_name(), titleC, menuY + g2d.getFontMetrics().getHeight() + 45);
        } else {
            int titleW = g2d.getFontMetrics().stringWidth("Pausado");
            int titleC = ((App.getMetrics().getWindowWidth() - titleW) / 2) - (App.getMetrics().getOffsetX() / 2);
            g2d.drawString("Pausado", titleC, menuY + g2d.getFontMetrics().getHeight() + 45);
        }

        int ghostButtonX = menuX + 75;
        int pad = 10;
        int ghostButtonWidth = menuW - 150;
        int ghostButtonHeight = 65;

        String[] ghostButtonStrings = new String[] { "Novo Jogo", "Contra ",
                "Configurações", "Sair" };

        g2d.setFont(audiowide.deriveFont(30f));
        for (int i = 0; i < ghostButtonStrings.length; i++) {

            if (i == 0) {
                if (!App.alreadyStartedAGame || App.playerWon) {
                    ghostButtonStrings[i] = "Novo Jogo";
                    if (hasAlreadyAddMenuButtons) {
                        setComponentZOrder(menuButton_continue, 1);
                        setComponentZOrder(menuButton_new, 0);
                    }
                } else {
                    ghostButtonStrings[i] = "Continue";
                    if (hasAlreadyAddMenuButtons) {
                        setComponentZOrder(menuButton_continue, 0);
                        setComponentZOrder(menuButton_new, 1);
                    }
                }
            }

            if (i == 1) {
                if (App.settings.isPlaying_against_ai()) {
                    ghostButtonStrings[i] = "Contra IA";
                } else {
                    ghostButtonStrings[i] = "Contra Player";
                }
            }

            int baseHeight = ((menuY + 175) + (i * (ghostButtonHeight + pad)));

            if (((menuButton_continue.onHover || menuButton_new.onHover) && i == 0) || (paiButton.onHover && i == 1)
                    || (menuButton_settings.onHover && i == 2) || (menuButton_exit.onHover && i == 3)) {
                g2d.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                drawHoverButton(g2d, ghostButtonStrings[i], ghostButtonX, baseHeight, ghostButtonWidth,
                        ghostButtonHeight);
            } else {
                g2d.setColor(App.colorScheme.getMenuButtonsColor());
                drawButton(g2d, ghostButtonStrings[i], ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
            }

            if (!hasAlreadyAddMenuButtons) {
                switch (i) {
                    case 0:
                        menuButton_continue.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(menuButton_continue);
                        menuButton_new.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(menuButton_new);
                        break;
                    case 1:
                        paiButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(paiButton);
                        break;
                    case 2:
                        menuButton_exit.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(menuButton_exit);
                        break;
                    case 3:
                        menuButton_settings.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                        add(menuButton_settings);
                        break;
                    default:
                        break;
                }
            } else {
                if (i == 0) {
                    menuButton_new.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                    menuButton_continue.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                } else if (i == 1) {
                    paiButton.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                } else if (i == 2) {
                    menuButton_settings.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                } else if (i == 3) {
                    menuButton_exit.setBounds(ghostButtonX, baseHeight, ghostButtonWidth, ghostButtonHeight);
                }
            }
        }

        g2d.setFont(audiowide.deriveFont(15.0f));
        String text = "Made by: NathanSDC";

        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int padding = 10;

        int gitLinkW = textWidth + (padding * 2);
        int gitLinkH = g2d.getFontMetrics().getHeight() + (padding);
        int gitLinkX = (App.getMetrics().getWindowWidth() - gitLinkW) - App.getMetrics().getOffsetX();
        int gitLinkY = (App.getMetrics().getWindowHeight() - gitLinkH) - App.getMetrics().getOffsetY();

        g2d.setColor(App.colorScheme.getMenuColor());
        g2d.fillRect(gitLinkX, gitLinkY, gitLinkW, gitLinkH);
        gitLinkButton.setBounds(gitLinkX, gitLinkY, gitLinkW, gitLinkH);

        g2d.setColor(App.colorScheme.getTextColor());
        g2d.drawString(text, gitLinkX + padding, gitLinkY + g2d.getFontMetrics().getHeight());

        if (!hasAlreadyAddMenuButtons) {
            add(gitLinkButton);
        }

        hasAlreadyAddMenuButtons = true;

        if (hasAlreadyAddMenuButtons) {
            if (!App.alreadyStartedAGame || App.playerWon) {
                setComponentZOrder(menuButton_continue, 1);
                setComponentZOrder(menuButton_new, 0);
            } else {
                setComponentZOrder(menuButton_continue, 0);
                setComponentZOrder(menuButton_new, 1);
            }
        }

        g2d.setStroke(new BasicStroke());

    }

    private void drawConfirmationDiallogue1(Graphics2D g) {
        int menuW = 500;
        int menuH = 200;

        int menuX = ((App.getMetrics().getWindowWidth() - menuW) / 2) - (App.getMetrics().getOffsetX() / 2);
        int menuY = ((App.getMetrics().getWindowHeight() - menuH) / 2) - (App.getMetrics().getOffsetY() / 2);

        g.setColor(App.colorScheme.getMenuColor());
        g.fillRect(menuX, menuY, menuW, menuH);

        g.setColor(App.colorScheme.getTextColor());
        g.setFont(audiowide.deriveFont(20f));

        String ask = "Ao trocar o modo de jogo, a partida";
        String ask2 = "atual será resetada! Prosseguir?";

        g.drawString(ask, menuX + 50, menuY + 40);
        g.drawString(ask2, menuX + 70, menuY + 65);

        int w = (menuW - 70) / 2;
        int h = 65;

        int acceptX = menuX + 30;
        int acceptY = (menuY + menuH - 30) - 65;

        if (acceptButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Sim", acceptX, acceptY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        }

        acceptButton.setBounds(acceptX, acceptY, w, h);

        int declineX = (menuX + menuW - 30) - w;
        int declineY = (menuY + menuH - 30) - h;

        if (declineButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Não", declineX, declineY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
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

        int menuX = ((App.getMetrics().getWindowWidth() - menuW) / 2) - (App.getMetrics().getOffsetX() / 2);
        int menuY = ((App.getMetrics().getWindowHeight() - menuH) / 2) - (App.getMetrics().getOffsetY() / 2);

        g.setColor(App.colorScheme.getMenuColor());
        g.fillRect(menuX, menuY, menuW, menuH);

        g.setColor(App.colorScheme.getTextColor());
        g.setFont(audiowide.deriveFont(20f));

        String ask = "Você será redirecionado para um site";
        String ask2 = "externo! Deseja prosseguir?";

        g.drawString(ask, menuX + 50, menuY + 40);
        g.drawString(ask2, menuX + 90, menuY + 65);

        int w = (menuW - 70) / 2;
        int h = 65;

        int acceptX = menuX + 30;
        int acceptY = (menuY + menuH - 30) - 65;

        if (acceptRedButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Sim", acceptX, acceptY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        }

        acceptRedButton.setBounds(acceptX, acceptY, w, h);

        int declineX = (menuX + menuW - 30) - w;
        int declineY = (menuY + menuH - 30) - h;

        if (declineRedButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Não", declineX, declineY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
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

        int menuX = ((App.getMetrics().getWindowWidth() - menuW) / 2) - (App.getMetrics().getOffsetX() / 2);
        int menuY = ((App.getMetrics().getWindowHeight() - menuH) / 2) - (App.getMetrics().getOffsetY() / 2);

        g.setColor(App.colorScheme.getMenuColor());
        g.fillRect(menuX, menuY, menuW, menuH);

        g.setColor(App.colorScheme.getTextColor());
        g.setFont(audiowide.deriveFont(20f));

        String ask = "";
        if (App.settings.isPlaying_against_ai()) {
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

        if (wonNewButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Sim", acceptX, acceptY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Sim", acceptX, acceptY, w, h);
        }

        wonNewButton.setBounds(acceptX, acceptY, w, h);

        int menuButtonX = (menuX + (menuW / 2)) - (w / 2);
        int menuButtonY = (menuY + menuH - 30) - h;

        if (wonMenButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Menu", menuButtonX, menuButtonY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Menu", menuButtonX, menuButtonY, w, h);
        }

        wonMenButton.setBounds(menuButtonX, menuButtonY, w, h);

        int declineX = (menuX + menuW - 30) - w;
        int declineY = (menuY + menuH - 30) - h;

        if (wonExtButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Sair", declineX, declineY, w, h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
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

    private void drawSettingsMenu(Graphics2D g) {
        g.setColor(App.colorScheme.getMenuColor());
        g.fillRect(0, 0, App.getMetrics().getWindowWidth(), App.getMetrics().getWindowHeight());

        g.setColor(App.colorScheme.getTextColor());
        g.setFont(audiowide.deriveFont(35f));

        String title = "Configurações";
        int titleX = (App.getMetrics().getWindowWidth() / 2) - (g.getFontMetrics().stringWidth(title) / 2);
        g.drawString(title, titleX, 60);

        /*
         * Jogo:
         * Captura bilateral: pecas comuns podem capturar tanto para frente quanto para
         * tras
         * Captura obrigatória: Se houverem movimentos de captura disponíveis somente
         * eles são permitidos
         * Nivel de dificuldade do modo ia
         * Obrigar promoção á dama (se o modo de captura obrigatória estiver ativo,
         * peças que podem ser promovidas por meio de captura tem prioridade)
         * Aplicativo:
         * Tamanho da janela
         * Tema do aplicativo
         * Estilo do menu
         */

        int lastHeight = 0;

        // Aplicativo
        g.setColor(App.colorScheme.getTextColor());
        g.setFont(audiowide.deriveFont(20f));

        g.drawString("Aplicativo", App.getMetrics().getSettingsOptListMargin(),
                (App.getMetrics().getSettingsOptListMargin() + 75));

        String[] optionList_A = new String[] { "Temas:", "Tamanho da janela:", "Estilo do menu:" };
        for (int i = 0; i < optionList_A.length; i++) {
            int baseHeight = ((App.getMetrics().getSettingsOptListMargin() + 75)
                    + (i * (App.getMetrics().getButtonHeight() + App.getMetrics().getButtonSpacing())));

            g.setFont(audiowide.deriveFont(20f));
            g.setColor(App.colorScheme.getTextColor());
            g.drawString(optionList_A[i], App.getMetrics().getSettingsOptListMargin() + 30,
                    baseHeight + ((App.getMetrics().getButtonHeight() / 3) * 2));
            lastHeight = baseHeight;

            String text = "Escolher >";
            int tw = g.getFontMetrics().stringWidth(text) + 50;

            g.setFont(audiowide.deriveFont(25f));
            if (i == 0) {
                if (themeSettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                }
                themeSettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, tw, App.getMetrics().getButtonHeight());
            } else if (i == 1) {
                if (windowSizeSettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                }
                windowSizeSettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, tw, App.getMetrics().getButtonHeight());
            } else if (i == 2) {
                if (menuStyleSettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                }
                menuStyleSettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, tw, App.getMetrics().getButtonHeight());
            }
        }

        lastHeight += ((App.getMetrics().getButtonHeight() / 3) * 2) - 10;
        g.setFont(audiowide.deriveFont(20f));
        g.setColor(App.colorScheme.getTextColor());
        g.drawString("Jogabilidade:", App.getMetrics().getSettingsOptListMargin(), (lastHeight + 75));
        String[] optionList_B = new String[] { "Dificuldade:", "Captura obrigatória:", "Captura bilateral:",
                "Obrigar promoção:" };
        for (int i = 0; i < optionList_B.length; i++) {
            int baseHeight = ((lastHeight + 75)
                    + (i * (App.getMetrics().getButtonHeight() + App.getMetrics().getButtonSpacing())));

            g.setFont(audiowide.deriveFont(20f));
            g.setColor(App.colorScheme.getTextColor());
            g.drawString(optionList_B[i], App.getMetrics().getSettingsOptListMargin() + 30,
                    baseHeight + ((App.getMetrics().getButtonHeight() / 3) * 2));

            String text = "Escolher >";
            int tw = g.getFontMetrics().stringWidth(text) + 50;

            g.setFont(audiowide.deriveFont(25f));
            if (i == 0) {
                if (difficultySettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, text,
                            App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, tw, App.getMetrics().getButtonHeight());
                }
                difficultySettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - tw - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, tw, App.getMetrics().getButtonHeight());
            } else if (i == 1) {
                g.setFont(new Font("Monospace", Font.PLAIN, 20));
                if (App.settings.isForced_capture()) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_selectedCheckbox());
                    drawHoverButton(g, "\u2713",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                } else if (forcedCaptureSettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, "\u2715",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, "\u2715",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                }
                forcedCaptureSettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
            } else if (i == 2) {
                g.setFont(new Font("Monospace", Font.PLAIN, 20));
                if (App.settings.isBoth_direction_capture()) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_selectedCheckbox());
                    drawHoverButton(g, "\u2713",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                } else if (captureDirectionBothSettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, "\u2715",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, "\u2715",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                }
                captureDirectionBothSettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
            } else if (i == 3) {
                g.setFont(new Font("Monospace", Font.PLAIN, 20));
                if (App.settings.isForced_promotion()) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_selectedCheckbox());
                    drawHoverButton(g, "\u2713",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                } else if (forcedPromotionSettingsButton.onHover) {
                    g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                    drawHoverButton(g, "\u2715",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                } else {
                    g.setColor(App.colorScheme.getMenuButtonsColor());
                    drawButton(g, "\u2715",
                            App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                    - App.getMetrics().getSettingsOptListMargin(),
                            baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
                }
                forcedPromotionSettingsButton.setBounds(
                        App.getMetrics().getWindowWidth() - App.getMetrics().getButtonHeight()
                                - App.getMetrics().getSettingsOptListMargin(),
                        baseHeight, App.getMetrics().getButtonHeight(), App.getMetrics().getButtonHeight());
            }
        }

        int cb_w = App.getMetrics().getSettingsButtonWidth_confirmation();
        int cb_h = App.getMetrics().getButtonHeight();
        int cb_y = App.getMetrics().getSettingsButtonY_confirmation();

        g.setFont(audiowide.deriveFont(25f));
        if (saveSettingsButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Salvar", App.getMetrics().getSettingsButtonX_confirmationLeft(), cb_y, cb_w, cb_h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Salvar", App.getMetrics().getSettingsButtonX_confirmationLeft(), cb_y, cb_w, cb_h);
        }
        saveSettingsButton.setBounds(App.getMetrics().getSettingsButtonX_confirmationLeft(), cb_y, cb_w, cb_h);

        if (discardSettingsButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Cancelar", App.getMetrics().getSettingsButtonX_confirmationRight(), cb_y, cb_w, cb_h);
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Cancelar", App.getMetrics().getSettingsButtonX_confirmationRight(), cb_y, cb_w, cb_h);
        }
        discardSettingsButton.setBounds(App.getMetrics().getSettingsButtonX_confirmationRight(), cb_y, cb_w, cb_h);

        if (!hasSettingsButtons) {
            add(saveSettingsButton);
            add(discardSettingsButton);
            add(themeSettingsButton);
            add(windowSizeSettingsButton);
            add(menuStyleSettingsButton);
            add(difficultySettingsButton);
            add(captureDirectionBothSettingsButton);
            add(forcedCaptureSettingsButton);
            add(forcedPromotionSettingsButton);

            hasSettingsButtons = true;
        }
    }

    private Board sample = new Board();
    {
        sample.populate();
        sample.movePiece(new Movement(5, 2, 4, 3, sample), false);
        sample.movePiece(new Movement(4, 3, 5, 4, sample), false);
        sample.movePiece(new Movement(6, 5, 7, 4, sample), false);
        sample.movePiece(new Movement(7, 6, 6, 5, sample), false);
    }

    private Integer[][] sampleAuxiliar = App.boardManager.showavailableMovements(sample, 6, 5);

    private void drawThemeChooserDialogue(Graphics2D g) {
        g.setColor(App.colorScheme.getMenuColor());
        g.fillRect(0, 0, App.getMetrics().getWindowWidth(), App.getMetrics().getWindowHeight());

        // ------------------------------------------------------------------
        // Demonstração do tema escolhido
        // ------------------------------------------------------------------
        for (int i = 0; i < sampleAuxiliar.length; i++) {
            for (int j = 0; j < sampleAuxiliar.length; j++) {
                if (sampleAuxiliar[i][j] == 1) { // Pode se mover para >
                    g.setColor(App.colorScheme.getAuxiliarBoardColor1());
                } else if (sampleAuxiliar[i][j] == 2) { // Peça inimiga marcada como possivel captura
                    g.setColor(App.colorScheme.getAuxiliarBoardColor2());
                } else if (sampleAuxiliar[i][j] == 3) { // Peça atual selecionada
                    g.setColor(App.colorScheme.getAuxiliarBoardColor3());
                } else {
                    if ((i + j) % 2 == 0) {
                        g.setColor(App.colorScheme.getBoardColor1());
                    } else {
                        g.setColor(App.colorScheme.getBoardColor2());
                    }
                }
                g.fillRect(i * 100, j * 100, 100, 100);
            }
        }

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                drawPieceAt(g, i, j, sample.getPiece(i, j));
            }
        }

        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, App.getMetrics().getWindowWidth(), App.getMetrics().getWindowHeight());

        int margin = 35;
        int w = (App.getMetrics().getWindowWidth() / 2) - (margin);
        int h = (App.getMetrics().getWindowHeight()) - (margin * 3);

        drawShadow(g, margin, margin, w, h, 1, 10, App.colorScheme.getMenuColor());
        g.setColor(App.colorScheme.getMenuColor());
        g.fillRect(margin, margin, w, h);

        // ------------------------------------------------------------------
        // Botões de opção
        // ------------------------------------------------------------------

        g.setFont(audiowide.deriveFont(35f));
        int titleX = (margin + (w / 2)) - (g.getFontMetrics().stringWidth("Temas") / 2);
        g.setColor(App.colorScheme.getTextColor());
        g.drawString("Temas", titleX, margin + g.getFontMetrics().getHeight() + 20);

        g.setFont(audiowide.deriveFont(25f));
        String[] themeNames = Theme.getStringValues();
        int themeNameX = margin + 20;

        int buttonW = App.getMetrics().getButtonHeight();
        int buttonX = (margin + w) - buttonW - 20;
        for (int i = 0; i < themeNames.length; i++) {
            int baseHeight = ((margin + 150)
                    + (i * (App.getMetrics().getButtonHeight() + App.getMetrics().getButtonSpacing())));
            themeNames[i] += ":";

            g.setFont(audiowide.deriveFont(25f));
            g.setColor(App.colorScheme.getTextColor());
            g.drawString(themeNames[i], themeNameX, baseHeight + ((buttonW / 3) * 2));

            g.setFont(new Font("Monospace", Font.PLAIN, 20));
            if ((i == 0 && App.colorScheme.getCurrentTheme() == ColorScheme.Theme.CLASSIC) ||
                    (i == 1 && App.colorScheme.getCurrentTheme() == ColorScheme.Theme.DARK) ||
                    (i == 2 && App.colorScheme.getCurrentTheme() == ColorScheme.Theme.LIGHT) ||
                    (i == 3 && App.colorScheme.getCurrentTheme() == ColorScheme.Theme.BLUE) ||
                    (i == 4 && App.colorScheme.getCurrentTheme() == ColorScheme.Theme.PINK) ||
                    (i == 5 && App.colorScheme.getCurrentTheme() == ColorScheme.Theme.GREEN)) {
                g.setColor(App.colorScheme.getMenuButtonsColor_selectedCheckbox());
                drawHoverButton(g, "\u2713", buttonX, baseHeight, buttonW, buttonW);
            } else if ((classicThemeButton.onHover && i == 0) ||
                    (darkThemeButton.onHover && i == 1) ||
                    (lightThemeButton.onHover && i == 2) ||
                    (blueThemeButton.onHover && i == 3) ||
                    (pinkThemeButton.onHover && i == 4) ||
                    (greenThemeButton.onHover && i == 5)) {
                g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
                drawHoverButton(g, "\u2715", buttonX, baseHeight, buttonW, buttonW);
            } else {
                g.setColor(App.colorScheme.getMenuButtonsColor());
                drawButton(g, "\u2715", buttonX, baseHeight, buttonW, buttonW);
            }

            if (i == 0) {
                classicThemeButton.setBounds(buttonX, baseHeight, buttonW, buttonW);
                if (!hasAATCButtons)
                    add(classicThemeButton);
            } else if (i == 1) {
                darkThemeButton.setBounds(buttonX, baseHeight, buttonW, buttonW);
                if (!hasAATCButtons)
                    add(darkThemeButton);
            } else if (i == 2) {
                lightThemeButton.setBounds(buttonX, baseHeight, buttonW, buttonW);
                if (!hasAATCButtons)
                    add(lightThemeButton);
            } else if (i == 3) {
                blueThemeButton.setBounds(buttonX, baseHeight, buttonW, buttonW);
                if (!hasAATCButtons)
                    add(blueThemeButton);
            } else if (i == 4) {
                pinkThemeButton.setBounds(buttonX, baseHeight, buttonW, buttonW);
                if (!hasAATCButtons)
                    add(pinkThemeButton);
            } else if (i == 5) {
                greenThemeButton.setBounds(buttonX, baseHeight, buttonW, buttonW);
                if (!hasAATCButtons)
                    add(greenThemeButton);
            }
        }

        int internalMargin = 20;
        int buttonWidth = ((w / 2) - (internalMargin)) - App.getMetrics().getButtonSpacing();

        int button1X = ((margin + (w / 2)) - buttonWidth) - (App.getMetrics().getButtonSpacing() / 2);
        int button2X = (margin + (w / 2)) + (App.getMetrics().getButtonSpacing() / 2);

        if (saveThemeButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Salvar", button1X, h - (internalMargin * 3), buttonWidth,
                    App.getMetrics().getButtonHeight());
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Salvar", button1X + 2, h - (internalMargin * 3), buttonWidth,
                    App.getMetrics().getButtonHeight());
        }
        saveThemeButton.setBounds(button1X, h - (internalMargin * 3), buttonWidth, App.getMetrics().getButtonHeight());

        if (discardThemeButton.onHover) {
            g.setColor(App.colorScheme.getMenuButtonsColor_onHover());
            drawHoverButton(g, "Descartar", button2X, h - (internalMargin * 3), buttonWidth,
                    App.getMetrics().getButtonHeight());
        } else {
            g.setColor(App.colorScheme.getMenuButtonsColor());
            drawButton(g, "Descartar", button2X, h - (internalMargin * 3), buttonWidth,
                    App.getMetrics().getButtonHeight());
        }
        discardThemeButton.setBounds(button2X, h - (internalMargin * 3), buttonWidth,
                App.getMetrics().getButtonHeight());

        if (!hasAATCButtons) {
            add(saveThemeButton);
            add(discardThemeButton);
        }

        hasAATCButtons = true;
    }

    private void drawShadow(Graphics2D g, int x, int y, int w, int h, int strokeWidth, int spessure, Color color) {
        if (strokeWidth > spessure && spessure >= 1) {
            strokeWidth = 1;
        }

        Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 20);
        g.setColor(c);
        g.drawRoundRect(x + 1, y + 1, w - 2, h - 3, 10, 10);

        for (int i = 0; i < spessure; i += strokeWidth) {
            c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 20 - ((10 / spessure) * i));
            g.setColor(c);
            g.drawRoundRect(x - i, y - i, w + (i * 2), h + (i * 2), 10, 10);
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

        g.setColor(App.colorScheme.getMenuColor());
        int gbsW = g.getFontMetrics().stringWidth(text);

        g.drawString(text, x + (w / 2) - (gbsW / 2), y + (int) (h / 1.5));
    }

    private void drawPieceAt(Graphics2D g, int x, int y, Piece piece) {
        switch (piece) {
            case PLAYER1:
                g.setColor(App.colorScheme.getPlayer1_Color1());
                g.fillOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(App.colorScheme.getPlayer1_Color2());
                g.drawOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());
                break;
            case PLAYER2:
                g.setColor(App.colorScheme.getPlayer2_Color1());
                g.fillOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(App.colorScheme.getPlayer2_Color2());
                g.drawOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());
                break;
            case PLAYER1_KING:
                g.setColor(App.colorScheme.getPlayer1_Color1());
                g.fillOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(App.colorScheme.getKingsColor());
                g.drawOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());
                break;
            case PLAYER2_KING:
                g.setColor(App.colorScheme.getPlayer2_Color1());
                g.fillOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());

                g.setStroke(new BasicStroke(10.0f));
                g.setColor(App.colorScheme.getKingsColor());
                g.drawOval(x * 100 + 10, y * 100 + 10, App.getMetrics().getPieceSize(),
                        App.getMetrics().getPieceSize());
                break;

            default:
                break;
        }

    }

    public void update() {
        if (App.isGameActive)
            startBoard = App.boardManager;
        repaint();
    }

}
