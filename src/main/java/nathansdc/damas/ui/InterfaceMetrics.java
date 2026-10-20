package main.java.nathansdc.damas.ui;

import main.java.nathansdc.damas.jogo.Board;

public class InterfaceMetrics {

    // IN GAME MENU ---------------------------------------------------------------------|
    private int inGameMenuWidth = 75;
    private int inGameMenuHeight = getWindowHeight();
    // Esquerda
    private int inGameMenuXL = 0;
    // Direita
    private int inGameMenuXR = getWindowWidth() - inGameMenuWidth;

    // TABULEIRO (1) --------------------------------------------------------------------|
    private int squareSize = 100; // Tamanho das casas do tabuleiro

    // PieceSize é uma constante relativa ao valor de SquareSize e possui sempre 80%
    // do valor total do mesmo
    private int pieceSize = (int) (squareSize * 0.8); // Tamanho das peças

    private int boardWidth = (Board.SIZE * squareSize);
    private int boardHeight = (Board.SIZE * squareSize);

    private int boardX = inGameMenuWidth;

    // JANELA ---------------------------------------------------------------------------|
    
    private int offsetX = 15;
    // private int offsetXL = 15;
    private int offsetY = 39;

    private int windowWidth = boardWidth + offsetX + (inGameMenuWidth*2); // TAMANHO (LARGURA)
    private int windowHeight = boardHeight + offsetY; // TAMANHO (ALTURA)

    // MENU (1) -------------------------------------------------------------------------|
    
    private int menuWidth = 450; // TAMANHO (LARGURA)
    private int menuHeight = 540; // TAMANHO (ALTURA)

    private int menuX = ((windowWidth - menuWidth) / 2) - (offsetX / 2);
    private int menuY = ((windowHeight - menuHeight) / 2) - (offsetY / 2);

    private int buttonSpacing = 10; // ESPAÇAMENTO ENTRE BOTÕES
    private int buttonHeight = 65; // TAMANHO (ALTURA)

    private int menuTitleFontSize = 40;
    private int menuTitleY = menuY + 100;

    
    // Estes botões se ordenam em formato de lista, portanto seu espaçamento é na vertical
    private int menuButtonMargin = 75;
    private int menuButtonWidth = menuWidth - (menuButtonMargin * 2); // TAMANHO (LARGURA)
    
    // MENUS DE CONFIRMAÇÃO (2) ---------------------------------------------------------|
    private int confirmationMenuWidth = 500;
    private int confirmationMenuHeight = 200;
    
    private int confirmationMenuX = ((windowWidth - confirmationMenuWidth) / 2) - (offsetX / 2);
    private int confirmationMenuY = ((windowHeight - confirmationMenuHeight) / 2) - (offsetY / 2);

    private int confirmationText1 = confirmationMenuY + 40;
    private int confirmationText2 = confirmationMenuY + 65;
    
    // DIÁLOGO DE MUDANÇA DE MODO DE JOGO -----------------------------------------------
    // DIÁLOGO DE REDIRECIONAMENTO PARA SITE EXTERNO ------------------------------------
    // DIÁLOGO DE CONFIRMAÇÃO DE SAIDA --------------------------------------------------
    private int confirmationButtonMargin = 30; // Margem esquerda e direita
    // Tamanho do menu - (margem esuerda e direita) - (espaçamento entre botões) / quantidade de botões
    private int confirmationButtonWidth = (confirmationMenuWidth - (confirmationButtonMargin * 2) - buttonSpacing) / 2;
    
    // ALGUM JOGADOR GANHOU (2) ---------------------------------------------------------|
    // Tamanho do menu - (margem esuerda e direita) - (espaçamento entre botões) / quantidade de botões
    private int playerWonButtonWidth = (confirmationMenuWidth - (confirmationButtonMargin * 2) - (buttonSpacing * 2)) / 3;
    // MENU DE CONFIGURAÇÕES (3) ---------------------------------------------------------------|
    private int settingsButtonSpacing = 15;

    private int settingsOptListMargin = 40;
    private int settingsButtonWidth_optList = ((windowWidth/2) - (settingsOptListMargin*2)) + settingsButtonSpacing;
    private int settingsButtonX_optListLeft = settingsOptListMargin;
    private int settingsButtonX_optListRight = settingsOptListMargin + settingsButtonWidth_optList + settingsButtonSpacing;

    private int settingsConfirmationMargin = 75;
    private int settingsButtonWidth_confirmation = ((windowWidth/2) - (settingsConfirmationMargin*2)) - settingsButtonSpacing;
    private int settingsButtonY_confirmation = windowHeight - (buttonHeight + (settingsConfirmationMargin));
    private int settingsButtonX_confirmationLeft = ((windowWidth/2) - settingsButtonWidth_confirmation) - (settingsButtonSpacing/2);
    private int settingsButtonX_confirmationRight = ((windowWidth/2)) + (settingsButtonSpacing/2);

    private InterfaceMetrics() {
    }

