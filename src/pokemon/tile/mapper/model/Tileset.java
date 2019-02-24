/*
 * Copyright 2019 Yvtq8K3n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pokemon.tile.mapper.model;


import pokemon.tile.mapper.ApplicationController;
import pokemon.tile.mapper.utils.UtilsImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static pokemon.tile.mapper.utils.FxUtils.deepCopy;
import static pokemon.tile.mapper.model.Model.INSTANCE;

public class Tileset {
    private BufferedImage bufferedImage;
    private ArrayList<Color> paletteColors;

    private ArrayList<Pair> overwriteColors;
    private BufferedImage generatedImage;
    private ArrayList<Color> invisibleColors;

    Tileset(BufferedImage originalTile) {
        bufferedImage = deepCopy(originalTile);
        generatedImage = deepCopy(originalTile);
        paletteColors = readColors();
        invisibleColors = new ArrayList<>();
        overwriteColors = new ArrayList<>();
    }

    /**
     * Auxiliary method that identifies and adds to a list
     * the paletteColors found in the given tile
     */
    private ArrayList<Color> readColors(){
        paletteColors = new ArrayList<>();
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                Color color = new Color(bufferedImage.getRGB(j, i));
                if (!paletteColors.contains(color)) paletteColors.add(color);
            }
        }
        return paletteColors;
    }

    /**
     * Temporarily hides the color in the generatedImage
     * and doesnt show the other previously removed paletteColors
     * @param invisible
     */
    public void hideColor(Color invisible){
        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGB = new Color(bufferedImage.getRGB(j, i));
                generatedImage.setRGB(j, i , RGB.getRGB());
                if (invisible.equals(RGB) || invisibleColors.contains(RGB))
                    generatedImage.setRGB(j, i , INSTANCE.getInvisible().getRGB());
            }
        }
    }

    /**Removes
     * @param invisible color
     * from the image
     */
    public void removeColor(Color invisible){
        paletteColors.remove(invisible);
        invisibleColors.add(invisible);
        redraw();
    }

    /** Show a previously identified
     * @param invisible color
     */
    public void showColor(Color invisible){
        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGB = new Color(bufferedImage.getRGB(j, i));
                generatedImage.setRGB(j, i , RGB.getRGB());
                if (!RGB.equals(invisible) && invisibleColors.contains(RGB))
                    generatedImage.setRGB(j, i , INSTANCE.getInvisible().getRGB());
            }
        }
    }

    public void addRemovedColor(Color color){
        invisibleColors.remove(color);
        paletteColors.add(color);
        redraw();
    }

    private void redraw(){
        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGB = new Color(bufferedImage.getRGB(j, i));
                generatedImage.setRGB(j, i , RGB.getRGB());
                if (invisibleColors.contains(RGB))
                    generatedImage.setRGB(j, i , INSTANCE.getInvisible().getRGB());
            }
        }
    }

    public void setColorsTranslucide(Color color){
        //value: 50, shift:24 bytes, mask: 0x00ffffff
        int alpha = 50<<24|0x00ffffff;

        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGB = new Color(bufferedImage.getRGB(j,i));
                generatedImage.setRGB(j,i,  RGB.getRGB() & alpha);

                if (color.equals(RGB)) generatedImage.setRGB(j,i,  RGB.getRGB());
            }
        }
    }

    /**
     * Method that shows what color is going to replaced by the
     * @param original to the
     * @param replacing color
     */
    public void replaceShowColor(Color original, Color replacing) {
        //value: 50, shift:24 bytes, mask: 0x00ffffff
        int alpha = 50<<24|0x00ffffff;
        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGB = new Color(bufferedImage.getRGB(j,i));
                generatedImage.setRGB(j, i, RGB.getRGB()&alpha);
                if (RGB.equals(original) || RGB.equals(replacing)) generatedImage.setRGB(j, i, replacing.getRGB());
            }
        }
    }


    public void overwriteColor(Color original, Color replacing) {
        //Tries to remove the color if exists
        overwriteColors.remove(original);
        overwriteColors.add(new Pair(original, replacing));

        for (int i = 0; i < bufferedImage.getHeight(); i++){
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                Color RGB = new Color(bufferedImage.getRGB(j, i));
                bufferedImage.setRGB(j, i, RGB.getRGB());
                for (Pair o : overwriteColors)
                {
                    if (o.getOriginal().equals(RGB)) bufferedImage.setRGB(j, i, o.getReplacement().getRGB());
                }
            }
        }
        paletteColors.remove(original);
        ApplicationController.displayOriginalTile();
    }


    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage getGeneratedImage() {
        return generatedImage;
    }

    public BufferedImage getScaledGeneratedImage() {
        return UtilsImage.resize(generatedImage,
                generatedImage.getWidth() * INSTANCE.getScale(),
                generatedImage.getHeight()  * INSTANCE.getScale()
        );
    }
    public void setGeneratedImage(BufferedImage generatedImage) {
        this.generatedImage = generatedImage;
    }

    public ArrayList<Color> getPaletteColors() {
        return paletteColors;
    }
    public void setPaletteColors(ArrayList<Color> paletteColors) {
        this.paletteColors = paletteColors;
    }

    public ArrayList<Color> getInvisibleColors() {
        return invisibleColors;
    }

    public void setInvisibleColors(ArrayList<Color> invisibleColors) {
        this.invisibleColors = invisibleColors;
    }
}
