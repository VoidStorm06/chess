package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    ChessPosition wKing;
    ChessPosition bKing;
    TeamColor currColor;
    Collection<ChessMove> wMoves;
    Collection<ChessMove> bMoves;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        wKing = new ChessPosition(1, 5);
        bKing = new ChessPosition(8, 5);
        currColor = TeamColor.WHITE;
        wMoves = board.getPiece(wKing).pieceMoves(board, wKing);
        bMoves = board.getPiece(wKing).pieceMoves(board, wKing);;
        updateChessMoves();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && Objects.equals(wKing, chessGame.wKing)
                && Objects.equals(bKing, chessGame.bKing) && currColor == chessGame.currColor
                && Objects.equals(wMoves, chessGame.wMoves) && Objects.equals(bMoves, chessGame.bMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, wKing, bKing, currColor, wMoves, bMoves);
    }

    private void updateChessMoves() {
        wMoves.clear();
        bMoves.clear();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == TeamColor.WHITE) {
                    wMoves.addAll(board.getPiece(pos).pieceMoves(board, pos));
                    if (board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING) {
                        wKing = pos;
                    }
                } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == TeamColor.BLACK) {
                    bMoves.addAll(board.getPiece(pos).pieceMoves(board, pos));
                    if (board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING) {
                        bKing = pos;
                    }
                }
            }
        }
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currColor;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        return piece.pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessMove move;
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() != teamColor) {
                    if (teamColor == TeamColor.WHITE) {
                        move = new ChessMove(pos, wKing, null);
                        if (bMoves.contains(move)) {
                            return true;
                        } else {
                            continue;
                        }
                    } else {
                        move = new ChessMove(pos, bKing, null);
                        if (wMoves.contains(move)) {
                            return true;
                        } else  {
                            continue;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            if (wMoves.isEmpty() && !isInCheck(teamColor)) {
                return true;
            }
        } else {
            if (bMoves.isEmpty() && !isInCheck(teamColor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        updateChessMoves();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
