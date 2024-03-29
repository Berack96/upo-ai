package net.berack.upo.ai.problem2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import static net.berack.upo.ai.problem2.Tris.Symbol.*;

import org.junit.jupiter.api.Test;


public class TestTris {

    @Test
    public void testConstructor() {
        var tris = new Tris();
        for(var state : tris) assertEquals(EMPTY, state);
    }

    @Test
    public void testContructor2() {
        var tris = new Tris();
        tris.play(0, 0);

        var move = tris.availablePlays()[0];
        var second = new Tris(tris, move);

        assertFalse(second.isPlayAvailable(move.x, move.y));
        assertEquals(VALUE_O, second.get(move.x, move.y));
    }

    @Test
    public void testPlay() {
        var tris = new Tris();
        tris.play(0, 0);

        var i = 0;
        var states = new Tris.Symbol[] {VALUE_X, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};
        for(var state : tris) assertEquals(states[i++], state);

        tris.play(2, 2);
        states[8] = VALUE_O;
        i = 0;
        for(var state : tris) assertEquals(states[i++], state);

        tris.play(1, 1);
        states[4] = VALUE_X;
        i = 0;
        for(var state : tris) assertEquals(states[i++], state);

        tris.play(0, 1);
        states[3] = VALUE_O;
        i = 0;
        for(var state : tris) assertEquals(states[i++], state);

        tris.play(1, 0);
        states[1] = VALUE_X;
        i = 0;
        for(var state : tris) assertEquals(states[i++], state);

        tris.play(0, 2);
        states[6] = VALUE_O;
        i = 0;
        for(var state : tris) assertEquals(states[i++], state);
    }

    @Test
    public void testAvailableActions() {
        var tris = new Tris();
        var actions = tris.availablePlays();

        assertEquals(9, actions.length);
        for(var i = 0; i < actions.length -2; i++) {

            for(var j = 0; j < actions.length; j++) {
                var curr = actions[j];
                assertEquals(j >= i, tris.isPlayAvailable(curr.x, curr.y));
            }

            var array = Arrays.copyOfRange(actions, i, actions.length);
            var avail = tris.availablePlays();
            assertArrayEquals(array, avail, "Array differ at iteration " + i);

            var curr = actions[i];
            tris.play(curr.x, curr.y);
        }

        assertEquals(0, tris.availablePlays().length, "The game is over, there aren't any moves left");
    }

    @Test
    public void testPlayException() {
        var tris = new Tris();
        tris.play(0, 0);

        assertThrows(IllegalArgumentException.class, () -> tris.play(0, 0));

        tris.play(1, 1);
        assertThrows(IllegalArgumentException.class, () -> tris.play(0, 0));
        assertThrows(IllegalArgumentException.class, () -> tris.play(1, 1));

        tris.play(0, 2);
        assertThrows(IllegalArgumentException.class, () -> tris.play(0, 0));
        assertThrows(IllegalArgumentException.class, () -> tris.play(1, 1));
        assertThrows(IllegalArgumentException.class, () -> tris.play(0, 2));

        tris.play(2, 1);
        assertThrows(IllegalArgumentException.class, () -> tris.play(0, 0));
        assertThrows(IllegalArgumentException.class, () -> tris.play(1, 1));
        assertThrows(IllegalArgumentException.class, () -> tris.play(0, 2));
        assertThrows(IllegalArgumentException.class, () -> tris.play(2, 1));

        tris.play(0, 1);
        for(var i = 0; i < Tris.LENGTH; i++)
            for(var j = 0; j < Tris.LENGTH; j++) {
                final var x = i;
                final var y = j;
                assertThrows(UnsupportedOperationException.class, () -> tris.play(x, y));
            }
    }

    @Test
    public void testWinner() {

        // horizontal 1 line X
        var tris = new Tris();
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,2);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,0);
        assertEquals(VALUE_X, tris.getWinner());
        assertTrue(tris.isFinished());

        var trisPos = new ArrayList<int[]>();
        trisPos.add(new int[] {0, 0});
        trisPos.add(new int[] {1, 0});
        trisPos.add(new int[] {2, 0});

        var trisCoord = tris.getWinnerTris();
        trisCoord.sort((a, b) -> Arrays.compare(a, b));
        assertEquals(trisPos.size(), trisCoord.size());
        for(var i = 0; i < trisPos.size(); i++)
            assertArrayEquals(trisPos.get(i), trisCoord.get(i));


        // diagonal \ O
        tris = new Tris();
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,2);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,2);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,0);
        assertEquals(VALUE_O, tris.getWinner());
        assertTrue(tris.isFinished());

        trisPos = new ArrayList<int[]>();
        trisPos.add(new int[] {0, 0});
        trisPos.add(new int[] {1, 1});
        trisPos.add(new int[] {2, 2});

        trisCoord = tris.getWinnerTris();
        trisCoord.sort((a, b) -> Arrays.compare(a, b));
        assertEquals(trisPos.size(), trisCoord.size());
        for(var i = 0; i < trisPos.size(); i++)
            assertArrayEquals(trisPos.get(i), trisCoord.get(i));


        // vertical 2 column X
        tris = new Tris();
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,2);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,2);
        assertEquals(VALUE_X, tris.getWinner());
        assertTrue(tris.isFinished());

        trisPos = new ArrayList<int[]>();
        trisPos.add(new int[] {1, 0});
        trisPos.add(new int[] {1, 1});
        trisPos.add(new int[] {1, 2});

        trisCoord = tris.getWinnerTris();
        trisCoord.sort((a, b) -> Arrays.compare(a, b));
        assertEquals(trisPos.size(), trisCoord.size());
        for(var i = 0; i < trisPos.size(); i++)
            assertArrayEquals(trisPos.get(i), trisCoord.get(i));


        // No winner
        tris = new Tris();
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,0);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(0,2);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,1);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(2,2);
        assertEquals(EMPTY, tris.getWinner());
        assertFalse(tris.isFinished());
        tris.play(1,2);
        assertEquals(EMPTY, tris.getWinner());
        assertTrue(tris.isFinished());
        assertNull(tris.getWinnerTris());
    }

    @Test
    public void testWinner2() {
        var tris = new Tris();

        tris.play(1,1);
        assertFalse(tris.isFinished());
        tris.play(0,0);
        assertFalse(tris.isFinished());
        tris.play(0,1);
        assertFalse(tris.isFinished());
        tris.play(2,1);
        assertFalse(tris.isFinished());
        tris.play(0,2);
        assertFalse(tris.isFinished());
        tris.play(2,0);
        assertFalse(tris.isFinished());
        tris.play(1,0);
        assertFalse(tris.isFinished());
        tris.play(2,2);
        assertTrue(tris.isFinished());
    }
}
