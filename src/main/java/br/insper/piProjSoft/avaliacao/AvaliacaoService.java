 package br.insper.piProjSoft.avaliacao;

import br.insper.piProjSoft.avaliacao.dto.EditAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.ResponseAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.SaveAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.exception.AvaliacaoAlreadyExistsException;
import br.insper.piProjSoft.avaliacao.exception.AvaliacaoNotFoundException;
// ALTERAÇÃO: imports das interfaces do observer
import br.insper.piProjSoft.avaliacao.exception.AvaliacaoWrongNotaException;
import br.insper.piProjSoft.avaliacao.observer.AvaliacaoObservable;
import br.insper.piProjSoft.avaliacao.observer.AvaliacaoObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List; // ALTERAÇÃO: import necessário para a lista de observers

// ALTERAÇÃO: o service agora implementa AvaliacaoObservable, assumindo o papel de "Subject"
@Service
public class AvaliacaoService implements AvaliacaoObservable {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    // ALTERAÇÃO: injeção de todos os beans que implementam AvaliacaoObserver
    // (EmailNotifierObserver e SmsNotifierObserver). "required = false" evita
    // erro caso nenhum observer esteja registrado no contexto.
    @Autowired(required = false)
    private List<AvaliacaoObserver> observers;

    // ALTERAÇÃO: implementação do método da interface AvaliacaoObservable.
    // Percorre todos os observers e chama atualizar(), notificando-os.
    @Override
    public void notificarObservadores(Avaliacao avaliacao) {
        if (observers != null) {
            for (AvaliacaoObserver observer : observers) {
                observer.atualizar(avaliacao);
            }
        }
    }

    public Avaliacao get(Integer id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException());

        if (!avaliacao.getAtivo()) {
            throw new AvaliacaoNotFoundException();
        }

        return avaliacao;
    }

    public ResponseAvaliacaoDTO getDTO(Integer id) {
        return ResponseAvaliacaoDTO.toDTO(get(id));
    }

    public ResponseAvaliacaoDTO save(SaveAvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor(dto.getAutor());
        avaliacao.setConteudo(dto.getConteudo());
        avaliacao.setNota(dto.getNota());
        avaliacao.setDataAvaliacao(dto.getDataAvaliacao());
        avaliacao.setAtivo(true);

        avaliacao = avaliacaoRepository.save(avaliacao);

        // ALTERAÇÃO: após salvar o avaliacao com sucesso, notificamos os observers.
        notificarObservadores(avaliacao);

        return ResponseAvaliacaoDTO.toDTO(avaliacao);
    }

    public Page<ResponseAvaliacaoDTO> list(String nome, Pageable pageable) {
        Page<Avaliacao> avaliacaos;

        avaliacaos = avaliacaoRepository.findByAtivoTrue(pageable);

        return avaliacaos.map(ResponseAvaliacaoDTO::toDTO);
    }

    public ResponseAvaliacaoDTO edit(Integer id, EditAvaliacaoDTO dto) {
        Avaliacao avaliacao = get(id);

        if (dto.getAutor() != null) {
            avaliacao.setAutor(dto.getAutor());
        }
        if (dto.getConteudo() != null) {
            avaliacao.setConteudo(dto.getConteudo());
        }
        if (dto.getNota() != null) {
            avaliacao.setNota(dto.getNota());
        }
        if (dto.getDataAvaliacao() != null) {
            avaliacao.setDataAvaliacao(dto.getDataAvaliacao());
        }

        if (dto.getAtivo() != null) {
            avaliacao.setAtivo(dto.getAtivo());
        }

        avaliacao = avaliacaoRepository.save(avaliacao);

        return ResponseAvaliacaoDTO.toDTO(avaliacao);
    }

    public void delete(Integer id) {
        Avaliacao avaliacao = get(id);

        avaliacao.setAtivo(false);
        avaliacaoRepository.save(avaliacao);
    }
}