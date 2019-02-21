/*
 * Copyright 2019 Yvtq8K3n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pokemon.tile.mapper;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import pokemon.tile.mapper.gui.CustomGridCell;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import static pokemon.tile.mapper.model.Model.INSTANCE;


public abstract class ApplicationController {

    public static EventHandler<MouseEvent> colorClicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CustomGridCell cell = (CustomGridCell) event.getSource();
            Color c = cell.getItem();


            INSTANCE.getTile().removeColor(c);
            MainApplication.statusBar.setText(String.format("Removing Color: (%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
            MainApplication.generated.setImage(SwingFXUtils.toFXImage(INSTANCE.getTile().getGeneratedImage(), null));
            MainApplication.myGrid.getItems().remove(cell.getItem());
        }
    };

    public static EventHandler<MouseEvent> hideColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            CustomGridCell cell = (CustomGridCell) event.getSource();
            Color c = cell.getItem();


            INSTANCE.getTile().hideColor(c);
            MainApplication.statusBar.setText(String.format("Hiding Color: (%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
            MainApplication.generated.setImage(SwingFXUtils.toFXImage(INSTANCE.getTile().getGeneratedImage(), null));
        }
    };

    public static void displayOriginalTile(){
        BufferedImage img = INSTANCE.getTile().getGeneratedImage();
        ImageIcon icon=new ImageIcon(img);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(img.getWidth(),img.getHeight());
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
