package net.berack.upo.ai.problem3;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import net.berack.upo.ai.MyPanel;

/**
 * Classe usata per far vedere il risultato di una run di lw su un network
 * @author Berack
 */
public class LikelihoodWeightingGUI extends MyPanel {

    private LikelihoodWeighting lw = null;
    private OutcomeChartGUI[] chartGUI = null;

    private final JPanel scroll = new JPanel(); // tried using JScrollPane but nothing shows up
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
            this.chartGUI = null;
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

        if(chartGUI == null) buildPanel(nodes);

        var i = 0;
        for(var node : nodes) chartGUI[i++].updateValues(node.values, node.evidence);

        this.invalidate();
        this.validate();
        this.repaint();
    }

    /**
     * Crea il pannello da zero usando i nodi passati in input come valori
     * @param nodes i nodi da mostrare
     */
    private void buildPanel(Collection<NetworkNode> nodes) {
        var panel = new JPanel();
        var layout = new GroupLayout(panel);

        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);

        var gLabel = layout.createParallelGroup();
        var gBarch = layout.createParallelGroup();
        var vGroup = layout.createSequentialGroup();

        this.chartGUI = new OutcomeChartGUI[nodes.size()];
        var i = 0;

        for(var node : nodes) {
            var net = node.net;
            var handle = node.handle;

            var label = new JTextArea(node.name);
            label.setEditable(false);
            label.setLineWrap(true);
            label.setWrapStyleWord(true);
            label.setOpaque(false);
            label.setBorder(BorderFactory.createEmptyBorder());

            var barch = new OutcomeChartGUI(node.outcomes, e -> {
                if(net.isEvidence(handle) && net.getEvidence(handle) == e)
                    net.clearEvidence(handle);
                else net.setEvidence(handle, e);
                this.updateLW();
            });
            this.chartGUI[i++] = barch;

            var font = label.getFont();
            font = new Font(font.getName(), Font.BOLD, font.getSize() + 2);
            label.setFont(font);

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

        var sizes = this.scroll.getPreferredSize();
        sizes.height = panel.getMinimumSize().height;
        sizes.width -= 10;
        panel.setPreferredSize(sizes);

        this.scroll.removeAll();
        this.scroll.add(panel);
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("File");

        var open = new JMenuItem("Open");
        open.addActionListener(action -> this.openFile());

        var nets = new JMenuItem[3];
        nets[0] = new JMenuItem("Alarm net");
        nets[0].addActionListener(action -> this.openFile("lw/Alarm.xdsl"));

        nets[1] = new JMenuItem("WetGrass net");
        nets[1].addActionListener(action -> this.openFile("lw/WetGrass.xdsl"));

        nets[2] = new JMenuItem("Malaria net");
        nets[2].addActionListener(action -> this.openFile("lw/Malaria.xdsl"));

        menu.add(open);
        menu.add(new JSeparator());
        for(var net : nets) menu.add(net);

        return menu;
    }
}
