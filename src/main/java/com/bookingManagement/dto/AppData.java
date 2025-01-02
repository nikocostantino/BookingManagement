package com.bookingManagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AppData implements Serializable {

    @JsonProperty("initialCount")
    private int initialCount;

    @JsonProperty("isClose")
    private boolean isClose;

    @JsonProperty("error")
    private Boolean error;
}
