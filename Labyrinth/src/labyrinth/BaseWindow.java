package labyrinth;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;

public class BaseWindow extends JFrame {

    /**
     * Kezdőablak beállításai.
     * @param n 
     * Ekkora lesz a játszóablak.
     */
    public BaseWindow(int n) {
        setTitle("Labyrinth");
        setSize(n+100, n);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }   

        });
        setVisible(true);
    }
    
    /**
     * Kilépéskor ez a megerősítő opció ugrik fel.
     */
    private void showExitConfirmation() {
        int n = JOptionPane.showConfirmDialog(this, "Valóban ki akar lépni?",
                "Megerősítés", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            doUponExit();
        }
    }
    
    /**
     * Ez történik amikor kilépünk.
     */
    protected void doUponExit() {
        this.dispose();
    }
}
