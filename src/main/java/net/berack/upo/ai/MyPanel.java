package net.berack.upo.ai;

import javax.swing.JMenu;
import javax.swing.JPanel;

/**
 * Classe utilizzata per far si che tutti i frame della finestra abbiano lo stesso metodo
 * per la generazione dei menu.
 * @author Berack
 */
public abstract class MyPanel extends JPanel {

    /**
     * Crea un menu da aggiungere alla lista, in modo che appaia solamente quando
     * la finestra venga utilizzata.
     * Questo metodo viene usato solo dalla finestra MainGUI
     * @return il menu contestuale da aggiungere
     */
    abstract public JMenu getMenu();
}
