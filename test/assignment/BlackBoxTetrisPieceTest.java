package assignment;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import assignment.Piece.PieceType;

import java.awt.*;
import java.util.*;

/*
 * Any comments and methods here are purely descriptions or suggestions.
 * This is your test file. Feel free to change this as much as you want.
 */

public class BlackBoxTetrisPieceTest {

    @Test
    void testSpawnPiece() {
        Piece t = new TetrisPiece(PieceType.T);
        assertEquals(PieceType.T, t.getType());
        assertEquals(0, t.getRotationIndex());
        assertEquals(3, t.getWidth());
        assertEquals(3, t.getHeight());
    }

    @Test
    void testRotatedBody() {
        // Stick rotated clockwise once should be a vertical line in column 2.
        Piece stick = new TetrisPiece(PieceType.STICK).clockwisePiece();
        Set<Point> expected = Set.of(new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3));
        Set<Point> actual = new HashSet<>(Arrays.asList(stick.getBody()));
        assertEquals(expected, actual);
    }

    @Test
    void testSkirt() {
        Piece t = new TetrisPiece(PieceType.T);
        assertArrayEquals(new int[] {1, 1, 1}, t.getSkirt());

        Piece tRotated = t.clockwisePiece();
        assertArrayEquals(new int[] {Integer.MAX_VALUE, 0, 1}, tRotated.getSkirt());
    }

    @Test
    void testFourRotationsReturnToStart() {
        Piece start = new TetrisPiece(PieceType.T);
        Piece rotated = start.clockwisePiece().clockwisePiece().clockwisePiece().clockwisePiece();
        assertEquals(start, rotated);
        assertEquals(3, start.counterclockwisePiece().getRotationIndex());
    }

    @Test
    void testEquals() {
        Piece a = new TetrisPiece(PieceType.T);
        Piece b = new TetrisPiece(PieceType.T);
        assertEquals(a, b);
        assertNotEquals(a, a.clockwisePiece());
        assertNotEquals(a, new TetrisPiece(PieceType.STICK));
    }

    @Test
    void testSquare() {
        Piece square = new TetrisPiece(PieceType.SQUARE);
        assertEquals(2, square.getWidth());
        assertArrayEquals(new int[] {0, 0}, square.getSkirt());
        assertNotEquals(square, square.clockwisePiece());
    }

    @Test
    void testTwoRotationsBody() {
        // T rotated twice points down.
        Piece t = new TetrisPiece(PieceType.T).clockwisePiece().clockwisePiece();
        Set<Point> expected = Set.of(new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 0));
        assertEquals(expected, new HashSet<>(Arrays.asList(t.getBody())));
    }

    @Test
    void testCounterclockwiseBody() {
        // Left L rotated counterclockwise once.
        Piece l = new TetrisPiece(PieceType.LEFT_L).counterclockwisePiece();
        Set<Point> expected = Set.of(new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(1, 2));
        assertEquals(expected, new HashSet<>(Arrays.asList(l.getBody())));
    }
}
