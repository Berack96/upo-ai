package net.berack.upo.ai.gui.nodes;

import javax.swing.JPanel;

import smile.Network;

/**
 * Classe astratta che implementa un nodo base data la rete in input.
 * @author Berack
 */
public abstract class NodeGUI extends JPanel {

    public final Network net;
    public final int node;
    private double[] values = null;

    /**
     * Salva informazioni essenziali del nodo
     * I costruttori delle sottoclassi dovranno creare il contenuto del pannello
     * ed utilizzare questo costruttore.
     * I valori passati saranno poi disponibili alle proprietà pubbliche net e node.
     * 
     * @param net la rete a cui appartiene
     * @param node il valore dell'handle
     */
    protected NodeGUI(Network net, int node) {
        this.net = net;
        this.node = node;
    }

    /**
     * Permette di impostare dei valori custom da mostrare.
     * Dopo la chiamata non verranno modificati i valori a schermo se non
     * si ha chiamato il metodo updateNode.
     * 
     * @param values i valori da mostrare (dovranno essere compresi tra 0 e 1)
     */
    public void setValues(double[] values) {
        for(var val : values) if(val < 0 || val > 1) throw new IllegalArgumentException();
        this.values = values;
    }

    /**
     * Mostra i valori del nodo. Se sono stati passati dei valori custom allora userà quelli,
     * altrimenti prenderà i valori contenuti all'interno della rete.
     * @return i valori del nodo
     */
    public double[] getValues() {
        if(this.values != null) return this.values;
        return this.net.getNodeValue(this.node);
    }

    /**
     * In questo metodo si deve fare un refresh dei valori mostrati nel pannello.
     * I valori potranno essere presi direttamente dalla rete utilizzando le proprietà pubbliche net e node.
     */
    abstract public void updateNode();
}
