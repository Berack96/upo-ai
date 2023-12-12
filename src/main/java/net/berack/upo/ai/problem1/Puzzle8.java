package net.berack.upo.ai.problem1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Classe utilizzata per la rappresentazione del problema delle 8 tessere.
 * In essa ci sono dei metodi base per il modello del problema tra cui getter e setter.
 * Oltre a questi ci sono dei metodi per la risoluzione del problema sia manuale,
 * che automatico tramite l'algoritmo A*
 * 
 * @author Berack96
 */
public class Puzzle8 implements Iterable<Integer> {
    public static final int LENGTH = 3;

    /**
     * Possibili movimenti della tessera "vuota"
     */
    public enum Move {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private int[] puzzle;
    private int blank = 0;

    /**
     * Genera una nuova istanza del problema con le tessere posizionate in modo casuale.
     */
    public Puzzle8() {
        var rand = new Random();
        this.puzzle = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};

        for(var i = this.puzzle.length - 1; i > 0; i--) {
            var j = rand.nextInt(i + 1);

            var temp = this.puzzle[i];
            this.puzzle[i] = this.puzzle[j];
            this.puzzle[j] = temp;

            if(this.puzzle[i] == 0) this.blank = i;
        }
    }

    /**
     * Genera una nuova istanza del problema con le tessere posizionate come il
     * problema passato in input e dopo aver eseguito la mossa indicata.
     * 
     * @param original il puzzle di partenza che si vuole copiare
     * @param move la mossa da eseguire
     * 
     * @throws UnsupportedOperationException nel caso la mossa non sia disponibile.
     */
    public Puzzle8(Puzzle8 original, Move move) {
        this.puzzle = Arrays.copyOf(original.puzzle, this.puzzle.length);
        this.blank = original.blank;

        this.move(move);
    }

    /**
     * Inizializza il problema con gli interi passati.
     * L'array inserito deve essere di 9 valori dato che la matrice è di 3x3.
     * I valori saranno posizionati a tre a tre nella matrice, quindi i primi 3 saranno nella
     * prima riga, i secondi tre nella seconda e i terzi saranno nella terza riga.
     * Nel caso in cui vengano passati meno valori una eccezione verrà sollevata.
     * Gli elementi inseriti devono essere UNICI e compresi tra 0 e 8 estremi inclusi.
     * 
     * @param values i valori a cui inizializzare il problema.
     * @throws IllegalArgumentException nel caso in cui 
     */
    public Puzzle8(int...values) {
        if(values == null || values.length != LENGTH*LENGTH)
            throw new IllegalArgumentException("The size of the array must be " + LENGTH*LENGTH);

        var check = new int[LENGTH * LENGTH];
        this.puzzle = Arrays.copyOf(values, values.length);

        for(var i = 0; i < this.puzzle.length; i++) {
            var curr = this.puzzle[i];
            var ok = true;

            ok = (curr >= 0 && curr < check.length);
            if(ok) {
                if(curr == 0) this.blank = i;

                check[curr] += 1;
                ok = (check[curr] == 1);
            }

            if(!ok) throw new IllegalArgumentException("The input values must be UNIQUE integers between 0 and " + (LENGTH*LENGTH -1) + " inclusive");
        }
    }


    /**
     * Indica la grandezza della tavoletta che contiene le tessere.
     * Essa è una matrice 3x3.
     * 
     * @see #LENGTH
     * @return la grandezza del lato della matrice
     */
    public int size() {
        return LENGTH;
    }

    /**
     * Permette di ricevere il valore della tessera posizionata nella zona richiesta.
     * La zona è indicata come una matrice 3x3 dove la prima zona si trova nelle coordinate
     * 0x0 e l'ultima nella zona 2x2.
     * Il valore varia da 1 a 8 ed il valore della tessera "vuota" equivale a 0.
     * 
     * @param x la prima coordinata, i possibili valori sono 0, 1, 2
     * @param y la seconda coordinata, i possibili valori sono 0, 1, 2
     * @return il valore della tessera nelle coordinate selezionate
     */
    public int get(int x, int y) {
        if(x >= LENGTH) throw new IndexOutOfBoundsException();
        return puzzle[y * LENGTH + x];
    }

