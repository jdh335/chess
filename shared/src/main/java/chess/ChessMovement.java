package chess;

import java.util.Collection;

public abstract class ChessMovement {

    abstract Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition);

    abstract boolean isValidMove();
    ChessGame.TeamColor getPieceColor (ChessBoard board, ChessPosition position) {
        return (board.getPiece(position) != null) ? board.getPiece(position).getTeamColor() : null;
    }

    boolean inBounds(ChessPosition position) {
        return (position.getRow() > 8 || position.getRow() < 1) ||
                (position.getColumn() > 8 || position.getColumn() < 1);
    }

    public class PawnMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            return null;
        }

        @Override
        boolean isValidMove() {
            return false;
        }
    }

    public class BishopMovement extends ChessMovement {

        @Override
        public Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            ChessPosition possiblePosition;
            ChessMove possibleMove;
//            for (int i = 0; i < )
            return null;
        }

        @Override
        boolean isValidMove() {
            return false;
        }
    }
}
