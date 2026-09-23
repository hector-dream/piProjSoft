package br.insper.piProjSoft.avaliacao.observer;

import br.insper.piProjSoft.avaliacao.Avaliacao;

public interface AvaliacaoObservable {
    void notificarObservadores(Avaliacao avaliacao);
}