    private static InterfaceMetrics instance;
    public static InterfaceMetrics getInstance() {
        if (instance == null)
            instance = new InterfaceMetrics();
        return instance;
    }

    /**
     * Recalcula todos os valores não constantes e seus dependentes
    */
    public boolean update() {
        pieceSize = (int) (squareSize * 0.8);
        // windowWidth = (boardWidth) + offsetX + 200;
        // windowHeight = (boardHeight) + offsetY;

        menuX = ((windowWidth - menuWidth) / 2) - (offsetX / 2);
        menuY = ((windowHeight - menuHeight) / 2) - (offsetY / 2);
        menuTitleY = menuY + 100;

        confirmationMenuX = ((windowWidth - confirmationMenuWidth) / 2) - (offsetX / 2);
        confirmationMenuY = ((windowHeight - confirmationMenuHeight) / 2) - (offsetY / 2);
        confirmationText1 = confirmationMenuY + 40;
        confirmationText2 = confirmationMenuY + 65;

        menuButtonWidth = menuWidth - (menuButtonMargin * 2);
        confirmationButtonWidth = (confirmationMenuWidth - (confirmationButtonMargin * 2) - buttonSpacing) / 2;
        playerWonButtonWidth = (confirmationMenuWidth - (confirmationButtonMargin * 2) - (buttonSpacing * 2)) / 3;

        return true;
    }

    /**
     * padrão: 100
    */
    public int getSquareSize() {
        return squareSize;
    }

    /**
     * PieceSize é uma constante relativa ao valor de SquareSize e possui sempre 80%
     * do valor total do mesmo
    */
    public int getPieceSize() {
        return pieceSize;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Possui sempre: <br> {@code (}{@link main.java.nathansdc.damas.jogo.Board Board.SIZE}{@code *  (squareSize)) + offsetX)} como tamanho mínimo
    */
    public int getWindowWidth() {
        return windowWidth;
    }

    /**
     * Possui sempre: <br> {@code (}{@link main.java.nathansdc.damas.jogo.Board Board.SIZE}{@code *  (squareSize)) + offsetY)} como tamanho mínimo
    */
    public int getWindowHeight() {
        return windowHeight;
    }

    public int getMenuWidth() {
        return menuWidth;
    }

    public int getMenuHeight() {
        return menuHeight;
    }

    public int getButtonSpacing() {
        return buttonSpacing;
    }

    public int getButtonHeight() {
        return buttonHeight;
    }

    public int getMenuTitleFontSize() {
        return menuTitleFontSize;
    }

    public int getMenuTitleY() {
        return menuTitleY;
    }

    public int getMenuButtonMargin() {
        return menuButtonMargin;
    }

    public int getMenuButtonWidth() {
        return menuButtonWidth;
    }

    public int getConfirmationMenuWidth() {
        return confirmationMenuWidth;
    }

    public int getConfirmationMenuHeight() {
        return confirmationMenuHeight;
    }

    public int getConfirmationMenuX() {
        return confirmationMenuX;
    }

    public int getConfirmationMenuY() {
        return confirmationMenuY;
    }

    public int getConfirmationText1() {
        return confirmationText1;
    }

    public int getConfirmationText2() {
        return confirmationText2;
    }

    public int getConfirmationButtonMargin() {
        return confirmationButtonMargin;
    }

    public int getConfirmationButtonWidth() {
        return confirmationButtonWidth;
    }

    public int getPlayerWonButtonWidth() {
        return playerWonButtonWidth;
    }

    public int getMenuX() {
        return menuX;
    }

    public int getMenuY() {
        return menuY;
    }

    public int getSettingsButtonSpacing() {
        return settingsButtonSpacing;
    }

    public int getSettingsOptListMargin() {
        return settingsOptListMargin;
    }

    public int getSettingsButtonWidth_optList() {
        return settingsButtonWidth_optList;
    }

    public int getSettingsButtonX_optListLeft() {
        return settingsButtonX_optListLeft;
    }

    public int getSettingsButtonX_optListRight() {
        return settingsButtonX_optListRight;
    }

    public int getSettingsConfirmationMargin() {
        return settingsConfirmationMargin;
    }

    public int getSettingsButtonWidth_confirmation() {
        return settingsButtonWidth_confirmation;
    }

    public int getSettingsButtonY_confirmation() {
        return settingsButtonY_confirmation;
    }

    public int getSettingsButtonX_confirmationLeft() {
        return settingsButtonX_confirmationLeft;
    }

    public int getSettingsButtonX_confirmationRight() {
        return settingsButtonX_confirmationRight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getInGameMenuWidth() {
        return inGameMenuWidth;
    }

    public int getInGameMenuHeight() {
        return inGameMenuHeight;
    }

    public int getInGameMenuXL() {
        return inGameMenuXL;
    }

    public int getInGameMenuXR() {
        return inGameMenuXR;
    }

    public int getBoardX() {
        return boardX;
    }

    

}
