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
    private ChessBoard board;
    private ChessPosition wKing;
    private ChessPosition bKing;
    private TeamColor currColor;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        wKing = new ChessPosition(1, 5);
        bKing = new ChessPosition(8, 5);
        currColor = TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && Objects.equals(wKing, chessGame.wKing)
                && Objects.equals(bKing, chessGame.bKing) && currColor == chessGame.currColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, wKing, bKing, currColor);
    }


    private void updateKings() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == TeamColor.WHITE) {
                    if (board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING) {
                        wKing = pos;
                    }
                } else if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == TeamColor.BLACK) {
                    if (board.getPiece(pos).getPieceType() == ChessPiece.PieceType.KING) {
                        bKing = pos;
                    }
                }
            }
        }
    }

    private Collection<ChessMove> collectAllMovesColor(TeamColor color) {
        updateKings();
        Collection<ChessMove> moves = board.getPiece(wKing).pieceMoves(board, wKing);
        moves.clear();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j<= 8; j++) {
                ChessPosition pos = new ChessPosition(i , j);
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == color) {
                    moves.addAll(board.getPiece(pos).pieceMoves(board, pos));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> collectValidMovesColor(TeamColor color) {
        updateKings();
        Collection<ChessMove> moves = board.getPiece(wKing).pieceMoves(board, wKing);
        moves.clear();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j<= 8; j++) {
                ChessPosition pos = new ChessPosition(i , j);
                if (board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == color) {
                    moves.addAll(validMoves(pos));
                }
            }
        }
        return moves;
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
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> newMoves = piece.pieceMoves(board, startPosition);
        newMoves.clear();
        for (var move : moves) {
            try {
                tryMove(move);
            } catch (InvalidMoveException e) {
                continue;
            }
            newMoves.add(move);
        }
        return newMoves;
    }


    public void tryMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No Piece there");
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, move.getStartPosition());
        if (!moves.contains(move)) {
            throw new InvalidMoveException("Not a Legal Move for the Piece");
        }
        ChessPiece enemyPiece = board.getPiece(move.getEndPosition());
        board.addPiece(move.getStartPosition(), null);
        if (piece.getTeamColor() == TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getEndPosition().getRow() ==8) {
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.WHITE, move.getPromotionPiece()));
        } else if (piece.getTeamColor() == TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getEndPosition().getRow() ==1) {
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.BLACK, move.getPromotionPiece()));
        } else  {
            board.addPiece(move.getEndPosition(), piece);
        }
        updateKings();
        if (isInCheck(piece.getTeamColor())) {
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), enemyPiece);
            updateKings();
            throw new InvalidMoveException("Move Leaves King in Check");
        }
        board.addPiece(move.getStartPosition(), piece);
        board.addPiece(move.getEndPosition(), enemyPiece);
        updateKings();
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No Piece there");
        }
        if (piece.getTeamColor() != currColor) {
            throw new InvalidMoveException("Not current player's turn");
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, move.getStartPosition());
        if (!moves.contains(move)) {
            throw new InvalidMoveException("Not a Legal Move for the Piece");
        }
        ChessPiece enemyPiece = board.getPiece(move.getEndPosition());
        board.addPiece(move.getStartPosition(), null);
        if (piece.getTeamColor() == TeamColor.WHITE && piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getEndPosition().getRow() ==8) {
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.WHITE, move.getPromotionPiece()));
        } else if (piece.getTeamColor() == TeamColor.BLACK && piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getEndPosition().getRow() ==1) {
            board.addPiece(move.getEndPosition(), new ChessPiece(TeamColor.BLACK, move.getPromotionPiece()));
        } else  {
            board.addPiece(move.getEndPosition(), piece);
        }
        updateKings();
        if (isInCheck(piece.getTeamColor())) {
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), enemyPiece);
            updateKings();
            throw new InvalidMoveException("Move Leaves King in Check");
        }
        if (piece.getTeamColor() == TeamColor.WHITE) {
            currColor = TeamColor.BLACK;
        } else {
            currColor = TeamColor.WHITE;
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> moves;
        if (teamColor == TeamColor.WHITE){
            moves = collectAllMovesColor(TeamColor.BLACK);
        } else {
            moves = collectAllMovesColor(TeamColor.WHITE);
        }
        for (var move : moves) {
            if (teamColor == TeamColor.WHITE){
                if (move.getEndPosition().equals(wKing)) {
                    return true;
                }
            } else {
                if (move.getEndPosition().equals(bKing)) {
                    return true;
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
        Collection<ChessMove> moves = collectValidMovesColor(teamColor);
        if (moves.isEmpty() && isInCheck(teamColor)) {
            return true;
        }
    return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> moves = collectValidMovesColor(teamColor);
        if (moves.isEmpty() && !isInCheck(teamColor)) {
            return true;
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
