package net.berack.upo.ai.problem2;

import static net.berack.upo.ai.problem2.Tris.Symbol.*;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Classe che viene usata per giocare a Tris.
 * Questa classe permette dato una classe Tris di giocare contro una AI.
 * 
 * @author Berack96
 */
public class TrisAi {

    /**
     * Funzione che permette di far vedere quali azioni sono disponibili dato uno stato della partita.
     * Essa richiama la già esistente funzione dalla classe Tris
     */
    public static final Function<Tris, Tris.Coordinate[]> ACTIONS = tris -> tris.availablePlays();
    /**
     * Funzione che permette l'avanzamento della partita. Siccome e' solo una funzione temporanea,
     * ovvero che la partita vera e propria non deve andara avanti, viene restituita una nuova istanza del gioco.
     */
    public static final BiFunction<Tris, Tris.Coordinate, Tris> TRANSITION = (tris, coord) -> new Tris(tris, coord);
    /**
     * Funzione euristica che permette di controllare, dato uno stato, quanto sia favorevole o meno al giocatore
     * passato in input.
     */
    public static final BiFunction<Tris, Tris.Symbol, Integer> GAIN = (tris, player) -> {
        var count = 0;

        // top left
        count += TrisAi.value(player, tris.get(0,0), tris.get(1,0), tris.get(2,0));
        count += TrisAi.value(player, tris.get(0,0), tris.get(0,1), tris.get(0,2));

        // bottom right
        count += TrisAi.value(player, tris.get(2,2), tris.get(1,2), tris.get(0,2));
        count += TrisAi.value(player, tris.get(2,2), tris.get(2,1), tris.get(2,0));

        // center diagonals
        count += TrisAi.value(player, tris.get(0,0), tris.get(1,1), tris.get(2,2));
        count += TrisAi.value(player, tris.get(0,2), tris.get(1,1), tris.get(2,0));

        // center horizontal & vertical
        count += TrisAi.value(player, tris.get(0,1), tris.get(1,1), tris.get(2,1));
        count += TrisAi.value(player, tris.get(1,0), tris.get(1,1), tris.get(1,2));

        return count;
    };

    /**
     * Questo metodo restituisce un valore in base ai simboli passati, con valore positivo se
     * sono uguali al primo parametro in input, altrimenti negativo:
     * - Se ho 3 simboli uguali -> 100
     * - Se ho 2 simboli uguali e uno vuoto -> 10
     * - Se ho un solo simbolo e 2 vuoti -> 1
     * 
     * @param symbol il simbolo da testare
     * @param values i successivi simboli a cui assegnare un valore
     * @return un punteggio in base alle combinazioni dei simboli e al fatto che siano uguali al primo parametro
     */
    static int value(Tris.Symbol symbol, Tris.Symbol...values) {
        var totE = 0;
        var totO = 0;
        var totX = 0;

        for(var val : values) switch(val) {
            case VALUE_O: totO += 1; break;
            case VALUE_X: totX += 1; break;
            case EMPTY: totE += 1; break;
        }

        var value = 0;
        if(totO == 3 || totX == 3) value = 100;
        else if(totE + totO == 3 || totE + totX == 3) value = switch(totE) {
            case 1 -> 10;
            case 2 -> 1;
            default -> 0;
        };

        if((totO > totX && symbol == VALUE_X) || totX > totO && symbol == VALUE_O)
            return -value;
        return value;
    }


    private Tris tris;
    private MiniMax<Tris, Tris.Coordinate, Tris.Symbol> minimax;

    /**
     * Crea una istanza dell'AI collegata al gioco passato in input.
     * Questa AI non giocherà in automatico ma solo quando lo si
     * ritiene opportuno attraverso il metodo {@link #playNext()}.
     * 
     * @param tris il gioco a cui aggiungere una AI
     */
    public TrisAi(Tris tris) {
        this.tris = Objects.requireNonNull(tris);
        this.minimax =  new MiniMax<>(TRANSITION, ACTIONS, GAIN);
    }

    /**
     * L'AI farà una mossa sul gioco collegato ad essa.
     * Equivale chiamare {@link #playNext(int)} con input 2
     */
    public void playNext() {
        this.playNext(2);
    }

    /**
     * L'AI farà una mossa sul gioco collegato ad essa.
     * Questo metodo permette di passare in input il lookahead
     * in modo da limitare lo spazio delle possibili mosse che
     * l'AI andrà ad osservare per scegliere la sua azione.
     * Un valore basso e la mossa sarà "peggiore", un valore alto e
     * per trovare una mossa ci impiegherà più tempo.
     * NOTA: Il valore del lookahead è un valore esponenziale, quindi
     * se si mette 5 rispetto a 4, ci impiegherà un tempo almeno doppio.
     * 
     * @param lookahead quante mosse consecutive può osservare
     */
    public void playNext(int lookahead) {
        var myself = tris.getNextPlaySymbol();
        var action = minimax.next(this.tris, lookahead, myself);
        tris.play(action.x, action.y);
    }
}
