package com.bradenn.dev.enclave.messages;

public enum Response {

    /* Enclave Messages */
    ENCLAVE_NAME_CHANGED("Enclave name has been changed to %s."),

    ENCLAVE_CREATED("The enclave %s has been created."),
    E_INVALID_ENCLAVE_NAME("Invalid enclave name.", true),

    SHOW_BORDER_ENABLED("Enclave chunk borders have been enabled."),
    SHOW_BORDER_DISABLED("Enclave chunk borders have been disabled."),

    ENCLAVE_DISBANDED("The enclave %s has been disbanded."),

    TAG_ENABLED("The attribute %s has been enabled."),
    TAG_DISABLED("The attribute %s has been disabled."),
    E_INVALID_TAG("Invalid tag provided.", true),

    COLOR_CHANGED("The enclave color has been changed to %s."),
    E_INVALID_COLOR("Invalid color provided.", true),

    HOME_SET("Your enclave home has been set to your current position."),
    E_NO_HOME("Your enclave does not have a home set.", true),

    TAG_LIST_ENABLED("Enabled attributes: %s"),
    TAG_LIST_DISABLED("Disabled attributes: %s"),

    ENCLAVE_JOINED("You have joined the enclave %s."),
    ENCLAVE_LEFT("You have left the enclave %s."),
    WELCOME_HOME("Welcome home."),

    ENCLAVE_CANNOT_LEAVE("You cannot leave an enclave that you own."),

    INVITE_SENT("An invite has been sent to %s."),
    INVITE_PLAYER("You have been invited to join the enclave %s. Type /e join to accept and join."),
    E_NO_PENDING("You have no pending invites.", true),
    E_ALREADY_IN_ENCLAVE("This player is already in an enclave.", true),

    /* Region Messages */
    CHUNK_CLAIMED("Chunk claimed. (%d/%d)"),
    CHUNK_UNCLAIMED("This chunk has been unclaimed."),
    E_CHUNK_QUOTA_EXCEEDED("Cannot claim chunk. Your enclave has exceeded its chunk claim quota. (%d/%d)", true),

    E_CLAIMED("This chunk is claimed.", true),
    E_CHUNK_CLAIMED("Chunk has already been claimed.", true),
    E_NONMEMBER_CHUNK("This chunk does not belong to your enclave.", true),

    /* Generic errors */
    E_ENCLAVE("You are already a member of an enclave.", true),
    E_NO_ENCLAVE("You are not a member of any enclave.", true),

    E_INVALID_PLAYER("Invalid player provided.", true),
    E_PLAYER_OFFLINE("The player provided is currently offline.", true),

    E_INSUFFICIENT_PERMISSION("You do not have permission to do that.", true),
    E_INSUFFICIENT_CLOUT("You must be the owner of the enclave to do that.", true),
    ;

    private final String message;
    private boolean error = false;

    Response(String s) {
        this.message = s;
    }

    Response(String s, boolean error) {
        this.message = s;
        this.error = error;
    }

    String getMessage() {
        return message;
    }

    boolean isError() {
        return error;
    }
}