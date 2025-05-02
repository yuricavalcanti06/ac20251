package br.edu.cs.poo.ac.seguro.testes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;

public class TesteSinistroDAO extends TesteDAO {
    private SinistroDAO dao = new SinistroDAO();

    @Override
    protected Class<? extends Serializable> getClasse() {
        return Sinistro.class;
    }

    private Sinistro criarSinistroTeste(String numero, String placaVeiculo) {
        Veiculo veiculo = new Veiculo(placaVeiculo, 2023, null, null, CategoriaVeiculo.BASICO);
        LocalDateTime agora = LocalDateTime.now();
        Sinistro sin = new Sinistro(
                numero,
                veiculo,
                agora.minusHours(2),
                agora,
                "usuario_teste",
                new BigDecimal("5000.00").setScale(2, RoundingMode.HALF_UP),
                TipoSinistro.COLISAO
        );
        return sin;
    }

    @Test
    public void teste01_buscarExistente() {
        String numero = "SIN2025001";
        Sinistro sin = criarSinistroTeste(numero, "VCL0001");
        cadastro.incluir(sin, numero);
        Sinistro sinBuscado = dao.buscar(numero);
        Assertions.assertNotNull(sinBuscado);
        Assertions.assertEquals(numero, sinBuscado.getNumero());
        Assertions.assertEquals(TipoSinistro.COLISAO, sinBuscado.getTipo());
    }
    @Test
    public void teste02_buscarInexistente() {
        String numeroExistente = "SIN2025002";
        String numeroInexistente = "SIN2025999";
        Sinistro sin = criarSinistroTeste(numeroExistente, "VCL0002");
        cadastro.incluir(sin, numeroExistente);
        Sinistro sinBuscado = dao.buscar(numeroInexistente);
        Assertions.assertNull(sinBuscado);
    }
    @Test
    public void teste03_excluirExistente() {
        String numero = "SIN2025003";
        Sinistro sin = criarSinistroTeste(numero, "VCL0003");
        cadastro.incluir(sin, numero);
        boolean ret = dao.excluir(numero);
        Assertions.assertTrue(ret);
        Assertions.assertNull(dao.buscar(numero));
    }
    @Test
    public void teste04_excluirInexistente() {
        String numeroExistente = "SIN2025004";
        String numeroInexistente = "SIN2025998";
        Sinistro sin = criarSinistroTeste(numeroExistente, "VCL0004");
        cadastro.incluir(sin, numeroExistente);
        boolean ret = dao.excluir(numeroInexistente);
        Assertions.assertFalse(ret);
        Assertions.assertNotNull(dao.buscar(numeroExistente));
    }
    @Test
    public void teste05_incluirNovo() {
        String numero = "SIN2025005";
        Sinistro sin = criarSinistroTeste(numero, "VCL0005");
        boolean ret = dao.incluir(sin);
        Assertions.assertTrue(ret);
        Sinistro sinBuscado = dao.buscar(numero);
        Assertions.assertNotNull(sinBuscado);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(sin, sinBuscado));
    }

    @Test
    public void teste06_incluirExistente() {
        String numero = "SIN2025006";
        Sinistro sin = criarSinistroTeste(numero, "VCL0006");
        cadastro.incluir(sin, numero);
        boolean ret = dao.incluir(sin);
        Assertions.assertFalse(ret);
    }
    @Test
    public void teste07_alterarInexistente() {
        String numero = "SIN2025007";
        Sinistro sin = criarSinistroTeste(numero, "VCL0007");
        boolean ret = dao.alterar(sin);
        Assertions.assertFalse(ret);
        Sinistro sinBuscado = dao.buscar(numero);
        Assertions.assertNull(sinBuscado);
    }

    @Test
    public void teste08_alterarExistente() {
        String numero = "SIN2025008";
        Sinistro sinOriginal = criarSinistroTeste(numero, "VCL0008");
        cadastro.incluir(sinOriginal, numero);

        Sinistro sinAlterado = criarSinistroTeste(numero, "VCL0008");
        sinAlterado.setTipo(TipoSinistro.FURTO);
        sinAlterado.setValorSinistro(new BigDecimal("7500.50").setScale(2, RoundingMode.HALF_UP));
        sinAlterado.setUsuarioRegistro("admin_update");

        boolean ret = dao.alterar(sinAlterado);
        Assertions.assertTrue(ret);

        Sinistro sinBuscado = dao.buscar(numero);
        Assertions.assertNotNull(sinBuscado);
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(sinAlterado, sinBuscado));
        Assertions.assertEquals(TipoSinistro.FURTO, sinBuscado.getTipo());
        Assertions.assertEquals(new BigDecimal("7500.50"), sinBuscado.getValorSinistro());
        Assertions.assertEquals("admin_update", sinBuscado.getUsuarioRegistro());
    }

    @Test
    public void teste09_incluirAlterarExcluirSemNumero() {
        Sinistro sin = criarSinistroTeste(null, "VCL0009");
        Assertions.assertFalse(dao.incluir(sin));
        sin = criarSinistroTeste("   ", "VCL0009");
        Assertions.assertFalse(dao.incluir(sin));

        sin = criarSinistroTeste(null, "VCL0009");
        Assertions.assertFalse(dao.alterar(sin));
        sin = criarSinistroTeste("   ", "VCL0009");
        Assertions.assertFalse(dao.alterar(sin));

        Assertions.assertFalse(dao.excluir(null));
        Assertions.assertFalse(dao.excluir("   "));
    }

    @Test
    public void teste10_buscarTodos() {
        setUp();

        Sinistro sin1 = criarSinistroTeste("SIN_ALL_1", "VCL_ALL_1");
        Sinistro sin2 = criarSinistroTeste("SIN_ALL_2", "VCL_ALL_2");
        Sinistro sin3 = criarSinistroTeste("SIN_ALL_3", "VCL_ALL_3");
        dao.incluir(sin1);
        dao.incluir(sin2);
        dao.incluir(sin3);

        Sinistro[] todos = dao.buscarTodos();

        Assertions.assertEquals(3, todos.length);

        boolean achou1 = false, achou2 = false, achou3 = false;
        for (Sinistro s : todos) {
            if (s.getNumero().equals("SIN_ALL_1")) achou1 = true;
            if (s.getNumero().equals("SIN_ALL_2")) achou2 = true;
            if (s.getNumero().equals("SIN_ALL_3")) achou3 = true;
        }
        Assertions.assertTrue(achou1);
        Assertions.assertTrue(achou2);
        Assertions.assertTrue(achou3);

        setUp();
        todos = dao.buscarTodos();
        Assertions.assertEquals(0, todos.length);
    }
}
