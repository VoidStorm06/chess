package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        ChessPiece.PieceType insertPieceType;
        for (int row = 1; row <=8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition temp = new ChessPosition(row, col);
                if (row == 1 || row == 2) {
                    color = ChessGame.TeamColor.WHITE;
                }
                if (row == 8 || row == 7) {
                    color = ChessGame.TeamColor.BLACK;
                }
                if (this.getPiece(temp) != null) {
                    this.addPiece(temp, null);
                }
                if (row==7 || row == 2) {
                    this.addPiece(temp, new ChessPiece(color, ChessPiece.PieceType.PAWN));
                }
                if (row==8 || row == 1) {
                    if(col==1 || col ==8) {
                        this.addPiece(temp, new ChessPiece(color, ChessPiece.PieceType.ROOK));
                    } else if (col ==2 || col ==7) {
                        this.addPiece(temp, new ChessPiece(color, ChessPiece.PieceType.KNIGHT));
                    } else if (col ==3 || col ==6) {
                        this.addPiece(temp, new ChessPiece(color, ChessPiece.PieceType.BISHOP));
                    } else if (col==4) {
                        this.addPiece(temp, new ChessPiece(color, ChessPiece.PieceType.QUEEN));
                    } else {
                        this.addPiece(temp, new ChessPiece(color, ChessPiece.PieceType.KING));
                    }
                }
            }
        }
    }
}
