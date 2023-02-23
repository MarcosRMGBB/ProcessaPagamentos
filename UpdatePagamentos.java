import java.time.LocalDate;
import java.time.Period;

public class UpdatePagamentos {
    public static final double MULTA = 50.0;
    public static final double JUROS = 0.01;
    public static final int PONTOS_ATRASO = 1;
    public static final double CONVERSOR_PORCENTAGEM = 100.0;
    LocalDate hoje = LocalDate.now();
    int diasCalculados;
    int mesesCalculados;
    Pagamentos pagamento;

    public UpdatePagamentos(Pagamentos pagamento) {
        this.pagamento = pagamento;
    }

    public void periodoCalculado() {
        LocalDate vencimento = this.pagamento.getDataVencimento();
        Period periodo = Period.between(vencimento, hoje);
        this.diasCalculados = periodo.getDays();
        this.mesesCalculados = periodo.getMonths();
    }

    public int getPeriodoCalculado() {
        return diasCalculados;
    }

    public void atualizaValor() {
        double valorAtual = this.pagamento.getValor();
        int semanas = (int) this.diasCalculados / 7; 
        double juros = semanas * JUROS;
        double jurosReal = juros * valorAtual;
        double jurosMulta = jurosReal + MULTA;
        double novoValor = jurosMulta + valorAtual;
        this.pagamento.setValor(novoValor); 
        
    }

    public void atualizaPontos() {
        int classificacaoAtual = pagamento.getClassificacao();
        int pontosPerdidos = mesesCalculados * PONTOS_ATRASO;
        int novaClassificacao = classificacaoAtual - pontosPerdidos;
        this.pagamento.setClassificacao(novaClassificacao);
    }

    public void aplicaDesconto() {
        double desconto = (double) this.pagamento.getClassificacao() / CONVERSOR_PORCENTAGEM;
        double valorDesconto = this.pagamento.getValor() * desconto;
        if (valorDesconto > 500) {
            valorDesconto = 500;
        }
        double novoValor = pagamento.getValor() - valorDesconto;
        pagamento.setValor(novoValor);  
    }
}
