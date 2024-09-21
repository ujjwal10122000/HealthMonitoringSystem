import java.util.List;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private List<String> medicalHistory;

    public Patient(int id, String name, int age, String gender, List<String> medicalHistory, String vitals) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public List<String> getMedicalHistory() { return medicalHistory; }

    @Override
    public String toString() {
        return "Patient ID: " + id + ", Name: " + name + ", Age: " + age +
               ", Gender: " + gender + ", Medical History: " + medicalHistory;
    }
}
