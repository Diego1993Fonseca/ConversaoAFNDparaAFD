/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversaoafndparaafd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue; //Lista ordenada, sustituindo o queue
import java.util.regex.Pattern;
import javafx.util.Pair;



/**
 *
 * @author Diego
 */


public class ConversaoAFNDparaAFD {
       //variaveis globais 
        int qtdestados = 4;
        int qtdsimbolos = 3;

        String estadoInicial;
        ArrayList <String> estados;
        ArrayList <String> terminais;
        ArrayList <String> estadosfinais;
        Map <String,Integer> it; // :: iterator it;
        String matriz[][];
        ArrayList < String > AFD;
        PriorityQueue < String > estadoparaconversao;

    public ConversaoAFNDparaAFD() {
        this.matriz = new String [qtdestados][qtdsimbolos];
        this.estadoInicial  = null ;
        this.estados = new ArrayList<String>();
        this.terminais = new ArrayList<String>();
        this.estadosfinais = new ArrayList<String>();
        this.it = new HashMap <String,Integer>(); // :: iterator it;
        this.AFD = new ArrayList<String>();
        this.estadoparaconversao = new PriorityQueue<String>();
    }

    
    public ArrayList<String> quebrar(String modelo) {
        if(modelo == null)modelo = "";
        ArrayList<String> retorno = new ArrayList<String>();
        String  aux = "";
        char modeloChar[] = modelo.toCharArray();
        
        
	for(int i=0;i<modelo.length();i++){
		if ( modeloChar[i] == ',' || modeloChar[i] == ' ' ){
			if ( aux != "" ) retorno.add(aux);
			aux = "";
		} else { 
			aux += modeloChar[i];
		}

		if ( i == modelo.length() - 1 ){
			if ( aux != "" ) retorno.add(aux);
		}
	}
	return retorno;
    }
  	void adicionarEstados ( String conjuntoDeEstados )
        {
	      this.estados = quebrar(conjuntoDeEstados);
        }

        void adicionarEstadosFinais ( String conjuntoDeEstadosFinais )
        {
               this.estadosfinais = quebrar(conjuntoDeEstadosFinais);
        }

        void adicionarSimbolos ( String conjuntoDeSimbolos )
        {
                this.terminais = quebrar(conjuntoDeSimbolos);
        }

        void setEstadoInicial ( String estadoInicial )
        {
                this.estadoInicial = estadoInicial;
                this.estadoparaconversao.add(estadoInicial);
        }

        void adicionarTransicao ( String inicio, String fim, String simbolo ){
                if ( matriz[getIndiceEstado(inicio)][getIndiceSimbolo(simbolo)] == null || matriz[getIndiceEstado(inicio)][getIndiceSimbolo(simbolo)].length()== 0 ){
                        matriz[getIndiceEstado(inicio)][getIndiceSimbolo(simbolo)] = fim;
                } else {
                        matriz[getIndiceEstado(inicio)][getIndiceSimbolo(simbolo)] += ", " + fim;
                }
        }

        int getIndiceEstado ( String estado ){
                for(int i=0;i<estados.size();i++){
                        if ( estados.get(i).equals(estado) ) return i;
                }
                return 0;
        }
        int getIndiceSimbolo ( String simbolo ){
                for(int i=0;i<terminais.size();i++){
                        if ( terminais.get(i).equals(simbolo)) return i;
                }
                return 0;
        }

        String pegarPossibilidades ( String estado ){
                Map <String, Integer> mapa = new HashMap <String,Integer>();;
                String possiveis = null;
                mapa =  possibilidades(mapa,estado); 
                 int i = 1;

                 for (String key : mapa.keySet()) {
                    
                        if ( i == 1 ) 
                            possiveis = key;
                        else 
                            possiveis = possiveis + ", " + key; 
                    
                      i++;    
                }
                return possiveis;
        }

        Map <String,Integer> possibilidades ( Map <String,Integer>  mapa, String estado ){
                ArrayList <String> estadosAlcancaveis = quebrar(estado);
                Integer valor = null;
                for(int i=0;i<estadosAlcancaveis.size();i++){
                         valor  = mapa.get(estadosAlcancaveis.get(i));
                        if ( valor == mapa.get(mapa.size())){
                                mapa.put(estadosAlcancaveis.get(i),1);
                           mapa = possibilidades(mapa,matriz[getIndiceEstado(estadosAlcancaveis.get(i))][getIndiceSimbolo("*")]);
                        }
                }
                return mapa;
        }

