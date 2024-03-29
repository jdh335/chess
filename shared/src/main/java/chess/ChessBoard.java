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
        board = new ChessPiece[8][8];
//        this.resetBoard();
    }

    private final Map<ChessPosition, ChessPiece> whitePieces = new HashMap<>();

    private final Map<ChessPosition, ChessPiece> blackPieces = new HashMap<>();

    public Map<ChessPosition, ChessPiece> getTeamPieces(ChessGame.TeamColor team) {
        return (team.equals(ChessGame.TeamColor.WHITE) ? this.whitePieces : this.blackPieces);
    }

    public boolean inBounds(ChessPosition position) {
        return (position.getRow() <= 8 && position.getRow() >= 1) &&
                (position.getColumn() <= 8 && position.getColumn() >= 1);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (this.inBounds(position)) {
            ChessPiece oldPiece = this.getPiece(position);
            if (oldPiece != null) {
                Map<ChessPosition, ChessPiece> oldPieceTeam = this.getTeamPieces(oldPiece.getTeamColor());
                oldPieceTeam.remove(position);
            }
            if (piece != null) {
                Map<ChessPosition, ChessPiece> newPieceTeam = this.getTeamPieces(piece.getTeamColor());
                newPieceTeam.put(position, piece);
            }
            board[position.getRow() - 1][position.getColumn() - 1] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (!this.inBounds(position)) return null;
        return board[position.getRow()-1][position.getColumn()-1];
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        Map<ChessPosition, ChessPiece> teamPieces = this.getTeamPieces(teamColor);
        for (ChessPosition position : teamPieces.keySet()) {
            ChessPiece currPiece = this.getPiece(position);
            if (currPiece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                return position;
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
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;

        // Insert pawns into the game board
        for (int i = 1; i <= 8; i++ ) {
            this.addPiece(new ChessPosition(2, i) , new ChessPiece(white, ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(7, i), new ChessPiece(black, ChessPiece.PieceType.PAWN));
        }

        // Insert all other pieces into the game board
        for (int i = 1; i <= 8; i++) {
            // Insert rooks into the game board
            if (i == 1 || i == 8) {
                this.addPiece(new ChessPosition(1,i), new ChessPiece(white, ChessPiece.PieceType.ROOK));
                this.addPiece(new ChessPosition(8,i), new ChessPiece(black, ChessPiece.PieceType.ROOK));
            }
            // Insert knights into the game board
            else if (i == 2 || i == 7) {
                this.addPiece(new ChessPosition(1,i), new ChessPiece(white, ChessPiece.PieceType.KNIGHT));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(black, ChessPiece.PieceType.KNIGHT));
            }
            // Insert bishops into the game board
            else if (i == 3 || i == 6) {
                this.addPiece(new ChessPosition(1,i), new ChessPiece(white, ChessPiece.PieceType.BISHOP));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(black, ChessPiece.PieceType.BISHOP));
            }
            // Insert queens into the game board
            else if (i == 4) {
                this.addPiece(new ChessPosition(1, i), new ChessPiece(white, ChessPiece.PieceType.QUEEN));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(black, ChessPiece.PieceType.QUEEN));
            }
            // Insert kings into the game board
            else if (i == 5) {
                this.addPiece(new ChessPosition(1, i), new ChessPiece(white, ChessPiece.PieceType.KING));
                this.addPiece(new ChessPosition(8, i), new ChessPiece(black, ChessPiece.PieceType.KING));
            }
        }
    }

    public ChessBoard duplicateBoard() {
        ChessBoard newBoard = new ChessBoard();
        Map<ChessPosition, ChessPiece> blackPieces = this.getTeamPieces(ChessGame.TeamColor.BLACK);
        Map<ChessPosition, ChessPiece> whitePieces = this.getTeamPieces(ChessGame.TeamColor.WHITE);

        for (ChessPosition position : blackPieces.keySet()) {
            newBoard.addPiece(position, blackPieces.get(position));
        }
        for (ChessPosition position : whitePieces.keySet()) {
            newBoard.addPiece(position, whitePieces.get(position));
        }

        return newBoard;
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













