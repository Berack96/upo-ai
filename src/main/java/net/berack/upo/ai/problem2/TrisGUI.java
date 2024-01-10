package net.berack.upo.ai.problem2;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import net.berack.upo.ai.gui.MyPanel;

/**
 * Classe che permette di visualizzare graficamente il gioco Tris
 * In questa classe si trova un main che crea una istanza di questa finestra
 * 
 * @author Berack96
 */
public class TrisGUI extends MyPanel {

    /**
     * Caricamento statico delle immagini
     */
    private static final ImageIcon IMAGE_X;
    private static final ImageIcon IMAGE_O;
    private static final ImageIcon IMAGE_X_RED;
    private static final ImageIcon IMAGE_O_RED;
    private static final ImageIcon IMAGE_EMPTY;
    static {
        var loader = TrisGUI.class.getClassLoader();
        IMAGE_X = new ImageIcon(loader.getResource("tris/value_x.png"));
        IMAGE_O = new ImageIcon(loader.getResource("tris/value_o.png"));
        IMAGE_X_RED = new ImageIcon(loader.getResource("tris/value_x_red.png"));
        IMAGE_O_RED = new ImageIcon(loader.getResource("tris/value_o_red.png"));
        IMAGE_EMPTY = new ImageIcon(loader.getResource("tris/value_empty.png"));
    }

    private final MyComponent[][] buttons = new MyComponent[Tris.LENGTH][Tris.LENGTH];
    private Tris tris = new Tris();
    private TrisAi ai = new TrisAi(this.tris);
    private JCheckBoxMenuItem aiFirst;

    /**
     * Crea una nuova istanza del gioco sottoforma di finestra.
     * In essa si potrà giocare premendo sulla zona in cui si vuole mettere il proprio simbolo.
     * Inoltre ci saranno sarà una menubar con delle opzioni tra le quali
     * il resettare il gioco e la possibilità di far giocar una AI.
     * 
     * Come default viene abilitata la AI come secondo giocatore.
     */
    public TrisGUI() {
        super();

        var grid = new GridLayout(Tris.LENGTH, Tris.LENGTH);
        this.setLayout(grid);
        for(var i = 0; i < Tris.LENGTH; i++) {
            for(var j = 0; j < Tris.LENGTH; j++) {
                var comp = new MyComponent(Tris.LENGTH, j, i);
                this.add(comp);
                this.buttons[j][i] = comp;
            }
        }
    }

    /**
     * Resetta il gioco e ne crea uno vuoto
     */
    public void reset() {
        this.tris = new Tris();
        this.ai = this.ai == null? null:new TrisAi(this.tris);
        if(this.ai != null && aiFirst.getState()) this.ai.playNext();
        this.updateAll();
    }

    /**
     * Dopo questo metodo la finestra verrà ridisegnata (sempre se ci sono stati dei cambiamenti)
     * Nel caso in cui ci sia un vincitore il tris verrà colorato di rosso
     */
    @Override
    public void updateAll() {
        for(var arr: this.buttons) {
            for(var button: arr) {
                var value = tris.get(button.x, button.y);
                var newIcon = switch(value) {
                    case VALUE_X -> IMAGE_X;
                    case VALUE_O -> IMAGE_O;
                    case EMPTY -> IMAGE_EMPTY;
                };

                if(!newIcon.equals(button.getIcon()))
                    button.setIcon(newIcon);
            }
        }

        var icon = switch(this.tris.getWinner()) {
            case VALUE_X -> IMAGE_X_RED;
            case VALUE_O -> IMAGE_O_RED;
            default -> null;
        };

        if(icon != null)
            for(var coord : this.tris.getWinnerTris())
                this.buttons[coord[0]][coord[1]].setIcon(icon);
    }

    /**
     * Classe privata usata come appoggio per la gestione dei pulsanti.
     * Qui vengono disabilitate alcune impostazioni base dei bottoni e
     * viene creato un listener per quando viene premuto su di esso.
     */
    private class MyComponent extends JButton {
        final int x;
        final int y;

        private MyComponent(int n, int x, int y) {
            this.x = x;
            this.y = y;

            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.setBackground(Color.WHITE);
            this.setContentAreaFilled(false);
            this.setFocusable(false);

            this.setIcon(IMAGE_EMPTY);
            this.addActionListener(action -> {
                if(tris.isPlayAvailable(x, y) && !tris.isFinished()) {
                    tris.play(x, y);
                    if(ai != null && !tris.isFinished()) ai.playNext();
                    updateAll();
                }
            });
        }
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("Game");
        var item1 = new JMenuItem("Reset");
        item1.addActionListener(action -> this.reset());
        menu.add(item1);

        var separator = new JSeparator();
        menu.add(separator);

        var item2 = new JCheckBoxMenuItem("AI Enabled");
        item2.setSelected(this.ai != null);
        item2.addChangeListener(action -> this.ai = item2.getState()? new TrisAi(this.tris):null);
        menu.add(item2);

        this.aiFirst = new JCheckBoxMenuItem("AI First");
        this.aiFirst.setSelected(false);
        menu.add(this.aiFirst);

        return menu;
    }
}
