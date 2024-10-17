/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fungsi;

import AESsecurity.EnkripsiAES;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author khanzasoft
 */
public class koneksiDB {

    private static Connection connection = null;
    private static final Properties prop = new Properties();
    private static final MysqlDataSource dataSource = new MysqlDataSource();

    public koneksiDB() {
    }

    public static Connection condb() {
        if (connection == null) {
            try {
                prop.loadFromXML(new FileInputStream("setting/database.xml"));
                dataSource.setURL("jdbc:mysql://" + EnkripsiAES.decrypt(prop.getProperty("HOST")) + ":" + EnkripsiAES.decrypt(prop.getProperty("PORT")) + "/" + EnkripsiAES.decrypt(prop.getProperty("DATABASE")) + "?zeroDateTimeBehavior=convertToNull");
                dataSource.setUser(EnkripsiAES.decrypt(prop.getProperty("USER")));
                dataSource.setPassword(EnkripsiAES.decrypt(prop.getProperty("PAS")));
                connection = dataSource.getConnection();
                System.out.println("  Koneksi Berhasil. Sorry bro loading, silahkan baca dulu.... \n\n"
                    + "	 Software ini adalah Software Menejemen Rumah Sakit/Klinik/\n"
                    + "  Puskesmas yang gratis dan boleh digunakan siapa saja tanpa dikenai\n"
                    + "  biaya apapun. Dilarang keras memperjualbelikan/mengambil \n"
                    + "  keuntungan dari Software ini dalam bentuk apapun tanpa seijin pembuat \n"
                    + "  software (Khanza.Soft Media).\n"
                    + "                                                                           \n"
                    + "  #    ____  ___  __  __  ____   ____    _  __ _                              \n"
                    + "  #   / ___||_ _||  \\/  ||  _ \\ / ___|  | |/ /| |__    __ _  _ __   ____ __ _ \n"
                    + "  #   \\___ \\ | | | |\\/| || |_) |\\___ \\  | ' / | '_ \\  / _` || '_ \\ |_  // _` |\n"
                    + "  #    ___) || | | |  | ||  _ <  ___) | | . \\ | | | || (_| || | | | / /| (_| |\n"
                    + "  #   |____/|___||_|  |_||_| \\_\\|____/  |_|\\_\\|_| |_| \\__,_||_| |_|/___|\\__,_|\n"
                    + "  #                                                                           \n"
                    + "                                                                           \n"
                    + "  Licensi yang dianut di software ini https://en.wikipedia.org/wiki/Aladdin_Free_Public_License \n"
                    + "  Informasi dan panduan bisa dicek di halaman https://github.com/mas-elkhanza/SIMRS-Khanza/wiki \n"
                    + "                                                                           \n"
                    + "  Versi APM : ");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Koneksi Putus : " + e);
            }
        }
        return connection;
    }

    public static String PRINTERREGISTRASI() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("PRINTER_REGISTRASI");
        } catch (Exception e) {
            return "";
        }
    }

    public static String PRINTERBARCODE() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("PRINTER_BARCODE");
        } catch (Exception e) {
            return "";
        }
    }

    public static int PRINTJUMLAHBARCODE() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return Integer.parseInt(prop.getProperty("PRINTJUMLAHBARCODE"));
        } catch (Exception e) {
            return 3;
        }
    }

    public static String URLAPLIKASIFINGERPRINTBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("URLAPLIKASIFINGERPRINTBPJS");
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String URLAPLIKASIFRISTABPJS() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("URLAPLIKASIFRISTABPJS");
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String USERFINGERPRINTBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("USERFINGERPRINTBPJS"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String PASSFINGERPRINTBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/apm.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("PASSFINGERPRINTBPJS"));
        } catch (Exception e) {
            return "";
        }
    }
    
    public static boolean CARICEPAT() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("CARICEPAT").trim().equalsIgnoreCase("aktif");
        } catch (Exception e) {
            return false;
        }
    }

    public static String URLAPIBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("URLAPIBPJS");
        } catch (Exception e) {
            return "";
        }
    }

    public static String SECRETKEYAPIBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIBPJS"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String CONSIDAPIBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIBPJS"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String USERKEYAPIBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIBPJS"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String URLAPIMOBILEJKN() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("URLAPIMOBILEJKN");
        } catch (Exception e) {
            return "";
        }
    }

    public static String SECRETKEYAPIMOBILEJKN() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("SECRETKEYAPIMOBILEJKN"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String CONSIDAPIMOBILEJKN() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("CONSIDAPIMOBILEJKN"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String USERKEYAPIMOBILEJKN() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("USERKEYAPIMOBILEJKN"));
        } catch (Exception e) {
            return "";
        }
    }

    public static String BASENOREG() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("BASENOREG").trim().toLowerCase().replaceAll("\\s+", "");
        } catch (Exception e) {
            return "";
        }
    }

    public static String URUTNOREG() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("URUTNOREG").trim().toLowerCase().replaceAll("\\s+", "");
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean JADWALDOKTERDIREGISTRASI() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("JADWALDOKTERDIREGISTRASI").trim().equalsIgnoreCase("yes");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean AKTIFKANTRACKSQL() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return EnkripsiAES.decrypt(prop.getProperty("AKTIFKANTRACKSQL")).trim().equalsIgnoreCase("yes");
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean DIAGNOSARUJUKANMASUKAPIBPJS() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("DIAGNOSARUJUKANMASUKAPIBPJS").trim().equalsIgnoreCase("yes");
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean ADDANTRIANAPIMOBILEJKN() {
        try (FileInputStream fs = new FileInputStream("setting/database.xml")) {
            prop.loadFromXML(fs);
            return prop.getProperty("ADDANTRIANAPIMOBILEJKN").trim().equalsIgnoreCase("yes");
        } catch (Exception e) {
            return false;
        }
    }
}
