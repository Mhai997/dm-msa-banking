package com.pichincha.dm.msa.banking.domain;

import com.pichincha.dm.msa.banking.domain.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Person {

  @Column(name = "name", nullable = false, length = 150)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender", nullable = false, length = 20)
  private Gender gender;

  @Column(name = "age", nullable = false)
  private Integer age;

  @Column(name = "identification", nullable = false, length = 20, unique = true)
  private String identification;

  @Column(name = "address", nullable = false, length = 255)
  private String address;

  @Column(name = "phone", nullable = false, length = 30)
  private String phone;
}

