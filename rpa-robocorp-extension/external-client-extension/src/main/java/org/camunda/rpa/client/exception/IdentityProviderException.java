package org.camunda.rpa.client.exception;

/**
 * To throw during the identity provider setup / token refresh
 */
public class IdentityProviderException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public IdentityProviderException(String message) {
        super(message);
    }

    public IdentityProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
