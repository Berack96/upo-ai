package net.berack.upo.ai.decision;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.berack.upo.ai.gui.MyDecisionPanel;
import net.berack.upo.ai.problem3.SmileLib;

/**
 * Classe che mostra le decisioni possibili, insieme ai guadagni
 * per la rete Prototipo.xdsl
 * @author Berack
 */
public class PrototypeGUI extends MyDecisionPanel {

    private static final String PROTOTIPO = "Prototipo.xdsl";
    private static final String[] NODES = new String[] {
        "Effettuare_la_Ricerca", "Ricerca_di_Mercato",
        "Migliorare_la_Qualità", "Produzione",
        "Valore_Profitto",
    };

    public PrototypeGUI() {
        this.buildPanel(SmileLib.getNetworkFrom(PROTOTIPO), NODES);
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("Network");
        var item = new JMenuItem("Reset");
        item.addActionListener(a -> { this.net.clearAllEvidence(); this.updateAll(); });
        menu.add(item);
        item = new JMenuItem("Help");
        item.addActionListener(a -> JOptionPane.showMessageDialog(this,
            "Click on the decision and see the changes in the net\n"
            + "The BOLD marked outcomes are the best decision calculated by the model\n"
            + "At the end it will be displayed the expected utility of all the decisions."
        ));
        menu.add(item);
        return menu;
    }
}
