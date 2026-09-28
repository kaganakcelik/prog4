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
     * 
     * You may freely add additional constructors, but please leave this one - it is used both in
     * the runner code and testing code.
     */
    private PieceType type;
    private int rotationIndex;
    private Point[] body;
    private int[] skirt;
    private int width;
    private int height;


    public TetrisPiece(PieceType type) {
        // TODO: Implement me.
        this.type = type;
        this.body = type.getSpawnBody();
        System.out.println(body);
    }

    private TetrisPiece(PieceType type, int rotationIndex) {
        this.type = type;
        this.rotationIndex = rotationIndex;
    }

    @Override
    public PieceType getType() {
        // TODO: Implement me.
        return type;
    }

    @Override
    public int getRotationIndex() {
        // TODO: Implement me.
        return rotationIndex;
    }

    @Override
    public Piece clockwisePiece() {
        // TODO: Implement me.
        int newRotationIndex = (rotationIndex + 1) % 4;
        return new TetrisPiece(type, newRotationIndex);
    }

    @Override
    public Piece counterclockwisePiece() {
        // TODO: Implement me.
        int newRotationIndex = rotationIndex - 1;
        if (newRotationIndex < 0) newRotationIndex = 3;
        return new TetrisPiece(type, newRotationIndex);
    }

    @Override
    public int getWidth() {
        // TODO: Implement me.
        return width;
    }

    @Override
    public int getHeight() {
        // TODO: Implement me.
        return height;
    }

    @Override
    public Point[] getBody() {
        // TODO: Implement me.
        return body;
    }

    @Override
    public int[] getSkirt() {
        // TODO: Implement me.
        return skirt;
    }

    @Override
    public boolean equals(Object other) {
        // Ignore objects which aren't also tetris pieces.
        if(!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        // TODO: Implement me.

        return false;
    }
}
