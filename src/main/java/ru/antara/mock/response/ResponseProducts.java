package ru.antara.mock.response;

import ru.antara.mock.data.Product;
import ru.antara.mock.data.Status;

import java.util.List;

public record ResponseProducts(Status status, List<Product> items) {}
