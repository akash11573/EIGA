package com.eiga.service;

import com.eiga.dao.UserDAO;
import com.eiga.model.User;
import com.eiga.util.PasswordUtil;
import java.time.LocalDateTime;

public class UserService {
    private UserDAO userDAO = new UserDAO();
    
    public User authenticate(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new Exception("Invalid email or password");
        }
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new Exception("Account is inactive");
        }
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new Exception("Invalid email or password");
        }
        return user;
    }
    
    public User registerUser(String fullName, String email, String mobile, String password) throws Exception {
        if (userDAO.findByEmail(email) != null) {
            throw new Exception("Email is already registered");
        }
        if (fullName == null || fullName.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new Exception("All fields are required");
        }
        User user = new User();
        user.setUserId("U" + System.currentTimeMillis());
        user.setFullName(fullName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setRole("USER");
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        
        userDAO.save(user);
        return user;
    }
}
