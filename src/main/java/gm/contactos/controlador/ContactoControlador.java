package gm.contactos.controlador;

import gm.contactos.modelo.Contacto;
import gm.contactos.servicio.ContactoServicio;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ContactoControlador {

    private static final Logger logger = LoggerFactory.getLogger(ContactoControlador.class);

    // Inyectamos el servicio (el repositorio ya está inyectado en el servicio)
    @Autowired
    ContactoServicio contactoServicio;

    // GetMapping es la notación que se utiliza para peticiones tipo GET:
    @GetMapping("/")
    public String iniciar(ModelMap modelo){
        List<Contacto> contactos = contactoServicio.listarContactos();
        contactos.forEach((contacto) -> logger.info(contacto.toString()));
        modelo.put("contactos", contactos);
        return "index"; // Se procesa automáticamente index.html
    }

    @GetMapping("/agregar")
    public String mostrarAgregar(){
        return "agregar";
    }

    @PostMapping("/agregar")
    public String agregar(@ModelAttribute("contactoForma") Contacto contacto){
        logger.info("Contacto a agregar: " + contacto);
        contactoServicio.guardarContacto(contacto);
        return "redirect:/";
    }

    // Hacemos el request del id para poder mapear el contacto:
    @GetMapping("/editar/{id}")
    public String mostrarEditar(@PathVariable(value = "id") int id, ModelMap modelo){
        Contacto contacto = contactoServicio.buscarContactoPorId(id);
        logger.info("Contacto a editar: " + contacto);
        modelo.put("contacto", contacto);
        return "editar";
    }

    // En este caso, el objeto es "contacto" y está creado a través de un th:object y no de modelAttribute
    // porque el objeto ya existe, y se aplicó al formulario de editar cuando se asignó con mostrarEditar()
    @PostMapping("/editar")
    public String editar(@ModelAttribute("contacto") Contacto contacto){
        logger.info("Contacto a guardar: " + contacto);
        contactoServicio.guardarContacto(contacto);
        return "redirect:/";
    }

    // Añadido por mí para practicar algo nuevo:
    @GetMapping("/confirmar-eliminar/{id}")
    public String confirmarEliminar(@PathVariable(value = "id") int id, ModelMap modelo){
        Contacto contacto = contactoServicio.buscarContactoPorId(id);
        modelo.put("contacto", contacto);
        return "confirmar-eliminar";
    }

    // Versión refinada por mí con respecto al sistema que hice con JSP
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") int id){
        Contacto contacto = contactoServicio.buscarContactoPorId(id);
        contactoServicio.eliminarContacto(contacto);
        return "redirect:/";
    }
}
