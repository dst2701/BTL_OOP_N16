package nhom16oop.game;

import nhom16oop.constants.PieceColor;
import nhom16oop.core.model.ChessMove;
import nhom16oop.core.model.ChessPiece;
import nhom16oop.core.model.ChessPosition;

public interface MoveExecutor {
    boolean executeMove(ChessMove move, ChessPiece promotionPiece);

    default boolean executeMove(ChessMove move) {
        return executeMove(move, null);
    }

    boolean performCastling(boolean isKingside, PieceColor color);

    boolean performEnPassant(ChessMove move);

    ChessPiece promotePawn(ChessPosition position, PieceColor color);
}
