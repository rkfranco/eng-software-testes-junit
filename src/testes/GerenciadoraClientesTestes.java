package testes;

import negocio.Cliente;
import negocio.GerenciadoraClientes;
import negocio.IdadeNaoPermitidaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GerenciadoraClientesTestes {

    private static GerenciadoraClientes gerClientes;
    private static List<Cliente> listaClientes;

    @Before
    public void inicializaSistemaBancario() {
        List<Cliente> clientesDoBanco = new ArrayList<>();

        Cliente cliente01 = new Cliente(1, "Gustavo Farias", 31, "gugafarias@gmail.com", 1, true);
        Cliente cliente02 = new Cliente(2, "Felipe Augusto", 34, "felipeaugusto@gmail.com", 2, true);

        clientesDoBanco.add(cliente01);
        clientesDoBanco.add(cliente02);

        gerClientes = new GerenciadoraClientes(clientesDoBanco);
        listaClientes = List.copyOf(clientesDoBanco);
    }

    @After
    public void finalizaSistemaBancario() {
        gerClientes = null;
        listaClientes = null;
    }

    @Test
    public void testGetClientesDoBanco() {
        assertNotNull(gerClientes.getClientesDoBanco());
        List<Cliente> lista = gerClientes.getClientesDoBanco();
        for (int i = 0; i < listaClientes.size(); i++)
            assertEquals(listaClientes.get(i), lista.get(i));
    }

    @Test
    public void testPesquisaCliente() {
        for (int i = 0; i <= 1; i++) {
            assertSame(listaClientes.get(i), gerClientes.pesquisaCliente(i + 1));
        }
    }

    @Test
    public void testAdicionaCliente() {
        Cliente cliente1 = new Cliente(3, "Teste da Silva", 22, "teste_silva@gmail.com", 3, true);
        Cliente cliente2 = new Cliente(4, "Teste de Oliveira", 43, "teste_olive@gmail.com", 4, true);
        gerClientes.adicionaCliente(cliente1);
        gerClientes.adicionaCliente(cliente2);
        assertSame(cliente1, gerClientes.pesquisaCliente(3));
        assertSame(cliente2, gerClientes.pesquisaCliente(4));

        gerClientes.adicionaCliente(null);
        for (Cliente cliente : gerClientes.getClientesDoBanco())
            assertNotNull(cliente);

    }

    @Test
    public void testRemoverCliente() {
        Cliente cliente1 = new Cliente(3, "Teste da Silva", 22, "teste_silva@gmail.com", 3, true);
        Cliente cliente2 = new Cliente(4, "Teste de Oliveira", 43, "teste_olive@gmail.com", 4, true);
        gerClientes.adicionaCliente(cliente1);
        gerClientes.adicionaCliente(cliente2);

        boolean extraRegisters = true;

        // Aceita mais de um registro com o mesmo ID
        gerClientes.adicionaCliente(cliente1);
        gerClientes.adicionaCliente(cliente2);

        while (extraRegisters) {
            gerClientes.removeCliente(3);
            gerClientes.removeCliente(4);

            if (gerClientes.pesquisaCliente(3) == null && gerClientes.pesquisaCliente(4) == null) {
                extraRegisters = false;
            }
        }
        assertNull(gerClientes.pesquisaCliente(3));
        assertNull(gerClientes.pesquisaCliente(4));
    }

    @Test
    public void testClienteAtivo() {
        Cliente cliente1 = new Cliente(3, "Teste da Silva", 22, "teste_silva@gmail.com", 3, true);
        Cliente cliente2 = new Cliente(4, "Teste de Oliveira", 43, "teste_olive@gmail.com", 4, true);
        gerClientes.adicionaCliente(cliente1);
        gerClientes.adicionaCliente(cliente2);

        for (int i = 1; i <= 4; i++) {
            assertTrue(gerClientes.clienteAtivo(i));
            gerClientes.pesquisaCliente(i).setAtivo(false);
        }

        for (int i = 1; i <= 4; i++) {
            assertFalse(gerClientes.clienteAtivo(i));
            gerClientes.pesquisaCliente(i).setAtivo(true);
        }
    }

    @Test
    public void testLimpa() {
        Cliente cliente1 = new Cliente(3, "Teste da Silva", 22, "teste_silva@gmail.com", 3, true);
        Cliente cliente2 = new Cliente(4, "Teste de Oliveira", 43, "teste_olive@gmail.com", 4, true);
        gerClientes.adicionaCliente(cliente1);
        gerClientes.adicionaCliente(cliente2);

        gerClientes.limpa();
        assertEquals(0, gerClientes.getClientesDoBanco().size());
    }

    @Test
    public void testValidaIdade() throws IdadeNaoPermitidaException {
        for (int i = 1; i < 100; i++) {
            if (i < 18 || i > 65) {
                try {
                    assertEquals(false, gerClientes.validaIdade(i));
                    fail("Permitiu valor invalido para idade");
                } catch (IdadeNaoPermitidaException e) {
                    assertEquals(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA, e.getMessage());
                }
            } else {
                assertEquals(true, gerClientes.validaIdade(i));
            }
        }
    }
}
