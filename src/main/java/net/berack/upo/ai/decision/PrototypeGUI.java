package net.berack.upo.ai.decision;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
        super(SmileLib.getNetworkFrom(PROTOTIPO), NODES);
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("Network");
        var item = new JMenuItem("Reset");
        item.addActionListener(a -> { this.net.clearAllEvidence(); this.updateAll(); });
        menu.add(item);
        return menu;
    }
}
