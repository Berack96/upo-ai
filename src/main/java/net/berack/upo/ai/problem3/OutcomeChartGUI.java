package net.berack.upo.ai.problem3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Classe che rappresenta gli outcome di un nodo di un network.
 * I valori deli outcome vengono visualizzati con un grafico.
 * @author Berack
 */
public class OutcomeChartGUI extends JPanel {
    public static final Color[] COLORS = {
        new Color(255,127,0),
        new Color(0,127,255),
        new Color(0,255,127),
        new Color(255,0,127),
        new Color(127,0,255),
        new Color(127,255,0),
    };

    /**
     * Crea il JPanel da visualizzare a partire da un NetworkNode appropriamente inizializzato.
     * Quando verrà visualizzato, il nodo avrà il nome degli output e i suoi valori in %
     * con una barra colorata per indicare la grandezza visivamente.
     * 
     * @param node il nodo di cui si vogliono visualizzare gli outcome
     * @param action una azione da fare nel caso in cui venga premuto su un outcome
     */
    public OutcomeChartGUI(NetworkNode node, Consumer<Integer> action) {
        var labels = node.outcomes;
        var values = node.values;

        if(labels.length != values.length) throw new IllegalArgumentException("Arrays length myst be equals!");
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(1, 0, 0, 0), BorderFactory.createLineBorder(Color.GRAY)));

        var layout = new GridLayout(labels.length, 2);
        this.setLayout(layout);

        for(var i = 0; i < labels.length; i++) {
            var value = values[i] * 100;

            var lName = new JButton(labels[i]);
            var lValue = new Label(String.format("% 4.2f%%", value));
            var barchart = new Label();
            var size = barchart.getPreferredSize();

            final var index = i;
            lName.setContentAreaFilled(false);
            lName.setFocusable(false);
            lName.addActionListener(a -> action.accept(index));
            if(node.evidence == i) lName.setForeground(Color.RED);

            size.width = (int) (value * 1.5);
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
}
