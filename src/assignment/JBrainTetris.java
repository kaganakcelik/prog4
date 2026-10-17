package assignment;

public class JBrainTetris extends JTetris{
    Brain ai = new TetrisBrain();

    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }

    JBrainTetris() { super(); }

    //whenever the addNewPiece method is run, it runs the moves to place the piece from the ai
    @Override
    public void addNewPiece() {
        super.addNewPiece();
        if (gameOn) {
            while (board.getCurrentPiece() != null) {
                super.tick(ai.nextMove(board));
            }
        }
    }
}
