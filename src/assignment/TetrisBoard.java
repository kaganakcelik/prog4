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

    int width, height;
    Piece.PieceType[][] board;

    Piece currentPiece;
    Point currentPosition;

    Result lastResult = Result.NO_PIECE;
    Action lastAction = Action.NOTHING;

    //max height of the blocks currently
    int currentHeight = 0;


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

        //switch statement between all actions
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
                    clearRows();
                    lastResult = Result.PLACE;
                }
                break;
            case DROP:
                // keep moving down until blocked, then place
                while (setPosition(currentPiece, new Point(currentPosition.x, currentPosition.y - 1))) { }
                placePiece();
                clearRows();
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
                    if (setRotation(currentPiece.clockwisePiece(),
                            new Point(currentPosition.x, currentPosition.y),
                            Piece.I_CLOCKWISE_WALL_KICKS[currentPiece.getRotationIndex()])) {
                        lastResult = Result.SUCCESS;
                    }
                    else lastResult = Result.OUT_BOUNDS;
                }
                break;
            case COUNTERCLOCKWISE:
                //checks piece type
                //runs the rotation function to try and create rotation
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
    public int getMaxHeight() { return currentHeight; }

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
            currentHeight = Math.max(currentHeight, body[i].y+currentPosition.y + 1);
            board[body[i].y+currentPosition.y][body[i].x+currentPosition.x] = currentPiece.getType();
        }

        // the current piece is no longer in play (it's been placed)
        currentPiece = null;
    }

    //checks if all Points in the body are valid
    //checks and applies wall kicks
    private boolean setRotation(Piece p, Point position, Point[] wallKicks) {
        Point[] body = p.getBody();
        for (int i = 0; i < wallKicks.length; i++) {
            boolean checkRotation = true;
            for (int j = 0; j < body.length && checkRotation; j++) {

                // define the absolute x and y coordinates of the piece body
                int x = body[j].x + position.x + wallKicks[i].x;
                int y = body[j].y + position.y + wallKicks[i].y;

                // check to make sure the new position isn't out of bounds or where another piece is
                if (x < 0 || x >= width || y < 0 || y >= height) {
                    checkRotation = false;
                }
                else if (board[y][x] != null) {
                    checkRotation = false;
                }
            }

            //applies rotations if valid
            if (checkRotation) {
                currentPiece = p;
                currentPosition.x = currentPosition.x + wallKicks[i].x;
                currentPosition.y = currentPosition.y + wallKicks[i].y;
                return true;
            }
        }
        return false;
    }

    //clears rows if row is full
    //creates a duplicate board and only saves the rows that are not full
    private void clearRows() {
        Piece.PieceType[][] newBoard = new Piece.PieceType[height][width];
        int currentRow = 0;

        //loops through either board
        for (int i = 0; i < height ; i++) {
            boolean cleared = true;
            for (int j = 0; j < width && cleared; j++) {
                //checks if space is empty
                if (board[i][j] == null) {
                    cleared = false;
                }
            }

            //if the row have null spaces, then it copies to the new board
            if (!cleared) {
                newBoard[currentRow] = board[i];
                currentRow++;
            }
            else {
                currentHeight--;
            }
        }
        board = newBoard;
    }
}