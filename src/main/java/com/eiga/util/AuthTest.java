package com.eiga.util;

import com.eiga.dao.UserDAO;
import com.eiga.model.User;
import com.eiga.service.UserService;
import java.io.File;

public class AuthTest {
    public static void main(String[] args) {
        System.out.println("Starting Auth Tests...");
        try {
            File usersXml = XmlUtil.getDataFile("users.xml");
            if (usersXml.exists()) usersXml.delete();
            
            UserDAO dao = new UserDAO();
            String sharedHash = PasswordUtil.hashPassword("password123");
            
            String[][] initialUsers = {
                {"U1001", "User John", "user@eiga.com", "USER", "", "ACTIVE"},
                {"O1001", "Owner Bob", "owner@eiga.com", "OWNER", "TH001", "ACTIVE"},
                {"S1001", "Staff Alice", "staff@eiga.com", "STAFF", "TH001", "ACTIVE"},
                {"A1001", "Admin Boss", "admin@eiga.com", "ADMIN", "", "ACTIVE"},
                {"I1001", "Inactive Joe", "inactive@eiga.com", "USER", "", "INACTIVE"}
            };
            
            for (String[] uData : initialUsers) {
                User u = new User();
                u.setUserId(uData[0]);
                u.setFullName(uData[1]);
                u.setEmail(uData[2]);
                u.setPasswordHash(sharedHash);
                u.setRole(uData[3]);
                u.setTheatreId(uData[4]);
                u.setStatus(uData[5]);
                dao.save(u);
            }
            System.out.println("Test accounts populated.");
            
            UserService service = new UserService();
            
            System.out.println("Test 1 (Verification): " + (PasswordUtil.verifyPassword("password123", sharedHash) ? "PASS" : "FAIL"));
            
            User loggedIn = service.authenticate("user@eiga.com", "password123");
            System.out.println("Test 2 (Login USER): " + (loggedIn != null && "USER".equals(loggedIn.getRole()) ? "PASS" : "FAIL"));
            
            try {
                service.authenticate("user@eiga.com", "wrongpass");
                System.out.println("Test 3 (Wrong Pass): FAIL (Exception expected)");
            } catch (Exception e) {
                System.out.println("Test 3 (Wrong Pass): PASS");
            }
            
            try {
                service.authenticate("inactive@eiga.com", "password123");
                System.out.println("Test 4 (Inactive): FAIL (Exception expected)");
            } catch (Exception e) {
                System.out.println("Test 4 (Inactive): PASS");
            }
            
            User registered = service.registerUser("New Guy", "new@eiga.com", "123", "pass");
            System.out.println("Test 5 (Register): " + (registered != null && "USER".equals(registered.getRole()) ? "PASS" : "FAIL"));
            
            try {
                service.registerUser("Copy Guy", "new@eiga.com", "123", "pass2");
                System.out.println("Test 6 (Duplicate Email): FAIL (Exception expected)");
            } catch (Exception e) {
                System.out.println("Test 6 (Duplicate Email): PASS");
            }

            System.out.println("All Auth Tests Executed Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
