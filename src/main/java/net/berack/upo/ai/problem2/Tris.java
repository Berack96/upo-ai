package net.berack.upo.ai.problem2;

import java.util.Arrays;
import java.util.Objects;

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

    private State[] tris;

    /**
     * Crea una nuova istanza del gioco con tutti gli spazi vuoti
     */
    public Tris() {
        this.tris = new State[LENGTH * LENGTH];
        Arrays.fill(tris, State.EMPTY);
    }

    /**
     * Crea una nuova istanza del gioco a partire da quella passata in input
     * @param current l'istanza correte
     * @param state lo stato che si vuole mettere ad una cella
     * @param x la coordinata x in cui mettere lo stato
     * @param y la coordinata y in cui mettere lo stato
     */
    public Tris(Tris current, State state, int x, int y) {
        Arrays.copyOf(current.tris, current.tris.length);
        this.set(state, x, y);
    }

    /**
     * Permette di prendere il valore della cella specificata dalle coordinate
     * @param x la coordinata x da cui ricevere lo stato
     * @param y la coordinata y da cui ricevere lo stato
     * @return il valore della cella
     */
    public State get(int x, int y) {
        if(x >= LENGTH) throw new IndexOutOfBoundsException();
        return this.tris[y * LENGTH + x];
    }

    /**
     * Permette di mettere lo stato scelto nella cella specificata dalle coordinate.
     * Nel caso in cui lo stato scelto dalle coordinate non risulti EMPTY
     * il metodo lancerà una eccezione.
     * @param state lo stato da impostare
     * @param x la coordinata x in cui mettere lo stato
     * @param y la coordinata y in cui mettere lo stato
     */
    public void set(State state, int x, int y) {
        if(x >= LENGTH) throw new IndexOutOfBoundsException();
        var index = y * LENGTH + x;

        if(this.tris[index] != State.EMPTY)
            throw new IllegalArgumentException();
        this.tris[index] = Objects.requireNonNull(state);
    }
}
