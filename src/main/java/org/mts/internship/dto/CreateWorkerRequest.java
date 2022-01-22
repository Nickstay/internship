package org.mts.internship.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWorkerRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String departmentName;
}
