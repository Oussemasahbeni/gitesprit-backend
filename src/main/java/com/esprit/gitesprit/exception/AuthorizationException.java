package com.esprit.gitesprit.exception;

public class AuthorizationException extends ApplicationException {

    private static final long serialVersionUID = 5477619054099558741L;

    public enum AuthorizationExceptionType implements ExceptionType {
        GENERIC("error.server.authorization.generic.title", "error.server.authorization.generic.msg", null),
        INVALID_CREDENTIALS(
                "error.server.authorization.invalid-credential.title",
                "error.server.authorization.invalid-credential.msg",
                null),
        USER_TEMPORARY_TOKEN_INVALID(
                "error.server.authorization.temporary-token-invalid.title",
                "error.server.authorization.temporary-token-invalid.msg",
                null),
        CURRENT_PASSWORD_INVALID(
                "error.server.authorization.current-password-invalid.title",
                "error.server.authorization.current-password-invalid.msg",
                null),
        TOO_MANY_PASSWORD_CHANGES(
                "error.server.authorization.too-many-password-changes.title",
                "error.server.authorization.too-many-password-changes.msg",
                null),
        ACCOUNT_DISABLED(
                "error.server.authorization.account-disabled.title",
                "error.server.authorization.account-disabled.msg",
                null);

        private final String messageKey;
        private final String titleKey;
        private final String messageCause;

        AuthorizationExceptionType(String titleKey, String messageKey, String messageCause) {
            this.messageKey = messageKey;
            this.titleKey = titleKey;
            this.messageCause = messageCause;
        }

        @Override
        public String getTitleKey() {
            return titleKey;
        }

        @Override
        public String getMessageKey() {
            return messageKey;
        }

        @Override
        public String getMessageCause() {
            return messageCause;
        }
    }

    public AuthorizationException(AuthorizationExceptionType type) {
        super(type);
    }

    public AuthorizationException(AuthorizationExceptionType type, Throwable cause) {
        super(type, cause);
    }

    public AuthorizationException(AuthorizationExceptionType type, String message, Throwable cause) {
        super(type, message, cause);
    }

    public AuthorizationException(
            AuthorizationExceptionType type, String message, Throwable cause, Object... keyParams) {
        super(type, message, cause, keyParams);
    }

    public AuthorizationException(AuthorizationExceptionType type, Object[] valueParams, Object... keyParams) {
        super(type, valueParams, keyParams);
    }

    public AuthorizationException(AuthorizationExceptionType type, Object... valueParams) {
        super(type, valueParams);
    }
}
