package br.insper.piProjSoft.avaliacao;

import br.insper.piProjSoft.avaliacao.dto.EditAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.ResponseAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.SaveAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.exception.AvaliacaoAlreadyExistsException;
import br.insper.piProjSoft.avaliacao.exception.AvaliacaoNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AvaliacaoServiceTests {

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    private Avaliacao criarAvaliacao(Integer id, String autor, String conteudo, Integer nota, LocalDate dataAvaliacao, boolean ativo) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(id);
        avaliacao.setAutor(autor);
        avaliacao.setConteudo(conteudo);
        avaliacao.setNota(nota);
        avaliacao.setDataAvaliacao(dataAvaliacao);
        avaliacao.setAtivo(ativo);
        return avaliacao;
    }

    @Test
    public void test_shouldReturnAvaliacaoDTOWhenAvaliacaoExists() {
        LocalDate dataAvaliacao = LocalDate.now();
        Avaliacao avaliacao = criarAvaliacao(1, "Engenharia", "oi", 5, dataAvaliacao, true);

        Mockito.when(avaliacaoRepository.findById(1))
                .thenReturn(Optional.of(avaliacao));

        ResponseAvaliacaoDTO response = avaliacaoService.getDTO(1);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("Engenharia", response.getAutor());
        Assertions.assertEquals("oi", response.getConteudo());
        Assertions.assertEquals(5, response.getNota());
        Assertions.assertEquals(dataAvaliacao, response.getDataAvaliacao());
    }

    @Test
    public void test_shouldThrowExceptionWhenAvaliacaoDoesNotExistOrIsInactive() {
        LocalDate dataAvaliacao = LocalDate.now();
        Avaliacao avaliacaoInativo = criarAvaliacao(1, "Engenharia", "oi", 5, dataAvaliacao, false);

        Mockito.when(avaliacaoRepository.findById(1))
                .thenReturn(Optional.empty());

        Mockito.when(avaliacaoRepository.findById(2))
                .thenReturn(Optional.of(avaliacaoInativo));

        Assertions.assertThrows(
                AvaliacaoNotFoundException.class,
                () -> avaliacaoService.get(1)
        );

        Assertions.assertThrows(
                AvaliacaoNotFoundException.class,
                () -> avaliacaoService.get(2)
        );
    }
//
//    @Test
//    public void test_shouldSaveAvaliacao() {
//        LocalDate dataAvaliacao = LocalDate.now();
//        SaveAvaliacaoDTO dto = new SaveAvaliacaoDTO();
//        dto.setAutor("Computação");
//        dto.setConteudo("oii");
//        dto.setNota(1);
//        dto.setDataAvaliacao(dataAvaliacao);
//
//        Mockito.when(avaliacaoRepository.existsByAutor("Computação"))
//                .thenReturn(false);
//        Mockito.when(avaliacaoRepository.existsByConteudo("oii"))
//                .thenReturn(false);
//        Mockito.when(avaliacaoRepository.existsByNota(1))
//                .thenReturn(false);
//        Mockito.when(avaliacaoRepository.existsByDataAvaliacao(dataAvaliacao))
//                .thenReturn(false);
//
//        Mockito.when(avaliacaoRepository.save(Mockito.any(Avaliacao.class)))
//                .thenAnswer(invocation -> {
//                    Avaliacao avaliacao = invocation.getArgument(0);
//                    avaliacao.setId(1);
//                    avaliacao.setAutor("Computação");
//                    avaliacao.setConteudo("oii");
//                    avaliacao.setNota(1);
//                    avaliacao.setDataAvaliacao(1);
//                    avaliacao.setAtivo(true);
//                    return avaliacao;
//                });
//
//        ResponseAvaliacaoDTO response = avaliacaoService.save(dto);
//
//        Assertions.assertEquals(1, response.getId());
//        Assertions.assertEquals("Computação", response.getAutor());
//        Assertions.assertEquals("oii", response.getConteudo());
//        Assertions.assertEquals(1, response.getNota());
//        Assertions.assertEquals(dataAvaliacao, response.getDataAvaliacao());
//    }
//
//    @Test
//    public void test_shouldNotSaveAvaliacaoWithDuplicatedAutor() {
//        LocalDate dataAvaliacao = LocalDate.now();
//        SaveAvaliacaoDTO dto = new SaveAvaliacaoDTO();
//        dto.setAutor("Computação");
//        dto.setConteudo("oii");
//        dto.setNota(1);
//        dto.setDataAvaliacao(dataAvaliacao);
//
//        Mockito.when(avaliacaoRepository.existsByAutor("Computação"))
//                .thenReturn(true);
//
//        Assertions.assertThrows(
//                AvaliacaoAlreadyExistsException.class,
//                () -> avaliacaoService.save(dto)
//        );
//
//        Mockito.verify(
//                avaliacaoRepository,
//                Mockito.never()
//        ).save(Mockito.any(Avaliacao.class));
//    }

    @Test
    public void test_shouldEditAvaliacaoFields() {
        LocalDate dataAvaliacao = LocalDate.now();
        Avaliacao avaliacao = criarAvaliacao(1, "Nome antigo", "oiii", 2, dataAvaliacao,  true);

        EditAvaliacaoDTO dto = new EditAvaliacaoDTO();
        dto.setAutor("Nome novo");
        dto.setConteudo("oiiii");
        dto.setNota(4);
        dto.setDataAvaliacao(dataAvaliacao);
        dto.setAtivo(false);

        Mockito.when(avaliacaoRepository.findById(1))
                .thenReturn(Optional.of(avaliacao));

        Mockito.when(avaliacaoRepository.save(Mockito.any(Avaliacao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseAvaliacaoDTO response = avaliacaoService.edit(1, dto);

        Assertions.assertEquals("Nome novo", response.getAutor());
        Assertions.assertEquals("oiiii", response.getConteudo());
        Assertions.assertEquals(4, response.getNota());
        Assertions.assertEquals(dataAvaliacao, response.getDataAvaliacao());
    }

    @Test
    public void test_shouldDeactivateAvaliacaoWhenDeleting() {
        LocalDate dataAvaliacao = LocalDate.now();
        Avaliacao avaliacao = criarAvaliacao(1, "Nome antigo", "oiii", 2, dataAvaliacao,  true);

        Mockito.when(avaliacaoRepository.findById(1))
                .thenReturn(Optional.of(avaliacao));

        avaliacaoService.delete(1);

        ArgumentCaptor<Avaliacao> captor =
                ArgumentCaptor.forClass(Avaliacao.class);

        Mockito.verify(avaliacaoRepository).save(captor.capture());

        Assertions.assertFalse(captor.getValue().getAtivo());
    }
}