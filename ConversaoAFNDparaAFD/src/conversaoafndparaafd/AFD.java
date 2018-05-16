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
import javafx.util.Pair;



/**
 *
 * @author Diego
 */


public class AFD implements Cortar{
       //variaveis globais 
        int qtdestados = 4;
        int qtdsimbolos = 3;

        String estadoInicial;
        ArrayList <String> estados;
        ArrayList <String> terminais;
        ArrayList <String> estadosfinais; 
        Map <String,Integer> it = new HashMap <String,Integer>(); // :: iterator it;

        String matriz[][]= new String [qtdestados][qtdsimbolos];

        ArrayList < String > AFD;
        PriorityQueue < String > estadoparaconversao;

    @Override
    public ArrayList<String> picotar(String modelo) {
        ArrayList<String> retorno = null;
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
  	void addEstados ( String conjuntoDeEstados )
        {
	      this.estados = picotar(conjuntoDeEstados);
        }

        void addEstadosFinais ( String conjuntoDeEstadosFinais )
        {
               this.estadosfinais = picotar(conjuntoDeEstadosFinais);
        }

        void addSimbolos ( String conjuntoDeSimbolos )
        {
                this.terminais = picotar(conjuntoDeSimbolos);
        }

        void setEstadoInicial ( String estadoInicial )
        {
                this.estadoInicial = estadoInicial;
                this.estadoparaconversao.add(estadoInicial);
        }

        void addTransicao ( String inicio, String fim, String simbolo ){
                if ( matriz[getIndiceEstado(inicio)][getIndiceSimbolo(simbolo)].length()== 0 ){
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
                possibilidades(mapa,estado); 

                 for (String key : this.it.keySet()) {
              //  for(it = mapa.;it != mapa.end();it++){
                        if ( it.get(1) != mapa.get(key) ) possiveis += ", ";
                        possiveis += it.get(key);
                        
                }
                return possiveis;
        }

        void possibilidades ( Map <String,Integer>  mapa, String estado ){
                ArrayList <String> estadosAlcancaveis = picotar(estado);
                Integer valor = null;
                for(int i=0;i<estadosAlcancaveis.size();i++){
                         valor  = mapa.get(estadosAlcancaveis.get(i));
                        if ( valor == mapa.get(mapa.size())){
                                mapa.put(estadosAlcancaveis.get(i),1);
                                possibilidades(mapa,matriz[getIndiceEstado(estadosAlcancaveis.get(i))][getIndiceSimbolo("*")]);
                        }
                }
        }

        void imprimirMatriz(){
            for(int i=0;i<estados.size();i++){
                for(int j=0;j<terminais.size();j++){
                        System.out.println(estados.get(i) + "-" + terminais.get(j) + " = " + matriz[getIndiceEstado(estados.get(i))][getIndiceSimbolo(terminais.get(j))]+" ") ;
                        }
                        System.out.println("");
                }
        }
//TRATAR INTERATOR
        String distinct ( String todosestados ){
                Map <String,Integer> mapa = null;
                Map <String,Integer> it = new HashMap <String,Integer>();
                Integer valor = null;
                String novovalor = null;                

                ArrayList <String> valores = picotar(todosestados);
                for(int i=0;i<valores.size();i++){
                        valor = mapa.get(valores.get((i)));
                        if ( valor == mapa.get(mapa.size())){
                                mapa.put(valores.get(i),1);
                           
                        }
                }
                for (String key : it.keySet()) {
               // for(it = mapa.begin();it != mapa.end();it++){
                        if ( it.get(key) != mapa.get(1)) novovalor += ", ";
                        novovalor += it.get(1);
                }

                return novovalor;
        }

        String adjacentes ( String estado, String simbolo ){
                String nova = matriz[getIndiceEstado(estado)][getIndiceSimbolo(simbolo)];
                nova += "," + pegarPossibilidades(nova);
                return nova;
        }

        String fatiar ( String estado, String simbolo ){
                ArrayList <String> novo = picotar(estado);
                String retorno = null;
                for(int i=0;i<novo.size();i++){
                        retorno += adjacentes(novo.get(i),simbolo) + ", "; 
                }
                retorno = distinct(retorno);
                return retorno;
        }

        String verificaEstado ( String estado ){
                ArrayList <String> picotado = picotar(estado);
                String retorno = "";

                if ( picotado.size() == 1 && picotado.get(0) == estadoInicial ) retorno += "->";

                boolean fim = false;
                for(int i=0;i<picotado.size();i++){
                        for(int j=0;j<estadosfinais.size();j++){
                                if ( picotado.get(i) == estadosfinais.get(j)) fim = true;
                        }
                }
                if ( fim == true ) return (retorno + "*");
                return retorno;
        }

        void converter () {
                Map < String, Integer > mapa = null;
                Integer it = null ;

                while ( !estadoparaconversao.isEmpty() ){
                        String estado = estadoparaconversao.poll();
                      
                        for(int i=0;i<terminais.size();i++){
                                if ( terminais.get(i) != "*" ){
                                        String fim = fatiar(estado,terminais.get(i));
                                        String query = verificaEstado(estado) + "{ " + estado + " } (" + terminais.get(i) + ") = {" + fim + "}";
                                        it = mapa.get(query);
                                        if ( it == mapa.get(mapa.size()) && estado != "" ){
                                                estadoparaconversao.add(fim);
                                                mapa.put(query,1);
                                        }
                                }
                        }
                }
                for(it = mapa.get(1); it!= mapa.get(mapa.size());it++){
                        System.out.println(mapa.get(1) );
                }
        }  



        public static void main(String[] args) {
         
                AFD automato = new AFD();

                automato.addEstados("q1,q2,q3,q4");
                automato.setEstadoInicial("q1");
                automato.addEstadosFinais("q4");
                automato.addSimbolos("0,1,*");

                automato.addTransicao("q1","q1","0");
                automato.addTransicao("q1","q1","1");
                automato.addTransicao("q1","q2","1");
                automato.addTransicao("q2","q3","0");
                automato.addTransicao("q2","q3","*");
                automato.addTransicao("q3","q4","1");
                automato.addTransicao("q4","q4","0");
                automato.addTransicao("q4","q4","1");

                automato.converter();
             
             
	 }	 
}