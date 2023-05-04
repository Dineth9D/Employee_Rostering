package org.employee_rostering;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

import java.util.List;

public class App
{
    public static void main(String[] args) {
        String connectionString = "mongodb+srv://admin:*****@ercluster.xcl52gw.mongodb.net/?retryWrites=true&w=majority";
        try {
            // Connect to the database
            MongoClient mongoClient = MongoClients.create(connectionString);

            // Get the database and the employees collection
            MongoDatabase database = mongoClient.getDatabase("employee_rostering_db");
            MongoCollection<Document> employeesCollection = database.getCollection("employees");

            // Create the employees table if it does not exist
            employeesCollection.createIndex(Indexes.ascending("name", "day", "start_time", "end_time"));

            // Insert employee roster data into the employees table
            List<Employee> employeeList = EmployeeParser.parseEmployees("employee_rostering.json");
            if (employeeList != null) {
                for (Employee employee : employeeList) {
                    String name = employee.getName();
                    List<Availability> availabilityList = employee.getAvailability();
                    for (Availability availability : availabilityList) {
                        String day = availability.getDay();
                        String startTime = availability.getStartTime();
                        String endTime = availability.getEndTime();
                        Document document = new Document("name", name)
                                .append("day", day)
                                .append("start_time", startTime)
                                .append("end_time", endTime);
                        employeesCollection.insertOne(document);
                    }
                }
            } else {
                System.out.println("Failed to read employee roster data from JSON file.");
            }

            // Close the connection
            mongoClient.close();

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }
}

class Availability {
    private String day;
    private String startTime;
    private String endTime;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}

class Employee {
    private String name;
    private List<Availability> availability;

    public Employee(String name, List<Availability> availability) {
        this.name = name;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public List<Availability> getAvailability() {
        return availability;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailability(List<Availability> availability) {
        this.availability = availability;
    }

    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", availability=" + availability +
                '}';
    }
}

class EmployeeParser {
    public static List<Employee> parseEmployees(String fileName) {
        // code to parse employees from a JSON file
        return null;
    }
}