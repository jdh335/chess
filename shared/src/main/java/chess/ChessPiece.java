package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;

    private String pieceChar;

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
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
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
        Collection<ChessMove> pieceMoves = null;
        ChessPiece currPiece = board.getPiece(myPosition);

        switch (currPiece.pieceType) {
            case PAWN -> pieceMoves = new ChessMovement.PawnMovement().calculateMovements(board, myPosition);
            case ROOK -> pieceMoves = new ChessMovement.RookMovement().calculateMovements(board, myPosition);
            case KNIGHT -> pieceMoves = new ChessMovement.KnightMovement().calculateMovements(board, myPosition);
            case BISHOP -> pieceMoves = new ChessMovement.BishopMovement().calculateMovements(board, myPosition);
            case QUEEN -> pieceMoves = new ChessMovement.QueenMovement().calculateMovements(board, myPosition);
            case KING -> pieceMoves = new ChessMovement.KingMovement().calculateMovements(board, myPosition);
        }
        return pieceMoves;
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
