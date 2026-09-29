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
    private PieceType type;
    private int rotationIndex;
    private Point[] body = new Point[4];
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
        for (int i = 0; i < rotationIndex; i++) {
            for (int j = 0; j < NUMPOINTS; j++) {
                int spawnX = type.getSpawnBody()[i].x;
                int spawnY = type.getSpawnBody()[i].y;
                int boxHeight = type.getBoundingBox().height;
                body[i] = new Point(spawnY, (boxHeight - 1) - spawnX);
            }
        }
        this.skirt = findSkirt();
        //what is n in O(n) because everything should be in constant time
        //how should we generate all rotations of a type?
        //what is width and height here
        //this.width =
        //this.height =

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
        if (!(other instanceof TetrisPiece)) return false;
        TetrisPiece otherPiece = (TetrisPiece) other;

        // TODO: Implement me.

        return false;
    }


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

