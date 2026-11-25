package org.example.dynamicsearcharray.Data;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.Main;

import java.io.InputStream;
import java.util.StringJoiner;
import java.util.Scanner;

public class NestedArraySearch {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // Step 1: Get search inputs from the user

        System.out.print("Enter company name: ");
        String companyName = scanner.nextLine().trim();

        System.out.print("Enter project name: ");
        String projectName = scanner.nextLine().trim();

        System.out.print("Enter skill to search: ");
        String skillName = scanner.nextLine().trim();

        // Step 2: Read the JSON file

        ObjectMapper mapper = new ObjectMapper();

        InputStream input = Main.class.getClassLoader()
                .getResourceAsStream("companies.json");

        if (input == null) {
            System.out.println("JSON file not found!");
            return;
        }

        JsonNode root = mapper.readTree(input);

        // Step 3: Initialize search status variables

        boolean companyFound = false;
        boolean projectFound = false;
        boolean employeeFound = false;

        // Step 4: Search for the company

        for (JsonNode company : root.path("Companies")) {

            String actualCompanyName = company.path("Company").asText();


            if (actualCompanyName.equalsIgnoreCase(companyName)) {

                companyFound = true;

                // Step 5: Search for the project

                for (JsonNode project : company.path("Projects")) {

                    String actualProjectName = project.path("ProjectName").asText();

                    if (actualProjectName.equalsIgnoreCase(projectName)) {

                        projectFound = true;

                        // Step 6: Search employees in the project

                        for (JsonNode employee : project.path("Employees")) {

                            boolean skillMatched = false;

                            // Step 7: Check employee skills

                            for (JsonNode skill : employee.path("Skills")) {

                                if (skill.asText().equalsIgnoreCase(skillName)) {

                                    skillMatched = true;
                                    break;
                                }
                            }

                            // Step 8: Display employee if skill matches

                            if (skillMatched) {

                                employeeFound = true;

                                System.out.println("\nEmployee Details");

                                System.out.println("EmployeeId : " + employee.path("EmployeeId").asText());

                                System.out.println("EmployeeName : " + employee.path("EmployeeName").asText());

                                StringJoiner skills = new StringJoiner(", ");

                                for (JsonNode skill : employee.path("Skills")) {

                                    skills.add(skill.asText());
                                }

                                System.out.println("Skills : " + skills);

                                System.out.println();
                            }
                        }
                    }
                }
            }
        }

        // Step 9: Display appropriate messages

        if (!companyFound) {
            System.out.println("Company not found!");
        }
        else if (!projectFound) {
            System.out.println("Project not found in this company!");
        }
        else if (!employeeFound) {
            System.out.println(
                    "No employees found with the required skill!");
        }
        scanner.close();
    }
}