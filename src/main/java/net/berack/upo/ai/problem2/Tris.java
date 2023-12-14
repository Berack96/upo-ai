package net.berack.upo.ai.problem2;

import static net.berack.upo.ai.problem2.Tris.Symbol.EMPTY;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Classe che rappresenta il classico gioco del tris, dove per vincere bisogna
 * mettere il carattere che si gioca in fila di tre prima dell'avversario.
 * 
 * @author Berack96
 */
public class Tris implements Iterable<Tris.Symbol> {
    public static final int LENGTH = 3;

    /**
     * Classe di appoggio per la creazione di una lista di possibili coordinate
     * da utilizzare per giocare una mossa.
     */
    public static class Coordinate {
        public final int x;
        public final int y;
        Coordinate(int x, int y) {
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
     * Possibili simboli delle zone
     */
    public static enum Symbol {
        EMPTY,
        VALUE_X,
        VALUE_O
    }

    private Symbol[] tris;
    private Symbol currentTurn = Symbol.VALUE_X;

    /**
     * Crea una nuova istanza del gioco con tutti gli spazi vuoti
     */
    public Tris() {
        this.tris = new Symbol[LENGTH * LENGTH];
        Arrays.fill(tris, Symbol.EMPTY);
    }

    /**
     * Crea una nuova istanza del gioco a partire da quella passata in input
     * @param current l'istanza correte
     * @param coord le coordinate in cui il giocatore vuole giocare la sua mossa
     */
    public Tris(Tris current, Coordinate coord) {
        this.tris = Arrays.copyOf(current.tris, current.tris.length);
        this.currentTurn = current.currentTurn;
        this.play(coord.x, coord.y);
    }

    /**
     * Indica il valore del simbolo che verrà inserito nella successiva mossa giocata,
     * ovvero il simbolo che verrà giocato alla prossima mossa.
     * 
     * @return il simbolo da giocare
     */
    public Symbol getNextPlaySymbol() {
        return this.currentTurn;
    }

    /**
     * Permette di prendere il valore della cella specificata dalle coordinate
     * @param x la coordinata x da cui ricevere il simbolo
     * @param y la coordinata y da cui ricevere il simbolo
     * @return il valore della cella
     */
    public Symbol get(int x, int y) {
        return this.tris[this.index(x, y)];
    }

    /**
     * Permette di far avanzare il gioco, facendo giocare il turno al giocatore corrente nella cella specificata dalle coordinate.
     * Nel caso in cui il simbolo scelto dalle coordinate non risulti EMPTY il metodo lancerà una eccezione.
     * Una volta che si ha un vincitore questo metodo non potrà essere piu chiamato e lancerà una eccezione.
     * 
     * @param x la coordinata x in cui mettere il simbolo
     * @param y la coordinata y in cui mettere il simbolo
     * @throws UnsupportedOperationException nel caso in cui si ha già avuto un vincitore
     */
    public void play(int x, int y) {
        if(this.haveWinner() != Symbol.EMPTY) throw new UnsupportedOperationException("The game has already finished!");
        if(!isPlayAvailable(x, y)) throw new IllegalArgumentException("The state to modify must be Empty!");

        this.tris[this.index(x, y)] = this.currentTurn;
        this.currentTurn = switch(this.currentTurn) {
            case VALUE_X -> Symbol.VALUE_O;
            case VALUE_O -> Symbol.VALUE_X;
            default -> Symbol.EMPTY;
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
        return this.tris[this.index(x, y)] == Symbol.EMPTY;
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
        if(this.haveWinner() != EMPTY) return new Coordinate[0];

        var count = 0;
        for(var i = 0; i < this.tris.length; i++)
            if(this.tris[i] == Symbol.EMPTY)
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
     * Indica se il gioco è finito.
     * Il gioco finisce se si ha un vincitore o se non ci sono più caselle vuote.
     * 
     * @return vero se iol gioco è finito
     */
    public boolean isFinished() {
        if(haveWinner() != EMPTY) return true;

        for(var symbol : this.tris)
            if(symbol == EMPTY)
                return false;
        return true;
    }

    /**
     * Indica se si ha un vincitore e restituisce chi ha vinto.
     * @return EMPTY se non c'è ancora un vincitore, altrimenti restituisci il vincitore
     */
    public Symbol haveWinner() {
        // top left corner -> horizontal and vertical
        var state = this.tris[0];
        if(state != Symbol.EMPTY) {
            if(this.tris[1] == state && this.tris[2] == state) return state;
            if(this.tris[3] == state && this.tris[6] == state) return state;
        }

        // bottom right corner -> horizontal and vertical
        state = this.tris[8];
        if(state != Symbol.EMPTY) {
            if(this.tris[7] == state && this.tris[6] == state) return state;
            if(this.tris[5] == state && this.tris[2] == state) return state;
        }

        // central -> diagonals, horizontal and vertical 
        state = this.tris[4];
        if(state != Symbol.EMPTY) {
            if(this.tris[0] == state && this.tris[8] == state) return state;
            if(this.tris[6] == state && this.tris[2] == state) return state;
            if(this.tris[3] == state && this.tris[5] == state) return state;
            if(this.tris[1] == state && this.tris[7] == state) return state;
        }

        return Symbol.EMPTY;
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
    public String toString() {
        var builder = new StringBuilder();
        for(var y = 0; y < LENGTH; y++) {
            if(y > 0) builder.append("\n-----\n");

            for(var x = 0; x < LENGTH; x++) {
                if(x > 0) builder.append('|');
                builder.append(switch(this.tris[this.index(x, y)]) {
                    case VALUE_O -> 'O';
                    case VALUE_X -> 'X';
                    default -> ' ';
                });
            }
        }

        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().isInstance(this)) return false;
        return Arrays.equals(this.tris, ((Tris) obj).tris);
    }

    @Override
    public Iterator<Symbol> iterator() {
        return new Iterator<Symbol>() {
            int current = 0;
            @Override public boolean hasNext() { return current < tris.length; }
            @Override public Symbol next() { return tris[current++]; }
        };
    }
}
