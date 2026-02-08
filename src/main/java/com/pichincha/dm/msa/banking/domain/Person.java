package com.pichincha.dm.msa.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String gender;
  private Integer age;

  @Column(unique = true)
  private String identification;

  private String address;
  private String phone;
}