        void imprimirMatriz(){
            for(int i=0;i<estados.size();i++){
                for(int j=0;j<terminais.size();j++){
                        System.out.println(estados.get(i) + "-" + terminais.get(j) + " = " + matriz[getIndiceEstado(estados.get(i))][getIndiceSimbolo(terminais.get(j))]+" ") ;
                        }
                        System.out.println("");
                }
        }

        String distinct ( String todosestados ){
                Map <String,Integer> mapa = new HashMap <String,Integer>();;
                Map <String,Integer> it = new HashMap <String,Integer>();
                Integer valor = null;
                String novovalor = null;
                int l = 1;  

                ArrayList <String> valores = quebrar(todosestados);
                for(int i=0;i<valores.size();i++){
                        valor = mapa.get(valores.get((i)));
                        if ( valor == mapa.get(mapa.size()) && !valores.get(i).equals("null") ){
                                mapa.put(valores.get(i),1);
                           
                        }
                }
                it = mapa;
               
                for (String key : it.keySet()) {
                  
                        if ( l== 1)
                             novovalor = key;
                        else
                        novovalor = novovalor + ", "+ key;
                        
                      l++; 
                }

                return novovalor;
        }

        String adjacentes ( String estado, String simbolo ){
                String nova = matriz[getIndiceEstado(estado)][getIndiceSimbolo(simbolo)];
                nova += "," + pegarPossibilidades(nova);
                return nova;
        }

        String fatiar ( String estado, String simbolo ){
                ArrayList <String> novo = quebrar(estado);
                String retorno = null;

                for(int i=0;i<novo.size();i++){
                   if(i == 0 &&  novo.get(i) != null)
                        retorno = adjacentes(novo.get(i),simbolo); 
                    else if( novo.get(i) != null)
                        retorno += ", " + adjacentes(novo.get(i),simbolo);    
                }
                retorno = distinct(retorno);
                
                return retorno;
        }

        String verificaEstado ( String estado ){
                ArrayList <String> picotado = quebrar(estado);
                String retorno = "";

                if ( picotado.size() == 1 && picotado.get(0).equals(estadoInicial) ) retorno += "->";

                boolean fim = false;
                for(int i=0;i<picotado.size();i++){
                        for(int j=0;j<estadosfinais.size();j++){
                                if ( picotado.get(i).equals(estadosfinais.get(j))) fim = true;
                        }
                }
                if ( fim == true ) return (retorno + "*");
                return retorno;
        }

        void converter () {
                Map < String, Integer > mapa = new HashMap <String,Integer>();
                Integer it = null ;

                while ( !estadoparaconversao.isEmpty() ){
                        String estado = estadoparaconversao.poll();
                      
                        for(int i=0;i<terminais.size();i++){
                                if ( !terminais.get(i).equals("*") ){
                                        String fim = fatiar(estado,terminais.get(i));
                                        String query = verificaEstado(estado) + "{" + estado + "} (" + terminais.get(i) + ") = {" + fim + "}";
                                        it = mapa.get(query);
                                        if ( it == mapa.get(mapa.size()) && estado != "" && fim != null){
                                                estadoparaconversao.add(fim);
                                                mapa.put(query,1);
                                        }
                                }
                        }
                }
                    //   for (String key : mapa.keySet()) {
                    //    System.out.println(key);
             //   }
          
                traducao(mapa);
        }
        
        void traducao(Map < String, Integer > mapa){
            String matrizTraduzida[][] = new String[mapa.size()][qtdsimbolos-1];
            String[] estadosTraducao = null;
            int i = 0;
            int l = 0;
            int numEstados = 0 ;
            
            for (String key : mapa.keySet()) {
                estadosTraducao =  key.split(("="));
            
           
              matrizTraduzida[i][l] = estadosTraducao[numEstados];
              l++;
              matrizTraduzida[i][l] = estadosTraducao[numEstados+1];              
              
              l = 0;
              i++;
              
        } //  impressaoAFD(matrizTraduzida);
            FinalTraducao(matrizTraduzida);
 }  

