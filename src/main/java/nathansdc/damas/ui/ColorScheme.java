package main.java.nathansdc.damas.ui;

import java.awt.Color;

public class ColorScheme {

    public enum Theme {
        CLASSIC,
        DARK,
        LIGHT,
        BLUE,
        PINK,
        GREEN;

        public static String[] getStringValues() {
            String[] values = new String[values().length];
            int iterator = 0;
            for (Theme t : values()) {
                String n = t.name().toLowerCase();
                values[iterator] = n.substring(0, 1).toUpperCase() + n.substring(1);
                iterator++;
            }
            return values;
        }
    }

    private Theme currentTheme = Theme.CLASSIC;

    private static final ColorScheme instance = new ColorScheme();

    private ColorScheme() {
    }

    public static ColorScheme getInstance() {
        return instance;
    }

    // ------------------------------------------------------------------------

    /**
     * texto
     */
    private Color textColor = Color.decode("#000000");

    /**
     * casa a qual a peça selecionada pode se mover para
     */
    private Color auxiliarBoardColor1 = Color.decode("#699279");

    /**
     * peça inimiga marcada como possivel captura
     */
    private Color auxiliarBoardColor2 = Color.decode("#cf7f8d");

    /**
     * peça selecionada atualmente
     */
    private Color auxiliarBoardColor3 = Color.decode("#c7dbdd");

    /**
     * tabuleiro: casa +escura
     */
    private Color boardColor1 = Color.decode("#c08b60");

    /**
     * tabuleiro: casa +clara
     */
    private Color boardColor2 = Color.decode("#dfc399");

    /**
     * menu: cor principal
     */
    private Color menuColor = Color.decode("#dfc399");

    /**
     * menu: botões: cor padrão
     */
    private Color menuButtonsColor = Color.decode("#000000");

    /**
     * menu: botões: cor padrão
     */
    private Color menuButtonsColor_selectedCheckbox = Color.decode("#00acd7");

    /**
     * menu: botões "onHover" / menu: botões: texto "onHover"
     */
    private Color menuButtonsColor_onHover = Color.decode("#c08b60");

    /**
     * peças: peça branca, cor principal
     */
    private Color player1_Color1 = Color.decode("#ffffff");
    // 412b17
    // ffffff

    /**
     * peças: peça branca, cor secundária
     */
    private Color player1_Color2 = Color.decode("#dfdfdf");
    // 332411
    // dfdfdf

    /**
     * peças: peça preta, cor principal
     */
    private Color player2_Color1 = Color.decode("#d85757");
    // d85757
    // 333333

    /**
     * peças: peça preta, cor secundária
     */
    private Color player2_Color2 = Color.decode("#ad3e3e");
    // ad3e3e
    // 1f1f1f

    /**
     * peças: marcador de dama
     */
    private Color kingsColor = Color.decode("#dbbd36");

    // ------------------------------------------------------------------------

