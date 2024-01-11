package net.berack.upo.ai.gui;

import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JTextArea;

import net.berack.upo.ai.gui.nodes.NodeGUI;
import net.berack.upo.ai.gui.nodes.NodeGUIFactory;
import smile.Network;

/**
 * Classe creata per espandere la classe MyPanel aggiungendo la possibilità di mostrare
 * a schermo una rete e dei suoi nodi passati in input.
 * Il costruttore costruisce automaticamente i nodi indicati ma non ne mostra i valori.
 * Per farlo bisognerà utilizzare il metodo updateAll() appena creato il pannello.
 * NOTA: updateAll() richiama il metodo della rete updateBeliefs() che può essere lento con molti nodi.
 * @author Berack
 */
public abstract class MyDecisionPanel extends MyPanel {

    protected Network net;
    protected final List<NodeGUI> update = new ArrayList<>();
    private Component[] components = new Component[0];

    /**
     * Aggiunge dei componenti nel caso siano necessari che verranno mostrati nella prima parte del pannello
     * @param components i componenti da mostrare (devono essere pari)
     */
    protected void setExtraComponents(Component[] components) {
        if(components.length % 2 != 0) throw new IllegalArgumentException("Must be a an even length array");
        this.components = components;
    }

    /**
     * Costruisce il dinamicamente da una rete e da una lista di nodi da mostrare.
     * Il risultato sarà già inserito nel pannello e per vedere i valori bisognerà utilizzare il metodo updateAll()
     * @param net la rete da mostrare
     * @param nodes il sottoinsieme di nodi da mostrare o (empty / null) per tutti i nodi
     */
    public void buildPanel(Network net, String...nodes) {
        this.net = net;
        if(this.net == null) return;

        if(nodes == null || nodes.length == 0)
            this.buildNodes(this.net.getAllNodes());
        else {
            var handles = new int[nodes.length];
            for(var i = 0; i < nodes.length; i++) handles[i] = this.net.getNode(nodes[i]);
            this.buildNodes(handles);
        }
    }

    /**
     * Costruisce i nodi passati in input nel pannello
     * @param handles i nodi da costruire
     */
    private void buildNodes(int[] handles) {
        this.update.clear();
        this.removeAll();

        var layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);

        var factory = new NodeGUIFactory(net, () -> this.updateAll());
        var gLabel = layout.createParallelGroup();
        var gBarch = layout.createParallelGroup();
        var vGroup = layout.createSequentialGroup();

        for(var i = 0; i < this.components.length; i += 2) {
            var comp1 = this.components[i];
            var comp2 = this.components[i+1];

            gLabel.addComponent(comp1);
            gBarch.addComponent(comp2);
            vGroup.addGroup(layout.createParallelGroup()
                .addComponent(comp1)
                .addComponent(comp2));
        }

        for(var handle : handles) {
            var panel = factory.create(handle);
            update.add(panel);

            var label = new JTextArea(net.getNodeName(handle));
            label.setEditable(false);
            label.setLineWrap(true);
            label.setWrapStyleWord(true);
            label.setOpaque(false);
            label.setBorder(BorderFactory.createEmptyBorder());
            this.setFont(label);

            gLabel.addComponent(label);
            gBarch.addComponent(panel);
            vGroup.addGroup(layout.createParallelGroup()
                .addComponent(label)
                .addComponent(panel));
        }

        var hGroup = layout.createSequentialGroup();
        hGroup.addGroup(gLabel).addGroup(gBarch);

        layout.setVerticalGroup(vGroup);
        layout.setHorizontalGroup(hGroup);
    }

    /**
     * Utile per cambiare il font in BOLD e aumentarne la grandezza
     * @param component il componente da cambiare il font
     */
    protected void setFont(Component component) {
        var font = component.getFont();
        font = new Font(font.getName(), Font.BOLD, font.getSize() + 2);
        component.setFont(font);
    }

    @Override
    public void updateAll() {
        net.updateBeliefs();

        for (var node : update)
            node.updateNode();
    }
}
