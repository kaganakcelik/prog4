package assignment;

import assignment.Board.Action;

import java.util.ArrayList;

public class TetrisBrain implements Brain {

    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMoves;

    //decides the next move on the board
    public Board.Action nextMove(Board currentBoard) {
        // Fill the our options array with versions of the new Board
        options = new ArrayList<>();
        firstMoves = new ArrayList<>();
        enumerateOptions(currentBoard);

        int best = 0;
        int bestIndex = 0;

        // Check all of the options and get the one with the highest score
        for (int i = 0; i < options.size(); i++) {
            int score = scoreBoard(options.get(i));
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // We want to return the first move on the way to the best Board
        return firstMoves.get(bestIndex);
    }

    //tests all possible positions on the board with the current piece
    private void enumerateOptions(Board currentBoard) {
        final int NUMROTS = 4;
        //considers all rotations for placements
        for (int i = 0; i < NUMROTS && currentBoard != null; i++) {

            // We can always drop our current Piece
            options.add(currentBoard.testMove(Board.Action.DROP));
            if (i == 0) {
                firstMoves.add(Board.Action.DROP);
            }
            else {
                firstMoves.add(Board.Action.CLOCKWISE);
            }

            // Now we'll add all the places to the left we can DROP
            Board left = currentBoard.testMove(Board.Action.LEFT);
            while (left.getLastResult() == Board.Result.SUCCESS) {
                options.add(left.testMove(Board.Action.DROP));
                if (i == 0) {
                    firstMoves.add(Board.Action.LEFT);
                }
                else {
                    firstMoves.add(Board.Action.CLOCKWISE);
                }
                left.move(Board.Action.LEFT);
            }

            // And then the same thing to the right
            Board right = currentBoard.testMove(Board.Action.RIGHT);
            while (right.getLastResult() == Board.Result.SUCCESS) {
                options.add(right.testMove(Board.Action.DROP));
                if (i == 0) {
                    firstMoves.add(Board.Action.RIGHT);
                }
                else {
                    firstMoves.add(Board.Action.CLOCKWISE);
                }
                right.move(Board.Action.RIGHT);
            }

            currentBoard = currentBoard.testMove(Action.CLOCKWISE);

        }
    }

    //scores the position based on gaps, height, and bumpiness
    private int scoreBoard(Board newBoard) {
        return 100 - (1 * countGaps(newBoard)) - (3 * newBoard.getMaxHeight()) - (1 * heightDiff(newBoard));
    }

    //counts the number of spaces with blocks above it
    private int countGaps(Board newBoard) {
        int numGaps = 0;
        for (int x = 0; x < newBoard.getWidth(); x++) {
            for (int y = newBoard.getColumnHeight(x); y >= 0; y--) {
                if (newBoard.getGrid(x,y) == null) {
                    numGaps++;
                }
            }
        }
        return numGaps;
    }

    //finds the difference in heights for adjacent columns
    private int heightDiff(Board newBoard) {
        int diff = 0;
        for (int x = 0; x < newBoard.getWidth() - 1; x++) {
            diff += Math.abs(newBoard.getColumnHeight(x) - newBoard.getColumnHeight(x) - 1);
        }
        return diff;

    }
/*
    private int linesCleared(Board newBoard) {
        return newBoard.getRowsCleared();
    }

    private int heightSum(Board newBoard) {
        int sum = 0;
        for (int x = 0; x < newBoard.getWidth(); x++) {
            sum += newBoard.getColumnHeight(x);
        }
        return sum;
    }
*/
}
