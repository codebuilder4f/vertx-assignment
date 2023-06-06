package com.oloid.studentservice.app;

import com.oloid.studentservice.repository.MySQLStudentRepository;
import com.oloid.studentservice.repository.StudentRepository;
import com.oloid.studentservice.service.EventBusService;
import com.oloid.studentservice.service.StudentService;
import io.vertx.core.Vertx;

public class App {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    StudentRepository studentRepository = new MySQLStudentRepository();

    // Deploy the MyRestServiceVerticle with the MySQLStudentRepository instance
    vertx.deployVerticle(new StudentService(studentRepository));
    vertx.deployVerticle(new EventBusService());

  }
}
