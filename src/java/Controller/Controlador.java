/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Config.Conexion;
import Entity.log;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class Controlador {

    Conexion conexion = new Conexion();
    JdbcTemplate jdbcTemplate = new JdbcTemplate(conexion.Conectar());
    ModelAndView mv = new ModelAndView();
    List datos;

    @RequestMapping(value = "index.htm", method = RequestMethod.GET)
    public ModelAndView Login() {
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping(value = "registro.htm", method = RequestMethod.GET)
    public ModelAndView Registrar() {
        mv.addObject(new log());
        mv.setViewName("registro");
        return mv;
    }

    @RequestMapping(value = "list.htm", method = RequestMethod.GET)
    public ModelAndView Listar() {
        String sql = "select * from log";
        datos = this.jdbcTemplate.queryForList(sql);
        mv.addObject("lista", datos);
        mv.setViewName("list");
        return mv;
    }

    @RequestMapping(value = "registro.htm", method = RequestMethod.POST)
    public ModelAndView Registrar(log l) {
        String sql = "insert into log(usuario,mail,password)values(?,?,?)";
        try {
            this.jdbcTemplate.update(sql, l.getUsuario(), l.getMail(), l.getPassword());
            return new ModelAndView("redirect:/index.htm");
        } catch (DataAccessException e) {
            e.getMessage();
        }
        return mv;
    }

    @RequestMapping(value = "index.htm", method = RequestMethod.POST)
    public ModelAndView Login(log l) {
        String sql = "select usuario from log where usuario = ? and password = ?";
        try {
            String usuario = this.jdbcTemplate.queryForObject(sql, new Object[]{l.getUsuario(), l.getPassword()}, String.class);

        } catch (DataAccessException e) {
            e.getMessage();
        }
        return new ModelAndView("redirect:/list.htm");
    }

}
