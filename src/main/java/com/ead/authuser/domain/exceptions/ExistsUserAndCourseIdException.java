package com.ead.authuser.domain.exceptions;

public class ExistsUserAndCourseIdException extends EntityInUseException{
    public ExistsUserAndCourseIdException() {
        super("There is already a user enrolled in this course");
    }
}
