package org.mts.internship.dto;

import lombok.Getter;

@Getter
public class CreateWorkerRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String departmentName;
}
