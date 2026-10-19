package main.java.nathansdc.damas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import main.java.nathansdc.damas.ia.AI.Difficulty;
import main.java.nathansdc.damas.ui.ColorScheme.Theme;

public class Settings {

    public static final String SETTINGS_PATH = System.getProperty("user.home") + File.separator + ".damas" + File.separator + "settings.conf";

    private Properties bridge = new Properties();

    private Settings() {
        restore();

        recover();
    }

    private static Settings instance;

    public static Settings getInstance(){
        if (instance == null)
            return instance = new Settings();
        return instance;
    }

    public void save() {
        try {
            File file = new File(SETTINGS_PATH);
            // Se a pasta não existia agora existe
            file.getParentFile().mkdirs();
            // Se o arquivo não existia agora existe
            if (file.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(file);
                bridge.store(fos, "Settings updated");
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(file);
                fos.flush();
                bridge.store(fos, "Settings updated");
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean recover() {
        try {
            File f = new File(SETTINGS_PATH);

            if (!f.exists()) {
                if(f.getParentFile().mkdirs())
                    f.createNewFile();
                else throw new Exception("Erro ao recuperar o arquivo > Erro ao gerar o arquivo");
            }

            
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (FileInputStream fis = new FileInputStream(SETTINGS_PATH)) {
            bridge.load(fis);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            restore();
            return false;
        }
    }

    public void restore() {
        bridge.setProperty("playing-against-ai", ""+false);
        bridge.setProperty("forced-capture", ""+false);
        bridge.setProperty("forced-promotion", ""+false);
        bridge.setProperty("both-direction-capture", ""+false);
        bridge.setProperty("app-name", "damas");
        bridge.setProperty("app-theme", Theme.CLASSIC.name());
        bridge.setProperty("difficulty", Difficulty.NORMAL.name());
        bridge.setProperty("menu-style","default");
    }

    public boolean isPlaying_against_ai() {
        return Boolean.parseBoolean(bridge.getProperty("playing-against-ai"));
    }

    public void setPlaying_against_ai(boolean arg0) {
        bridge.setProperty("playing-against-ai", ""+arg0);
    }

    public boolean isForced_promotion() {
        return Boolean.parseBoolean(bridge.getProperty("forced-promotion"));
    }

    public void setForced_promotion(boolean arg0) {
        bridge.setProperty("forced-promotion", ""+arg0);
    }

    public boolean isForced_capture() {
        return Boolean.parseBoolean(bridge.getProperty("forced-capture"));
    }

    public void setForced_capture(boolean arg0) {
        bridge.setProperty("forced-capture", ""+arg0);
    }

    public boolean isBoth_direction_capture() {
        return Boolean.parseBoolean(bridge.getProperty("both-direction-capture"));
    }

    public void setBoth_direction_capture(boolean arg0) {
        bridge.setProperty("both-direction-capture", ""+arg0);
    }

    public String getApp_name() {
        return bridge.getProperty("app-name");
    }

    public void setApp_name(String arg0) {
        bridge.setProperty("app-name", arg0);
    }

    public Theme getApp_theme() {
        return Theme.valueOf(bridge.getProperty("app-theme").toUpperCase().trim());
    }

    public void setApp_theme(Theme arg0) {
        bridge.setProperty("app-theme", ""+arg0.name());
    }

    public Difficulty getDifficulty() {
        return Difficulty.valueOf(bridge.getProperty("difficulty").toUpperCase().trim());
    }

    public void setDifficulty(Difficulty arg0) {
        bridge.setProperty("difficulty", arg0.name());
    }

    public String getMenu_style() {
        return bridge.getProperty("menu-style");
    }

    public void setMenu_style(String arg0) {
        bridge.setProperty("menu-style", arg0);
    }

    public Properties getAll() {
        return bridge;
    }

}
