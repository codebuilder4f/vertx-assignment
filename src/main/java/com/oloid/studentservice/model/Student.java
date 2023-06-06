package com.oloid.studentservice.model;

public class Student {

  private String name;
  private String identityNumber;

  public Student() {
  }

  public Student(String name, String identityNumber) {
    this.name = name;
    this.identityNumber = identityNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdentityNumber() {
    return identityNumber;
  }

  public void setIdentityNumber(String identityNumber) {
    this.identityNumber = identityNumber;
  }
}
