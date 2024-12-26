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
package fungsi;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import uz.ncipro.calendar.JDateTimePicker;

/**
 *
 * @author Owner
 */
public final class sekuel {
    private final Connection koneksi = KoneksiDB.condb();
    private final String AKTIFKANTRACKSQL = KoneksiDB.AKTIFKANTRACKSQL();
    private final DecimalFormat df2 = new DecimalFormat("####");
    private javax.swing.ImageIcon icon = null;
    private String folder, dicari = "", track = "";
    private PreparedStatement ps;
    private ResultSet rs;
    private int angka = 0;
    private double angka2 = 0;
    private Date tanggal = new Date();
    private boolean bool = false;

    public sekuel() {
        super();
    }
    
    public String autonomorSmc(String prefix, String separator, String table, String kolom, int panjang, String pad, String tanggal, int next) {
        String sql = 
            "select concat(if(? is null or ? = '', '', concat(?, ?)), date_format(" +
            "?, concat_ws(?, '%Y', '%m', '%d')), ?, lpad(ifnull(max(convert(right(" +
            table + "." + kolom + ", ?), unsigned)), 0) + ?, ?, ?)) from " + table +
            " where " + table + "." + kolom + " like concat(if(? is null or ? = '', " +
            "'', concat(?, ?)), date_format(?, concat_ws(?, '%Y', '%m', '%d')), '%')";
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            ps.setString(1, prefix);
            ps.setString(2, prefix);
            ps.setString(3, prefix);
            ps.setString(4, separator);
            ps.setString(5, tanggal);
            ps.setString(6, separator);
            ps.setString(7, separator);
            ps.setInt(8, panjang);
            ps.setInt(9, next);
            ps.setInt(10, panjang);
            ps.setString(11, pad);
            ps.setString(12, prefix);
            ps.setString(13, prefix);
            ps.setString(14, prefix);
            ps.setString(15, separator);
            ps.setString(16, tanggal);
            ps.setString(17, separator);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return "";
    }
    
    
    public String autonomorSmc(String prefix, String separator, String table, String kolom, int panjang, String pad, String tanggal) {
        return autonomorSmc(prefix, separator, table, kolom, panjang, pad, tanggal, 1);
    }

