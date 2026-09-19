package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMove implements MovementRule {
    ChessBoard board;
    ChessPosition position;
    ChessPiece piece;

    public KingMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<ChessMove>() {
        };
        int row = this.position.getRow();
        int col = this.position.getColumn();
        for (int i = -1; i <=1; i++) {
            if (row+i < 1 || row+i > 8) {
                continue;
            }
            for (int j = -1; j <=1; j++) {
                if (col+j < 1 || col+j > 8) {
                    continue;
                }
                ChessPosition pos = new ChessPosition(row+i, col+j);
                if (board.getPiece(pos)==null || board.getPiece(pos).getPieceType() != this.piece.getPieceType()) {
                    moves.add(new ChessMove(position, pos, null));
                }
            }
        }
        return moves;
    }
}
