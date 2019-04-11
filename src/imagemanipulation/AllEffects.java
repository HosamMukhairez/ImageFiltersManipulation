
/*
 * by eng.hosam84@hotmail.com
 */
package imagemanipulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;

/** A demonstration of various image processing filters */
public class AllEffects extends JPanel {

    Image image;
    //static final int WIDTH = 600, HEIGHT = 675; // Size of our example
    int WIDTH = 0, HEIGHT = 0; // Size of our example

    public String getName() {
        return "Image Processing";
    }

    public int getWidth() {
        return WIDTH;
        //return image.getWidth(null) + 100;
    }

    public int getHeight() {
        return HEIGHT;
        //return image.getHeight(null) + 100;
    }

    public void setHieght() {
        HEIGHT = (image.getHeight(null) + 30) * 10;
        System.out.println("HEIGHT:" + HEIGHT);
    }

    public void setWidth() {
        WIDTH = (image.getWidth(null) + 30) * 10;
        System.out.println("WIDTH:" + WIDTH);
    }

    /** This constructor loads the image we will manipulate */
    public AllEffects(String imgName) {
        try {
            /*java.net.URL imageurl = this.getClass().getResource("4.gif");
            image = new javax.swing.ImageIcon(imageurl).getImage();*/
            image = Toolkit.getDefaultToolkit().getImage("images/" + imgName);
            //added new
            MediaTracker tracker = new MediaTracker(this);
            tracker.addImage(image, 0);
            tracker.waitForAll();
            //System.out.println(tracker.isErrorAny());
            //end
            setHieght();
            setWidth();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // These arrays of bytes are used by the LookupImageOp image filters below
    /*static byte[] brightenTable = new byte[256];
    static byte[] thresholdTable = new byte[256];*/
    static byte[] brightenTable = new byte[256];
    static byte[] thresholdTable = new byte[256];

    static { // Initialize the arrays
        for (int i = 0; i < 256; i++) {
            brightenTable[i] = (byte) (Math.sqrt(i / 255.0) * 255);
            thresholdTable[i] = (byte) ((i < 255) ? 0 : i);
        }
    }
    // This AffineTransform is used by one of the image filters below
    static AffineTransform mirrorTransform;

    static { // Create and initialize the AffineTransform
        //mirrorTransform = AffineTransform.getTranslateInstance(127, 0);
        mirrorTransform = AffineTransform.getTranslateInstance(130, 0);
        mirrorTransform.scale(-1.0, 1.0); // flip horizontally
    }
    // These are the labels we'll display for each of the filtered images
    static String[] filterNames = new String[]{"Original", "Gray Scale",
        "Negative", "Brighten (linear)", "Brighten (sqrt)", "Threshold",
        "Blur", "Sharpen", "Edge Detect", "Mirror", "Rotate (center)",
        "Rotate (lower left)"};
    // The following BufferedImageOp image filter objects perform
    // different types of image processing operations.
    BufferedImageOp[] filters = new BufferedImageOp[]{
        // 1) No filter here. We'll display the original image
        null,
        // 2) Convert to Grayscale color space
        new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null),
        // 3) Image negative. Multiply each color value by -1.0 and add 255
        new RescaleOp(-1.0f, 255f, null),
        // 4) Brighten using a linear formula that increases all color
        // values
        new RescaleOp(1.25f, 0, null),
        // 5) Brighten using the lookup table defined above
        new LookupOp(new ByteLookupTable(0, brightenTable), null),
        // 6) Threshold using the lookup table defined above
        new LookupOp(new ByteLookupTable(0, thresholdTable), null),
        // 7) Blur by "convolving" the image with a matrix
        new ConvolveOp(new Kernel(3, 3, new float[]{.1111f, .1111f, .1111f, .1111f, .1111f, .1111f, .1111f, .1111f, .1111f,})),
        // 8) Sharpen by using a different matrix
        new ConvolveOp(new Kernel(3, 3, new float[]{0.0f, -0.75f, 0.0f, -0.75f, 4.0f, -0.75f, 0.0f, -0.75f, 0.0f})),
        // 9) Edge detect using yet another matrix
        new ConvolveOp(new Kernel(3, 3, new float[]{0.0f, -0.75f, 0.0f, -0.75f, 3.0f, -0.75f, 0.0f, -0.75f, 0.0f})),
        // 10) Compute a mirror image using the transform defined above
        new AffineTransformOp(mirrorTransform, AffineTransformOp.TYPE_BILINEAR),
        // 11) Rotate the image 180 degrees about its center point
        //new AffineTransformOp(AffineTransform.getRotateInstance(Math.PI, 64, 95), AffineTransformOp.TYPE_NEAREST_NEIGHBOR),
        new AffineTransformOp(AffineTransform.getRotateInstance(Math.PI, 64, 50), AffineTransformOp.TYPE_NEAREST_NEIGHBOR),
        // 12) Rotate the image 15 degrees about the bottom left
        new AffineTransformOp(AffineTransform.getRotateInstance(Math.PI / 12, 0, 50), AffineTransformOp.TYPE_NEAREST_NEIGHBOR),};

    /** Draw the example */
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        // Create a BufferedImage big enough to hold the Image loaded
        // in the constructor. Then copy that image into the new
        // BufferedImage object so that we can process it.
        BufferedImage bimage = new BufferedImage(image.getWidth(this) + 5, image.getHeight(this) + 5, BufferedImage.TYPE_INT_RGB);
        //BufferedImage bimage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D ig = bimage.createGraphics();
        ig.drawImage(image, 0, 0, this); // copy the image

        // Set some default graphics attributes
        if (getWidth() < 650) {
            g.setFont(new Font("SansSerif", Font.PLAIN, 10)); // 12pt bold text
            g.setColor(Color.BLUE); // Draw in green
            g.translate(10, 10); // Set some margins
        } else {
            g.setFont(new Font("SansSerif", Font.PLAIN, 14)); // 12pt bold text
            g.setColor(Color.BLUE); // Draw in green
            g.translate(10, 10); // Set some margins
        }

        // Loop through the filters
        for (int i = 0; i < filters.length; i++) {
            // If the filter is null, draw the original image, otherwise,
            // draw the image as processed by the filter
            if (filters[i] == null) {
                g.drawImage(bimage, 0, 0, this);
            } else {
                g.drawImage(filters[i].filter(bimage, null), 0, 0, this);
            }
            g.drawString(filterNames[i], 0, image.getHeight(this) + 20); // Label the image
            g.translate(image.getWidth(this) + 50, 0); // Move over
            if (i % 4 == 3) {
                g.translate((0 - (image.getWidth(this) + 50)) * 4, image.getHeight(this) + 50); // Move down after 4
            }
        }
    }

    public void rotate() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}







