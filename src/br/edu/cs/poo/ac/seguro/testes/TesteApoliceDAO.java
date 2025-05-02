package br.edu.cs.poo.ac.seguro.testes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class TesteApoliceDAO extends TesteDAO {
    private ApoliceDAO dao = new ApoliceDAO();

    @Override
    protected Class<? extends Serializable> getClasse() {
        return Apolice.class;
    }

    private Apolice criarApoliceTeste(String numero, String placaVeiculo) {
        Veiculo veiculo = new Veiculo(placaVeiculo, 2023, null, null, CategoriaVeiculo.INTERMEDIARIO);
        Apolice ap = new Apolice(
                numero,
                veiculo,
                new BigDecimal("1500.00").setScale(2, RoundingMode.HALF_UP),
                new BigDecimal("3000.00").setScale(2, RoundingMode.HALF_UP),
                new BigDecimal("80000.00").setScale(2, RoundingMode.HALF_UP),
                LocalDate.now()
        );
        return ap;
    }

    @Test
    public void teste01_buscarExistente() {
        String numero = "AP2025001";
        Apolice ap = criarApoliceTeste(numero, "TST0001");
        cadastro.incluir(ap, numero);
        Apolice apBuscada = dao.buscar(numero);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(numero, apBuscada.getNumero());
        Assertions.assertEquals(new BigDecimal("1500.00"), apBuscada.getValorFranquia());
    }
    @Test
    public void teste02_buscarInexistente() {
        String numeroExistente = "AP2025002";
        String numeroInexistente = "AP2025999";
        Apolice ap = criarApoliceTeste(numeroExistente, "TST0002");
        cadastro.incluir(ap, numeroExistente); // Configuração
        Apolice apBuscada = dao.buscar(numeroInexistente); // Ação
        Assertions.assertNull(apBuscada); // Verificação
    }
    @Test
    public void teste03_excluirExistente() {
        String numero = "AP2025003";
        Apolice ap = criarApoliceTeste(numero, "TST0003");
        cadastro.incluir(ap, numero); // Configuração
        boolean ret = dao.excluir(numero); // Ação
        Assertions.assertTrue(ret); // Verificação
        Assertions.assertNull(dao.buscar(numero)); // Confirma exclusão
    }
    @Test
    public void teste04_excluirInexistente() {
        String numeroExistente = "AP2025004";
        String numeroInexistente = "AP2025998";
        Apolice ap = criarApoliceTeste(numeroExistente, "TST0004");
        cadastro.incluir(ap, numeroExistente); // Configuração
        boolean ret = dao.excluir(numeroInexistente); // Ação
        Assertions.assertFalse(ret); // Verificação
        Assertions.assertNotNull(dao.buscar(numeroExistente)); // Confirma que não excluiu o existente
    }
    @Test
    public void teste05_incluirNovo() {
        String numero = "AP2025005";
        Apolice ap = criarApoliceTeste(numero, "TST0005");
        boolean ret = dao.incluir(ap); // Ação
        Assertions.assertTrue(ret); // Verificação
        Apolice apBuscada = dao.buscar(numero);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(ap, apBuscada));
    }

    @Test
    public void teste06_incluirExistente() {
        String numero = "AP2025006";
        Apolice ap = criarApoliceTeste(numero, "TST0006");
        cadastro.incluir(ap, numero); // Configuração
        boolean ret = dao.incluir(ap); // Ação: tenta incluir de novo
        Assertions.assertFalse(ret); // Verificação: não deve incluir
    }
    @Test
    public void teste07_alterarInexistente() {
        String numero = "AP2025007";
        Apolice ap = criarApoliceTeste(numero, "TST0007");
        boolean ret = dao.alterar(ap); // Ação: tenta alterar sem existir
        Assertions.assertFalse(ret); // Verificação
        Apolice apBuscada = dao.buscar(numero);
        Assertions.assertNull(apBuscada); // Confirma que não criou
    }

    @Test
    public void teste08_alterarExistente() {
        String numero = "AP2025008";
        Apolice apOriginal = criarApoliceTeste(numero, "TST0008");
        cadastro.incluir(apOriginal, numero); // Configuração

        Apolice apAlterada = criarApoliceTeste(numero, "TST0008"); // Mesmo veículo neste caso
        apAlterada.setValorPremio(new BigDecimal("3500.00").setScale(2, RoundingMode.HALF_UP)); // Valor do prêmio diferente
        apAlterada.setDataInicioVigencia(LocalDate.now().minusDays(1)); // Data diferente

        boolean ret = dao.alterar(apAlterada); // Ação
        Assertions.assertTrue(ret); // Verificação

        Apolice apBuscada = dao.buscar(numero);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apAlterada, apBuscada));
        Assertions.assertEquals(new BigDecimal("3500.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(LocalDate.now().minusDays(1), apBuscada.getDataInicioVigencia());
    }

    @Test
    public void teste09_incluirAlterarExcluirSemNumero() {
        Apolice ap = criarApoliceTeste(null, "TST0009");
        Assertions.assertFalse(dao.incluir(ap));
        ap = criarApoliceTeste("   ", "TST0009");
        Assertions.assertFalse(dao.incluir(ap));

        ap = criarApoliceTeste(null, "TST0009");
        Assertions.assertFalse(dao.alterar(ap));
        ap = criarApoliceTeste("   ", "TST0009");
        Assertions.assertFalse(dao.alterar(ap));

        Assertions.assertFalse(dao.excluir(null));
        Assertions.assertFalse(dao.excluir("   "));
    }
}
