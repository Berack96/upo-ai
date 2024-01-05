package net.berack.upo.ai.problem3;

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

    private JButton[] outcomes;
    private Label[] valuesChart;
    private JLabel[] valuesPercent;

    /**
     * Crea il JPanel da visualizzare a partire da un NetworkNode appropriamente inizializzato.
     * Quando verrà visualizzato, il nodo avrà il nome degli output e i suoi valori in %
     * con una barra colorata per indicare la grandezza visivamente.
     * 
     * @param labels i label degli output del nodo
     * @param action una azione da fare nel caso in cui venga premuto su un outcome
     */
    public OutcomeChartGUI(String[] labels, Consumer<Integer> action) {
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
            size.height = 100;
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

    /**
     * Modifica i valori mostrati a schermo cambiando anche la grandezza
     * della barra indicante il valore
     * @param values i nuovi valori da mostrare
     * @param evidence indica l'evidenza del nodo
     */
    public void updateValues(double[] values, int evidence) {
        if(this.outcomes.length != values.length) throw new IllegalArgumentException("Arrays length myst be equals!");

        for(var i = 0; i < this.outcomes.length; i++) {
            var value = values[i] * 100;
            var barchart = this.valuesChart[i];

            var size = barchart.getSize();
            size.width = (int) (value * 1.5);
            barchart.setSize(size);
            barchart.setPreferredSize(size);

            this.valuesPercent[i].setText(String.format("% 4.2f%%", value));
            if(evidence == i) this.outcomes[i].setForeground(Color.RED);
            else this.outcomes[i].setForeground(Color.BLACK);
        }
    }
}
