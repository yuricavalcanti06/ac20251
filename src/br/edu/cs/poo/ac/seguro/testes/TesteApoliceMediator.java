package br.edu.cs.poo.ac.seguro.testes;

import java.io.File;
import java.io.Serializable; // Import necessário
import java.math.BigDecimal;
import java.math.RoundingMode; // Import necessário
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.cesarschool.next.oo.persistenciaobjetos.CadastroObjetos;
import br.edu.cs.poo.ac.seguro.entidades.Apolice;
import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.entidades.Sinistro;
import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.entidades.Veiculo;
import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;

public class TesteApoliceMediator extends TesteMediator {

    private static final String NUM_SINISTRO = "SIN1234567";
    private static final String PLACA_KKK6666 = "KKK6666";
    private static final String NUM_AP = "AP123456";
    private ApoliceMediator mediator = ApoliceMediator.getInstancia();
    private CadastroObjetos cadPessoa = new CadastroObjetos(SeguradoPessoa.class);
    private CadastroObjetos cadEmpresa = new CadastroObjetos(SeguradoEmpresa.class);
    private CadastroObjetos cadVeiculo = new CadastroObjetos(Veiculo.class);
    private CadastroObjetos cadSinistro = new CadastroObjetos(Sinistro.class);

    @BeforeEach
    public void setUp() {
        super.setUp();
        String sep = File.separator;
        FileUtils.limparDiretorio("." + sep + Veiculo.class.getSimpleName());
        FileUtils.limparDiretorio("." + sep + SeguradoEmpresa.class.getSimpleName());
        FileUtils.limparDiretorio("." + sep + SeguradoPessoa.class.getSimpleName());
        FileUtils.limparDiretorio("." + sep + Sinistro.class.getSimpleName());
    }

    @Test
    public void test000_construtorRetornoInclusao() {
        try {
            new RetornoInclusaoApolice(null, null);
            Assertions.fail("Deveria lançar exceção para ambos nulos");
        } catch (RuntimeException e) {
            Assertions.assertEquals(
                    "Número da apólice e mensagem de erro não podem ser ambas nulas",
                    e.getMessage());
        }
        try {
            new RetornoInclusaoApolice("AAA", "VVV");
            Assertions.fail("Deveria lançar exceção para ambos preenchidos");
        } catch (RuntimeException e) {
            Assertions.assertEquals(
                    "Número da apólice e mensagem de erro não podem ser ambas preenchidas",
                    e.getMessage());
        }
        try {
            new RetornoInclusaoApolice(null, "DDD");
            // Sucesso esperado
        } catch (RuntimeException e) {
            Assertions.fail("Não deveria lançar exceção para mensagem de erro preenchida");
        }
        try {
            new RetornoInclusaoApolice("ZZZ", null);
            // Sucesso esperado
        } catch (RuntimeException e) {
            Assertions.fail("Não deveria lançar exceção para número da apólice preenchido");
        }
    }

