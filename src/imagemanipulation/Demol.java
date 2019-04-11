/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Demol.java
 *
 * Created on 15/01/2010, 05:15:29 ص
 * by eng.hosam84@hotmail.com
 */
package imagemanipulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author eng
 */
public class Demol extends javax.swing.JPanel implements ChangeListener {

    String imgName[] = null;
    BufferedImage img[] = null;
    String opsName[] = {
        "Threshold", "RescaleOp", "Invert", "Yellow Invert", "3x3 Blur",
        "3x3 Sharpen", "3x3 Edge", "5x5 Edge"};
    BufferedImageOp biop[] = new BufferedImageOp[opsName.length];
    int rescaleFactor = 128;
    float rescaleOffset = 0;
    int low = 100, high = 200;
    int opsIndex, imgIndex;
    BufferedImage bimg;

    /** Creates new form Demol */
    public Demol() {
        try {
            initComponents();

            setImagesName();

            thresholdOp(low, high);
            int i = 1;

            // multiplies pixel value by 1.0f and adds offset of 0.
            biop[i++] = new RescaleOp(1.0f, 0, null);
            byte invert[] = new byte[256];
            byte ordered[] = new byte[256];
            for (int j = 0; j < 256; j++) {
                invert[j] = (byte) (256 - j);
                ordered[j] = (byte) j;
            }

            // used to invert bytes
            biop[i++] = new LookupOp(new ByteLookupTable(0, invert), null);

            // used to invert the the bytes for the red and green components
            byte[][] yellowInvert = new byte[][]{invert, invert, ordered};
            biop[i++] = new LookupOp(new ByteLookupTable(0, yellowInvert), null);

            int dim[][] = {{3, 3}, {3, 3}, {3, 3}, {5, 5}};

            // holds the kernels used with ConvolveOp
            float data[][] = {{0.1f, 0.1f, 0.1f, // 3x3 blur
                    0.1f, 0.2f, 0.1f,
                    0.1f, 0.1f, 0.1f},
                {-1.0f, -1.0f, -1.0f, // 3x3 sharpen
                    -1.0f, 9.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f},
                {0.f, -1.f, 0.f, // 3x3 edge
                    -1.f, 5.f, -1.f,
                    0.f, -1.f, 0.f},
                {-1.0f, -1.0f, -1.0f, -1.0f, -1.0f, // 5x5 edge
                    -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, 24.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f, -1.0f, -1.0f}};
            for (int j = 0; j < data.length; j++, i++) {
                biop[i] = new ConvolveOp(new Kernel(dim[j][0], dim[j][1], data[j]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImagesName() {
        try {
            File f = new File("images/");
            String names[] = f.list();
            imgName = new String[names.length];
            img = new BufferedImage[names.length];
            int i = 0;
            while (i < names.length) {
                imgName[i] = names[i];
                i++;
            }

            //===
            setBackground(Color.WHITE);
            for (i = 0; i < imgName.length; i++) {
                Image image = Toolkit.getDefaultToolkit().getImage("images/" + imgName[i]);

                MediaTracker tracker = new MediaTracker(this);
                tracker.addImage(image, 0);
                tracker.waitForAll();

                int iw = image.getWidth(null) + 30;
                int ih = image.getHeight(null) + 30;
                img[i] = new BufferedImage(iw, ih, BufferedImage.SCALE_SMOOTH);
                img[i].createGraphics().drawImage(image, 0, 0, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void thresholdOp(int low, int high) {
        byte threshold[] = new byte[256];
        for (int j = 0; j < 256; j++) {
            if (j > high) {
                threshold[j] = (byte) 255;
            } else if (j < low) {
                threshold[j] = (byte) 0;
            } else {
                threshold[j] = (byte) j;
            }
        }
        biop[0] = new LookupOp(new ByteLookupTable(0, threshold), null);
    }

    // filters the image with the selected filter and draws it
    public void drawDemo(int w, int h, Graphics2D g2) {
        try {
            int iw = img[imgIndex].getWidth(null);
            int ih = img[imgIndex].getHeight(null);
            BufferedImage bimg = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB);
            biop[opsIndex].filter(img[imgIndex], bimg);
            g2.drawImage(bimg, 0, 0, w, h, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Graphics2D createGraphics2D(int w, int h) {
        Graphics2D g2 = null;
        try {
            if (bimg == null || bimg.getWidth() != w || bimg.getHeight() != h) {
                bimg = (BufferedImage) createImage(w, h);
            }
            g2 = bimg.createGraphics();
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, w, h);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            return g2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return g2;
    }

    public void paint(Graphics g) {
        try {
            Dimension d = getSize();
            Graphics2D g2 = createGraphics2D(d.width, d.height);
            drawDemo(d.width, d.height, g2);
            g2.dispose();
            g.drawImage(bimg, 0, 0, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void stateChanged(ChangeEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
