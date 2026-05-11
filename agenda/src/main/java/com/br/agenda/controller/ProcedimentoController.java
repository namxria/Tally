package com.br.agenda.controller;

import com.br.agenda.dto.CriarProcedimentoRequest;
import com.br.agenda.dto.EditarProcedimentoRequest;
import com.br.agenda.service.ProcedimentoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/procedimento")
public class ProcedimentoController {

    private final ProcedimentoService procedimentoService;

    public ProcedimentoController(ProcedimentoService procedimentoService) {
        this.procedimentoService = procedimentoService;
    }

    @GetMapping
    public ModelAndView index(){
        var mv = new ModelAndView("/procedimento/listagem_de_procedimentos");
        var lista = procedimentoService.findAll();
        mv.addObject("procedimentos", lista);
        return mv;
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable("id") Long id) {
        procedimentoService.deletarPeloId(id);
        return "redirect:/procedimento";
    }

    @GetMapping("/{id}")
    public ModelAndView vizualizar(@PathVariable("id") Long id) {
        var optional = procedimentoService.obterPeloId(id);
        if(optional.isEmpty()){
            return new ModelAndView("procedimento/nao_ha_procedimentos");
        }
        var mv = new ModelAndView("procedimento/visualizar_procedimento");
        mv.addObject("procedimento", optional.get());
        return mv;
    }

    @GetMapping("/criar")
    public ModelAndView criar() {
        var mv = new ModelAndView("procedimento/cadastroprocedimento");
        mv.addObject("criarProcedimentoRequest", new CriarProcedimentoRequest(null, null, null));
        return mv;
    }

    @PostMapping("/criar")
    public ModelAndView criar(@ModelAttribute  CriarProcedimentoRequest request) {
        ModelAndView mv;
        try {
            var procedimento = procedimentoService.criar(request);
            return new ModelAndView("redirect:/procedimento/"+procedimento.getId());
        } catch (Exception e) {
            mv = new ModelAndView("procedimento/novo_procedimento");
            mv.addObject("criarProcedimentoRequest", request);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        var optional = procedimentoService.obterPeloId(id);
        if(optional.isEmpty()){
            return new ModelAndView("procedimento/nao_ha_procedimentos");
        }
        var mv = new ModelAndView("procedimento/editar_procedimento");
        var procedimento = optional.get();
        var request = new EditarProcedimentoRequest(procedimento.getId(), procedimento.getNome(),procedimento.getValor());
        mv.addObject("editarProcedimentoRequest", optional.get());
        return mv;
    }
    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute EditarProcedimentoRequest request) {
        ModelAndView mv;
        try {
            var procedimento = procedimentoService.editar(request);
            return new ModelAndView("redirect:/procedimento/"+procedimento.getId());
        } catch (Exception e) {
            mv = new ModelAndView("procedimento/editar_procedimento");
            mv.addObject("editarProcedimentoRequest", request);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }
}
