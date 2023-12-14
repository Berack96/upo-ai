package net.berack.upo.ai.problem2;

import static net.berack.upo.ai.problem2.Tris.Symbol.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestTrisAi {

    @Test
    public void testValue() {

        assertEquals(0, TrisAi.value(VALUE_O, VALUE_O, EMPTY, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_O, EMPTY, VALUE_O, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_O, EMPTY, VALUE_X, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_X, EMPTY, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_X, VALUE_O, EMPTY));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_O, VALUE_X, EMPTY));

        assertEquals(0, TrisAi.value(VALUE_X, VALUE_O, EMPTY, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_X, EMPTY, VALUE_O, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_X, EMPTY, VALUE_X, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_X, EMPTY, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_X, VALUE_O, EMPTY));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_O, VALUE_X, EMPTY));

        assertEquals(0, TrisAi.value(VALUE_O, VALUE_X, VALUE_X, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_X, VALUE_O, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_O, VALUE_X, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_O, VALUE_O, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_O, VALUE_X, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_O, VALUE_X, VALUE_O, VALUE_O));

        assertEquals(0, TrisAi.value(VALUE_X, VALUE_X, VALUE_X, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_X, VALUE_O, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_O, VALUE_X, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_O, VALUE_O, VALUE_X));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_O, VALUE_X, VALUE_O));
        assertEquals(0, TrisAi.value(VALUE_X, VALUE_X, VALUE_O, VALUE_O));

        assertEquals(0, TrisAi.value(VALUE_O, EMPTY, EMPTY, EMPTY));
        assertEquals(0, TrisAi.value(VALUE_X, EMPTY, EMPTY, EMPTY));

        assertEquals(1, TrisAi.value(VALUE_O, VALUE_O, EMPTY, EMPTY));
        assertEquals(1, TrisAi.value(VALUE_O, EMPTY, VALUE_O, EMPTY));
        assertEquals(1, TrisAi.value(VALUE_O, EMPTY, EMPTY, VALUE_O));

        assertEquals(1, TrisAi.value(VALUE_X, VALUE_X, EMPTY, EMPTY));
        assertEquals(1, TrisAi.value(VALUE_X, EMPTY, VALUE_X, EMPTY));
        assertEquals(1, TrisAi.value(VALUE_X, EMPTY, EMPTY, VALUE_X));

        assertEquals(10, TrisAi.value(VALUE_O, EMPTY, VALUE_O, VALUE_O));
        assertEquals(10, TrisAi.value(VALUE_O, VALUE_O, EMPTY, VALUE_O));
        assertEquals(10, TrisAi.value(VALUE_O, VALUE_O, VALUE_O, EMPTY));

        assertEquals(10, TrisAi.value(VALUE_X, EMPTY, VALUE_X, VALUE_X));
        assertEquals(10, TrisAi.value(VALUE_X, VALUE_X, EMPTY, VALUE_X));
        assertEquals(10, TrisAi.value(VALUE_X, VALUE_X, VALUE_X, EMPTY));

        assertEquals(100, TrisAi.value(VALUE_O, VALUE_O, VALUE_O, VALUE_O));
        assertEquals(100, TrisAi.value(VALUE_X, VALUE_X, VALUE_X, VALUE_X));


        assertEquals(-1, TrisAi.value(VALUE_X, VALUE_O, EMPTY, EMPTY));
        assertEquals(-1, TrisAi.value(VALUE_X, EMPTY, VALUE_O, EMPTY));
        assertEquals(-1, TrisAi.value(VALUE_X, EMPTY, EMPTY, VALUE_O));

        assertEquals(-1, TrisAi.value(VALUE_O, VALUE_X, EMPTY, EMPTY));
        assertEquals(-1, TrisAi.value(VALUE_O, EMPTY, VALUE_X, EMPTY));
        assertEquals(-1, TrisAi.value(VALUE_O, EMPTY, EMPTY, VALUE_X));

        assertEquals(-10, TrisAi.value(VALUE_X, EMPTY, VALUE_O, VALUE_O));
        assertEquals(-10, TrisAi.value(VALUE_X, VALUE_O, EMPTY, VALUE_O));
        assertEquals(-10, TrisAi.value(VALUE_X, VALUE_O, VALUE_O, EMPTY));

        assertEquals(-10, TrisAi.value(VALUE_O, EMPTY, VALUE_X, VALUE_X));
        assertEquals(-10, TrisAi.value(VALUE_O, VALUE_X, EMPTY, VALUE_X));
        assertEquals(-10, TrisAi.value(VALUE_O, VALUE_X, VALUE_X, EMPTY));

        assertEquals(-100, TrisAi.value(VALUE_X, VALUE_O, VALUE_O, VALUE_O));
        assertEquals(-100, TrisAi.value(VALUE_O, VALUE_X, VALUE_X, VALUE_X));

    }


    @Test
    public void testGain() {
        var tris = new Tris();
        tris.play(0, 0);
        assertEquals(3, TrisAi.GAIN.apply(tris));

        tris.play(2, 2);
        assertEquals(0, TrisAi.GAIN.apply(tris));

        tris.play(1, 0);
        assertEquals(10, TrisAi.GAIN.apply(tris));

        tris.play(1, 2);
        assertEquals(0, TrisAi.GAIN.apply(tris));

        tris.play(2, 0);
        assertEquals(92, TrisAi.GAIN.apply(tris));
    }


}
