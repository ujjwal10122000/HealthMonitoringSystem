import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    // Read patients from CSV
    public static List<Patient> readPatients(String filePath) {
        List<Patient> patients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true; // Skip header
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header
                    continue;
                }
                String[] data = line.split(",");
                
                // Check if the data has enough fields
                if (data.length < 6) {
                    System.out.println("Invalid patient data: " + line);
                    continue; // Skip invalid lines
                }
                
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String gender = data[3].trim();
                List<String> medicalHistory = List.of(data[4].replaceAll("[\\[\\]\"]", "").split(","));
                String vitals = data[5].trim(); // Assuming vitals are stored as a string
                patients.add(new Patient(id, name, age, gender, medicalHistory, vitals));
            }
        } catch (IOException e) {
            System.out.println("Error reading patients file: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from patients data.");
            e.printStackTrace();
        }
        return patients;
    }

    // Read doctors from CSV
    public static List<Doctor> readDoctors(String filePath) {
        List<Doctor> doctors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true; // Skip header
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // Skip header
                    continue;
                }
                String[] data = line.split(",");
                
                // Check if the data has enough fields
                if (data.length < 3) {
                    System.out.println("Invalid doctor data: " + line);
                    continue; // Skip invalid lines
                }
                
                try {
                    int doctorId = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    String specialization = data[2].trim();
                    doctors.add(new Doctor(doctorId, name, specialization));
                } catch (NumberFormatException e) {
                    // No print statement for errors
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading doctors file: " + e.getMessage());
        }
        return doctors;
    }
    

    // Read appointments from CSV
    // Read appointments from CSV
public static List<Appointment> readAppointments(String filePath) {
    List<Appointment> appointments = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        boolean firstLine = true; // Skip header
        while ((line = br.readLine()) != null) {
            if (firstLine) {
                firstLine = false; // Skip header
                continue;
            }
            String[] data = line.split(",");

            // Check if the data has enough fields
            if (data.length < 5) {
                continue; // Skip invalid lines
            }

            int appointmentId = Integer.parseInt(data[0].trim());
            int patientId = Integer.parseInt(data[1].trim());
            int doctorId = Integer.parseInt(data[2].trim());
            LocalDate appointmentDate;

            // Handle different date formats
            try {
                appointmentDate = LocalDate.parse(data[3].trim());
            } catch (DateTimeParseException e) {
                continue; // Skip this line without printing an error
            }

            String status = data[4].trim();
            appointments.add(new Appointment(appointmentId, patientId, doctorId, appointmentDate, status));
        }
    } catch (IOException e) {
        System.out.println("Error reading appointments file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Error parsing number from appointments data.");
    }
    return appointments;
}

}
