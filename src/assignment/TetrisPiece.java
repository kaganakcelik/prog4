package assignment;

import java.awt.*;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * 
 * All operations on a TetrisPiece should be constant time, except for it's
 * initial construction. This means that rotations should also be fast - calling
 * clockwisePiece() and counterclockwisePiece() should be constant time! You may
 * need to do precomputation in the constructor to make this possible.
 */
public final class TetrisPiece implements Piece {

    /**
     * Construct a tetris piece of the given type. The piece should be in it's spawn orientation,
     * i.e., a rotation index of 0.
     * <p>
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */

    private final int NUMPOINTS = 4;
    private final int NUMROTATIONS = 4;

    private PieceType type;
    private int rotationIndex;
    private Point[] body;
    private int[] skirt;
    private int width;
    private int height;

    private TetrisPiece clockwise;
    private TetrisPiece counterClockwise;

    public TetrisPiece(PieceType type) {
        // make this piece rotation 0 using type's spawn shape
        this(type, 0, type.getSpawnBody());

        // go around the circular linked-list ring clockwise and and and call build
        Piece current = this;
        for (int i = 0; i < NUMROTATIONS; i++) {
            current = current.clockwisePiece();
        }
    }

    // builds a single rotation from a body that's already been rotated
    private TetrisPiece(PieceType type, int rotationIndex, Point[] body) {
        this.type = type;
        this.rotationIndex = rotationIndex;
        this.width = type.getBoundingBox().width;
        this.height = type.getBoundingBox().height;
        this.body = new Point[NUMPOINTS];

        for (int i = 0; i < NUMPOINTS; i++) {
            this.body[i] = new Point(body[i].x, body[i].y);
        }

        this.skirt = findSkirt();
    }

    @Override
    public PieceType getType() {
        return type;
    }

    @Override
    public int getRotationIndex() {
        return rotationIndex;
    }


    // returns this piece rotated 90 degrees clockwise
    @Override
    public Piece clockwisePiece() {
        if (clockwise == null) {
            if (rotationIndex == NUMROTATIONS - 1) {
                clockwise = counterClockwise.counterClockwise.counterClockwise;
            } else {
                Point[] rotated = new Point[NUMPOINTS];
                for (int i = 0; i < NUMPOINTS; i++) {
                    rotated[i] = new Point(body[i].y, (width - 1) - body[i].x);
                }
                clockwise = new TetrisPiece(type, rotationIndex + 1, rotated);
            }
            clockwise.counterClockwise = this;
        }
        return clockwise;
    }

    // returns this piece rotated 90 degrees counterclockwise (already computed in the clockwisePiece method)
    @Override
    public Piece counterclockwisePiece() {
        return counterClockwise;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Point[] getBody() {
        return body;
    }

    @Override
    public int[] getSkirt() {
        return skirt;
    }

    // two pieces are equal when they're the same type and rotation
    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if (!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        return type == otherPiece.type && rotationIndex == otherPiece.rotationIndex;
    }

    // computes the skirt
    private int[] findSkirt() {
        int[] newSkirt = new int[type.getBoundingBox().width];
        for (int i = 0; i < newSkirt.length; i++) {
            newSkirt[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < NUMPOINTS; i++) {
            int x = body[i].x;
            int minSkirt = body[i].y;
            newSkirt[x] = Math.min(newSkirt[x], minSkirt);
        }
        return newSkirt;
    }
}

