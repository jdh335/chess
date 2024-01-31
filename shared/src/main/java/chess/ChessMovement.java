package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class ChessMovement {

    abstract Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition);

    private Collection<ChessMove> moveToEnd(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> pieceMoves = new HashSet<>();

        int rowDirection = endPosition.getRow() - startPosition.getRow();
        int colDirection = endPosition.getColumn() - startPosition.getColumn();

        ChessPiece currPiece = board.getPiece(startPosition);

        while (inBounds(endPosition)) {
            ChessPiece movePiece = board.getPiece(endPosition);
            if (movePiece == null) {
                pieceMoves.add(new ChessMove(startPosition, endPosition, null));
            } else {
                if (!currPiece.getTeamColor().equals(movePiece.getTeamColor())) {
                    pieceMoves.add(new ChessMove(startPosition, endPosition, null));
                    break;
                } else {
                    break;
                }
            }
            endPosition = new ChessPosition(endPosition.getRow() + rowDirection, endPosition.getColumn() + colDirection);
        }
        return pieceMoves;
    }

    private Collection<ChessMove> checkNeighbors(ChessBoard board, ChessPosition startPosition, int rowDirection, int colDirection) {
        Collection<ChessMove> pieceMoves = new HashSet<>();
        ChessPosition rowChangePosition = new ChessPosition(startPosition.getRow() + 2*rowDirection, startPosition.getColumn() + colDirection);
        ChessPosition colChangePosition = new ChessPosition(startPosition.getRow() + rowDirection, startPosition.getColumn() + 2*colDirection);
        if(inBounds(rowChangePosition) && isValidMove(board, startPosition, rowChangePosition)) {
            pieceMoves.add(new ChessMove(startPosition, rowChangePosition, null));
        }
        if(inBounds(colChangePosition) && isValidMove(board, startPosition, colChangePosition)) {
            pieceMoves.add(new ChessMove(startPosition, colChangePosition, null));
        }
        return pieceMoves;
    }

    boolean inBounds(ChessPosition position) {
        return (position.getRow() <= 8 && position.getRow() >= 1) &&
                (position.getColumn() <= 8 && position.getColumn() >= 1);
    }

    boolean isValidMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        //if (inBounds(startPosition) && inBounds(endPosition)) return false;
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);
        return (endPiece == null) ? true : !endPiece.getTeamColor().equals(startPiece.getTeamColor());
    }

    public static class KingMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> pieceMoves = new HashSet<>();
            ChessPiece currPiece = board.getPiece(startPosition);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j<= 1; j++) {
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + j);
                    if (inBounds(endPosition)) {
                        ChessMove endMove = (super.isValidMove(board, startPosition, endPosition)) ? new ChessMove(startPosition, endPosition, null) : null;
                        if (endMove != null) {
                            pieceMoves.add(endMove);
                        }
                    }
                }
            }
            return  pieceMoves;
        }
    }

    public static class PawnMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> pieceMoves = new HashSet<>();
            ChessPiece.PieceType promotionPiece = null;

            ChessPiece currPiece = board.getPiece(startPosition);
            int direction = (currPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) ? 1 : -1;

            ChessPosition forward = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn());
            ChessPosition diagonal1 = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn() + direction);
            ChessPosition diagonal2 = new ChessPosition(startPosition.getRow() + direction, startPosition.getColumn() - direction);


            ChessMove forwardMove = (board.getPiece(forward) == null) ? new ChessMove(startPosition, forward, null) : null;
            ChessMove diagonalMove1 = (board.getPiece(diagonal1) != null && super.isValidMove(board, startPosition, diagonal1)) ? new ChessMove(startPosition, diagonal1, null) : null;
            ChessMove diagonalMove2 = (board.getPiece(diagonal2) != null && super.isValidMove(board, startPosition, diagonal2)) ? new ChessMove(startPosition, diagonal2, null) : null;

            if ((direction == 1 && startPosition.getRow() == 7) || (direction == -1 && startPosition.getRow() == 2)) {
                if (forwardMove != null) {
                    for (int i = 1; i <= 4; i++) {
                        forwardMove.setPromotionPiece((ChessPiece.PieceType.values()[i]));
                        pieceMoves.add(new ChessMove(forwardMove));
                    }
                }
                if (diagonalMove1 != null) {
                    for (int i = 1; i <= 4; i++) {
                        diagonalMove1.setPromotionPiece((ChessPiece.PieceType.values()[i]));
                        pieceMoves.add(new ChessMove(diagonalMove1));
                    }
                }
                if (diagonalMove2 != null) {
                    for (int i = 1; i <= 4; i++) {
                        diagonalMove2.setPromotionPiece((ChessPiece.PieceType.values()[i]));
                        pieceMoves.add(new ChessMove(diagonalMove2));
                    }
                }
            } else {
                if (forwardMove != null) {
                    pieceMoves.add(forwardMove);
                    if ((direction == 1 && startPosition.getRow() == 2) || (direction == -1 && startPosition.getRow() == 7)) {
                        ChessPosition forwardTwo = new ChessPosition(startPosition.getRow() + 2*direction, startPosition.getColumn());
                        ChessMove forwardTwoMove = (board.getPiece(forwardTwo) == null) ? new ChessMove(startPosition, forwardTwo, null) : null;
                        if (forwardTwoMove != null) {
                            pieceMoves.add(forwardTwoMove);
                        }
                    }
                }
                if (diagonalMove1 != null) {
                    pieceMoves.add(diagonalMove1);
                }
                if (diagonalMove2 != null) {
                    pieceMoves.add(diagonalMove2);
                }
            }

            return pieceMoves;
        }

    }

    public static class BishopMovement extends ChessMovement {

        @Override
        public Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> pieceMoves = new HashSet<>();
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1)));
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1)));
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1)));
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1)));
            return pieceMoves;
        }

    }

    public static class RookMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> pieceMoves = new HashSet<>();
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1)));
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1)));
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn())));
            pieceMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn())));
            return pieceMoves;
        }
    }

    public static class QueenMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> pieceMoves = new HashSet<>();
            pieceMoves.addAll(new BishopMovement().calculateMovements(board, startPosition));
            pieceMoves.addAll(new RookMovement().calculateMovements(board, startPosition));
            return pieceMoves;
        }
    }

    public static class KnightMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> pieceMoves = new HashSet<>();
            pieceMoves.addAll(super.checkNeighbors(board, startPosition, 1, 1));
            pieceMoves.addAll(super.checkNeighbors(board, startPosition, 1, -1));
            pieceMoves.addAll(super.checkNeighbors(board, startPosition, -1, 1));
            pieceMoves.addAll(super.checkNeighbors(board, startPosition, -1, -1));
            return pieceMoves;
        }
    }
}
