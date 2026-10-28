
package com.example.testing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class StudentRegistrationValidatorTest {

    private final StudentRegistrationValidator validator =
            new StudentRegistrationValidator();

    @Test
    void testValidStudent() {
        assertTrue(
            validator.isValidStudent("Riddhi", "STU101", 20)
        );
    }

    @Test
    void testEmptyStudentName() {
        assertFalse(
            validator.isValidStudent("", "STU101", 20)
        );
    }

    @Test
    void testNullStudentName() {
        assertFalse(
            validator.isValidStudent(null, "STU101", 20)
        );
    }

    @Test
    void testEmptyStudentId() {
        assertFalse(
            validator.isValidStudent("Riddhi", "", 20)
        );
    }

    @Test
    void testMinimumValidAge() {
        assertTrue(
            validator.isValidStudent("Riddhi", "STU101", 16)
        );
    }

    @Test
    void testMaximumValidAge() {
        assertTrue(
            validator.isValidStudent("Riddhi", "STU101", 100)
        );
    }

    @Test
    void testAgeBelowMinimum() {
        assertFalse(
            validator.isValidStudent("Riddhi", "STU101", 15)
        );
    }

    @Test
    void testAgeAboveMaximum() {
        assertFalse(
            validator.isValidStudent("Riddhi", "STU101", 101)
        );
    }
}
