package org.mts.internship.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class WorkerDto {
    private long Id;
    private String firstName;
    private String lastName;
    private String departmentName;
}
