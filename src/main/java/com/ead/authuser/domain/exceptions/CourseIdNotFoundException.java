package com.ead.authuser.domain.exceptions;

public class CourseIdNotFoundException extends EntityNotExistsException{
    public CourseIdNotFoundException() {
        super("That courseId does not exist in this relationship");
    }
}
