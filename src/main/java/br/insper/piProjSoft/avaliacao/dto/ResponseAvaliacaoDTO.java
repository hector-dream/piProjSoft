package br.insper.piProjSoft.avaliacao.dto;

import br.insper.piProjSoft.avaliacao.Avaliacao;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ResponseAvaliacaoDTO {

    private Integer id;
    private String autor;
    private String conteudo;
    private Integer nota;
    private LocalDate dataAvaliacao;

    public static ResponseAvaliacaoDTO toDTO(Avaliacao avaliacao) {
        ResponseAvaliacaoDTO dto = new ResponseAvaliacaoDTO();

        dto.setId(avaliacao.getId());
        dto.setAutor(avaliacao.getAutor());
        dto.setConteudo(avaliacao.getConteudo());
        dto.setNota(avaliacao.getNota());
        dto.setDataAvaliacao(avaliacao.getDataAvaliacao());

        return dto;
    }
}