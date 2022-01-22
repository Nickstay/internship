package org.mts.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DepartmentDto {
    private long id;
    private String name;
}
