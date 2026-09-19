package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class QueenMove implements MovementRule {
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public QueenMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueenMove queenMove = (QueenMove) o;
        return Objects.equals(board, queenMove.board) && Objects.equals(position, queenMove.position) && Objects.equals(piece, queenMove.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        RookMove rook = new RookMove(board, position, piece);
        BishopMove bishop = new BishopMove(board, position, piece);
        Collection<ChessMove> moves = rook.pieceMoves();
        Collection<ChessMove> moves2 = bishop.pieceMoves();
        moves.addAll(moves2);
        return moves;

    }
}
