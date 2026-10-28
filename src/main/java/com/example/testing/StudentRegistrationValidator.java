
package com.example.testing;

public class StudentRegistrationValidator {

    public boolean isValidStudent(String name, String studentId, int age) {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }

        if (age < 16 || age > 100) {
            return false;
        }

        return true;
    }
}
