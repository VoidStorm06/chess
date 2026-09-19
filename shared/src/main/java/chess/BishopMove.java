package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class BishopMove implements MovementRule {

    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public BishopMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }



    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = this.position.getRow();
        int col = this.position.getColumn();
        for (int i = 1; i <= 8; i++) {
            if (row + i > 8 || col + i > 8) {
                break;
            }
            ChessPosition pos = new ChessPosition(row + i, col+i);
            if (board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            } else if (board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
                break;
            } else {
                break;
            }
        }
        for (int i = 1; i <= 8; i++) {
            if (row-i < 1|| col + i > 8) {
                break;
            }
            ChessPosition pos = new ChessPosition(row-i, col + i);
            if (board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            } else if (board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
                break;
            } else {
                break;
            }
        }
        for (int i = 1; i <= 8; i++) {
            if (row - i < 1 || col-i < 1) {
                break;
            }
            ChessPosition pos = new ChessPosition(row - i, col-i);
            if (board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            } else if (board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
                break;
            } else {
                break;
            }
        }
        for (int i = 1; i <= 8; i++) {
            if (col - i < 1 || row+i > 8) {
                break;
            }
            ChessPosition pos = new ChessPosition(row+i, col - i);
            if (board.getPiece(pos) == null) {
                moves.add(new ChessMove(position, pos, null));
            } else if (board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
                moves.add(new ChessMove(position, pos, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }
}