    public String cariIsiSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return "";
    }

    public boolean cariExistsSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement("select exists(" + sql + ")")) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return false;
    }

    public int cariIntegerSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return -1;
    }

    public double cariDoubleSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return -1;
    }

    public Date cariTglSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (Date) rs.getTimestamp(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return null;
    }

    public Blob cariBlobSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBlob(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return null;
    }

    public ByteArrayInputStream cariGambarSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ByteArrayInputStream(rs.getBytes(1));
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return null;
    }

    public void menyimpanSmc(String table, String kolom, String... values) {
        String sql = "insert into " + table + " (" + kolom + ") values (";
        if (kolom == null || kolom.isBlank()) {
            sql = "insert into " + table + " values (";
        }
        for (int i = 0; i < values.length; i++) {
            sql = sql.concat("?, ");
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql.substring(0, sql.length() - 2).concat(")"))) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("insert")));
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data!");
        }
    }

    public boolean menyimpantfSmc(String table, String kolom, String... values) {
        String sql = "insert into " + table + " (" + kolom + ") values (";
        if (kolom == null || kolom.isBlank()) {
            sql = "insert into " + table + " values (";
        }
        for (int i = 0; i < values.length; i++) {
            sql = sql.concat("?, ");
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql.substring(0, sql.length() - 2).concat(")"))) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("insert")));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return false;
    }

    public boolean menyimpantfNotifSmc(String judulOnDuplicate, String table, String kolom, String... values) {
        String sql = "insert into " + table + " (" + kolom + ") values (";
        if (kolom == null || kolom.isBlank()) {
            sql = "insert into " + table + " values (";
        }
        for (int i = 0; i < values.length; i++) {
            sql = sql.concat("?, ");
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql.substring(0, sql.length() - 2).concat(")"))) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("insert")));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (judulOnDuplicate != null && !judulOnDuplicate.isBlank()) {
                JOptionPane.showMessageDialog(null, "Tidak bisa menyimpan data, kemungkinan ada " + judulOnDuplicate + " yang sama dimasukkan sebelumnya.");
            }
        }
        return false;
    }

    public void mengupdateSmc(String table, String kolom, String where, String... values) {
        String sql = "update " + table + " set " + kolom + " where " + where;
        if (where == null || where.isBlank()) {
            sql = "update " + table + " set " + kolom;
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("update")));
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Gagal mengupdate data!");
        }
    }

    public boolean mengupdatetfSmc(String table, String kolom, String where, String... values) {
        String sql = "update " + table + " set " + kolom + " where " + where;
        if (where == null || where.isBlank()) {
            sql = "update " + table + " set " + kolom;
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("update")));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            JOptionPane.showMessageDialog(null, "Gagal mengupdate data!");
        }
        return false;
    }

    public void menghapusSmc(String table, String where, String... values) {
        String sql = "delete from " + table + " where " + where;
        if (where == null || where.isBlank()) {
            sql = "delete from " + table;
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("delete")));
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
            if (e.getMessage().contains("constraint")) {
                JOptionPane.showMessageDialog(null, "Gagal menghapus data, kemungkinan masih digunakan di bagian lainnya!");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus data!");
            }
        }
    }

    public void menghapusSmc(String table) {
        menghapusSmc(table, null);
    }

    public boolean menghapustfSmc(String table, String where, String... values) {
        String sql = "delete from " + table + " where " + where;
        if (where == null || where.isBlank()) {
            sql = "delete from " + table;
        }

        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf("delete")));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return false;
    }

    public boolean executeRawSmc(String sql, String... values) {
        try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                ps.setString(i + 1, values[i]);
            }
            if (ps.executeUpdate() > 0) {
                track = ps.toString();
                SimpanTrack(track.substring(track.indexOf(sql.substring(0, 8))));
                return true;
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
        return false;
    }
    
    public void logTaskid(String norawat, String kodebooking, String jenisPasien, String taskid, String request, String code, String message, String response, String wakturs) {
        try (PreparedStatement ps = koneksi.prepareStatement(
            "insert into referensi_mobilejkn_bpjs_taskid_response2 (no_rawat, kodebooking, jenispasien, taskid, request, code, message, response, waktu, waktu_rs) values (?, ?, ?, ?, ?, ?, ?, ?, now(), ?)"
        )) {
            ps.setString(1, norawat);
            ps.setString(2, kodebooking);
            ps.setString(3, jenisPasien);
            ps.setString(4, taskid);
            ps.setString(5, request);
            ps.setString(6, code);
            ps.setString(7, message);
            ps.setString(8, response);
            ps.setString(9, wakturs);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }

    public void menyimpan(String table, String value, String sama) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada " + sama + " yang sama dimasukkan sebelumnya...!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void menyimpan2(String table, String value, String sama) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public boolean menyimpantf(String table, String value, String sama) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            ps.executeUpdate();
            if (ps != null) {
                ps.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada " + sama + " yang sama dimasukkan sebelumnya...!");
            return false;
        }
    }

    public boolean menyimpantf2(String table, String value, String sama) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            ps.executeUpdate();
            if (ps != null) {
                ps.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            return false;
        }
    }

    public boolean menyimpantf(String table, String value, int i, String[] a, String acuan_field, String update, int j, String[] b) {
        bool = false;
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            for (angka = 1; angka <= i; angka++) {
                ps.setString(angka, a[angka - 1]);
            }
            ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }
            bool = true;
        } catch (Exception e) {
            try {
                ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
                for (angka = 1; angka <= j; angka++) {
                    ps.setString(angka, b[angka - 1]);
                }
                ps.executeUpdate();

                if (ps != null) {
                    ps.close();
                }
                bool = true;
            } catch (Exception e2) {
                bool = false;
                System.out.println("Notifikasi : " + e2);
            }
        }
        return bool;
    }

    public void menyimpan(String table, String value, String sama, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada " + sama + " yang sama dimasukkan sebelumnya...!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void menyimpan2(String table, String value, String sama, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public boolean menyimpantf(String table, String value, String sama, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            for (angka = 1; angka <= i; angka++) {
                ps.setString(angka, a[angka - 1]);
            }
            ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }

            if (AKTIFKANTRACKSQL.equals("yes")) {
                dicari = "";
                for (angka = 1; angka <= i; angka++) {
                    dicari = dicari + "|" + a[angka - 1];
                }
            }
            SimpanTrack("insert into " + table + " values(" + dicari + ")");

            return true;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            if (e.toString().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada " + sama + " yang sama dimasukkan sebelumnya...!");
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Ada kesalahan Query...!");
            }
            return false;
        }
    }

    public boolean menyimpantfautoincrement(String table, String columns, int count, String[] values) {
        try {
            StringBuilder sqlQuery = new StringBuilder("INSERT INTO " + table + " (" + columns + ") VALUES (");

            // Append placeholders for the values
            for (int i = 0; i < count; i++) {
                sqlQuery.append("?");
                if (i < count - 1) {
                    sqlQuery.append(",");
                }
            }

            sqlQuery.append(")");

            // Create PreparedStatement
            ps = koneksi.prepareStatement(sqlQuery.toString());

            // Set values for parameters
            for (int i = 0; i < count; i++) {
                ps.setString(i + 1, values[i]);
            }

            // Execute update
            ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }

            if (AKTIFKANTRACKSQL.equals("yes")) {
                // You may want to adjust this part based on your requirements
                dicari = String.join("|", values);
            }
            SimpanTrack("INSERT INTO " + table + " (" + columns + ") VALUES (" + dicari + ")");

            return true;
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
            if (e.toString().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada data yang sama dimasukkan sebelumnya...!");
            } else {
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Ada kesalahan Query...!");
            }
            return false;
        }
    }

    public boolean menyimpantf2(String table, String value, String sama, int i, String[] a) {
        bool = true;
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
                bool = true;
            } catch (Exception e) {
                bool = false;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }

            if (AKTIFKANTRACKSQL.equals("yes")) {
                dicari = "";
                for (angka = 1; angka <= i; angka++) {
                    dicari = dicari + "|" + a[angka - 1];
                }
            }
            SimpanTrack("insert into " + table + " values(" + dicari + ")");
        } catch (Exception e) {
            bool = false;
            System.out.println("Notifikasi : " + e);
        }
        return bool;
    }

    public void menyimpan(String table, String value, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }

            if (AKTIFKANTRACKSQL.equals("yes")) {
                dicari = "";
                for (angka = 1; angka <= i; angka++) {
                    dicari = dicari + "|" + a[angka - 1];
                }
            }
            SimpanTrack("insert into " + table + " values(" + dicari + ")");
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void menyimpan2(String table, String value, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi " + table + " : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
        }
    }

    public void menyimpan(String table, String value, int i, String[] a, String acuan_field, String update, int j, String[] b) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            for (angka = 1; angka <= i; angka++) {
                ps.setString(angka, a[angka - 1]);
            }
            ps.executeUpdate();

            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            try {
                ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
                for (angka = 1; angka <= j; angka++) {
                    ps.setString(angka, b[angka - 1]);
                }
                ps.executeUpdate();

                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e2) {
                System.out.println("Notifikasi : " + e2);
            }
        }
    }

    public void menyimpan3(String table, String value, int i, String[] a, String acuan_field, String update, int j, String[] b) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            for (angka = 1; angka <= i; angka++) {
                ps.setString(angka, a[angka - 1]);
            }
            ps.executeUpdate();

            JOptionPane.showMessageDialog(null, "Proses simpan berhasil..!!");
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            try {
                ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
                for (angka = 1; angka <= j; angka++) {
                    ps.setString(angka, b[angka - 1]);
                }
                ps.executeUpdate();

                JOptionPane.showMessageDialog(null, "Proses simpan berhasil..!!");
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e2) {
                System.out.println("Notifikasi : " + e2);
            }
        }
    }

    public void menyimpan(String table, String value) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ")");
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void menyimpan(String table, String isisimpan, String isiedit, String acuan_field) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + isisimpan + ")");
            ps.executeUpdate();
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
            try {
                ps = koneksi.prepareStatement("update " + table + " set " + isiedit + " where " + acuan_field);
                ps.executeUpdate();
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi Edit : " + ex);
            }
        }
    }

    public void menyimpan(String table, String value, String sama, JTextField AlmGb) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ",?)");
            try {
                ps.setBinaryStream(1, new FileInputStream(AlmGb.getText()), new File(AlmGb.getText()).length());
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada " + sama + " yang sama dimasukkan sebelumnya...!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

    }

    public void menyimpan(String table, String value, String sama, JTextField AlmGb, JTextField AlmPhoto) {
        try {
            ps = koneksi.prepareStatement("insert into " + table + " values(" + value + ",?,?)");
            try {
                ps.setBinaryStream(1, new FileInputStream(AlmGb.getText()), new File(AlmGb.getText()).length());
                ps.setBinaryStream(2, new FileInputStream(AlmPhoto.getText()), new File(AlmPhoto.getText()).length());
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, gagal menyimpan data. Kemungkinan ada " + sama + " yang sama dimasukkan sebelumnya...!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void meghapus(String table, String field, String nilai_field) {
        try {
            ps = koneksi.prepareStatement("delete from " + table + " where " + field + "=?");
            try {
                ps.setString(1, nilai_field);
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus. Kemungkinan data tersebut masih dipakai di table lain...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void meghapus(String table, String field, String field2, String nilai_field, String nilai_field2) {
        try {
            ps = koneksi.prepareStatement("delete from " + table + " where " + field + "=? and " + field2 + "=?");
            try {
                ps.setString(1, nilai_field);
                ps.setString(2, nilai_field2);
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus. Kemungkinan data tersebut masih dipakai di table lain...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void meghapus2(String table, String field, String nilai_field) {
        try {
            ps = koneksi.prepareStatement("delete from " + table + " where " + field + "=?");
            try {
                ps.setString(1, nilai_field);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Proses hapus berhasil...!!!!");
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, data gagal dihapus. Kemungkinan data tersebut masih dipakai di table lain...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void meghapus3(String table, String field, String nilai_field) {
        try {
            ps = koneksi.prepareStatement("delete from " + table + " where " + field + "=?");
            try {
                ps.setString(1, nilai_field);
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void mengedit(String table, String acuan_field, String update) {
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Gagal Mengedit. Mungkin kode sudah digunakan sebelumnya...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public boolean mengedittf(String table, String acuan_field, String update) {
        bool = true;
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                ps.executeUpdate();
                bool = true;
            } catch (Exception e) {
                bool = false;
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Gagal Mengedit. Mungkin kode sudah digunakan sebelumnya...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            bool = false;
            System.out.println("Notifikasi : " + e);
        }
        return bool;
    }

    public void mengedit(String table, String acuan_field, String update, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Gagal Mengedit. Periksa kembali data...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void mengedit2(String table, String acuan_field, String update, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Proses edit berhasil...!!!!");
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Gagal mengedit. Periksa kembali data...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void mengedit3(String table, String acuan_field, String update, int i, String[] a) {
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public boolean mengedittf(String table, String acuan_field, String update, int i, String[] a) {
        bool = true;
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
                bool = true;
            } catch (Exception e) {
                bool = false;
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Gagal Mengedit. Periksa kembali data...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
            if (AKTIFKANTRACKSQL.equals("yes")) {
                dicari = "";
                for (angka = 1; angka <= i; angka++) {
                    dicari = dicari + "|" + a[angka - 1];
                }
            }
            SimpanTrack("update " + table + " set " + update + " " + dicari + " where " + acuan_field);

        } catch (Exception e) {
            bool = false;
            System.out.println("Notifikasi : " + e);
        }
        return bool;
    }

    public void mengedit(String table, String acuan_field, String update, JTextField AlmGb) {
        try {
            ps = koneksi.prepareStatement("update " + table + " set " + update + " where " + acuan_field);
            try {
                ps.setBinaryStream(1, new FileInputStream(AlmGb.getText()), new File(AlmGb.getText()).length());
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Pilih dulu data yang mau anda edit...\n Klik data pada table untuk memilih...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void query(String qry) {
        try {
            ps = koneksi.prepareStatement(qry);
            try {
                ps.executeQuery();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Query tidak bisa dijalankan...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void queryu(String qry) {
        try {
            ps = koneksi.prepareStatement(qry);
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Query tidak bisa dijalankan...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public boolean queryutf(String qry) {
        bool = false;
        try {
            ps = koneksi.prepareStatement(qry);
            try {
                ps.executeUpdate();
                bool = true;
            } catch (Exception e) {
                bool = false;
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Query tidak bisa dijalankan...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            bool = false;
            System.out.println("Notifikasi : " + e);
        }
        return bool;
    }

    public void queryu(String qry, String parameter) {
        try {
            ps = koneksi.prepareStatement(qry);
            try {
                ps.setString(1, parameter);
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
                JOptionPane.showMessageDialog(null, "Maaf, Query tidak bisa dijalankan...!!!!");
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void queryu2(String qry) {
        try {
            ps = koneksi.prepareStatement(qry);
            try {
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void queryu2(String qry, int i, String[] a) {
        try {
            try {
                ps = koneksi.prepareStatement(qry);
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
            if (AKTIFKANTRACKSQL.equals("yes")) {
                dicari = "";
                for (angka = 1; angka <= i; angka++) {
                    dicari = dicari + "|" + a[angka - 1];
                }
            }
            SimpanTrack(qry + " " + dicari);
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public boolean queryu2tf(String qry, int i, String[] a) {
        bool = false;
        try {
            try {
                ps = koneksi.prepareStatement(qry);
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
                bool = true;
            } catch (Exception e) {
                bool = false;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        return bool;
    }

    public void queryu3(String qry, int i, String[] a) {
        try {
            try {
                ps = koneksi.prepareStatement(qry);
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void queryu4(String qry, int i, String[] a) {
        try {
            try {
                ps = koneksi.prepareStatement(qry);
                for (angka = 1; angka <= i; angka++) {
                    ps.setString(angka, a[angka - 1]);
                }
                ps.executeUpdate();
            } catch (Exception e) {
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
        }
    }

    public void AutoComitFalse() {
        try {
            koneksi.setAutoCommit(false);
        } catch (Exception e) {
        }
    }

    public void AutoComitTrue() {
        try {
            koneksi.setAutoCommit(true);
        } catch (Exception e) {
        }
    }

    public void cariIsi(String sql, JComboBox cmb) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    String dicari = rs.getString(1);
                    cmb.setSelectedItem(dicari);
                } else {
                    cmb.setSelectedItem("");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void cariIsi(String sql, JDateTimePicker dtp) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    try {
                        dtp.setDisplayFormat("yyyy-MM-dd");
                        dtp.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1)));
                        dtp.setDisplayFormat("dd-MM-yyyy");
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void cariIsi(String sql, JTextField txt) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    txt.setText(rs.getString(1));
                } else {
                    txt.setText("");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public int cariRegistrasi(String norawat) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(
                    "select count(billing.no_rawat) from billing where billing.no_rawat=?");
            try {
                ps.setString(1, norawat);
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka = rs.getInt(1);
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return angka;
    }

    public void cariIsi(String sql, JTextField txt, String kunci) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, kunci);
                rs = ps.executeQuery();
                if (rs.next()) {
                    txt.setText(rs.getString(1));
                } else {
                    txt.setText("");
                }
            } catch (SQLException e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void cariIsi(String sql, JTextArea txt, String kunci) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, kunci);
                rs = ps.executeQuery();
                if (rs.next()) {
                    txt.setText(rs.getString(1));
                } else {
                    txt.setText("");
                }
            } catch (SQLException e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void cariIsi(String sql, JLabel txt) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    txt.setText(rs.getString(1));
                } else {
                    txt.setText("");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public String cariIsi(String sql) {
        dicari = "";
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    dicari = rs.getString(1);
                } else {
                    dicari = "";
                }
            } catch (Exception e) {
                dicari = "";
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return dicari;
    }

    public ByteArrayInputStream cariGambar(String sql) {
        ByteArrayInputStream inputStream = null;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    inputStream = new ByteArrayInputStream(rs.getBytes(1));
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return inputStream;
    }

    public String cariIsi(String sql, String data) {
        dicari = "";
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, data);
                rs = ps.executeQuery();
                if (rs.next()) {
                    dicari = rs.getString(1);
                } else {
                    dicari = "";
                }
            } catch (Exception e) {
                dicari = "";
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return dicari;
    }

    public Date cariIsi2(String sql) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    tanggal = rs.getDate(1);
                } else {
                    tanggal = new Date();
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        return tanggal;
    }

    public Integer cariInteger(String sql) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka = rs.getInt(1);
                } else {
                    angka = 0;
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka;
    }

    public Integer cariIntegerCount(String sql) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    angka = angka + rs.getInt(1);
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka;
    }

    public Integer cariInteger(String sql, String data) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, data);
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka = rs.getInt(1);
                } else {
                    angka = 0;
                }
            } catch (Exception e) {
                angka = 0;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka;
    }

    public Integer cariInteger(String sql, String data, String data2) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, data);
                ps.setString(2, data2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka = rs.getInt(1);
                } else {
                    angka = 0;
                }
            } catch (Exception e) {
                angka = 0;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka;
    }

    public Integer cariInteger(String sql, String data, String data2, String data3) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, data);
                ps.setString(2, data2);
                ps.setString(3, data3);
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka = rs.getInt(1);
                } else {
                    angka = 0;
                }
            } catch (Exception e) {
                angka = 0;
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka;
    }

    public Integer cariInteger2(String sql) {
        angka = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                rs.last();
                angka = rs.getRow();
                if (angka < 1) {
                    angka = 0;
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka;
    }

    public void cariIsiAngka(String sql, JTextField txt) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    txt.setText(df2.format(rs.getDouble(1)));
                } else {
                    txt.setText("0");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void cariIsiAngka(String sql, JLabel txt) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    txt.setText(df2.format(rs.getDouble(1)));
                } else {
                    txt.setText("0");
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public double cariIsiAngka(String sql) {
        angka2 = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka2 = rs.getDouble(1);
                } else {
                    angka2 = 0;
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka2;
    }

    public double cariIsiAngka(String sql, String data) {
        angka2 = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, data);
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka2 = rs.getDouble(1);
                } else {
                    angka2 = 0;
                }
                //rs.close();
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka2;
    }

    public double cariIsiAngka2(String sql, String data, String data2) {
        angka2 = 0;
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, data);
                ps.setString(2, data2);
                rs = ps.executeQuery();
                if (rs.next()) {
                    angka2 = rs.getDouble(1);
                } else {
                    angka2 = 0;
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return angka2;
    }

    public void cariGambar(String sql, JLabel txt) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    icon = new javax.swing.ImageIcon(rs.getBlob(1).getBytes(1L, (int) rs.getBlob(1).length()));
                    createThumbnail();
                    txt.setIcon(icon);
                } else {
                    txt.setText(null);
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
    }

    public void cariGambar(String sql, java.awt.Canvas txt, String text) {
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                for (int I = 0; rs.next(); I++) {
                    ((Painter) txt).setImage(gambar(text));
                    Blob blob = rs.getBlob(5);
                    ((Painter) txt).setImageIcon(new javax.swing.ImageIcon(
                            blob.getBytes(1, (int) (blob.length()))));
                }
            } catch (Exception ex) {
                cetak(ex.toString());
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

    }

    private void SimpanTrack(String sql) {
        if (AKTIFKANTRACKSQL.equals("yes")) {
            try (PreparedStatement ps = koneksi.prepareStatement("insert into trackersql values(now(), ?, 'APM')")) {
                ps.setString(1, InetAddress.getLocalHost().getHostAddress() + " " + sql);
                ps.executeUpdate();
            } catch (Exception e) {
                System.out.println("Notif : " + e);
            }
        }
    }

    public String cariString(String sql) {
        dicari = "";
        try {
            ps = koneksi.prepareStatement(sql);
            try {
                rs = ps.executeQuery();
                if (rs.next()) {
                    dicari = rs.getString(1);
                } else {
                    dicari = "";
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        return dicari;
    }

    private String gambar(String id) {
        return folder + File.separator + id.trim() + ".jpg";
    }

    public void Tabel(javax.swing.JTable tb, int lebar[]) {
        tb.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        angka = tb.getColumnCount();
        for (int i = 0; i < angka; i++) {
            javax.swing.table.TableColumn tbc = tb.getColumnModel().getColumn(i);
            tbc.setPreferredWidth(lebar[i]);
            //tb.setRowHeight(17);
        }
    }

    private void createThumbnail() {
        int maxDim = 150;
        try {
            Image inImage = icon.getImage();

            double scale = (double) maxDim / (double) inImage.getHeight(null);
            if (inImage.getWidth(null) > inImage.getHeight(null)) {
                scale = (double) maxDim / (double) inImage.getWidth(null);
            }

            int scaledW = (int) (scale * inImage.getWidth(null));
            int scaledH = (int) (scale * inImage.getHeight(null));

            BufferedImage outImage = new BufferedImage(scaledW, scaledH,
                    BufferedImage.TYPE_INT_RGB);

            AffineTransform tx = new AffineTransform();

            if (scale < 1.0d) {
                tx.scale(scale, scale);
            }

            Graphics2D g2d = outImage.createGraphics();
            g2d.drawImage(inImage, tx, null);
            g2d.dispose();

            new javax.swing.ImageIcon(outImage);
        } catch (Exception e) {
        }
    }

    private void cetak(String str) {
        System.out.println(str);
    }

    public class Painter extends Canvas {

        Image image;

        private void setImage(String file) {
            URL url = null;
            try {
                url = new File(file).toURI().toURL();
            } catch (MalformedURLException ex) {
                cetak(ex.toString());
            }
            image = getToolkit().getImage(url);
            repaint();
        }

        private void setImageIcon(ImageIcon file) {
            image = file.getImage();
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            double d = image.getHeight(this) / this.getHeight();
            double w = image.getWidth(this) / d;
            double x = this.getWidth() / 2 - w / 2;
            g.drawImage(image, (int) x, 0, (int) (w), this.getHeight(), this);
        }

        private void cetak(String str) {
            System.out.println(str);
        }
    }

    public class NIOCopier {

        public NIOCopier(String asal, String tujuan) throws IOException {
            FileOutputStream outFile;
            try (FileInputStream inFile = new FileInputStream(asal)) {
                outFile = new FileOutputStream(tujuan);
                FileChannel outChannel;
                try (FileChannel inChannel = inFile.getChannel()) {
                    outChannel = outFile.getChannel();
                    for (ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                            inChannel.read(buffer) != -1;
                            buffer.clear()) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            outChannel.write(buffer);
                        }
                    }
                }
                outChannel.close();
            }
            outFile.close();
        }
    }

}
