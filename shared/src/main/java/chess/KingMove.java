package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMove implements MovementRule {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    public KingMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = this.position.getRow();
        int col = this.position.getColumn();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                ChessMove checkMove = collectMoves(row, i, col, j);
                if(checkMove != null) {
                    moves.add(checkMove);
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
