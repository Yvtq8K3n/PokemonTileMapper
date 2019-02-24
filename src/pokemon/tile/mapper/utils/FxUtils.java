/*
 * Copyright 2019 Yvtq8K3n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pokemon.tile.mapper.utils;

import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FxUtils
{

    /** Unfortunately javafx and awt are incompatible
     *  This method converts A javafx Color into awt Color
     * @param c
     * @return
     */
    public static java.awt.Color convertColor(javafx.scene.paint.Color c){
        return new java.awt.Color((float) c.getRed(),
                (float) c.getGreen(),
                (float) c.getBlue(),
                (float) c.getOpacity());
    }

    /** Unfortunately javafx and awt are incompatible
     *  This method converts A awt Color into javafx Color
     * @param c
     * @return
     */
    public static javafx.scene.paint.Color convertColor(java.awt.Color c){
        return Color.rgb(c.getRed(), c.getGreen(), c.getBlue());
    }


    public static String toRGBCode( Color color )
    {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    private static String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }


    /** Unfortunately bufferImaged doesn't extend from Cloneable
     *  so we need to create a hard copy. In order to have make sure
     *  we have new images
     * @param bi
     * @return
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);

        return newImage;
    }


    /** A method that calculate how close a color is
     *  to the human eye
     * @param color1
     * @param color2
     * @return
     */
    public static double colorDistance(Color color1, Color color2)
    {
        long rmean = ( (long)color1.getRed() + (long)color2.getRed() ) / 2;
        long r = (long)color1.getRed() - (long)color2.getRed();
        long g = (long)color1.getGreen() - (long)color2.getGreen();
        long b = (long)color1.getBlue() - (long)color2.getBlue();
        return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
    }
}