package br.insper.piProjSoft.avaliacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {
    Page<Avaliacao> findByAtivoTrue(Pageable pageable);

    Object existsByAutor(String autor);

    Object existsByConteudo(String conteudo);

    Object existsByNota(Integer nota);

    Object existsByDataAvaliacao(LocalDate data);
}