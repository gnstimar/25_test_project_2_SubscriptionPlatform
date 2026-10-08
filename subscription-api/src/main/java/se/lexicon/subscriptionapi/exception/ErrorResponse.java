package se.lexicon.subscriptionapi.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String message,
        LocalDateTime timeStamp
) {
}
