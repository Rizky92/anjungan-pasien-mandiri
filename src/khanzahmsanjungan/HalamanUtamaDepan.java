/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package khanzahmsanjungan;

import fungsi.sekuel;
import fungsi.validasi;
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
public class HalamanUtamaDepan extends javax.swing.JFrame {

    private static HalamanUtamaDepan myInstance;
    
    private final DlgCekNoRM pendaftaranwalkin = new DlgCekNoRM(null, true);
    private final DlgCekKunjunganPertamaSEP sepkunjunganpertama = new DlgCekKunjunganPertamaSEP(null, true);
    private final DlgCekSKDPKontrol sepkontrol = new DlgCekSKDPKontrol(null, true);
    private final DlgCekKunjunganBedaPoliSEP sepkontrolbedapoli = new DlgCekKunjunganBedaPoliSEP(null, true);
    private final DlgCekinMobileJKN cekinmobilejkn = new DlgCekinMobileJKN(null, true);

    /**
     * Creates new form HalamanUtamaDepan
     */
    public HalamanUtamaDepan() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(super.getClass().getResource("/picture/indriati48.png")).getImage());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width, screenSize.height);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        btnAdmin10.setEnabled(false);
        jPanel1.remove(btnAdmin10);
        jPanel1.remove(btnAdmin11);
        jPanel1.remove(btnAdmin7);

        jPanel1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel39 = new widget.Label();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        PanelWall = new usu.widget.glass.PanelGlass();
        jPanel1 = new widget.Panel();
        btnAdmin10 = new widget.ButtonBig();
        btnAdmin7 = new widget.ButtonBig();
        btnAdmin2 = new widget.ButtonBig();
        btnAdmin5 = new widget.ButtonBig();
        btnAdmin6 = new widget.ButtonBig();
        btnAdmin9 = new widget.ButtonBig();
        btnAdmin8 = new widget.ButtonBig();
        btnAdmin11 = new widget.ButtonBig();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ANJUNGAN PASIEN MANDIRI");
        setBackground(new java.awt.Color(102, 102, 102));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel3.setBackground(new java.awt.Color(238, 238, 255));
        jPanel3.setForeground(new java.awt.Color(238, 238, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel39.setForeground(new java.awt.Color(0, 131, 62));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("ANJUNGAN PASIEN MANDIRI");
        jLabel39.setFont(new java.awt.Font("Inter", 1, 36)); // NOI18N
        jLabel39.setPreferredSize(new java.awt.Dimension(750, 75));
        jPanel3.add(jLabel39, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Segoe UI SemiBold", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Build 2025-01-01      ");
        jLabel1.setFocusable(false);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel3.add(jLabel1, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBackground(new java.awt.Color(238, 238, 255));
        jPanel2.setForeground(new java.awt.Color(238, 238, 255));

        PanelWall.setBackground(new java.awt.Color(238, 238, 255));
        PanelWall.setBackgroundImage(new javax.swing.ImageIcon(getClass().getResource("/picture/icon-128x128.png"))); // NOI18N
        PanelWall.setBackgroundImageType(usu.widget.constan.BackgroundConstan.BACKGROUND_IMAGE_STRECT);
        PanelWall.setForeground(new java.awt.Color(238, 238, 255));
        PanelWall.setPreferredSize(new java.awt.Dimension(150, 150));
        PanelWall.setRound(false);
        PanelWall.setWarna(new java.awt.Color(238, 238, 255));

        javax.swing.GroupLayout PanelWallLayout = new javax.swing.GroupLayout(PanelWall);
        PanelWall.setLayout(PanelWallLayout);
        PanelWallLayout.setHorizontalGroup(
            PanelWallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        PanelWallLayout.setVerticalGroup(
            PanelWallLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        jPanel2.add(PanelWall);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBorder(null);
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 1024));
        jPanel1.setLayout(new java.awt.GridLayout(0, 2));

        btnAdmin10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/antrianpasien.png"))); // NOI18N
        btnAdmin10.setText("ANTRIAN PASIEN");
        btnAdmin10.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin10.setIconTextGap(0);
        btnAdmin10.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin10ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin10);

        btnAdmin7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/checkin.png"))); // NOI18N
        btnAdmin7.setText("CEK IN REGISTRASI");
        btnAdmin7.setEnabled(false);
        btnAdmin7.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        btnAdmin7.setIconTextGap(0);
        btnAdmin7.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin7ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin7);

        btnAdmin2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/kioskselfservice.png"))); // NOI18N
        btnAdmin2.setText("PENDAFTARAN POLIKLINIK");
        btnAdmin2.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        btnAdmin2.setIconTextGap(0);
        btnAdmin2.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin2ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin2);

        btnAdmin5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/bpjs-kesehatan.png"))); // NOI18N
        btnAdmin5.setText("SEP KUNJUNGAN PERTAMA");
        btnAdmin5.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        btnAdmin5.setIconTextGap(0);
        btnAdmin5.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin5ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin5);

        btnAdmin6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/bpjs-kesehatan.png"))); // NOI18N
        btnAdmin6.setText("SEP KONTROL");
        btnAdmin6.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        btnAdmin6.setIconTextGap(0);
        btnAdmin6.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin6ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin6);

        btnAdmin9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/bpjs-kesehatan.png"))); // NOI18N
        btnAdmin9.setText("SEP KONTROL BEDA POLI");
        btnAdmin9.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        btnAdmin9.setIconTextGap(0);
        btnAdmin9.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin9ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin9);

        btnAdmin8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/mobilejkn48.png"))); // NOI18N
        btnAdmin8.setText("CEK IN MOBILEJKN");
        btnAdmin8.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        btnAdmin8.setIconTextGap(0);
        btnAdmin8.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin8ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin8);

        btnAdmin11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/logo-satset.png"))); // NOI18N
        btnAdmin11.setText("AKTIVASI SATUSEHAT");
        btnAdmin11.setEnabled(false);
        btnAdmin11.setFont(new java.awt.Font("Inter", 1, 30)); // NOI18N
        btnAdmin11.setIconTextGap(0);
        btnAdmin11.setPreferredSize(new java.awt.Dimension(200, 90));
        btnAdmin11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdmin11ActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdmin11);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdmin6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin6ActionPerformed
        sepkontrol.setSize(this.getWidth(), this.getHeight());
        sepkontrol.setLocationRelativeTo(this);
        sepkontrol.setVisible(true);
    }//GEN-LAST:event_btnAdmin6ActionPerformed

    private void btnAdmin2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin2ActionPerformed
        pendaftaranwalkin.setSize(this.getWidth(), this.getHeight());
        pendaftaranwalkin.setLocationRelativeTo(this);
        pendaftaranwalkin.setVisible(true);
    }//GEN-LAST:event_btnAdmin2ActionPerformed

    private void btnAdmin7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin7ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Mohon maaf, fitur masih dalam tahap pengembangan");
