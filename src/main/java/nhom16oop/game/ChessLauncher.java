package nhom16oop.game;

import nhom16oop.constants.PieceColor;
import nhom16oop.ui.ChessUI;
import nhom16oop.ui.components.dialogs.GameModeSelectionDialog;

public class ChessLauncher {
    public static void launch() {
        GameModeSelectionDialog dialog = new GameModeSelectionDialog(null);
        dialog.setVisible(true);

        int selectedMode = dialog.getSelectedMode();
        PieceColor selectedColor = dialog.getSelectedColor();

        ChessUI chessUI = new ChessUI(selectedMode, selectedColor);
        chessUI.show();
    }
}
