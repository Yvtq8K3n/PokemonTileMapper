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

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import pokemon.tile.mapper.gui.InsivibleGridCell;
import pokemon.tile.mapper.gui.PalletGridCell;
import pokemon.tile.mapper.gui.PopOverTooltip;
import pokemon.tile.mapper.gui.ReplaceGridCell;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static pokemon.tile.mapper.model.Model.INSTANCE;
import static pokemon.tile.mapper.MainApplication.updateView;
import static pokemon.tile.mapper.MainApplication.popOver;

public abstract class ApplicationController {

    public static EventHandler<MouseEvent> paletteGridMouseClick = new EventHandler<>() {
        @Override
        public void handle(MouseEvent event) {
            PalletGridCell cell = (PalletGridCell) event.getSource();

            switch (event.getButton()){
                case PRIMARY :
                    //Display/Create popup
                    if (popOver != null){
                        if (cell != popOver.getOwnerNode()) {
                            popOver.hide();
                            popOver = new PopOverTooltip(cell);
                            popOver.show(cell);
                        }else{
                            if(popOver.isShowing()) popOver.hide();
                            else popOver.show(cell);
                        }
                    }else {
                        popOver = new PopOverTooltip(cell);
                        popOver.show(cell);
                    }
                    convertColorsTranslucent(cell.getItem());
                    break;
                case SECONDARY : removeColor(cell.getItem());
                    break;
                default: updateView("Invalid Operation");
            }
        }
    };

    @SuppressWarnings("Duplicates")
    public static EventHandler<MouseEvent> replaceColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            PalletGridCell palletCell = (PalletGridCell) popOver.getOwnerNode();
            ReplaceGridCell replaceCell = (ReplaceGridCell) event.getSource();

            //Retrieves the original and the replace color
            Color original = palletCell.getItem();
            Color replace = replaceCell.getItem();

            if (original.equals(replace)) throw new IllegalArgumentException("SAME COLOR!, change to normal value back later");

            //Apply changes in Model
            INSTANCE.getTileset().overwriteColor(original, replace);

            //Apply changes to the view
            palletCell.setReplaceColor(replace);
            updateView("Replacing Color a whit color b");

            popOver.hide();
        }
    };

    public static EventHandler<MouseEvent> replaceShowSelectedColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            PalletGridCell palletCell = (PalletGridCell) popOver.getOwnerNode();

            Color original = palletCell.getItem();
            Color replaced = palletCell.getReplaced();

            //Changes the second slot color on corresponding grid
            INSTANCE.getTileset().replaceShowColor(original, replaced);
            palletCell.changeReplaceColor(replaced);

            MainApplication.updateView("Changing back");
        }
    };

    public static EventHandler<MouseEvent> addRemovedColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            InsivibleGridCell cell = (InsivibleGridCell) event.getSource();
            Color c = cell.getItem();

            //Call method
            INSTANCE.getTileset().addRemovedColor(c);

            //Expecififv behavior
            //Cannot delete the cell without 1st adding it otherwise the cell will be lost
            MainApplication.paletteGrid.getItems().add(cell.getItem());
            MainApplication.invisibleGrid.getItems().remove(cell.getItem());

            updateView(String.format("Adding back into Palette Color: (%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
        }
    };

    public static EventHandler<MouseEvent> hideColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            PalletGridCell cell = (PalletGridCell) event.getSource();
            Color c = cell.getItem();

            //Call method
            INSTANCE.getTileset().hideColor(c);

            updateView(String.format("Hiding Color: (%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
        }
    };

    public static EventHandler<MouseEvent> showColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            InsivibleGridCell cell = (InsivibleGridCell) event.getSource();
            Color c = cell.getItem();

            //Call method
            INSTANCE.getTileset().showColor(c);

            updateView(String.format("Showing Color: (%d, %d, %d)", c.getRed(), c.getGreen(), c.getBlue()));
        }
    };

    @SuppressWarnings("Duplicates")
    public static EventHandler<MouseEvent> replaceShowColor = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            PalletGridCell palletCell = (PalletGridCell) popOver.getOwnerNode();
            ReplaceGridCell replaceCell = (ReplaceGridCell) event.getSource();

            //Retrieves the original and the replace color
            Color original = palletCell.getItem();
            Color replace = replaceCell.getItem();

            //Applies changes to the generatedImage in the Model
            INSTANCE.getTileset().replaceShowColor(original, replace);

            //Changes the second slot color on corresponding grid
            palletCell.changeReplaceColor(replace);
            updateView(String.format("Replacing Color: (%d, %d, %d)", original.getRed(), original.getGreen(), original.getBlue()));
        }
    };

    public static void removeColor(Color color){
        if (popOver != null && popOver.isShowing()) {
            popOver.hide();
            popOver = null;
        }

        INSTANCE.getTileset().removeColor(color);

        //Cannot delete the cell without 1st adding it otherwise the cell will be lost
        MainApplication.invisibleGrid.getItems().add(color);
        MainApplication.paletteGrid.getItems().remove(color);

        updateView(String.format("Removing Color: (%d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue()));
    }

    /** Convert every other color except
     * @param color
     * to translucent
     */
    public static void convertColorsTranslucent(Color color){
        //Make all other colors translucide!
        INSTANCE.getTileset().setColorsTranslucide(color);

        updateView(String.format("Operation no action yet"));
    }

    @SuppressWarnings("Duplicates")
    public static void displayOriginalTile(){
        BufferedImage img = INSTANCE.getTileset().getBufferedImage();
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
