package net.berack.upo.ai.problem2;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Algoritmo MiniMax per i giochi a due concorrenti.
 * 
 * @author Berack96
 * @param <State> la classe degli stati in cui si trova il problema da risolvere
 * @param <Action> la classe di azioni che si possono compiere da uno stato e l'altro
 * @param <Player> la classe che indica il giocatore, essa serve da dare in input alla funzione di gain
 */
public class MiniMax<State, Action, Player> {

    private BiFunction<State, Action, State> transition;
    private BiFunction<State, Player, Integer> playerGain;
    private Function<State, Action[]> actions;

    /**
     * Crea una nuova istanza dell'algoritmo MiniMax per i giochi a due concorrenti.
     * Questo costruttore richiede delle particolari funzioni in input:
     * - transizione per passare da uno stato all'altro
     * - actions per avere una lista di azioni da poter svolgere dato uno stato
     * - playerGain per avere un gain dato uno stato (questa funzione DEVE guardare il gain dato solo dal giocatore in input, dato che per l'altro sarà -playerGain())
     * 
     * @param transition la funzione di transizione che permette al gioco di avanzare.
     * @param actions le possibili azioni disponibili da uno stato.
     * @param playerGain il gain che il giocatore ottiene ad essere in quello stato.
     */
    public MiniMax(BiFunction<State, Action, State> transition, Function<State, Action[]> actions, BiFunction<State, Player, Integer> playerGain) {
        this.transition = Objects.requireNonNull(transition);
        this.actions = Objects.requireNonNull(actions);
        this.playerGain = Objects.requireNonNull(playerGain);
    }

    /**
     * Restituisce la migliore azione da fare dato lo stato corrente.
     * Questo metodo ha un lookahead di mosse che guarderà e viene richiesto come parametro.
     * Con valori bassi si potranno ottenere mosse non ottimali globalmente,
     * mentre con valori alti si avranno mosse migliori ma il tempo di computazione
     * sarà esponenzialmente peggiore.
     * Si consigliano valori inferiori a 10.
     * 
     * @param state lo stato corrente
     * @param player il giocatore che deve fare la mossa
     * @param lookahead quante mosse guardare nel futuro
     * @return la migliore mossa localmente
     */
    public Action next(State state, int lookahead, Player player) {
        if(lookahead < 1) throw new IllegalArgumentException("Lookahead must be at least 1, otherwise it is useless");

        Action best = null;
        var bestGain = Integer.MIN_VALUE;

        for(var action : this.actions.apply(state)) {
            var nextState = this.transition.apply(state, action);
            var nextValue = this.expectedGain(nextState, player, lookahead-1, false, bestGain);

            if(nextValue > bestGain) {
                bestGain = nextValue;
                best = action;
            }
        }

        return best;
    }

    /**
     * Implementazione ricorsiva dell'algoritmo minimax
     * @param state lo stato corrente
     * @param depth la profondità a cui arrivare per controllare le mosse
     * @param player il giocatore che dovrà scegliere la mossa migliore
     * @param max se a questa profondità sta giocando max o min
     * @return il guadagno maggiore locale
     */
    private int expectedGain(State state, Player player, int depth, boolean max, int currentBest) {
        var value = this.playerGain.apply(state, player);
        if(depth == 0)
            return value;

        var actions = this.actions.apply(state);
        if(actions.length == 0)
            return value;

        value = max? Integer.MIN_VALUE:Integer.MAX_VALUE;

        for(var action : actions) {
            var nextState = this.transition.apply(state, action);
            var nextValue = this.expectedGain(nextState, player, depth-1, !max, currentBest);

            var condition = (max? value > nextValue : value < nextValue);
            value = condition? value:nextValue;
        }

        return value;
    }
}
