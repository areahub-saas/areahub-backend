package br.com.areahub.app.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StandartError implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Integer statusCode;
    private String errorCode;
    private String message;
    private Long timestamp;
}
