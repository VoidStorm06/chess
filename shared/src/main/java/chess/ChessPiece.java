package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.getPieceType() == PieceType.KING) {
            return new KingMove(board, myPosition, this).pieceMoves();
        } else if (this.getPieceType()==PieceType.PAWN) {
            return new PawnMove(board, myPosition, this).pieceMoves();
        } else if (this.getPieceType()==PieceType.QUEEN) {
            return new QueenMove(board, myPosition, this).pieceMoves();
        } else if (this.getPieceType()==PieceType.ROOK) {
            return new  RookMove(board, myPosition, this).pieceMoves();
        } else if (this.getPieceType()==PieceType.BISHOP) {
            return new BishopMove(board, myPosition, this).pieceMoves();
        } else {
            return new KnightMove(board, myPosition, this).pieceMoves();
        }
    }
}
