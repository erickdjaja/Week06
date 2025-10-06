import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/* ====== ENUM ====== */
enum Status { PRESENT, LATE, ABSENT }

/* ====== RECORD PRESENSI ====== */
class Attendance {
    LocalDate date; LocalTime scheduled, actual; Status status;
    Attendance(LocalDate d, LocalTime s, LocalTime a, Status st){date=d;scheduled=s;actual=a;status=st;}
    String line(){return String.format("%s | Jadwal %s | Datang %s | %s",date,scheduled,actual, status);}
}

/* ====== PARENT (INHERITANCE + POLYMORPHISM) ====== */
abstract class Participant {
    String id,name; List<Attendance> log = new ArrayList<>();
    Participant(String id,String name){this.id=id;this.name=name;}
    abstract int grace();           // toleransi telat (menit)
    abstract int maxLate();         // di atas ini = ABSENT
    String role(){return "Participant";}
    // Overloading: pakai waktu sekarang
    Attendance checkIn(LocalTime scheduled){ return checkIn(scheduled, LocalTime.now()); }
    // Overloading: pakai jam input
    Attendance checkIn(LocalTime scheduled, LocalTime actual){
        int diff = (int)Duration.between(scheduled, actual).toMinutes(); // >0 = telat
        Status st = diff <= grace() ? Status.PRESENT : (diff <= maxLate()? Status.LATE : Status.ABSENT);
        Attendance a = new Attendance(LocalDate.now(), scheduled, actual, st);
        log.add(a); return a;
    }
    String info(){return String.format("[%s] %s (%s) | Tol %d′, Max %d′",id,name,role(),grace(),maxLate());}
}

/* ====== CHILDREN ====== */
class Student extends Participant {
    Student(String id,String name){super(id,name);}
    int grace(){return 10;} int maxLate(){return 30;} String role(){return "Student";}
}
class Lecturer extends Participant {
    Lecturer(String id,String name){super(id,name);}
    int grace(){return 15;} int maxLate(){return 45;} String role(){return "Lecturer";}
}

/* ====== APP ====== */
public class Main {
    static final Scanner in = new Scanner(System.in);
    static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");
    static final List<Participant> roster = new ArrayList<>();
    static LocalTime scheduled = LocalTime.of(8,0);

    public static void main(String[] args){
        // data contoh
        roster.add(new Student("S001","Aminudin"));
        roster.add(new Student("S002","Basuri"));
        roster.add(new Lecturer("L010","Mr. Erick"));

        System.out.println("=== Presensi Kampus (Inheritance, Polymorphism, Overloading) ===");
        while(true){
            System.out.println("\n1) Lihat Peserta  2) Tambah  3) Set Jadwal  4) Presensi  5) Log Peserta  6) Keluar");
            System.out.print("Pilih: "); String p = in.nextLine().trim();
            switch(p){
                case "1": showRoster(); break;
                case "2": add(); break;
                case "3": setSchedule(); break;
                case "4": checkInFlow(); break;
                case "5": showLogs(); break;
                case "6": System.out.println("Bye!"); return;
                default: System.out.println("Pilihan tidak valid.");
            }
        }
    }

    static void showRoster(){
        System.out.println("\n--- Peserta (Jadwal: "+scheduled+") ---");
        for(int i=0;i<roster.size();i++) System.out.printf("%d) %s\n", i+1, roster.get(i).info());
    }

    static void add(){
        System.out.print("Tipe (1=Student, 2=Lecturer): "); String t = in.nextLine().trim();
        System.out.print("ID: "); String id = in.nextLine().trim();
        System.out.print("Nama: "); String name = in.nextLine().trim();
        if("1".equals(t)) roster.add(new Student(id,name));
        else if("2".equals(t)) roster.add(new Lecturer(id,name));
        else { System.out.println("Tipe salah."); return; }
        System.out.println("-> Ditambahkan.");
    }

    static void setSchedule(){
        System.out.print("Jam kuliah (HH:MM): ");
        try { scheduled = LocalTime.parse(in.nextLine().trim(), TF); System.out.println("-> Jadwal: "+scheduled); }
        catch(Exception e){ System.out.println("Format waktu salah."); }
    }

    static void checkInFlow(){
        if(roster.isEmpty()){System.out.println("Roster kosong.");return;}
        showRoster(); System.out.print("Nomor peserta: ");
        int idx = safeInt(in.nextLine(), -1)-1; if(idx<0||idx>=roster.size()){System.out.println("Nomor salah.");return;}
        Participant p = roster.get(idx);
        System.out.print("Pakai waktu SEKARANG? (y/n): "); String yn = in.nextLine().trim();
        Attendance a;
        if(yn.equalsIgnoreCase("y")) a = p.checkIn(scheduled);
        else {
            System.out.print("Masukkan jam hadir (HH:MM): ");
            try { a = p.checkIn(scheduled, LocalTime.parse(in.nextLine().trim(), TF)); }
            catch(Exception e){ System.out.println("Format salah."); return; }
        }
        System.out.println("-> "+a.line());
    }

    static void showLogs(){
        if(roster.isEmpty()){System.out.println("Roster kosong.");return;}
        showRoster(); System.out.print("Nomor peserta: ");
        int idx = safeInt(in.nextLine(), -1)-1; if(idx<0||idx>=roster.size()){System.out.println("Nomor salah.");return;}
        Participant p = roster.get(idx);
        System.out.println("\n--- Log "+p.info()+" ---");
        if(p.log.isEmpty()) System.out.println("(belum ada)");
        else p.log.forEach(r -> System.out.println(r.line()));
        long pr = p.log.stream().filter(x->x.status==Status.PRESENT).count();
        long lt = p.log.stream().filter(x->x.status==Status.LATE).count();
        long ab = p.log.stream().filter(x->x.status==Status.ABSENT).count();
        System.out.printf("Rekap: PRESENT=%d, LATE=%d, ABSENT=%d\n", pr, lt, ab);
    }

    static int safeInt(String s,int fb){ try{return Integer.parseInt(s.trim());}catch(Exception e){return fb;} }
}
