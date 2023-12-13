package net.berack.upo.ai.problem2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Classe che rappresenta il classico gioco del tris, dove per vincere bisogna
 * mettere il carattere che si gioca in fila di tre prima dell'avversario.
 * 
 * @author Berack96
 */
public class Tris implements Iterable<Tris.State> {
    public static final int LENGTH = 3;

    /**
     * Classe di appoggio per la creazione di una lista di possibili coordinate
     * da utilizzare per giocare una mossa.
     */
    public static class Coordinate {
        public final int x;
        public final int y;
        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override public boolean equals(Object obj) {
            if(!(obj instanceof Coordinate)) return false;
            var curr = (Coordinate) obj;
            return this.x == curr.x && this.y == curr.y;
        }
    }

    /**
     * Possibili stati delle zone
     */
    public static enum State {
        EMPTY,
        VALUE_X,
        VALUE_O
    }

    private State[] tris;
    private State currentTurn = State.VALUE_X;

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
     * @param coord le coordinate in cui il giocatore vuole giocare la sua mossa
     */
    public Tris(Tris current, Coordinate coord) {
        Arrays.copyOf(current.tris, current.tris.length);
        this.currentTurn = current.currentTurn;
        this.play(coord.x, coord.y);
    }

    /**
     * Permette di prendere il valore della cella specificata dalle coordinate
     * @param x la coordinata x da cui ricevere lo stato
     * @param y la coordinata y da cui ricevere lo stato
     * @return il valore della cella
     */
    public State get(int x, int y) {
        return this.tris[this.index(x, y)];
    }

    /**
     * Permette di far avanzare il gioco, facendo giocare il turno al giocatore corrente nella cella specificata dalle coordinate.
     * Nel caso in cui lo stato scelto dalle coordinate non risulti EMPTY il metodo lancerà una eccezione.
     * Una volta che si ha un vincitore questo metodo non potrà essere piu chiamato e lancerà una eccezione.
     * 
     * @param x la coordinata x in cui mettere lo stato
     * @param y la coordinata y in cui mettere lo stato
     * @throws UnsupportedOperationException nel caso in cui si ha già avuto un vincitore
     */
    public void play(int x, int y) {
        if(this.haveWinner() != State.EMPTY) throw new UnsupportedOperationException("The game has already finished!");
        if(!isPlayAvailable(x, y)) throw new IllegalArgumentException("The state to modify must be Empty!");

        this.tris[this.index(x, y)] = this.currentTurn;
        this.currentTurn = switch(this.currentTurn) {
            case VALUE_X -> State.VALUE_O;
            case VALUE_O -> State.VALUE_X;
            default -> State.EMPTY;
        };
    }

    /**
     * Indica se è possibile giocare nella cella indicata.
     * Questo metodo non controlla che il gioco sia finito, ma controlla solamente se la mossa
     * indicata nei parametri è legale.
     * 
     * @param x la coordinata X
     * @param y la coordinata Y
     * @return vero se la mossa può essere giocata, altrimenti falso
     */
    public boolean isPlayAvailable(int x, int y) {
        return this.tris[this.index(x, y)] == State.EMPTY;
    }

    /**
     * Permette di avere una lista di tutte le coordinate per le mosse possibili.
     * Le coordinate sono incapsulate dentro un oggetto Coordintate in cui l'elemento 
     * X corrsiponde alla coordinata X, mentre l'elemento Y corrisponde alla coordianta Y.
     * Nel caso in cui il gioco sia completato, la lista risultante sarà vuota.
     * 
     * @return un array di coordinate disponibili per giocare.
     */
    public Coordinate[] availablePlays() {
        var count = 0;
        for(var i = 0; i < this.tris.length; i++)
            if(this.tris[i] == State.EMPTY)
                count += 1;

        var res = new Coordinate[count];
        count = 0;

        for(var y = 0; y < LENGTH; y++)
            for(var x = 0; x < LENGTH; x++)
                if(isPlayAvailable(x, y))
                    res[count++] = new Coordinate(x, y);

        return res;
    }

    /**
     * Indica se si ha un vincitore e restituisce chi ha vinto.
     * @return EMPTY se non c'è ancora un vincitore, altrimenti restituisci il vincitore
     */
    public State haveWinner() {
        // top left corner -> horizontal and vertical
        var state = this.tris[0];
        if(state != State.EMPTY) {
            if(this.tris[1] == state && this.tris[2] == state) return state;
            if(this.tris[3] == state && this.tris[6] == state) return state;
        }

        // bottom right corner -> horizontal and vertical
        state = this.tris[8];
        if(state != State.EMPTY) {
            if(this.tris[7] == state && this.tris[6] == state) return state;
            if(this.tris[5] == state && this.tris[2] == state) return state;
        }

        // central -> diagonals, horizontal and vertical 
        state = this.tris[4];
        if(state != State.EMPTY) {
            if(this.tris[0] == state && this.tris[8] == state) return state;
            if(this.tris[6] == state && this.tris[2] == state) return state;
            if(this.tris[3] == state && this.tris[5] == state) return state;
            if(this.tris[1] == state && this.tris[7] == state) return state;
        }

        return State.EMPTY;
    }

    /**
     * Calcola l'indice trasformato da 2D a 1D.
     * Questo metodo serve internamente per la classe.
     * Nel caso in cui si inseriscano delle coordinate non valide lancia una eccezione.
     * 
     * @param x il valore della coordinata X
     * @param y il valore della coordinata Y
     * @return il valore risultante
     */
    private int index(int x, int y) {
        if(x >= LENGTH || y >= LENGTH) throw new IndexOutOfBoundsException();
        return x + y * LENGTH;
    }

    @Override
    public Iterator<State> iterator() {
        return new Iterator<State>() {
            int current = 0;
            @Override public boolean hasNext() { return current < tris.length; }
            @Override public State next() { return tris[current++]; }
        };
    }
}
