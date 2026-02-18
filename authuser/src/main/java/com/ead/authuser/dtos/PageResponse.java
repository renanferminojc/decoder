package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PageResponse<T>(
    List<T> content,
    @JsonProperty("total_elements") long totalElements,
    @JsonProperty("total_pages") int totalPages) {}
