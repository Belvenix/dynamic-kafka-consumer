package com.belvenix.dynamickafkaconsumer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String companyName;
    private String companyKafkaCode;
    private String companyKafkaGroup;
    private int status;

}
