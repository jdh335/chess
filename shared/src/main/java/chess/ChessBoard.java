package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board;
    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    public ChessBoard duplicateBoard() {
        ChessBoard newBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                newBoard.addPiece(currPosition, this.getPiece(currPosition));
            }
        }
        return newBoard;
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = this.getPiece(currPosition);
                if (currPiece != null && currPiece.pieceType == ChessPiece.PieceType.KING && currPiece.pieceColor == teamColor) {
                    return currPosition;
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Start with an empty board
        this.board = new ChessPiece[8][8];
        ChessPosition currPosition;
        // Insert pawns into the game board
        for (int i = 1; i <= 8; i++ ) {
            currPosition = new ChessPosition(2, i);
            this.addPiece(currPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            currPosition = new ChessPosition(7, i);
            this.addPiece(currPosition, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        // Insert all other pieces into the game board

        for (int i = 1; i <= 8; i++) {
            // Insert rooks into the game board
            if (i == 1 || i == 8) {
                this.addPiece(new ChessPosition(1,i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                this.addPiece(new ChessPosition(8,i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            }
            // Insert knights into the game board
            else if (i == 2 || i == 7) {
                this.addPiece(new ChessPosition(1,i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            }
            // Insert bishops into the game board
            else if (i == 3 || i == 6) {
                this.addPiece(new ChessPosition(1,i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            }
            // Insert queens into the game board
            else if (i == 4) {
                this.addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
            }
            // Insert kings into the game board
            else if (i == 5) {
                this.addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                sb.append("|");
                sb.append((this.getPiece(new ChessPosition(i, j)) != null) ?
                        this.getPiece(new ChessPosition(i, j)).toString() : "   ");
            }
            sb.append("|\n");
        }
        return sb.reverse().toString();
    }
}













