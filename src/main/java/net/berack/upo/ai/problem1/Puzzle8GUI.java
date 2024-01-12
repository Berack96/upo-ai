package net.berack.upo.ai.problem1;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import net.berack.upo.ai.gui.MyPanel;
import net.berack.upo.ai.problem1.Puzzle8.Move;

/**
 * Classe che permette di visualizzare graficamente il gioco Puzzle8
 * In questa classe si trova un main che crea una istanza di questa finestra
 * 
 * @author Berack96
 */
public class Puzzle8GUI extends MyPanel {

    /**
     * Carica staticamente le immagini necessarie per giocare al gioco
     */
    private static final ImageIcon[] PUZZLE_ICON = new ImageIcon[Puzzle8.LENGTH * Puzzle8.LENGTH];
    static {
        var loader = Puzzle8GUI.class.getClassLoader();
        for(var i = 0; i < PUZZLE_ICON.length; i++)
            PUZZLE_ICON[i] = new ImageIcon(loader.getResource("puzzle8/Puzzle_" + i + ".png"));
    }

    private final Puzzle8 puzzle = new Puzzle8(Puzzle8.DEFAULT_GOAL);
    private final MyComponent[][] buttons = new MyComponent[Puzzle8.LENGTH][Puzzle8.LENGTH];
    private List<Move> solution = new ArrayList<>();

    /**
     * Crea una nuova istanza del gioco sottoforma di finestra.
     * In essa si potrà giocare muovendo le tessere al posto della tessera vuota.
     * Inoltre ci saranno sarà una menubar con delle opzioni tra le quali
     * il mescolare le tessere e l'evidenziazione della migliore mossa.
     * 
     * Come default viene creato il puzzle "GOAL" ovvero non mescolato.
     */
    public Puzzle8GUI() {
        super();

        var grid = new GridLayout(Puzzle8.LENGTH, Puzzle8.LENGTH);
        grid.setHgap(6);
        grid.setVgap(grid.getHgap());

        this.setLayout(grid);
        for(var i = 0; i < Puzzle8.LENGTH; i++) {
            for(var j = 0; j < Puzzle8.LENGTH; j++) {
                var comp = new MyComponent(Puzzle8.LENGTH, j, i);
                this.add(comp);
                this.buttons[j][i] = comp;
            }
        }
    }

    /**
     * Permette di mescolare le tessere in modo casuale, ma sempre
     * in modo tale che il puzzle possa essere risolto.
     * Dopo questo metodo la finestra verrà aggiornata automaticamente.
     */
    public void shuffleGame() {
        do { this.puzzle.shuffle(); } while(!this.puzzle.isSolvable());
        this.solution.clear();
        this.updateAll();
    }

    /**
     * Risolve il gioco in modo da poter aiutare l'utente nella risoluzione.
     * Dopo questo metodo la finestra verrà aggiornata automaticamente
     * e la mossa migliore possibile verrà evidenziata in rosso.
     */
    public void solveGame() {
        this.solution = this.puzzle.solve();
        this.updateAll();
    }

    /**
     * Dopo questo metodo la finestra verrà ridisegnata (sempre se ci sono stati dei cambiamenti)
     * Nel caso in cui sia stata precedentemente calcolata la soluzione,
     * allora verrà evidenziata una tessera con il colore rosso per indicare
     * la mossa migliore da fare per la risoluzione.
     */
    @Override
    public void updateAll() {
        MyComponent zero = null;

        for(var arr: this.buttons) {
            for(var button: arr) {
                var value = puzzle.get(button.x, button.y);
                var newIcon = PUZZLE_ICON[value];

                button.setContentAreaFilled(false);
                if(value == 0) zero = button;
                if(!button.getIcon().equals(newIcon))
                    button.setIcon(newIcon);
            }
        }

        this.higlightNextMove(zero);
    }

    /**
     * Evidenzia la mossa migliore se disponibile.
     * @param zero il componente con il valore zero
     */
    private void higlightNextMove(MyComponent zero) {
        if(this.solution.size() == 0) return;

        zero = switch(this.solution.get(0)) {
            case UP -> this.buttons[zero.x][zero.y-1];
            case DOWN -> this.buttons[zero.x][zero.y+1];
            case LEFT -> this.buttons[zero.x-1][zero.y];
            case RIGHT -> this.buttons[zero.x+1][zero.y];
        };

        zero.setContentAreaFilled(true);
        zero.setBackground(Color.RED);
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

            this.setBorder(null);
            this.setBackground(Color.WHITE);
            this.setContentAreaFilled(false);
            this.setFocusable(false);

            this.setIcon(PUZZLE_ICON[puzzle.get(x, y)]);
            this.addActionListener(action -> {
                Move move = null;

                if(puzzle.get(x, y) == 0) return;
                else if(x-1 >= 0 && puzzle.get(x-1, y) == 0) move = Move.RIGHT;
                else if(x+1 < n  && puzzle.get(x+1, y) == 0) move = Move.LEFT;
                else if(y-1 >= 0 && puzzle.get(x, y-1) == 0) move = Move.DOWN;
                else if(y+1 < n  && puzzle.get(x, y+1) == 0) move = Move.UP;
                else return;

                puzzle.move(move);
                if(solution.size() > 0 && solution.remove(0) != move)
                    solution.clear();

                updateAll();
            });
        }
    }

    @Override
    public JMenu getMenu() {
        var menu = new JMenu("Game");
        var item = new JMenuItem("Shuffle");
        item.addActionListener(action -> this.shuffleGame());
        menu.add(item);

        item = new JMenuItem("Show solution");
        item.addActionListener(action -> this.solveGame());
        menu.add(item);

        menu.add(new JSeparator());

        item = new JMenuItem("Help");
        item.addActionListener(a -> JOptionPane.showMessageDialog(this,
            "This is a recreation of the game of the 8 tiles\n"
            + "To win the game you must sort the tiles in ascending order\n"
            + "with the empty tile at the end.\n"
            + "The empty space is used to move all the other tiles."
        ));
        menu.add(item);

        return menu;
    }
}
