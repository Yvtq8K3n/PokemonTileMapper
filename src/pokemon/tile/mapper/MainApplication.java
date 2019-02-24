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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.GridView;
import org.controlsfx.control.StatusBar;
import pokemon.tile.mapper.gui.InsivibleGridCell;
import pokemon.tile.mapper.gui.PalletGridCell;
import pokemon.tile.mapper.gui.PopOverTooltip;

import java.io.File;

import static pokemon.tile.mapper.model.Model.INSTANCE;



public class MainApplication extends Application {

    private final FileChooser fileChooser = new FileChooser();
    static PopOverTooltip popOver;

    private static ImageView generated;
    private static StatusBar statusBar;

    private static Label paletteAmmount;
    static GridView<java.awt.Color> paletteGrid;
    private static Label invisibleAmmount;
    static GridView<java.awt.Color> invisibleGrid;

    @Override
    public void start(Stage stage) {

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) INSTANCE.setOriginal(file);

        BorderPane layout = new BorderPane();
        layout.setLeft(palletDisplay());
        //layout.setCenter(centerSide());
        layout.setCenter(tabMenu());
        layout.setRight(toolBar());
        layout.setBottom(bottomSide());
        layout.setTop(topSide());

        stage.setTitle("Pokemon Tileset Mapper");
        stage.getIcons().add(new Image("pokemon/tile/mapper/resources/icon.png"));
        stage.setScene(new Scene(layout, 525, 265));
        stage.show();
    }


    /**
     * Creates topSide it includes a MenuBar and a disabled Toolbar
     * @return
     */
    private Node topSide() {
        //Generate Menu
        Menu settings = new Menu("Settings");
        settings.setOnAction(SettingsController.openSettings);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(settings);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar);

        return vBox;
    }

    private Node bottomSide() {
        this.statusBar = new StatusBar();
        return statusBar;
    }

    private Node leftSide(){
        ImageView original = new ImageView(SwingFXUtils.toFXImage(INSTANCE.getOriginal(), null));

        VBox vBox = new VBox(new Label("Original Tileset:"), original);
        vBox.setAlignment(Pos.TOP_CENTER);

        return vBox;
    }

    private Node rightSide(){
        //Creates ImageView
        generated = new ImageView();
        generated.setImage(SwingFXUtils.toFXImage(INSTANCE.getTileset().getScaledGeneratedImage(), null));
        VBox vBox = new VBox(new Label("Generated Tileset:"), generated);
        vBox.setAlignment(Pos.TOP_CENTER);

        return vBox;
    }

    private Node toolBar(){
        //Generate ToolBar
        ToolBar toolBar = new ToolBar();
        toolBar.setOrientation(Orientation.VERTICAL);
        Button copyGeneratedBtn = new Button();
        copyGeneratedBtn.setGraphic(new ImageView("pokemon/tile/mapper/resources/copy-icon.png"));
        copyGeneratedBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putImage(SwingFXUtils.toFXImage(INSTANCE.getTileset().getGeneratedImage(), null));
                clipboard.setContent(content);

            }
        });


        Button convertToTileBtn = new Button();
        convertToTileBtn.setGraphic(new ImageView("pokemon/tile/mapper/resources/transform.png"));
        toolBar.getItems().addAll(copyGeneratedBtn, convertToTileBtn);

        return toolBar;
    }



    /**Creates a GridCell, this will draw the all the colors the image has.
     * Uses external controlsfx library
     * In order for project to run add the following args to the VM:
     * --add-exports=javafx.base/com.sun.javafx=ALL-UNNAMED  --add-exports=javafx.base/com.sun.javafx.collections=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.css=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.cursor=ALL-UNNAMED  --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED  --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED  --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED  --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control.skin=ALL-UNNAMED  --add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED --add-opens=javafx.controls/javafx.scene.control.skin=ALL-UNNAMED
     * @return
     */
    private Node palletDisplay(){
        //Creates paletteAmmount label
        paletteAmmount = new Label();
        int paletteSize = INSTANCE.getTileset().getPaletteColors().size();
        paletteAmmount.setText(String.format("Pallet: %d/16", paletteSize));
        if (paletteSize > 16) paletteAmmount.setTextFill(Color.RED);

        //Creates paletteGrid
        final ObservableList pallet = FXCollections.observableArrayList(INSTANCE.getTileset().getPaletteColors());
        paletteGrid = new GridView<>(pallet);
        paletteGrid.setVerticalCellSpacing(0); paletteGrid.setHorizontalCellSpacing(0);
        paletteGrid.cellHeightProperty().set(25); paletteGrid.cellWidthProperty().set(25);
        paletteGrid.setCellFactory(gridView -> { return new PalletGridCell(); });
        paletteGrid.setPrefSize(225,100);

        //Creates invisibleAmmount label
        invisibleAmmount = new Label();
        int invisibleSize = INSTANCE.getTileset().getInvisibleColors().size();
        invisibleAmmount.setText(String.format("Invisible: %d", invisibleSize));

        //Creates insisibleGrid
        final ObservableList removedPallet = FXCollections.observableArrayList(INSTANCE.getTileset().getInvisibleColors());
        invisibleGrid = new GridView<>(removedPallet);
        invisibleGrid.setVerticalCellSpacing(0); invisibleGrid.setHorizontalCellSpacing(0);
        invisibleGrid.cellHeightProperty().set(25); invisibleGrid.cellWidthProperty().set(25);
        invisibleGrid.setCellFactory(gridView -> { return new InsivibleGridCell(); });

        //Creates Slider
        Slider slider = new Slider();
        slider.setMin(1);
        slider.setMax(3);
        slider.setValue(INSTANCE.getScale());
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Double myDouble = Double.valueOf((Double) newValue);
            Integer val = Integer.valueOf(myDouble.intValue()); // the simple way
            INSTANCE.setScale(val);
            MainApplication.generated.setImage(SwingFXUtils.toFXImage(INSTANCE.getTileset().getScaledGeneratedImage(), null));
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(
                paletteAmmount,
                paletteGrid,
                invisibleAmmount,
                invisibleGrid,
                new Label("Resize:"),
                slider
        );
        vBox.setAlignment(Pos.TOP_LEFT);

        return vBox;
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


    private Node tabMenu(){
        Tab tab = new Tab();
        tab.setText("new tab");
        tab.setContent(new HBox(leftSide(), rightSide()));
        tab.setClosable(false);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(tab);

        return tabPane;
    }

    /**
     * A method that actualizes the view labels after an action occur
     */
    public static void updateView(String statusOperation){
        int paletteSize = INSTANCE.getTileset().getPaletteColors().size();
        paletteAmmount.setText(String.format("Pallet: %d/16", paletteSize));
        if (INSTANCE.getTileset().getPaletteColors().size() > 16) paletteAmmount.setTextFill(Color.RED);
        else paletteAmmount.setTextFill(Color.BLACK);

        MainApplication.statusBar.setText(statusOperation);
        MainApplication.generated.setImage(SwingFXUtils.toFXImage(INSTANCE.getTileset().getScaledGeneratedImage(), null));


        //Creates invisibleAmmount label
        int invisibleSize = INSTANCE.getTileset().getInvisibleColors().size();
        invisibleAmmount.setText(String.format("Invisible: %d", invisibleSize));
    }

    public static void main(String[] args) {
        launch(args);
    }
}