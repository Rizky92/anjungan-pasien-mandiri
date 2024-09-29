/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * DlgAdmin.java
 *
 * Created on 04 Des 13, 12:59:34
 */
package khanzahmsanjungan;

import bridging.ApiBPJS;
import bridging.BPJSCekRiwayatPelayanan;
import bridging.BPJSCekReferensiDokterDPJP1;
import bridging.BPJSCekReferensiPenyakit;
import bridging.BPJSCekRiwayatRujukanTerakhir;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author Kode
 */
public class DlgRegistrasiSEPSMC extends javax.swing.JDialog {
    
    private final Connection koneksi = koneksiDB.condb();
    private final sekuel Sequel = new sekuel();
    private final validasi Valid = new validasi();
    private final ApiBPJS api = new ApiBPJS();
    private final BPJSCekReferensiDokterDPJP1 dokter = new BPJSCekReferensiDokterDPJP1(null, true);
    private final BPJSCekReferensiPenyakit penyakit = new BPJSCekReferensiPenyakit(null, true);
    private final DlgCariPoliBPJS poli = new DlgCariPoliBPJS(null, true);
    private final BPJSCekRiwayatRujukanTerakhir rujukanTerakhir = new BPJSCekRiwayatRujukanTerakhir(null, true);
    private final BPJSCekRiwayatPelayanan riwayatPelayanan = new BPJSCekRiwayatPelayanan(null, true);
    private final ObjectMapper mapper = new ObjectMapper();
    
    private final String CARABAYAR = Sequel.cariIsiSmc("select password_asuransi.kd_pj from password_asuransi limit 1"),
        BASENOREG = koneksiDB.BASENOREG(),
        URUTNOREG = koneksiDB.URUTNOREG().replaceAll("\\s+", ""),
        URLAPIBPJS = koneksiDB.URLAPIBPJS(),
        USERFINGERPRINTBPJS = koneksiDB.USERFINGERPRINTBPJS(),
        PASSFINGERPRINTBPJS = koneksiDB.PASSFINGERPRINTBPJS(),
        URLAPLIKASIFINGERPRINTBPJS = koneksiDB.URLAPLIKASIFINGERPRINTBPJS();
    
    private String kodeBooking = "",
        jenisKunjungan = "3",
        noReg = "",
        noRawat = "",
        umur = "0",
        statusUmur = "Th",
        namaPj = "",
        hubunganPj = "",
        alamatPj = "",
        biayaReg = "0",
        kodeDokterRS = "",
        kodePoliRS = "",
        statusPoli = "Baru",
        statusDaftar = "",
        hari = "",
        jamMulai = "",
        jamSelesai = "",
        instansiNama = "",
        instansiKab = "",
        url = "",
        json = "",
        aksi = "",
        prb = "",
        sTglKLL = "",
        dataJam = "",
        utc = "";
    
    private boolean statusfinger = false, aplikasiAktif = false;

    private int kuota = -1;

    private JsonNode root, response, metadata;
    private Calendar cal = Calendar.getInstance();
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date parsedDate;

