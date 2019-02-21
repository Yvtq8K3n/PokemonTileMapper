package pokemon.tile.mapper.deprecated;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import static pokemon.tile.mapper.model.Model.INSTANCE;


public class MainApplication {

    private JPanel jPanel1;
    final JFileChooser fileChooser = new JFileChooser();
    private JButton tileUpload;


    private final JButton crPopupButton = new JButton("OLA");


    public MainApplication() {
        tileUpload.addActionListener(new ImageUpload());
    }

    private class ImageUpload implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Bitmap", "bmp"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Png", "png"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try{
                    INSTANCE.setOriginalTile(file);
                    displayOriginalTile();
                }catch (Exception ex){
                    System.out.println("Unable to convert file into a bitmap image");
                }

            }
        }
    }



    //Temporary
    public void displayOriginalTile(){
        BufferedImage img = INSTANCE.getOriginalTile();
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img.getWidth(),img.getHeight());
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //Popup p = PopupFactory.getSharedInstance().getPopup(frame, new RoundedPanel(25), 50, 20);

        /*p.show();
        // create a timer to hide the popup later
        Timer t = new Timer(5000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                p.hide();

            }
        });
        t.setRepeats(false);
        t.start();*/
   }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("OptionPane.background", Color.GREEN);

            UIManager.put("Popup.background", Color.YELLOW);
            UIManager.put("Dialog.background", Color.ORANGE);
            UIManager.put("Frame.background", Color.GREEN);
        } catch (Exception e) { }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("MainApplication");
                frame.setTitle("Pokemon Tile Mapper");
                frame.setContentPane(new MainApplication().jPanel1);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

                //Sets window Icon
                //URL url = ClassLoader.getSystemResource("creator/tile/pokemon/resources/icon.png");
                //frame.setIconImage(Toolkit.getDefaultToolkit().createImage(url));
            }
        });
    }
}
