/*
 * Copyright 2019 Yvtq8K3n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pokemon.tile.mapper.gui;

import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.GridCell;
import pokemon.tile.mapper.utils.FxUtils;

import java.awt.*;

/**
 * Creates a Cell that extends from Awt.Color
 * By doing this, we need to parse between Awt.Color and javafx.Color
 */
public class AWTColorGridCell extends GridCell<Color> {
    private Rectangle colorRect;
    private static final boolean debug = false;

    AWTColorGridCell() {
        this.getStyleClass().add("color-grid-cell");
        this.colorRect = new Rectangle();
        this.colorRect.setStroke(javafx.scene.paint.Color.BLACK);
        this.colorRect.heightProperty().bind(this.heightProperty());
        this.colorRect.widthProperty().bind(this.widthProperty());
        this.setGraphic(this.colorRect);
    }

    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            this.setGraphic(null);
        } else {
            this.colorRect.setFill(FxUtils.convertColor(item));

            this.setGraphic(this.colorRect);
        }

    }
}