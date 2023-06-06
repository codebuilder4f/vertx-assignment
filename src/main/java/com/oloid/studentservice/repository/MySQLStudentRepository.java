package com.oloid.studentservice.repository;

import com.oloid.studentservice.model.Student;
import com.oloid.studentservice.exceptions.RepositoryException;

import java.sql.*;

public class MySQLStudentRepository implements StudentRepository {

  private static final String DB_URL = "jdbc:mysql://localhost:3306/students";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "";

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }

  @Override
  public void saveStudent(Student student) {
    try (Connection connection = getConnection()) {
      String sql = "INSERT INTO students (name, identity_number) VALUES (?, ?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, student.getName());
      statement.setString(2, student.getIdentityNumber());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RepositoryException("Failed to save student", e);
    }
  }

  @Override
  public boolean studentAlreadyExists(String identityNumber) {
    try (Connection connection = getConnection()) {
      String sql = "SELECT COUNT(*) FROM students WHERE identity_number = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, identityNumber);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        int count = resultSet.getInt(1);
        return count > 0;
      }
    } catch (SQLException e) {
      throw new RepositoryException("Failed to check student existence", e);
    }
    return false;
  }
}
