package com.epam.esm.validation;

import javax.validation.groups.Default;

public class ValidationGroup {

    public interface Update extends Default {
    }

    public interface Create extends Default {
    }
}
