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



/**
 *
 * @author Diego
 */


public class AFD {
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
}
