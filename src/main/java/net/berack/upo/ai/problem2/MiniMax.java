package net.berack.upo.ai.problem2;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Algoritmo MiniMax per i giochi a due concorrenti.
 * 
 * @author Berack96
 * @param State la classe degli stati in cui si trova il problema da risolvere
 * @param Action la classe di azioni che si possono compiere da uno stato e l'altro
 */
public class MiniMax<State, Action> {

    private BiFunction<State, Action, State> transition;
    private Function<State, Action[]> actions;
    private Function<State, Integer> maxGain;

    /**
     * Crea una nuova istanza dell'algoritmo MiniMax per i giochi a due concorrenti.
     * Questo costruttore richiede delle particolari funzioni in input:
     * - transizione per passare da uno stato all'altro
     * - actions per avere una lista di azioni da poter svolgere dato uno stato
     * - maxGain per avere un gain dato uno stato (questa funzione DEVE guardare il gain dato solo da max, dato che min sarà  -maxGain())
     * 
     * @param transition la funzione di transizione che permette al gioco di avanzare.
     * @param actions le possibili azioni disponibili da uno stato.
     * @param maxGain il gain che max ottiene ad essere in quello stato.
     */
    public MiniMax(BiFunction<State, Action, State> transition, Function<State, Action[]> actions, Function<State, Integer> maxGain) {
        this.transition = Objects.requireNonNull(transition);
        this.actions = Objects.requireNonNull(actions);
        this.maxGain = Objects.requireNonNull(maxGain);
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
     * @param lookahead quante mosse guardare nel futuro
     * @return la migliore mossa localmente
     */
    public Action next(State state, int lookahead) {
        return nextImpl(state, null, lookahead, true).action;
    }

    /**
     * Implementazione ricorsiva dell'algoritmo minimax
     * @param state lo stato corrente
     * @param action l'azione fatta per arrivarci
     * @param depth la profondità a cui arrivare per controllare le mosse
     * @param max se sta giocando max o min
     * @return la migliore mossa locale con anche il valore guadagnato
     */
    private BestAction nextImpl(State state, Action action, int depth, boolean max) {
        var availableMoves = this.actions.apply(state);
        BestAction best = null;

        if(depth == 0 || availableMoves.length == 0) {
            var gain = this.maxGain.apply(state);
            return new BestAction(action, max? gain : -gain);
        }

        for(var move: availableMoves) {
            var nextState = this.transition.apply(state, move);
            var localBest = this.nextImpl(nextState, move, depth-1, !max);

            best = BestAction.getBest(best, localBest, max);
        }

        if(action != null) best.action = action;
        return best;
    }


    /**
     * Classe di appoggio per restituire il gain e l'azione migliore localmente
     * Ha anche un metodo statico utile per confrontare due azioni.
     */
    private class BestAction {
        Action action;
        int value;

        BestAction(Action action, int value) {
            this.action = action;
            this.value = value;
        }

        static BestAction getBest(BestAction n1, BestAction n2, boolean max) {
            if(n1 == null) return n2;
            if(n2 == null) return n1;

            var condition = (max? n1.value >= n2.value : n1.value <= n2.value);
            return condition? n1:n2;
        }
    }
}
