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


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static pokemon.tile.mapper.FxUtils.deepCopy;
import static pokemon.tile.mapper.model.Model.INSTANCE;

public class Tile {
    private BufferedImage bufferedImage;
    private ArrayList<Color> colors;

    private BufferedImage generatedImage;


    public Tile(BufferedImage originalTile) {
        bufferedImage = deepCopy(originalTile);
        generatedImage = deepCopy(originalTile);
        readColors();
    }

    private void readColors(){
        colors = new ArrayList<>();
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                Color color = new Color(bufferedImage.getRGB(j, i));
                if (!colors.contains(color)) colors.add(color);
            }
        }
    }


    /**
     * Temporarly hides the color, in the generatedImage
     * @param temporaryInsisible
     */
    public void hideColor(Color temporaryInsisible){
        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGBA = new Color(generatedImage.getRGB(j, i));
                generatedImage.setRGB(j, i ,bufferedImage.getRGB(j, i));
                if (temporaryInsisible.equals(RGBA)) generatedImage.setRGB(j, i , INSTANCE.getInvisible().getRGB());
            }
        }
    }

    /**Removes the selected color and generate a new tile
     * @param
     */
    public void removeColor(Color color){
        colors.remove(color);

        for (int i = 0; i < generatedImage.getHeight(); i++) {
            for (int j = 0; j < generatedImage.getWidth(); j++) {
                Color RGBA = new Color(generatedImage.getRGB(j, i));
                if (color.equals(RGBA)) bufferedImage.setRGB(j, i , INSTANCE.getInvisible().getRGB());
                else bufferedImage.setRGB(j, i , bufferedImage.getRGB(j, i));
            }
        }
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

    public void setGeneratedImage(BufferedImage generatedImage) {
        this.generatedImage = generatedImage;
    }

    public ArrayList<Color> getColors() {
        return colors;
    }

    public void setColors(ArrayList<Color> colors) {
        this.colors = colors;
    }
}
