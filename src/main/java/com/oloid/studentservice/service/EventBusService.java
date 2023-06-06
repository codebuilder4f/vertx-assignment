package com.oloid.studentservice.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class EventBusService extends AbstractVerticle {

  private WebClient webClient;

  @Override
  public void start() {
    webClient = WebClient.create(vertx);

    // Subscribe to the "student-saved" event on the EventBus
    vertx.eventBus().consumer("student-saved", this::handleStudentSavedEvent);
  }

  private void handleStudentSavedEvent(Message<JsonObject> message) {
    JsonObject studentData = message.body();
    // Prepare the request to the REST web service
    webClient.post("http://localhost:8083/third-party-service")//TODO : Keep in property file
      .sendJsonObject(studentData, ar -> {
        if (ar.succeeded()) {
          System.out.println("Success");
        } else {
          // Handle the failure if the request failed
          System.out.println("Failed");
        }
      });
  }
}
