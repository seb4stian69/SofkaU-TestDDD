package co.com.sofkau.entrenamento.curso;

import co.com.sofka.business.generic.UseCaseHandler;
import co.com.sofka.business.repository.DomainEventRepository;
import co.com.sofka.business.support.RequestCommand;
import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofkau.entrenamiento.curso.commands.AgregarDirectrizMentoria;
import co.com.sofkau.entrenamiento.curso.events.CursoCreado;
import co.com.sofkau.entrenamiento.curso.events.DirectrizAgregadaAMentoria;
import co.com.sofkau.entrenamiento.curso.events.MentoriaCreada;
import co.com.sofkau.entrenamiento.curso.values.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgregarDirectrizMentoriaUseCaseTest {

    @InjectMocks
    private AgregarDirectrizMentoriaUseCase useCase;
    @Mock
    private DomainEventRepository repository;

    @Test
    void agregarDirectrizAMentoria() {

        // Preparar
        CursoId cursoId = CursoId.of("cursoID"); // Creamos los atributos de AgregarDirectrizMentoria
        MentoriaId mentoriaId = MentoriaId.of("MentoriaID"); // Creamos los atributos de AgregarDirectrizMentoria
        Directiz directriz = new Directiz("New Directriz"); // Creamos los atributos de AgregarDirectrizMentoria

        var command = new AgregarDirectrizMentoria(cursoId,mentoriaId,directriz); // Instanciamos AgregarDirectrizMentoria

        when(repository.getEventsBy("cursoID"))
                .thenReturn(ListaEventos()
                );

        useCase.addRepository(repository); // Añadimos el repositorio a nuestro mock (useCase)

        // Actuar
        var event = UseCaseHandler.getInstance()
                .setIdentifyExecutor(command.getMentoriaId().value())
                .syncExecutor(useCase, new RequestCommand<>(command))
                .orElseThrow()
                .getDomainEvents();

        // Confirmar
        var events = (DirectrizAgregadaAMentoria)event.get(0);
        Assertions.assertEquals("MentoriaID", events.getMentoriaId().value());
        Assertions.assertEquals("New Directriz", events.getDirectiz().value());

    }

    private List<DomainEvent> ListaEventos() {

        Nombre nombre = new Nombre("Nombre de curso nuevo");
        Descripcion descripcion = new Descripcion("Descripcion de curso nuevo");

        Nombre nombreMentoria = new Nombre("Mentoria en history");
        Fecha fechaMentoria = new Fecha(LocalDateTime.now(), LocalDate.now());
        MentoriaId mentoriaId = MentoriaId.of("MentoriaID");

        var event_one = new CursoCreado(
                nombre,
                descripcion
        );

        var event_two = new MentoriaCreada(
                mentoriaId,
                nombreMentoria,
                fechaMentoria
        );

        return List.of(event_one,event_two);

    }

}

