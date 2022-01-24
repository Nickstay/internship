package org.mts.internship.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String departmentName) {
        super("Department not found by name: " + departmentName);
    }
}
