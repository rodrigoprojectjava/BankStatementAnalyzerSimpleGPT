
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BankStatementAnalyzerSimpleGPT {

	String arquivoCSV = "bank-data-simple.csv";
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		
		try {
			List<String[]> transacoes = lerCSV("bank-data-simple.csv");
			double totalLucroPerda = calcularTotalLucroPerda(transacoes);
			int totalTransacoesMes = contarTransacoesMes(transacoes,2); // Altere para o mês desejado
			List<String> topDespesas = obterTopDespesas(transacoes, 10);
			String categoriaMaiorGasto = obterCategoriaMaiorGasto(transacoes);
			
			System.out.println("Total de Lucro/Perda: " + totalLucroPerda);
            System.out.println("Número de Transações no Mês: " + totalTransacoesMes);
            System.out.println("Top 10 Despesas: " + topDespesas);
            System.out.println("Categoria com Maior Gasto: " + categoriaMaiorGasto);

		} catch 
			(IOException | ParseException e) {
	            e.printStackTrace();
		}
	}
	
	 private static List<String[]> lerCSV(String arquivoCSV) throws IOException {
	        List<String[]> linhas = new ArrayList<>();
	        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
	            String linha;
	            while ((linha = br.readLine()) != null) {
	                String[] dados = linha.split(",");
	                linhas.add(dados);
	            }
	        }
	        return linhas;
	    }
	 
	 private static double calcularTotalLucroPerda(List<String[]> transacoes) {
	        double total = 0;
	        for (String[] transacao : transacoes) {
	            total += Double.parseDouble(transacao[1]);
	        }
	        return total;
	    }
	 
	 private static int contarTransacoesMes(List<String[]> transacoes, int mesDesejado) throws ParseException {
	        SimpleDateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy");
	        int contador = 0;

	        for (String[] transacao : transacoes) {
	            Date data = formatoData.parse(transacao[0]);
	            Calendar cal = Calendar.getInstance();
	            cal.setTime(data);

	            if (cal.get(Calendar.MONTH) + 1 == mesDesejado) {
	                contador++;
	            }
	        }

	        return contador;
	    }
	 
	 private static List<String> obterTopDespesas(List<String[]> transacoes, int topN) {
	        Map<String, Double> despesas = new HashMap<>();

	        for (String[] transacao : transacoes) {
	            if (Double.parseDouble(transacao[1]) < 0) {
	                String categoria = transacao[2];
	                despesas.put(categoria, despesas.getOrDefault(categoria, 0.0) + Double.parseDouble(transacao[1]));
	            }
	        }

	        return despesas.entrySet().stream()
	                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
	                .limit(topN)
	                .map(Map.Entry::getKey)
	                .toList();
	    }
	 
	 private static String obterCategoriaMaiorGasto(List<String[]> transacoes) {
	        Map<String, Double> gastosPorCategoria = new HashMap<>();

	        for (String[] transacao : transacoes) {
	            if (Double.parseDouble(transacao[1]) < 0) {
	                String categoria = transacao[2];
	                gastosPorCategoria.put(categoria, gastosPorCategoria.getOrDefault(categoria, 0.0) + Double.parseDouble(transacao[1]));
	            }
	        }

	        return gastosPorCategoria.entrySet().stream()
	                .max(Map.Entry.comparingByValue())
	                .map(Map.Entry::getKey)
	                .orElse("N/A");
	
	 }

}
