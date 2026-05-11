package com.br.agenda.service;

import com.br.agenda.dto.CriarProcedimentoRequest;
import com.br.agenda.dto.EditarProcedimentoRequest;
import com.br.agenda.entity.Procedimento;
import com.br.agenda.repository.ProcedimentoRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProcedimentoService {
    private final ProcedimentoRepository procedimentoRepository;

    public ProcedimentoService(ProcedimentoRepository procedimentoRepository){
        this.procedimentoRepository = procedimentoRepository;
    }

    public List<Procedimento> findAll(){
        return procedimentoRepository.findAll();
    }

    public Procedimento criar(CriarProcedimentoRequest request){
        StringBuilder builder = new StringBuilder();
        if(Strings.isBlank(request.nome())){
            builder.append("Favor, informar o nome do procedimento.").append("\n");
        }
        if(Strings.isBlank(String.valueOf(request.valor()))){
            builder.append("Favor, informar o valor do procedimento.").append("\n");
        }
        BigDecimal valor = new BigDecimal(
                request.valor()
                        .replace("R$", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .trim()
        );
        var procedimento = new Procedimento();
        procedimento.setNome(request.nome());
        procedimento.setValor(valor);
        return procedimentoRepository.save(procedimento);
    }

    public Optional<Procedimento> obterPeloId(Long id){
        return  procedimentoRepository.findById(id);
    }

    public Procedimento editar(EditarProcedimentoRequest request){
        StringBuilder builder = new StringBuilder();
        if(Strings.isBlank(request.nome())){
            builder.append("Favor, informar o nome do procedimento.").append("\n");
        }
        if(Strings.isBlank(String.valueOf(request.valor()))){
            builder.append("Favor, informar o telefone do procedimento.").append("\n");
        }
        var old = procedimentoRepository.findById(request.id()).orElseThrow();
        old.setNome(request.nome());
        old.setValor(request.valor());
        return procedimentoRepository.save(old);
    }

    public void deletarPeloId(Long id){
        procedimentoRepository.deleteById(id);
    }

}
