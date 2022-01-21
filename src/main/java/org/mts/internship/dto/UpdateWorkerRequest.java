package org.mts.internship.dto;

import lombok.Getter;

@Getter
public class UpdateWorkerRequest {
    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
