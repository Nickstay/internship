package org.mts.internship.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateWorkerRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String departmentName;
}
