package net.berack.upo.ai.problem1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Una classe che risolve problemi utilizzando l'algoritmo A*.
 * @author Berack96
 * @param State la classe degli stati in cui si trova il problema da risolvere
 * @param Action la classe di azioni che si possono compiere da uno stato e l'altro
 */
public class AStar<State, Action> {

    private Function<State, Action[]> actions;
    private BiFunction<State, Action, State> transition;

    private BiFunction<State, State, Integer> heuristic;
    private TriFunction<State, State, Action, Integer> cost;

    /**
     * Crea una istanza dell'algoritmo A* con una funzione di azioni possibili da compiere dato uno stato
     * e una funzione di transizione che dato uno stato permette di raggiungerne un'altro tramite un'azione.
     * 
     * @param actions la funzione che da uno stato permette di vedere le possibili azioni da compiere
     * @param transition la funzione di tansizione che ci permette di raggiungere uno stato da un'altro tramite un'azione
     */
    public AStar(Function<State, Action[]> actions, BiFunction<State, Action, State> transition) {
        this.actions = Objects.requireNonNull(actions);
        this.transition = Objects.requireNonNull(transition);

        this.cost = (s1,s2, a) -> 1;
        this.heuristic = (s1,s2) -> 0;
    }

    /**
     * Permette di impostare la funzione di costo che indica, facendo un'azione su uno stato e raggiungendone un'altro, quanto devo spendere, ovvero il costo.
     * Di default la funzione di costo equivale sempre ad 1
     * @param cost la funzione di costo
     * @return se stesso in modo da poter concatenare i vari set (un pò come una factory)
     */
    public AStar<State, Action> setCost(TriFunction<State, State, Action, Integer> cost) {
        this.cost = Objects.requireNonNull(cost);
        return this;
    }

    /**
     * Permette di impostare una funzione di transizione che indica, facendo un'azione su uno stato, a quale stato possiamo arrivare.
     * @param transition la funzione di transizione
     * @return se stesso in modo da poter concatenare i vari set (un pò come una factory)
     */
    public AStar<State, Action> setTransition(BiFunction<State, Action, State> transition) {
        this.transition = Objects.requireNonNull(transition);
        return this;
    }

    /**
     * Permette di impostare una funzione euristica che indica, prendendo uno stato e uno stato goal, la distanza di costo approssimata per arrivare al goal
     * @param heuristic la funzione euristica
     * @return se stesso in modo da poter concatenare i vari set (un pò come una factory)
     */
    public AStar<State, Action> setHeuristic(BiFunction<State, State, Integer> heuristic) {
        this.heuristic = Objects.requireNonNull(heuristic);
        return this;
    }

    /**
     * Permette di impostare una funzione che indica, a partire da uno stato, quali sono le azioni che posso svolgere
     * @param actions la funzione di azioni
     * @return se stesso in modo da poter concatenare i vari set (un pò come una factory)
     */
    public AStar<State, Action> setActions(Function<State, Action[]> actions) {
        this.actions = Objects.requireNonNull(actions);
        return this;
    }

    /**
     * Permette di risolvere il problema dopo aver indicato le varie funzioni.
     * In input viene richiesto uno stato iniziale e uno stato goal da raggiungere dallo stato corrente.
     * La funzione utilizzata per il raggiungimento dello stato goal è equals(), quindi è richiesto che
     * la classe State abbia implementato correttamente la funzione equals().
     * 
     * @param initial lo stato corrente
     * @param goal lo stato da raggiungere
     * @return una sequenza di azioni per poter raggiungere lo stato goal oppure null se non è possibile
     */
    public List<Action> solve(State initial, State goal) {
        Objects.requireNonNull(initial);
        Objects.requireNonNull(goal);

        NodeState found = null;
        var list = new PriorityQueue<NodeState>();
        list.add(new NodeState(null, initial, null, 0));

        while(list.size() > 0) {
            var current = list.poll();
            if(current.state.equals(goal)) {
                found = current;
                break;
            }

            for(var action : this.actions.apply(current.state)) try {

                var next = this.transition.apply(current.state, action);

                var cost = this.cost.apply(current.state, next, action);
                var dist = this.heuristic.apply(next, goal);

                list.add(new NodeState(current, next, action, current.cost + cost + dist));

            } catch (Exception ignore) {}
        }

        if(found == null) return null;

        var path = new ArrayList<Action>();
        while(found != null) {
            path.add(found.action);
            found = found.parent;
        }

        Collections.reverse(path);
        return path;
    }


    /**
     * Classe privata per mantenere i dati all'interno della PriorityQueue.
     */
    private class NodeState implements Comparable<NodeState> {
        NodeState parent;
        State state;
        Action action;
        int cost;

        NodeState(NodeState parent, State state, Action action, int cost) {
            this.parent = parent;
            this.state = state;
            this.action = action;
            this.cost = cost;
        }

        @Override
        public int compareTo(NodeState other) {
            return this.cost - other.cost;
        }
    }

    /**
     * Interfaccia privata per poter indicare una TriFunction, ovvero una funzione che accetta in input 3 parametri
     */
    @FunctionalInterface private interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v);
    }
}
