package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GenerateBoard {

    private static final int ROW_LENGTH = 8;

    private static final int COL_LENGTH = 8;

    public GenerateBoard() {

    }

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        ChessBoard cb = new ChessBoard();
        cb.resetBoard();
        drawChessBoard(cb, null);
    }

    public static void drawChessBoard(ChessBoard board, String perspective) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        for (int i = 0; i <= ROW_LENGTH + 1; i++) {
            for (int j = 0; j <= COL_LENGTH + 1; j++) {

                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);


                // If first or last row
                if (i == 0 || i == ROW_LENGTH + 1) {
                    out.print(SET_TEXT_ITALIC);
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_WHITE);
                    if (j != 0 && j != COL_LENGTH + 1) {
                        out.print(" ");
                        out.print((char) ('a' + j - 1));
                        out.print(" ");
                    } else {
                        out.print("   ");
                    }
                } else if (j == 0 || j == COL_LENGTH + 1) {
                    out.print(SET_TEXT_ITALIC);
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_WHITE);
                    out.print(" ");
                    out.print(i);
                    out.print(" ");
                } else {
                    if ((i + j) % 2 == 0) {
                        drawSquare(SET_BG_CB_LIGHT_COLOR, piece, out);
                    } else {
                        drawSquare(SET_BG_CB_DARK_COLOR, piece, out);
                    }
                }


            }
            out.print(RESET_BG_COLOR);
            out.print("\n");
        }
    }

    public static void drawSquare(String color, ChessPiece pieceType, PrintStream out) {
        if (pieceType == null) {
            out.print(color);
            out.print("   ");
        } else {
            out.print(color);
            out.print(RESET_TEXT_ITALIC);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print((pieceType.getTeamColor().equals(ChessGame.TeamColor.BLACK) ? SET_TEXT_COLOR_BLACK : SET_TEXT_COLOR_WHITE));
            out.printf(PIECE_SQUARE, pieceType);
        }
    }
}