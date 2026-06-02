package com.infnet.service.utils;

public final class UtilService {

    private UtilService() {
    }

    public static void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page não pode ser negativo.");
        }

        if (size < 1 || size > 50) {
            throw new IllegalArgumentException("size deve estar entre 1 e 50.");
        }
    }
}
