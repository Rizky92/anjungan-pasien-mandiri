/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package khanzahmsanjungan;

import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author it-rsib
 */
public class HalamanUtamaAntrian extends javax.swing.JFrame {

    private static HalamanUtamaAntrian myInstance;

    /**
     * Creates new form HalamanUtamaDepan
     */
    public HalamanUtamaAntrian() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(super.getClass().getResource("/picture/indriati48.png")).getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel39 = new widget.Label();
        jPanel1 = new widget.Panel();
        btnAdmin2 = new widget.ButtonBig();
        btnAdmin3 = new widget.ButtonBig();
        btnAdmin4 = new widget.ButtonBig();
        btnAdmin10 = new widget.ButtonBig();
        btnAdmin11 = new widget.ButtonBig();
        btnAdmin12 = new widget.ButtonBig();
        jPanel2 = new javax.swing.JPanel();
        PanelWall = new usu.widget.glass.PanelGlass();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ANJUNGAN PASIEN MANDIRI");
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel3.setBackground(new java.awt.Color(238, 238, 255));
        jPanel3.setForeground(new java.awt.Color(238, 238, 255));

        jLabel39.setForeground(new java.awt.Color(0, 131, 62));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("ANJUNGAN PASIEN MANDIRI");
        jLabel39.setFont(new java.awt.Font("Inter", 1, 48)); // NOI18N
        jLabel39.setPreferredSize(new java.awt.Dimension(750, 75));
        jPanel3.add(jLabel39);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel1.setBackground(new java.awt.Color(238, 238, 255));
        jPanel1.setBorder(null);
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 1024));
        jPanel1.setLayout(new java.awt.GridLayout(3, 0));

        btnAdmin2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/onlineadmisi.png"))); // NOI18N
        btnAdmin2.setText("ADMISI (Appointment)");
        btnAdmin2.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin2.setIconTextGap(0);
        btnAdmin2.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin2ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin2);

        btnAdmin3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/offlineadmisi.png"))); // NOI18N
        btnAdmin3.setText("ADMISI (Walk In)");
        btnAdmin3.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin3.setIconTextGap(0);
        btnAdmin3.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin3ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin3);

        btnAdmin4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/bedpasien.png"))); // NOI18N
        btnAdmin4.setText("ADMISI (Rawat Inap & IGD)");
        btnAdmin4.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin4.setIconTextGap(0);
        btnAdmin4.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin4ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin4);

        btnAdmin10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/customerservice.png"))); // NOI18N
        btnAdmin10.setText("CUSTOMER SERVICE");
        btnAdmin10.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin10.setIconTextGap(0);
        btnAdmin10.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin10ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin10);

        btnAdmin11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/farmasi.png"))); // NOI18N
        btnAdmin11.setText("FARMASI (Resep Racikan)");
        btnAdmin11.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin11.setIconTextGap(0);
        btnAdmin11.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin11ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin11);

        btnAdmin12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/farmasi.png"))); // NOI18N
        btnAdmin12.setText("FARMASI (Non Racikan)");
        btnAdmin12.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin12.setIconTextGap(0);
        btnAdmin12.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin12ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin12);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(238, 238, 255));
        jPanel2.setForeground(new java.awt.Color(238, 238, 255));

        PanelWall.setBackground(new java.awt.Color(238, 238, 255));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/indriatikars.png"))); // NOI18N
        PanelWall.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall.setForeground(new java.awt.Color(238, 238, 255));
        PanelWall.setPreferredSize(new java.awt.Dimension(500, 150));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(238, 238, 255));
        PanelWall.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelWallMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PanelWallLayout = new javax.swing.GroupLayout(PanelWall);
        PanelWall.setLayout(PanelWallLayout);
        PanelWallLayout.setHorizontalGroup(
            PanelWallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        PanelWallLayout.setVerticalGroup(
            PanelWallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel2.add(PanelWall);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdmin2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin2ActionPerformed
        DlgAmbilAntrean pilih = new DlgAmbilAntrean(null, true);
        pilih.setNoAntrian("ANTRIAN BOOKING", "C", "Loket by Appointment");
        pilih.setSize(this.getWidth(), this.getHeight());
        pilih.setLocationRelativeTo(this);
        pilih.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAdmin2ActionPerformed

    private void btnAdmin3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin3ActionPerformed
        DlgAmbilAntrean pilih = new DlgAmbilAntrean(null, true);
        pilih.setNoAntrian("ANTRIAN WALKIN", "A", "Loket");
        pilih.setSize(this.getWidth(), this.getHeight());
        pilih.setLocationRelativeTo(this);
        pilih.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAdmin3ActionPerformed

    private void btnAdmin4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin4ActionPerformed
        DlgAmbilAntrean pilih = new DlgAmbilAntrean(null, true);
        pilih.setNoAntrian("ANTRIAN RANAP/IGD", "D", "Ranap");
        pilih.setSize(this.getWidth(), this.getHeight());
        pilih.setLocationRelativeTo(this);
        pilih.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAdmin4ActionPerformed

    private void btnAdmin10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin10ActionPerformed
        DlgAmbilAntrean pilih = new DlgAmbilAntrean(null, true);
        pilih.setNoAntrian("CUSTOMER SERVICE", "B", "CS");
        pilih.setSize(this.getWidth(), this.getHeight());
        pilih.setLocationRelativeTo(this);
        pilih.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAdmin10ActionPerformed

    private void btnAdmin11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdmin11ActionPerformed

    private void btnAdmin12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdmin12ActionPerformed

    private void PanelWallMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelWallMouseClicked
        this.dispose();
//        HalamanUtamaDepan kembali = new HalamanUtamaDepan();
//        kembali.setSize(this.getWidth(), this.getHeight());
//        kembali.setLocationRelativeTo(this);
//        kembali.setVisible(true);
    }//GEN-LAST:event_PanelWallMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaAntrian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HalamanUtamaAntrian().setVisible(true);
            }
        });
    }

    public static HalamanUtamaAntrian getInstance() {
        if (myInstance == null) {
            myInstance = new HalamanUtamaAntrian();
        }

        return myInstance;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private usu.widget.glass.PanelGlass PanelWall;
    private widget.ButtonBig btnAdmin10;
    private widget.ButtonBig btnAdmin11;
    private widget.ButtonBig btnAdmin12;
    private widget.ButtonBig btnAdmin2;
    private widget.ButtonBig btnAdmin3;
    private widget.ButtonBig btnAdmin4;
    private widget.Label jLabel39;
    private widget.Panel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