    @Test
    public void test001_dadosNulos() {
        RetornoInclusaoApolice ret = mediator.incluirApolice(null);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Dados do veículo devem ser informados", ret.getMensagemErro());
    }
    @Test
    public void test002_cpfCnpjInvalido() {
        // CPF/CNPJ Nulo ou Branco
        DadosVeiculo dr = new DadosVeiculo(null, "KKK0019", 2020, new BigDecimal("60000.0"), 2);
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CPF ou CNPJ deve ser informado", ret.getMensagemErro());

        dr = new DadosVeiculo("  ", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CPF ou CNPJ deve ser informado", ret.getMensagemErro());

        // CPF inválido (dígito verificador)
        dr = new DadosVeiculo("07255431088", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CPF inválido", ret.getMensagemErro());

        // CNPJ inválido (dígito verificador)
        dr = new DadosVeiculo("11851715000171", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CNPJ inválido", ret.getMensagemErro());

        // Tamanho inválido
        dr = new DadosVeiculo("1234567890", "KKK0019", 2020, new BigDecimal("60000.0"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CPF ou CNPJ inválido", ret.getMensagemErro());
    }
    @Test
    public void test003_placaInvalida() {
        DadosVeiculo dr = new DadosVeiculo("07255431089", null, 2020, new BigDecimal("60000.0"), 2);
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Placa do veículo deve ser informada", ret.getMensagemErro());

        dr = new DadosVeiculo("07255431089", " ", 2020, new BigDecimal("60000.0"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Placa do veículo deve ser informada", ret.getMensagemErro());
    }
    @Test
    public void test004_anoInvalido() {
        // Testa apenas se o veículo NÃO existe, pois a validação completa só ocorre nesse caso
        // Se o veículo existir, o ano dos DadosVeiculo não é usado para validação completa.
        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2019, new BigDecimal("60000.0"), 2);
        // Pré-condição: Segurado Pessoa existe para passar da validação inicial
        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(), BigDecimal.ZERO, "07255431089", 0);
        cadPessoa.incluir(sp, "07255431089");
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Ano tem que estar entre 2020 e 2025, incluindo estes", ret.getMensagemErro());

        dr = new DadosVeiculo("07255431089", "KKK0019", 2026, new BigDecimal("60000.0"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Ano tem que estar entre 2020 e 2025, incluindo estes", ret.getMensagemErro());
    }
    @Test
    public void test006_valorMaximoInvalido() {
        // Valor Nulo
        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2025, null, 2);
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Valor máximo segurado deve ser informado", ret.getMensagemErro());

        // Valor acima de 100% (Ano 2025, Cat 2: 100000.00)
        dr = new DadosVeiculo("07255431089", "KKK0019", 2025, new BigDecimal("100000.01"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria", ret.getMensagemErro());

        // Valor abaixo de 75% (75% de 100000.00 = 75000.00)
        dr = new DadosVeiculo("07255431089", "KKK0019", 2025, new BigDecimal("74999.99"), 2);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria", ret.getMensagemErro());
    }
    @Test
    public void test007_categoriaInvalida() {
        // Testa apenas se o veículo NÃO existe
        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2024, new BigDecimal("60000.0"), 0); // Categoria 0
        // Pré-condição: Segurado Pessoa existe
        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(), BigDecimal.ZERO, "07255431089", 0);
        cadPessoa.incluir(sp, "07255431089");
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Categoria inválida", ret.getMensagemErro());

        dr = new DadosVeiculo("07255431089", "KKK0019", 2024, new BigDecimal("60000.0"), 6); // Categoria 6
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Categoria inválida", ret.getMensagemErro());
    }
    @Test
    public void test008_seguradoInexistente() {
        // CPF Inexistente
        DadosVeiculo dr = new DadosVeiculo("07255431089", "KKK0019", 2020, new BigDecimal("50000.0"), 1); // Cat 1, Ano 2020: 50000.00 (Valor Max = 100%)
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CPF inexistente no cadastro de pessoas", ret.getMensagemErro());

        // CNPJ Inexistente
        dr = new DadosVeiculo("11851715000174", "KKK0019", 2020, new BigDecimal("50000.0"), 1);
        ret = mediator.incluirApolice(dr);
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("CNPJ inexistente no cadastro de empresas", ret.getMensagemErro());
    }
    @Test
    public void test009_apolicePessoaExistente() {
        String cpf = "07255431089";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado, Veículo e Apolice existentes
        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(), new BigDecimal("0.0"), cpf, 20000.0);
        cadPessoa.incluir(sp, cpf);
        Veiculo vel = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(vel, placa);
        String numero = LocalDate.now().getYear() + "000" + cpf + placa;
        Apolice ap = new Apolice(numero, vel, new BigDecimal("2340.00"), new BigDecimal("1800.00"), new BigDecimal("60000.00"), LocalDate.now());
        cadastro.incluir(ap, numero); // Inclui a apólice diretamente no DAO de Apolice

        // Ação: Tenta incluir a mesma apólice via mediator
        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.0"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Apólice já existente para ano atual e veículo", ret.getMensagemErro());
    }
    @Test // Renomeado de test010 para evitar conflito
    public void test010_apoliceEmpresaExistente() {
        String cnpj = "11851715000174";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado, Veículo e Apolice existentes
        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(), new BigDecimal("0.0"), cnpj, 20000.0, false);
        cadEmpresa.incluir(se, cnpj);
        Veiculo vel = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(vel, placa);
        String numero = LocalDate.now().getYear() + cnpj + placa;
        Apolice ap = new Apolice(numero, vel, new BigDecimal("2340.00"), new BigDecimal("1800.00"), new BigDecimal("60000.00"), LocalDate.now());
        cadastro.incluir(ap, numero); // Inclui a apólice diretamente no DAO de Apolice

        // Ação: Tenta incluir a mesma apólice via mediator
        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.0"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação
        Assertions.assertNull(ret.getNumeroApolice());
        Assertions.assertEquals("Apólice já existente para ano atual e veículo", ret.getMensagemErro());
    }
    @Test
    public void test011_incluirNovoVeiculoPessoaSemBonusSemSinistro() {
        String cpf = "07255431089";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado Pessoa
        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(), new BigDecimal("0.00"), cpf, 20000.0);
        cadPessoa.incluir(sp, cpf);

        // Ação: Inclui apólice (veículo não existe)
        String numeroEsperado = LocalDate.now().getYear() + "000" + cpf + placa;
        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.00"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso, número correto
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Veículo foi criado corretamente
        Veiculo velBuscado = (Veiculo)cadVeiculo.buscar(placa);
        Assertions.assertNotNull(velBuscado);
        Assertions.assertEquals(ano, velBuscado.getAno());
        Assertions.assertEquals(CategoriaVeiculo.INTERMEDIARIO, velBuscado.getCategoria());
        Assertions.assertNotNull(velBuscado.getProprietarioPessoa());
        Assertions.assertEquals(cpf, velBuscado.getProprietarioPessoa().getCpf());
        Assertions.assertNull(velBuscado.getProprietarioEmpresa());

        // Verificação: Apólice foi criada corretamente
        // VMax = 57000.00, VPA = 1710.00, VPB = 1710.00, Bonus = 0, VPC = 1710.00, Premio = 1710.00
        // Franquia = 1.3 * VPB = 2223.00
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(numeroEsperado, apBuscada.getNumero());
        Assertions.assertEquals(new BigDecimal("2223.00"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1710.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());
        Assertions.assertEquals(LocalDate.now(), apBuscada.getDataInicioVigencia());
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velBuscado, apBuscada.getVeiculo()));

        // Verificação: Bônus do segurado foi creditado (30% do prêmio)
        // Bonus = 0.30 * 1710.00 = 513.00
        SeguradoPessoa spBuscado = (SeguradoPessoa)cadPessoa.buscar(cpf);
        Assertions.assertNotNull(spBuscado);
        Assertions.assertEquals(new BigDecimal("513.00"), spBuscado.getBonus());
    }
    @Test
    public void test012_incluirNovoVeiculoEmpresaSemBonusSemSinistro() {
        String cnpj = "11851715000174";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado Empresa (não locadora)
        SeguradoEmpresa se = new SeguradoEmpresa("ACME", null, LocalDate.now(), new BigDecimal("0.00"), cnpj, 20000.0, false);
        cadEmpresa.incluir(se, cnpj);

        // Ação: Inclui apólice (veículo não existe)
        String numeroEsperado = LocalDate.now().getYear() + cnpj + placa;
        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso, número correto
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Veículo foi criado corretamente
        Veiculo velBuscado = (Veiculo)cadVeiculo.buscar(placa);
        Assertions.assertNotNull(velBuscado);
        Assertions.assertEquals(ano, velBuscado.getAno());
        Assertions.assertEquals(CategoriaVeiculo.INTERMEDIARIO, velBuscado.getCategoria());
        Assertions.assertNotNull(velBuscado.getProprietarioEmpresa());
        Assertions.assertEquals(cnpj, velBuscado.getProprietarioEmpresa().getCnpj());
        Assertions.assertNull(velBuscado.getProprietarioPessoa());

        // Verificação: Apólice foi criada corretamente (mesmos valores do teste 11, pois não é locadora)
        // VMax = 57000.00, VPA = 1710.00, VPB = 1710.00, Bonus = 0, VPC = 1710.00, Premio = 1710.00
        // Franquia = 1.3 * VPB = 2223.00
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(numeroEsperado, apBuscada.getNumero());
        Assertions.assertEquals(new BigDecimal("2223.00"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1710.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());
        Assertions.assertEquals(LocalDate.now(), apBuscada.getDataInicioVigencia());
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velBuscado, apBuscada.getVeiculo()));

        // Verificação: Bônus do segurado foi creditado (30% do prêmio)
        // Bonus = 0.30 * 1710.00 = 513.00
        SeguradoEmpresa seBuscado = (SeguradoEmpresa)cadEmpresa.buscar(cnpj);
        Assertions.assertNotNull(seBuscado);
        Assertions.assertEquals(new BigDecimal("513.00"), seBuscado.getBonus());
    }
    @Test
    public void test013_alterarVeiculoExistentePessoaSemBonusSemSinistro() {
        String cpfAntigo = "00000000000"; // CPF do proprietário antigo
        String cpfNovo = "07255431089";   // CPF do novo proprietário
        String placa = "KKK0019";
        int ano = 2020;

        // Configuração: Cria segurados (antigo e novo) e veículo existente com segurado antigo
        SeguradoPessoa spAntigo = new SeguradoPessoa("Carlos Antigo", null, LocalDate.now(), new BigDecimal("0.00"), cpfAntigo, 20000.0);
        SeguradoPessoa spNovo = new SeguradoPessoa("Maria Nova", null, LocalDate.now(), new BigDecimal("0.00"), cpfNovo, 30000.0);
        cadPessoa.incluir(spAntigo, cpfAntigo);
        cadPessoa.incluir(spNovo, cpfNovo);
        Veiculo velExistente = new Veiculo(placa, ano, null, spAntigo, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(velExistente, placa);

        // Ação: Inclui apólice para o novo segurado (veículo já existe)
        String numeroEsperado = LocalDate.now().getYear() + "000" + cpfNovo + placa;
        DadosVeiculo dr = new DadosVeiculo(cpfNovo, placa, ano, new BigDecimal("57000.00"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso, número correto
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Veículo foi ALTERADO corretamente (proprietário mudou)
        Veiculo velAlterado = (Veiculo)cadVeiculo.buscar(placa);
        Assertions.assertNotNull(velAlterado);
        Assertions.assertEquals(ano, velAlterado.getAno()); // Ano não muda
        Assertions.assertEquals(CategoriaVeiculo.INTERMEDIARIO, velAlterado.getCategoria()); // Categoria não muda
        Assertions.assertNotNull(velAlterado.getProprietarioPessoa());
        Assertions.assertEquals(cpfNovo, velAlterado.getProprietarioPessoa().getCpf()); // Proprietário é o novo
        Assertions.assertNull(velAlterado.getProprietarioEmpresa());

        // Verificação: Apólice foi criada corretamente (valores iguais ao teste 11)
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(new BigDecimal("2223.00"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1710.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velAlterado, apBuscada.getVeiculo()));

        // Verificação: Bônus do NOVO segurado foi creditado
        SeguradoPessoa spNovoBuscado = (SeguradoPessoa)cadPessoa.buscar(cpfNovo);
        Assertions.assertNotNull(spNovoBuscado);
        Assertions.assertEquals(new BigDecimal("513.00"), spNovoBuscado.getBonus());

        // Verificação: Bônus do ANTIGO segurado não foi alterado
        SeguradoPessoa spAntigoBuscado = (SeguradoPessoa)cadPessoa.buscar(cpfAntigo);
        Assertions.assertNotNull(spAntigoBuscado);
        Assertions.assertEquals(new BigDecimal("0.00"), spAntigoBuscado.getBonus());
    }
    @Test
    public void test014_alterarVeiculoExistenteEmpresaSemBonusSemSinistro() {
        String cnpjAntigo = "00000000000000";
        String cnpjNovo = "11851715000174";
        String placa = "KKK0019";
        int ano = 2020;

        // Configuração: Cria segurados (antigo e novo) e veículo existente com segurado antigo
        SeguradoEmpresa seAntigo = new SeguradoEmpresa("ACME Antiga", null, LocalDate.now(), new BigDecimal("0.00"), cnpjAntigo, 200000.0, false);
        SeguradoEmpresa seNovo = new SeguradoEmpresa("ORG TABAJARA Nova", null, LocalDate.now(), new BigDecimal("0.00"), cnpjNovo, 3006700.0, false);
        cadEmpresa.incluir(seAntigo, cnpjAntigo);
        cadEmpresa.incluir(seNovo, cnpjNovo);
        Veiculo velExistente = new Veiculo(placa, ano, seAntigo, null, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(velExistente, placa);

        // Ação: Inclui apólice para o novo segurado (veículo já existe)
        String numeroEsperado = LocalDate.now().getYear() + cnpjNovo + placa;
        DadosVeiculo dr = new DadosVeiculo(cnpjNovo, placa, ano, new BigDecimal("57000.00"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso, número correto
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Veículo foi ALTERADO corretamente (proprietário mudou)
        Veiculo velAlterado = (Veiculo)cadVeiculo.buscar(placa);
        Assertions.assertNotNull(velAlterado);
        Assertions.assertEquals(ano, velAlterado.getAno());
        Assertions.assertEquals(CategoriaVeiculo.INTERMEDIARIO, velAlterado.getCategoria());
        Assertions.assertNotNull(velAlterado.getProprietarioEmpresa());
        Assertions.assertEquals(cnpjNovo, velAlterado.getProprietarioEmpresa().getCnpj()); // Proprietário é o novo
        Assertions.assertNull(velAlterado.getProprietarioPessoa());

        // Verificação: Apólice foi criada corretamente (valores iguais ao teste 12)
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(new BigDecimal("2223.00"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1710.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(velAlterado, apBuscada.getVeiculo()));

        // Verificação: Bônus do NOVO segurado foi creditado
        SeguradoEmpresa seNovoBuscado = (SeguradoEmpresa)cadEmpresa.buscar(cnpjNovo);
        Assertions.assertNotNull(seNovoBuscado);
        Assertions.assertEquals(new BigDecimal("513.00"), seNovoBuscado.getBonus());

        // Verificação: Bônus do ANTIGO segurado não foi alterado
        SeguradoEmpresa seAntigoBuscado = (SeguradoEmpresa)cadEmpresa.buscar(cnpjAntigo);
        Assertions.assertNotNull(seAntigoBuscado);
        Assertions.assertEquals(new BigDecimal("0.00"), seAntigoBuscado.getBonus());
    }
    @Test
    public void test015_incluirComBonusPessoaSemSinistro() {
        String cpf = "07255431089";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado Pessoa COM BÔNUS inicial
        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(), new BigDecimal("1000.00"), cpf, 20000.0);
        cadPessoa.incluir(sp, cpf);

        // Ação: Inclui apólice
        String numeroEsperado = LocalDate.now().getYear() + "000" + cpf + placa;
        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.00"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Apólice criada com prêmio descontado pelo bônus
        // VMax = 57000.00, VPA = 1710.00, VPB = 1710.00, Bonus = 1000.00
        // VPC = 1710.00 - (1000.00 / 10) = 1710.00 - 100.00 = 1610.00
        // Premio = 1610.00
        // Franquia = 1.3 * VPB = 2223.00
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(new BigDecimal("2223.00"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1610.00"), apBuscada.getValorPremio()); // Prêmio com desconto
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());

        // Verificação: Bônus do segurado foi creditado (30% do prêmio calculado)
        // Bonus a creditar = 0.30 * 1610.00 = 483.00
        // Bonus final = 1000.00 (inicial) + 483.00 (crédito) = 1483.00
        SeguradoPessoa spBuscado = (SeguradoPessoa)cadPessoa.buscar(cpf);
        Assertions.assertNotNull(spBuscado);
        Assertions.assertEquals(new BigDecimal("1483.00"), spBuscado.getBonus());
    }
    @Test
    public void test016_incluirComBonusEmpresaLocadoraSemSinistro() {
        String cnpj = "11851715000174";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado Empresa LOCADORA COM BÔNUS inicial
        SeguradoEmpresa se = new SeguradoEmpresa("ACME Locadora", null, LocalDate.now(), new BigDecimal("1000.00"), cnpj, 20000.0, true); // ehLocadora = true
        cadEmpresa.incluir(se, cnpj);

        // Ação: Inclui apólice
        String numeroEsperado = LocalDate.now().getYear() + cnpj + placa;
        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2); // Cat 2, Ano 2020: 60000.00
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Apólice criada com prêmio e franquia ajustados para locadora e bônus
        // VMax = 57000.00, VPA = 1710.00
        // VPB = 1.2 * VPA = 1.2 * 1710.00 = 2052.00
        // Bonus = 1000.00
        // VPC = 2052.00 - (1000.00 / 10) = 2052.00 - 100.00 = 1952.00
        // Premio = 1952.00
        // Franquia = 1.3 * VPB = 1.3 * 2052.00 = 2667.60
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(new BigDecimal("2667.60"), apBuscada.getValorFranquia()); // Franquia de locadora
        Assertions.assertEquals(new BigDecimal("1952.00"), apBuscada.getValorPremio()); // Prêmio com desconto e ajuste locadora
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());

        // Verificação: Bônus do segurado foi creditado (30% do prêmio calculado)
        // Bonus a creditar = 0.30 * 1952.00 = 585.60
        // Bonus final = 1000.00 (inicial) + 585.60 (crédito) = 1585.60
        SeguradoEmpresa seBuscado = (SeguradoEmpresa)cadEmpresa.buscar(cnpj);
        Assertions.assertNotNull(seBuscado);
        Assertions.assertEquals(new BigDecimal("1585.60"), seBuscado.getBonus());
    }
    @Test
    public void test017_incluirComBonusPessoaComSinistroAnoAnterior() {
        String cpf = "07255431089";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado Pessoa COM BÔNUS inicial
        SeguradoPessoa sp = new SeguradoPessoa("Carlos", null, LocalDate.now(), new BigDecimal("1000.00"), cpf, 20000.0);
        cadPessoa.incluir(sp, cpf);
        // Configuração: Cria Veículo associado ao segurado
        Veiculo vel = new Veiculo(placa, ano, null, sp, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(vel, placa);
        // Configuração: Cria Sinistro para este segurado no ANO ANTERIOR
        LocalDateTime dhSinistroAnoAnterior = LocalDateTime.now().minusYears(1);
        Sinistro s1 = new Sinistro(NUM_SINISTRO, vel, dhSinistroAnoAnterior, LocalDateTime.now(),"ego", new BigDecimal("30000.00"), TipoSinistro.COLISAO);
        cadSinistro.incluir(s1, NUM_SINISTRO);

        // Ação: Inclui apólice
        String numeroEsperado = LocalDate.now().getYear() + "000" + cpf + placa;
        DadosVeiculo dr = new DadosVeiculo(cpf, placa, ano, new BigDecimal("57000.00"), 2);
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Apólice criada com prêmio descontado pelo bônus (mesmo cálculo do teste 15)
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(new BigDecimal("2223.00"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1610.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());

        // Verificação: Bônus do segurado NÃO foi creditado devido ao sinistro no ano anterior
        SeguradoPessoa spBuscado = (SeguradoPessoa)cadPessoa.buscar(cpf);
        Assertions.assertNotNull(spBuscado);
        Assertions.assertEquals(new BigDecimal("1000.00"), spBuscado.getBonus()); // Permanece o bônus inicial
    }
    @Test
    public void test018_incluirComBonusEmpresaLocadoraComSinistroAnoAnterior() {
        String cnpj = "11851715000174";
        String placa = "KKK0019";
        int ano = 2020;
        // Configuração: Cria Segurado Empresa LOCADORA COM BÔNUS inicial
        SeguradoEmpresa se = new SeguradoEmpresa("ACME Locadora", null, LocalDate.now(), new BigDecimal("1000.00"), cnpj, 20000.0, true);
        cadEmpresa.incluir(se, cnpj);
        // Configuração: Cria Veículo associado ao segurado
        Veiculo vel = new Veiculo(placa, ano, se, null, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(vel, placa);
        // Configuração: Cria Sinistro para este segurado no ANO ANTERIOR
        LocalDateTime dhSinistroAnoAnterior = LocalDateTime.now().minusYears(1);
        Sinistro s1 = new Sinistro(NUM_SINISTRO, vel, dhSinistroAnoAnterior, LocalDateTime.now(),"ego", new BigDecimal("30000.00"), TipoSinistro.COLISAO);
        cadSinistro.incluir(s1, NUM_SINISTRO);

        // Ação: Inclui apólice
        String numeroEsperado = LocalDate.now().getYear() + cnpj + placa;
        DadosVeiculo dr = new DadosVeiculo(cnpj, placa, ano, new BigDecimal("57000.00"), 2);
        RetornoInclusaoApolice ret = mediator.incluirApolice(dr);

        // Verificação: Sucesso
        Assertions.assertNull(ret.getMensagemErro());
        Assertions.assertEquals(numeroEsperado, ret.getNumeroApolice());

        // Verificação: Apólice criada com prêmio e franquia ajustados (mesmo cálculo do teste 16)
        Apolice apBuscada = (Apolice)cadastro.buscar(numeroEsperado);
        Assertions.assertNotNull(apBuscada);
        Assertions.assertEquals(new BigDecimal("2667.60"), apBuscada.getValorFranquia());
        Assertions.assertEquals(new BigDecimal("1952.00"), apBuscada.getValorPremio());
        Assertions.assertEquals(new BigDecimal("57000.00"), apBuscada.getValorMaximoSegurado());

        // Verificação: Bônus do segurado NÃO foi creditado devido ao sinistro no ano anterior
        SeguradoEmpresa seBuscado = (SeguradoEmpresa)cadEmpresa.buscar(cnpj);
        Assertions.assertNotNull(seBuscado);
        Assertions.assertEquals(new BigDecimal("1000.00"), seBuscado.getBonus()); // Permanece o bônus inicial
    }
    @Test
    public void test019_buscarInexistente() {
        Apolice ap = mediator.buscarApolice(NUM_AP);
        Assertions.assertNull(ap);
        ap = mediator.buscarApolice(" ");
        Assertions.assertNull(ap);
        ap = mediator.buscarApolice(null);
        Assertions.assertNull(ap);
    }
    @Test
    public void test020_buscarExistente() {
        // Configuração: Cria uma apólice para buscar
        Apolice apEsp = new Apolice(NUM_AP, null, new BigDecimal("2667.60"), new BigDecimal("1952.00"), new BigDecimal("57000.00").setScale(2, RoundingMode.HALF_UP), LocalDate.now());
        cadastro.incluir(apEsp, NUM_AP);

        // Ação: Busca a apólice
        Apolice ap = mediator.buscarApolice(NUM_AP);

        // Verificação
        Assertions.assertNotNull(ap);
        // Compara os objetos serializados para garantir igualdade completa
        Assertions.assertTrue(ComparadoraObjetosSerial.compareObjectsSerial(apEsp, ap));
    }
    @Test
    public void test021_excluirValidacoes() {
        // Número inválido
        String msg = mediator.excluirApolice(" ");
        Assertions.assertEquals("Número deve ser informado", msg);
        msg = mediator.excluirApolice(null);
        Assertions.assertEquals("Número deve ser informado", msg);

        // Apólice inexistente
        msg = mediator.excluirApolice(NUM_AP);
        Assertions.assertEquals("Apólice inexistente", msg);
    }
    @Test
    public void test022_excluirComSinistroNoAno() {
        LocalDate dataApolice = LocalDate.now();
        // Configuração: Cria Veículo, Apolice e Sinistro no mesmo ano
        Veiculo vel = new Veiculo(PLACA_KKK6666, 2020, null, null, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(vel, PLACA_KKK6666);
        Apolice ap = new Apolice(NUM_AP, vel, new BigDecimal("2667.60"), new BigDecimal("1952.00"), new BigDecimal("57000.00"), dataApolice);
        cadastro.incluir(ap, NUM_AP);
        Sinistro sin = new Sinistro(NUM_SINISTRO, vel, LocalDateTime.now(), LocalDateTime.now(), "ego", new BigDecimal("50000.00"),TipoSinistro.COLISAO);
        cadSinistro.incluir(sin, NUM_SINISTRO);

        // Ação: Tenta excluir a apólice
        String msg = mediator.excluirApolice(NUM_AP);

        // Verificação: Não deve permitir excluir
        Assertions.assertEquals("Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice", msg);
        // Verifica se a apólice ainda existe
        Assertions.assertNotNull(cadastro.buscar(NUM_AP));
    }
    @Test
    public void test023_excluirSemSinistroNoAno() {
        LocalDate dataApolice = LocalDate.now();
        // Configuração: Cria Veículo, Apolice e Sinistro em ano diferente
        Veiculo vel = new Veiculo(PLACA_KKK6666, 2020, null, null, CategoriaVeiculo.INTERMEDIARIO);
        cadVeiculo.incluir(vel, PLACA_KKK6666);
        Apolice ap = new Apolice(NUM_AP, vel, new BigDecimal("2667.60"), new BigDecimal("1952.00"), new BigDecimal("57000.00"), dataApolice);
        cadastro.incluir(ap, NUM_AP);
        Sinistro sin = new Sinistro(NUM_SINISTRO, vel, LocalDateTime.now().minusYears(1), LocalDateTime.now(), "ego", new BigDecimal("50000.00"),TipoSinistro.COLISAO); // Ano anterior
        cadSinistro.incluir(sin, NUM_SINISTRO);

        // Ação: Tenta excluir a apólice
        String msg = mediator.excluirApolice(NUM_AP);

        // Verificação: Deve permitir excluir
        Assertions.assertNull(msg);
        // Verifica se a apólice foi realmente excluída
        Assertions.assertNull(cadastro.buscar(NUM_AP));
    }

    @Override
    protected Class<? extends Serializable> getClasse() {
        return Apolice.class;
    }
}
