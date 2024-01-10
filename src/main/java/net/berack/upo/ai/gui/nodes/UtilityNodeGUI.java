package net.berack.upo.ai.gui.nodes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;

import smile.Network;

/**
 * La rappresentazione grafica di un nodo utilità.
 * Esso mostrerà il valore solamente nel caso in cui esso sia l'unico.
 * @author Berack
 */
public class UtilityNodeGUI extends NodeGUI {

    private final JLabel utility;

    /**
     * Costruisce il nodo utilità che mostra il valore corrente
     * @param net la rete del nodo
     * @param node il nodo
     */
    UtilityNodeGUI(Network net, int node) {
        super(net, node);

        this.utility = new JLabel();
        var font = this.utility.getFont();
        font = new Font(font.getName(), Font.BOLD, font.getSize() + 5);

        this.setLayout(new FlowLayout());

        this.utility.setFont(font);
        this.utility.setForeground(Color.RED);

        this.add(utility);
    }

    @Override
    public void updateNode() {
        var values = this.net.getNodeValue(this.node);
        var val = (values.length == 1) ? String.format("% 5.2f", values[0]) : " ";
        this.utility.setText(val);
    }

}
