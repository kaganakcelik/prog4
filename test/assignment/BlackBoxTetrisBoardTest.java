package assignment;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import assignment.Piece.PieceType;

import java.awt.*;

/*
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

public class BlackBoxTetrisBoardTest {

    @Test
    void testSomething() {
        Board test = new TetrisBoard(3,3);
    }

    @Test
    void testMovement() {
        Board board = new TetrisBoard(10, 20);

        assertEquals(Board.Result.NO_PIECE, board.move(Board.Action.DOWN));

        board.nextPiece(new TetrisPiece(PieceType.T), new Point(0, 10));
        assertEquals(Board.Result.OUT_BOUNDS, board.move(Board.Action.LEFT));

        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.RIGHT));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.DOWN));
        assertEquals(new Point(1, 9), board.getCurrentPiecePosition());
    }

    @Test
    void testPlacing() {
        Board board = new TetrisBoard(10, 20);

        board.nextPiece(new TetrisPiece(PieceType.T), new Point(0, 10));
        assertEquals(Board.Result.PLACE, board.move(Board.Action.DROP));
        assertNull(board.getCurrentPiece());

        assertEquals(PieceType.T, board.getGrid(0, 0));
        assertEquals(PieceType.T, board.getGrid(1, 1));
        assertEquals(1, board.getColumnHeight(0));
        assertEquals(2, board.getColumnHeight(1));
        assertEquals(3, board.getRowWidth(0));
        assertEquals(2, board.getMaxHeight());
    }

    @Test
    void testClearRows() {
        Board board = new TetrisBoard(4, 8);

        board.nextPiece(new TetrisPiece(PieceType.T), new Point(0, 5));
        board.move(Board.Action.DROP);

        Piece stick = new TetrisPiece(PieceType.STICK).clockwisePiece();
        board.nextPiece(stick, new Point(1, 4));
        board.move(Board.Action.DROP);

        assertEquals(1, board.getRowsCleared());

        assertEquals(PieceType.T, board.getGrid(1, 0));
        assertNull(board.getGrid(0, 0));

        assertEquals(0, board.getColumnHeight(0));
        assertEquals(3, board.getColumnHeight(3));
    }

    @Test
    void testRotation() {
        Board board = new TetrisBoard(10, 20);
        Piece t = new TetrisPiece(PieceType.T);

        board.nextPiece(t, new Point(4, 10));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.CLOCKWISE));
        assertEquals(t.clockwisePiece(), board.getCurrentPiece());
        assertEquals(new Point(4, 10), board.getCurrentPiecePosition());

        board.nextPiece(t.clockwisePiece(), new Point(-1, 10));
        assertEquals(Board.Result.SUCCESS, board.move(Board.Action.CLOCKWISE));
        assertEquals(new Point(0, 10), board.getCurrentPiecePosition());
    }

    @Test
    void testDropHeight() {
        Board board = new TetrisBoard(10, 20);
        Piece t = new TetrisPiece(PieceType.T);

        assertEquals(-1, board.dropHeight(t, 0));

        board.nextPiece(new TetrisPiece(PieceType.SQUARE), new Point(0, 10));
        board.move(Board.Action.DROP);
        assertEquals(1, board.dropHeight(t, 0));
    }

    @Test
    void testEquals() {
        Board a = new TetrisBoard(10, 20);
        Board b = new TetrisBoard(10, 20);
        Piece t = new TetrisPiece(PieceType.T);

        assertEquals(a, b);

        a.nextPiece(t, new Point(4, 10));
        assertNotEquals(a, b);

        b.nextPiece(t, new Point(4, 10));
        assertEquals(a, b);
    }
}
