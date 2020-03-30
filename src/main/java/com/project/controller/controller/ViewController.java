package com.project.controller.controller;


import com.project.service.FormLoginErrorMessageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


@Controller
@AllArgsConstructor
public class ViewController {

    FormLoginErrorMessageService messageService;

    @GetMapping({"/home", "/"})
    public String getHomePage(){
        return "home";
    }

    @GetMapping("/page/{id}")
    public ModelAndView getPage(@PathVariable("id") long id, ModelAndView modelAndView) {
        modelAndView.addObject("book", id);
        modelAndView.setViewName("page");
        return modelAndView;
    }

    @GetMapping("/translate")
    public String getTranslatePage() {
        return "translate";
    }

    @GetMapping("/shoppingcart")
    public String getShoppingCartPage() {
        return "shoppingcartpage";
    }
}
