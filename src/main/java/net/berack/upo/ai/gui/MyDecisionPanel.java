package net.berack.upo.ai.gui;

import java.awt.Component;
import java.awt.Dimension;
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

    public final Network net;
    private final List<NodeGUI> update = new ArrayList<>();

    /**
     * Costruisce il dinamicamente da una rete e da una lista di nodi da mostrare.
     * Il risultato sarà già inserito nel pannello e per vedere i valori bisognerà utilizzare il metodo updateAll()
     * @param net la rete da mostrare
     * @param nodes il sottoinsieme di nodi da mostrare
     */
    protected MyDecisionPanel(Network net, String...nodes) {
        this.net = net;
        var layout = new GroupLayout(this);

        var size = new Dimension(500, 400);
        this.setSize(size);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);

        var factory = new NodeGUIFactory(net, () -> this.updateAll());
        var gLabel = layout.createParallelGroup();
        var gBarch = layout.createParallelGroup();
        var vGroup = layout.createSequentialGroup();

        for(var node: nodes) {
            var handle = net.getNode(node);
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
    private void setFont(Component component) {
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
