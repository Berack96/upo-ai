package net.berack.upo.ai.gui.nodes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import smile.Network;

/**
 * La rappresentazione grafica di un nodo probabilistico.
 * Esso mostrerà gli outcome ele probabilità associate ad essi, e permetterà di sceglierne uno o l'altro.
 * Nel caso in cui ci siano più valori per outcome allora il nodo non sarà abilitato
 * @author Berack
 */
public class ProbabilityNodeGUI extends NodeGUI {

    public static final Color[] COLORS = {
        new Color(255,127,0),
        new Color(0,127,255),
        new Color(0,255,127),
        new Color(255,0,127),
        new Color(127,0,255),
        new Color(127,255,0),
    };

    private final JButton[] outcomes;
    private final Label[] valuesChart;
    private final JLabel[] valuesPercent;

    /**
     * Costruisce il nodo probabilistico in modo che si possa interagire con esso
     * @param net la rete del nodo
     * @param node il nodo
     * @param action la azione da fare quando si preme su un outcome
     */
    public ProbabilityNodeGUI(Network net, int node, Consumer<Integer> action) {
        super(net, node);
        var labels = net.getOutcomeIds(node);

        var layout = new GridLayout(labels.length, 2);
        this.setLayout(layout);
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(1, 0, 0, 0), BorderFactory.createLineBorder(Color.GRAY)));

        this.outcomes = new JButton[labels.length];
        this.valuesChart = new Label[labels.length];
        this.valuesPercent = new JLabel[labels.length];

        for(var i = 0; i < labels.length; i++) {
            var lName = new JButton(labels[i]);
            var lValue = new JLabel();
            var barchart = new Label();

            this.outcomes[i] = lName;
            this.valuesChart[i] = barchart;
            this.valuesPercent[i] = lValue;

            final var index = i;
            lName.setContentAreaFilled(false);
            lName.setFocusable(false);
            if(action == null) lName.setBorder(null);
            else lName.addActionListener(a -> action.accept(index));

            var size = barchart.getPreferredSize();
            size.width = 100;
            size.height = 10;
            barchart.setPreferredSize(size);
            barchart.setBackground(COLORS[i % COLORS.length]);


            var panel1 = new JPanel();
            panel1.setLayout(new GridLayout(1, 2));
            panel1.add(lName);
            panel1.add(lValue);

            var panel2 = new JPanel();
            panel2.setLayout(new BorderLayout());
            panel2.add(barchart, BorderLayout.LINE_START);

            this.add(panel1);
            this.add(panel2);
        }
    }

    @Override
    public void updateNode() {
        var values = this.net.getNodeValue(this.node);
        var evidence = this.net.isEvidence(this.node)? this.net.getEvidence(node) : -1;
        var enable = (values.length == this.outcomes.length);

        for(var i = 0; i < this.outcomes.length; i++) {
            var value = values[i] * 100;
            var barchart = this.valuesChart[i];

            var size = barchart.getPreferredSize();
            size.width = enable? (int) (value * 1.5) : 0;
            barchart.setSize(size);
            barchart.setPreferredSize(size);

            this.valuesPercent[i].setText(enable? String.format("% 4.2f%%", value) : " ");
            if(evidence == i) this.outcomes[i].setForeground(Color.RED);
            else this.outcomes[i].setForeground(Color.BLACK);
            this.outcomes[i].setEnabled(enable);
        }
    }

}
