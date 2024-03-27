package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;

    private ChessBoard board;

    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
//        this.board = new ChessBoard();
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return this.teamTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) { this.teamTurn = team; }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) { this.board = board; }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() { return this.board; }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard originalBoard = getBoard();
        Collection<ChessMove> validMoves = new HashSet<>();

        ChessPiece piece = this.board.getPiece(startPosition);
        if (piece == null) { return null; }

        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : pieceMoves) {
            ChessBoard testBoard = board.duplicateBoard();

            testBoard.addPiece(move.getStartPosition(), null);
            testBoard.addPiece(move.getEndPosition(), piece);

            setBoard(testBoard);
            if (!isInCheck(piece.getTeamColor())) { validMoves.add(move); }
            setBoard(originalBoard);
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece currPiece = board.getPiece(move.getStartPosition());
        Collection<ChessMove> validMove = this.validMoves(move.getStartPosition());

        ChessPiece.PieceType pieceType = (move.getPromotionPiece() == null) ? currPiece.getPieceType() : move.getPromotionPiece();
        if (validMove.contains(move) && currPiece.getTeamColor().equals(this.getTeamTurn())) {

            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), new ChessPiece(currPiece.getTeamColor(), pieceType));

            this.setTeamTurn((this.getTeamTurn().equals(TeamColor.WHITE)) ? TeamColor.BLACK : TeamColor.WHITE);
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = this.board.getKingPosition(teamColor);
        TeamColor enemyTeamColor = (teamColor.equals(TeamColor.WHITE)) ? TeamColor.BLACK : TeamColor.WHITE;
        Map<ChessPosition, ChessPiece> enemyPieces = board.getTeamPieces(enemyTeamColor);

        for (ChessPosition position : enemyPieces.keySet()) {
            ChessPiece enemyPiece = enemyPieces.get(position);
            for (ChessMove move : enemyPiece.pieceMoves(this.board, position)) {
                if (move.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        for (ChessPosition position : board.getTeamPieces(teamColor).keySet()) {
            if (!validMoves(position).isEmpty()) { return false; }
        }
        return isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (ChessPosition position : board.getTeamPieces(teamColor).keySet()) {
            if (!validMoves(position).isEmpty()) { return false; }
        }
        return !isInCheck(teamColor);
    }

}
