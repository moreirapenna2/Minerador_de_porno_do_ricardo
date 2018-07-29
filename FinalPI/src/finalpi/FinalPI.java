/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author root
 */
public class FinalPI {
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        Scanner infile = new Scanner(System.in);
        String nomearq;
        System.out.println("Insira o nome do arquivo");
        nomearq = infile.nextLine();
        System.out.println(nomearq);
      
        processaImagem imagem = new processaImagem(nomearq);
        System.out.println("Insira o nome para salvar");
        imagem.nomesalvar = infile.nextLine();
        //imagem.passabaixa();
        //imagem.passaalta();
        imagem.sobel();
        //imagem.prewitt();
        //imagem.sobel2();
        //imagem.isotropico();
        System.out.print("\n\n\n\n\n\n\n\n");
    }

    
}

