/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dojointercalacao;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Executa o algoritmo Intercalação Ótima
 *
 * @param nomeParticoes array com os nomes dos arquivos que contêm as partições
 * de entrada
 * @param nomeArquivoSaida nome do arquivo de saída resultante da execução do
 * algoritmo
 */
public class Intercalacao {

    public void executa(List<String> nomeParticoes, String nomeArquivoSaida) throws Exception {

        DataOutputStream saidaFinal = null;
        DataInputStream entrada1 = null;
        DataInputStream entrada2 = null;
        DataInputStream entrada3 = null;
        DataOutputStream intermediario = null;
        DataInputStream entradaFinal = null;
        try {

            ArrayList<String> fila = new ArrayList<String>();
            for (int i = 0; i < nomeParticoes.size(); i++) {
                fila.add(nomeParticoes.get(i));
            }
            int i = 0;

            while (fila.size() > 1) {

                entrada1 = new DataInputStream(new BufferedInputStream(new FileInputStream(fila.get(0))));

                entrada2 = new DataInputStream(new BufferedInputStream(new FileInputStream(fila.get(1))));

                entrada3 = null;

                if (fila.size() >= 3) {
                    entrada3 = new DataInputStream(new BufferedInputStream(new FileInputStream(fila.get(2))));
                }

                intermediario = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("intermediario" + i + ".dat")));
                //intermediario = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nomeArquivoSaida)));

                if (entrada3 != null) {
                    Cliente a = Cliente.le(entrada1);
                    Cliente b = Cliente.le(entrada2);
                    Cliente c = Cliente.le(entrada3);
                    int menor = 0;
                    do {
                        if (menor == 1) {
                            a = Cliente.le(entrada1);
                        } else {
                            if (menor == 2) {
                                b = Cliente.le(entrada2);
                            } else {
                                if (menor == 3) {
                                    c = Cliente.le(entrada3);
                                }
                            }
                        }

                        if (a.codCliente < b.codCliente && a.codCliente < c.codCliente) {
                            a.salva(intermediario);
                            menor = 1;
                        } else {
                            if (b.codCliente < c.codCliente) {
                                b.salva(intermediario);
                                menor = 2;
                            } else {
                                c.salva(intermediario);
                                menor = 3;
                            }
                        }

                    } while (a.codCliente != Integer.MAX_VALUE || b.codCliente != Integer.MAX_VALUE || c.codCliente != Integer.MAX_VALUE);

                } else {// só tem 2 arquivos de entrada
                    Cliente a = Cliente.le(entrada1);
                    Cliente b = Cliente.le(entrada2);
                    int menor = 0;
                    do {
                        if (menor == 1) {
                            a = Cliente.le(entrada1);
                        }
                        if (menor == 2) {
                            b = Cliente.le(entrada2);
                        }

                        if (a.codCliente < b.codCliente) {
                            a.salva(intermediario);
                            menor = 1;
                        } else {
                            b.salva(intermediario);
                            menor = 2;
                        }

                    } while (a.codCliente != Integer.MAX_VALUE || b.codCliente != a.codCliente);

                }
                Cliente max = new Cliente(Integer.MAX_VALUE, "", "");
                max.salva(intermediario);
                fila.add("intermediario" + i + ".dat");
                i++;

                if (entrada3 != null) {
                    for (int j = 0; j < 3; j++) {
                        fila.remove(0);
                    }
                } else {
                    for (int j = 0; j < 2; j++) {
                        fila.remove(0);
                    }
                }
                
                //if (intermediario != null) {
                    intermediario.close();
                //}

            }

            if (fila.size() == 1) {
                //AQUI EU TENHO QUE LER TUDO QUE ESTA NO ARQUIVO COM NOME FILA.GET(0) E ESECREVER NO SAIDAFINAL!!
                Cliente cliente;
                entradaFinal = new DataInputStream(new BufferedInputStream(new FileInputStream(fila.get(0))));
                saidaFinal = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nomeArquivoSaida)));
                do {
                    int testzin = entradaFinal.available();

                    cliente = Cliente.le(entradaFinal);
                    cliente.salva(saidaFinal);
                } while (cliente.codCliente != Integer.MAX_VALUE);

            }

        } finally {
            if (saidaFinal != null) {
                saidaFinal.close();
            }
            if (intermediario != null) {
                intermediario.close();
            }
            if(entrada1 != null){
                entrada1.close();
            }
            if(entrada2 != null){
                entrada2.close();
            }
            if(entrada3 != null){
                entrada3.close();
            }
            if(entradaFinal != null){
                entradaFinal.close();
            }
            
        }

    }

}