//        DlgCekBooking pilih = new DlgCekBooking(null, true);
//        pilih.setSize(this.getWidth(), this.getHeight());
//        pilih.setLocationRelativeTo(this);
//        pilih.setVisible(true);
    }//GEN-LAST:event_btnAdmin7ActionPerformed

    private void btnAdmin5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin5ActionPerformed
        sepkunjunganpertama.setSize(this.getWidth(), this.getHeight());
        sepkunjunganpertama.setLocationRelativeTo(this);
        sepkunjunganpertama.setVisible(true);
    }//GEN-LAST:event_btnAdmin5ActionPerformed

    private void btnAdmin8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin8ActionPerformed
        cekinmobilejkn.setSize(this.getWidth(), this.getHeight());
        cekinmobilejkn.setLocationRelativeTo(this);
        cekinmobilejkn.setVisible(true);
    }//GEN-LAST:event_btnAdmin8ActionPerformed

    private void btnAdmin9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin9ActionPerformed
        sepkontrolbedapoli.setSize(this.getWidth(), this.getHeight());
        sepkontrolbedapoli.setLocationRelativeTo(this);
        sepkontrolbedapoli.setVisible(true);
    }//GEN-LAST:event_btnAdmin9ActionPerformed

    private void btnAdmin10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin10ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Mohon maaf, fitur masih dalam tahap pengembangan");
//        HalamanUtamaAntrian pilih = new HalamanUtamaAntrian();
//        pilih.setSize(this.getWidth(), this.getHeight());
//        pilih.setLocationRelativeTo(this);
//        pilih.setVisible(true);
    }//GEN-LAST:event_btnAdmin10ActionPerformed

    private void btnAdmin11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdmin11ActionPerformed
        JOptionPane.showMessageDialog(rootPane, "Mohon maaf, fitur masih dalam tahap pengembangan");
//        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
//        String nikktppetugas = Sequel.cariIsi("select no_ktp from pegawai where nik='0132' ");
//        Valid.panggilUrl("kyc/index.php?&nikagent=" + nikktppetugas + "");
//        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnAdmin11ActionPerformed

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
            java.util.logging.Logger.getLogger(HalamanUtamaDepan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaDepan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaDepan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HalamanUtamaDepan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HalamanUtamaDepan().setVisible(true);
            }
        });
    }

    public static HalamanUtamaDepan getInstance() {
        if (myInstance == null) {
            myInstance = new HalamanUtamaDepan();
        }

        return myInstance;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private usu.widget.glass.PanelGlass PanelWall;
    private widget.ButtonBig btnAdmin10;
    private widget.ButtonBig btnAdmin11;
    private widget.ButtonBig btnAdmin2;
    private widget.ButtonBig btnAdmin5;
    private widget.ButtonBig btnAdmin6;
    private widget.ButtonBig btnAdmin7;
    private widget.ButtonBig btnAdmin8;
    private widget.ButtonBig btnAdmin9;
    private javax.swing.JLabel jLabel1;
    private widget.Label jLabel39;
    private widget.Panel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
