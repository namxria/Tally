package com.br.agenda.controller;

import com.br.agenda.dto.CriarClienteRequest;
import com.br.agenda.dto.EditarClienteRequest;
import com.br.agenda.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ModelAndView index(){
        var mv = new ModelAndView("/cliente/listagem_de_clientes");
        var lista = clienteService.findAll();
        mv.addObject("clientes", lista);
        return mv;
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable("id") Long id) {
        clienteService.deletarPeloId(id);
        return "redirect:/cliente";
    }

    @GetMapping("/{id}")
    public ModelAndView vizualizar(@PathVariable("id") Long id) {
        var optional = clienteService.obterPeloId(id);
        var mv = new ModelAndView("cliente/visualizar_cliente");
        mv.addObject("cliente", optional.get());
        return mv;
    }

    @GetMapping("/criar")
    public ModelAndView criar() {
        var mv = new ModelAndView("cliente/cadastrocliente");
        mv.addObject("criarClienteRequest", new CriarClienteRequest(null, null, null));
        return mv;
    }

    @PostMapping("/criar")
    public ModelAndView criar(@ModelAttribute  CriarClienteRequest request) {
        ModelAndView mv;
        try {
            var cliente = clienteService.criar(request);
            return new ModelAndView("redirect:/cliente/"+cliente.getId());
        } catch (Exception e) {
            mv = new ModelAndView("cliente/cadastrocliente");
            mv.addObject("criarClienteRequest", request);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable("id") Long id) {
        var optional = clienteService.obterPeloId(id);
        var mv = new ModelAndView("cliente/editar_cliente");
        var cliente = optional.get();
        var request = new EditarClienteRequest(cliente.getId(), cliente.getNome(),cliente.getTelefone());
        mv.addObject("editarClienteRequest", optional.get());
        return mv;
    }
    @PostMapping("/editar")
    public ModelAndView editar(@ModelAttribute EditarClienteRequest request) {
        ModelAndView mv;
        try {
            var cliente = clienteService.editar(request);
            return new ModelAndView("redirect:/cliente/"+cliente.getId());
        } catch (Exception e) {
            mv = new ModelAndView("cliente/editar_cliente");
            mv.addObject("editarClienteRequest", request);
            mv.addObject("erro", e.getMessage());
            return mv;
        }
    }
}
