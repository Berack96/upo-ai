package net.berack.upo.ai.problem1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;

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
    public static final int[] DEFAULT_GOAL = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 0};
    public static final BiFunction<Puzzle8, Puzzle8, Integer> HEURISTIC_MANHATTAN = (p1, p2) -> {
        var sum = 0;
        var n = p1.puzzle.length;
        var index = new int[n];

        for(var i = 0; i < n; i++) index[p1.puzzle[i]] = i;
        for(var i = 0; i < n; i++) {
            if(i == p2.blank) continue;

            var curr = p2.puzzle[i];
            var i2 = index[curr];
            sum += Math.abs((i / LENGTH) - (i2 / LENGTH)) + Math.abs((i % LENGTH) - (i2 % LENGTH));
        }
        return sum;
    };

    /**
     * Possibili movimenti della tessera "vuota"
     */
    public static enum Move {
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
        this.puzzle = Arrays.copyOf(DEFAULT_GOAL, DEFAULT_GOAL.length);
        this.shuffle();
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
        this.puzzle = Arrays.copyOf(original.puzzle, original.puzzle.length);
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
        return get(y * LENGTH + x);
    }

    /**
     * Permette di ricevere in input il valore della coordinata passata in input.
     * Il valore in input ha formato flat2D, ovvero è un numero che rappresenta una coordinata 2D in uno spazio finito.
     * La formula generale per calcolare il numero è la seguente: x + (MAX_X + 1) * y.
     * Quindi nel nostro caso si può calcolare la formula con: x + Puzzle8.LENGTH * y.
     * NOTA: non usare questo metodo se non si ha capito che cosa significhi. Piuttosto utilizzare {@link #get(int, int)}
     * @param flat2D la rappresentazione monodimensionale della coordinata 2D
     * @return il valore in quel punto del puzzle.
     */
    public int get(int flat2D) {
        return puzzle[flat2D];
    }

    /**
     * Permette di avere la posizione della tessera vuota in formato flat2D utilizzabile nel metodo {@link #get(int)}
     * @return la posizione dello 0 o tessera vuota
     */
    public int getBlankPosition() {
        return this.blank;
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
     * Questo metodo mette a caso le tessere del puzzle.
     * É possibile che, una volta che le tessere sono state spostate a caso,
     * non siano più raggiungibili alcuni stati.
     * 
     * @see #isSolvable()
     * @see #isSolvable(Puzzle8)
     */
    public void shuffle() {
        var rand = new Random();
        for(var i = this.puzzle.length - 1; i > 0; i--) {
            var j = rand.nextInt(i + 1);
            var temp = this.puzzle[i];
            this.puzzle[i] = this.puzzle[j];
            this.puzzle[j] = temp;
        }

        for(var i = 0; i < this.puzzle.length; i++)
            if(this.puzzle[i] == 0)
                this.blank = i;
    }

    /**
     * Indica se l'istanza del puzzle corrente sia risolvibile per il caso default.
     * Con caso default si indica il puzzle composto da {@link #DEFAULT_GOAL}
     * 
     * @return true nel caso si possa risolvere, false altrimenti
     */
    public boolean isSolvable() {
        return this.isSolvable(new Puzzle8(DEFAULT_GOAL));
    }

    /**
     * Metodo per controllare la risolvibilità del puzzle corrente.
     * 
     * @param goal lo stato a cui si vuole arrivare
     * @return true nel caso si possa risolvere, false altrimenti
     */
    public boolean isSolvable(Puzzle8 goal) {
        var n = LENGTH * LENGTH;
        var indexGoal = new int[n];

        for(var i = 0; i < n; i++)
            indexGoal[goal.puzzle[i]] = i;

        var sum = 0;
        for(var i = 0; i < n; i++) {
            var curr = this.puzzle[i];
            var iGoal = indexGoal[curr];

            if(curr == 0) continue;

            for(var j = n - 1; j > i ; j--) {
                var val = this.puzzle[j];
                if(val != 0 && indexGoal[val] < iGoal) sum += 1;
            }
        }

        return (sum % 2) == 0;
    }

    /**
     * Usa l'algoritmo A* per la soluzione del problema.
     * Questo metodo cercherà di raggiungere lo stato goal indicato da {@link #DEFAULT_GOAL}
     * @return una sequenza di mosse per poter risolvere il problema o null se non risolvibile
     */
    public List<Move> solve() {
        return this.solve(new Puzzle8(DEFAULT_GOAL));
    }

    /**
     * Usa l'algoritmo A* per la soluzione del problema
     * @param goal lo stato goal in cui arrivare
     * @return una sequenza di mosse per poter risolvere il problema o null se non risolvibile
     */
    public List<Move> solve(Puzzle8 goal) {
        if(!this.isSolvable(goal)) return null;

        var aStar = new AStar<Puzzle8, Move>(Puzzle8::availableMoves, Puzzle8::new);
        aStar.setHeuristic(HEURISTIC_MANHATTAN);

        return aStar.solve(this, Objects.requireNonNull(goal));
    }

    @Override
    public String toString() {
        return Arrays.toString(this.puzzle);
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
