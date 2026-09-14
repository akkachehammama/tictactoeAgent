import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // =========================
    // Tests du constructeur
    // =========================

    @Test
    void testBoardIsInitialized() {
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        assertNull(board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
    }

    // =========================
    // Tests de restart()
    // =========================

    @Test
    void testRestartClearsWinner() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2); // X gagne

        assertEquals(Player.X, board.getWinner());

        board.restart();

        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
        assertEquals(Player.X, board.getCurrentTurn());
    }

    @Test
    void testRestartStartsNewGame() {
        board.mark(0, 0);
        board.mark(1, 1);

        board.restart();

        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());

        // X doit pouvoir rejouer sur une ancienne case
        board.mark(0, 0);

        assertEquals(Player.O, board.getCurrentTurn());
    }

    // =========================
    // Tests de mark()
    // =========================

    @Test
    void testMarkChangesCurrentTurn() {
        assertEquals(Player.X, board.getCurrentTurn());

        board.mark(0, 0);

        assertEquals(Player.O, board.getCurrentTurn());

        board.mark(1, 0);

        assertEquals(Player.X, board.getCurrentTurn());
    }

    @Test
    void testMarkOccupiedCellDoesNothing() {
        board.mark(0, 0);

        assertEquals(Player.O, board.getCurrentTurn());

        // La case est déjà occupée par X
        board.mark(0, 0);

        // Le joueur ne doit pas changer
        assertEquals(Player.O, board.getCurrentTurn());
    }

    @Test
    void testMarkOutOfBoundsDoesNothing() {
        assertEquals(Player.X, board.getCurrentTurn());

        board.mark(-1, 0);

        assertEquals(Player.X, board.getCurrentTurn());

        board.mark(3, 0);

        assertEquals(Player.X, board.getCurrentTurn());

        board.mark(0, -1);

        assertEquals(Player.X, board.getCurrentTurn());

        board.mark(0, 3);

        assertEquals(Player.X, board.getCurrentTurn());
    }

    // =========================
    // Tests de victoire
    // =========================

    @Test
    void testXWinsOnRow() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
        assertFalse(board.isInProgressMode());
    }

    @Test
    void testXWinsOnColumn() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 0); // X
        board.mark(1, 1); // O
        board.mark(2, 0); // X

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    @Test
    void testXWinsOnMainDiagonal() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 1); // X
        board.mark(0, 2); // O
        board.mark(2, 2); // X

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    @Test
    void testXWinsOnOppositeDiagonal() {
        board.mark(0, 2); // X
        board.mark(0, 0); // O
        board.mark(1, 1); // X
        board.mark(1, 0); // O
        board.mark(2, 0); // X

        assertEquals(Player.X, board.getWinner());
        assertTrue(board.isInFinishedMode());
    } 

    // =========================
    // Tests victoire de O
    // =========================

    @Test
    void testOWinsOnRow() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(2, 0); // X
        board.mark(1, 2); // O

        assertEquals(Player.O, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    @Test
    void testOWinsOnColumn() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 0); // X
        board.mark(1, 1); // O
        board.mark(2, 2); // X
        board.mark(2, 1); // O

        assertEquals(Player.O, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    @Test
    void testOWinsOnMainDiagonal() {
        board.mark(0, 1); // X
        board.mark(0, 0); // O
        board.mark(1, 0); // X
        board.mark(1, 1); // O
        board.mark(2, 0); // X
        board.mark(2, 2); // O

        assertEquals(Player.O, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    @Test
    void testOWinsOnOppositeDiagonal() {
        board.mark(0, 0); // X
        board.mark(0, 2); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(1, 0); // X
        board.mark(2, 0); // O

        assertEquals(Player.O, board.getWinner());
        assertTrue(board.isInFinishedMode());
    }

    // =========================
    // Test partie terminée
    // =========================

    @Test
    void testCannotMarkAfterGameIsFinished() {
        // X gagne
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());

        Player currentTurn = board.getCurrentTurn();

        // Tentative de jouer après la victoire
        board.mark(2, 2);

        // Rien ne doit changer
        assertEquals(currentTurn, board.getCurrentTurn());
        assertEquals(Player.X, board.getWinner());
    }

    // =========================
    // Tests des états
    // =========================

    @Test
    void testGameIsInProgressAtStart() {
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
    }

    @Test
    void testGameIsFinishedAfterWinningMove() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2);

        assertFalse(board.isInProgressMode());
        assertTrue(board.isInFinishedMode());
    }

    // =========================
    // Test setCurrentTurn()
    // =========================

    @Test
    void testSetCurrentTurn() {
        board.setCurrentTurn(Player.O);

        assertEquals(Player.O, board.getCurrentTurn());

        board.mark(0, 0);

        assertEquals(Player.X, board.getCurrentTurn());
    }
}
