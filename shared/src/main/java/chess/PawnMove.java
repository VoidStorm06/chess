package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnMove implements MovementRule {
    private ChessBoard board;
    private ChessPosition position;
    private ChessPiece piece;

    public PawnMove(ChessBoard board, ChessPosition position, ChessPiece piece) {
        this.board=board;
        this.position=position;
        this.piece=piece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PawnMove pawnMove = (PawnMove) o;
        return Objects.equals(board, pawnMove.board) && Objects.equals(position, pawnMove.position) && Objects.equals(piece, pawnMove.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, piece);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int row = this.position.getRow();
        int col = this.position.getColumn();
        ChessPosition forward;
        ChessPosition left;
        ChessPosition right;
        if (this.piece.getTeamColor()== ChessGame.TeamColor.WHITE) {
            if (row+1 <= 8) {
                forward = new ChessPosition(row+1, col);
                if(board.getPiece(forward) == null) {
                    if (checkPromotion(forward, this.piece)) {
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.QUEEN));
                    } else {
                        moves.add(new ChessMove(position, forward, null));
                    }
                    if (row==2 && board.getPiece(new ChessPosition(row+2, col)) == null) {
                        moves.add(new ChessMove(position, new ChessPosition(row+2, col), null));
                    }
                }
                if (col-1 >= 1) {
                    left = new ChessPosition(row+1, col-1);
                    if (board.getPiece(left) != null && board.getPiece(left).getTeamColor() != this.piece.getTeamColor()) {
                        if (checkPromotion(left, this.piece)) {
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, left, null));
                        }
                    }
                }
                if (col+1 <= 8) {
                    right = new ChessPosition(row+1, col+1);
                    if (board.getPiece(right) != null && board.getPiece(right).getTeamColor() != this.piece.getTeamColor()) {
                        if (checkPromotion(right, this.piece)) {
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, right, null));
                        }
                    }
                }
            }
        } else {
            if (row-1 >= 1) {
                forward = new ChessPosition(row-1, col);
                if(board.getPiece(forward) == null) {
                    if (checkPromotion(forward, this.piece)) {
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, forward, ChessPiece.PieceType.QUEEN));
                    } else {
                        moves.add(new ChessMove(position, forward, null));
                    }
                    if (row==7 && board.getPiece(new ChessPosition(row-2, col)) == null) {
                        moves.add(new ChessMove(position, new ChessPosition(row-2, col), null));
                    }

                }
                if (col-1 >= 1) {
                    left = new ChessPosition(row-1, col-1);
                    if (board.getPiece(left) != null && board.getPiece(left).getTeamColor() != this.piece.getTeamColor()) {
                        if (checkPromotion(left, this.piece)) {
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, left, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, left, null));
                        }
                    }
                }
                if (col+1 <=8) {
                    right = new ChessPosition(row-1, col+1);
                    if (board.getPiece(right) != null && board.getPiece(right).getTeamColor() != this.piece.getTeamColor()) {
                        if (checkPromotion(right, this.piece)) {
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, right, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, right, null));
                        }
                    }
                }
            }
        }
        return moves;
    }

    public boolean checkPromotion(ChessPosition position, ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (position.getRow() ==8) {
                return true;
            }
            return false;
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (position.getRow() == 1) {
                return true;
            }
            return false;
        }
        return false;

    }
}
