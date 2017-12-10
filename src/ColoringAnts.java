/**
 * Cuarto programa de Complejidad Computacional 2018-1
 * @author David Felipe Hernandez Chiapa
 */

 import java.util.Random;
 import java.util.ArrayList;

import org.graphstream.graph.*;

public class ColoringAnts {

    private Random rand;
    private int numVertices;
    private int[][] matriz;
    private Graph grafica;
    private ArrayList<Vertice> vertices;

    public ColoringAnts() {
        grafica = new SingleGraph("k-Coloracion.");
        rand = new Random();
        numVertices = rand.nextInt();
        vertices = new ArrayList<>();
        if(numVertices < 5){//asegurando que la grafica tenga al menos 5 vertices.
            numVertices = 5;
        }
        for (int i = 0; i < numVertices; i++) {
            grafica.addNode(Integer.toString(i));
            Vertice v = new Vertice(i,0);
            vertices.add(v);
        }

        //Llenado de matriz de adyacencias.
        //Aleatoriamente.
        matriz = new int[numVertices][numVertices];
        for(int i = 0; i < numVertices; i++)
            for(int j = i; j < numVertices; j++)
                if(i == j){
                    matriz[i][j] = 0;
                }else{
                    if(rand.nextGaussian() < 0.6){
                        matriz[i][j] = 1;
                        g.addEdge(i+"-"+j,i,j);
                    }else{
                        matriz[i][j] = 0;
                    }
                }
        for(int i = 0; i < numVertices; i++)
            for(int j = 0; j < numVertices; j++)
                matriz[j][i] = matriz[i][j];

        for (Vertice v: vertices) {
            int id = v.getId();
            int grado = 0;
            for (int vec: matriz[id]) {
                if(vec != 0) {
                    grado++;
                }
            }
            v.setGrado(grado);
        }

    }

    private class Vertice {

        private int id;
        private int color;
        private int grado;

        public Vertice(int id, int color) {
            this.id = id;
            this.color = color;
        }

        public int getId() {
            return id;
        }

        public int getColor() {
            return color;
        }

        public int getGrado() {
            return grado;
        }

        public void setGrado(int grado) {
            this.grado = grado;
        }

        public void setColor(int Color) {
            this.color = color;
        }
    }

    private ArrayList<Vertice> createPorR(int color,boolean p) {
        ArrayList<Vertice> pr = new ArrayList<>();
        for(Vertice v: vertices) {
            if(v.getColor() == 0) {//no tiene color
                int id = v.getId();
                for(int vec = 0; vec < matriz[id].size; vec++) {
                    if(matriz[id][vec] != 0) {//es adyacente
                        Vertice vecino = vertices.get(vec);
                        if(p) {
                            if(vecino.getColor() != color)
                                pr.add(vecino);
                        } else {
                            if(vecino.getColor() == color)
                                pr.add(vecino);
                        }
                    }
                }
            }
        }

        return pr;
    }

    private ArrayList<Vertice> ordenaGrado(ArrayList<Vertice> noOrdenada) {
        ArrayList<Vertice> ordenada = new ArrayList<>();
        ordenada.add(noOrdenada.remove(0));
        while (!noOrdenada.isEmpty()) {
            Vertice v = noOrdenada.remove(0);
            int index = 0;
            for(Vertice v1: ordenada) {
                if(v.getGrado() > v1.getGrado()) {
                    ordenada.add(index+1,v);
                    break;
                }
                index++;
            }
        }
        return ordenada;
    }

    private booelan haySinColor() {
        for (Vertice v: vertices) {
            if (v.getColor == 0) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Vertice> clona(ArrayList<Vertices> aClonar) {
        ArrayList<Vertice> clon = new ArrayList<>();

    }

    public int coloreaInicial() {
        int k = 1;

        while(haySinColor()) {
            ArrayList<Vertice> p = ordenaGrado(createPorR(k,true));
            ArrayList<Vertice> r = createPorR(k,false);

            while (!p.isEmpty()) {
                v = p.remove(p.size()-1);
                v.setColor(k);
                p = ordenaGrado(createPorR(k,true));
                r = createPorR(k,false);
            }
            k++;
        }

        return k;
    }


    public static void main(String[] args) {
        ColoringAnts ca = new ColoringAnts();
        int mejorCantidadColores = ca.coloreaInicial();
        ArrayList<Vertice> mejorColoracion = ca.clona(vertices);
    }
}
