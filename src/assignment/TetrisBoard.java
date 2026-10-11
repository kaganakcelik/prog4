package assignment;

import javax.swing.text.Position;
import java.awt.*;

import static assignment.Board.Action.COUNTERCLOCKWISE;

/**
 * Represents a Tetris board -- essentially a 2-d grid of piece types (or nulls). Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {

    // dimensions of board
    int width, height;

    // the grid of placed blocks
    Piece.PieceType[][] board;

    // the current piece (falling piece) and its position
    Piece currentPiece;
    Point currentPosition;

    // widths and heights for respective x/y index
    int[] rowWidths;
    int[] columnHeights;

    // height of tallest column and number of rows cleared
    int maxHeight;
    int rowsCleared;

    // the last action taken and its result
    Action lastAction = Action.NOTHING;
    Result lastResult = Result.NO_PIECE;


    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.width = width;
        this.height = height;

        rowWidths = new int[height];
        columnHeights = new int[width];

        // initialize the board (will be all null at start so empty)
        board = new Piece.PieceType[height][width];
    }

    @Override
    public Result move(Action act) {
        // store the action and rowscleared
        lastAction = act;
        rowsCleared = 0;

        // check to make sure there is a current tetris piece
        if (currentPiece == null) {
            lastResult = Result.NO_PIECE;
            return lastResult;
        }

        // current x,y position of piece before move
        int x = currentPosition.x;
        int y = currentPosition.y;

        //switch statement between all actions
        switch (act) {
            case LEFT:
                // move one to the left if nothing is blocking it
                if (setPosition(currentPiece, new Point(x - 1, y))) lastResult = Result.SUCCESS;
                else lastResult = Result.OUT_BOUNDS;
                break;
            case RIGHT:
                // move one to the right of nothing is blocking it
                if (setPosition(currentPiece, new Point(x + 1, y))) lastResult = Result.SUCCESS;
                else lastResult = Result.OUT_BOUNDS;
                break;
            case DOWN:
                // move down or if something is directly below it place piece
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
            case CLOCKWISE:
                //checks piece type
                //runs the rotation function to try and create rotation
                if (currentPiece.getWidth() == 3) {
                    if (setRotation(currentPiece.clockwisePiece(),
                            new Point(currentPosition.x, currentPosition.y),
                            Piece.NORMAL_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()])) {
                        lastResult = Result.SUCCESS;
                    }
                    else lastResult = Result.OUT_BOUNDS;
                }
                else if (currentPiece.getWidth() == 4) {
                    // the I piece has its own wall kick table
                    if (setRotation(currentPiece.clockwisePiece(),
                            new Point(currentPosition.x, currentPosition.y),
                            Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()])) {
                        lastResult = Result.SUCCESS;
                    }
                    else lastResult = Result.OUT_BOUNDS;
                }
                else {
                    // O piece doesn't change shape when rotated so no wall kicks needed
                    if (setPosition(currentPiece.clockwisePiece(), currentPosition)) lastResult = Result.SUCCESS;
                    else lastResult = Result.OUT_BOUNDS;
                }
                break;
            case COUNTERCLOCKWISE:
                //checks piece type
                //runs the rotation function to try and create rotation
                //same basic logic as clockwise
                if (currentPiece.getWidth() == 3) {
                    if (setRotation(currentPiece.counterclockwisePiece(),
                            new Point(currentPosition.x, currentPosition.y),
                            Piece.NORMAL_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()])) {
                        lastResult = Result.SUCCESS;
                    }
                    else lastResult = Result.OUT_BOUNDS;
                }
                else if (currentPiece.getWidth() == 4) {
                    if (setRotation(currentPiece.counterclockwisePiece(),
                            new Point(currentPosition.x, currentPosition.y),
                            Piece.I_COUNTERCLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()])) {
                        lastResult = Result.SUCCESS;
                    }
                    else lastResult = Result.OUT_BOUNDS;
                }
                else {
                    if (setPosition(currentPiece.counterclockwisePiece(), currentPosition)) lastResult = Result.SUCCESS;
                    else lastResult = Result.OUT_BOUNDS;
                }
                break;
            default:
                lastResult = Result.SUCCESS;
                break;
        }

        return lastResult;
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
        // set the new falling piece and copy the spawn position so it isn't shared
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
    public int getRowsCleared() { return rowsCleared; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getMaxHeight() { return maxHeight; }


    // returns the y of the bottom left of the piece's bounding box if it was dropped straight down at column x
    @Override
    public int dropHeight(Piece piece, int x) {
        // lowest block in each column of piece
        int[] skirt = piece.getSkirt();

        // minimum y
        int y = Integer.MIN_VALUE;

        for (int i = 0; i < skirt.length; i++) {
            if (skirt[i] == Integer.MAX_VALUE) continue;   // empty column in the piece

            // the piece has to sit high enough so that this column's lowest block rests on top of the board's column
            y = Math.max(y, columnHeights[x + i] - skirt[i]);
        }
        return y;
    }

    @Override
    public int getColumnHeight(int x) { return columnHeights[x]; }

    @Override
    public int getRowWidth(int y) { return rowWidths[y]; }

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

        // the piece fits so update piece and position
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
            int x = body[i].x + currentPosition.x;
            int y = body[i].y + currentPosition.y;

            board[y][x] = currentPiece.getType();

            // there's a new block in this row now
            rowWidths[y]++;

            // column only gets longer if this block is above the current top
            columnHeights[x] = Math.max(columnHeights[x], y + 1);

            maxHeight = Math.max(maxHeight, columnHeights[x]);
        }

        // clear any rows this piece may have filled
        clearRows();

        // the current piece is no longer in play (it's been placed)
        currentPiece = null;
    }

    //checks if all Points in the body are valid
    private boolean setRotation(Piece p, Point position, Point[] wallKicks) {
        Point[] body = p.getBody();

        // try every wall kick offset
        for (int i = 0; i < wallKicks.length; i++) {
            boolean checkRotation = true;

            // check every block of the rotated piece with this offset
            for (int j = 0; j < body.length; j++) {

                // define the absolute x and y coordinates of the piece body
                int x = body[j].x + position.x + wallKicks[i].x;
                int y = body[j].y + position.y + wallKicks[i].y;

                // check to make sure the new position isn't out of bounds or where another piece is
                if (x < 0 || x >= width || y < 0 || y >= height) {
                    checkRotation = false;
                    break;
                }
                else if (board[y][x] != null) {
                    checkRotation = false;
                    break;
                }
            }

            // this offset works so apply the rotation and shift position by offset
            if (checkRotation) {
                currentPiece = p;
                currentPosition.x = currentPosition.x + wallKicks[i].x;
                currentPosition.y = currentPosition.y + wallKicks[i].y;
                return true;
            }
        }

        // no offset works so rotation fails
        return false;
    }

    // removes full rows and everything above drops down by the number of full rows below it
    private void clearRows() {
        // store the next row to fill with a kept row (non cleared row)
        int write = 0;

        // scan every row from bottom to top
        for (int read = 0; read < height; read++) {

            // count full rows and skip so it gets cleared
            if (rowWidths[read] == width) {
                rowsCleared++;
                continue;
            }

            // this row isnt full so move into the write slot
            board[write] = board[read];
            rowWidths[write] = rowWidths[read];
            write++;
        }

        // top blocks left over after clearing the rows become the empty rows
        for (; write < height; write++) {
            board[write] = new Piece.PieceType[width];
            rowWidths[write] = 0;
        }

        // recompute column heights because they can go down after a clear
        if (rowsCleared > 0) {
            maxHeight = 0;
            for (int x = 0; x < width; x++) {
                // start at the top of the column
                int y = columnHeights[x] - 1;

                // walk down until a block is hit (or go below the screen)
                while (y >= 0 && board[y][x] == null) y--;

                // new height is one above heighest block
                columnHeights[x] = y + 1;

                //update the tallest column
                maxHeight = Math.max(maxHeight, columnHeights[x]);
            }
        }
    }
}
