package org.employee_rostering;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRosterSystem {
    private static final String DATABASE_NAME = "employee_rostering_db";
    private static final String COLLECTION_NAME = "employees";
    private static final String[] SHIFTS = {"9am-5pm", "5pm-1am", "1am-9am"};

    private final List<Employee> employees;
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> employeesCollection;

    public EmployeeRosterSystem(String connectionString, List<Employee> employees) {
        this.employees = employees;
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(DATABASE_NAME);
        this.employeesCollection = database.getCollection(COLLECTION_NAME);
    }

    public static void main(String[] args) {
        String connectionString = "mongodb+srv://admin:supersafe@ercluster.xcl52gw.mongodb.net/?retryWrites=true&w=majority";
        String jsonFilePath = "employee_rostering.json";

        // Parse employees from JSON file
        List<Employee> employees = EmployeeParser.parseEmployees(jsonFilePath);
        if (employees == null) {
            System.out.println("Failed to read employee data from JSON file.");
            return;
        }

        // Create an instance of EmployeeRosterSystem
        EmployeeRosterSystem rosterSystem = new EmployeeRosterSystem(connectionString, employees);

        // Create employees table in the MongoDB collection
        rosterSystem.createEmployeesTable();

        // Insert employee data into the collection
        rosterSystem.insertEmployees();

        // Print the employee roster
        rosterSystem.printRoster();

        // Close the database connection
        rosterSystem.closeConnection();
    }

    public void createEmployeesTable() {
        employeesCollection.createIndex(Indexes.ascending("name", "day", "shift"));
    }

    public void insertEmployees() {
        List<Document> documents = new ArrayList<>();
        for (Employee employee : employees) {
            String name = employee.getName();
            List<Availability> availabilityList = employee.getAvailability();
            for (Availability availability : availabilityList) {
                String day = availability.getDay();
                String shift = availability.getShift();
                Document document = new Document("name", name)
                        .append("day", day)
                        .append("shift", shift);
                documents.add(document);
            }
        }
        employeesCollection.insertMany(documents);
    }

    public void printRoster() {
        System.out.println("Employee Roster:");
        for (String shift : SHIFTS) {
            System.out.println("Shift: " + shift);
            for (int day = 1; day <= 7; day++) {
                System.out.println("Day " + day + ": " + getEmployeeForShift(day, shift));
            }
            System.out.println();
        }
    }

    private String getEmployeeForShift(int day, String shift) {
        List<String> employeesForShift = new ArrayList<>();
        for (Employee employee : employees) {
            String name = employee.getName();
            List<Availability> availabilityList = employee.getAvailability();
            for (Availability availability : availabilityList) {
                if (availability.getDay().equals(String.valueOf(day)) && availability.getShift().equals(shift)) {
                    employeesForShift.add(name);
                    break;
                }
            }
        }
        return employeesForShift.isEmpty() ? "No employee available" : String.join(", ", employeesForShift);
    }

    public void closeConnection() {
        mongoClient.close();
    }
}