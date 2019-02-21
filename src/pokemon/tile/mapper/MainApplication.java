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

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.GridView;
import org.controlsfx.control.StatusBar;
import pokemon.tile.mapper.gui.CustomGridCell;

import java.io.File;

import static pokemon.tile.mapper.model.Model.INSTANCE;



public class MainApplication extends Application {

    final FileChooser fileChooser = new FileChooser();
    static GridView<java.awt.Color> myGrid;
    static ImageView generated;
    static StatusBar statusBar;

    @Override
    public void start(Stage stage) {

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) INSTANCE.setOriginalTile(file);


        BorderPane layout = new BorderPane();
        layout.setLeft(leftSide());
        //layout.setCenter(centerSide());
        layout.setCenter(palletDisplay());
        layout.setRight(rightSide());
        layout.setBottom(bottomSide());

        stage.setTitle("Pokemon Tile Mapper");
        stage.getIcons().add(new Image("pokemon/tile/mapper/resources/icon.png"));
        stage.setScene(new Scene(layout, 460, 250));
        stage.show();
    }

    private Node bottomSide() {
        this.statusBar = new StatusBar();
        return statusBar;
    }

    private Node leftSide(){
        ImageView original = new ImageView(SwingFXUtils.toFXImage(INSTANCE.getOriginalTile(), null));

        VBox vBox = new VBox(new Label("Original Tile:"), original);
        vBox.setAlignment(Pos.CENTER);
        return new Group(vBox);
    }

    private Node rightSide(){
        generated = new ImageView();
        generated.setImage(SwingFXUtils.toFXImage(INSTANCE.getTile().getGeneratedImage(), null));
        VBox vBox = new VBox(new Label("Generated Tile:"), generated);
        vBox.setAlignment(Pos.CENTER);
        return new Group(vBox);
    }

    /**
     * Display all the colors contained in the generated image
     * Initialy creates 25x25 blocks and when the Parent Pane is created binds to its dimentions
     *
     * @return VBox vbox
     */
    @Deprecated//Likely gonna remove, but i SHOULD KEEP THE LABEL!
    private Pane centerSide(){
        int ammountColors = INSTANCE.getTile().getColors().size();

        Label label = new Label("Total of colors "+ammountColors+"/16");
        if (ammountColors>16) label.setTextFill(Color.RED);
        else label.setTextFill(Color.BLACK);

        VBox vBox = new VBox(label, label);
        vBox.setPrefSize(50, 50);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setStyle("-fx-border-color: black;");
        gridPane.setPadding(new Insets(0, 0, 0, 5));
        gridPane.setVgap(8); gridPane.setHgap(8);


        int position=0;
        for (java.awt.Color color: INSTANCE.getTile().getColors()) {
            Rectangle rectangle = new Rectangle(25, 25);
            rectangle.widthProperty().bind(gridPane.widthProperty().divide(8).subtract(1));
            rectangle.setFill(FxUtils.convertColor(color));

            rectangle.addEventHandler(MouseEvent.MOUSE_CLICKED , ApplicationController.colorClicked);
            rectangle.addEventHandler(MouseEvent.MOUSE_ENTERED , ApplicationController.hideColor);

            gridPane.add(rectangle, position % 8, position / 8);
            GridPane.setMargin(rectangle, new Insets(-5, -5, -5, -5));
            position++;
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-focus-color: transparent;");

        vBox.getChildren().add(scrollPane);
        vBox.setAlignment(Pos.CENTER_LEFT);
        return vBox;
    }

    /**Creates a GridCell, this will draw the all the colors the image has.
     * Uses external controlsfx library
     * In order for project to run add the following args to the VM:
     * --add-exports=javafx.base/com.sun.javafx=ALL-UNNAMED  --add-exports=javafx.base/com.sun.javafx.collections=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.css=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.cursor=ALL-UNNAMED  --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED  --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED  --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control.skin=ALL-UNNAMED  --add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED --add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
     * @return
     */
    private Node palletDisplay(){
        final ObservableList list = FXCollections.observableArrayList(INSTANCE.getTile().getColors());
        myGrid = new GridView<>(list);
        myGrid.setVerticalCellSpacing(0); myGrid.setHorizontalCellSpacing(0);
        myGrid.cellHeightProperty().set(25); myGrid.cellWidthProperty().set(25);
        myGrid.setCellFactory(gridView -> { return new CustomGridCell(); });

        return myGrid;
    }

    //There is a better tooltip in controlersfx library
    private Node createTooltip() {
        SplitPane sp = new SplitPane();
        final StackPane sp1 = new StackPane(); sp1.getChildren().add(new Button("Button One"));
        final StackPane sp2 = new StackPane(); sp2.getChildren().add(new Button("Button Two"));
        final StackPane sp3 = new StackPane(); sp3.getChildren().add(new Button("Button Three"));
        sp.getItems().addAll(sp1, sp2, sp3);
        sp.setStyle("-fx-border-color: red");
        sp.setDividerPositions(0.3f, 0.6f, 0.9f);
        return sp;

        /*StackPane bubble = new StackPane();
        bubble.setPadding(new Insets(20));
        bubble.setStyle("-fx-background-color: lightblue; -fx-border-color: navy; -fx-border-width: 2px; -fx-shape: \"" + ROUND_BUBBLE + "\";");
        bubble.setEffect(new DropShadow(10, 5, 5, Color.MIDNIGHTBLUE));
        bubble.setStyle("-fx-border-color: red");
        return bubble;*/
    }

    public static void main(String[] args) {
        launch(args);
    }
}