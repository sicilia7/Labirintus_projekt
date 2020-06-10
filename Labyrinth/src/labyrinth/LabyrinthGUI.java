package labyrinth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class LabyrinthGUI extends BaseWindow {

    private boolean madeInsert = false;
    private final JPanel rightPanel = new JPanel(new GridLayout(4,1));
    private final JPanel mainPanel = new JPanel();
    private final JLabel stepLabel = new JLabel("Illeszd be a mezőt!");
    private JLabel playerLabel;
    private JLabel treasureLabel;
    private final JLabel errorLabel = new JLabel("");
    private Model model;
    
    private ActionListener startAction;
    private ActionListener endAction;
    private ActionListener insertAction;
    private ActionListener rotateAction;
    private ActionListener stepAction;

    /**
     * Labyrinth játék grafikus felületének beállítása.
     * @param model 
     * Ezt a modelt használja a gui.
     */
    public LabyrinthGUI(Model model) {
        super(800);
        hookActionListeners();
        this.model= model;
        getContentPane().setLayout(new BorderLayout());
        setPageStart();
        setBoard();
        setRightPanel();
    }
    
    /**
     * Kezdő panel beállítása.
     */
    private void setPageStart() {
        JPanel startPanel = new JPanel(new FlowLayout());
        playerLabel = new JLabel("Aktuális játékos: " + (model.getPlayer()+1));
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        Font font2 = new Font(Font.SANS_SERIF, Font.ITALIC, 10);
        JButton newButton = new JButton("Új játék");
        JButton endButton = new JButton("Kör befejezése");
        newButton.addActionListener(startAction);
        endButton.addActionListener(endAction);

        playerLabel.setFont(font); playerLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        stepLabel.setFont(font2); stepLabel.setBorder(BorderFactory.createEmptyBorder(0,15,0,15));
        startPanel.add(newButton, FlowLayout.LEFT);
        startPanel.add(endButton, FlowLayout.CENTER);
        startPanel.add(stepLabel, FlowLayout.RIGHT);
        startPanel.add(playerLabel, FlowLayout.RIGHT);
        startPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(startPanel, BorderLayout.PAGE_START);
    }
        
    /**
     * Mező tulajdonságainak beállítása.
     */
    private void setBoard(){
        mainPanel.removeAll();
        mainPanel.setLayout(new GridLayout(7, 7));

        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < 7; ++j) {
                addButton(mainPanel, i, j);
            }
        }
        mainPanel.setBackground(Color.white);
        mainPanel.setPreferredSize(new Dimension(670,670));
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        revalidate();
    }
    
    /**
     * Információs panel beállításai.
     */
    private void setRightPanel(){
        rightPanel.removeAll();
        JPanel rotation = new JPanel(new GridLayout(3,1));
        JPanel treasure = new JPanel(new GridLayout(2,1));
        JButton mezo = new JButton();
        JButton btn1 = new JButton();btn1.addActionListener(rotateAction);
        JButton btn2 = new JButton();btn2.addActionListener(rotateAction);
        JButton btn3 = new JButton();btn3.addActionListener(rotateAction);
        
        BufferedImage combined = createImage(model.getMissing());
        mezo.setIcon(new ImageIcon(combined));
        mezo.setBorder(BorderFactory.createEmptyBorder());
        mezo.setContentAreaFilled(false);
        BufferedImage treasureImg = null;
        try {
            treasureImg = ImageIO.read(getClass().getResource("/resources/" + 
                    model.getNextTreasure(model.getPlayer())+ ".png"));
        } catch (IOException ex) {
            Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        treasureLabel = new JLabel(new ImageIcon(treasureImg));
        errorLabel.setForeground(Color.red);
        
        btn1.setText("90° jobbra");
        btn2.setText("90° balra");
        btn3.setText("180°");
        rotation.add(btn1);rotation.add(btn2);rotation.add(btn3);
        treasure.add(new JLabel("A következő kincs:"));
        treasure.add(treasureLabel);
        rightPanel.add(mezo);rightPanel.add(rotation);
        rightPanel.add(treasure);rightPanel.add(errorLabel);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10,35,10,35));
        getContentPane().add(rightPanel, BorderLayout.EAST);
        revalidate();
    }
    
    /**
     * Panelünkhöz egy gombot adhatunk hozzá
     * @param panel
     * Melyik panel
     * @param i
     * mező sora
     * @param j 
     * mező oszlopa
     */
    private void addButton(JPanel panel, final int i, final int j) {
        final JButton button = new JButton();
        Field field= model.getField(i,j);
        BufferedImage combined = createImage(field);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
        button.setIcon(new ImageIcon(combined));
        button.addActionListener(insertAction);
        button.addActionListener(stepAction);
        panel.add(button);
    }
    
    /**
     * Mező kinézeteinek beállítása
     * @param field
     * Melyik mezőt szeretnénk beállítani
     * @return 
     * Képeinkkel tér vissza.
     */
    private BufferedImage createImage(Field field){
        BufferedImage combined = null;
        try {
          BufferedImage img = ImageIO.read(getClass().getResource("/resources/" + getImage(field)+ ".png"));
          combined = img;
          if(field.getTreasure() != -1) {
            BufferedImage overlay = ImageIO.read(getClass().getResource("/resources/" + field.getTreasure() + ".png"));
            combined = new BufferedImage(img.getHeight()+1,img.getWidth()+1,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = combined.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.drawImage(overlay, img.getWidth()/2-overlay.getWidth()/2, 
                    img.getHeight()/2-overlay.getHeight()/2, null);
            g.dispose();
          }
            for (int i = 0; i < model.getMaxPlayers(); i++) {
                if(model.getField(model.posI.get(i),model.posJ.get(i)).equals(field)){
                    BufferedImage playerImage = ImageIO.read(getClass().getResource(
                            "/resources/player" + i + ".png"));
                    Graphics2D g = combined.createGraphics();
                    g.drawImage(combined, 0, 0, null);
                    g.drawImage(playerImage, combined.getWidth()/2-playerImage.getWidth()/2, 
                            combined.getHeight()/2-playerImage.getHeight()/2, null);
                    g.dispose();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return combined;
    }
    
    /**
     * Milyen képet kell raknunk az adott mezőre?
     * @param field
     * Erre a mezőre
     * @return 
     * Az ilyen beállítású képet.
     */
    private String getImage(Field field){
        if (field.isUp()) {
            if (field.isDown()) {
                if (field.isRight()) {
                    if (field.isLeft()) {
                        return "udlr";
                    }
                    return "udr";
                }
                if (field.isLeft()) {
                    return "udl";
                }
                return "ud";
            }
            if (field.isLeft()) {
                if (field.isRight()) {
                    return "ulr";
                }
                return "ul";
            }
            return "ur";
        }
        if (field.isDown()) {
            if (field.isLeft()) {
                if (field.isRight()) {
                    return "dlr";
                }
                return "dl";
            }
            return "dr";
        }
        return "lr";
    }
    
    /**
     * A bekövetkező akciókat kezeli
     */
    private void hookActionListeners() {
        startAction = (ActionEvent e) -> {
            madeInsert = false;
            errorLabel.setText("");
            String size = JOptionPane.showInputDialog(null, "Játékosok száma: ",
                    "Új játék", JOptionPane.QUESTION_MESSAGE);
            this.model = new Model(Integer.parseInt(size));
            stepLabel.setText("Illeszd be a mezőt!");
            playerLabel.setText("Aktuális játékos: " + (model.getPlayer()+1));
            setBoard();
            setRightPanel();
        };
        endAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(model.getWinner() < 0){
                    madeInsert = false;
                    errorLabel.setText("");
                    LabyrinthGUI.this.model.endTurn();
                    stepLabel.setText("Illeszd be a mezőt!");
                    playerLabel.setText("Aktuális játékos: " + (model.getPlayer()+1));
                    try {
                        treasureLabel.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/" +
                                model.getNextTreasure(model.getPlayer())+ ".png"))));
                    } catch (IOException ex) {
                        Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        rotateAction = (ActionEvent e) -> {
            errorLabel.setText("");
            int i = 0;
            JPanel panel = (JPanel) rightPanel.getComponent(1);
            while(!panel.getComponent(i).equals(e.getSource())){
                i++;
            }
            switch(i){
                case 0: model.rotate(1);
                        break;
                case 1: model.rotate(3);
                        break;
                case 2:model.rotate(2);
                        break;
            }
            setRightPanel();
        };
        stepAction = (ActionEvent e) -> {
            errorLabel.setText("");  
            int i = getSourceNum(e);
            int posi = model.posI.get(model.getPlayer());
            int posj = model.posJ.get(model.getPlayer());
            if(madeInsert && i != (7*posi+posj)){
                if(i%7 == posj){
                    if( i/7 < posi){
                        model.movePlayer(0);
                    }else{
                        model.movePlayer(2);
                    }
                }else if(i/7 == posi){
                    if(i%7 < posj){
                        model.movePlayer(3);
                    }else{
                        model.movePlayer(1);
                    }
                }else{
                    errorLabel.setText("Érvénytelen lépés!");
                }
                setBoard();
                
                if(model.getWinner() != -1){
                    for (Component component : mainPanel.getComponents()) {
                        component.setEnabled(false);
                    }
                    JPanel panel = (JPanel) rightPanel.getComponent(1);
                    for (Component component : panel.getComponents()) {
                        component.setEnabled(false);
                    }
                    JOptionPane.showMessageDialog(null, model.getWinner()+1 + ". játékos nyert!", "Gratulálunk!", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    try {
                        treasureLabel.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/" +
                                model.getNextTreasure(model.getPlayer())+ ".png"))));
                    } catch (IOException ex) {
                        Logger.getLogger(LabyrinthGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        insertAction = (ActionEvent e) -> {
            if(!madeInsert){
                errorLabel.setText("");
                int dir = -1;
                int i = getSourceNum(e);
                if(i < 7){
                    dir = 0;
                }else if(i > 41){
                    dir = 2;
                    i = i % 7;
                }else if(i % 7 == 6){
                    dir = 1;
                    i = i/7;
                }else if(i %7 == 0){
                    dir = 3;
                    i = i/7;
                }
                if(model.insert(dir, i)){
                    stepLabel.setText("Mozogj a bábuval!");
                    madeInsert = true;
                    setBoard();
                    setRightPanel();
                }else{
                    errorLabel.setText("Érvénytelen lépés!");
                }
            }
        };
    }
    
    /**
     * Akciófigyelő
     * @param e
     * Milyen esemény keletkezett.
     * @return 
     * A forrás komponens sorszáma
     */
    private int getSourceNum(ActionEvent e){
        int i = 0;
        while(!mainPanel.getComponent(i).equals(e.getSource())){
            i++;
        }
        return i;
    }
}