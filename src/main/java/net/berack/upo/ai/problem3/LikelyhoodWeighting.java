package net.berack.upo.ai.problem3;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import smile.Network;

/**
 * Calcolo dei valori tramite l'algoritmo del Likelyhood Weighting
 * @author Berack
 */
public class LikelyhoodWeighting {

    public final Network net;
    private Map<Integer, double[]> values = new HashMap<>();

    /**
     * Inizializza un nuovo oggetto che calcolerà i valori per la rete inserita
     * @param net la rete a cui calcolare i valori
     */
    public LikelyhoodWeighting(Network net) {
        this.net = Objects.requireNonNull(net);
    }

    /**
     * Recupera i valori del nodo dopo averli calcolati
     * Nel caso in cui non si abbia ancora fatto {@link #updateNetwork(int)} allora restituirà
     * una eccezione di tiop UnsupportedOperationException
     * @param node il nodo da vedere
     * @return l'array di valori da restituire
     */
    public double[] getNodeValue(int node) {
        if(values.size() == 0) throw new UnsupportedOperationException("You should run first updateNetwork method");
        return values.get(node);
    }

    /**
     * Calcola i valori possibili per la rete.
     * Per poterli vedere utilizzare il metodo {@link #getNodeValue(int)}
     * @param totalRuns
     */
    public void updateNetwork(int totalRuns) {
        totalRuns = Math.max(1, totalRuns);

        var nodes = SmileLib.buildListFrom(net);
        var rand = new SecureRandom();
        var prob = new double[totalRuns];
        var sum = 0.0d;

        for(var node : nodes)
            node.samples = new int[totalRuns];

        for(var run = 0; run < totalRuns; run++) {
            var probRun = 1.0d;

            for(var node: nodes) {
                if(!node.isEvidence()) node.setSample(rand.nextDouble(), run);
                else probRun *= node.getProbSampleEvidence(run);
            }

            prob[run] = probRun;
            sum += probRun;
        }

        for(var node : nodes) if(!node.isEvidence()) {
            var values = new double[node.outcomes.length];

            for(var run = 0; run < totalRuns; run++)
                values[node.samples[run]] += prob[run];
            for(var i = 0; i < values.length; i++)
                values[i] /= sum;

            this.values.put(node.handle, values);
        }
    }
}
