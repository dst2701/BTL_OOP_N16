package nhom16oop;

import nhom16oop.game.ChessLauncher;
import nhom16oop.utils.ImageLoader;
import nhom16oop.utils.SoundPlayer;

public class ChessGame {
    public static void main(String[] args) {
        // Set up JVM options for high DPI displays
        // This is a workaround for high DPI scaling issues on Windows
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            System.setProperty("sun.java2d.uiScale", "1.0");
            System.setProperty("sun.java2d.dpiaware", "true");
        }

        // Load resources
        ImageLoader.preloadImages();
        SoundPlayer.preloadSounds();

        // Launch the chess game
        ChessLauncher.launch();
    }
}
