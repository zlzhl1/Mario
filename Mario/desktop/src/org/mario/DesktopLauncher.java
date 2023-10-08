package org.mario;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * @author BINSHUO ZU (21012854)
 * @author LIMIN ZHOU (21012853)
 * @author ZIYE ZHANG (21012874)
 * @author ZHAOHUI LIANG (21012755)
 *     <p>Please note that on macOS your application needs to be started with the
 *     -XstartOnFirstThread JVM argument
 */
public class DesktopLauncher {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 720;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setWindowedMode(WIDTH, HEIGHT);
        config.useVsync(true);
        config.setTitle("Mario");
        config.setWindowIcon("images/icon.png");
        new Lwjgl3Application(new Game(), config);
    }
}
