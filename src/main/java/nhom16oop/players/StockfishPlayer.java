package nhom16oop.players;

import nhom16oop.constants.PieceColor;
import nhom16oop.core.model.ChessMove;
import nhom16oop.core.model.ChessPiece;
import nhom16oop.core.model.ChessPosition;
import nhom16oop.core.pieces.Bishop;
import nhom16oop.core.pieces.Knight;
import nhom16oop.core.pieces.Queen;
import nhom16oop.core.pieces.Rook;
import nhom16oop.engine.Stockfish;
import nhom16oop.game.ChessController;
import nhom16oop.ui.board.ChessTile;
import nhom16oop.utils.ChessNotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Represents an AI player powered by the Stockfish chess engine.
 */
public class StockfishPlayer implements Player {

    private static final Logger logger = LoggerFactory.getLogger(StockfishPlayer.class);
    private final Stockfish stockfishEngine;
    private final ChessController chessController;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final int MOVE_DELAY_TIME = 1500;
    private final PieceColor stockfishColor;

    /**
     * Constructs a StockfishPlayer with the specified chess controller and color.
     *
     * @param chessController the controller managing the chess game
     * @param stockfishColor  the color of the player (White or Black)
     */
    public StockfishPlayer(ChessController chessController, PieceColor stockfishColor) {
        this.stockfishEngine = new Stockfish();
        this.stockfishEngine.start();
        this.chessController = chessController;
        this.stockfishColor = stockfishColor;
        logger.info("Stockfish player initialized with color: {}", stockfishColor.isWhite() ? "White" : "Black");
    }

    /**
     * Executes a move using the Stockfish engine after a short delay.
     */
    @Override
    public void makeMove() {
        executor.schedule(() -> {
            if (chessController.isGameEnded()) {
                return;
            }
            try {
                String bestMoveStr = stockfishEngine.getBestMove(ChessNotationUtils.getFEN(chessController.getBoardManager().getCurrentBoardState()));
                if (bestMoveStr != null) {
                    String startPos = bestMoveStr.substring(0, 2);
                    String endPos = bestMoveStr.substring(2, 4);
                    ChessPosition start = ChessPosition.toChessPosition(startPos);
                    ChessPosition end = ChessPosition.toChessPosition(endPos);
                    ChessPiece promotionPiece = null;

                    if (bestMoveStr.length() > 4) {
                        char promotion = bestMoveStr.charAt(4);
                        promotionPiece = getPromotionPiece(promotion, stockfishColor); // GÃ¡n giÃ¡ trá»‹ cho promotionPiece
                        logger.info("Promotion detected: {} to {} with promotion to {}", startPos, endPos, promotion);
                    } else if (isCastling(start, end)) {
                        logger.info("Castling move detected: {} to {}", startPos, endPos);
                    } else {
                        logger.info("Best move from Stockfish: {} to {}", startPos, endPos);
                    }

                    ChessTile startTile = chessController.getBoardUI().getTile(start);

                    if (startTile != null && startTile.getPiece() != null) {
                        chessController.getBoardUI().setCurrentLeftClickedTile(startTile);
                        logger.debug("Generated valid moves for AI piece at {}", startPos);
                    } else {
                        logger.warn("No piece found at start position: {}", startPos);
                        return;
                    }

                    ChessMove move = new ChessMove(start, end);
                    boolean success = chessController.movePiece(move, promotionPiece);

                    if (!success) {
                        logger.warn("Failed to execute Stockfish move: {} to {}", startPos, endPos);
                    } else {
                        chessController.getBoardUI().setCurrentLeftClickedTile(null);
                    }
                } else {
                    logger.warn("No best move returned by Stockfish");
                }
            } catch (Exception e) {
                logger.error("Error making move with Stockfish", e);
            }
        }, MOVE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the color of the Stockfish player.
     *
     * @return the PieceColor of the player
     */
    @Override
    public PieceColor getColor() {
        return stockfishColor;
    }

    /**
     * Cleans up resources by stopping the Stockfish engine and shutting down the executor.
     */
    @Override
    public void shutdown() {
        stockfishEngine.stopEngine();
        executor.shutdown();
        logger.info("StockfishPlayer shutdown");
    }

    private boolean isCastling(ChessPosition start, ChessPosition end) {
        if (start.row() == end.row() && Math.abs(start.col() - end.col()) == 2) {
            return start.toChessNotation().equals("e1") || start.toChessNotation().equals("e8");
        }
        return false;
    }

    private ChessPiece getPromotionPiece(char promotion, PieceColor color) {
        return switch (Character.toLowerCase(promotion)) {
            case 'q' -> new Queen(color);
            case 'r' -> new Rook(color);
            case 'b' -> new Bishop(color);
            case 'n' -> new Knight(color);
            default -> {
                logger.warn("Invalid promotion piece: {}, defaulting to Queen", promotion);
                yield new Queen(color);
            }
        };
    }
}
