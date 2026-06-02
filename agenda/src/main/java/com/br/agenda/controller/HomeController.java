package com.br.agenda.controller;

import com.br.agenda.service.AgendamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final AgendamentoService agendamentoService;

    public HomeController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public ModelAndView home(
            @RequestParam(name = "semanaOffset", required = false, defaultValue = "0") int semanaOffset) {

        LocalDate hoje = LocalDate.now();

        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY).plusWeeks(semanaOffset);
        LocalDate fimSemana    = inicioSemana.plusDays(6);

        List<LocalDate> diasDaSemana = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            diasDaSemana.add(inicioSemana.plusDays(i));
        }

        List<String> nomesDias = List.of(
                "SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO", "DOMINGO"
        );

        LocalDateTime inicio = inicioSemana.atStartOfDay();
        LocalDateTime fim    = fimSemana.atTime(23, 59, 59);

        var agendamentos = agendamentoService.findByDataHoraBetween(inicio, fim);

        var mv = new ModelAndView("index");
        mv.addObject("agendamentos",   agendamentos);
        mv.addObject("diasDaSemana",   diasDaSemana);
        mv.addObject("nomesDias",      nomesDias);
        mv.addObject("inicioSemana",   inicioSemana);
        mv.addObject("fimSemana",      fimSemana);
        mv.addObject("semanaOffset",   semanaOffset);
        mv.addObject("hoje",           hoje);
        return mv;
    }
}