    public Theme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(Theme currentTheme) {
        this.currentTheme = currentTheme;

        switch (currentTheme) {
            case DARK:
                textColor = Color.decode("#dfc399");
                auxiliarBoardColor1 = Color.decode("#699279");
                auxiliarBoardColor2 = Color.decode("#cf7f8d");
                auxiliarBoardColor3 = Color.decode("#00acd7");
                boardColor1 = Color.decode("#333333");
                boardColor2 = Color.decode("#767667");
                menuColor = Color.decode("#333333");
                menuButtonsColor = Color.decode("#dfc399");
                menuButtonsColor_onHover = Color.decode("#dfc399");
                menuButtonsColor_selectedCheckbox = Color.decode("#699279");
                player1_Color1 = Color.decode("#303030");
                player1_Color2 = Color.decode("#272727");
                player2_Color1 = Color.decode("#d85757");
                player2_Color2 = Color.decode("#ad3e3e");
                kingsColor = Color.decode("#dbbd36");
                break;
            case LIGHT:
                textColor = Color.decode("#000000");
                auxiliarBoardColor1 = Color.decode("#699279");
                auxiliarBoardColor2 = Color.decode("#cf7f8d");
                auxiliarBoardColor3 = Color.decode("#00acd7");
                boardColor1 = Color.decode("#e9e9e9");
                boardColor2 = Color.decode("#c2bfbf");
                menuColor = Color.decode("#e9e9e9");
                menuButtonsColor = Color.decode("#3c3c3c");
                menuButtonsColor_onHover = Color.decode("#3c3c3c");
                menuButtonsColor_selectedCheckbox = Color.decode("#699279");
                player1_Color1 = Color.decode("#303030");
                player1_Color2 = Color.decode("#272727");
                player2_Color1 = Color.decode("#d85757");
                player2_Color2 = Color.decode("#ad3e3e");
                kingsColor = Color.decode("#dbbd36");
                break;
            case BLUE:
                textColor = Color.decode("#0000ff");
                auxiliarBoardColor1 = Color.decode("#699279");
                auxiliarBoardColor2 = Color.decode("#cf7f8d");
                auxiliarBoardColor3 = Color.decode("#c7dbdd");
                boardColor1 = Color.decode("#c08b60");
                boardColor2 = Color.decode("#dfc399");
                menuColor = Color.decode("#dfc399");
                menuButtonsColor = Color.decode("#000000");
                menuButtonsColor_onHover = Color.decode("#c08b60");
                menuButtonsColor_selectedCheckbox = Color.decode("#699279");
                player1_Color1 = Color.decode("#ffffff");
                player1_Color2 = Color.decode("#dfdfdf");
                player2_Color1 = Color.decode("#d85757");
                player2_Color2 = Color.decode("#ad3e3e");
                kingsColor = Color.decode("#dbbd36");
                break;
            case PINK:
                textColor = Color.decode("#ffffff");
                auxiliarBoardColor1 = Color.decode("#699279");
                auxiliarBoardColor2 = Color.decode("#cf7f8d");
                auxiliarBoardColor3 = Color.decode("#c7dbdd");
                boardColor1 = Color.decode("#c08b60");
                boardColor2 = Color.decode("#dfc399");
                menuColor = Color.decode("#e2a0e2");
                menuButtonsColor = Color.decode("#ffffff");
                menuButtonsColor_onHover = Color.decode("#ffffff");
                menuButtonsColor_selectedCheckbox = Color.decode("#699279");
                player1_Color1 = Color.decode("#ffffff");
                player1_Color2 = Color.decode("#dfdfdf");
                player2_Color1 = Color.decode("#d85757");
                player2_Color2 = Color.decode("#ad3e3e");
                kingsColor = Color.decode("#dbbd36");
                break;
            case GREEN:
                textColor = Color.decode("#ffffff");
                auxiliarBoardColor1 = Color.decode("#c7dbdd");
                auxiliarBoardColor2 = Color.decode("#cf7f8d");
                auxiliarBoardColor3 = Color.decode("#00acd7");
                boardColor1 = Color.decode("#316846");
                boardColor2 = Color.decode("#699279");
                menuColor = Color.decode("#699279");
                menuButtonsColor = Color.decode("#ffffff");
                menuButtonsColor_onHover = Color.decode("#ebebeb");
                menuButtonsColor_selectedCheckbox = Color.decode("#00acd7");
                player1_Color1 = Color.decode("#ffffff");
                player1_Color2 = Color.decode("#dfdfdf");
                player2_Color1 = Color.decode("#d85757");
                player2_Color2 = Color.decode("#ad3e3e");
                kingsColor = Color.decode("#dbbd36");
                break;
            case CLASSIC:
            default:
                textColor = Color.decode("#000000");
                auxiliarBoardColor1 = Color.decode("#699279");
                auxiliarBoardColor2 = Color.decode("#cf7f8d");
                auxiliarBoardColor3 = Color.decode("#c7dbdd");
                boardColor1 = Color.decode("#c08b60");
                boardColor2 = Color.decode("#dfc399");
                menuColor = Color.decode("#dfc399");
                menuButtonsColor = Color.decode("#000000");
                menuButtonsColor_onHover = Color.decode("#c08b60");
                menuButtonsColor_selectedCheckbox = Color.decode("#699279");
                player1_Color1 = Color.decode("#ffffff");
                player1_Color2 = Color.decode("#dfdfdf");
                player2_Color1 = Color.decode("#d85757");
                player2_Color2 = Color.decode("#ad3e3e");
                kingsColor = Color.decode("#dbbd36");
                break;
        }

    }

    public Color getTextColor() {
        return textColor;
    }

    public Color getAuxiliarBoardColor1() {
        return auxiliarBoardColor1;
    }

    public Color getAuxiliarBoardColor2() {
        return auxiliarBoardColor2;
    }

    public Color getAuxiliarBoardColor3() {
        return auxiliarBoardColor3;
    }

    public Color getBoardColor1() {
        return boardColor1;
    }

    public Color getBoardColor2() {
        return boardColor2;
    }

    public Color getMenuColor() {
        return menuColor;
    }

    public Color getMenuButtonsColor() {
        return menuButtonsColor;
    }

    public Color getMenuButtonsColor_onHover() {
        return menuButtonsColor_onHover;
    }

    public Color getMenuButtonsColor_selectedCheckbox() {
        return menuButtonsColor_selectedCheckbox;
    }

    public Color getPlayer1_Color1() {
        return player1_Color1;
    }

    public Color getPlayer1_Color2() {
        return player1_Color2;
    }

    public Color getPlayer2_Color1() {
        return player2_Color1;
    }

    public Color getPlayer2_Color2() {
        return player2_Color2;
    }

    public Color getKingsColor() {
        return kingsColor;
    }
}
