package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class KnightMove implements MovementRule {
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public KnightMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }



    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = this.position.getRow();
        int col = this.position.getColumn();
        if (row + 2 <= 8 && col + 1 <= 8) {
            ChessPosition pos = new ChessPosition(row + 2, col + 1);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row + 2 <= 8 && col - 1 >= 1) {
            ChessPosition pos = new ChessPosition(row + 2, col - 1);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row - 2 >= 1 && col + 1 <= 8) {
            ChessPosition pos = new ChessPosition(row - 2, col + 1);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row - 2 >= 1 && col - 1 >= 1) {
            ChessPosition pos = new ChessPosition(row - 2, col - 1);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row + 1 <= 8 && col + 2 <= 8) {
            ChessPosition pos = new ChessPosition(row + 1, col + 2);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row - 1 >= 1 && col + 2 <= 8) {
            ChessPosition pos = new ChessPosition(row - 1, col + 2);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row + 1 <= 8 && col - 2 >= 1) {
            ChessPosition pos = new ChessPosition(row + 1, col - 2);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        if (row - 1 >= 1 && col - 2 >= 1) {
            ChessPosition pos = new ChessPosition(row - 1, col - 2);
            if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
            }
        }
        return moves;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KnightMove that = (KnightMove) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.equals(piece, that.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, piece);
    }
}

