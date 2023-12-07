package net.berack.upo.ai.problem2;

import java.util.Arrays;

public class Tris {
    public static final int LENGTH = 3;

    /**
     * Possibili stati delle zone
     */
    public enum State {
        EMPTY,
        VALUE_X,
        VALUE_O
    }

    private State[] tris = new State[LENGTH * LENGTH];

    /**
     * Crea una nuova istanza del gioco con tutti gli spazi vuoti
     */
    public Tris() {
        Arrays.fill(tris, State.EMPTY);
    }

    public Tris(Tris current, State state, int x, int y) {
        
    }
}
