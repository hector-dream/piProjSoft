package br.insper.piProjSoft.avaliacao.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SaveAvaliacaoDTO {
    private String autor;
    private String conteudo;
    private Integer nota;
    private LocalDate dataAvaliacao;
}