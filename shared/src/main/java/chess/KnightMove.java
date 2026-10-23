package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMove implements MovementRule {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    public KnightMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = this.position.getRow();
        int col = this.position.getColumn();
        for (int i = -2; i <= 2; i = i+2) {
            for(int j = -1; j <= 1; j++) {
                if (j == 0 || i == 0) {
                    continue;
                }
                ChessMove moveCheck = collectMoves(row, i, col, j);
                if(moveCheck != null) {
                    moves.add(moveCheck);
                }
                moveCheck = collectMoves(row, j, col, i);
                if(moveCheck != null) {
                    moves.add(moveCheck);
                }
            }
        }
        return moves;
    }

    private ChessMove collectMoves(int row, int rowChange, int col, int colChange) {
        ChessPosition pos = new ChessPosition(row+rowChange, col+colChange);
        if (pos.getRow() > 8 || pos.getRow() < 1 || pos.getColumn() > 8 || pos.getColumn() < 1) {
            return null;
        }
        if (board.getPiece(pos) == null || board.getPiece(pos).getTeamColor() != this.piece.getTeamColor()) {
            return new ChessMove(position, pos, null);
        }
        return null;
    }
}

