package br.edu.cs.poo.ac.seguro.gui;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoEmpresa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoEmpresaMediator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaSeguradoEmpresaCRUD extends JFrame {

    private final SeguradoEmpresaMediator seguradoEmpresaMediator = SeguradoEmpresaMediator.getInstancia();

    private enum EstadoTela {
        INICIAL, NOVO, VISUALIZACAO, ALTERACAO
    }

    // Componentes da Tela
    private JTextField txtCnpj;
    private JTextField txtNome;
    private JTextField txtDataAbertura;
    private JTextField txtFaturamento;
    private JCheckBox chkEhLocadora;
    private JTextField txtBonus;
    private JTextField txtLogradouro;
    private JTextField txtNumero;
    private JTextField txtComplemento;
    private JTextField txtCep;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtPais;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnBuscar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public TelaSeguradoEmpresaCRUD() {
        initComponents();
        configurarEstado(EstadoTela.INICIAL);
    }

    private void initComponents() {
        setTitle("CRUD de Segurado Empresa");
        setSize(600, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);

        // --- Grupo de Dados da Empresa ---
        JPanel pnlDadosEmpresa = new JPanel(new GridBagLayout());
        pnlDadosEmpresa.setBorder(new TitledBorder("Dados da Empresa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlDadosEmpresa.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.9;
        txtCnpj = new JTextField(20);
        pnlDadosEmpresa.add(txtCnpj, gbc);

        gbc.gridy++; gbc.gridx = 0;
        pnlDadosEmpresa.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField();
        pnlDadosEmpresa.add(txtNome, gbc);

        gbc.gridy++; gbc.gridx = 0;
        pnlDadosEmpresa.add(new JLabel("Abertura (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1;
        txtDataAbertura = new JTextField();
        pnlDadosEmpresa.add(txtDataAbertura, gbc);

        gbc.gridy++; gbc.gridx = 0;
        pnlDadosEmpresa.add(new JLabel("Faturamento:"), gbc);
        gbc.gridx = 1;
        txtFaturamento = new JTextField();
        pnlDadosEmpresa.add(txtFaturamento, gbc);

        gbc.gridy++; gbc.gridx = 0;
        pnlDadosEmpresa.add(new JLabel("Bônus:"), gbc);
        gbc.gridx = 1;
        txtBonus = new JTextField();
        txtBonus.setEditable(false);
        pnlDadosEmpresa.add(txtBonus, gbc);

        gbc.gridy++; gbc.gridx = 1;
        chkEhLocadora = new JCheckBox("É locadora de veículos?");
        pnlDadosEmpresa.add(chkEhLocadora, gbc);

        contentPanel.add(pnlDadosEmpresa);

        // --- Grupo de Endereço ---
        JPanel pnlEndereco = new JPanel(new GridBagLayout());
        pnlEndereco.setBorder(new TitledBorder("Endereço"));
        // (Reutilizando a mesma configuração de GridBagConstraints 'gbc')

        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Logradouro:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.9;
        txtLogradouro = new JTextField();
        pnlEndereco.add(txtLogradouro, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 1;
        pnlEndereco.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtNumero = new JTextField();
        pnlEndereco.add(txtNumero, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtComplemento = new JTextField();
        pnlEndereco.add(txtComplemento, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtCep = new JTextField();
        pnlEndereco.add(txtCep, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("País:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtPais = new JTextField();
        pnlEndereco.add(txtPais, gbc);

        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtCidade = new JTextField();
        pnlEndereco.add(txtCidade, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Estado (UF):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtEstado = new JTextField();
        pnlEndereco.add(txtEstado, gbc);

        contentPanel.add(pnlEndereco);

        // --- Painel de Botões ---
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        pnlBotoes.add(btnNovo);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnBuscar);
        pnlBotoes.add(btnAlterar);
        pnlBotoes.add(btnExcluir);
        pnlBotoes.add(btnLimpar);
        add(pnlBotoes, BorderLayout.SOUTH);

        // --- Listeners ---
        btnNovo.addActionListener(e -> {
            limparCampos();
            configurarEstado(EstadoTela.NOVO);
        });
        btnLimpar.addActionListener(e -> {
            limparCampos();
            configurarEstado(EstadoTela.INICIAL);
        });
        btnBuscar.addActionListener(e -> {
            String cnpj = txtCnpj.getText().trim();
            SeguradoEmpresa segurado = seguradoEmpresaMediator.buscarSeguradoEmpresa(cnpj);
            if (segurado != null) {
                popularTela(segurado);
                configurarEstado(EstadoTela.VISUALIZACAO);
            } else {
                JOptionPane.showMessageDialog(this, "Segurado não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                limparCampos();
                configurarEstado(EstadoTela.INICIAL);
            }
        });
        btnAlterar.addActionListener(e -> configurarEstado(EstadoTela.ALTERACAO));
        btnExcluir.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este segurado?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                String cnpj = txtCnpj.getText().trim();
                String msg = seguradoEmpresaMediator.excluirSeguradoEmpresa(cnpj);
                if (msg == null) {
                    JOptionPane.showMessageDialog(this, "Segurado excluído com sucesso!");
                    limparCampos();
                    configurarEstado(EstadoTela.INICIAL);
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnSalvar.addActionListener(e -> {
            SeguradoEmpresa segurado = obterDadosDaTela();
            if (segurado != null) {
                boolean isInclusao = !btnAlterar.isEnabled();
                String msg = isInclusao ? seguradoEmpresaMediator.incluirSeguradoEmpresa(segurado) : seguradoEmpresaMediator.alterarSeguradoEmpresa(segurado);

                if (msg == null) {
                    JOptionPane.showMessageDialog(this, "Operação realizada com sucesso!");
                    SeguradoEmpresa seguradoSalvo = seguradoEmpresaMediator.buscarSeguradoEmpresa(segurado.getCnpj());
                    if (seguradoSalvo != null) {
                        popularTela(seguradoSalvo);
                    }
                    configurarEstado(EstadoTela.VISUALIZACAO);
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void configurarEstado(EstadoTela estado) {
        boolean camposEditaveis = (estado == EstadoTela.NOVO || estado == EstadoTela.ALTERACAO);

        txtNome.setEditable(camposEditaveis);
        txtDataAbertura.setEditable(camposEditaveis);
        txtFaturamento.setEditable(camposEditaveis);
        chkEhLocadora.setEnabled(camposEditaveis);
        txtLogradouro.setEditable(camposEditaveis);
        txtNumero.setEditable(camposEditaveis);
        txtComplemento.setEditable(camposEditaveis);
        txtCep.setEditable(camposEditaveis);
        txtCidade.setEditable(camposEditaveis);
        txtEstado.setEditable(camposEditaveis);
        txtPais.setEditable(camposEditaveis);

        txtCnpj.setEditable(estado == EstadoTela.INICIAL || estado == EstadoTela.NOVO);

        btnNovo.setEnabled(estado == EstadoTela.INICIAL || estado == EstadoTela.VISUALIZACAO);
        btnSalvar.setEnabled(estado == EstadoTela.NOVO || estado == EstadoTela.ALTERACAO);
        btnBuscar.setEnabled(estado == EstadoTela.INICIAL);
        btnAlterar.setEnabled(estado == EstadoTela.VISUALIZACAO);
        btnExcluir.setEnabled(estado == EstadoTela.VISUALIZACAO);
        btnLimpar.setEnabled(true);
    }

    private void limparCampos() {
        for (Component panel : getContentPane().getComponents()) {
            if (panel instanceof JPanel) {
                for (Component group : ((JPanel) panel).getComponents()) {
                    if (group instanceof JPanel) {
                        for (Component field : ((JPanel) group).getComponents()) {
                            if (field instanceof JTextField) {
                                ((JTextField) field).setText("");
                            } else if (field instanceof JCheckBox) {
                                ((JCheckBox) field).setSelected(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void popularTela(SeguradoEmpresa segurado) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtCnpj.setText(segurado.getCnpj());
        txtNome.setText(segurado.getNome());
        txtDataAbertura.setText(segurado.getDataAbertura() != null ? segurado.getDataAbertura().format(dtf) : "");
        txtFaturamento.setText(String.valueOf(segurado.getFaturamento()));
        txtBonus.setText(segurado.getBonus() != null ? segurado.getBonus().toPlainString() : "0.00");
        chkEhLocadora.setSelected(segurado.isEhLocadoraDeVeiculos());

        if (segurado.getEndereco() != null) {
            Endereco end = segurado.getEndereco();
            txtLogradouro.setText(end.getLogradouro());
            txtNumero.setText(end.getNumero());
            txtComplemento.setText(end.getComplemento());
            txtCep.setText(end.getCep());
            txtCidade.setText(end.getCidade());
            txtEstado.setText(end.getEstado());
            txtPais.setText(end.getPais());
        }
    }

    private SeguradoEmpresa obterDadosDaTela() {
        String cnpj = txtCnpj.getText().trim();
        String nome = txtNome.getText().trim();
        String dataAberturaStr = txtDataAbertura.getText().trim();
        String faturamentoStr = txtFaturamento.getText().trim();
        boolean ehLocadora = chkEhLocadora.isSelected();

        String logradouro = txtLogradouro.getText().trim();
        String numero = txtNumero.getText().trim();
        String complemento = txtComplemento.getText().trim();
        String cep = txtCep.getText().trim();
        String cidade = txtCidade.getText().trim();
        String estado = txtEstado.getText().trim();
        String pais = txtPais.getText().trim();

        LocalDate dataAbertura = null;
        if (!dataAberturaStr.isEmpty()) {
            try {
                dataAbertura = LocalDate.parse(dataAberturaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de data de abertura inválido. Use dd/mm/aaaa.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        double faturamento = 0.0;
        if (!faturamentoStr.isEmpty()) {
            try {
                faturamento = Double.parseDouble(faturamentoStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Formato de faturamento inválido. Use um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        BigDecimal bonus = BigDecimal.ZERO;
        if (!txtBonus.getText().isEmpty()) {
            try {
                bonus = new BigDecimal(txtBonus.getText());
            } catch (NumberFormatException e) {
                // Ignora.
            }
        }

        Endereco end = new Endereco(logradouro, cep, numero, complemento, pais, estado, cidade);
        return new SeguradoEmpresa(nome, end, dataAbertura, bonus, cnpj, faturamento, ehLocadora);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaSeguradoEmpresaCRUD().setVisible(true));
    }
}