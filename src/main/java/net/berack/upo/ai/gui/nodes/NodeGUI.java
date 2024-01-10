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
     * In questo metodo si deve fare un refresh dei valori mostrati nel pannello.
     * I valori potranno essere presi direttamente dalla rete utilizzando le proprietà pubbliche net e node.
     */
    abstract public void updateNode();
}
