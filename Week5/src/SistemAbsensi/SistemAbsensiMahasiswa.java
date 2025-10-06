public abstract class Person {
    protected String nama;
    protected String nim;

    public Person(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
    }

    public abstract void tampilkanInfo();
}

Class Mahasiswa:

import java.util.ArrayList;
import java.util.List;

public class Mahasiswa extends Person {
    private List<StatusAbsensi> riwayatAbsensi;

    public Mahasiswa(String nama, String nim) {
        super(nama, nim);
        this.riwayatAbsensi = new ArrayList<>();
    }

    public void tambahAbsensi(StatusAbsensi status) {
        riwayatAbsensi.add(status);
    }

    public void tampilkanRiwayat() {
        System.out.println("=== Absensi " + nama + " ===");
        int hadir = 0;
        for (int i = 0; i < riwayatAbsensi.size(); i++) {
            StatusAbsensi status = riwayatAbsensi.get(i);
            System.out.println((i + 1) + ". " + status.getDetail());
            if (status instanceof Hadir) hadir++;
        }
        System.out.println("Kehadiran: " + hadir + "/" + riwayatAbsensi.size() +
                " (" + (riwayatAbsensi.size() == 0 ? 0 :
                (100.0 * hadir / riwayatAbsensi.size())) + "%)");
    }

    @Override
    public void tampilkanInfo() {
        System.out.println("Mahasiswa: " + nama + " (NIM: " + nim + ")");
    }
}

Class StatusAbsensi:
import java.time.LocalDate;

public abstract class StatusAbsensi {
    protected LocalDate tanggal;

    public StatusAbsensi() {
        this.tanggal = LocalDate.now();
    }

    public abstract String getDetail();
}

class Hadir extends StatusAbsensi {
    @Override
    public String getDetail() {
        return "HADIR - " + tanggal;
    }
}

class TidakHadir extends StatusAbsensi {
    private String alasan;

    public TidakHadir(String alasan) {
        this.alasan = alasan;
    }

    @Override
    public String getDetail() {
        return "TIDAK HADIR - " + tanggal + " (Alasan: " + alasan + ")";
    }
}

class Izin extends StatusAbsensi {
    private String keterangan;

    public Izin(String keterangan) {
        this.keterangan = keterangan;
    }

    @Override
    public String getDetail() {
        return "IZIN - " + tanggal + " (Keterangan: " + keterangan + ")";
    }
}

AbsensiSederhana (main):

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AbsensiSederhana {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Mahasiswa> daftarMahasiswa = new ArrayList<>();

        // Data default
        daftarMahasiswa.add(new Mahasiswa("Budi Santoso", "123456"));
        daftarMahasiswa.add(new Mahasiswa("Sari Dewi", "789012"));
        daftarMahasiswa.add(new Mahasiswa("Ahmad Rizki", "345678"));

        int pilihan;
        do {
            System.out.println("\n=== SISTEM ABSENSI MAHASISWA ===");
            System.out.println("1. Lihat Daftar Mahasiswa");
            System.out.println("2. Tambah Mahasiswa Baru");
            System.out.println("3. Tambah Absensi");
            System.out.println("4. Lihat Riwayat Absensi");
            System.out.println("0. Keluar");
            System.out.print("Pilihan: ");
            pilihan = sc.nextInt();
            sc.nextLine();

            switch (pilihan) {
                case 1 -> {
                    System.out.println("=== DAFTAR MAHASISWA ===");
                    for (int i = 0; i < daftarMahasiswa.size(); i++) {
                        System.out.print((i + 1) + ". ");
                        daftarMahasiswa.get(i).tampilkanInfo();
                    }
                }
                case 2 -> {
                    System.out.print("Masukkan nama mahasiswa: ");
                    String nama = sc.nextLine();
                    System.out.print("Masukkan NIM mahasiswa: ");
                    String nim = sc.nextLine();
                    daftarMahasiswa.add(new Mahasiswa(nama, nim));
                    System.out.println("Mahasiswa baru berhasil ditambahkan!");
                }
                case 3 -> {
                    if (daftarMahasiswa.isEmpty()) {
                        System.out.println("Belum ada mahasiswa.");
                        break;
                    }
                    System.out.println("=== Pilih Mahasiswa ===");
                    for (int i = 0; i < daftarMahasiswa.size(); i++) {
                        System.out.println((i + 1) + ". " + daftarMahasiswa.get(i).nama);
                    }
                    System.out.print("Pilih: ");
                    int mIndex = sc.nextInt() - 1;
                    sc.nextLine();

                    if (mIndex >= 0 && mIndex < daftarMahasiswa.size()) {
                        System.out.println("Pilih status:");
                        System.out.println("1. Hadir");
                        System.out.println("2. Tidak Hadir");
                        System.out.println("3. Izin");
                        int s = sc.nextInt();
                        sc.nextLine();

                        StatusAbsensi status = switch (s) {
                            case 1 -> new Hadir();
                            case 2 -> {
                                System.out.print("Alasan tidak hadir: ");
                                String alasan = sc.nextLine();
                                yield new TidakHadir(alasan);
                            }
                            case 3 -> {
                                System.out.print("Keterangan izin: ");
                                String ket = sc.nextLine();
                                yield new Izin(ket);
                            }
                            default -> null;
                        };

                        if (status != null) {
                            daftarMahasiswa.get(mIndex).tambahAbsensi(status);
                            System.out.println("Absensi berhasil ditambahkan!");
                        }
                    } else {
                        System.out.println("Pilihan tidak valid!");
                    }
                }
                case 4 -> {
                    System.out.println("=== Pilih Mahasiswa ===");
                    for (int i = 0; i < daftarMahasiswa.size(); i++) {
                        System.out.println((i + 1) + ". " + daftarMahasiswa.get(i).nama);
                    }
                    System.out.print("Pilih: ");
                    int mIndex = sc.nextInt() - 1;
                    if (mIndex >= 0 && mIndex < daftarMahasiswa.size()) {
                        daftarMahasiswa.get(mIndex).tampilkanRiwayat();
                    } else {
                        System.out.println("Pilihan tidak valid!");
                    }
                }
                case 0 -> System.out.println("Aplikasi ditutup.");
                default -> System.out.println("Pilihan tidak valid.");
            }
        } while (pilihan != 0);
    }
}