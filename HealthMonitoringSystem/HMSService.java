import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HMSService {
    private List<Patient> patients;
    private List<Doctor> doctors;
    private List<Appointment> appointments;
    private Scanner scanner;
    private SimpleDateFormat dateFormat;

    public HMSService() {
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    private void loadData() {
        patients = FileUtil.readPatients("src/data/Patients.csv");
        doctors = FileUtil.readDoctors("src/data/Doctors.csv");
        appointments = FileUtil.readAppointments("src/data/Appointments.csv");
    }

    public void start() {
        while (true) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1 -> handleRegisterNewPatient();
                case 2 -> handleScheduleAppointment();
                case 3 -> handleUpdatePatientVitals();
                case 4 -> retrieveSortedPatientData();
                case 5 -> handleManageDoctorsAppointments();
                case 6 -> handleRetrieveMedicalHistory();
                case 0 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("Health Monitoring System");
        System.out.println("1. Register New Patient");
        System.out.println("2. Schedule Appointment");
        System.out.println("3. Update Patient Vitals");
        System.out.println("4. Retrieve Sorted Patient Data");
        System.out.println("5. Manage Doctor's Appointments");
        System.out.println("6. Retrieve Medical History");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private int getUserChoice() {
        return scanner.nextInt();
    }

    private void handleRegisterNewPatient() {
        loadData(); // Load data here
        System.out.print("Enter Patient ID: ");
        String id = scanner.next();
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();
        System.out.print("Enter Medical History: ");
        String medicalHistory = scanner.next();
        System.out.print("Enter Vitals: ");
        String vitals = scanner.next();

        registerPatient(id, name, age, gender, medicalHistory, vitals);
    }

    private void handleScheduleAppointment() {
        loadData(); // Load data here
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.next();
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.next();
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.next();
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        String appointmentDate = scanner.next();
        System.out.print("Enter Status: ");
        String status = scanner.next();

        // Validate the appointment date
        try {
            Date date = dateFormat.parse(appointmentDate);
            scheduleAppointment(appointmentId, patientId, doctorId, appointmentDate, status);
        } catch (ParseException e) {
            System.out.println("Error: Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    private void handleUpdatePatientVitals() {
        loadData(); // Load data here
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.next();
        System.out.print("Enter New Vitals: ");
        String newVitals = scanner.next();

        updatePatientVitals(patientId, newVitals);
    }

    private void handleManageDoctorsAppointments() {
        loadData(); // Load data here
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.next();
        manageDoctorAppointments(doctorId);
    }

    private void handleRetrieveMedicalHistory() {
        loadData(); // Load data here
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.next();
        retrieveMedicalHistory(patientId);
    }

    public void registerPatient(String id, String name, int age, String gender, String medicalHistory, String vitals) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/Patients.csv", true))) {
            String newPatient = String.join(",", id, name, String.valueOf(age), gender, medicalHistory, vitals);
            writer.write(newPatient);
            writer.newLine();
            System.out.println("New patient registered successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while registering the patient: " + e.getMessage());
        }
    }

    public void scheduleAppointment(String appointmentId, String patientId, String doctorId, String appointmentDate, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/Appointments.csv", true))) {
            String newAppointment = String.join(",", appointmentId, patientId, doctorId, appointmentDate, status);
            writer.write(newAppointment);
            writer.newLine();
            System.out.println("Appointment scheduled successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while scheduling the appointment: " + e.getMessage());
        }
    }

    public void updatePatientVitals(String patientId, String newVitals) {
        try {
            File file = new File("src/data/Patients.csv");
            List<String> fileContent = new ArrayList<>(Files.readAllLines(file.toPath()));

            for (int i = 0; i < fileContent.size(); i++) {
                String[] data = fileContent.get(i).split(",");
                if (data[0].equals(patientId)) {
                    data[5] = newVitals; // Assuming the 6th column is vitals
                    fileContent.set(i, String.join(",", data));
                    System.out.println("Vitals updated successfully.");
                    Files.write(file.toPath(), fileContent);
                    return;
                }
            }
            System.out.println("Patient ID not found.");
        } catch (IOException e) {
            System.out.println("Error occurred while updating vitals: " + e.getMessage());
        }
    }

    public void retrieveSortedPatientData() {
        try {
            List<String[]> patientData = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("src/data/Patients.csv"))) {
                String line;
                boolean isFirstLine = true; // To skip the header line
                while ((line = reader.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false; // Skip the header
                        continue;
                    }
                    String[] data = line.split(",");
                    patientData.add(data);
                }
            }
    
            // Sort by age (bubble sort)
            patientData.sort((p1, p2) -> {
                try {
                    return Integer.compare(Integer.parseInt(p1[2]), Integer.parseInt(p2[2]));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid age format in data: " + Arrays.toString(p1) + " or " + Arrays.toString(p2));
                    return 0; // Handle as equal for sorting
                }
            });
    
            System.out.println("Sorted patient data by age:");
            for (String[] patient : patientData) {
                System.out.println(Arrays.toString(patient));
            }
    
        } catch (IOException e) {
            System.out.println("Error occurred while retrieving sorted patient data: " + e.getMessage());
        }
    }
    

    public void manageDoctorAppointments(String doctorId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/Appointments.csv"))) {
            String line;
            List<String> doctorAppointments = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[2].equals(doctorId)) {
                    doctorAppointments.add(line);
                }
            }

            if (doctorAppointments.isEmpty()) {
                System.out.println("No appointments found for doctor ID " + doctorId + ".");
            } else {
                System.out.println("Appointments for doctor ID " + doctorId + ":");
                for (String appointment : doctorAppointments) {
                    System.out.println(appointment);
                }
            }

            System.out.println("Enter the appointment ID to update status or 'exit' to go back: ");
            String appointmentId = scanner.next();
            if (!appointmentId.equals("exit")) {
                updateAppointmentStatus(appointmentId, "Completed");
            }

        } catch (IOException e) {
            System.out.println("Error occurred while managing doctor's appointments: " + e.getMessage());
        }
    }

    public void retrieveMedicalHistory(String patientId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/Patients.csv"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(patientId)) {
                    System.out.println("Medical history for patient ID " + patientId + ": " + data[4]);
                    return;
                }
            }
            System.out.println("Patient ID not found.");
        } catch (IOException e) {
            System.out.println("Error occurred while retrieving medical history: " + e.getMessage());
        }
    }

    private void updateAppointmentStatus(String appointmentId, String newStatus) {
        try {
            File file = new File("src/data/Appointments.csv");
            List<String> fileContent = new ArrayList<>(Files.readAllLines(file.toPath()));

            for (int i = 0; i < fileContent.size(); i++) {
                String[] data = fileContent.get(i).split(",");
                if (data[0].equals(appointmentId)) {
                    data[4] = newStatus; // Assuming the 5th column is status
                    fileContent.set(i, String.join(",", data));
                    System.out.println("Appointment status updated successfully.");
                    Files.write(file.toPath(), fileContent);
                    return;
                }
            }
            System.out.println("Appointment ID not found.");
        } catch (IOException e) {
            System.out.println("Error occurred while updating appointment status: " + e.getMessage());
        }
    }
}
