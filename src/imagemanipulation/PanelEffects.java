/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PanelEffects.java
 *
 * Created on 15/01/2010, 06:06:41 م
 */
package imagemanipulation;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author eng
 */
public class PanelEffects extends javax.swing.JPanel {

    /** Creates new form PanelEffects */
    public PanelEffects(String imgName) {
        try {
            initComponents();
            JFrame jf = new JFrame();
            jf.setTitle("Image & effects");
            jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JScrollPane jp = new JScrollPane(new AllEffects(imgName));

            jf.add(jp);
            repaint();
            revalidate();
            jf.setSize(700, 600);
            jf.setVisible(true);
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
}
