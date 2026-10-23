package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnMove implements MovementRule {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece piece;

    public PawnMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }

    private Collection<ChessMove> promoteMoves(ChessPosition end) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.add(new ChessMove(position, end, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(position, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(position, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(position, end, ChessPiece.PieceType.QUEEN));
        return moves;
    }

    private boolean checkPromotion(ChessPosition position, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return position.getRow() == 8;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            return position.getRow() == 1;
        }
        return false;
    }

    private boolean checkDoubleMove(ChessPiece piece, ChessPosition doubleMove) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 2) {
            return board.getPiece(doubleMove) == null;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7) {
            return board.getPiece(doubleMove) == null;
        }
        return false;
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = this.position.getRow();
        int col = this.position.getColumn();
        int moveForward = row+1;
        int moveDouble = row+2;
        if (this.piece.getTeamColor()== ChessGame.TeamColor.BLACK) {
            moveForward = row-1;
            moveDouble = row-2;
        }
        ChessPosition forward;
        ChessPosition left;
        ChessPosition right;
        if (moveForward <= 8 && moveForward >= 1) {
            forward = new ChessPosition(moveForward, col);
            if(board.getPiece(forward) == null) {
                if (checkPromotion(forward, this.piece)) {
                    moves.addAll(promoteMoves(forward));
                } else {
                    moves.add(new ChessMove(position, forward, null));
                }
                if (checkDoubleMove(piece, new ChessPosition(moveDouble, col))) {
                    moves.add(new ChessMove(position, new ChessPosition(moveDouble, col), null));
                }
            }
            if (col-1 >= 1) {
                left = new ChessPosition(moveForward, col-1);
                if (board.getPiece(left) != null &&
                        board.getPiece(left).getTeamColor() != this.piece.getTeamColor()) {
                    if (checkPromotion(left, this.piece)) {
                        moves.addAll(promoteMoves(left));
                    } else {
                        moves.add(new ChessMove(position, left, null));
                    }
                }
            }
            if (col+1 <= 8) {
                right = new ChessPosition(moveForward, col+1);
                if (board.getPiece(right) != null &&
                        board.getPiece(right).getTeamColor() != this.piece.getTeamColor()) {
                    if (checkPromotion(right, this.piece)) {
                        moves.addAll(promoteMoves(right));
                    } else {
                        moves.add(new ChessMove(position, right, null));
                    }
                }
            }
        }
        return moves;
    }
}
