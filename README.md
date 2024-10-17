## APM Custom
Aplikasi anjungan pasien mandiri (APM) modifikasi dari [APM RS Indriati Boyolali](https://github.com/abdulrokhimrepo/anjunganmandiriSEP)

### Requirements
- [Apache Netbeans](https://netbeans.apache.org/front/main/download/index.html)
- Liberica JDK 17 [Download](https://github.com/bell-sw/Liberica/releases?q=17.0&expanded=true)
- Aplikasi Fingerprint BPJS v2.0+
- Aplikasi FRISTA BPJS v3.0+

### Konfigurasi
Berikut adalah konfigurasi yang disediakan dalam file `apm.xml`:
```xml
<entry key="URLAPLIKASIFINGERPRINTBPJS">D:\BPJS Kesehatan\Aplikasi fingerprint BPJS\After.exe</entry>
<entry key="URLAPLIKASIFRISTABPJS">D:\BPJS Kesehatan\Aplikasi frista BPJS\frista.exe</entry>
<entry key="USERFINGERPRINTBPJS"></entry>
<entry key="PASSWORDFINGERPRINTBPJS"></entry>
<entry key="PRINTER_REGISTRASI"></entry>
<entry key="PRINTER_BARCODE"></entry>
<entry key="PRINTERJUMLAHBARCODE">3</entry>
```

#### key "URLAPLIKASIFINGERPRINTBPJS"
Berisi path ke aplikasi Fingerprint BPJS.

#### key "URLAPLIKASIFINGERPRINTBPJS"
Berisi path ke aplikasi FIRSTA BPJS.

#### key "USERFINGERPRINTBPJS" dan "PASSFINGERPRINTBPJS"
Berisi kredensial login username dan password aplikasi Fingerprint/FRISTA BPJS, dengan kredensial dienkripsi menggunakan enkripsi dari SIMRS Khanza.

#### key "PRINTER_REGISTRASI"
Digunakan untuk mengetahui nama printer service untuk mencetak lembar registrasi pasien dan SEP pasien.

#### key "PRINTER_BARCODE"
Digunakan untuk mengetahui nama printer service untuk mencetak barcode.

#### key "PRINTERJUMLAHBARCODE"
Digunakan untuk mensetting jumlah barcode yang akan dicetak secara default.