    void FinalTraducao(String[][] matrizTraduzida){
        String est = null;
        String aux = null;
        int valor = 0;
        
        for(int i = 0; i < matrizTraduzida.length; i++){    
                if(matrizTraduzida[i][0].substring(0,1).equals("*"))
                    est = "A" + valor + "*";
                else if(matrizTraduzida[i][0].substring(0,2).equals("->"))
                    est = "A" + valor + "#";
                else
                     est = "A" + valor;
                
                if(matrizTraduzida[i][0].length() > 5 && !matrizTraduzida[i][0].substring(0,1).replaceAll(" ","").equals("A") ){
                     aux = matrizTraduzida[i][0].substring(0, matrizTraduzida[i][0].length() - 5).replaceAll(" ","").replaceAll("\\*", "").replaceAll("->", "");
    
                        for(int l = 0; l < matrizTraduzida.length; l++){
                            if(matrizTraduzida[l][0].length() >5 &&  aux.equals(matrizTraduzida[l][0].replaceAll("\\*", "").replaceAll("->", "").substring(0, matrizTraduzida[l][0].replaceAll("->", "").length() - 5).replaceAll(" ",""))) 
                                matrizTraduzida[l][0] = est +" - "+ matrizTraduzida[l][0].replaceAll("\\*", "").replaceAll("->", "").substring(matrizTraduzida[l][0].replaceAll("\\*", "").replaceAll("->", "").length() - 5);


                            for(int j = 0; j < matrizTraduzida[0].length; j++){
                                if (aux.equals(matrizTraduzida[l][j].replaceAll("\\*", "").replaceAll("->", "").replaceAll(" ","")))
                                   matrizTraduzida[l][j] = est;
                            }       
                        }
           
                 
                    valor++;
                }
        }
       // impressaoAFD(matrizTraduzida);
        FinalTraducao2(matrizTraduzida);
    }
    
    
    void FinalTraducao2(String[][] matrizTraduzida){
        String matrizCompleta[][] = new String[matrizTraduzida.length][qtdsimbolos];
        String est = null;
        String aux = null;
        int valor = 0;
            
        
        for(int i = 0; i < matrizTraduzida.length; i++){
            if(!matrizTraduzida[i][0].equals("OK")){
            aux = matrizTraduzida[i][0].substring(0, matrizTraduzida[i][0].length() - 7).replaceAll(" ","");
            matrizCompleta[valor][0] = aux;
                if(matrizTraduzida[i][0].substring(matrizTraduzida[i][0].length() - 5).replaceAll(" ", "").equals("(0)"))
                    matrizCompleta[valor][1] = matrizTraduzida[i][1];
                
                else if(matrizTraduzida[i][0].substring(matrizTraduzida[i][0].length() - 5).replaceAll(" ", "").equals("(1)"))
                    matrizCompleta[valor][2] = matrizTraduzida[i][1];
                
                matrizTraduzida[i][0] = "OK";
                
            for(int l = i+1 ; l < matrizTraduzida.length; l++){
                if(!matrizTraduzida[l][0].equals("OK") && aux.equals(matrizTraduzida[l][0].substring(0, matrizTraduzida[l][0].length() - 7).replaceAll(" ",""))){
                matrizCompleta[valor][0] = aux;
                 
                
                if(matrizTraduzida[l][0].substring(matrizTraduzida[l][0].length() - 5).replaceAll(" ", "").equals("(0)"))
                    matrizCompleta[valor][1] = matrizTraduzida[l][1];
                
                else if(matrizTraduzida[l][0].substring(matrizTraduzida[l][0].length() - 5).replaceAll(" ", "").equals("(1)"))
                    matrizCompleta[valor][2] = matrizTraduzida[l][1];
                       
                matrizTraduzida[l][0] = "OK";
                l  = matrizTraduzida.length;
                }
            }
            valor++;
         }
            
        }
        
       impressaoAFD(matrizCompleta); 
    }

        void impressaoAFD(String[][] matrizTraduzida){
        String est = null;
        String aux = null;
        int valor = 0;
        
        System.out.println("   |  0  |  1  |");
        
        for(int i = 0; i < matrizTraduzida.length; i++)
        {    
                if(matrizTraduzida[i][0] != null)
                for(int l = 0; l < matrizTraduzida[0].length; l++)
                {
                    System.out.print(matrizTraduzida[i][l]+ " | ");        
                }
           System.out.println("");
                 
                
        }
    }
    
    
    
    
    

        public static void main(String[] args) {
         
                ConversaoAFNDparaAFD automato = new ConversaoAFNDparaAFD();

            
                automato.adicionarEstados("q1,q2,q3");
                automato.setEstadoInicial("q1");
                automato.adicionarEstadosFinais("q3");
                automato.adicionarSimbolos("0,1,*");

                automato.adicionarTransicao("q1","q1","1");
                automato.adicionarTransicao("q1","q1","0");
                automato.adicionarTransicao("q1","q2","1");
                automato.adicionarTransicao("q2","q3","0");

                

                automato.converter();
             
             
	 }	 
}