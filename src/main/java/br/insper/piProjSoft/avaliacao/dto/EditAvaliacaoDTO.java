package br.insper.piProjSoft.avaliacao.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EditAvaliacaoDTO {
    private String autor;
    private String conteudo;
    private Integer nota;
    private LocalDate dataAvaliacao;
    private Boolean ativo;
}
//id, autor, conteudo, nota (1-5), dataAvaliacao.