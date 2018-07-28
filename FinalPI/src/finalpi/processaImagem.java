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
/**
 *
 * @author CLIENTE
 */
public final class processaImagem {
    
    private String tipo_arquivo;
    private String comentario;
    private int cols;
    private int rows;
    private int max_value;
    private int mat_imagem[][];
    private int mat_aux[][];
    public String nomesalvar;
    
    
    public processaImagem(String filename) throws FileNotFoundException{
        
       carregaImagem(filename);
        
    }

    
   
    public void carregaImagem(String filename) throws FileNotFoundException{
        //Lendo arquivo
        Scanner infile = new Scanner(new FileReader(filename));
        //Pulando as duas primeiras linhas
        this.tipo_arquivo = infile.nextLine();

        //Le o comentario
        this.comentario = infile.nextLine();
        
        
        String linha = infile.nextLine();
        
        //Salvando dimensões nas variáveis
        this.cols = Integer.parseInt(linha.split("\\s+")[0]);
	this.rows = Integer.parseInt(linha.split("\\s+")[1]);

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
    
    //pode deletar essa funcao depois
    public void printaimagem(){
        for(int i=0; i< this.rows; i++){
            for(int j=0; j< this.cols; j++){
                System.out.print(mat_imagem[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }
    }
    
    //pode deletar essa funcao depois
    public void printaaux(){
        for(int i=0; i< this.rows; i++){
            for(int j=0; j< this.cols; j++){
                System.out.print(mat_aux[i][j]);
                System.out.print(" ");
            }
            System.out.println("");
        }   
    }
    
    
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
    
    
    
    public void passabaixa() throws IOException{
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
    
    
    public void passaalta() throws IOException{
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
                        if(k!=i && z!=j){
                            valor = valor + (-1*mat_imagem[k][z]);
                        }else{
                            valor = valor + (8*mat_imagem[k][z]);
                        }
                    }
                }
                
                if(valor > max_value){
                    valor=max_value;
                }else if(valor < 0){
                    valor=0;
                }
                mat_aux[i][j]=valor;
            }
        }
        
        //depois de aplicar o filtro salva a imagem
        salvarimagem();
    }
}
