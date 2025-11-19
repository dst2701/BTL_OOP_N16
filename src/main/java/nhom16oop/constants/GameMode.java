package nhom16oop.constants;

public final class GameMode {
    public static final int PLAYER_VS_PLAYER = 1;
    public static final int PLAYER_VS_AI = 2;
    public static final int PUZZLE_MODE = 3;

    private GameMode() {
        throw new AssertionError("Cannot instantiate GameMode class");
    }
}
