package net.berack.upo.ai.problem3;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import smile.Network;

/**
 * Calcolo dei valori tramite l'algoritmo del Likelihood Weighting
 * @author Berack
 */
public class LikelihoodWeighting {

    public final Network net;
    private final Map<Integer, NetworkNode> nodes = new HashMap<>();

    /**
     * Inizializza un nuovo oggetto che calcolerà i valori per la rete inserita
     * @param net la rete a cui calcolare i valori
     */
    public LikelihoodWeighting(Network net) {
        this.net = Objects.requireNonNull(net);
    }

    /**
     * Recupera i valori del nodo dopo averli calcolati
     * Nel caso in cui non si abbia ancora fatto {@link #updateNetwork(int)} allora restituirà
     * una eccezione di tipo UnsupportedOperationException
     * @param node il nodo da vedere
     * @return l'array di valori da restituire
     */
    public double[] getNodeValue(int node) {
        if(nodes.size() == 0) throw new UnsupportedOperationException("You should run first updateNetwork method");
        return nodes.get(node).values;
    }

    /**
     * Permette di recuperare tutti i nodi.
     * Nel caso in cui non si abbia ancora fatto {@link #updateNetwork(int)} allora restituirà
     * una eccezione di tipo UnsupportedOperationException
     * @return Una collezione di tutti i nodi.
     */
    public Collection<NetworkNode> getAllNodes() {
        if(nodes.size() == 0) throw new UnsupportedOperationException("You should run first updateNetwork method");
        return this.nodes.values();
    }

    /**
     * Calcola i valori possibili per la rete.
     * Per poterli vedere utilizzare il metodo {@link #getNodeValue(int)}
     * @param totalRuns
     */
    public void updateNetwork(int totalRuns) {
        totalRuns = Math.max(1, totalRuns);

        var list = SmileLib.buildListFrom(net);
        var rand = new SecureRandom();
        var sum = 0.0d;

        for(var run = 0; run < totalRuns; run++) {
            var probRun = 1.0d;

            for(var node: list) {
                if(node.isEvidence()) probRun *= node.getProbSampleEvidence();
                else node.setSample(rand.nextDouble());
            }

            for(var node: list) {
                if(!node.isEvidence()) {
                    node.values[node.sample] += probRun;
                    node.sample = -1;
                }
            }

            sum += probRun;
        }

        this.nodes.clear();
        for(var node : list) {
            this.nodes.put(node.handle, node);

            if(!node.isEvidence())
                for(var i = 0; i < node.values.length; i++)
                    node.values[i] /= sum;
        }
    }

    @Override
    public String toString() {
        return this.nodes.values().toString();
    }
}
