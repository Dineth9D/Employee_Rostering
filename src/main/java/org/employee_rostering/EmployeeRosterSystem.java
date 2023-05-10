package org.employee_rostering;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class EmployeeRosterSystem {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public EmployeeRosterSystem() {
        // Connect to MongoDB
        // Replace the uri string with your MongoDB deployment's connection string.
        ConnectionString connString = new ConnectionString("mongodb+srv://admin:supersafe@ercluster.xcl52gw.mongodb.net/?retryWrites=true&w=majority");

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .codecRegistry(
                        CodecRegistries.fromRegistries(
                                MongoClientSettings.getDefaultCodecRegistry(),
                                CodecRegistries.fromProviders(
                                        PojoCodecProvider.builder().automatic(true).build()
                                )
                        )
                )
                .retryWrites(true)
                .build();

        // Create a new client and connect to the server
        mongoClient = MongoClients.create(settings);

        try {
            // Send a ping to confirm a successful connection
            database = mongoClient.getDatabase("employee_rostering_db");
            collection = database.getCollection("employees");
            database.runCommand(new Document("ping", 1));
            System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public void loadEmployeeData(List<Employee> employees) {
        for (Employee employee : employees) {
            Document doc = new Document("name", employee.getName())
                    .append("availability", employee.getAvailability());
            collection.insertOne(doc);
        }
    }

    public void generateRoster() {
        // TODO: Implement roster generation logic
    }

    public static void main(String[] args) {
        // Load employee data from the JSON file
        List<Employee> employees = loadEmployeeDataFromJSON();

        if (employees != null) {
            // Create an instance of the EmployeeRosterSystem
            EmployeeRosterSystem rosterSystem = new EmployeeRosterSystem();

            // Load employee data into MongoDB
            rosterSystem.loadEmployeeData(employees);

            // Generate the roster
            rosterSystem.generateRoster();
        }
    }

    public static List<Employee> loadEmployeeDataFromJSON() {
        try (FileReader reader = new FileReader("employee_rostering.json")) {
            Gson gson = new Gson();
            Type employeeListType = new TypeToken<List<Employee>>() {}.getType();
            return gson.fromJson(reader, employeeListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
