package assignment;

import javax.swing.text.Position;
import java.awt.*;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    int width, height;
    Piece.PieceType[][] board;

    TetrisPiece currentPiece;
    Point currentPosition;


    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        board = new Piece.PieceType[height][width];
    }

    @Override
    public Result move(Action act) {
        Point newPosition = new Point(currentPosition.x, currentPosition.y-1);
        setPosition(currentPiece, newPosition);

        return Result.SUCCESS;
    }

    @Override
    public Board testMove(Action act) { return null; }

    @Override
    public Piece getCurrentPiece() { return null; }

    @Override
    public Point getCurrentPiecePosition() { return null; }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        currentPiece = new TetrisPiece(p.getType());
        currentPosition = spawnPosition;
        setPosition(p, spawnPosition);
    }

    @Override
    public boolean equals(Object other) { return false; }

    @Override
    public Result getLastResult() { return Result.NO_PIECE; }

    @Override
    public Action getLastAction() { return Action.NOTHING; }

    @Override
    public int getRowsCleared() { return -1; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getMaxHeight() { return -1; }

    @Override
    public int dropHeight(Piece piece, int x) { return -1; }

    @Override
    public int getColumnHeight(int x) { return -1; }

    @Override
    public int getRowWidth(int y) { return -1; }

    @Override
    public Piece.PieceType getGrid(int x, int y) {
        return board[y][x];
    }

    private void setPosition(Piece p, Point position) {
        Point[] body = p.getBody();
        for (int i = 0; i < body.length; i++) {
            board[body[i].y+currentPosition.y][body[i].x+currentPosition.x] = null;
        }
        for (int i = 0; i < body.length; i++) {
            board[body[i].y+position.y][body[i].x+position.x] = p.getType();
        }
        currentPosition = position;
    }

}
