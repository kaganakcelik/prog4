package assignment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import java.awt.*;
import java.util.*;

/*
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

public class TetrisTest {

    // This will run ONCE before all other tests. It can be useful to setup up
    // global variables and anything needed for all of the tests.

    private TetrisBoard board;
    private TetrisPiece stickPiece;
    private TetrisPiece squarePiece;

    //sets up the tetris board before each run
    @BeforeEach
    public void setUp() {
        board = new TetrisBoard(10, 20);
        stickPiece = new TetrisPiece(Piece.PieceType.STICK);
        squarePiece = new TetrisPiece(Piece.PieceType.SQUARE);
    }

    //checks each of the pieces properties are correct
    @Test
    public void testPieceProperties() {
        assertEquals(Piece.PieceType.STICK, stickPiece.getType());
        assertEquals(4, stickPiece.getWidth());
        assertEquals(4, stickPiece.getHeight());
        assertNotNull(stickPiece.getSkirt());
        assertNotNull(stickPiece.getBody());
    }

    //checks that the piece is rotated with the right positions
    @Test
    public void testPieceRotation() {
        Piece clockwise = stickPiece.clockwisePiece();
        assertNotNull(clockwise);
        assertEquals(1, clockwise.getRotationIndex());

        Piece fullRotation = clockwise.clockwisePiece().clockwisePiece().clockwisePiece();
        assertEquals(stickPiece.getRotationIndex(), fullRotation.getRotationIndex());
    }

    //checks the size of the board at default
    @Test
    public void testBoardInitialization() {
        assertEquals(10, board.getWidth());
        assertEquals(20, board.getHeight());
        assertEquals(0, board.getMaxHeight());
        assertNull(board.getCurrentPiece());
    }

    //test various moves with different pieces
    @Test
    public void testBoardNextPieceAndMoves() {
        board.nextPiece(squarePiece, new Point(4, 18));
        assertEquals(squarePiece, board.getCurrentPiece());
        assertNotNull(board.getCurrentPiecePosition());

        // Test moving left
        Board.Result result = board.move(Board.Action.LEFT);
        assertEquals(Board.Result.SUCCESS, result);
        assertEquals(3, board.getCurrentPiecePosition().x);

        // Test moving right
        board.move(Board.Action.RIGHT);
        assertEquals(4, board.getCurrentPiecePosition().x);
    }

    //tests dropping and placing pieces
    @Test
    public void testBoardDropAndPlace() {
        board.nextPiece(squarePiece, new Point(4, 18));
        Board.Result result = board.move(Board.Action.DROP);
        assertEquals(Board.Result.PLACE, result);
        assertNull(board.getCurrentPiece());
        assertTrue(board.getMaxHeight() > 0);
    }

    @Test
    public void testTetrisBrain() {
        TetrisBrain tetrisBrain = new TetrisBrain();
        board.nextPiece(stickPiece, new Point(3, 18));

        Board.Action action = tetrisBrain.nextMove(board);
        assertNotNull(action);
    }
}