    /**
     * Genera un array di mosse disponibili a partire dalla configurazione corrente.
     * Questo metodo esiste dato che non tutte le mosse sono disponibili in tutti i casi.
     * per esempio se mi trovo nell'angolo in basso a dx potrò fare solo
     * le mosse UP e LEFT.
     * 
     * @return un array di mosse disponibili
     */
    public Move[] availableMoves() {
        return switch(this.blank) {
            case 0 -> new Move[] { Move.DOWN, Move.RIGHT };
            case 1 -> new Move[] { Move.DOWN, Move.LEFT, Move.RIGHT };
            case 2 -> new Move[] { Move.DOWN, Move.LEFT };
            case 3 -> new Move[] { Move.UP, Move.DOWN, Move.RIGHT };
            case 4 -> new Move[] { Move.UP, Move.DOWN, Move.LEFT, Move.RIGHT };
            case 5 -> new Move[] { Move.UP, Move.DOWN, Move.LEFT };
            case 6 -> new Move[] { Move.UP, Move.RIGHT };
            case 7 -> new Move[] { Move.UP, Move.LEFT, Move.RIGHT };
            case 8 -> new Move[] { Move.UP, Move.LEFT };
            default -> new Move[] {};
        };
    }

    /**
     * Indica se la mossa inserita sia una mossa valida, dato che non tutte le mosse sono disponibili in tutti i casi.
     * per esempio se mi trovo nell'angolo in basso a dx potrò fare solo le mosse UP e LEFT.
     * @param move la mossa da testare
     * @return VERO se la mossa è valida altrimenti falso
     */
    public boolean isValidMove(Move move) {
        return switch(move) {
            case UP -> this.blank >= LENGTH;
            case DOWN -> this.blank <= 2*LENGTH - 1;
            case LEFT -> this.blank % LENGTH != 0;
            case RIGHT -> this.blank % LENGTH !=  LENGTH - 1;
        };
    }

    /**
     * Muove la tessera "vuota" in una delle direzioni indicate.
     * Essa viene scambiata con una delle tessere adiacenti.
     * 
     * Nota: non tutte le mosse sono disponibili in tutti i casi.
     * per esempio se mi trovo nell'angolo in basso a dx potrò fare solo
     * le mosse UP e LEFT.
     * 
     * @see #availableMoves()
     * @see #isValidMove(Move)
     * @see #solve()
     * 
     * @param move
     * @throws UnsupportedOperationException nel caso in cui la mossa inserita non sia valida
     */
    public void move(Move move) {
        if(!this.isValidMove(move))
            throw new UnsupportedOperationException("Move not available");

        var other = switch(move) {
            case UP -> this.blank - LENGTH;
            case DOWN -> this.blank + LENGTH;
            case LEFT -> this.blank - 1;
            case RIGHT -> this.blank + 1;
        };

        this.puzzle[this.blank] = this.puzzle[other];
        this.puzzle[other] = 0;
        this.blank = other;
    }

    /**
     * Usa l'algoritmo A* per la soluzione del problema
     * @param goal lo stato goal in cui arrivare
     * @return una sequenza di mosse per poter risolvere il problema o null se non risolvibile
     */
    public List<Move> solve(Puzzle8 goal) {
        var aStar = new AStar<Puzzle8, Move>(Puzzle8::availableMoves, (p, m) -> new Puzzle8(p, m))
            .setHeuristic((p1, p2) -> {
                var sum = 0;
                var n = p1.puzzle.length;
                var index = new int[n];

                for(var i = 0; i < n; i++) index[p1.puzzle[i]] = i;
                for(var i = 0; i < n; i++) {
                    var curr = p2.puzzle[i];
                    var i2 = index[curr];

                    sum += Math.abs((i / LENGTH) - (i2 / LENGTH)) + Math.abs((i % LENGTH) - (i2 % LENGTH));
                }
                return sum;
            });

        return aStar.solve(this, Objects.requireNonNull(goal));
    }


    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().isInstance(this)) return false;
        return Arrays.equals(((Puzzle8) obj).puzzle, this.puzzle);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int current = 0;
            @Override public boolean hasNext() { return current < puzzle.length; }
            @Override public Integer next() { return puzzle[current++]; }
        };
    }
}
