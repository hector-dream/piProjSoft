package br.insper.piProjSoft.avaliacao;

import br.insper.piProjSoft.avaliacao.dto.EditAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.ResponseAvaliacaoDTO;
import br.insper.piProjSoft.avaliacao.dto.SaveAvaliacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseAvaliacaoDTO saveAvaliacao(@RequestBody SaveAvaliacaoDTO dto) {
        return avaliacaoService.save(dto);
    }

    @GetMapping
    public Page<ResponseAvaliacaoDTO> listAvaliacaos(
            @RequestParam(required = false) String nome,
            Pageable pageable) {
        return avaliacaoService.list(nome, pageable);
    }

    @GetMapping("/{id}")
    public ResponseAvaliacaoDTO getAvaliacao(@PathVariable Integer id) {
        return avaliacaoService.getDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseAvaliacaoDTO editAvaliacao(@PathVariable Integer id, @RequestBody EditAvaliacaoDTO dto) {
        return avaliacaoService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAvaliacao(@PathVariable Integer id) {
        avaliacaoService.delete(id);
    }
}