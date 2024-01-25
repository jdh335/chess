package chess;

import java.util.Collection;
import java.util.HashSet;

public abstract class ChessMovement {

    abstract Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition);

    private Collection<ChessMove> moveToEnd(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        int rowDirection = endPosition.getRow() - startPosition.getRow();
        int colDirection = endPosition.getColumn() - startPosition.getColumn();

        ChessPiece currPiece = board.getPiece(startPosition);

        while (inBounds(endPosition)) {
            ChessPiece movePiece = board.getPiece(endPosition);
            if (movePiece == null) {
                validMoves.add(new ChessMove(startPosition, endPosition, null));
            } else {
                if (!currPiece.getTeamColor().equals(movePiece.getTeamColor())) {
                    validMoves.add(new ChessMove(startPosition, endPosition, null));
                    break;
                } else {
                    break;
                }
            }
            endPosition = new ChessPosition(endPosition.getRow() + rowDirection, endPosition.getColumn() + colDirection);
        }
        return validMoves;
    }

    private Collection<ChessMove> checkNeighbors(ChessBoard board, ChessPosition startPosition, int rowDirection, int colDirection) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition rowChangePosition = new ChessPosition(startPosition.getRow() + 2*rowDirection, startPosition.getColumn() + colDirection);
        ChessPosition colChangePosition = new ChessPosition(startPosition.getRow() + rowDirection, startPosition.getColumn() + 2*colDirection);
        if(inBounds(rowChangePosition) && isValidMove(board, startPosition, rowChangePosition)) {
            validMoves.add(new ChessMove(startPosition, rowChangePosition, null));
        }
        if(inBounds(colChangePosition) && isValidMove(board, startPosition, colChangePosition)) {
            validMoves.add(new ChessMove(startPosition, colChangePosition, null));
        }
        return validMoves;
    }

    boolean inBounds(ChessPosition position) {
        return (position.getRow() <= 8 && position.getRow() >= 1) &&
                (position.getColumn() <= 8 && position.getColumn() >= 1);
    }

    boolean isValidMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);
        return (endPiece == null) ? true : !endPiece.getTeamColor().equals(startPiece.getTeamColor());
    }

    public static class KingMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> validMoves = new HashSet<>();
            ChessPiece currPiece = board.getPiece(startPosition);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j<= 1; j++) {
                    ChessPosition endPosition = new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + j);
                    if (inBounds(endPosition)) {
                        ChessMove endMove = (super.isValidMove(board, startPosition, endPosition)) ? new ChessMove(startPosition, endPosition, null) : null;
                        if (endMove != null) {
                            validMoves.add(endMove);
                        }
                    }
                }
            }
            return  validMoves;
        }
    }

    public static class PawnMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> validMoves = new HashSet<>();
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
                        validMoves.add(new ChessMove(forwardMove));
                    }
                }
                if (diagonalMove1 != null) {
                    for (int i = 1; i <= 4; i++) {
                        diagonalMove1.setPromotionPiece((ChessPiece.PieceType.values()[i]));
                        validMoves.add(new ChessMove(diagonalMove1));
                    }
                }
                if (diagonalMove2 != null) {
                    for (int i = 1; i <= 4; i++) {
                        diagonalMove2.setPromotionPiece((ChessPiece.PieceType.values()[i]));
                        validMoves.add(new ChessMove(diagonalMove2));
                    }
                }
            } else {
                if (forwardMove != null) {
                    validMoves.add(forwardMove);
                    if ((direction == 1 && startPosition.getRow() == 2) || (direction == -1 && startPosition.getRow() == 7)) {
                        ChessPosition forwardTwo = new ChessPosition(startPosition.getRow() + 2*direction, startPosition.getColumn());
                        ChessMove forwardTwoMove = (board.getPiece(forwardTwo) == null) ? new ChessMove(startPosition, forwardTwo, null) : null;
                        if (forwardTwoMove != null) {
                            validMoves.add(forwardTwoMove);
                        }
                    }
                }
                if (diagonalMove1 != null) {
                    validMoves.add(diagonalMove1);
                }
                if (diagonalMove2 != null) {
                    validMoves.add(diagonalMove2);
                }
            }

            return validMoves;
        }

    }

    public static class BishopMovement extends ChessMovement {

        @Override
        public Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> validMoves = new HashSet<>();
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1)));
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1)));
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1)));
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1)));
            return validMoves;
        }

    }

    public static class RookMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> validMoves = new HashSet<>();
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()+1)));
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn()-1)));
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()+1, startPosition.getColumn())));
            validMoves.addAll(super.moveToEnd(board, startPosition, new ChessPosition(startPosition.getRow()-1, startPosition.getColumn())));
            return validMoves;
        }
    }

    public static class QueenMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> validMoves = new HashSet<>();
            validMoves.addAll(new BishopMovement().calculateMovements(board, startPosition));
            validMoves.addAll(new RookMovement().calculateMovements(board, startPosition));
            return validMoves;
        }
    }

    public static class KnightMovement extends ChessMovement {

        @Override
        Collection<ChessMove> calculateMovements(ChessBoard board, ChessPosition startPosition) {
            Collection<ChessMove> validMoves = new HashSet<>();
            validMoves.addAll(super.checkNeighbors(board, startPosition, 1, 1));
            validMoves.addAll(super.checkNeighbors(board, startPosition, 1, -1));
            validMoves.addAll(super.checkNeighbors(board, startPosition, -1, 1));
            validMoves.addAll(super.checkNeighbors(board, startPosition, -1, -1));
            return validMoves;
        }
    }
}
