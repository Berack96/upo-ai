package net.berack.upo.ai.problem3;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.berack.upo.ai.MyPanel;

/**
 * Classe usata per far vedere il risultato di una run di lw su un network
 * @author Berack
 */
public class LikelihoodWeightingGUI extends MyPanel {

    private LikelihoodWeighting lw = null;

    private final JPanel scroll = new JPanel();
    private final int totalRuns = 1000;

    /**
     * Crea il pannello con gli elementi di default.
     * Siccome non c'è nessun network, il pannello sarà vuoto finchè non si apriranno dei network
     */
    public LikelihoodWeightingGUI() {
        this.scroll.setPreferredSize(new Dimension(500, 400));
        this.add(this.scroll);
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
            this.updateLW();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Esegue l'algoritmo sul network corrente e mostra i risultati
     */
    private void updateLW() {
        this.lw.updateNetwork(totalRuns);

        var nodes = this.lw.getAllNodes();
        var panel = new JPanel();
        var layout = new GroupLayout(panel);

        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);

        var gLabel = layout.createParallelGroup();
        var gBarch = layout.createParallelGroup();
        var vGroup = layout.createSequentialGroup();

        for(var node : nodes) {
            var label = new JLabel(node.name);
            var barch = new OutcomeChartGUI(node, i -> {
                if(node.evidence == i) node.net.clearEvidence(node.handle);
                else node.net.setEvidence(node.handle, i);
                this.updateLW();
            });

            var font = label.getFont();
            label.setFont(new Font(font.getName(), font.getStyle(), font.getSize() + 4));

            gLabel.addComponent(label);
            gBarch.addComponent(barch);
            vGroup.addGroup(layout.createParallelGroup()
                .addComponent(label)
                .addComponent(barch));
        }

        var hGroup = layout.createSequentialGroup();
        hGroup.addGroup(gLabel).addGroup(gBarch);

        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);

        this.scroll.removeAll();
        this.scroll.add(panel);
        this.repaint();

        this.invalidate();
        this.validate();
        this.repaint();
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("File");

        var open = new JMenuItem("Open");
        open.addActionListener(action -> this.openFile());

        var net1 = new JMenuItem("WetGrass net");
        net1.addActionListener(action -> this.openFile("lw/WetGrass.xdsl"));

        var net2 = new JMenuItem("Malaria net");
        net2.addActionListener(action -> this.openFile("lw/Malaria.xdsl"));


        menu.add(open);
        menu.add(new JSeparator());
        menu.add(net1);
        menu.add(net2);

        return menu;
    }
}
