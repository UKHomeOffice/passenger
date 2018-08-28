package org.gov.uk.homeoffice.digital.permissions.passenger.admin.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(NotFoundException.class)
    public String notFound() {
        return "error/404";
    }



}
