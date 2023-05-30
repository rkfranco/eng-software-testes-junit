package testes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GerenciadoraClientesTestes.class,
        GerenciadoraContasTestes.class
})
public class mainTest {}
