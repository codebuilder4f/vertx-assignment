package com.oloid.studentservice.repository;

import com.oloid.studentservice.model.Student;

public interface StudentRepository {
  void saveStudent(Student student);
  boolean studentAlreadyExists(String identityNumber);
}
