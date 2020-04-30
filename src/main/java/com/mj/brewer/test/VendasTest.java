package com.mj.brewer.test;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.mj.brewer.dto.VendaMes;

public class VendasTest {
	
	@Test
	public void vendasMesesTest() {
		List<VendaMes> vendasMeses = new ArrayList<>();
		vendasMeses.add(new VendaMes("2018/07", 1));
		vendasMeses.add(new VendaMes("2018/04", 1));
		vendasMeses.add(new VendaMes("2018/03", 1));
		vendasMeses.add(new VendaMes("2018/02", 1));
		vendasMeses.add(new VendaMes("2018/01", 1));
		vendasMeses.add(new VendaMes("2017/12", 1));
		
		System.out.println(vendasMeses);

		if (!vendasMeses.isEmpty()) {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM");
			YearMonth ultimo = null;

			for (int i = 0; i < 6; i++) {

				if (i < vendasMeses.size()) {
					VendaMes vm = vendasMeses.get(i);
					YearMonth atual = YearMonth.parse(vm.getMes(), formatter);
					
					// se não é o primeiro elemento da iteracao
					if (ultimo != null) {
						int mesAnterior = ultimo.getMonthValue() - 1;
						
						// se o mes anterior do ultimo mes obtido for diferente do mes atual, será adicionado o mes anterior do ultimo mes a lista
						if (mesAnterior != atual.getMonthValue()) {
							ultimo = YearMonth.of(ultimo.getYear(), mesAnterior);
							vendasMeses.add(i, new VendaMes(ultimo.format(formatter), 0));
						} else {
							ultimo = atual;
						}
						
					} else {
						ultimo = atual;
					}
					
				} else {
					int mesAnterior = ultimo.getMonthValue() - 1;
					ultimo = YearMonth.of(ultimo.getYear(), mesAnterior);
					vendasMeses.add(i, new VendaMes(ultimo.format(formatter), 0));
				}
			}
			
			vendasMeses = vendasMeses.subList(0, 6);
		}
		
		System.out.println(vendasMeses);
		
		assert(vendasMeses.size() == 6);
	}

}
