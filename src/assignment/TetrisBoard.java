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

    Piece currentPiece;
    Point currentPosition;

    Result lastResult = Result.NO_PIECE;
    Action lastAction = Action.NOTHING;


    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;
        board = new Piece.PieceType[height][width];
    }

    @Override
    public Result move(Action act) {
        lastAction = act;

        // check to make sure there is a current tetris piece
        if (currentPiece == null) {
            lastResult = Result.NO_PIECE;
            return lastResult;
        }

        int x = currentPosition.x;
        int y = currentPosition.y;

        switch (act) {
            case LEFT:
                if (setPosition(currentPiece, new Point(x - 1, y))) lastResult = Result.SUCCESS;
                else lastResult = Result.OUT_BOUNDS;
                break;
            case RIGHT:
                if (setPosition(currentPiece, new Point(x + 1, y))) lastResult = Result.SUCCESS;
                else lastResult = Result.OUT_BOUNDS;
                break;
            case DOWN:
                if (setPosition(currentPiece, new Point(x, y - 1))) lastResult = Result.SUCCESS;
                else {
                    placePiece();
                    lastResult = Result.PLACE;
                }
                break;
            case DROP:
                // keep moving down until blocked, then place
                while (setPosition(currentPiece, new Point(currentPosition.x, currentPosition.y - 1))) { }
                placePiece();
                lastResult = Result.PLACE;
                break;
            default:
                lastResult = Result.SUCCESS;
                break;
        }

        return lastResult;

        // determine the new position (tetris piece is always falling down)
//        Point newPosition = new Point(currentPosition.x, currentPosition.y-1);
//
//        if (setPosition(currentPiece, newPosition)) {
//            // return success if setPosition is successful
//            return Result.SUCCESS;
//        }
//
//        // write the piece to the board if success is no longer being called
//        placePiece();
//
//        // return the fact that we just placed a piece as the new result status
//        return Result.PLACE;
    }

    @Override
    public Board testMove(Action act) { return null; }

    @Override
    public Piece getCurrentPiece() { return currentPiece; }

    @Override
    public Point getCurrentPiecePosition() {
        // return null if there isn't a currentPiece
        if (currentPosition == null) return null;

        // return the currentPiece position
        return new Point(currentPosition);
    }

    @Override
    public void nextPiece(Piece p, Point spawnPosition) {
        currentPiece = p;
        currentPosition = new Point(spawnPosition);
    }

    @Override
    public boolean equals(Object other) { return false; }

    @Override
    public Result getLastResult() { return lastResult; }

    @Override
    public Action getLastAction() { return lastAction; }

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
        // return null for out of bounds coordinates
        if (x < 0 || x >= width || y < 0 || y >= height) return null;

        // return the PieceType at coordinate x,y
        return board[y][x];
    }

    // moves the falling piece if it can (nothing blocking)
    private boolean setPosition(Piece p, Point position) {
        Point[] body = p.getBody();

        for (int i = 0; i < body.length; i++) {
            // define the absolute x and y coordinates of the piece body
            int x = body[i].x + position.x;
            int y = body[i].y + position.y;

            // check to make sure the new position isn't out of bounds or where another piece is
            if (x < 0 || x >= width || y < 0 || y >= height) return false;
            if (board[y][x] != null) return false;
        }

        currentPiece = p;
        currentPosition = new Point(position);
        return true;
    }

    // writes the current piece to the board once it lands
    private void placePiece() {
        // get the body of the current piece
        Point[] body = currentPiece.getBody();

        // write the body to the board
        for (int i = 0; i < body.length; i++) {
            board[body[i].y+currentPosition.y][body[i].x+currentPosition.x] = currentPiece.getType();
        }

        // the current piece is no longer in play (it's been placed)
        currentPiece = null;
    }

}
