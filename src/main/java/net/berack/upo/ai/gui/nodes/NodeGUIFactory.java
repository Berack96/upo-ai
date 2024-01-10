package net.berack.upo.ai.gui.nodes;

import java.util.function.Consumer;

import smile.Network;
import smile.Network.NodeType;

/**
 * Classe che raggruppa tutte le rappresentazioni grafiche dei nodi.
 * È una classe comoda per la creazione dei nodi dato che crea automaticamente
 * anche la funzione di update e scelta della evidenza per ogni nodo.
 * @author Berack
 */
public class NodeGUIFactory {

    private final Network net;
    private final Runnable updateAll;

    /**
     * Crea una factory pronta per la creazione dei nodi
     * @param net la rete
     * @param updateAll una funzione richiesta per l'update di tutti gli altri nodi
     */
    public NodeGUIFactory(Network net, Runnable updateAll) {
        this.net = net;
        this.updateAll = updateAll;
    }

    /**
     * Crea una nuova rappresentazione grafica di un nodo passato in input.
     * 
     * @throws IllegalArgumentException nel caso in cui il nodo non sia ancora supportato
     * @param handle il nodo
     * @return la rappresentazione grafica del nodo
     */
    public NodeGUI create(int handle) {
        Consumer<Integer> action = outcome -> {
            if(net.isEvidence(handle) && net.getEvidence(handle) == outcome) this.net.clearEvidence(handle);
            else this.net.setEvidence(handle, outcome);

            this.updateAll.run();
        };

        return switch(net.getNodeType(handle)) {
            case NodeType.DECISION -> new DecisionNodeGUI(net, handle, action);
            case NodeType.UTILITY -> new UtilityNodeGUI(net, handle);
            case NodeType.MAU -> new UtilityNodeGUI(net, handle);
            case NodeType.NOISY_MAX -> new ProbabilityNodeGUI(net, handle, action);
            case NodeType.CPT -> new ProbabilityNodeGUI(net, handle, action);

            default -> throw new IllegalArgumentException("Node type not supported! ");
        };
    }
}
