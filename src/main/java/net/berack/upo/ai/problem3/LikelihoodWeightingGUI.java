package net.berack.upo.ai.problem3;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import net.berack.upo.ai.gui.MyDecisionPanel;

/**
 * Classe usata per far vedere il risultato di una run di lw su un network
 * @author Berack
 */
public class LikelihoodWeightingGUI extends MyDecisionPanel {

    private LikelihoodWeighting lw = null;
    private int totalRuns = 1000;

    /**
     * Crea il pannello con gli elementi di default.
     * Siccome non c'è nessun network, il pannello sarà vuoto finchè non si apriranno dei network
     */
    public LikelihoodWeightingGUI() {
        var totLabel = new JLabel("Total Runs");
        var totValue = new JComboBox<>(new Integer[] {this.totalRuns, 5 * this.totalRuns, 10 * this.totalRuns, 20 * this.totalRuns});
        totValue.addItemListener(a -> this.totalRuns = (Integer) totValue.getSelectedItem());

        this.setFont(totLabel);
        this.setExtraComponents(new Component[] { totLabel, totValue });
        this.buildPanel(null);

        var label = new JLabel("Use the menù 'File' to open a net");
        this.setLayout(new GridBagLayout());
        this.add(label);
    }

    /**
     * Crea una finestra di dialog per la richiesta di un file.
     * Il file richiesto è in forma .xdsl
     */
    public void openFile() {
        var parent = this.getParent();
        while(parent.getParent() != null) parent = parent.getParent();
        if(!(parent instanceof JFrame)) throw new IllegalArgumentException(parent.getClass().getName());

        var dialog = new FileDialog((JFrame) parent, "Select net");
        dialog.setLocationRelativeTo(null);
        dialog.setFile("*.xdsl");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);

        if(dialog.getFile() != null) this.openFile(dialog.getDirectory() + dialog.getFile());
    }

    /**
     * Carica il file indicato nella finestra, esegue l'algoritmo e mostra i risultati
     * @param fileName il nome del file
     */
    public void openFile(String fileName) {

        try {
            var net = SmileLib.getNetworkFrom(fileName);
            this.lw = new LikelihoodWeighting(net);
            this.buildPanel(net);
            var totLabel = new JLabel("Total Runs");
            var totValue = new JComboBox<>(new Integer[] {this.totalRuns, 5 * this.totalRuns, 10 * this.totalRuns, 20 * this.totalRuns});
            totValue.addItemListener(a -> this.totalRuns = (Integer) totValue.getSelectedItem());
            this.setFont(totLabel);

            this.add(totLabel);
            this.add(totValue);

            this.updateAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Esegue l'algoritmo sul network corrente e mostra i risultati
     */
    @Override
    public void updateAll() {
        if(this.lw == null) return;

        this.lw.updateNetwork(totalRuns);
        for(var node : this.update) {
            var values = this.lw.getNodeValue(node.node);
            node.setValues(values);
            node.updateNode();
        }
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("File");

        var open = new JMenuItem("Open");
        open.addActionListener(action -> this.openFile());
        menu.add(open);
        menu.add(new JSeparator());

        var nets = new JMenuItem[3];
        nets[0] = new JMenuItem("Alarm net");
        nets[0].addActionListener(action -> this.openFile("lw/Alarm.xdsl"));
        nets[1] = new JMenuItem("WetGrass net");
        nets[1].addActionListener(action -> this.openFile("lw/WetGrass.xdsl"));
        nets[2] = new JMenuItem("Malaria net");
        nets[2].addActionListener(action -> this.openFile("lw/Malaria.xdsl"));

        for(var net : nets) menu.add(net);
        menu.add(new JSeparator());

        var item = new JMenuItem("Help");
        item.addActionListener(a -> JOptionPane.showMessageDialog(this,
            "Here you can see the likleihood weighting algoritm.\n"
            + "To do it you have to load a net from the samples provided,\n"
            + "or you can open a custom net.\n\n"
            + "NOTE: this algorithm do not support decision nodes yet"
        ));
        menu.add(item);

        return menu;
    }
}
