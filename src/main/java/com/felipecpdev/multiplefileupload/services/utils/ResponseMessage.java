package com.felipecpdev.multiplefileupload.services.utils;

import java.time.LocalDateTime;

public record ResponseMessage(
        String message,
        int statusCode,
        LocalDateTime localDateTime) {
}
