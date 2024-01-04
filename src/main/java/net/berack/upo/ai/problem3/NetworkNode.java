package net.berack.upo.ai.problem3;

import java.util.Arrays;
import java.util.Map;

import smile.Network;

/**
 * Una classe di appoggio per i nodi di un network.
 * Questa classe contiene anche un array di samples in modo da facilitare il
 * calcolo di valori.
 * 
 * @see SmileLib#buildListFrom(NetWork)
 * @author Berack
 */
public class NetworkNode {

    final int handle;
    final String name;
    final String[] outcomes;
    final double[] definition;
    final int evidence;
    final Network net;
    final int type;
    final NetworkNode[] parents;

    public int sample = -1;
    public final double[] values;

    /**
     * Questo costruttore crea un nodo e gli assegna i valori essenziali.
     * @apiNote Non usare questo costruttore se non si sa che si sa quello che si sta facendo
     * @param net la rete
     * @param handle l'handle del nodo
     * @param nodes i nodi creati prima di questo
     */
    NetworkNode(Network net, int handle, Map<Integer, NetworkNode> nodes) {
        this.handle = handle;
        this.type = net.getNodeType(handle);
        this.name = net.getNodeId(handle);
        this.net = net;

        this.outcomes = net.getOutcomeIds(handle);
        this.values = new double[this.outcomes.length];
        this.evidence = net.isEvidence(handle)? net.getEvidence(handle) : -1;
        if(this.isEvidence()) {
            this.values[this.evidence] = 1.0d;
            this.sample = this.evidence;
        }

        var parentsHandle = net.getParents(handle);
        this.parents = new NetworkNode[parentsHandle.length];
        for(var i = 0; i < parentsHandle.length; i++)
            this.parents[i] = nodes.get(parentsHandle[i]);

        this.definition = switch(this.type) {
            case Network.NodeType.CPT -> net.getNodeDefinition(handle);
            case Network.NodeType.NOISY_MAX -> net.getNoisyExpandedDefinition(handle);
            default -> throw new IllegalArgumentException("Network with node type not supporrted -> " + this.type);
        };
    }

    /**
     * Indica se il nodo è evidenza o meno
     * @return vero se lo è
     */
    public boolean isEvidence() {
        return this.evidence >= 0 ;
    }

    /**
     * Per utilizzare questo metodo il nodo deve essere una evidenza.
     * Permette di ricevere il valore della probabilità che, dati i sample dei genitori,
     * il nodo abbia il valore dell'evidenza impostata.
     * 
     * @return il valore della probabilità della evidenza
     */
    public double getProbSampleEvidence() {
        if(!this.isEvidence()) throw new IllegalArgumentException("Evidence");

        var init = getStartingIndex();
        return this.definition[init + this.evidence];
    }

    /**
     * Mette un sample al nodo nel round selezionato.
     * Il valore di rand deve essere un numero casuale tra 0 e 1 ed
     * esso permetterà di impostare un valore in base ai valori dei sample dei genitori.
     * 
     * @param rand un valore casuale tra 0 e 1
     */
    public void setSample(double rand) {
        var init = getStartingIndex();
        var end = init + this.values.length;
        var prob = 0.0d;

        for(var i = init; i < end; i++) {
            prob += this.definition[i];

            if(prob >= rand) {
                this.sample = i - init;
                break;
            }
        }
    }

    /**
     * Permette di ricavare l'indice di partenza della CPT.
     * Questo metodo serve perchè i genitori del nodo nel sample hanno
     * dei valori e io devo generarli in accordo con la CPT di questo nodo.
     * 
     * @return l'indice iniziale per gli output del nodo in base ai valori dei genitori
     */
    private int getStartingIndex() {
        var init = 0;
        var tot = this.definition.length;

        for(var p : this.parents) {
            if(p.sample < 0) throw new IllegalArgumentException("Parent"); // in theory impossible since Topological sorted

            tot /= p.outcomes.length;
            init += tot * p.sample;
        }

        return init;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().isInstance(this)) return false;

        var other = (NetworkNode) obj;
        if(this.handle != other.handle) return false;
        if(this.evidence != other.evidence) return false;
        if(!Arrays.equals(this.definition, other.definition)) return false;

        return true;
    }

    @Override
    public String toString() {
        return this.net.getNodeId(this.handle) + "->" + Arrays.toString(this.values);
    }
}