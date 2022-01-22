package org.mts.internship.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateWorkerRequest {
    private long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
