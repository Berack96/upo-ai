package net.berack.upo.ai.decision;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.berack.upo.ai.gui.MyDecisionPanel;
import net.berack.upo.ai.problem3.SmileLib;

/**
 * Classe che mostra le decisioni possibili, insieme ai guadagni
 * per la rete Veicolo.xdsl
 * @author Berack
 */
public class VehicleGUI extends MyDecisionPanel {

    private static final String VEICOLO = "Veicolo unrolled.xdsl";
    private static final String[] NODES = new String[] {
        "Sensore_Posizione", "Comando",
        "Sensore_Posizione_1", "Comando_1",
        "Sensore_Posizione_2", "Comando_2",
        "Sensore_Posizione_3", "Comando_3",
        "Sensore_Posizione_4", "Comando_4",
        "Somma_Utilità"
    };

    public VehicleGUI() {
        this.buildPanel(SmileLib.getNetworkFrom(VEICOLO), NODES);
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
            + "At the end it will be displayed the expected utility of all the decisions.\n\n"
            + "NOTE: this model is hard to compute, so after making a decision\n"
            + "it might freeze the window while it's calculating the results"
        ));
        menu.add(item);
        return menu;
    }

}
