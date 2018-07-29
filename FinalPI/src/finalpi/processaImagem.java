/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalpi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author CLIENTE
 */
public final class processaImagem {
    
    //cria os dados do arquivo de imagem
    private String tipo_arquivo;
    private String comentario;
    private int cols;
    private int rows;
    private int max_value;
    private int mat_imagem[][];
    private int mat_aux[][];
    public String nomesalvar;
    
    //construtor da classe, automaticamente carrega a imagem
    public processaImagem(String filename) throws FileNotFoundException{
        
       carregaImagem(filename);
        
    }

    
   
    public void carregaImagem(String filename) throws FileNotFoundException{
        //Lendo arquivo
        Scanner infile = new Scanner(new FileReader(filename));
        //Le o tipo do arquivo (esperado P2)
        this.tipo_arquivo = infile.nextLine();

        //Le o comentario
        this.comentario = infile.nextLine();
        
        //Le a linha que contem linhas e colunas
        String linha = infile.nextLine();
        
        //Salvando dimensões nas variáveis
        this.cols = Integer.parseInt(linha.split("\\s+")[0]);
	this.rows = Integer.parseInt(linha.split("\\s+")[1]);
        
        //Informa ao usuario a quantidade de linhas e colunas
        System.out.println(cols + " colunas e " + rows + " linhas");
        
        //Salva o maior valor existente no arquivo
        linha = infile.nextLine();
        this.max_value = Integer.parseInt(linha);
        
        //Cria a matriz da imagem com as dimensões obtidas
        this.mat_imagem = new int[rows][cols];
        int cont = 0, i=0, j;
        
        
        //roda ate completar a imagem com os dados
        //se o proximo valor for inteiro passa para a matriz, caso contrario apenas
        //ignora (em caso de quebras de linha ou espacos)
        while(i<rows){
            j=0;
            while(j<cols){
                if(infile.hasNextInt() == true) {
                    mat_imagem[i][j] = infile.nextInt();
                    j++;
                }else{
                    infile.next();
                }
            }
            i++;
        }
        
    }
    
    
    //funcao para salvar a imagem
    private void salvarimagem() throws IOException{
        //cria o objeto que salva a imagem
        FileWriter fileWriter = new FileWriter(nomesalvar);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        
        //salva as propriedades
        printWriter.println(tipo_arquivo);
        printWriter.println(comentario);
        printWriter.println(rows + " " + cols);
        printWriter.println(max_value);
        
        //salva a imagem auxiliar
        for(int i=0; i < this.rows; i++){
            for(int j=0; j< this.cols; j++){
                printWriter.print(mat_aux[i][j]+" ");
            }
            printWriter.println("");
        }
   
    }
    
    
    //se o valor obtido for maior que o valor maximo, o valor obtido
    //sera igual ao valor maximo
    //se o valor obtido for menor que 0, o valor obtido sera igual a 0
    private int testarvalor(int valor, int max_value){
        if(valor > max_value){
                    valor = max_value;
                }else if(valor < 0){
                    valor = 0;
                }
        return(valor);
    }
    
    
    //filtro passa baixa 3x3
    public void passabaixa() throws IOException{
        //cria a imagem auxiliar
        this.mat_aux = new int[rows][cols];
        int i, j, k, z;
        
        //faz a imagem aux ser igual a imagem original
        mat_aux = mat_imagem;
        
        
        int valor;
        //itera pela imagem, faz o filtro passa baixa
        for(i=1; i< ((this.rows)-1); i++){
            for(j=1; j< ((this.cols)-1); j++){
                valor=0;
                for(k=(i-1); k <= (i+1); k++){
                    for(z=(j-1); z <= (j+1); z++){
                        valor = valor + mat_imagem[k][z];
                    }
                }
                mat_aux[i][j]=valor/9;
            }
        }
        
        //depois de aplicar o filtro salva a imagem
        salvarimagem();
    }
    
    
    //funcao filtro passa alta 3x3
    public void passaalta() throws IOException{
        this.mat_aux = new int[rows][cols];
        int i, j, k, z;
        int filtroLinha = 0, filtroCol = 0;
        
        //cria a mascara 
        int filtro[][] = {{-1, -1, -1}, 
                           {-1, 8, -1}, 
                           {-1, -1, -1}};
        
        //faz a imagem aux ser igual a imagem original
        mat_aux = mat_imagem;
        
        int valor;
        //itera pela imagem, aplicando a mascara na imagem
        for(i=1; i< ((this.rows)-1); i++){
            for(j=1; j< ((this.cols)-1); j++){
                valor=0;
                for(k=(i-1); k <= (i+1); k++){
                    for(z=(j-1); z <= (j+1); z++){
                        valor = valor + (filtro[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        filtroCol++;
                    }
                    filtroLinha++;
                    filtroCol = 0;
                }
                
                valor = testarvalor(valor, max_value);
                
                //passa o valor para a imagem e reseta o contador do filtro
                mat_aux[i][j]=valor;
                filtroLinha = 0;
                filtroCol = 0;
            }
            filtroLinha = 0;
            filtroCol = 0;
        }
        
        //depois de aplicar o filtro salva a imagem
        salvarimagem();
    }
    
    
    //funcao filtro sobel 3x3
    public void sobel() throws IOException{
        //cria a imagem auxiliar
        this.mat_aux = new int[rows][cols];
        int i, j, k, z;
        int valor1, valor2, valorFinal;
        int filtroLinha = 0, filtroCol = 0;
        
        //cria as mascaras 
        int filtro1[][] = {{-1, 0, 1}, 
                           {-2, 0, 2}, 
                           {-1, 0, 1}};
        int filtro2[][] = {{-1, -2, -1}, 
                           {0, 0, 0}, 
                           {1, 2, 1}};
        
        
        //itera pela imagem, aplicando as mascaras
        for(i=1; i< ((this.rows)-1); i++){
            for(j=1; j< ((this.cols)-1); j++){
                valor1 = 0;
                valor2 = 0;
                for(k=(i-1); k <= (i+1); k++){
                    for(z=(j-1); z <= (j+1); z++){
                        valor1 = valor1 + (filtro1[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        valor2 = valor2 + (filtro2[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        filtroCol++;
                    }
                    filtroLinha++;
                    filtroCol = 0;
                }
                
                valor1 = testarvalor(valor1, max_value);
                valor2 = testarvalor(valor2, max_value);
                
                //aplica a formula do gtadiente
                valorFinal = (int)Math.round(Math.sqrt(valor1 * valor1 + valor2 * valor2));
                
                //passa o valor para a imagem
                mat_aux[i][j]=valorFinal;
                filtroLinha = 0;
                filtroCol = 0;
            }
            filtroLinha = 0;
            filtroCol = 0;
        }
        salvarimagem();
    }
    
    
    //funcao filtro de prewitt 3x3
    public void prewitt() throws IOException{
        //cria a imagem auxiliar
        this.mat_aux = new int[rows][cols];
        int i, j, k, z;
        int valor1, valor2, valorFinal;
        int filtroLinha = 0, filtroCol = 0;
        
        //cria as mascaras
        int filtro1[][] = {{-1, -1, -1}, 
                           {0, 0, 0}, 
                           {1, 1, 1}};
        int filtro2[][] = {{-1, 0, 1}, 
                           {-1, 0, 1},
                           {-1, 0, 1}};
        
        //itera pela imagem, aplicando as mascaras
        for(i=1; i< ((this.rows)-1); i++){
            for(j=1; j< ((this.cols)-1); j++){
                valor1 = 0;
                valor2 = 0;
                for(k=(i-1); k <= (i+1); k++){
                    for(z=(j-1); z <= (j+1); z++){
                        valor1 = valor1 + (filtro1[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        valor2 = valor2 + (filtro2[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        filtroCol++;
                    }
                    filtroLinha++;
                    filtroCol = 0;
                }
                
                valor1 = testarvalor(valor1, max_value);
                valor2 = testarvalor(valor2, max_value);
                
                //aplica a formula do gradiente
                valorFinal = (int)Math.round(Math.sqrt(valor1 * valor1 + valor2 * valor2));
                
                //passa o valor para a imagem
                mat_aux[i][j]=valorFinal;
                filtroLinha = 0;
                filtroCol = 0;
            }
            filtroLinha = 0;
            filtroCol = 0;
        }
        salvarimagem();
    }
    
    
    //filtro sobel 2x2
    public void sobel2() throws IOException{
        //cria a imagem auxiliar
        this.mat_aux = new int[rows][cols];
        int i, j, k, z;
        int valor1, valor2, valorFinal;
        int filtroLinha = 0, filtroCol = 0;
        
        //cria as mascaras
        int filtro1[][] = {{-1, 0}, 
                           {0, 1}};
        int filtro2[][] = {{0, -1}, 
                           {1, 0}};
        
        //itera pela imagem, aplicando as mascaras
        for(i=1; i< ((this.rows)-1); i++){
            for(j=1; j< ((this.cols)-1); j++){
                valorFinal = 0;
                valor1 = 0;
                valor2 = 0;
                for(k=(i-1); k <= i; k++){
                    for(z=(j-1); z <= j; z++){
                        valor1 = valor1 + (filtro1[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        valor2 = valor2 + (filtro2[filtroLinha][filtroCol] * mat_imagem[k][z]);
                        filtroCol++;
                    }
                    filtroLinha++;
                    filtroCol = 0;
                }
                
                valor1 = testarvalor(valor1, max_value);
                valor2 = testarvalor(valor2, max_value);
                
                //aplica a formula do gradiente
                valorFinal = (int)Math.round(Math.sqrt(valor1 * valor1 + valor2 * valor2));
                
                //passa o valor para a imagem
                mat_aux[i][j]=valorFinal;
                filtroLinha = 0;
                filtroCol = 0;
            }
            filtroLinha = 0;
            filtroCol = 0;
        }
        salvarimagem();
    }
    
    
    //filto isotropico
    public void isotropico() throws IOException{
        //cria a imagem auxiliar
        this.mat_aux = new int[rows][cols];
        int i, j, k, z;
        int valorFinal;
        int filtroLinha = 0, filtroCol = 0;
        //nesse o valor deve ser double por contem raiz de 2
        double valor1, valor2;
        
        //cria a mascara
        double filtro1[][] = {{-1, 0, 1}, 
                           {-(Math.sqrt(2)), 0, (Math.sqrt(2))}, 
                           {-1, 0, 1}};
        double filtro2[][] = {{-1, -(Math.sqrt(2)), -1}, 
                           {0, 0, 0},
                           {1, (Math.sqrt(2)), 1}};
        
        //itera pela imagem, aplicando as mascaras
        for(i=1; i< ((this.rows)-1); i++){
            for(j=1; j< ((this.cols)-1); j++){
                valorFinal = 0;
                valor1 = 0;
                valor2 = 0;
                for(k=(i-1); k <= (i+1); k++){
                    for(z=(j-1); z <= (j+1); z++){
                        valor1 = (valor1 + (filtro1[filtroLinha][filtroCol] * mat_imagem[k][z]));
                        valor2 = (valor2 + (filtro2[filtroLinha][filtroCol] * mat_imagem[k][z]));
                        filtroCol++;
                    }
                    filtroLinha++;
                    filtroCol = 0;
                }
                
                
                //aqui nao pode usar a funcao pois o valor pode ser double e a 
                //funcao apenas usa inteiros
                if(valor1 > max_value){
                    valor1 = max_value;
                }else if(valor1 < 0){
                    valor1 = 0;
                }
                
                if(valor2 > max_value){
                    valor2 = max_value;
                }else if(valor2 < 0){
                    valor2 = 0;
                }
                
                //aplica a formula do gradiente
                valorFinal = (int)Math.round(Math.sqrt(valor1 * valor1 + valor2 * valor2));
                
                //passa o valor para a imagem
                mat_aux[i][j]=valorFinal;
                filtroLinha = 0;
                filtroCol = 0;
            }
            filtroLinha = 0;
            filtroCol = 0;
        }
        salvarimagem();
    }
    
}