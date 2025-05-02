package br.edu.cs.poo.ac.seguro.testes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;

public class TesteSeguradoEmpresaDAO extends TesteDAO {
    private SeguradoEmpresaDAO dao = new SeguradoEmpresaDAO();

    @Override
    protected Class<? extends Serializable> getClasse() {
        return SeguradoEmpresa.class;
    }

    @Test
    public void teste01() {
        String cnpj = "00000000000000";
        cadastro.incluir(new SeguradoEmpresa("TESTE1", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1000.0, false), cnpj);
        SeguradoEmpresa seg = dao.buscar(cnpj);
        Assertions.assertNotNull(seg);
    }
    @Test
    public void teste02() {
        String cnpj = "10000000000000";
        cadastro.incluir(new SeguradoEmpresa("TESTE2", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1001.0, false), cnpj);
        SeguradoEmpresa seg = dao.buscar("11000000000000");
        Assertions.assertNull(seg);
    }
    @Test
    public void teste03() {
        String cnpj = "22000000000000";
        cadastro.incluir(new SeguradoEmpresa("TESTE3", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1002.0, false), cnpj);
        boolean ret = dao.excluir(cnpj);
        Assertions.assertTrue(ret);
        Assertions.assertNull(dao.buscar(cnpj));
    }
    @Test
    public void teste04() {
        String cnpj = "33000000000000";
        cadastro.incluir(new SeguradoEmpresa("TESTE4", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1003.0, false), cnpj);
        boolean ret = dao.excluir("33100000000000");
        Assertions.assertFalse(ret);
        Assertions.assertNotNull(dao.buscar(cnpj));
    }
    @Test
    public void teste05() {
        String cnpj = "44000000000000";
        boolean ret = dao.incluir(new SeguradoEmpresa("TESTE5", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1004.0, false));
        Assertions.assertTrue(ret);
        SeguradoEmpresa seg = dao.buscar(cnpj);
        Assertions.assertNotNull(seg);
        Assertions.assertEquals("TESTE5", seg.getNome());
    }

    @Test
    public void teste06() {
        String cnpj = "55000000000000";
        SeguradoEmpresa seg = new SeguradoEmpresa("TESTE6", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1005.0, false);
        cadastro.incluir(seg, cnpj);
        boolean ret = dao.incluir(seg);
        Assertions.assertFalse(ret);
    }
    @Test
    public void teste07() {
        String cnpj = "66000000000000";
        boolean ret = dao.alterar(new SeguradoEmpresa("TESTE7", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1006.0, false));
        Assertions.assertFalse(ret);
        SeguradoEmpresa seg = dao.buscar(cnpj);
        Assertions.assertNull(seg);
    }

    @Test
    public void teste08() {
        String cnpj = "77000000000000";
        SeguradoEmpresa segOriginal = new SeguradoEmpresa("TESTE8", null, LocalDate.now(), BigDecimal.ZERO,
                cnpj, 1007.0, false);
        cadastro.incluir(segOriginal, cnpj);

        SeguradoEmpresa segAlterado = new SeguradoEmpresa("TESTE9-ALTERADO", null, LocalDate.now().minusDays(1), BigDecimal.TEN,
                cnpj, 1008.0, true);
        boolean ret = dao.alterar(segAlterado);
        Assertions.assertTrue(ret);

        SeguradoEmpresa segBuscado = dao.buscar(cnpj);
        Assertions.assertNotNull(segBuscado);
        Assertions.assertEquals("TESTE9-ALTERADO", segBuscado.getNome());
        Assertions.assertEquals(1008.0, segBuscado.getFaturamento());
        Assertions.assertTrue(segBuscado.isEhLocadoraDeVeiculos());
    }
}
