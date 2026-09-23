package br.insper.piProjSoft.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ErrorDTOTests {

    @Test
    public void test_shouldSetAndGetAllFields() {
        ErrorDTO dto = new ErrorDTO();
        LocalDateTime data = LocalDateTime.now();

        dto.setMensagem("Recurso não encontrado");
        dto.setCodigoHttp(404);
        dto.setData(data);
        dto.setPath("/cursos/1");
        dto.setCodigoErro("CURSO_NOT_FOUND");

        Assertions.assertEquals(
                "Recurso não encontrado",
                dto.getMensagem()
        );
        Assertions.assertEquals(404, dto.getCodigoHttp());
        Assertions.assertEquals(data, dto.getData());
        Assertions.assertEquals("/cursos/1", dto.getPath());
        Assertions.assertEquals(
                "CURSO_NOT_FOUND",
                dto.getCodigoErro()
        );
    }
}