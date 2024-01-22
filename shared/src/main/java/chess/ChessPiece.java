package chess;

import chess.movement.BishopMovement;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType pieceType;

    String pieceChar;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
        switch (type) {
            case PAWN -> this.pieceChar = (pieceColor.equals(ChessGame.TeamColor.WHITE)) ? "♙" : "♟";
            case ROOK -> this.pieceChar = (pieceColor.equals(ChessGame.TeamColor.WHITE)) ? "♖" : "♜";
            case KNIGHT -> this.pieceChar = (pieceColor.equals(ChessGame.TeamColor.WHITE)) ? "♘" : "♞";
            case BISHOP -> this.pieceChar = (pieceColor.equals(ChessGame.TeamColor.WHITE)) ? "♗" : "♝";
            case QUEEN -> this.pieceChar = (pieceColor.equals(ChessGame.TeamColor.WHITE)) ? "♕" : "♛";
            case KING -> this.pieceChar = (pieceColor.equals(ChessGame.TeamColor.WHITE)) ? "♔" : "♚";

        }
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = null;
        ChessPiece currPiece = board.getPiece(myPosition);

        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        ChessPosition possiblePosition;
        ChessMove possibleMove;

        ChessMovement chessMovement;

        switch (currPiece.pieceType) {
            case PAWN -> validMoves = null;
            case ROOK -> validMoves = null;
            case KNIGHT -> validMoves = null;
            case BISHOP -> validMoves = new BishopMovement().calculateMovements(board, myPosition);
            case QUEEN -> validMoves = null;
            case KING -> validMoves = null;
        }

        if (currPiece.pieceType.equals(PieceType.PAWN)) {

            // forward advancement move
            if (currPiece.pieceColor.equals(ChessGame.TeamColor.WHITE)) {
                possiblePosition = new ChessPosition(row + 1, col);
                if (board.getPiece(possiblePosition) == null) {
                    possibleMove = new ChessMove(myPosition, possiblePosition, null);
                    validMoves.add(possibleMove);
                }
            } else {
                possiblePosition = new ChessPosition(row - 1, col);
                if (board.getPiece(possiblePosition) == null) {
                    possibleMove = new ChessMove(myPosition, possiblePosition, null);
                    validMoves.add(possibleMove);
                }
            }

            // diagonal capture move
            if (currPiece.pieceColor.equals(ChessGame.TeamColor.WHITE)) {
                possiblePosition = new ChessPosition(row + 1, col + 1);
                if (board.getPiece(possiblePosition) != null) {
                    possibleMove = new ChessMove(myPosition, possiblePosition, null);
                    validMoves.add(possibleMove);
                }
                possiblePosition = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(possiblePosition) != null) {
                    possibleMove = new ChessMove(myPosition, possiblePosition, null);
                    validMoves.add(possibleMove);
                }
            } else {
                possiblePosition = new ChessPosition(row - 1, col);
                if (board.getPiece(possiblePosition) == null) {
                    possibleMove = new ChessMove(myPosition, possiblePosition, null);
                    validMoves.add(possibleMove);
                }
            }
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && getPieceType() == that.getPieceType();
    }

    @Override
    public String toString() {
        return pieceChar;
    }
}
