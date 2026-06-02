package com.br.agenda.service;

import com.br.agenda.dto.CriarAgendamentoRequest;
import com.br.agenda.dto.EditarAgendamentoRequest;
import com.br.agenda.entity.Agendamento;
import com.br.agenda.repository.AgendamentoRepository;
import com.br.agenda.repository.ClienteRepository;
import com.br.agenda.repository.ProcedimentoRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ProcedimentoRepository procedimentoRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, ClienteRepository clienteRepository, ProcedimentoRepository procedimentoRepository){
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.procedimentoRepository = procedimentoRepository;
    }

    public List<Agendamento> findAll(){
        return agendamentoRepository.findAll();
    }

    public List<Agendamento> findAllByOrderByDataHoraAsc(){
        return agendamentoRepository.findAllByOrderByDataHoraAsc();
    }

    public List<Agendamento> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentoRepository.findByDataHoraBetween(inicio, fim);
    }
    public Agendamento criar(CriarAgendamentoRequest request){
        StringBuilder builder = new StringBuilder();
        if(request.cliente() == null){
            builder.append("Favor, informar o nome do cliente.").append("\n");
        }
        if(request.procedimento() == null){
            builder.append("Favor, informar o procedimento.").append("\n");
        }
        if(Strings.isBlank(String.valueOf(request.dataHora()))){
            builder.append("Favor, informar a data e o horário do agendamento.").append("\n");
        }
        var agendamento = new Agendamento();
        var cliente = clienteRepository.findById(request.cliente()).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        var procedimento = procedimentoRepository.findById(request.procedimento()).orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        agendamento.setCliente(cliente);
        agendamento.setProcedimento(procedimento);
        agendamento.setDataHora(request.dataHora());
        return agendamentoRepository.save(agendamento);
    }

    public Optional<Agendamento> obterPeloId(Long id){
        return  agendamentoRepository.findById(id);
    }

    public Agendamento editar(EditarAgendamentoRequest request){
        StringBuilder builder = new StringBuilder();
        if(request.cliente() == null){
            builder.append("Favor, informar o nome do cliente.").append("\n");
        }
        if(request.procedimento() == null){
            builder.append("Favor, informar o procedimento.").append("\n");
        }
        if(Strings.isBlank(String.valueOf(request.dataHora()))){
            builder.append("Favor, informar a data e o horário do agendamento.").append("\n");
        }
        var old = agendamentoRepository.findById(request.id()).orElseThrow();
        var cliente = clienteRepository.findById(request.cliente()).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        var procedimento = procedimentoRepository.findById(request.procedimento()).orElseThrow(() -> new RuntimeException("Procedimento não encontrado"));
        old.setCliente(cliente);
        old.setProcedimento(procedimento);
        old.setDataHora(request.dataHora());
        return agendamentoRepository.save(old);
    }

    public void deletarPeloId(Long id){
        agendamentoRepository.deleteById(id);
    }

  }
