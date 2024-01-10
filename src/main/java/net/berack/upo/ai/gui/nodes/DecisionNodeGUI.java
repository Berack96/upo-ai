package net.berack.upo.ai.gui.nodes;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import smile.Network;

/**
 * La rappresentazione grafica di un nodo a decisione.
 * Esso mostrerà gli outcome e i valori associati ad essi, e permetterà di sceglierne uno o l'altro.
 * Nel caso in cui ci siano più valori per outcome allora il nodo non sarà abilitato
 * @author Berack
 */
public class DecisionNodeGUI extends NodeGUI {

    private final AbstractButton[] buttons;
    private final JLabel[] values;
    private final ButtonGroup group;

    /**
     * Costruisce il nodo decisionale in modo che si possa interagire con esso
     * @param net la rete del nodo
     * @param node il nodo
     * @param action la azione da fare quando si preme su una decisione
     */
    public DecisionNodeGUI(Network net, int node, Consumer<Integer> action) {
        super(net, node);

        var outcomes = net.getOutcomeCount(node);
        this.buttons = new AbstractButton[outcomes];
        this.values = new JLabel[outcomes];

        this.group = new ButtonGroup();
        var grid = new GridLayout(outcomes, 2);
        this.setLayout(grid);

        for(var i = 0; i < outcomes; i++) {
            final var index = i;
            var label = net.getOutcomeId(node, i);
            var value = new JLabel();
            var button = new JCheckBox(label);
            button.setContentAreaFilled(false);
            button.setFocusable(false);
            button.addActionListener(a -> action.accept(index));

            this.buttons[i] = button;
            this.values[i] = value;
            this.add(button);
            this.add(value);
            this.group.add(button);
        }
    }

    @Override
    public void updateNode() {
        var selected = this.net.isEvidence(this.node)? this.net.getEvidence(this.node) : -1;
        var values = this.net.getNodeValue(this.node);
        var max = -1;
        if(values.length == this.buttons.length) {
            max = 0;
            for(int i = 1; i < values.length; i++)
                if(values[max] < values[i])
                    max = i;
        }

        this.group.clearSelection();

        for(var i = 0; i < this.buttons.length; i++) {
            var font = buttons[i].getFont();
            var style = (i == max)? (Font.BOLD + Font.ITALIC) : Font.PLAIN;
            var newFont = new Font(font.getName(), style, font.getSize());
            var value = max < 0? "" : String.format("% 5.2f", values[i]);

            this.buttons[i].setFont(newFont);
            this.buttons[i].setSelected(i == selected);
            this.buttons[i].setEnabled(max != -1);
            this.values[i].setText(value);
        }
    }
}
