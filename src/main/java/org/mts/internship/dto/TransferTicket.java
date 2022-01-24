package org.mts.internship.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TransferTicket {
    private long workerId;
    private String destinationDepartmentName;
}