    /**
     * Creates new form DlgAdmin
     *
     * @param parent
     * @param id
     */
    public DlgRegistrasiSEPSMC(java.awt.Frame parent, boolean id) {
        super(parent, id);
        initComponents();

        jumlahBarcode.setDocument(new batasInput((byte) 3).getOnlyAngka(jumlahBarcode));

        try (ResultSet rs = koneksi.createStatement().executeQuery("select nama_instansi, kabupaten, kode_ppk from setting")) {
            if (rs.next()) {
                kodePPKPelayanan.setText(rs.getString("kode_ppk"));
                namaPPKPelayanan.setText(rs.getString("nama_instansi"));
                instansiNama = rs.getString("nama_instansi");
                instansiKab = rs.getString("kabupaten");
            }
        } catch (SQLException e) {
            System.out.println("Notif : " + e);
        }

        dokter.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (dokter.getTable().getSelectedRow() != -1) {
                    kodeDokterBPJS.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                    namaDokterBPJS.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 2).toString());
                    if (jenisPelayanan.getSelectedIndex() == 1) {
                        kodeDPJPLayanan.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 1).toString());
                        namaDPJPLayanan.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(), 2).toString());
                    }

                }
            }
        });

        poli.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (poli.getTable().getSelectedRow() != -1) {
                    kodePoliBPJS.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(), 0).toString());
                    namaPoliBPJS.setText(poli.getTable().getValueAt(poli.getTable().getSelectedRow(), 1).toString());

                }
            }
        });

        penyakit.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (penyakit.getTable().getSelectedRow() != -1) {
                    kodeDiagnosaBPJS.setText(penyakit.getTable().getValueAt(penyakit.getTable().getSelectedRow(), 1).toString());
                    namaDiagnosaBPJS.setText(penyakit.getTable().getValueAt(penyakit.getTable().getSelectedRow(), 2).toString());
                }
            }
        });

        rujukanTerakhir.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (rujukanTerakhir.getTable().getSelectedRow() != -1) {
                    kodeDiagnosaBPJS.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 0).toString());
                    namaDiagnosaBPJS.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 1).toString());
                    noRujukan.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 2).toString());
                    kodePoliBPJS.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 3).toString());
                    namaPoliBPJS.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 4).toString());
                    kodePPK.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 6).toString());
                    namaPPK.setText(rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 7).toString());
                    Valid.SetTgl(tglRujukan, rujukanTerakhir.getTable().getValueAt(rujukanTerakhir.getTable().getSelectedRow(), 5).toString());
                    catatan.requestFocus();
                }
            }
        });

        riwayatPelayanan.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (riwayatPelayanan.getTable().getSelectedRow() != -1) {
                    if ((riwayatPelayanan.getTable().getSelectedColumn() == 6) || (riwayatPelayanan.getTable().getSelectedColumn() == 7)) {
                        noRujukan.setText(riwayatPelayanan.getTable().getValueAt(riwayatPelayanan.getTable().getSelectedRow(), riwayatPelayanan.getTable().getSelectedColumn()).toString());
                    }
                }
                noRujukan.requestFocus();
            }
        });

        jumlahBarcode.setText("3");
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        WindowAksi = new javax.swing.JDialog();
        internalFrame1 = new widget.InternalFrame();
        pwUserId = new widget.PasswordBox();
        pwPass = new widget.PasswordBox();
        btnAksiKonfirmasi = new widget.Button();
        btnAksiBatal = new widget.Button();
        label1 = new widget.Label();
        label2 = new widget.Label();
        label3 = new widget.Label();
        jPanel1 = new component.Panel();
        jPanel2 = new component.Panel();
        namaPasien = new widget.TextBox();
        noRM = new widget.TextBox();
        noPesertaBPJS = new widget.TextBox();
        jLabel20 = new widget.Label();
        tglSEP = new widget.Tanggal();
        jLabel22 = new widget.Label();
        tglRujukan = new widget.Tanggal();
        jLabel23 = new widget.Label();
        noRujukan = new widget.TextBox();
        jLabel9 = new widget.Label();
        kodePPKPelayanan = new widget.TextBox();
        namaPPKPelayanan = new widget.TextBox();
        jLabel10 = new widget.Label();
        kodePPK = new widget.TextBox();
        namaPPK = new widget.TextBox();
        jLabel11 = new widget.Label();
        kodeDiagnosaBPJS = new widget.TextBox();
        namaDiagnosaBPJS = new widget.TextBox();
        namaPoliBPJS = new widget.TextBox();
        kodePoliBPJS = new widget.TextBox();
        LabelPoli = new widget.Label();
        jLabel13 = new widget.Label();
        jLabel14 = new widget.Label();
        catatan = new widget.TextBox();
        jenisPelayanan = new widget.ComboBox();
        LabelKelas = new widget.Label();
        kelas = new widget.ComboBox();
        lakaLantas = new widget.ComboBox();
        jLabel8 = new widget.Label();
        tglLahir = new widget.TextBox();
        jLabel18 = new widget.Label();
        jk = new widget.TextBox();
        jLabel24 = new widget.Label();
        jenisPeserta = new widget.TextBox();
        jLabel25 = new widget.Label();
        statusPeserta = new widget.TextBox();
        jLabel27 = new widget.Label();
        asalRujukan = new widget.ComboBox();
        noTelp = new widget.TextBox();
        katarak = new widget.ComboBox();
        jLabel37 = new widget.Label();
        labelKLL1 = new widget.Label();
        tglKLL = new widget.Tanggal();
        LabelPoli2 = new widget.Label();
        kodeDokterBPJS = new widget.TextBox();
        namaDokterBPJS = new widget.TextBox();
        labelKLL2 = new widget.Label();
        keterangan = new widget.TextBox();
        labelKLL3 = new widget.Label();
        suplesi = new widget.ComboBox();
        noSEPSuplesi = new widget.TextBox();
        labelKLL4 = new widget.Label();
        labelKLL5 = new widget.Label();
        kodeProvKLL = new widget.TextBox();
        namaProvKLL = new widget.TextBox();
        labelKLL6 = new widget.Label();
        kodeKabKLL = new widget.TextBox();
        namaKabKLL = new widget.TextBox();
        labelKLL7 = new widget.Label();
        kodeKecKLL = new widget.TextBox();
        namaKecKLL = new widget.TextBox();
        jLabel42 = new widget.Label();
        tujuanKunjungan = new widget.ComboBox();
        flagProsedur = new widget.ComboBox();
        jLabel43 = new widget.Label();
        jLabel44 = new widget.Label();
        penunjang = new widget.ComboBox();
        jLabel45 = new widget.Label();
        asesmenPelayanan = new widget.ComboBox();
        kodeDPJPLayanan = new widget.TextBox();
        namaDPJPLayanan = new widget.TextBox();
        pilihDokterBPJS = new widget.Button();
        jLabel55 = new widget.Label();
        jLabel56 = new widget.Label();
        jLabel12 = new widget.Label();
        jLabel6 = new widget.Label();
        noSuratKontrol = new widget.TextBox();
        jLabel26 = new widget.Label();
        nik = new widget.TextBox();
        jLabel7 = new widget.Label();
        pilihPoliBPJS = new widget.Button();
        pilihDiagnosaBPJS = new widget.Button();
        pilihRujukan = new widget.Button();
        riwayatPelayananBPJS = new widget.Button();
        LabelPoli7 = new widget.Label();
        approvalFP = new widget.Button();
        pengajuanFP = new widget.Button();
        jLabel15 = new widget.Label();
        jumlahBarcode = new widget.TextBox();
        pilihSuratKontrol = new widget.Button();
        labelNoRawat = new widget.Label();
        jPanel3 = new javax.swing.JPanel();
        simpan = new component.Button();
        validasi = new component.Button();
        batal = new component.Button();

        WindowAksi.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowAksi.setModal(true);
        WindowAksi.setUndecorated(true);
        WindowAksi.setResizable(false);

        internalFrame1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pwUserId.setForeground(new java.awt.Color(40, 40, 40));
        pwUserId.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pwUserId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwUserIdKeyPressed(evt);
            }
        });
        internalFrame1.add(pwUserId, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 230, 23));

        pwPass.setForeground(new java.awt.Color(40, 40, 40));
        pwPass.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pwPass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwPassKeyPressed(evt);
            }
        });
        internalFrame1.add(pwPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 230, 23));

        btnAksiKonfirmasi.setIcon(new javax.swing.ImageIcon("D:\\Projects\\java\\khanza-apm-custom\\src\\picture\\Save.png")); // NOI18N
        btnAksiKonfirmasi.setText("Konfirmasi");
        btnAksiKonfirmasi.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAksiKonfirmasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAksiKonfirmasiActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAksiKonfirmasi, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, -1, -1));

        btnAksiBatal.setIcon(new javax.swing.ImageIcon("D:\\Projects\\java\\khanza-apm-custom\\src\\picture\\Delete.png")); // NOI18N
        btnAksiBatal.setText("Batal");
        btnAksiBatal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnAksiBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAksiBatalActionPerformed(evt);
            }
        });
        internalFrame1.add(btnAksiBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        label1.setText("User ID :");
        label1.setFocusable(false);
        label1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        internalFrame1.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 120, 23));

        label2.setText("Password :");
        label2.setFocusable(false);
        label2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        internalFrame1.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 120, 23));

        label3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label3.setText("Konfirmasi Aksi");
        label3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        internalFrame1.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 400, -1));

        WindowAksi.getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new java.awt.BorderLayout(1, 1));

        jPanel1.setBackground(new java.awt.Color(238, 238, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(238, 238, 255), 1, true), "DATA ELIGIBILITAS PESERTA JKN", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Inter", 0, 24), new java.awt.Color(0, 131, 62))); // NOI18N
        jPanel1.setMinimumSize(new java.awt.Dimension(543, 106));
        jPanel1.setPreferredSize(new java.awt.Dimension(543, 106));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 1));

        jPanel2.setBackground(new java.awt.Color(238, 238, 255));
        jPanel2.setForeground(new java.awt.Color(0, 131, 62));
        jPanel2.setPreferredSize(new java.awt.Dimension(390, 120));
        jPanel2.setLayout(null);

        namaPasien.setEditable(false);
        namaPasien.setBackground(new java.awt.Color(245, 250, 240));
        namaPasien.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaPasien.setHighlighter(null);
        jPanel2.add(namaPasien);
        namaPasien.setBounds(340, 10, 230, 30);

        noRM.setEditable(false);
        noRM.setBackground(new java.awt.Color(245, 250, 240));
        noRM.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        noRM.setHighlighter(null);
        jPanel2.add(noRM);
        noRM.setBounds(230, 10, 110, 30);

        noPesertaBPJS.setEditable(false);
        noPesertaBPJS.setBackground(new java.awt.Color(255, 255, 153));
        noPesertaBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        noPesertaBPJS.setHighlighter(null);
        jPanel2.add(noPesertaBPJS);
        noPesertaBPJS.setBounds(730, 70, 300, 30);

        jLabel20.setForeground(new java.awt.Color(0, 131, 62));
        jLabel20.setText("Tgl. SEP :");
        jLabel20.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel20.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel20);
        jLabel20.setBounds(615, 130, 110, 30);

        tglSEP.setEditable(false);
        tglSEP.setForeground(new java.awt.Color(50, 70, 50));
        tglSEP.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-09-2024" }));
        tglSEP.setDisplayFormat("dd-MM-yyyy");
        tglSEP.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tglSEP.setOpaque(false);
        tglSEP.setPreferredSize(new java.awt.Dimension(95, 25));
        jPanel2.add(tglSEP);
        tglSEP.setBounds(730, 130, 170, 30);

        jLabel22.setForeground(new java.awt.Color(0, 131, 62));
        jLabel22.setText("Tgl.  Rujukan :");
        jLabel22.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel22.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel22);
        jLabel22.setBounds(615, 160, 110, 30);

        tglRujukan.setEditable(false);
        tglRujukan.setForeground(new java.awt.Color(50, 70, 50));
        tglRujukan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-09-2024" }));
        tglRujukan.setDisplayFormat("dd-MM-yyyy");
        tglRujukan.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tglRujukan.setOpaque(false);
        tglRujukan.setPreferredSize(new java.awt.Dimension(95, 23));
        jPanel2.add(tglRujukan);
        tglRujukan.setBounds(730, 160, 170, 30);

        jLabel23.setForeground(new java.awt.Color(0, 131, 62));
        jLabel23.setText("No. Surat Kontrol :");
        jLabel23.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel23.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel23);
        jLabel23.setBounds(80, 70, 145, 30);

        noRujukan.setEditable(false);
        noRujukan.setBackground(new java.awt.Color(255, 255, 153));
        noRujukan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        noRujukan.setHighlighter(null);
        jPanel2.add(noRujukan);
        noRujukan.setBounds(230, 100, 340, 30);

        jLabel9.setForeground(new java.awt.Color(0, 131, 62));
        jLabel9.setText("PPK Pelayanan :");
        jLabel9.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel9);
        jLabel9.setBounds(80, 250, 145, 30);

        kodePPKPelayanan.setEditable(false);
        kodePPKPelayanan.setBackground(new java.awt.Color(245, 250, 240));
        kodePPKPelayanan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodePPKPelayanan.setHighlighter(null);
        jPanel2.add(kodePPKPelayanan);
        kodePPKPelayanan.setBounds(230, 250, 75, 30);

        namaPPKPelayanan.setEditable(false);
        namaPPKPelayanan.setBackground(new java.awt.Color(245, 250, 240));
        namaPPKPelayanan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaPPKPelayanan.setHighlighter(null);
        jPanel2.add(namaPPKPelayanan);
        namaPPKPelayanan.setBounds(310, 250, 260, 30);

        jLabel10.setForeground(new java.awt.Color(0, 131, 62));
        jLabel10.setText("PPK Rujukan :");
        jLabel10.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel10);
        jLabel10.setBounds(80, 130, 145, 30);

        kodePPK.setEditable(false);
        kodePPK.setBackground(new java.awt.Color(245, 250, 240));
        kodePPK.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodePPK.setHighlighter(null);
        jPanel2.add(kodePPK);
        kodePPK.setBounds(230, 130, 75, 30);

        namaPPK.setEditable(false);
        namaPPK.setBackground(new java.awt.Color(245, 250, 240));
        namaPPK.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaPPK.setHighlighter(null);
        jPanel2.add(namaPPK);
        namaPPK.setBounds(310, 130, 260, 30);

        jLabel11.setForeground(new java.awt.Color(0, 131, 62));
        jLabel11.setText("Diagnosa Awal :");
        jLabel11.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel11);
        jLabel11.setBounds(80, 160, 145, 30);

        kodeDiagnosaBPJS.setEditable(false);
        kodeDiagnosaBPJS.setBackground(new java.awt.Color(255, 255, 153));
        kodeDiagnosaBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodeDiagnosaBPJS.setHighlighter(null);
        jPanel2.add(kodeDiagnosaBPJS);
        kodeDiagnosaBPJS.setBounds(230, 160, 75, 30);

        namaDiagnosaBPJS.setEditable(false);
        namaDiagnosaBPJS.setBackground(new java.awt.Color(255, 255, 153));
        namaDiagnosaBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaDiagnosaBPJS.setHighlighter(null);
        jPanel2.add(namaDiagnosaBPJS);
        namaDiagnosaBPJS.setBounds(310, 160, 260, 30);

        namaPoliBPJS.setEditable(false);
        namaPoliBPJS.setBackground(new java.awt.Color(255, 255, 153));
        namaPoliBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaPoliBPJS.setHighlighter(null);
        jPanel2.add(namaPoliBPJS);
        namaPoliBPJS.setBounds(310, 190, 260, 30);

        kodePoliBPJS.setEditable(false);
        kodePoliBPJS.setBackground(new java.awt.Color(255, 255, 153));
        kodePoliBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodePoliBPJS.setHighlighter(null);
        jPanel2.add(kodePoliBPJS);
        kodePoliBPJS.setBounds(230, 190, 75, 30);

        LabelPoli.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli.setText("Poli Tujuan :");
        LabelPoli.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        LabelPoli.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(LabelPoli);
        LabelPoli.setBounds(80, 190, 145, 30);

        jLabel13.setForeground(new java.awt.Color(0, 131, 62));
        jLabel13.setText("Jenis Pelayanan :");
        jLabel13.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel13);
        jLabel13.setBounds(80, 280, 145, 30);

        jLabel14.setForeground(new java.awt.Color(0, 131, 62));
        jLabel14.setText("Catatan :");
        jLabel14.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel14);
        jLabel14.setBounds(615, 460, 110, 30);

        catatan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
        catatan.setHighlighter(null);
        jPanel2.add(catatan);
        catatan.setBounds(730, 460, 300, 30);

        jenisPelayanan.setBackground(new java.awt.Color(255, 255, 153));
        jenisPelayanan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        jenisPelayanan.setForeground(new java.awt.Color(0, 131, 62));
        jenisPelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Ranap", "2. Ralan" }));
        jenisPelayanan.setSelectedIndex(1);
        jenisPelayanan.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jenisPelayanan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jenisPelayananItemStateChanged(evt);
            }
        });
        jPanel2.add(jenisPelayanan);
        jenisPelayanan.setBounds(230, 280, 110, 30);

        LabelKelas.setForeground(new java.awt.Color(0, 131, 62));
        LabelKelas.setText("Kelas :");
        LabelKelas.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(LabelKelas);
        LabelKelas.setBounds(350, 280, 50, 30);

        kelas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        kelas.setForeground(new java.awt.Color(0, 131, 62));
        kelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Kelas 1", "2. Kelas 2", "3. Kelas 3" }));
        kelas.setSelectedIndex(2);
        kelas.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(kelas);
        kelas.setBounds(405, 280, 100, 30);

        lakaLantas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        lakaLantas.setForeground(new java.awt.Color(0, 131, 62));
        lakaLantas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Bukan KLL", "1. KLL Bukan KK", "2. KLL dan KK", "3. KK" }));
        lakaLantas.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        lakaLantas.setPreferredSize(new java.awt.Dimension(64, 25));
        lakaLantas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                lakaLantasItemStateChanged(evt);
            }
        });
        jPanel2.add(lakaLantas);
        lakaLantas.setBounds(730, 250, 170, 30);

        jLabel8.setForeground(new java.awt.Color(0, 131, 62));
        jLabel8.setText("Data Pasien :");
        jLabel8.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel8);
        jLabel8.setBounds(80, 10, 145, 30);

        tglLahir.setEditable(false);
        tglLahir.setBackground(new java.awt.Color(245, 250, 240));
        tglLahir.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        tglLahir.setHighlighter(null);
        jPanel2.add(tglLahir);
        tglLahir.setBounds(230, 40, 110, 30);

        jLabel18.setForeground(new java.awt.Color(0, 131, 62));
        jLabel18.setText("J.K :");
        jLabel18.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel18);
        jLabel18.setBounds(905, 10, 30, 30);

        jk.setEditable(false);
        jk.setBackground(new java.awt.Color(245, 250, 240));
        jk.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        jk.setHighlighter(null);
        jPanel2.add(jk);
        jk.setBounds(940, 10, 90, 30);

        jLabel24.setForeground(new java.awt.Color(0, 131, 62));
        jLabel24.setText("Peserta :");
        jLabel24.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel24.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel24);
        jLabel24.setBounds(615, 10, 110, 30);

        jenisPeserta.setEditable(false);
        jenisPeserta.setBackground(new java.awt.Color(245, 250, 240));
        jenisPeserta.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        jenisPeserta.setHighlighter(null);
        jPanel2.add(jenisPeserta);
        jenisPeserta.setBounds(730, 10, 173, 30);

        jLabel25.setForeground(new java.awt.Color(0, 131, 62));
        jLabel25.setText("Status :");
        jLabel25.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel25);
        jLabel25.setBounds(350, 40, 50, 30);

        statusPeserta.setEditable(false);
        statusPeserta.setBackground(new java.awt.Color(245, 250, 240));
        statusPeserta.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        statusPeserta.setHighlighter(null);
        jPanel2.add(statusPeserta);
        statusPeserta.setBounds(405, 40, 165, 30);

        jLabel27.setForeground(new java.awt.Color(0, 131, 62));
        jLabel27.setText("Asal Rujukan :");
        jLabel27.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel27);
        jLabel27.setBounds(615, 100, 110, 30);

        asalRujukan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        asalRujukan.setForeground(new java.awt.Color(0, 131, 62));
        asalRujukan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Faskes 1", "2. Faskes 2(RS)" }));
        asalRujukan.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(asalRujukan);
        asalRujukan.setBounds(730, 100, 170, 30);

        noTelp.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        noTelp.setHighlighter(null);
        jPanel2.add(noTelp);
        noTelp.setBounds(730, 190, 170, 30);

        katarak.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        katarak.setForeground(new java.awt.Color(0, 131, 62));
        katarak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Tidak", "1.Ya" }));
        katarak.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        katarak.setPreferredSize(new java.awt.Dimension(64, 25));
        jPanel2.add(katarak);
        katarak.setBounds(730, 220, 170, 30);

        jLabel37.setForeground(new java.awt.Color(0, 131, 62));
        jLabel37.setText("Katarak :");
        jLabel37.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel37);
        jLabel37.setBounds(615, 220, 110, 30);

        labelKLL1.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL1.setText("Tgl. KLL :");
        labelKLL1.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        labelKLL1.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(labelKLL1);
        labelKLL1.setBounds(615, 280, 110, 30);

        tglKLL.setEditable(false);
        tglKLL.setForeground(new java.awt.Color(50, 70, 50));
        tglKLL.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29-09-2024" }));
        tglKLL.setDisplayFormat("dd-MM-yyyy");
        tglKLL.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tglKLL.setOpaque(false);
        tglKLL.setPreferredSize(new java.awt.Dimension(64, 25));
        jPanel2.add(tglKLL);
        tglKLL.setBounds(730, 280, 170, 30);

        LabelPoli2.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli2.setText("Dokter DPJP :");
        LabelPoli2.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        LabelPoli2.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(LabelPoli2);
        LabelPoli2.setBounds(80, 220, 145, 30);

        kodeDokterBPJS.setEditable(false);
        kodeDokterBPJS.setBackground(new java.awt.Color(255, 255, 153));
        kodeDokterBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodeDokterBPJS.setHighlighter(null);
        jPanel2.add(kodeDokterBPJS);
        kodeDokterBPJS.setBounds(230, 220, 75, 30);

        namaDokterBPJS.setEditable(false);
        namaDokterBPJS.setBackground(new java.awt.Color(255, 255, 153));
        namaDokterBPJS.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaDokterBPJS.setHighlighter(null);
        jPanel2.add(namaDokterBPJS);
        namaDokterBPJS.setBounds(310, 220, 260, 30);

        labelKLL2.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL2.setText("Keterangan :");
        labelKLL2.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        labelKLL2.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(labelKLL2);
        labelKLL2.setBounds(615, 310, 110, 30);

        keterangan.setEditable(false);
        keterangan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        keterangan.setHighlighter(null);
        jPanel2.add(keterangan);
        keterangan.setBounds(730, 310, 300, 30);

        labelKLL3.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL3.setText("Suplesi :");
        labelKLL3.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(labelKLL3);
        labelKLL3.setBounds(615, 340, 110, 30);

        suplesi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        suplesi.setForeground(new java.awt.Color(0, 131, 62));
        suplesi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Tidak", "1.Ya" }));
        suplesi.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        suplesi.setPreferredSize(new java.awt.Dimension(64, 25));
        jPanel2.add(suplesi);
        suplesi.setBounds(730, 340, 90, 30);

        noSEPSuplesi.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        noSEPSuplesi.setHighlighter(null);
        jPanel2.add(noSEPSuplesi);
        noSEPSuplesi.setBounds(890, 340, 140, 30);

        labelKLL4.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL4.setText("No. SEP :");
        labelKLL4.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        labelKLL4.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(labelKLL4);
        labelKLL4.setBounds(825, 340, 60, 30);

        labelKLL5.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL5.setText("Propinsi KLL :");
        labelKLL5.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(labelKLL5);
        labelKLL5.setBounds(615, 370, 110, 30);

        kodeProvKLL.setEditable(false);
        kodeProvKLL.setBackground(new java.awt.Color(245, 250, 240));
        kodeProvKLL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodeProvKLL.setHighlighter(null);
        jPanel2.add(kodeProvKLL);
        kodeProvKLL.setBounds(730, 370, 55, 30);

        namaProvKLL.setEditable(false);
        namaProvKLL.setBackground(new java.awt.Color(245, 250, 240));
        namaProvKLL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaProvKLL.setHighlighter(null);
        jPanel2.add(namaProvKLL);
        namaProvKLL.setBounds(790, 370, 240, 30);

        labelKLL6.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL6.setText("Kabupaten KLL :");
        labelKLL6.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(labelKLL6);
        labelKLL6.setBounds(615, 400, 110, 30);

        kodeKabKLL.setEditable(false);
        kodeKabKLL.setBackground(new java.awt.Color(245, 250, 240));
        kodeKabKLL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodeKabKLL.setHighlighter(null);
        jPanel2.add(kodeKabKLL);
        kodeKabKLL.setBounds(730, 400, 55, 30);

        namaKabKLL.setEditable(false);
        namaKabKLL.setBackground(new java.awt.Color(245, 250, 240));
        namaKabKLL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaKabKLL.setHighlighter(null);
        jPanel2.add(namaKabKLL);
        namaKabKLL.setBounds(790, 400, 240, 30);

        labelKLL7.setForeground(new java.awt.Color(0, 131, 62));
        labelKLL7.setText("Kecamatan KLL :");
        labelKLL7.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(labelKLL7);
        labelKLL7.setBounds(615, 430, 110, 30);

        kodeKecKLL.setEditable(false);
        kodeKecKLL.setBackground(new java.awt.Color(245, 250, 240));
        kodeKecKLL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodeKecKLL.setHighlighter(null);
        jPanel2.add(kodeKecKLL);
        kodeKecKLL.setBounds(730, 430, 55, 30);

        namaKecKLL.setEditable(false);
        namaKecKLL.setBackground(new java.awt.Color(245, 250, 240));
        namaKecKLL.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaKecKLL.setHighlighter(null);
        jPanel2.add(namaKecKLL);
        namaKecKLL.setBounds(790, 430, 240, 30);

        jLabel42.setForeground(new java.awt.Color(0, 131, 62));
        jLabel42.setText("Tujuan Kunjungan :");
        jLabel42.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel42.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel42);
        jLabel42.setBounds(80, 310, 145, 30);

        tujuanKunjungan.setBackground(new java.awt.Color(255, 255, 153));
        tujuanKunjungan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        tujuanKunjungan.setForeground(new java.awt.Color(0, 131, 62));
        tujuanKunjungan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0. Normal", "1. Prosedur", "2. Konsul Dokter" }));
        tujuanKunjungan.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        tujuanKunjungan.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tujuanKunjunganItemStateChanged(evt);
            }
        });
        jPanel2.add(tujuanKunjungan);
        tujuanKunjungan.setBounds(230, 310, 340, 30);

        flagProsedur.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        flagProsedur.setForeground(new java.awt.Color(0, 131, 62));
        flagProsedur.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "0. Prosedur Tidak Berkelanjutan", "1. Prosedur dan Terapi Berkelanjutan" }));
        flagProsedur.setEnabled(false);
        flagProsedur.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(flagProsedur);
        flagProsedur.setBounds(230, 340, 340, 30);

        jLabel43.setForeground(new java.awt.Color(0, 131, 62));
        jLabel43.setText("Flag Prosedur :");
        jLabel43.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel43.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel43);
        jLabel43.setBounds(80, 340, 145, 30);

        jLabel44.setForeground(new java.awt.Color(0, 131, 62));
        jLabel44.setText("Penunjang :");
        jLabel44.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel44.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel44);
        jLabel44.setBounds(80, 370, 145, 30);

        penunjang.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        penunjang.setForeground(new java.awt.Color(0, 131, 62));
        penunjang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "1. Radioterapi", "2. Kemoterapi", "3. Rehabilitasi Medik", "4. Rehabilitasi Psikososial", "5. Transfusi Darah", "6. Pelayanan Gigi", "7. Laboratorium", "8. USG", "9. Farmasi", "10. Lain-Lain", "11. MRI", "12. HEMODIALISA" }));
        penunjang.setEnabled(false);
        penunjang.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(penunjang);
        penunjang.setBounds(230, 370, 340, 30);

        jLabel45.setForeground(new java.awt.Color(0, 131, 62));
        jLabel45.setText("Asesmen Pelayanan :");
        jLabel45.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel45.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel45);
        jLabel45.setBounds(80, 400, 145, 30);

        asesmenPelayanan.setBackground(new java.awt.Color(255, 255, 153));
        asesmenPelayanan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)));
        asesmenPelayanan.setForeground(new java.awt.Color(0, 131, 62));
        asesmenPelayanan.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "1. Poli spesialis tidak tersedia pada hari sebelumnya", "2. Jam Poli telah berakhir pada hari sebelumnya", "3. Spesialis yang dimaksud tidak praktek pada hari sebelumnya", "4. Atas Instruksi RS", "5. Tujuan Kontrol" }));
        asesmenPelayanan.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(asesmenPelayanan);
        asesmenPelayanan.setBounds(230, 400, 340, 30);

        kodeDPJPLayanan.setEditable(false);
        kodeDPJPLayanan.setBackground(new java.awt.Color(255, 255, 153));
        kodeDPJPLayanan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        kodeDPJPLayanan.setHighlighter(null);
        jPanel2.add(kodeDPJPLayanan);
        kodeDPJPLayanan.setBounds(230, 430, 75, 30);

        namaDPJPLayanan.setEditable(false);
        namaDPJPLayanan.setBackground(new java.awt.Color(255, 255, 153));
        namaDPJPLayanan.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        namaDPJPLayanan.setHighlighter(null);
        jPanel2.add(namaDPJPLayanan);
        namaDPJPLayanan.setBounds(310, 430, 260, 30);

        pilihDokterBPJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        pilihDokterBPJS.setMnemonic('X');
        pilihDokterBPJS.setToolTipText("Alt+X");
        pilihDokterBPJS.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        pilihDokterBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihDokterBPJSActionPerformed(evt);
            }
        });
        jPanel2.add(pilihDokterBPJS);
        pilihDokterBPJS.setBounds(570, 220, 40, 30);

        jLabel55.setForeground(new java.awt.Color(0, 131, 62));
        jLabel55.setText("Laka Lantas :");
        jLabel55.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel55);
        jLabel55.setBounds(615, 250, 110, 30);

        jLabel56.setForeground(new java.awt.Color(0, 131, 62));
        jLabel56.setText("No. Telp :");
        jLabel56.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel56.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel56);
        jLabel56.setBounds(615, 190, 110, 30);

        jLabel12.setForeground(new java.awt.Color(0, 131, 62));
        jLabel12.setText("Tgl. Lahir :");
        jLabel12.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel12);
        jLabel12.setBounds(80, 40, 145, 30);

        jLabel6.setForeground(new java.awt.Color(0, 131, 62));
        jLabel6.setText("NIK :");
        jLabel6.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel6);
        jLabel6.setBounds(615, 40, 110, 30);

        noSuratKontrol.setEditable(false);
        noSuratKontrol.setBackground(new java.awt.Color(255, 255, 153));
        noSuratKontrol.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        noSuratKontrol.setHighlighter(null);
        jPanel2.add(noSuratKontrol);
        noSuratKontrol.setBounds(230, 70, 340, 30);

        jLabel26.setForeground(new java.awt.Color(0, 131, 62));
        jLabel26.setText("No. Rujukan :");
        jLabel26.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel26.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(jLabel26);
        jLabel26.setBounds(80, 100, 145, 30);

        nik.setEditable(false);
        nik.setBackground(new java.awt.Color(255, 255, 153));
        nik.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        nik.setHighlighter(null);
        jPanel2.add(nik);
        nik.setBounds(730, 40, 300, 30);

        jLabel7.setForeground(new java.awt.Color(0, 131, 62));
        jLabel7.setText("No. Kartu :");
        jLabel7.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel7);
        jLabel7.setBounds(615, 70, 110, 30);

        pilihPoliBPJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        pilihPoliBPJS.setMnemonic('X');
        pilihPoliBPJS.setToolTipText("Alt+X");
        pilihPoliBPJS.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        pilihPoliBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihPoliBPJSActionPerformed(evt);
            }
        });
        jPanel2.add(pilihPoliBPJS);
        pilihPoliBPJS.setBounds(570, 190, 40, 30);

        pilihDiagnosaBPJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        pilihDiagnosaBPJS.setMnemonic('X');
        pilihDiagnosaBPJS.setToolTipText("Alt+X");
        pilihDiagnosaBPJS.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        pilihDiagnosaBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihDiagnosaBPJSActionPerformed(evt);
            }
        });
        jPanel2.add(pilihDiagnosaBPJS);
        pilihDiagnosaBPJS.setBounds(570, 160, 40, 30);

        pilihRujukan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        pilihRujukan.setMnemonic('X');
        pilihRujukan.setToolTipText("Alt+X");
        pilihRujukan.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        pilihRujukan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihRujukanActionPerformed(evt);
            }
        });
        jPanel2.add(pilihRujukan);
        pilihRujukan.setBounds(570, 100, 40, 30);

        riwayatPelayananBPJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        riwayatPelayananBPJS.setMnemonic('X');
        riwayatPelayananBPJS.setText("Riwayat Layanan BPJS");
        riwayatPelayananBPJS.setToolTipText("Alt+X");
        riwayatPelayananBPJS.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        riwayatPelayananBPJS.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        riwayatPelayananBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                riwayatPelayananBPJSActionPerformed(evt);
            }
        });
        jPanel2.add(riwayatPelayananBPJS);
        riwayatPelayananBPJS.setBounds(1040, 190, 220, 50);

        LabelPoli7.setForeground(new java.awt.Color(0, 131, 62));
        LabelPoli7.setText("DPJP Layanan :");
        LabelPoli7.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        LabelPoli7.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel2.add(LabelPoli7);
        LabelPoli7.setBounds(80, 430, 145, 30);

        approvalFP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/approvalfp.png"))); // NOI18N
        approvalFP.setMnemonic('X');
        approvalFP.setText("Approval FP BPJS");
        approvalFP.setToolTipText("Alt+X");
        approvalFP.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        approvalFP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        approvalFP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                approvalFPActionPerformed(evt);
            }
        });
        jPanel2.add(approvalFP);
        approvalFP.setBounds(1040, 310, 190, 50);

        pengajuanFP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pengajuan.png"))); // NOI18N
        pengajuanFP.setMnemonic('X');
        pengajuanFP.setText("Pengajuan FP BPJS");
        pengajuanFP.setToolTipText("Alt+X");
        pengajuanFP.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        pengajuanFP.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        pengajuanFP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pengajuanFPActionPerformed(evt);
            }
        });
        jPanel2.add(pengajuanFP);
        pengajuanFP.setBounds(1040, 250, 190, 50);

        jLabel15.setForeground(new java.awt.Color(0, 131, 62));
        jLabel15.setText("Jumlah Barcode :");
        jLabel15.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(jLabel15);
        jLabel15.setBounds(1040, 70, 110, 30);

        jumlahBarcode.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(192, 192, 223)), javax.swing.BorderFactory.createEmptyBorder(2, 4, 2, 4)));
        jumlahBarcode.setText("3");
        jumlahBarcode.setHighlighter(null);
        jPanel2.add(jumlahBarcode);
        jumlahBarcode.setBounds(1155, 70, 50, 30);

        pilihSuratKontrol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/pilih.png"))); // NOI18N
        pilihSuratKontrol.setMnemonic('X');
        pilihSuratKontrol.setToolTipText("Alt+X");
        pilihSuratKontrol.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        pilihSuratKontrol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihSuratKontrolActionPerformed(evt);
            }
        });
        jPanel2.add(pilihSuratKontrol);
        pilihSuratKontrol.setBounds(570, 70, 40, 30);

        labelNoRawat.setForeground(new java.awt.Color(0, 131, 62));
        labelNoRawat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNoRawat.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jPanel2.add(labelNoRawat);
        labelNoRawat.setBounds(1040, 160, 190, 30);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(238, 238, 255));
        jPanel3.setMinimumSize(new java.awt.Dimension(533, 120));
        jPanel3.setPreferredSize(new java.awt.Dimension(533, 120));

        simpan.setForeground(new java.awt.Color(0, 131, 62));
        simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/konfirmasi.png"))); // NOI18N
        simpan.setMnemonic('S');
        simpan.setText("Konfirmasi");
        simpan.setToolTipText("Alt+S");
        simpan.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        simpan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        simpan.setPreferredSize(new java.awt.Dimension(300, 45));
        simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanActionPerformed(evt);
            }
        });
        jPanel3.add(simpan);

        validasi.setForeground(new java.awt.Color(0, 131, 62));
        validasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/fingerprint.png"))); // NOI18N
        validasi.setMnemonic('K');
        validasi.setText("FINGERPRINT BPJS");
        validasi.setToolTipText("Alt+K");
        validasi.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        validasi.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        validasi.setPreferredSize(new java.awt.Dimension(300, 45));
        validasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validasiActionPerformed(evt);
            }
        });
        jPanel3.add(validasi);

        batal.setForeground(new java.awt.Color(0, 131, 62));
        batal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/48x48/reset.png"))); // NOI18N
        batal.setMnemonic('K');
        batal.setText("Batal");
        batal.setToolTipText("Alt+K");
        batal.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        batal.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        batal.setPreferredSize(new java.awt.Dimension(300, 45));
        batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalActionPerformed(evt);
            }
        });
        jPanel3.add(batal);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalActionPerformed
        dispose();
    }//GEN-LAST:event_batalActionPerformed

    private void simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        cekFinger();
        if (noRM.getText().isBlank() || namaPasien.getText().isBlank()) {
            Valid.textKosong(namaPasien, "Pasien");
        } else if (noPesertaBPJS.getText().isBlank()) {
            Valid.textKosong(noPesertaBPJS, "Nomor Kartu");
        } else if (Sequel.cariBooleanSmc("select * from pasien where no_rkm_medis = ?", noRM.getText())) {
            JOptionPane.showMessageDialog(null, "Maaf, no RM tidak sesuai");
        } else if (kodePPK.getText().isBlank() || namaPPK.getText().isBlank()) {
            Valid.textKosong(kodePPK, "PPK Rujukan");
        } else if (kodePPKPelayanan.getText().isBlank() || namaPPKPelayanan.getText().isBlank()) {
            Valid.textKosong(kodePPKPelayanan, "PPK Pelayanan");
        } else if (kodeDiagnosaBPJS.getText().isBlank() || namaDiagnosaBPJS.getText().isBlank()) {
            Valid.textKosong(kodeDiagnosaBPJS, "Diagnosa");
        } else if (catatan.getText().isBlank()) {
            Valid.textKosong(catatan, "Catatan");
        } else if ((jenisPelayanan.getSelectedIndex() == 1) && (kodePoliBPJS.getText().isBlank() || namaPoliBPJS.getText().isBlank())) {
            Valid.textKosong(kodePoliBPJS, "Poli Tujuan");
        } else if ((lakaLantas.getSelectedIndex() == 1) && keterangan.getText().isBlank()) {
            Valid.textKosong(keterangan, "Keterangan");
        } else if (kodeDokterBPJS.getText().isBlank() || namaDokterBPJS.getText().isBlank()) {
            Valid.textKosong(kodeDokterBPJS, "DPJP");
        } else if (!statusfinger && Sequel.cariIntegerSmc("select timestampdiff(year, ?, current_date())", tglLahir.getText()) >= 17 && jenisPelayanan.getSelectedIndex() != 0 && !kodePoliBPJS.getText().contains("IGD")) {
            JOptionPane.showMessageDialog(null, "Maaf, Pasien belum melakukan Fingerprint");
            bukaAplikasiFingerprint();
        } else {
            biayaRegistPoli();
            cekStatusPasien();
            autoNomor();

            // cek apabila pasien sudah pernah diregistrasikan sebelumnya
            if (Sequel.cariIntegerSmc(
                "select count(*) from reg_periksa where reg_periksa.no_rkm_medis = ? and reg_periksa.tgl_registrasi = ? and reg_periksa.kd_poli = ? " +
                "and reg_periksa.kd_dokter = ? and reg_periksa.kd_pj = ? and reg_periksa.no_rawat != ?", noRM.getText(), Valid.getTglSmc(tglSEP), 
                kodePoliRS, kodeDokterRS, CARABAYAR, labelNoRawat.getText()
            ) > 0) {
                JOptionPane.showMessageDialog(null, "Maaf, Telah terdaftar pemeriksaan hari ini. Mohon konfirmasi ke Bagian Admisi");
                emptTeks();
            } else {
                if (registerPasien()) {
                    if (jenisPelayanan.getSelectedIndex() == 0) {
                        insertSEP();
                    } else if (jenisPelayanan.getSelectedIndex() == 1) {
                        if (namaPoliBPJS.getText().toLowerCase().contains("darurat")) {
                            if (Sequel.cariIntegerSmc(
                                "select count(*) from bridging_sep where bridging_sep.no_kartu = ? and bridging_sep.jnspelayanan = ? " +
                                "and bridging_sep.tglsep = ? and bridging_sep.nmpolitujuan like '%darurat%'", noPesertaBPJS.getText(),
                                jenisPelayanan.getSelectedItem().toString().substring(0, 1), Valid.getTglSmc(tglSEP)) >= 3
                            ) {
                                JOptionPane.showMessageDialog(null, "Maaf, sebelumnya sudah dilakukan 3x pembuatan SEP di jenis pelayanan yang sama..!!");
                            } else {
                                if ((!kodeDokterRS.equals("")) && (!kodePoliRS.equals(""))) {
                                    if (koneksiDB.bRaw("ADDANTRIANAPIMOBILEJKN")) {
                                        if (addAntreanOnsite()) {
                                            insertSEP();
                                        }
                                    } else {
                                        insertSEP();
                                    }
                                }
                            }
                        } else if (!namaPoliBPJS.getText().toLowerCase().contains("darurat")) {
                            if (Sequel.cariIntegerSmc(
                                "select count(*) from bridging_sep where bridging_sep.no_kartu = ? and bridging_sep.jnspelayanan = ? " +
                                "and bridging_sep.tglsep = ? and bridging_sep.nmpolitujuan not like '%darurat%'", noPesertaBPJS.getText(),
                                jenisPelayanan.getSelectedItem().toString().substring(0, 1), Valid.getTglSmc(tglSEP)) >= 1
                            ) {
                                JOptionPane.showMessageDialog(null, "Maaf, sebelumnya sudah dilakukan pembuatan SEP di jenis pelayanan yang sama..!!");
                            } else {
                                if ((!kodeDokterRS.equals("")) && (!kodePoliRS.equals(""))) {
                                    if (koneksiDB.bRaw("ADDANTRIANAPIMOBILEJKN")) {
                                        if (addAntreanOnsite()) {
                                            insertSEP();
                                        }
                                    } else {
                                        insertSEP();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada saat pendaftaran pasien!");
                }
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_simpanActionPerformed

    private void pilihDokterBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihDokterBPJSActionPerformed
        dokter.setSize(jPanel1.getWidth() - 75, jPanel1.getHeight() - 75);
        dokter.setLocationRelativeTo(jPanel1);
        dokter.carinamadokter(kodePoliBPJS.getText(), namaPoliBPJS.getText());
        dokter.setVisible(true);
    }//GEN-LAST:event_pilihDokterBPJSActionPerformed

    private void tujuanKunjunganItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tujuanKunjunganItemStateChanged
        if (tujuanKunjungan.getSelectedIndex() == 0) {
            flagProsedur.setEnabled(false);
            flagProsedur.setSelectedIndex(0);
            penunjang.setEnabled(false);
            penunjang.setSelectedIndex(0);
            asesmenPelayanan.setEnabled(true);
        } else {
            if (tujuanKunjungan.getSelectedIndex() == 1) {
                asesmenPelayanan.setSelectedIndex(0);
                asesmenPelayanan.setEnabled(false);
            } else {
                asesmenPelayanan.setEnabled(true);
            }
            if (flagProsedur.getSelectedIndex() == 0) {
                flagProsedur.setSelectedIndex(2);
            }
            flagProsedur.setEnabled(true);
            if (penunjang.getSelectedIndex() == 0) {
                penunjang.setSelectedIndex(10);
            }
            penunjang.setEnabled(true);
        }
    }//GEN-LAST:event_tujuanKunjunganItemStateChanged

    private void lakaLantasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_lakaLantasItemStateChanged
        if (evt.getItem().toString().substring(0, 1).equals("0")) {
            labelKLL1.setVisible(false);
            tglKLL.setDate(new Date());
            tglKLL.setVisible(false);
            labelKLL2.setVisible(false);
            keterangan.setText("");
            keterangan.setVisible(false);
            labelKLL3.setVisible(false);
            suplesi.setSelectedIndex(0);
            suplesi.setVisible(false);
            labelKLL4.setVisible(false);
            noSEPSuplesi.setText("");
            noSEPSuplesi.setVisible(false);
            labelKLL5.setVisible(false);
            kodeProvKLL.setText("");
            kodeProvKLL.setVisible(false);
            namaProvKLL.setText("");
            namaProvKLL.setVisible(false);
            labelKLL6.setVisible(false);
            kodeKabKLL.setText("");
            kodeKabKLL.setVisible(false);
            namaKabKLL.setText("");
            namaKabKLL.setVisible(false);
            labelKLL7.setVisible(false);
            kodeKecKLL.setText("");
            kodeKecKLL.setVisible(false);
            namaKecKLL.setText("");
            namaKecKLL.setVisible(false);
            jLabel14.setBounds(615, 280, 110, 30);
            catatan.setBounds(730, 280, 300, 30);
        } else {
            labelKLL1.setVisible(true);
            tglKLL.setVisible(true);
            labelKLL2.setVisible(true);
            keterangan.setVisible(true);
            labelKLL3.setVisible(true);
            suplesi.setVisible(true);
            labelKLL4.setVisible(true);
            noSEPSuplesi.setVisible(true);
            labelKLL5.setVisible(true);
            kodeProvKLL.setVisible(true);
            namaProvKLL.setVisible(true);
            labelKLL6.setVisible(true);
            kodeKabKLL.setVisible(true);
            namaKabKLL.setVisible(true);
            labelKLL7.setVisible(true);
            kodeKecKLL.setVisible(true);
            namaKecKLL.setVisible(true);
            jLabel14.setBounds(615, 460, 110, 30);
            catatan.setBounds(730, 460, 300, 30);
        }
    }//GEN-LAST:event_lakaLantasItemStateChanged

    private void jenisPelayananItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jenisPelayananItemStateChanged
        if (jenisPelayanan.getSelectedIndex() == 0) {
            kodePoliBPJS.setText("");
            namaPoliBPJS.setText("");
            LabelPoli.setVisible(false);
            kodePoliBPJS.setVisible(false);
            namaPoliBPJS.setVisible(false);
            kodeDPJPLayanan.setText("");
            namaDPJPLayanan.setText("");
            pilihDokterBPJS.setEnabled(false);
        } else if (jenisPelayanan.getSelectedIndex() == 1) {
            LabelPoli.setVisible(true);
            kodePoliBPJS.setVisible(true);
            namaPoliBPJS.setVisible(true);
            pilihDokterBPJS.setEnabled(true);
        }
    }//GEN-LAST:event_jenisPelayananItemStateChanged

    private void validasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validasiActionPerformed
        bukaAplikasiFingerprint();
    }//GEN-LAST:event_validasiActionPerformed

    private void pilihPoliBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihPoliBPJSActionPerformed
        poli.setSize(jPanel1.getWidth() - 100, jPanel1.getHeight() - 100);
        poli.tampil();
        poli.setLocationRelativeTo(jPanel1);
        poli.setVisible(true);
    }//GEN-LAST:event_pilihPoliBPJSActionPerformed

    private void pilihDiagnosaBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihDiagnosaBPJSActionPerformed
        penyakit.setSize(jPanel1.getWidth() - 100, jPanel1.getHeight() - 100);
        penyakit.setLocationRelativeTo(jPanel1);
        penyakit.setVisible(true);
    }//GEN-LAST:event_pilihDiagnosaBPJSActionPerformed

    private void pilihRujukanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihRujukanActionPerformed
        if (noPesertaBPJS.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "No.Kartu masih kosong...!!");
        } else {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            rujukanTerakhir.setSize(jPanel1.getWidth() - 50, jPanel1.getHeight() - 50);
            rujukanTerakhir.setLocationRelativeTo(jPanel1);
            rujukanTerakhir.tampil(noPesertaBPJS.getText(), namaPasien.getText());
            rujukanTerakhir.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_pilihRujukanActionPerformed

    private void riwayatPelayananBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_riwayatPelayananBPJSActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        riwayatPelayanan.setSize(jPanel1.getWidth() - 50, jPanel1.getHeight() - 50);
        riwayatPelayanan.setLocationRelativeTo(jPanel1);
        riwayatPelayanan.setKartu(noPesertaBPJS.getText());
        riwayatPelayanan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_riwayatPelayananBPJSActionPerformed

    private void approvalFPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_approvalFPActionPerformed
        resetAksi();
        if (!noPesertaBPJS.getText().isBlank()) {
            aksi = "Approval";
            WindowAksi.setSize(400, 300);
            WindowAksi.setLocationRelativeTo(null);
            WindowAksi.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, No. Kartu Peserta tidak ada...!!!");
        }
    }//GEN-LAST:event_approvalFPActionPerformed

    private void pengajuanFPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengajuanFPActionPerformed
        resetAksi();
        if (!noPesertaBPJS.getText().isBlank()) {
            aksi = "Pengajuan";
            WindowAksi.setSize(400, 300);
            WindowAksi.setLocationRelativeTo(null);
            WindowAksi.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, No. Kartu Peserta tidak ada...!!!");
        }
    }//GEN-LAST:event_pengajuanFPActionPerformed

    private void btnAksiKonfirmasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAksiKonfirmasiActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (noPesertaBPJS.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Maaf, No. Kartu Peserta tidak ada...!!!");
        } else {
            try (PreparedStatement ps = koneksi.prepareStatement("select id_user from user where user.id_user = aes_encrypt(?, 'nur') and password = aes_encrypt(?, 'windi') limit 1")) {
                ps.setString(1, new String(pwUserId.getPassword()));
                ps.setString(2, new String(pwPass.getPassword()));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (aksi.equals("Pengajuan")) {
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                utc = api.getUTCDateTimeAsString();
                                headers.add("X-Timestamp", utc);
                                headers.add("X-Signature", api.getHmac(utc));
                                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                url = URLAPIBPJS + "/Sep/pengajuanSEP";
                                json = " {"
                                    + "\"request\": {"
                                    + "\"t_sep\": {"
                                    + "\"noKartu\": \"" + noPesertaBPJS.getText() + "\","
                                    + "\"tglSep\": \"" + Valid.getTglSmc(tglSEP) + "\","
                                    + "\"jnsPelayanan\": \"" + jenisPelayanan.getSelectedItem().toString().substring(0, 1) + "\","
                                    + "\"jnsPengajuan\": \"2\","
                                    + "\"keterangan\": \"Pengajuan SEP Finger oleh Anjungan Pasien Mandiri RS Samarinda Medika Citra\","
                                    + "\"user\": \"" + new String(pwUserId.getPassword()) + "\""
                                    + "}"
                                    + "}"
                                    + "}";
                                requestEntity = new HttpEntity(json, headers);
                                root = mapper.readTree(api.getRest().exchange(url, HttpMethod.POST, requestEntity, String.class).getBody());
                                metadata = root.path("metaData");
                                System.out.println("code : " + metadata.path("code").asText());
                                System.out.println("message : " + metadata.path("message").asText());
                                if (metadata.path("code").asText().equals("200")) {
                                    JOptionPane.showMessageDialog(null, "Pengajuan Berhasil");
                                } else {
                                    JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : " + e);
                                if (e.toString().contains("UnknownHostException")) {
                                    JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                                }
                            }
                        } else if (aksi.equals("Approval")) {
                            try {
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                utc = api.getUTCDateTimeAsString();
                                headers.add("X-Timestamp", utc);
                                headers.add("X-Signature", api.getHmac(utc));
                                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                url = URLAPIBPJS + "/Sep/aprovalSEP";
                                json = " {"
                                    + "\"request\": {"
                                    + "\"t_sep\": {"
                                    + "\"noKartu\": \"" + noPesertaBPJS.getText() + "\","
                                    + "\"tglSep\": \"" + Valid.getTglSmc(tglSEP) + "\","
                                    + "\"jnsPelayanan\": \"" + jenisPelayanan.getSelectedItem().toString().substring(0, 1) + "\","
                                    + "\"jnsPengajuan\": \"2\","
                                    + "\"keterangan\": \"Approval FingerPrint karena Gagal FP melalui Anjungan Pasien Mandiri\","
                                    + "\"user\": \"" + new String(pwUserId.getPassword()) + "\""
                                    + "}"
                                    + "}"
                                    + "}";
                                requestEntity = new HttpEntity(json, headers);
                                root = mapper.readTree(api.getRest().exchange(url, HttpMethod.POST, requestEntity, String.class).getBody());
                                metadata = root.path("metaData");
                                System.out.println("code : " + metadata.path("code").asText());
                                System.out.println("message : " + metadata.path("message").asText());
                                if (metadata.path("code").asText().equals("200")) {
                                    JOptionPane.showMessageDialog(null, "Approval Berhasil");
                                } else {
                                    JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                }
                            } catch (Exception e) {
                                System.out.println("Notif : " + e);
                                if (e.toString().contains("UnknownHostException")) {
                                    JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Anda tidak diizinkan untuk melakukan aksi ini...!!!");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan pada saat memproses aksi...!!!");
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnAksiKonfirmasiActionPerformed

    private void pwUserIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwUserIdKeyPressed
        Valid.pindah(evt, btnAksiBatal, pwPass);
    }//GEN-LAST:event_pwUserIdKeyPressed

    private void pwPassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwPassKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAksiKonfirmasiActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            pwUserId.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            btnAksiKonfirmasi.requestFocus();
        }
    }//GEN-LAST:event_pwPassKeyPressed

    private void btnAksiBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAksiBatalActionPerformed
        resetAksi();
        WindowAksi.dispose();
    }//GEN-LAST:event_btnAksiBatalActionPerformed

    private void pilihSuratKontrolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihSuratKontrolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pilihSuratKontrolActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgRegistrasiSEPSMC dialog = new DlgRegistrasiSEPSMC(new javax.swing.JFrame(), true);
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
    private widget.Label LabelKelas;
    private widget.Label LabelPoli;
    private widget.Label LabelPoli2;
    private widget.Label LabelPoli7;
    private javax.swing.JDialog WindowAksi;
    private widget.Button approvalFP;
    private widget.ComboBox asalRujukan;
    private widget.ComboBox asesmenPelayanan;
    private component.Button batal;
    private widget.Button btnAksiBatal;
    private widget.Button btnAksiKonfirmasi;
    private widget.TextBox catatan;
    private widget.ComboBox flagProsedur;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel18;
    private widget.Label jLabel20;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel37;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private component.Panel jPanel1;
    private component.Panel jPanel2;
    private javax.swing.JPanel jPanel3;
    private widget.ComboBox jenisPelayanan;
    private widget.TextBox jenisPeserta;
    private widget.TextBox jk;
    private widget.TextBox jumlahBarcode;
    private widget.ComboBox katarak;
    private widget.ComboBox kelas;
    private widget.TextBox keterangan;
    private widget.TextBox kodeDPJPLayanan;
    private widget.TextBox kodeDiagnosaBPJS;
    private widget.TextBox kodeDokterBPJS;
    private widget.TextBox kodeKabKLL;
    private widget.TextBox kodeKecKLL;
    private widget.TextBox kodePPK;
    private widget.TextBox kodePPKPelayanan;
    private widget.TextBox kodePoliBPJS;
    private widget.TextBox kodeProvKLL;
    private widget.Label label1;
    private widget.Label label2;
    private widget.Label label3;
    private widget.Label labelKLL1;
    private widget.Label labelKLL2;
    private widget.Label labelKLL3;
    private widget.Label labelKLL4;
    private widget.Label labelKLL5;
    private widget.Label labelKLL6;
    private widget.Label labelKLL7;
    private widget.Label labelNoRawat;
    private widget.ComboBox lakaLantas;
    private widget.TextBox namaDPJPLayanan;
    private widget.TextBox namaDiagnosaBPJS;
    private widget.TextBox namaDokterBPJS;
    private widget.TextBox namaKabKLL;
    private widget.TextBox namaKecKLL;
    private widget.TextBox namaPPK;
    private widget.TextBox namaPPKPelayanan;
    private widget.TextBox namaPasien;
    private widget.TextBox namaPoliBPJS;
    private widget.TextBox namaProvKLL;
    private widget.TextBox nik;
    private widget.TextBox noPesertaBPJS;
    private widget.TextBox noRM;
    private widget.TextBox noRujukan;
    private widget.TextBox noSEPSuplesi;
    private widget.TextBox noSuratKontrol;
    private widget.TextBox noTelp;
    private widget.Button pengajuanFP;
    private widget.ComboBox penunjang;
    private widget.Button pilihDiagnosaBPJS;
    private widget.Button pilihDokterBPJS;
    private widget.Button pilihPoliBPJS;
    private widget.Button pilihRujukan;
    private widget.Button pilihSuratKontrol;
    private widget.PasswordBox pwPass;
    private widget.PasswordBox pwUserId;
    private widget.Button riwayatPelayananBPJS;
    private component.Button simpan;
    private widget.TextBox statusPeserta;
    private widget.ComboBox suplesi;
    private widget.Tanggal tglKLL;
    private widget.TextBox tglLahir;
    private widget.Tanggal tglRujukan;
    private widget.Tanggal tglSEP;
    private widget.ComboBox tujuanKunjungan;
    private component.Button validasi;
    // End of variables declaration//GEN-END:variables

    private void autoNomor() {
        if (labelNoRawat.getText().equals("")) {
            if (BASENOREG.equals("booking")) {
                switch (URUTNOREG) {
                    case "poli":
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(greatest((select ifnull(max(convert(booking_registrasi.no_reg, signed)), 0) + 1 from booking_registrasi where booking_registrasi.tanggal_periksa = ? and booking_registrasi.kd_poli = ?), "
                            + "(select ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1 from reg_periksa where reg_periksa.tgl_registrasi = ? and reg_periksa.kd_poli = ?)), 3, '0')", Valid.getTglSmc(tglSEP), kodePoliRS, Valid.getTglSmc(tglSEP), kodePoliRS
                        );
                        break;
                    case "dokter":
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(greatest((select ifnull(max(convert(booking_registrasi.no_reg, signed)), 0) + 1 from booking_registrasi where booking_registrasi.tanggal_periksa = ? and booking_registrasi.kd_dokter = ?), "
                            + "(select ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1 from reg_periksa where reg_periksa.tgl_registrasi = ? and reg_periksa.kd_dokter = ?)), 3, '0')", Valid.getTglSmc(tglSEP), kodeDokterRS, Valid.getTglSmc(tglSEP), kodeDokterRS
                        );
                        break;
                    case "poli+dokter":
                    case "dokter+poli":
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(greatest((select ifnull(max(convert(booking_registrasi.no_reg, signed)), 0) + 1 from booking_registrasi where booking_registrasi.tanggal_periksa = ? and booking_registrasi.kd_poli = ? and booking_registrasi.kd_dokter = ?), "
                            + "(select ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1 from reg_periksa where reg_periksa.tgl_registrasi = ? and reg_periksa.kd_poli = ? and reg_periksa.kd_dokter = ?)), 3, '0')", Valid.getTglSmc(tglSEP), kodePoliRS, kodeDokterRS, Valid.getTglSmc(tglSEP), kodePoliRS, kodeDokterRS
                        );
                        break;
                    default:
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(greatest((select ifnull(max(convert(booking_registrasi.no_reg, signed)), 0) + 1 from booking_registrasi where booking_registrasi.tanggal_periksa = ? and booking_registrasi.kd_dokter = ? and booking_registrasi.kd_poli = ?), "
                            + "(select ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1 from reg_periksa where reg_periksa.tgl_registrasi = ? and reg_periksa.kd_dokter = ? and reg_periksa.kd_poli = ?)), 3, '0')", Valid.getTglSmc(tglSEP), kodeDokterRS, kodePoliRS, Valid.getTglSmc(tglSEP), kodeDokterRS, kodePoliRS
                        );
                        break;
                }
            } else {
                switch (URUTNOREG) {
                    case "poli":
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1, 3, '0') from reg_periksa where reg_periksa.tgl_registrasi = ? "
                            + "and reg_periksa.kd_poli = ?", Valid.getTglSmc(tglSEP), kodePoliRS
                        );
                        break;
                    case "dokter":
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1, 3, '0') from reg_periksa where reg_periksa.tgl_registrasi = ? "
                            + "and reg_periksa.kd_dokter = ?", Valid.getTglSmc(tglSEP), kodeDokterRS
                        );
                        break;
                    case "poli+dokter":
                    case "dokter+poli":
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1, 3, '0') from reg_periksa where reg_periksa.tgl_registrasi = ? "
                            + "and reg_periksa.kd_poli = ? and reg_periksa.kd_dokter = ?", Valid.getTglSmc(tglSEP), kodePoliRS, kodeDokterRS
                        );
                        break;
                    default:
                        noReg = Sequel.cariIsiSmc(
                            "select lpad(ifnull(max(convert(reg_periksa.no_reg, signed)), 0) + 1, 3, '0') from reg_periksa where reg_periksa.tgl_registrasi = ? "
                            + "and reg_periksa.kd_poli = ? and reg_periksa.kd_dokter = ?", Valid.getTglSmc(tglSEP), kodePoliRS, kodeDokterRS
                        );
                        break;
                }
            }

            noRawat = Sequel.autoNomorSmc("/", "reg_periksa", "no_rawat", 6, "0", Valid.getTglSmc(tglSEP));
        }
    }

    private void cekStatusPasien() {
        if (labelNoRawat.getText().equals("")) {
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select pasien.nm_pasien, pasien.namakeluarga, pasien.keluarga, pasien.kd_pj, penjab.png_jawab, pasien.no_peserta, pasien.no_ktp, if(pasien.tgl_daftar = ?, "
                + "'Baru', 'Lama') as daftar, timestampdiff(year, pasien.tgl_lahir, curdate()) as tahun, timestampdiff(month, pasien.tgl_lahir, curdate()) - ((timestampdiff(month, "
                + "pasien.tgl_lahir, curdate()) div 12) * 12) as bulan, timestampdiff(day, date_add(date_add(pasien.tgl_lahir, interval timestampdiff(year, pasien.tgl_lahir, "
                + "curdate()) year), interval timestampdiff(month, pasien.tgl_lahir, curdate()) - ((timestampdiff(month, pasien.tgl_lahir, curdate()) div 12) * 12) month), "
                + "curdate()) as hari, concat_ws(', ', pasien.alamat, kelurahan.nm_kel, kecamatan.nm_kec, kabupaten.nm_kab) asal, from pasien join kelurahan on pasien.kd_kel "
                + "= kelurahan.kd_kel join kecamatan on pasien.kd_kec = kecamatan.kd_kec join kabupaten on pasien.kd_kab = kabupaten.kd_kab join penjab on pasien.kd_pj "
                + "= penjab.kd_pj where pasien.no_rkm_medis = ?"
            )) {
                ps.setString(1, Valid.getTglSmc(tglSEP));
                ps.setString(2, noRM.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        alamatPj = rs.getString("asal");
                        namaPj = rs.getString("namakeluarga");
                        hubunganPj = rs.getString("keluarga");
                        statusDaftar = rs.getString("daftar");
                        umur = "0";
                        statusUmur = "Th";
                        if (rs.getInt("tahun") > 0) {
                            umur = rs.getString("tahun");
                            statusUmur = "Th";
                        } else if (rs.getInt("tahun") == 0 && rs.getInt("bulan") > 0) {
                            umur = rs.getString("bulan");
                            statusUmur = "Bl";
                        } else if (rs.getInt("bulan") == 0 && rs.getInt("hari") > 0) {
                            umur = rs.getString("hari");
                            statusUmur = "Hr";
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
            statusPoli = "Baru";
            if (Sequel.cariBooleanSmc("select * from reg_periksa where reg_periksa.no_rkm_medis = ? and reg_periksa.kd_poli = ?", noRM.getText(), kodePoliRS)) {
                statusPoli = "Lama";
            }
        }
    }

    private void cetakRegistrasi(String noSEP) {
        Map<String, Object> param = new HashMap<>();
        param.put("norawat", noRawat);
        param.put("parameter", noSEP);
        param.put("namars", instansiNama);
        param.put("kotars", instansiKab);
        if (jenisPelayanan.getSelectedIndex() == 0) {
            Valid.printReport("rptBridgingSEPAPM1.jasper", koneksiDB.PRINTER_REGISTRASI(), "::[ Cetak SEP Model 4 ]::", 1, param);
            Valid.MyReport("rptBridgingSEPAPM1.jasper", "report", "::[ Cetak SEP Model 4 ]::", param);
        } else {
            Valid.printReport("rptBridgingSEPAPM2.jasper", koneksiDB.PRINTER_REGISTRASI(), "::[ Cetak SEP Model 4 ]::", 1, param);
            Valid.MyReport("rptBridgingSEPAPM2.jasper", "report", "::[ Cetak SEP Model 4 ]::", param);
        }
        Valid.printReport("rptBarcodeRawatAPM.jasper", koneksiDB.PRINTER_BARCODE(), "::[ Barcode Perawatan ]::", Integer.parseInt(jumlahBarcode.getText().trim()), param);
        Valid.MyReport("rptBarcodeRawatAPM.jasper", "report", "::[ Barcode Perawatan ]::", param);
    }

    private void insertSEP() {
        try {
            sTglKLL = "";
            if (lakaLantas.getSelectedIndex() > 0) {
                sTglKLL = Valid.getTglSmc(tglKLL);
            }
            
            utc = api.getUTCDateTimeAsString();
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());

            url = URLAPIBPJS + "/SEP/2.0/insert";
            json = "{"
                + "\"request\":{"
                + "\"t_sep\":{"
                + "\"noKartu\":\"" + noPesertaBPJS.getText() + "\","
                + "\"tglSep\":\"" + Valid.getTglSmc(tglSEP) + "\","
                + "\"ppkPelayanan\":\"" + kodePPKPelayanan.getText() + "\","
                + "\"jnsPelayanan\":\"" + jenisPelayanan.getSelectedItem().toString().substring(0, 1) + "\","
                + "\"klsRawat\":{"
                + "\"klsRawatHak\":\"" + kelas.getSelectedItem().toString().substring(0, 1) + "\","
                + "\"klsRawatNaik\":\"\","
                + "\"pembiayaan\":\"\","
                + "\"penanggungJawab\":\"\""
                + "},"
                + "\"noMR\":\"" + noRM.getText() + "\","
                + "\"rujukan\": {"
                + "\"asalRujukan\":\"" + asalRujukan.getSelectedItem().toString().substring(0, 1) + "\","
                + "\"tglRujukan\":\"" + Valid.getTglSmc(tglRujukan) + "\","
                + "\"noRujukan\":\"" + noRujukan.getText() + "\","
                + "\"ppkRujukan\":\"" + kodePPK.getText() + "\""
                + "},"
                + "\"catatan\":\"" + catatan.getText() + "\","
                + "\"diagAwal\":\"" + kodeDiagnosaBPJS.getText() + "\","
                + "\"poli\": {"
                + "\"tujuan\": \"" + kodePoliBPJS.getText() + "\","
                + "\"eksekutif\": \"0\""
                + "},"
                + "\"cob\": {"
                + "\"cob\": \"0\""
                + "},"
                + "\"katarak\": {"
                + "\"katarak\": \"" + katarak.getSelectedItem().toString().substring(0, 1) + "\""
                + "},"
                + "\"jaminan\": {"
                + "\"lakaLantas\":\"" + lakaLantas.getSelectedItem().toString().substring(0, 1) + "\","
                + "\"penjamin\": {"
                + "\"tglKejadian\": \"" + sTglKLL + "\","
                + "\"keterangan\": \"" + keterangan.getText() + "\","
                + "\"suplesi\": {"
                + "\"suplesi\": \"" + suplesi.getSelectedItem().toString().substring(0, 1) + "\","
                + "\"noSepSuplesi\": \"" + noSEPSuplesi.getText() + "\","
                + "\"lokasiLaka\": {"
                + "\"kdPropinsi\": \"" + kodeProvKLL.getText() + "\","
                + "\"kdKabupaten\": \"" + kodeKabKLL.getText() + "\","
                + "\"kdKecamatan\": \"" + kodeKecKLL.getText() + "\""
                + "}"
                + "}"
                + "}"
                + "},"
                + "\"tujuanKunj\": \"" + tujuanKunjungan.getSelectedItem().toString().substring(0, 1) + "\","
                + "\"flagProcedure\": \"" + (flagProsedur.getSelectedIndex() > 0 ? flagProsedur.getSelectedItem().toString().substring(0, 1) : "") + "\","
                + "\"kdPenunjang\": \"" + (penunjang.getSelectedIndex() > 0 ? penunjang.getSelectedIndex() + "" : "") + "\","
                + "\"assesmentPel\": \"" + (asesmenPelayanan.getSelectedIndex() > 0 ? asesmenPelayanan.getSelectedItem().toString().substring(0, 1) : "") + "\","
                + "\"skdp\": {"
                + "\"noSurat\": \"" + noSuratKontrol.getText() + "\","
                + "\"kodeDPJP\": \"" + kodeDokterBPJS.getText() + "\""
                + "},"
                + "\"dpjpLayan\": \"" + (kodeDPJPLayanan.getText().equals("") ? "" : kodeDPJPLayanan.getText()) + "\","
                + "\"noTelp\": \"" + noTelp.getText() + "\","
                + "\"user\":\"" + noPesertaBPJS.getText() + "\""
                + "}"
                + "}"
                + "}";

            requestEntity = new HttpEntity(json, headers);
            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.POST, requestEntity, String.class).getBody());
            metadata = root.path("metaData");

            System.out.println("code : " + metadata.path("code").asText());
            System.out.println("message : " + metadata.path("message").asText());
            JOptionPane.showMessageDialog(null, "Respon BPJS : " + metadata.path("message").asText());

            if (metadata.path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("sep").path("noSep");
                System.out.println("SEP berhasil terbit!");
                System.out.println("No. SEP: " + response.asText());
                if (!noRawat.equals(Sequel.cariIsiSmc(
                    "select reg_periksa.no_rawat from reg_periksa where reg_periksa.tgl_registrasi = ? and reg_periksa.no_rkm_medis = ? "
                    + "and reg_periksa.kd_poli = ? and reg_periksa.kd_dokter = ? and reg_periksa.kd_pj = ?", Valid.getTglSmc(tglSEP),
                    noRM.getText(), kodePoliRS, kodeDokterRS, CARABAYAR
                ))) {
                    System.out.println("Tidak dapat mendaftarkan pasien dengan no. rawat " + noRawat);
                    return;
                }
                
                Sequel.menyimpanSmc("bridging_sep", null, response.asText(), noRawat, Valid.getTglSmc(tglSEP), Valid.getTglSmc(tglRujukan), noRujukan.getText(),
                    kodePPK.getText(), namaPPK.getText(), kodePPKPelayanan.getText(), namaPPKPelayanan.getText(), jenisPelayanan.getSelectedItem().toString().substring(0, 1),
                    catatan.getText(), kodeDiagnosaBPJS.getText(), namaDiagnosaBPJS.getText(), kodePoliBPJS.getText(), namaPoliBPJS.getText(), kelas.getSelectedItem().toString().substring(0, 1),
                    "", "", "", lakaLantas.getSelectedItem().toString().substring(0, 1), noRM.getText(), noRM.getText(), namaPasien.getText(), tglLahir.getText(), jenisPeserta.getText(),
                    jk.getText(), noPesertaBPJS.getText(), "0000-00-00 00:00:00", asalRujukan.getSelectedItem().toString(), "0. Tidak", "0. Tidak", noTelp.getText(), katarak.getSelectedItem().toString(),
                    sTglKLL, keterangan.getText(), suplesi.getSelectedItem().toString(), noSEPSuplesi.getText(), kodeProvKLL.getText(), namaProvKLL.getText(), kodeKabKLL.getText(), namaKabKLL.getText(),
                    kodeKecKLL.getText(), namaKecKLL.getText(), noSuratKontrol.getText(), kodeDokterBPJS.getText(), namaDokterBPJS.getText(), tujuanKunjungan.getSelectedItem().toString().substring(0, 1),
                    (flagProsedur.getSelectedIndex() > 0 ? flagProsedur.getSelectedItem().toString().substring(0, 1) : ""), (penunjang.getSelectedIndex() > 0 ? String.valueOf(penunjang.getSelectedIndex()) : ""),
                    (asesmenPelayanan.getSelectedIndex() > 0 ? asesmenPelayanan.getSelectedItem().toString().substring(0, 1) : ""), kodeDPJPLayanan.getText(), namaDPJPLayanan.getText()
                );

                simpanRujukMasuk();

                if (jenisPelayanan.getSelectedIndex() == 1) {
                    Sequel.mengupdateSmc("bridging_sep", "tglpulang = ?", "no_sep = ?", Valid.getTglSmc(tglSEP), response.asText());
                }

                if (!prb.equals("")) {
                    Sequel.menyimpanSmc("bpjs_prb", null, response.asText(), prb);
                    prb = "";
                }

                if (Sequel.cariBooleanSmc(
                    "select * from booking_registrasi where booking_registrasi.no_rkm_medis = ? and booking_registrasi.tanggal_periksa = ? "
                    + "and booking_registrasi.kd_dokter = ? and booking_registrasi.kd_poli = ? and status != 'Terdaftar'",
                    noRM.getText(), Valid.getTglSmc(tglSEP), kodeDokterRS, kodePoliRS
                )) {
                    Sequel.mengupdateSmc("booking_registrasi",
                        "status = 'Terdaftar', waktu_kunjungan = now()",
                        "no_rkm_medis = ? and tanggal_periksa = ? and kd_dokter = ? and kd_poli = ?",
                        noRM.getText(), Valid.getTglSmc(tglSEP), kodeDokterRS, kodePoliRS
                    );
                }

                cetakRegistrasi(response.asText());
                emptTeks();
                dispose();
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (e.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
            }
        }
    }

    private void cekFinger() {
        statusfinger = false;
        if (!noPesertaBPJS.getText().isBlank()) {
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                utc = api.getUTCDateTimeAsString();
                headers.add("X-Timestamp", utc);
                headers.add("X-Signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                url = URLAPIBPJS + "/SEP/FingerPrint/Peserta/" + noPesertaBPJS.getText() + "/TglPelayanan/" + Valid.getTglSmc(tglSEP);
                requestEntity = new HttpEntity(headers);
                root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                metadata = root.path("metaData");
                System.out.println("Response FP : " + metadata.path("code").asText() + " " + metadata.path("message").asText());
                if (metadata.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                    if (response.path("kode").asText().equals("1")) {
                        if (response.path("status").asText().contains(Sequel.cariIsi("select current_date()"))) {
                            statusfinger = true;
                        } else {
                            statusfinger = false;
                            JOptionPane.showMessageDialog(null, response.path("status").asText());
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, response.path("status").asText());
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
                if (e.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data peserta!");
        }
    }

    public void tampilKodeBookingMobileJKN(String noBooking) {
        try (PreparedStatement psjkn = koneksi.prepareStatement(
            "select referensi_mobilejkn_bpjs.*, maping_poli_bpjs.nm_poli_bpjs, maping_poli_bpjs.kd_poli_rs, maping_dokter_dpjpvclaim.nm_dokter_bpjs, " +
            "maping_dokter_dpjpvclaim.kd_dokter from referensi_mobilejkn_bpjs join maping_poli_bpjs on referensi_mobilejkn_bpjs.kodepoli = " +
            "maping_poli_bpjs.kd_poli_bpjs join maping_dokter_dpjpvclaim on referensi_mobilejkn_bpjs.kodedokter = maping_dokter_dpjpvclaim.kd_dokter_bpjs " +
            "where referensi_mobilejkn_bpjs.nobooking = ? and referensi_mobilejkn_bpjs.tanggalperiksa = current_date() and " +
            "referensi_mobilejkn_bpjs.status in ('Belum', 'Checkin') and not exists(select * from pemeriksaan_ralan where " +
            "pemeriksaan_ralan.no_rawat = referensi_mobilejkn_bpjs.no_rawat)"
        )) {
            psjkn.setString(1, noBooking);
            try (ResultSet rsjkn = psjkn.executeQuery()) {
                if (rsjkn.next()) {
                    kodeBooking = rsjkn.getString("nobooking");
                    jenisKunjungan = rsjkn.getString("jeniskunjungan").substring(0, 1);
                    noRawat = rsjkn.getString("no_rawat");
                    labelNoRawat.setText(noRawat);
                    kodePoliBPJS.setText(rsjkn.getString("kodepoli"));
                    namaPoliBPJS.setText(rsjkn.getString("nm_poli_bpjs"));
                    kodePoliRS = rsjkn.getString("kd_poli_rs");
                    kodeDokterBPJS.setText(rsjkn.getString("kodedokter"));
                    namaDokterBPJS.setText(rsjkn.getString("nm_dokter_bpjs"));
                    kodeDokterRS = rsjkn.getString("kd_dokter");
                    kodeDPJPLayanan.setText(kodeDokterBPJS.getText());
                    namaDPJPLayanan.setText(namaDokterBPJS.getText());
                    noPesertaBPJS.setText(rsjkn.getString("nomorkartu"));
                    noRM.setText(rsjkn.getString("norm"));
                    nik.setText(rsjkn.getString("nik"));
                    noTelp.setText(rsjkn.getString("nohp"));
                    if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                        noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                    }
                    // CEK STATUS PASIEN
                    try {
                        url = URLAPIBPJS + "/Peserta/nokartu/" + rsjkn.getString("nomorkartu") + "/tglSEP/" + Valid.getTglSmc(tglSEP);
                        System.out.println("URL : " + url);
                        utc = api.getUTCDateTimeAsString();
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                        headers.add("X-Timestamp", utc);
                        headers.add("X-Signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                        requestEntity = new HttpEntity(headers);
                        root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                        metadata = root.path("metaData");
                        if (metadata.path("code").asText().equals("200")) {
                            response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("peserta");
                            switch (response.path("hakKelas").path("kode").asText()) {
                                case "1": kelas.setSelectedIndex(0); break;
                                case "2": kelas.setSelectedIndex(1); break;
                                case "3": kelas.setSelectedIndex(2); break;
                                default: break;
                            }
                            prb = response.path("informasi").path("prolanisPRB").asText();
                            if (prb.contains("null")) {
                                prb = "";
                            }
                            namaPasien.setText(response.path("nama").asText());
                            jk.setText(response.path("sex").asText());
                            statusPeserta.setText(response.path("statusPeserta").path("kode").asText() + " " + response.path("statusPeserta").path("keterangan").asText());
                            tglLahir.setText(response.path("tglLahir").asText());
                            jenisPeserta.setText(response.path("jenisPeserta").path("keterangan").asText());
                            if (jenisKunjungan.equals("1")) {
                                // RUJUKAN FKTP
                                asalRujukan.setSelectedIndex(0);
                                try {
                                    url = URLAPIBPJS + "/Rujukan/" + rsjkn.getString("nomorreferensi");
                                    System.out.println("URL : " + url);
                                    utc = api.getUTCDateTimeAsString();
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                    headers.add("X-Timestamp", utc);
                                    headers.add("X-Signature", api.getHmac(utc));
                                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                    requestEntity = new HttpEntity(headers);
                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                                    metadata = root.path("metaData");
                                    if (metadata.path("code").asText().equals("200")) {
                                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                        noRujukan.setText(response.path("noKunjungan").asText());
                                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                        Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                    } else {
                                        System.out.println("Notif : " + metadata.toString());
                                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                        emptTeks();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif : " + e);
                                    if (e.toString().contains("UnknownHostException")) {
                                        JOptionPane.showMessageDialog(null, "Koneksi ke Server BPJS terputus...!!!");
                                    }
                                    emptTeks();
                                }
                            } else if (jenisKunjungan.equals("4")) {
                                // RUJUKAN FKTL
                                asalRujukan.setSelectedIndex(1);
                                try {
                                    url = URLAPIBPJS + "/Rujukan/RS/" + rsjkn.getString("nomorreferensi");
                                    System.out.println("URL : " + url);
                                    utc = api.getUTCDateTimeAsString();
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                    headers.add("X-Timestamp", utc);
                                    headers.add("X-Signature", api.getHmac(utc));
                                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                    requestEntity = new HttpEntity(headers);
                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                                    metadata = root.path("metaData");
                                    if (metadata.path("code").asText().equals("200")) {
                                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                        noRujukan.setText(response.path("noKunjungan").asText());
                                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                        Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                    } else {
                                        System.out.println("Notif : " + metadata.asText());
                                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                        emptTeks();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif : " + e);
                                    if (e.toString().contains("UnknownHostException")) {
                                        JOptionPane.showMessageDialog(null, "Koneksi ke Server BPJS terputus...!!!");
                                    }
                                    emptTeks();
                                }
                            } else if (jenisKunjungan.equals("3")) {
                                // CEK JENIS KONTROL DULU
                                try (PreparedStatement pskontrol = koneksi.prepareStatement(
                                    "select bridging_surat_kontrol_bpjs.*, left(bridging_sep.asal_rujukan, 1) as asal_rujukan, bridging_sep.jnspelayanan, bridging_sep.no_rujukan, bridging_sep.klsrawat " +
                                    "from bridging_surat_kontrol_bpjs join bridging_sep on bridging_surat_kontrol_bpjs.no_sep = bridging_sep.no_sep where bridging_surat_kontrol_bpjs.no_surat = ?"
                                )) {
                                    pskontrol.setString(1, rsjkn.getString("nomorreferensi"));
                                    try (ResultSet rskontrol = pskontrol.executeQuery()) {
                                        if (rskontrol.next()) {
                                            if (!rskontrol.getString("tgl_rencana").equals(Valid.getTglSmc(tglSEP))) {
                                                updateSuratKontrol(
                                                    rskontrol.getString("no_surat"), rskontrol.getString("no_sep"), rsjkn.getString("nomorkartu"), Valid.getTglSmc(tglSEP),
                                                    rsjkn.getString("kodedokter"), rsjkn.getString("nm_dokter_bpjs"), rsjkn.getString("kodepoli"), rsjkn.getString("nm_poli_bpjs")
                                                );
                                            }
                                            if (rskontrol.getString("jnspelayanan").equals("1")) {
                                                // KONTROL POST RANAP
                                                kodeDiagnosaBPJS.setText("Z09.8");
                                                namaDiagnosaBPJS.setText("Follow-up examination after other treatment for other conditions");
                                                noRujukan.setText(rskontrol.getString("no_sep"));
                                                tujuanKunjungan.setSelectedIndex(0);
                                                flagProsedur.setSelectedIndex(0);
                                                penunjang.setSelectedIndex(0);
                                                asesmenPelayanan.setSelectedIndex(0);
                                                asalRujukan.setSelectedIndex(1);
                                                noSuratKontrol.setText(rskontrol.getString("no_surat"));
                                                kodePPK.setText(Sequel.cariIsiSmc("select kode_ppk from setting"));
                                                namaPPK.setText(Sequel.cariIsiSmc("select nama_instansi from setting"));
                                            } else {
                                                // KONTROL POLI
                                                try {
                                                    if (rskontrol.getString("asal_rujukan").equals("1")) {
                                                        url = URLAPIBPJS + "/Rujukan/" + rskontrol.getString("no_rujukan");
                                                        asalRujukan.setSelectedIndex(0);
                                                    } else if (rskontrol.getString("asal_rujukan").equals("2")) {
                                                        url = URLAPIBPJS + "/Rujukan/RS/" + rskontrol.getString("no_rujukan");
                                                        asalRujukan.setSelectedIndex(1);
                                                    }
                                                    utc = api.getUTCDateTimeAsString();
                                                    headers = new HttpHeaders();
                                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                                    headers.add("X-Timestamp", utc);
                                                    headers.add("X-Signature", api.getHmac(utc));
                                                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                                    requestEntity = new HttpEntity(headers);
                                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                                                    metadata = root.path("metaData");
                                                    System.out.println("URL : " + url);
                                                    if (metadata.path("code").asText().equals("200")) {
                                                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                                        noRujukan.setText(response.path("noKunjungan").asText());
                                                        noSuratKontrol.setText(rskontrol.getString("no_surat"));
                                                        tujuanKunjungan.setSelectedIndex(2);
                                                        flagProsedur.setSelectedIndex(0);
                                                        penunjang.setSelectedIndex(0);
                                                        asesmenPelayanan.setSelectedIndex(5);
                                                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                                        Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                                    } else {
                                                        System.out.println("Notif : " + metadata.asText());
                                                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                                        emptTeks();
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Notifikasi Peserta : " + e);
                                                    if (e.toString().contains("UnknownHostException")) {
                                                        JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                                                    }
                                                    emptTeks();
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Maaf, rujukan kontrol pasien tidak ditemukan!\nSilahkan hubungi administrasi.");
                                            emptTeks();
                                        }
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Maaf, rujukan kontrol pasien tidak ditemukan!\nSilahkan hubungi administrasi.");
                                    emptTeks();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Maaf, antrian JKN tidak ditemukan!\nSilahkan hubungi administrasi.");
                                emptTeks();
                            }
                        } else {
                            System.out.println("Notif : " + metadata.toString());
                            JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                            emptTeks();
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                        if (e.toString().contains("UnknownHostException")) {
                            JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                        }
                        emptTeks();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Maaf, pasien membatalkan antrian MobileJKN, atau telah menerima pelayanan!\nSilahkan hubungi administrasi.");
                    emptTeks();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan pada saat mencari rujukan di MobileJKN!\nSilahkan hubungi administrasi.");
            emptTeks();
        }
    }

    public void tampilNoKartuMobileJKN(String noKartu) {
        try (PreparedStatement psjkn = koneksi.prepareStatement(
            "select referensi_mobilejkn_bpjs.*, maping_poli_bpjs.nm_poli_bpjs, maping_poli_bpjs.kd_poli_rs, maping_dokter_dpjpvclaim.nm_dokter_bpjs, maping_dokter_dpjpvclaim.kd_dokter from referensi_mobilejkn_bpjs " +
            "join maping_poli_bpjs on referensi_mobilejkn_bpjs.kodepoli = maping_poli_bpjs.kd_poli_bpjs join maping_dokter_dpjpvclaim on referensi_mobilejkn_bpjs.kodedokter = maping_dokter_dpjpvclaim.kd_dokter_bpjs " +
            "where referensi_mobilejkn_bpjs.nomorkartu = ? and referensi_mobilejkn_bpjs.tanggalperiksa = current_date() and referensi_mobilejkn_bpjs.status in ('Belum', 'Checkin') and not exists(select * from " +
            "pemeriksaan_ralan where pemeriksaan_ralan.no_rawat = referensi_mobilejkn_bpjs.no_rawat)"
        )) {
            psjkn.setString(1, noKartu);
            try (ResultSet rsjkn = psjkn.executeQuery()) {
                if (rsjkn.next()) {
                    kodeBooking = rsjkn.getString("nobooking");
                    jenisKunjungan = rsjkn.getString("jeniskunjungan").substring(0, 1);
                    noRawat = rsjkn.getString("no_rawat");
                    labelNoRawat.setText(noRawat);
                    kodePoliBPJS.setText(rsjkn.getString("kodepoli"));
                    namaPoliBPJS.setText(rsjkn.getString("nm_poli_bpjs"));
                    kodePoliRS = rsjkn.getString("kd_poli_rs");
                    kodeDokterBPJS.setText(rsjkn.getString("kodedokter"));
                    namaDokterBPJS.setText(rsjkn.getString("nm_dokter_bpjs"));
                    kodeDokterRS = rsjkn.getString("kd_dokter");
                    kodeDPJPLayanan.setText(kodeDokterBPJS.getText());
                    namaDPJPLayanan.setText(namaDokterBPJS.getText());
                    noPesertaBPJS.setText(rsjkn.getString("nomorkartu"));
                    noRM.setText(rsjkn.getString("norm"));
                    nik.setText(rsjkn.getString("nik"));
                    noTelp.setText(rsjkn.getString("nohp"));
                    if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                        noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                    }
                    // CEK STATUS PASIEN
                    try {
                        url = URLAPIBPJS + "/Peserta/nokartu/" + rsjkn.getString("nomorkartu") + "/tglSEP/" + Valid.getTglSmc(tglSEP);
                        System.out.println("URL : " + url);
                        utc = api.getUTCDateTimeAsString();
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                        headers.add("X-Timestamp", utc);
                        headers.add("X-Signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                        requestEntity = new HttpEntity(headers);
                        root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                        metadata = root.path("metaData");
                        if (metadata.path("code").asText().equals("200")) {
                            response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("peserta");
                            switch (response.path("hakKelas").path("kode").asText()) {
                                case "1": kelas.setSelectedIndex(0); break;
                                case "2": kelas.setSelectedIndex(1); break;
                                case "3": kelas.setSelectedIndex(2); break;
                                default: break;
                            }
                            prb = response.path("informasi").path("prolanisPRB").asText();
                            if (prb.contains("null")) {
                                prb = "";
                            }
                            namaPasien.setText(response.path("nama").asText());
                            jk.setText(response.path("sex").asText());
                            statusPeserta.setText(response.path("statusPeserta").path("kode").asText() + " " + response.path("statusPeserta").path("keterangan").asText());
                            tglLahir.setText(response.path("tglLahir").asText());
                            jenisPeserta.setText(response.path("jenisPeserta").path("keterangan").asText());
                            if (jenisKunjungan.equals("1")) {
                                // RUJUKAN FKTP
                                asalRujukan.setSelectedIndex(0);
                                try {
                                    url = URLAPIBPJS + "/Rujukan/" + rsjkn.getString("nomorreferensi");
                                    System.out.println("URL : " + url);
                                    utc = api.getUTCDateTimeAsString();
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                    headers.add("X-Timestamp", utc);
                                    headers.add("X-Signature", api.getHmac(utc));
                                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                    requestEntity = new HttpEntity(headers);
                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                                    metadata = root.path("metaData");
                                    if (metadata.path("code").asText().equals("200")) {
                                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                        noRujukan.setText(response.path("noKunjungan").asText());
                                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                        Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                    } else {
                                        System.out.println("Notif : " + metadata.toString());
                                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                        emptTeks();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif : " + e);
                                    if (e.toString().contains("UnknownHostException")) {
                                        JOptionPane.showMessageDialog(null, "Koneksi ke Server BPJS terputus...!!!");
                                    }
                                    emptTeks();
                                }
                            } else if (jenisKunjungan.equals("4")) {
                                // RUJUKAN FKTL
                                asalRujukan.setSelectedIndex(1);
                                try {
                                    url = URLAPIBPJS + "/Rujukan/RS/" + rsjkn.getString("nomorreferensi");
                                    System.out.println("URL : " + url);
                                    utc = api.getUTCDateTimeAsString();
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                    headers.add("X-Timestamp", utc);
                                    headers.add("X-Signature", api.getHmac(utc));
                                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                    requestEntity = new HttpEntity(headers);
                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                                    metadata = root.path("metaData");
                                    if (metadata.path("code").asText().equals("200")) {
                                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                        noRujukan.setText(response.path("noKunjungan").asText());
                                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                        Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                    } else {
                                        System.out.println("Notif : " + metadata.asText());
                                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                        emptTeks();
                                    }
                                } catch (Exception e) {
                                    System.out.println("Notif : " + e);
                                    if (e.toString().contains("UnknownHostException")) {
                                        JOptionPane.showMessageDialog(null, "Koneksi ke Server BPJS terputus...!!!");
                                    }
                                    emptTeks();
                                }
                            } else if (jenisKunjungan.equals("3")) {
                                // CEK JENIS KONTROL DULU
                                try (PreparedStatement pskontrol = koneksi.prepareStatement(
                                    "select bridging_surat_kontrol_bpjs.*, left(bridging_sep.asal_rujukan, 1) as asal_rujukan, bridging_sep.jnspelayanan, bridging_sep.no_rujukan, bridging_sep.klsrawat " +
                                    "from bridging_surat_kontrol_bpjs join bridging_sep on bridging_surat_kontrol_bpjs.no_sep = bridging_sep.no_sep where bridging_surat_kontrol_bpjs.no_surat = ?"
                                )) {
                                    pskontrol.setString(1, rsjkn.getString("nomorreferensi"));
                                    try (ResultSet rskontrol = pskontrol.executeQuery()) {
                                        if (rskontrol.next()) {
                                            if (!rskontrol.getString("tgl_rencana").equals(Valid.getTglSmc(tglSEP))) {
                                                updateSuratKontrol(
                                                    rskontrol.getString("no_surat"), rskontrol.getString("no_sep"), rsjkn.getString("nomorkartu"), Valid.getTglSmc(tglSEP),
                                                    rsjkn.getString("kodedokter"), rsjkn.getString("nm_dokter_bpjs"), rsjkn.getString("kodepoli"), rsjkn.getString("nm_poli_bpjs")
                                                );
                                            }
                                            if (rskontrol.getString("jnspelayanan").equals("1")) {
                                                // KONTROL POST RANAP
                                                kodeDiagnosaBPJS.setText("Z09.8");
                                                namaDiagnosaBPJS.setText("Follow-up examination after other treatment for other conditions");
                                                noRujukan.setText(rskontrol.getString("no_sep"));
                                                tujuanKunjungan.setSelectedIndex(0);
                                                flagProsedur.setSelectedIndex(0);
                                                penunjang.setSelectedIndex(0);
                                                asesmenPelayanan.setSelectedIndex(0);
                                                asalRujukan.setSelectedIndex(1);
                                                noSuratKontrol.setText(rskontrol.getString("no_surat"));
                                                kodePPK.setText(Sequel.cariIsiSmc("select kode_ppk from setting"));
                                                namaPPK.setText(Sequel.cariIsiSmc("select nama_instansi from setting"));
                                            } else {
                                                // KONTROL POLI
                                                try {
                                                    if (rskontrol.getString("asal_rujukan").equals("1")) {
                                                        url = URLAPIBPJS + "/Rujukan/" + rskontrol.getString("no_rujukan");
                                                        asalRujukan.setSelectedIndex(0);
                                                    } else if (rskontrol.getString("asal_rujukan").equals("2")) {
                                                        url = URLAPIBPJS + "/Rujukan/RS/" + rskontrol.getString("no_rujukan");
                                                        asalRujukan.setSelectedIndex(1);
                                                    }
                                                    utc = api.getUTCDateTimeAsString();
                                                    headers = new HttpHeaders();
                                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                                                    headers.add("X-Timestamp", utc);
                                                    headers.add("X-Signature", api.getHmac(utc));
                                                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                                                    requestEntity = new HttpEntity(headers);
                                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                                                    metadata = root.path("metaData");
                                                    System.out.println("URL : " + url);
                                                    if (metadata.path("code").asText().equals("200")) {
                                                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                                        noRujukan.setText(response.path("noKunjungan").asText());
                                                        noSuratKontrol.setText(rskontrol.getString("no_surat"));
                                                        tujuanKunjungan.setSelectedIndex(2);
                                                        flagProsedur.setSelectedIndex(0);
                                                        penunjang.setSelectedIndex(0);
                                                        asesmenPelayanan.setSelectedIndex(5);
                                                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                                        Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                                    } else {
                                                        System.out.println("Notif : " + metadata.asText());
                                                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                                                        emptTeks();
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Notifikasi Peserta : " + e);
                                                    if (e.toString().contains("UnknownHostException")) {
                                                        JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                                                    }
                                                    emptTeks();
                                                }
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Maaf, rujukan kontrol pasien tidak ditemukan!\nSilahkan hubungi administrasi.");
                                            emptTeks();
                                        }
                                    }
                                } catch (Exception e) {
                                    JOptionPane.showMessageDialog(null, "Maaf, rujukan kontrol pasien tidak ditemukan!\nSilahkan hubungi administrasi.");
                                    emptTeks();
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "Maaf, antrian JKN tidak ditemukan!\nSilahkan hubungi administrasi.");
                                emptTeks();
                            }
                        } else {
                            System.out.println("Notif : " + metadata.toString());
                            JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                            emptTeks();
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : " + e);
                        if (e.toString().contains("UnknownHostException")) {
                            JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                        }
                        emptTeks();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Maaf, pasien membatalkan antrian MobileJKN, atau telah menerima pelayanan!\nSilahkan hubungi administrasi.");
                    emptTeks();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Maaf, terjadi kesalahan pada saat mencari rujukan di MobileJKN!\nSilahkan hubungi administrasi.");
            emptTeks();
        }
    }

    public void tampilKunjunganPertama(String noKartu) {
        try {
            url = URLAPIBPJS + "/Rujukan/Peserta/" + noKartu;
            utc = api.getUTCDateTimeAsString();
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            requestEntity = new HttpEntity(headers);
            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
            metadata = root.path("metaData");
            System.out.println("URL : " + url);
            if (metadata.path("code").asText().equals("200")) {
                asalRujukan.setSelectedIndex(0);
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                noRujukan.setText(response.path("noKunjungan").asText());
                switch (response.path("peserta").path("hakKelas").path("kode").asText()) {
                    case "1":
                        kelas.setSelectedIndex(0);
                        break;
                    case "2":
                        kelas.setSelectedIndex(1);
                        break;
                    case "3":
                        kelas.setSelectedIndex(2);
                        break;
                    default:
                        break;
                }
                prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                namaPasien.setText(response.path("peserta").path("nama").asText());
                noPesertaBPJS.setText(response.path("peserta").path("noKartu").asText());
                noRM.setText(Sequel.cariIsiSmc("select pasien.no_rkm_medis from pasien where pasien.no_peserta = ?", noPesertaBPJS.getText()));
                nik.setText(response.path("peserta").path("nik").asText());
                if (nik.getText().contains("null") || nik.getText().isBlank()) {
                    nik.setText(Sequel.cariIsiSmc("select no_ktp from pasien where no_rkm_medis = ?", noRM.getText()));
                }
                jk.setText(response.path("peserta").path("sex").asText());
                statusPeserta.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                tglLahir.setText(response.path("peserta").path("tglLahir").asText());
                kodePoliBPJS.setText(response.path("poliRujukan").path("kode").asText());
                namaPoliBPJS.setText(response.path("poliRujukan").path("nama").asText());
                jenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                kodePoliRS = Sequel.cariIsiSmc("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs = ?", response.path("poliRujukan").path("kode").asText());
                biayaRegistPoli();
                kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                Valid.SetTgl(tglRujukan, response.path("tglKunjungan").asText());
                catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                noTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                    noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                }
            } else {
                System.out.println("Pesan pencarian rujukan FKTP : " + metadata.path("message").asText());
                JOptionPane.showMessageDialog(null, "Pesan Pencarian Rujukan FKTP : " + metadata.path("message").asText());
                try {
                    url = URLAPIBPJS + "/Rujukan/RS/Peserta/" + noKartu;
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                    utc = api.getUTCDateTimeAsString();
                    headers.add("X-Timestamp", utc);
                    headers.add("X-Signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                    requestEntity = new HttpEntity(headers);
                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                    metadata = root.path("metaData");
                    if (metadata.path("code").asText().equals("200")) {
                        asalRujukan.setSelectedIndex(1);
                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                        noRujukan.setText(response.path("noKunjungan").asText());
                        switch (response.path("peserta").path("hakKelas").path("kode").asText()) {
                            case "1":
                                kelas.setSelectedIndex(0);
                                break;
                            case "2":
                                kelas.setSelectedIndex(1);
                                break;
                            case "3":
                                kelas.setSelectedIndex(2);
                                break;
                            default:
                                break;
                        }
                        prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                        namaPasien.setText(response.path("peserta").path("nama").asText());
                        noPesertaBPJS.setText(response.path("peserta").path("noKartu").asText());
                        noRM.setText(Sequel.cariIsiSmc("select no_rkm_medis from pasien where no_peserta = ?", noPesertaBPJS.getText()));
                        nik.setText(response.path("peserta").path("nik").asText());
                        if (nik.getText().contains("null") || nik.getText().isBlank()) {
                            nik.setText(Sequel.cariIsiSmc("select no_ktp from pasien where no_rkm_medis = ?", noRM.getText()));
                        }
                        jk.setText(response.path("peserta").path("sex").asText());
                        statusPeserta.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                        tglLahir.setText(response.path("peserta").path("tglLahir").asText());
                        kodePoliBPJS.setText(response.path("poliRujukan").path("kode").asText());
                        namaPoliBPJS.setText(response.path("poliRujukan").path("nama").asText());
                        jenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                        kodePoliRS = Sequel.cariIsi("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs=?", response.path("poliRujukan").path("kode").asText());
                        kodeDokterRS = Sequel.cariIsi("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs=?", kodeDokterBPJS.getText());
                        noTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                        if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                            noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                        }
                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                        Valid.SetTgl(tglRujukan, response.path("tglKunjungan").asText());
                        asalRujukan.setSelectedIndex(1);
                        autoNomor();
                        catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                    } else {
                        emptTeks();
                        JOptionPane.showMessageDialog(null, "Pesan Pencarian Rujukan FKRTL : " + metadata.path("message").asText());
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                    if (e.toString().contains("UnknownHostException")) {
                        JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (e.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
            }
        }

        try (PreparedStatement ps = koneksi.prepareStatement(
            "select maping_dokter_dpjpvclaim.kd_dokter, maping_dokter_dpjpvclaim.kd_dokter_bpjs, maping_dokter_dpjpvclaim.nm_dokter_bpjs "
            + "from maping_dokter_dpjpvclaim join jadwal on maping_dokter_dpjpvclaim.kd_dokter = jadwal.kd_dokter "
            + "where jadwal.kd_poli = ? and jadwal.hari_kerja = ?"
        )) {
            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    hari = "AKHAD";
                    break;
                case 2:
                    hari = "SENIN";
                    break;
                case 3:
                    hari = "SELASA";
                    break;
                case 4:
                    hari = "RABU";
                    break;
                case 5:
                    hari = "KAMIS";
                    break;
                case 6:
                    hari = "JUMAT";
                    break;
                case 7:
                    hari = "SABTU";
                    break;
                default:
                    break;
            }
            ps.setString(1, kodePoliRS);
            ps.setString(2, hari);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kodeDokterBPJS.setText(rs.getString("kd_dokter_bpjs"));
                    namaDokterBPJS.setText(rs.getString("nm_dokter_bpjs"));
                    kodeDPJPLayanan.setText(rs.getString("kd_dokter_bpjs"));
                    namaDPJPLayanan.setText(rs.getString("nm_dokter_bpjs"));
                    kodeDokterRS = rs.getString("kd_dokter");
                    autoNomor();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    public void tampilKunjunganBedaPoli(String noKartu) {
        tujuanKunjungan.setSelectedIndex(0);
        flagProsedur.setSelectedIndex(0);
        penunjang.setSelectedIndex(0);
        asesmenPelayanan.setSelectedIndex(1);
        try {
            url = URLAPIBPJS + "/Rujukan/Peserta/" + noKartu;
            utc = api.getUTCDateTimeAsString();
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            requestEntity = new HttpEntity(headers);
            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
            metadata = root.path("metaData");
            System.out.println("URL : " + url);
            if (metadata.path("code").asText().equals("200")) {
                asalRujukan.setSelectedIndex(0);
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                noRujukan.setText(response.path("noKunjungan").asText());
                switch (response.path("peserta").path("hakKelas").path("kode").asText()) {
                    case "1":
                        kelas.setSelectedIndex(0);
                        break;
                    case "2":
                        kelas.setSelectedIndex(1);
                        break;
                    case "3":
                        kelas.setSelectedIndex(2);
                        break;
                    default:
                        break;
                }
                prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                namaPasien.setText(response.path("peserta").path("nama").asText());
                noPesertaBPJS.setText(response.path("peserta").path("noKartu").asText());
                noRM.setText(Sequel.cariIsiSmc("select pasien.no_rkm_medis from pasien where pasien.no_peserta = ?", noPesertaBPJS.getText()));
                nik.setText(response.path("peserta").path("nik").asText());
                if (nik.getText().contains("null") || nik.getText().isBlank()) {
                    nik.setText(Sequel.cariIsiSmc("select no_ktp from pasien where no_rkm_medis = ?", noRM.getText()));
                }
                jk.setText(response.path("peserta").path("sex").asText());
                statusPeserta.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                tglLahir.setText(response.path("peserta").path("tglLahir").asText());
                kodePoliBPJS.setText(response.path("poliRujukan").path("kode").asText());
                namaPoliBPJS.setText(response.path("poliRujukan").path("nama").asText());
                jenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                kodePoliRS = Sequel.cariIsiSmc("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs = ?", response.path("poliRujukan").path("kode").asText());
                biayaRegistPoli();
                kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                Valid.SetTgl(tglRujukan, response.path("tglKunjungan").asText());
                catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                noTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                    noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                }
            } else {
                System.out.println("Pesan pencarian rujukan FKTP : " + metadata.path("message").asText());
                try {
                    url = URLAPIBPJS + "/Rujukan/RS/Peserta/" + noKartu;
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                    utc = api.getUTCDateTimeAsString();
                    headers.add("X-Timestamp", utc);
                    headers.add("X-Signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                    requestEntity = new HttpEntity(headers);
                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                    metadata = root.path("metaData");
                    if (metadata.path("code").asText().equals("200")) {
                        asalRujukan.setSelectedIndex(1);
                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                        kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                        namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                        noRujukan.setText(response.path("noKunjungan").asText());
                        switch (response.path("peserta").path("hakKelas").path("kode").asText()) {
                            case "1":
                                kelas.setSelectedIndex(0);
                                break;
                            case "2":
                                kelas.setSelectedIndex(1);
                                break;
                            case "3":
                                kelas.setSelectedIndex(2);
                                break;
                            default:
                                break;
                        }
                        prb = response.path("peserta").path("informasi").path("prolanisPRB").asText().replaceAll("null", "");
                        namaPasien.setText(response.path("peserta").path("nama").asText());
                        noPesertaBPJS.setText(response.path("peserta").path("noKartu").asText());
                        noRM.setText(Sequel.cariIsiSmc("select no_rkm_medis from pasien where no_peserta = ?", noPesertaBPJS.getText()));
                        nik.setText(response.path("peserta").path("nik").asText());
                        if (nik.getText().contains("null") || nik.getText().isBlank()) {
                            nik.setText(Sequel.cariIsiSmc("select no_ktp from pasien where no_rkm_medis = ?", noRM.getText()));
                        }
                        jk.setText(response.path("peserta").path("sex").asText());
                        statusPeserta.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                        tglLahir.setText(response.path("peserta").path("tglLahir").asText());
                        kodePoliBPJS.setText(response.path("poliRujukan").path("kode").asText());
                        namaPoliBPJS.setText(response.path("poliRujukan").path("nama").asText());
                        jenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                        kodePoliRS = Sequel.cariIsiSmc("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs = ?", response.path("poliRujukan").path("kode").asText());
                        noTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                        if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                            noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                        }
                        kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                        namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                        Valid.SetTgl(tglRujukan, response.path("tglKunjungan").asText());
                        asalRujukan.setSelectedIndex(1);
                        catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                    } else {
                        emptTeks();
                        System.out.println("Pesan pencarian rujukan FKTL : " + metadata.path("message").asText());
                        JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                    }
                } catch (Exception e) {
                    System.out.println("Notif : " + e);
                    if (e.toString().contains("UnknownHostException")) {
                        JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (e.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
            }
            emptTeks();
            return;
        }
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select maping_dokter_dpjpvclaim.kd_dokter, maping_dokter_dpjpvclaim.kd_dokter_bpjs, maping_dokter_dpjpvclaim.nm_dokter_bpjs "
            + "from maping_dokter_dpjpvclaim join jadwal on maping_dokter_dpjpvclaim.kd_dokter = jadwal.kd_dokter "
            + "where jadwal.kd_poli = ? and jadwal.hari_kerja = ?"
        )) {
            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    hari = "AKHAD";
                    break;
                case 2:
                    hari = "SENIN";
                    break;
                case 3:
                    hari = "SELASA";
                    break;
                case 4:
                    hari = "RABU";
                    break;
                case 5:
                    hari = "KAMIS";
                    break;
                case 6:
                    hari = "JUMAT";
                    break;
                case 7:
                    hari = "SABTU";
                    break;
                default:
                    break;
            }
            ps.setString(1, kodePoliRS);
            ps.setString(2, hari);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    kodeDokterBPJS.setText(rs.getString("kd_dokter_bpjs"));
                    namaDokterBPJS.setText(rs.getString("nm_dokter_bpjs"));
                    kodeDPJPLayanan.setText(rs.getString("kd_dokter_bpjs"));
                    namaDPJPLayanan.setText(rs.getString("nm_dokter_bpjs"));
                    kodeDokterRS = rs.getString("kd_dokter");
                    autoNomor();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    public void tampilKontrol(String noSKDP) {
        try (PreparedStatement ps = koneksi.prepareStatement(
            "select bridging_surat_kontrol_bpjs.*, bridging_sep.no_kartu, left(bridging_sep.asal_rujukan, 1) as asal_rujukan, bridging_sep.jnspelayanan, "
            + "bridging_sep.no_rujukan, bridging_sep.klsrawat from bridging_surat_kontrol_bpjs join bridging_sep on bridging_surat_kontrol_bpjs.no_sep "
            + "= bridging_sep.no_sep where bridging_surat_kontrol_bpjs.no_surat = ?"
        )) {
            ps.setString(1, noSKDP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (!rs.getString("tgl_rencana").equals(Valid.getTglSmc(tglSEP))) {
                        updateSuratKontrol(rs.getString("no_surat"), rs.getString("no_sep"), rs.getString("no_kartu"), Valid.getTglSmc(tglSEP),
                            rs.getString("kd_dokter_bpjs"), rs.getString("nm_dokter_bpjs"), rs.getString("kd_poli_bpjs"), rs.getString("nm_poli_bpjs")
                        );
                    }
                    if (rs.getString("jnspelayanan").equals("1")) {
                        try {
                            url = URLAPIBPJS + "/Peserta/nokartu/" + rs.getString("no_kartu") + "/tglSEP/" + Valid.getTglSmc(tglSEP);
                            utc = api.getUTCDateTimeAsString();
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                            headers.add("X-Timestamp", utc);
                            headers.add("X-Signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                            requestEntity = new HttpEntity(headers);
                            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                            metadata = root.path("metaData");
                            System.out.println("URL : " + url);
                            if (metadata.path("code").asText().equals("200")) {
                                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("peserta");
                                kodeDiagnosaBPJS.setText("Z09.8");
                                namaDiagnosaBPJS.setText("Z09.8 - Follow-up examination after other treatment for other conditions");
                                noRujukan.setText(rs.getString("no_sep"));
                                tujuanKunjungan.setSelectedIndex(0);
                                flagProsedur.setSelectedIndex(0);
                                penunjang.setSelectedIndex(0);
                                asesmenPelayanan.setSelectedIndex(0);
                                asalRujukan.setSelectedIndex(1);
                                kodePoliBPJS.setText(rs.getString("kd_poli_bpjs"));
                                namaPoliBPJS.setText(rs.getString("nm_poli_bpjs"));
                                kodeDokterBPJS.setText(rs.getString("kd_dokter_bpjs"));
                                namaDokterBPJS.setText(rs.getString("nm_dokter_bpjs"));
                                kodeDPJPLayanan.setText(kodeDokterBPJS.getText());
                                namaDPJPLayanan.setText(namaDokterBPJS.getText());
                                kodePoliRS = Sequel.cariIsiSmc("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs = ?", kodePoliBPJS.getText());
                                kodeDokterRS = Sequel.cariIsiSmc("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs = ?", kodeDokterBPJS.getText());
                                noSuratKontrol.setText(rs.getString("no_surat"));
                                switch (rs.getString("klsrawat")) {
                                    case "1":
                                        kelas.setSelectedIndex(0);
                                        break;
                                    case "2":
                                        kelas.setSelectedIndex(1);
                                        break;
                                    case "3":
                                        kelas.setSelectedIndex(2);
                                        break;
                                    default:
                                        break;
                                }
                                prb = response.path("informasi").path("prolanisPRB").asText();
                                if (prb.contains("null")) {
                                    prb = "";
                                }
                                namaPasien.setText(response.path("nama").asText());
                                noPesertaBPJS.setText(response.path("noKartu").asText());
                                noRM.setText(Sequel.cariIsiSmc("select no_rkm_medis from pasien where no_peserta = ?", noPesertaBPJS.getText()));
                                nik.setText(response.path("nik").asText());
                                if (nik.getText().contains("null") || nik.getText().isBlank()) {
                                    nik.setText(Sequel.cariIsiSmc("select no_ktp from pasien where no_rkm_medis = ?", noRM.getText()));
                                }
                                jk.setText(response.path("sex").asText());
                                statusPeserta.setText(response.path("statusPeserta").path("kode").asText() + " " + response.path("statusPeserta").path("keterangan").asText());
                                tglLahir.setText(response.path("tglLahir").asText());
                                jenisPeserta.setText(response.path("jenisPeserta").path("keterangan").asText());
                                kodePPK.setText(Sequel.cariIsiSmc("select kode_ppk from setting"));
                                namaPPK.setText(Sequel.cariIsiSmc("select nama_instansi from setting"));
                                autoNomor();
                                catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                                noTelp.setText(response.path("mr").path("noTelepon").asText());
                                if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                                    noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                                }
                            } else {
                                emptTeks();
                                JOptionPane.showMessageDialog(null, metadata.path("message").asText());
                            }
                        } catch (Exception ex) {
                            System.out.println("Notif : " + ex);
                            if (ex.toString().contains("UnknownHostException")) {
                                JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                            }
                        }
                    } else {
                        try {
                            if (rs.getString("asal_rujukan").equals("1")) {
                                url = URLAPIBPJS + "/Rujukan/" + rs.getString("no_rujukan");
                            } else if (rs.getString("asal_rujukan").equals("2")) {
                                url = URLAPIBPJS + "/Rujukan/RS/" + rs.getString("no_rujukan");
                            }
                            utc = api.getUTCDateTimeAsString();
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
                            headers.add("X-Timestamp", utc);
                            headers.add("X-Signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
                            requestEntity = new HttpEntity(headers);
                            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.GET, requestEntity, String.class).getBody());
                            metadata = root.path("metaData");
                            System.out.println("URL : " + url);
                            if (metadata.path("code").asText().equals("200")) {
                                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc)).path("rujukan");
                                kodeDiagnosaBPJS.setText(response.path("diagnosa").path("kode").asText());
                                namaDiagnosaBPJS.setText(response.path("diagnosa").path("nama").asText());
                                noRujukan.setText(response.path("noKunjungan").asText());
                                noSuratKontrol.setText(rs.getString("no_surat"));
                                kodePoliBPJS.setText(rs.getString("kd_poli_bpjs"));
                                namaPoliBPJS.setText(rs.getString("nm_poli_bpjs"));
                                kodeDokterBPJS.setText(rs.getString("kd_dokter_bpjs"));
                                namaDokterBPJS.setText(rs.getString("nm_dokter_bpjs"));
                                kodeDPJPLayanan.setText(kodeDokterBPJS.getText());
                                namaDPJPLayanan.setText(namaDokterBPJS.getText());
                                kodePoliRS = Sequel.cariIsiSmc("select kd_poli_rs from maping_poli_bpjs where kd_poli_bpjs = ?", kodePoliBPJS.getText());
                                kodeDokterRS = Sequel.cariIsiSmc("select kd_dokter from maping_dokter_dpjpvclaim where kd_dokter_bpjs = ?", kodeDokterBPJS.getText());
                                tujuanKunjungan.setSelectedIndex(2);
                                flagProsedur.setSelectedIndex(0);
                                penunjang.setSelectedIndex(0);
                                asesmenPelayanan.setSelectedIndex(5);
                                if (rs.getString("asal_rujukan").equals("2")) {
                                    asalRujukan.setSelectedIndex(1);
                                } else {
                                    asalRujukan.setSelectedIndex(0);
                                }
                                switch (response.path("peserta").path("hakKelas").path("kode").asText()) {
                                    case "1":
                                        kelas.setSelectedIndex(0);
                                        break;
                                    case "2":
                                        kelas.setSelectedIndex(1);
                                        break;
                                    case "3":
                                        kelas.setSelectedIndex(2);
                                        break;
                                    default:
                                        break;
                                }
                                prb = response.path("peserta").path("informasi").path("prolanisPRB").asText();
                                if (prb.contains("null")) {
                                    prb = "";
                                }
                                namaPasien.setText(response.path("peserta").path("nama").asText());
                                noPesertaBPJS.setText(response.path("peserta").path("noKartu").asText());
                                noRM.setText(Sequel.cariIsiSmc("select pasien.no_rkm_medis from pasien where pasien.no_peserta = ?", noPesertaBPJS.getText()));
                                nik.setText(response.path("peserta").path("nik").asText());
                                if (nik.getText().contains("null") || nik.getText().isBlank()) {
                                    nik.setText(Sequel.cariIsiSmc("select no_ktp from pasien where no_rkm_medis = ?", noRM.getText()));
                                }
                                jk.setText(response.path("peserta").path("sex").asText());
                                statusPeserta.setText(response.path("peserta").path("statusPeserta").path("kode").asText() + " " + response.path("peserta").path("statusPeserta").path("keterangan").asText());
                                tglLahir.setText(response.path("peserta").path("tglLahir").asText());
                                jenisPeserta.setText(response.path("peserta").path("jenisPeserta").path("keterangan").asText());
                                biayaRegistPoli();
                                kodePPK.setText(response.path("provPerujuk").path("kode").asText());
                                namaPPK.setText(response.path("provPerujuk").path("nama").asText());
                                Valid.setTglSmc(tglRujukan, response.path("tglKunjungan").asText());
                                autoNomor();
                                catatan.setText("Anjungan Pasien Mandiri RS Samarinda Medika Citra");
                                noTelp.setText(response.path("peserta").path("mr").path("noTelepon").asText());
                                if (noTelp.getText().contains("null") || noTelp.getText().isBlank()) {
                                    noTelp.setText(Sequel.cariIsiSmc("select no_tlp from pasien where no_rkm_medis = ?", noRM.getText()));
                                }
                            } else {
                                emptTeks();
                                System.out.println("Pesan pencarian rujukan : " + metadata.path("message").asText());
                            }
                        } catch (Exception ex) {
                            System.out.println("Notif : " + ex);
                            if (ex.toString().contains("UnknownHostException")) {
                                JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Maaf, Data surat kontrol tidak ditemukan...!!!");
        }
    }

    private boolean addAntreanOnsite() {
        String code = "200", pesan = "";
        boolean output = true;
        
        if (!kodeBooking.equals("")) {
            if (Sequel.cariBooleanSmc("select * from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.nobooking = ? and referensi_mobilejkn_bpjs.status = 'Belum'", kodeBooking)) {
                Sequel.mengupdateSmc("referensi_mobilejkn_bpjs", "referensi_mobilejkn_bpjs.validasi = now(), referensi_mobilejkn_bpjs.status = 'Checkin'", "referensi_mobilejkn_bpjs.nobooking = ? and referensi_mobilejkn_bpjs.status = 'Belum'", kodeBooking);
                Sequel.mengupdateSmc("reg_periksa", "reg_periksa.jam_reg = current_time()", "reg_periksa.no_rawat = ? and stts != 'Batal'", noRawat);
            }
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select referensi_mobilejkn_bpjs.*, reg_periksa.no_rkm_medis, pasien.nm_pasien, poliklinik.nm_poli, dokter.nm_dokter from referensi_mobilejkn_bpjs " +
                "join reg_periksa on referensi_mobilejkn_bpjs.no_rawat = reg_periksa.no_rawat join pasien on reg_periksa.no_rkm_medis = pasien.no_rkm_medis " +
                "join poliklinik on reg_periksa.kd_poli = poliklinik.kd_poli join dokter on reg_periksa.kd_dokter = dokter.kd_dokter " +
                "where referensi_mobilejkn_bpjs.statuskirim = 'Belum' and referensi_mobilejkn_bpjs.nobooking = ? and referensi_mobilejkn.status = 'Checkin'"
            )) {
                ps.setString(1, kodeBooking);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try {
                            utc = api.getUTCDateTimeAsString();
                            headers = new HttpHeaders();
                            headers.setContentType(MediaType.APPLICATION_JSON);
                            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                            headers.add("x-timestamp", utc);
                            headers.add("x-signature", api.getHmac(utc));
                            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                            json = "{"
                                + "\"kodebooking\": \"" + rs.getString("nobooking") + "\","
                                + "\"jenispasien\": \"JKN\","
                                + "\"nomorkartu\": \"" + rs.getString("nomorkartu") + "\","
                                + "\"nik\": \"" + rs.getString("nik") + "\","
                                + "\"nohp\": \"" + noTelp.getText().trim() + "\","
                                + "\"kodepoli\": \"" + rs.getString("kodepoli") + "\","
                                + "\"namapoli\": \"" + rs.getString("nm_poli") + "\","
                                + "\"pasienbaru\": " + rs.getString("pasienbaru") + ","
                                + "\"norm\": \"" + rs.getString("no_rkm_medis") + "\","
                                + "\"tanggalperiksa\": \"" + rs.getString("tanggalperiksa") + "\","
                                + "\"kodedokter\": " + rs.getString("kodedokter") + ","
                                + "\"namadokter\": \"" + rs.getString("nm_dokter") + "\","
                                + "\"jampraktek\": \"" + rs.getString("jampraktek") + "\","
                                + "\"jeniskunjungan\": " + rs.getString("jeniskunjungan").substring(0, 1) + ","
                                + "\"nomorreferensi\": \"" + rs.getString("nomorreferensi") + "\","
                                + "\"nomorantrean\": \"" + rs.getString("nomorantrean") + "\","
                                + "\"angkaantrean\": " + rs.getInt("angkaantrean") + ","
                                + "\"estimasidilayani\": " + rs.getString("estimasidilayani") + ","
                                + "\"sisakuotajkn\": " + rs.getInt("sisakuotajkn") + ","
                                + "\"kuotajkn\": " + rs.getInt("kuotajkn") + ","
                                + "\"sisakuotanonjkn\": " + rs.getInt("sisakuotanonjkn") + ","
                                + "\"kuotanonjkn\": " + rs.getInt("kuotanonjkn") + ","
                                + "\"keterangan\": \"Peserta harap 30 menit lebih awal guna pencatatan administrasi.\""
                                + "}";
                            requestEntity = new HttpEntity(json, headers);
                            url = koneksiDB.URLAPIMOBILEJKN() + "/antrean/add";
                            System.out.println("URL : " + url);
                            System.out.println("JSON : " + json);
                            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.POST, requestEntity, String.class).getBody());
                            metadata = root.path("metadata");
                            code = metadata.path("code").asText();
                            Sequel.logTaskid(noRawat, noRawat, "Onsite", "addantrean", json, metadata.path("code").asText(), metadata.path("message").asText(), root.toString(), dataJam);
                            System.out.println("Response addantrean SKDP : " + metadata.path("code").asText() + " " + metadata.path("message").asText() + "\n");
                        } catch (Exception e) {
                            output = false;
                            System.out.println("Notif SKDP : " + e);
                        }
                    }
                }
            } catch (Exception e) {
                output = false;
                System.out.println("Notif : " + e);
            }
        } else {
            int angkaAntrean = Integer.parseInt(noReg);
            if ((!noRujukan.getText().equals("")) || (!noSuratKontrol.getText().equals(""))) {
                if (tujuanKunjungan.getSelectedItem().toString().equals("0. Normal") && flagProsedur.getSelectedItem().toString().equals("") && penunjang.getSelectedItem().toString().equals("") && asesmenPelayanan.getSelectedItem().toString().equals("")) {
                    if (asalRujukan.getSelectedIndex() == 0) {
                        jenisKunjungan = "1";
                    } else {
                        jenisKunjungan = "4";
                    }
                } else if (tujuanKunjungan.getSelectedItem().toString().equals("2. Konsul Dokter") && flagProsedur.getSelectedItem().toString().equals("") && penunjang.getSelectedItem().toString().equals("") && asesmenPelayanan.getSelectedItem().toString().equals("5. Tujuan Kontrol")) {
                    jenisKunjungan = "3";
                } else if (tujuanKunjungan.getSelectedItem().toString().equals("0. Normal") && flagProsedur.getSelectedItem().toString().equals("") && penunjang.getSelectedItem().toString().equals("") && asesmenPelayanan.getSelectedItem().toString().equals("4. Atas Instruksi RS")) {
                    jenisKunjungan = "2";
                } else if (tujuanKunjungan.getSelectedItem().toString().equals("0. Normal") && flagProsedur.getSelectedItem().toString().equals("") && penunjang.getSelectedItem().toString().equals("") && asesmenPelayanan.getSelectedItem().toString().equals("1. Poli spesialis tidak tersedia pada hari sebelumnya")) {
                    jenisKunjungan = "2";
                } else {
                    if (tujuanKunjungan.getSelectedItem().toString().equals("2. Konsul Dokter") && asesmenPelayanan.getSelectedItem().toString().equals("5. Tujuan Kontrol")) {
                        jenisKunjungan = "3";
                    } else {
                        jenisKunjungan = "2";
                    }
                }

                try {
                    if (jamMulai.isBlank() || jamSelesai.isBlank()) {
                        switch (cal.get(Calendar.DAY_OF_WEEK)) {
                            case 1:
                                hari = "AKHAD";
                                break;
                            case 2:
                                hari = "SENIN";
                                break;
                            case 3:
                                hari = "SELASA";
                                break;
                            case 4:
                                hari = "RABU";
                                break;
                            case 5:
                                hari = "KAMIS";
                                break;
                            case 6:
                                hari = "JUMAT";
                                break;
                            case 7:
                                hari = "SABTU";
                                break;
                            default:
                                output = false;
                                break;
                        }

                        if (output) {
                            try (PreparedStatement ps = koneksi.prepareStatement(
                                "select jadwal.jam_mulai, jadwal.jam_selesai, kuota from jadwal where jadwal.hari_kerja = ? and jadwal.kd_poli = ? and jadwal.kd_dokter = ?"
                            )) {
                                ps.setString(1, hari);
                                ps.setString(2, kodePoliRS);
                                ps.setString(3, kodeDokterRS);
                                try (ResultSet rs = ps.executeQuery()) {
                                    if (rs.next()) {
                                        jamMulai = rs.getString("jam_mulai");
                                        jamSelesai = rs.getString("jam_selesai");
                                        kuota = rs.getInt("kuota");
                                        dataJam = Sequel.cariIsiSmc("select date_add(concat(?, ' ', ?), interval ? minute)", Valid.getTglSmc(tglSEP), jamMulai, String.valueOf(angkaAntrean * 5));
                                        parsedDate = dateFormat.parse(dataJam);
                                    } else {
                                        output = false;
                                        pesan = "Jadwal praktek dokter tidak ditemukan..!!";
                                        System.out.println("Jadwal tidak ditemukan..!!");
                                    }
                                }
                            } catch (Exception e) {
                                output = false;
                                pesan = "Terjadi kesalahan pada saat mencari jadwal praktek dokter..!!";
                                System.out.println("Notif : " + e);
                            }
                        }
                    }

                    if (output) {
                        if (!noSuratKontrol.getText().equals("")) {
                            try {
                                utc = api.getUTCDateTimeAsString();
                                headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                                headers.add("x-timestamp", utc);
                                headers.add("x-signature", api.getHmac(utc));
                                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                                json = "{"
                                    + "\"kodebooking\": \"" + noRawat + "\","
                                    + "\"jenispasien\": \"JKN\","
                                    + "\"nomorkartu\": \"" + noPesertaBPJS.getText() + "\","
                                    + "\"nik\": \"" + nik.getText() + "\","
                                    + "\"nohp\": \"" + noTelp.getText() + "\","
                                    + "\"kodepoli\": \"" + kodePoliBPJS.getText() + "\","
                                    + "\"namapoli\": \"" + namaPoliBPJS.getText() + "\","
                                    + "\"pasienbaru\": 0,"
                                    + "\"norm\": \"" + noRM.getText() + "\","
                                    + "\"tanggalperiksa\": \"" + Valid.getTglSmc(tglSEP) + "\","
                                    + "\"kodedokter\": " + kodeDokterBPJS.getText() + ","
                                    + "\"namadokter\": \"" + namaDokterBPJS.getText() + "\","
                                    + "\"jampraktek\": \"" + jamMulai.substring(0, 5) + "-" + jamSelesai.substring(0, 5) + "\","
                                    + "\"jeniskunjungan\": " + jenisKunjungan + ","
                                    + "\"nomorreferensi\": \"" + noSuratKontrol.getText() + "\","
                                    + "\"nomorantrean\": \"" + noReg + "\","
                                    + "\"angkaantrean\": " + angkaAntrean + ","
                                    + "\"estimasidilayani\": " + parsedDate.getTime() + ","
                                    + "\"sisakuotajkn\": " + (kuota - angkaAntrean) + ","
                                    + "\"kuotajkn\": " + kuota + ","
                                    + "\"sisakuotanonjkn\": " + (kuota - angkaAntrean) + ","
                                    + "\"kuotanonjkn\": " + kuota + ","
                                    + "\"keterangan\": \"Peserta harap 30 menit lebih awal guna pencatatan administrasi.\""
                                    + "}";
                                requestEntity = new HttpEntity(json, headers);
                                url = koneksiDB.URLAPIMOBILEJKN() + "/antrean/add";
                                System.out.println("URL : " + url);
                                System.out.println("JSON : " + json);
                                root = mapper.readTree(api.getRest().exchange(url, HttpMethod.POST, requestEntity, String.class).getBody());
                                metadata = root.path("metadata");
                                code = metadata.path("code").asText();
                                Sequel.logTaskid(noRawat, noRawat, "Onsite", "addantrean", json, metadata.path("code").asText(), metadata.path("message").asText(), root.toString(), dataJam);
                                System.out.println("Response addantrean SKDP : " + metadata.path("code").asText() + " " + metadata.path("message").asText() + "\n");
                            } catch (Exception e) {
                                output = false;
                                System.out.println("Notif SKDP : " + e);
                            }
                        }
                    }

                    if (output) {
                        if (code.equals("201")) {
                            pesan = "Response addantrean SKDP : " + metadata.path("message").asText();
                            if (!noRujukan.getText().isBlank()) {
                                try {
                                    headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_JSON);
                                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                                    utc = api.getUTCDateTimeAsString();
                                    headers.add("x-timestamp", utc);
                                    headers.add("x-signature", api.getHmac(utc));
                                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                                    json = "{"
                                        + "\"kodebooking\": \"" + noRawat + "\","
                                        + "\"jenispasien\": \"JKN\","
                                        + "\"nomorkartu\": \"" + noPesertaBPJS.getText() + "\","
                                        + "\"nik\": \"" + nik.getText() + "\","
                                        + "\"nohp\": \"" + noTelp.getText() + "\","
                                        + "\"kodepoli\": \"" + kodePoliBPJS.getText() + "\","
                                        + "\"namapoli\": \"" + namaPoliBPJS.getText() + "\","
                                        + "\"pasienbaru\": 0,"
                                        + "\"norm\": \"" + noRM.getText() + "\","
                                        + "\"tanggalperiksa\": \"" + Valid.getTglSmc(tglSEP) + "\","
                                        + "\"kodedokter\": " + kodeDokterBPJS.getText() + ","
                                        + "\"namadokter\": \"" + namaDokterBPJS.getText() + "\","
                                        + "\"jampraktek\": \"" + jamMulai.substring(0, 5) + "-" + jamSelesai.substring(0, 5) + "\","
                                        + "\"jeniskunjungan\": " + jenisKunjungan + ","
                                        + "\"nomorreferensi\": \"" + noRujukan.getText() + "\","
                                        + "\"nomorantrean\": \"" + noReg + "\","
                                        + "\"angkaantrean\": " + angkaAntrean + ","
                                        + "\"estimasidilayani\": " + parsedDate.getTime() + ","
                                        + "\"sisakuotajkn\": " + (kuota - angkaAntrean) + ","
                                        + "\"kuotajkn\": " + kuota + ","
                                        + "\"sisakuotanonjkn\": " + (kuota - angkaAntrean) + ","
                                        + "\"kuotanonjkn\": " + kuota + ","
                                        + "\"keterangan\": \"Peserta harap 30 menit lebih awal guna pencatatan administrasi.\""
                                        + "}";
                                    requestEntity = new HttpEntity(json, headers);
                                    url = koneksiDB.URLAPIMOBILEJKN() + "/antrean/add";
                                    System.out.println("URL : " + url);
                                    System.out.println("JSON : " + json);
                                    root = mapper.readTree(api.getRest().exchange(url, HttpMethod.POST, requestEntity, String.class).getBody());
                                    metadata = root.path("metadata");
                                    code = metadata.path("code").asText();
                                    Sequel.logTaskid(noRawat, noRawat, "Onsite", "addantrean", json, metadata.path("code").asText(), metadata.path("message").asText(), root.toString(), dataJam);
                                    System.out.println("respon WS BPJS : " + metadata.path("code").asText() + " " + metadata.path("message").asText() + "\n");
                                    if (code.equals("201")) {
                                        output = false;
                                        pesan = pesan + "\nResponse addantrean No. Rujukan : " + metadata.path("message").asText();
                                    }
                                } catch (Exception e) {
                                    output = false;
                                    System.out.println("Notif No. Rujuk : " + e);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    output = false;
                    pesan = "Terjadi kesalahan pada saat memproses addantrean BPJS..!!";
                    System.out.println("Notif : " + e);
                }
            } else {
                pesan = "No. SKDP atau No. Rujukan tidak ada..!!";
                output = false;
            }
        }

        if (!output) {
            JOptionPane.showMessageDialog(null, pesan);
        }

        return output;
    }

    private void emptTeks() {
        noRM.setText("");
        namaPasien.setText("");
        tglLahir.setText("");
        statusPeserta.setText("");
        noSuratKontrol.setText("");
        noRujukan.setText("");
        kodePPK.setText("");
        namaPPK.setText("");
        kodeDiagnosaBPJS.setText("");
        namaDiagnosaBPJS.setText("");
        kodePoliBPJS.setText("");
        namaPoliBPJS.setText("");
        kodeDokterBPJS.setText("");
        namaDokterBPJS.setText("");
        kodePPKPelayanan.setText("");
        namaPPKPelayanan.setText("");
        jenisPelayanan.setSelectedIndex(1);
        kelas.setSelectedIndex(2);
        tujuanKunjungan.setSelectedIndex(0);
        flagProsedur.setSelectedIndex(0);
        penunjang.setSelectedIndex(0);
        asesmenPelayanan.setSelectedIndex(0);
        kodeDPJPLayanan.setText("");
        namaDPJPLayanan.setText("");
        jenisPeserta.setText("");
        jk.setText("");
        nik.setText("");
        noPesertaBPJS.setText("");
        asalRujukan.setSelectedIndex(0);
        tglSEP.setDate(new Date());
        tglRujukan.setDate(new Date());
        noTelp.setText("");
        katarak.setSelectedIndex(0);
        lakaLantas.setSelectedIndex(0);
    }

    private void biayaRegistPoli() {
        if (labelNoRawat.getText().equals("")) {
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select poliklinik.registrasi, poliklinik.registrasilama from poliklinik where poliklinik.kd_poli = ?"
            )) {
                ps.setString(1, kodePoliRS);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (statusDaftar.equals("Lama")) {
                            biayaReg = rs.getString("registrasilama");
                        } else {
                            biayaReg = rs.getString("registrasi");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
        }
    }

    private void bukaAplikasiFingerprint() {
        if (noPesertaBPJS.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "No. kartu peserta tidak ada..!!");
            return;
        }
        toFront();
        try {
            aplikasiAktif = false;
            User32 u32 = User32.INSTANCE;
            u32.EnumWindows((WinDef.HWND hwnd, Pointer pntr) -> {
                char[] windowText = new char[512];
                u32.GetWindowText(hwnd, windowText, 512);
                String wText = Native.toString(windowText);

                if (wText.isEmpty()) {
                    return true;
                }

                if (wText.contains("Registrasi Sidik Jari")) {
                    DlgRegistrasiSEPSMC.this.aplikasiAktif = true;
                    u32.SetForegroundWindow(hwnd);
                }

                return true;
            }, Pointer.NULL);

            Robot r = new Robot();
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection ss;

            if (aplikasiAktif) {
                Thread.sleep(1000);
                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_A);
                r.keyRelease(KeyEvent.VK_A);
                r.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(500);

                ss = new StringSelection(noPesertaBPJS.getText().trim());
                c.setContents(ss, ss);
                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_CONTROL);
            } else {
                Runtime.getRuntime().exec(URLAPLIKASIFINGERPRINTBPJS);
                Thread.sleep(2000);
                ss = new StringSelection(USERFINGERPRINTBPJS);
                c.setContents(ss, ss);

                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_TAB);
                r.keyRelease(KeyEvent.VK_TAB);
                Thread.sleep(1000);

                ss = new StringSelection(PASSFINGERPRINTBPJS);
                c.setContents(ss, ss);

                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_ENTER);
                r.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(1000);

                ss = new StringSelection(noPesertaBPJS.getText().trim());
                c.setContents(ss, ss);
                r.keyPress(KeyEvent.VK_CONTROL);
                r.keyPress(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_V);
                r.keyRelease(KeyEvent.VK_CONTROL);
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    private void updateSuratKontrol(String noSKDP, String noSEP, String noKartu, String tanggalPeriksa, String kodeDPJP, String namaDPJP, String kodePoli, String namaPoli) {
        if (noSKDP.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Maaf, data surat kontrol tidak ditemukan...!!\nSilahkan hubungi administrasi...!!");
            return;
        }
        try {
            utc = api.getUTCDateTimeAsString();
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("X-Cons-ID", koneksiDB.CONSIDAPIBPJS());
            headers.add("X-Timestamp", utc);
            headers.add("X-Signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIBPJS());
            url = URLAPIBPJS + "/RencanaKontrol/Update";
            json = "{"
                + "\"request\": {"
                + "\"noSuratKontrol\":\"" + noSKDP + "\","
                + "\"noSEP\":\"" + noSEP + "\","
                + "\"kodeDokter\":\"" + kodeDPJP + "\","
                + "\"poliKontrol\":\"" + kodePoli + "\","
                + "\"tglRencanaKontrol\":\"" + tanggalPeriksa + "\","
                + "\"user\":\"" + noKartu + "\""
                + "}"
                + "}";
            System.out.println("JSON : " + json);
            requestEntity = new HttpEntity(json, headers);
            root = mapper.readTree(api.getRest().exchange(url, HttpMethod.PUT, requestEntity, String.class).getBody());
            metadata = root.path("metaData");
            System.out.println("code : " + metadata.path("code").asText());
            System.out.println("message : " + metadata.path("message").asText());
            if (metadata.path("code").asText().equals("200")) {
                Sequel.mengupdateSmc("bridging_surat_kontrol_bpjs",
                    "tgl_rencana = ?, kd_dokter_bpjs = ?, nm_dokter_bpjs = ?, kd_poli_bpjs = ?, nm_poli_bpjs = ?", "no_surat = ?",
                    tanggalPeriksa, kodeDPJP, namaDPJP, kodePoli, namaPoli, noSKDP
                );
            } else {
                JOptionPane.showMessageDialog(null, metadata.path("message").asText());
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (e.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(null, "Koneksi ke server BPJS terputus...!");
            }
        }
    }

    private boolean registerPasien() {
        if (!labelNoRawat.getText().equals("")) {
            return true;
        }
        int coba = 0, maxCoba = 5;
        while (coba < maxCoba && (!Sequel.menyimpantfSmc("reg_periksa", null,
            noReg, noRawat, Valid.getTglSmc(tglSEP),
            Sequel.cariIsi("select current_time()"), kodeDokterRS, noRM.getText(), kodePoliRS,
            namaPj, alamatPj, hubunganPj, biayaReg, "Belum", statusDaftar, "Ralan", CARABAYAR, umur,
            statusUmur, "Belum Bayar", statusPoli
        ))) {
            autoNomor();
            System.out.println("Mencoba mendaftarkan pasien dengan no. rawat: " + noRawat);
            coba++;
        }

        String isNoRawat = Sequel.cariIsiSmc("select reg_periksa.no_rawat from reg_periksa where reg_periksa.tgl_registrasi = ? and reg_periksa.no_rkm_medis = ? and reg_periksa.kd_poli = ? and reg_periksa.kd_dokter = ?", Valid.getTglSmc(tglSEP), noRM.getText(), kodePoliRS, kodeDokterRS);
        if (coba == maxCoba && (isNoRawat == null || !isNoRawat.equals(noRawat))) {
            System.out.println("Tidak dapat mendaftarkan pasien dengan No. Rawat : " + noRawat);
            return false;
        }

        updateUmurPasien();
        return true;
    }

    private void simpanRujukMasuk() {
        int coba = 0, maxCoba = 5;
        String noRujuk = Sequel.cariIsiSmc(
            "select concat('BR/', date_format(?, '%Y/%m/%d'), '/', lpad(ifnull(max(convert(right(rujuk_masuk.no_balasan, 4), signed)), 0) + 1, 4, '0')) "
            + "from rujuk_masuk where rujuk_masuk.no_balasan like concat('BR/', date_format(?, '%Y/%m/%d/'), '%')",
            Valid.getTglSmc(tglSEP), Valid.getTglSmc(tglSEP)
        );

        while (coba < maxCoba && (!Sequel.menyimpantfSmc("rujuk_masuk", null,
            noRawat, namaPPK.getText(), "-", noRujukan.getText(), "0", namaPPK.getText(), kodeDiagnosaBPJS.getText(), "-", "-", noRujuk
        ))) {
            noRujuk = Sequel.cariIsiSmc(
                "select concat('BR/', date_format(?, '%Y/%m/%d'), '/', lpad(ifnull(max(convert(right(rujuk_masuk.no_balasan, 4), signed)), 0) + 1, 4, '0')) "
                + "from rujuk_masuk where rujuk_masuk.no_balasan like concat('BR/', date_format(?, '%Y/%m/%d/'), '%')",
                Valid.getTglSmc(tglSEP), Valid.getTglSmc(tglSEP)
            );
            coba++;
        }
    }

    private void updateUmurPasien() {
        Sequel.mengupdateSmc("pasien",
            "no_tlp = ?, no_ktp = ?, umur = concat(concat(concat(timestampdiff(year, tgl_lahir, curdate()), ' Th '), concat(timestampdiff(month, tgl_lahir, curdate()) - ((timestampdiff(month, tgl_lahir, curdate()) div 12) * 12), ' Bl ')), concat(timestampdiff(day, date_add(date_add(tgl_lahir, interval timestampdiff(year, tgl_lahir, curdate()) year), interval timestampdiff(month, tgl_lahir, curdate()) - ((timestampdiff(month, tgl_lahir, curdate()) div 12) * 12) month), curdate()), ' Hr'))",
            "no_rkm_medis = ?",
            noTelp.getText(), nik.getText(), noRM.getText()
        );
    }

    private void resetAksi() {
        pwUserId.setText("");
        pwPass.setText("");
        aksi = "";
    }
}
