package org.employee_rostering;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class EmployeeParser {
    public static List<Employee> parseEmployees(String fileName) {
        try {
            Gson gson = new Gson();
            Type employeeListType = new TypeToken<List<Employee>>(){}.getType();
            return gson.fromJson(new FileReader(fileName), employeeListType);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}