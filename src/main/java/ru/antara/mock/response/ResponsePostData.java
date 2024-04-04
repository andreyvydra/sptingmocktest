package ru.antara.mock.response;

import ru.antara.mock.data.Status;

public record ResponsePostData(Status status, String message) {
}
