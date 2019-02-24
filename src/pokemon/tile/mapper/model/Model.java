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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public enum Model {
    INSTANCE;
    private BufferedImage original;
    private Tileset tile;
    private BufferedImage generated;
    private Color invisible = Color.YELLOW;
    private int scale = 1;

    public BufferedImage getOriginal() {
        return original;
    }
    public void setOriginal(File file) {
        try {
            this.original = ImageIO.read(file); //if (BufferedImage.TYPE_3BYTE_BGR)
            tile = new Tileset(this.original);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getGenerated() {
        return generated;
    }
    public void setGenerated(BufferedImage generated) {
        this.generated = generated;
    }

    public Color getInvisible() {
        return invisible;
    }
    public void setInvisible(Color invisible) {
        this.invisible = invisible;
    }

    public Tileset getTileset() {
        return tile;
    }
    public void setTile(Tileset tile) {
        this.tile = tile;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
