package com.oloid.studentservice.service;

import com.google.gson.Gson;
import com.oloid.studentservice.model.Student;
import com.oloid.studentservice.repository.MySQLStudentRepository;
import com.oloid.studentservice.repository.StudentRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

public class StudentService extends AbstractVerticle {

  private StudentRepository studentRepository;
  private EventBus eventBus;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @Override
  public void start(Promise<Void> startPromise) {
    eventBus = vertx.eventBus();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    // Define routes
    router.post("/students").handler(this::createStudent);

    // Create HTTP server and listen on port 8080
    vertx.createHttpServer().requestHandler(router).listen(8084, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server started on port 8082");
        startPromise.complete();
      } else {
        System.out.println("Failed to start server: " + ar.cause());
        startPromise.fail(ar.cause());
      }
    });
  }

  private void createStudent(RoutingContext context) {

    HttpServerResponse response = context.response();
    response.putHeader("content-type", "application/json");

    //Student student = Json.decodeValue(context.getBodyAsString(), Student.class);
    JsonObject body = context.body().asJsonObject();
    Gson gson = new Gson();
    Student student = gson.fromJson(body.toString(), Student.class);
    if (studentRepository.studentAlreadyExists(student.getIdentityNumber())) {
      response.setStatusCode(400).end("Student already exists");
    } else {
      studentRepository.saveStudent(student);
      //eventBus.publish("student-saved", JsonObject.mapFrom(student));
      handleStudentSavedEvent();
      response.setStatusCode(201).end(Json.encode(student));
    }
  }

  private void handleStudentSavedEvent() {
    WebClient webClient = WebClient.create(vertx);

// Prepare the request data
    JsonObject requestData = new JsonObject()
      .put("name", "John")
      .put("age", 25);

// Make the POST request to the third-party endpoint
    webClient.post("http://localhost:8083/third-party-service")// TODO: Keep in property file
      .sendJsonObject(requestData, ar -> {
        if (ar.succeeded()) {
          // Handle the successful response
          HttpResponse<Buffer> response = ar.result();
          int statusCode = response.statusCode();
          String responseBody = response.bodyAsString();
          System.out.println(responseBody);// TODO : Process the response as needed

        } else {
          // Handle the failure scenario
          Throwable cause = ar.cause();
          cause.printStackTrace();
        }
      });

  }
}
