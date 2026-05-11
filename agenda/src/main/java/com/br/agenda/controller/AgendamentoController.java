package com.br.agenda.controller;

import com.br.agenda.dto.CriarAgendamentoRequest;
import com.br.agenda.dto.EditarAgendamentoRequest;
import com.br.agenda.repository.AgendamentoRepository;
import com.br.agenda.repository.ClienteRepository;
import com.br.agenda.repository.ProcedimentoRepository;
import com.br.agenda.service.AgendamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Controller
@RequestMapping("/agenda")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final AgendamentoRepository agendamentoRepository;
    private final ClienteRepository clienteRepository;
    private final ProcedimentoRepository procedimentoRepository;


    public AgendamentoController(AgendamentoService agendamentoService, AgendamentoRepository agendamentoRepository, ClienteRepository clienteRepository, ProcedimentoRepository procedimentoRepository) {
        this.agendamentoService = agendamentoService;
        this.agendamentoRepository = agendamentoRepository;
        this.clienteRepository = clienteRepository;
        this.procedimentoRepository = procedimentoRepository;
    }

    @GetMapping
    public ModelAndView index(){
        var mv = new ModelAndView("/agenda/listagem_de_agendamentos");
        var lista = agendamentoService.findAllByOrderByDataHoraAsc();
        mv.addObject("agendamentos", lista);
        return mv;
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable("id") Long id) {
        agendamentoService.deletarPeloId(id);
        return "redirect:/agenda";
    }

    @GetMapping("/{id:[0-9]+}")
    public ModelAndView vizualizar(@PathVariable("id") Long id) {
        var optional = agendamentoService.obterPeloId(id);
        if(optional.isEmpty()){
            return new ModelAndView("agenda/nao_ha_agendamentos");
        }
        var mv = new ModelAndView("agenda/visualizar_agendamento");
        mv.addObject("agendamento", optional.get());
        return mv;
    }

    @GetMapping("/criar")
    public ModelAndView criar() {
        var mv = new ModelAndView("agenda/criaragendamento");
        mv.addObject("clientes", clienteRepository.findAll());
        mv.addObject("procedimentos", procedimentoRepository.findAll());
        mv.addObject("criarAgendamentoRequest", new CriarAgendamentoRequest(null, null,null, null));
        return mv;
    }

    @PostMapping("/criar")
    public ModelAndView criar(@ModelAttribute  CriarAgendamentoRequest request) {
        ModelAndView mv = new ModelAndView();
        try {
            var agendamento = agendamentoService.criar(request);
            return new ModelAndView("redirect:/agenda/"+agendamento.getId());
        } catch (Exception e) {
            mv = new ModelAndView("agenda/novo_agendamento");
            mv.addObject("criarAgendamentoRequest", request);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        var optional = agendamentoService.obterPeloId(id);
        if(optional.isEmpty()){
            return new ModelAndView("agenda/nao_ha_agendamentos");
        }
        var mv = new ModelAndView("agenda/editar_agendamento");
        var agendamento = optional.get();
        var request = new EditarAgendamentoRequest(agendamento.getId(), agendamento.getCliente().getId(), agendamento.getProcedimento().getId(), agendamento.getDataHora());
        mv.addObject("clientes", clienteRepository.findAll());
        mv.addObject("procedimentos", procedimentoRepository.findAll());
        mv.addObject("editarAgendamentoRequest", optional.get());
        return mv;
    }
    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute EditarAgendamentoRequest request) {
        ModelAndView mv;
        try {
            var agendamento = agendamentoService.editar(request);
            return new ModelAndView("redirect:/agenda/"+agendamento.getId());
        } catch (Exception e) {
            mv = new ModelAndView("agenda/editar_agendamento");
            mv.addObject("editarAgendamentoRequest", request);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/calendario_mensal")
    public ModelAndView calendario(
            @RequestParam(name = "mes", required = false) Integer mes,
            @RequestParam(name = "ano", required = false) Integer ano)  {

        var hoje = LocalDate.now();
        if (mes == null) mes = hoje.getMonthValue();
        if (ano == null) ano = hoje.getYear();

        var primeiroDia = LocalDate.of(ano, mes, 1);
        var ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());

        var agendamentos = agendamentoService.findAllByOrderByDataHoraAsc();

        ModelAndView mv = new ModelAndView("agenda/calendario_mensal");
        mv.addObject("mes", mes);
        mv.addObject("ano", ano);
        mv.addObject("primeiroDia", primeiroDia);
        mv.addObject("ultimoDia", ultimoDia);
        mv.addObject("agendamentos", agendamentos);
        return mv;
    }
    }

