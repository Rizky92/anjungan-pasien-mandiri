/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */
package bridging;

import fungsi.WarnaTable;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.validasi;
import fungsi.KoneksiDB;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author dosen
 */
public final class BPJSCekRiwayatRujukanTerakhir extends javax.swing.JDialog {

    private final DefaultTableModel tabMode;
    private validasi Valid = new validasi();
    private int i = 0;
    private ApiBPJS api = new ApiBPJS();
    private String URL = "", link = "", utc = "";
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode nameNode;
    private JsonNode response;

    /**
     * Creates new form DlgKamar
     *
     * @param parent
     * @param modal
     */
    public BPJSCekRiwayatRujukanTerakhir(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(10, 2);
        setSize(628, 674);

        Object[] row = {"ICD 10", "Nama Diagnosa", "No.Rujukan", "Kode Tujuan", "Nama Tujuan", "Tgl.Rujukan", "Kode PPK", "Nama PPK", "Status"};
        tabMode = new DefaultTableModel(null, row) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbKamar.setModel(tabMode);

        //tbKamar.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbKamar.getBackground()));
        tbKamar.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbKamar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 9; i++) {
            TableColumn column = tbKamar.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(60);
            } else if (i == 1) {
                column.setPreferredWidth(160);
            } else if (i == 2) {
                column.setPreferredWidth(200);
            } else if (i == 3) {
                column.setPreferredWidth(70);
            } else if (i == 4) {
                column.setPreferredWidth(200);
            } else if (i == 5) {
                column.setPreferredWidth(150);
            } else if (i == 6) {
                column.setPreferredWidth(150);
            } else if (i == 7) {
                column.setPreferredWidth(160);
            } else if (i == 8) {
                column.setPreferredWidth(100);
            }
        }

        tbKamar.setDefaultRenderer(Object.class, new WarnaTable());
        try {
            link = KoneksiDB.URLAPIBPJS();
        } catch (Exception e) {
            System.out.println("E : " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbKamar = new widget.Table();
        panelGlass6 = new widget.panelisi();
        jLabel16 = new widget.Label();
        NoKartu = new widget.TextBox();
        NamaPasien = new widget.TextBox();
        jLabel17 = new widget.Label();
        BtnKeluar = new widget.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);
        setIconImages(null);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pencarian Riwayat Rujukan VClaim ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbKamar.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbKamar.setName("tbKamar"); // NOI18N
        tbKamar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKamarMouseClicked(evt);
            }
        });
        Scroll.setViewportView(tbKamar);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        panelGlass6.setName("panelGlass6"); // NOI18N
        panelGlass6.setPreferredSize(new java.awt.Dimension(44, 54));
        panelGlass6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel16.setText("No.Kartu :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass6.add(jLabel16);

        NoKartu.setEditable(false);
        NoKartu.setName("NoKartu"); // NOI18N
        NoKartu.setPreferredSize(new java.awt.Dimension(130, 23));
        panelGlass6.add(NoKartu);

        NamaPasien.setEditable(false);
        NamaPasien.setName("NamaPasien"); // NOI18N
        NamaPasien.setPreferredSize(new java.awt.Dimension(350, 23));
        panelGlass6.add(NamaPasien);

        jLabel17.setName("jLabel17"); // NOI18N
        jLabel17.setPreferredSize(new java.awt.Dimension(30, 23));
        panelGlass6.add(jLabel17);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass6.add(BtnKeluar);

        internalFrame1.add(panelGlass6, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            dispose();
        }
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void tbKamarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKamarMouseClicked
        if (evt.getClickCount() == 1) {
            dispose();
        }

        if (evt.getClickCount() == 2) {
            dispose();
        }
    }//GEN-LAST:event_tbKamarMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            BPJSCekRiwayatRujukanTerakhir dialog = new BPJSCekRiwayatRujukanTerakhir(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnKeluar;
    private widget.TextBox NamaPasien;
    private widget.TextBox NoKartu;
    private widget.ScrollPane Scroll;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.panelisi panelGlass6;
    private widget.Table tbKamar;
    // End of variables declaration//GEN-END:variables

    public void tampil(String nomorkartu, String namapasien) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            NoKartu.setText(nomorkartu);
            NamaPasien.setText(namapasien);
            Valid.tabelKosong(tabMode);
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Cons-ID", KoneksiDB.CONSIDAPIBPJS());
            utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", KoneksiDB.USERKEYAPIBPJS());
            requestEntity = new HttpEntity(headers);
            URL = link + "/Rujukan/List/Peserta/" + nomorkartu;
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
            nameNode = root.path("metaData");
            if (nameNode.path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                //response = root.path("response").path("rujukan");
                if (response.isArray()) {
                    for (JsonNode list : response) {
                        tabMode.addRow(new Object[]{
                            list.path("diagnosa").path("kode").asText(), list.path("diagnosa").path("nama").asText(), list.path("noKunjungan").asText(),
                            list.path("poliRujukan").path("kode").asText(), list.path("poliRujukan").path("nama").asText(), list.path("tglKunjungan").asText(),
                            list.path("provPerujuk").path("kode").asText(), list.path("provPerujuk").path("nama").asText(), "FKTP"
                        });
                    }
                }
            }

            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Cons-ID", KoneksiDB.CONSIDAPIBPJS());
            utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", KoneksiDB.USERKEYAPIBPJS());
            requestEntity = new HttpEntity(headers);
            URL = link + "/Rujukan/RS/List/Peserta/" + nomorkartu;
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
            nameNode = root.path("metaData");
            if (nameNode.path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                //response = root.path("response").path("rujukan");
                if (response.isArray()) {
                    for (JsonNode list : response) {
                        tabMode.addRow(new Object[]{
                            list.path("diagnosa").path("kode").asText(), list.path("diagnosa").path("nama").asText(), list.path("noKunjungan").asText(),
                            list.path("poliRujukan").path("kode").asText(), list.path("poliRujukan").path("nama").asText(), list.path("tglKunjungan").asText(),
                            list.path("provPerujuk").path("kode").asText(), list.path("provPerujuk").path("nama").asText(), "FKTL"
                        });
                    }
                }
            }
            if (tabMode.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Tidak ditemukan rujukan...!!");
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi Peserta : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server BPJS terputus...!");
            }
        }

        this.setCursor(Cursor.getDefaultCursor());
    }

    public JTable getTable() {
        return tbKamar;
    }
}
