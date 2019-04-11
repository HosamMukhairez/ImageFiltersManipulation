package imagemanipulation;

/*
 * @(#)ImageOps.java	1.6	98/12/03
 * by eng.hosam84@hotmail.com
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The ImageOps class displays images drawn using operators such as 
 * ConvolveOp LowPass & Sharpen, LookupOp and RescaleOp.
 */
public class ImageOps_temp extends JApplet {

    Demol demo;

    public void init() {
        this.setSize(500, 500);
        this.setBackground(Color.WHITE);
        this.setLocation(250, 250);

        getContentPane().add(demo = new Demol());
        getContentPane().add("South", new DemoControls(demo));
        this.repaint();
    }

    /**
     * The Demo class renders the selected image with the selected
     * Image Operation filter.
     */
    /**
     * The DemoControls class provides controls for selecting the imaging
     * operation, the image to be filtered and the ranges to be used in
     * the filtering operation.  When rescaling is selected, slider1 
     * controls the rescale factor and slider2 controls the rescale
     * offset.  When thresholding is selected, slider1 controls the
     * low range and slider2 controls the high range of the pixel byte
     * value.  Since the demo only offers two sliders that control a 
     * single byte array of color components, thresholding the gray scale
     * boat.gif image is more practical.  To better demonstrate the use of
     * thresholding with RGB images, six sliders, representing the low and
     * the high range for each color component, would be needed.  If an
     * imaging operation besides rescaling or thresholding is selected,
     * both sliders are disabled.
     */
    //static class DemoControls extends JPanel implements ActionListener, Runnable {
    public static void main(String argv[]) {
        try {
            final ImageOps_temp ops = new ImageOps_temp();
            ops.init();
            JFrame f = new JFrame("Image Manipulation");
            f.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            f.getContentPane().add("Center", ops);
            f.pack();
            f.setSize(new Dimension(720, 700));
            f.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
