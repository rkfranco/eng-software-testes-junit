package testes;
/*
    contaAtiva()
 */

import negocio.Cliente;
import negocio.ContaCorrente;
import negocio.GerenciadoraContas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GerenciadoraContasTestes {

    private static GerenciadoraContas gerContas;

    private static List<ContaCorrente> listaContas;

    @Before
    public void inicializaSistemaBancario() {
        ArrayList<ContaCorrente> contasDoBanco = new ArrayList<>();

        ContaCorrente conta01 = new ContaCorrente(1, 0, true);
        ContaCorrente conta02 = new ContaCorrente(2, 0, true);
        contasDoBanco.add(conta01);
        contasDoBanco.add(conta02);

        gerContas = new GerenciadoraContas(contasDoBanco);
        listaContas = List.copyOf(contasDoBanco);
    }

    @After
    public void finalizaSistemaBancario() {
        gerContas = null;
        listaContas = null;
    }

    private static void limpaTodasContas() {
        gerContas.getContasDoBanco().clear();
    }

    @Test
    public void testGetContasDoBanco() {
        List<ContaCorrente> lista = gerContas.getContasDoBanco();
        assertNotNull(lista);
        for (int i = 0; i < lista.size(); i++)
            assertEquals(listaContas.get(i).toString(), lista.get(i).toString());
    }

    @Test
    public void testPesquisaConta() {
        for (int i = 0; i < gerContas.getContasDoBanco().size(); i++) {
            ContaCorrente conta = gerContas.pesquisaConta(i + 1);
            assertNotNull(conta);
            assertEquals(listaContas.get(i).toString(), conta.toString());
        }
    }

    @Test
    public void testAdicionaConta() {
        ArrayList<ContaCorrente> list = inserirRegistros();
        assertNotNull(gerContas.getContasDoBanco());
        assertEquals(100, gerContas.getContasDoBanco().size());
        for (int i = 0; i < gerContas.getContasDoBanco().size(); i++)
            assertEquals(list.get(i).toString(), gerContas.pesquisaConta(i).toString());

        gerContas.adicionaConta(null);
        for (ContaCorrente conta : gerContas.getContasDoBanco())
            assertNotNull(conta);
    }

    private ArrayList<ContaCorrente> inserirRegistros() {
        limpaTodasContas();
        ArrayList<ContaCorrente> list = new ArrayList<>();

        ContaCorrente conta;
        ContaCorrente conta2;
        boolean ativa = false;
        for (int i = 0; i < 100; i++) {
            conta = new ContaCorrente(i, i * 10, ativa);
            conta2 = new ContaCorrente(i, i * 10, ativa);
            ativa = !ativa;
            gerContas.adicionaConta(conta);
            list.add(conta2);
        }
        return list;
    }

    @Test
    public void testRemoveConta() {
        ArrayList<ContaCorrente> list = inserirRegistros();
        assertNotNull(gerContas.getContasDoBanco());
        for (int i = 0; i < list.size(); i++) {
            assertTrue(gerContas.removeConta(i));
            // Esse assert está dando erro poís há um defeito no método "removeConta"
            // O método independente de encontrar ou não um objeto com o id passado
            // returna o valor "false"
            assertEquals(99 - i, gerContas.getContasDoBanco().size());
        }
        assertEquals(0, gerContas.getContasDoBanco().size());
    }

    @Test
    public void testContaAtiva() {
        inserirRegistros();
        int ativa = 0, desativa = 0;

        for (ContaCorrente conta : gerContas.getContasDoBanco()) {
            if (gerContas.contaAtiva(conta.getId())) ativa++;
            else desativa++;
        }
        assertEquals(50, ativa);
        assertEquals(50, desativa);
    }

    @Test
    public void testTransfereValor() {

        double[] valor = {5000.25, 1000.0, 500.0};
        double[] valorTransferencia = {1000.25, 2000.0, 500.0};

        for (int i = 0; i < valor.length; i++) {
            // Define um valor inicial na conta1
            ContaCorrente conta1 = gerContas.pesquisaConta(1);
            conta1.setSaldo(valor[i]);

            // Verifica se o valor está correto
            assertEquals(valor[i], gerContas.pesquisaConta(1).getSaldo(), 0);

            // Verifica se o valor inicial da conta2 e zero
            ContaCorrente conta2 = gerContas.pesquisaConta(2);
            conta2.setSaldo(0.0);
            assertEquals(0.0, conta2.getSaldo(), 0);

            // Realiza a transferencia
            boolean transfer = gerContas.transfereValor(conta1.getId(), valorTransferencia[i], conta2.getId());
            // Verifica se a transação retornou true (Sucesso na tranferencia)
            assertEquals(true, transfer);

            // Conta 1 possui saldo suficiente para realizar a transacao
            assertTrue(valor[i] > valorTransferencia[i]);

            // Averiguar se os valores são condizentes com o resultado da operação
            assertEquals(valor[i] - valorTransferencia[i], conta1.getSaldo(), 0);
            assertEquals(valorTransferencia[i], conta2.getSaldo(), 0);
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
