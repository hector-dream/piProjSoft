package br.insper.piProjSoft.avaliacao;

import br.insper.piProjSoft.avaliacao.dto.EditAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.ResponseAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.SaveAvaliacaoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AvaliacaoControllerTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("avaliacao_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_shouldCreateAvaliacao() throws Exception {
        LocalDate dataAvaliacao = LocalDate.now();
        SaveAvaliacaoDTO dto = new SaveAvaliacaoDTO();
        dto.setAutor("Computação");
        dto.setConteudo("oii");
        dto.setNota(1);
        dto.setDataAvaliacao(dataAvaliacao);

        // chamada
        MvcResult result = mockMvc.perform(
                        post("/avaliacao")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn();

        // asserts
        ResponseAvaliacaoDTO avaliacao = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ResponseAvaliacaoDTO.class);
        Assertions.assertNotNull(avaliacao.getId());
        Assertions.assertEquals("Computação", avaliacao.getAutor());
        Assertions.assertEquals("oii", avaliacao.getConteudo());
        Assertions.assertEquals(1, avaliacao.getNota());
        Assertions.assertEquals(dataAvaliacao, avaliacao.getDataAvaliacao());
    }
}