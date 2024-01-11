package net.berack.upo.ai.decision;

import javax.swing.JMenu;

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
        return null;
    }

}
