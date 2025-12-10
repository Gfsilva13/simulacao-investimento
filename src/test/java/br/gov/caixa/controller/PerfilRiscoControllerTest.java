package br.gov.caixa.controller;

import br.gov.caixa.controller.business.PerfilRiscoController;
import br.gov.caixa.service.business.PerfilRiscoService;
import br.gov.caixa.dto.business.PerfilRiscoDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class PerfilRiscoControllerTest {

    @InjectMock
    private PerfilRiscoService perfilRiscoService;

    @Inject
    private PerfilRiscoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void testObterPerfil_IdInvalido() {
        Response response = controller.obterPerfil(-1L);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("O ID do cliente deve ser um número positivo.", response.getEntity());
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void testObterPerfil_PerfilEncontrado() {
        Long clienteId = 1L;
        PerfilRiscoDTO dto = new PerfilRiscoDTO(clienteId, "Conservador", 0, "Sem histórico, perfil padrão conservador.");
        when(perfilRiscoService.perfilRisco(clienteId)).thenReturn(Optional.of(dto));

        Response response = controller.obterPerfil(clienteId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(dto, response.getEntity());
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void testObterPerfil_PerfilNaoEncontrado() {
        Long clienteId = 2L;
        when(perfilRiscoService.perfilRisco(clienteId)).thenReturn(Optional.empty());

        Response response = controller.obterPerfil(clienteId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Perfil de risco não encontrado para o cliente ID 2", response.getEntity());
    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    void testObterPerfil_ErroInterno() {
        Long clienteId = 3L;
        when(perfilRiscoService.perfilRisco(clienteId)).thenThrow(new RuntimeException("Falha inesperada"));

        Response response = controller.obterPerfil(clienteId);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Erro ao calcular perfil de risco para o cliente. Falha inesperada", response.getEntity());
    }
}
