package chess;

import java.util.HashMap;
import java.util.Map;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private Map<ChessPosition, ChessPiece> board;

    public ChessBoard() {
        this.board = new HashMap<>();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPosition currPosition;
        // Insert pawns into the game board
        for (int i = 1; i <= 8; i++ ) {
            currPosition = new ChessPosition(i, 2);
            board.put(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            currPosition = new ChessPosition(i, 7);
            board.put(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        // Insert all other pieces into the game board
        for (ChessPiece.PieceType pieceType : ChessPiece.PieceType.values()) {
            for (int i = 1; i <= 8; i++) {
                // Insert rooks into the game board
                if (i == 1 || i == 8) {
                    currPosition = new ChessPosition(1,i);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    currPosition = new ChessPosition(8,i);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                }
                // Insert knights into the game board
                else if (i == 2 || i == 7) {
                    currPosition = new ChessPosition(1,i);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    currPosition = new ChessPosition(8, i);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                }
                // Insert bishops into the game board
                else if (i == 3 || i == 6) {
                    currPosition = new ChessPosition(1,i);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    currPosition = new ChessPosition(8, i);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                }
                // Insert kings into the game board
                else if (i == 4) {
                    currPosition = new ChessPosition(1, 4);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                    currPosition = new ChessPosition(8, 5);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
                }
                // Insert queens into the game board
                else if (i == 5) {
                    currPosition = new ChessPosition(1, 5);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                    currPosition = new ChessPosition(8, 4);
                    board.put(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
                }
            }
        }
    }
}
