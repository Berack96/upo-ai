package net.berack.upo.ai;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import net.berack.upo.ai.decision.PrototypeGUI;
import net.berack.upo.ai.decision.VehicleGUI;
import net.berack.upo.ai.problem1.Puzzle8GUI;
import net.berack.upo.ai.problem2.TrisGUI;
import net.berack.upo.ai.problem3.LikelihoodWeightingGUI;

/**
 * Classe che rappresenta il main di tutto il progetto
 * In essa si può navigare in tutti gli esercizi e testarli
 * @author Berack
 */
public class MainGUI extends JFrame {

    public final Puzzle8GUI Puzzle8GUI = new Puzzle8GUI();
    public final TrisGUI TrisGUI = new TrisGUI();
    public final LikelihoodWeightingGUI LikelihoodWeightingGUI = new LikelihoodWeightingGUI();
    public final PrototypeGUI PrototypeGUI = new PrototypeGUI();
    public final VehicleGUI VehicleGUI = new VehicleGUI();

    public static void main(String[] args) {
        new MainGUI();
    }

    private final JMenuBar menuBar = new JMenuBar();

    /**
     * Crea una finestra con nulla da mostrare, ma si può visualizzare uno degli esercizi tramite
     * la barre dei menù che permette di cambiare da un esercizio all'altro
     */
    private MainGUI() {
        super("Progetto per AI");
        this.buildMenu();
        this.setJMenuBar(menuBar);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Crea la barra dei menu aggiungendo tutti i menù passati in input
     * @param menus una lista di menù da aggiungere
     */
    private void buildMenu(JMenu...menus) {
        menuBar.removeAll();
        var menu = new JMenu("View");

        var puzzle = new JMenuItem("Puzzle8 Game");
        puzzle.addActionListener(action -> this.setPanel(Puzzle8GUI));
        var tris = new JMenuItem("Tris Game");
        tris.addActionListener(action -> this.setPanel(TrisGUI));
        var lw = new JMenuItem("Likelihood Weighting");
        lw.addActionListener(action -> this.setPanel(LikelihoodWeightingGUI));
        var prototype = new JMenuItem("Prototype net");
        prototype.addActionListener(action -> this.setPanel(PrototypeGUI));
        var vehicle = new JMenuItem("Vehicle net");
        vehicle.addActionListener(action -> this.setPanel(VehicleGUI));

        menu.add(puzzle);
        menu.add(tris);
        menu.add(lw);
        menu.add(new JSeparator());
        menu.add(prototype);
        menu.add(vehicle);

        menuBar.add(menu);
        for(var m : menus) menuBar.add(m);
    }

    /**
     * Cambia il pannello principale con quello passato in input
     * @param panel il pannello da mostrare
     */
    private void setPanel(MyPanel panel) {
        this.setContentPane(panel);
        this.buildMenu(panel.getMenu());
        this.pack();
        this.invalidate();
        this.validate();
        this.repaint();
    }
}
