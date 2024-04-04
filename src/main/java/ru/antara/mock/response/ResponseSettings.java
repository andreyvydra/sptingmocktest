package ru.antara.mock.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.antara.mock.data.Status;

@JsonSerialize
public record ResponseSettings(Long timeSleep, Long timeOut) {
}
