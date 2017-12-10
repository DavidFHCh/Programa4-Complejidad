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
    protected int[][] matriz;
    private Graph grafica;
    protected ArrayList<Vertice> vertices;
    private final double alpha = 0.9;
    private final double beta = 0.9;
    private final double gamma = 0.9;
    private final int nCiclos = 100;
    private final int nAnts = 20;
    private final int nMovimientos = 1000;
    private int conflictoTotal;

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
        private int conflicto;
        private ArrayList<Vertices> vecinos;
        private ColoringAnts ca = new ColoringAnts();

        public Vertice(int id, int color, ColoringAnts ca) {
            this.id = id;
            this.color = color;
            grado = 0;
            conflicto = 0;
            this.ca = ca;
            vecinos();
        }

        public Vertice(int id, int color, int grado,int conflicto,ColoringAnts ca) {
            this.id = id;
            this.color = color;
            this.grado = grado;
            this.conflicto = conflicto;
            this.ca = ca;
            vecinos();
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

        public int conflicto() {
            return conflicto;
        }

        public ArrayList<Vertice> getVecinos(){
            return vecinos;
        }

        public void setVecinos(ArrayList<Vertice> vecinos) {
            this.vecinos = vecinos;
        }

        public void setGrado(int grado) {
            this.grado = grado;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public void setConflicto(int conflicto) {
            this.conflicto = conflicto;
        }

        public Vertice clona(){
            return new Vertice(id,color,grado,conflicto);
        }

        private void vecinos() {
            vecinos = new ArrayList<>();
            for(int vec = 0; vec < ca.matriz[id].size; vec++) {
                if(matriz[id][vec] != 0) {
                    for(Vertice v1: vertices) {
                        if(v1.getId() == vec) {
                            vecinos.add(v1);
                        }
                    }
                }
            }
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
        for (Vertice v: aClonar) {
            Vertice v1 = new Vertice(v.getId(), v.getColor(), v.getGrado(),v.getConflicto());
            clon.add(v1);
        }

        return clon;
    }

    private int coloreaInicial() {
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

        return k-1;
    }

    private void conflictoVertice(Vertice v) {
        int id = v.getId();
        int conflicto = 0;
        for(int vec = 0; vec < matriz[id].size; vec++) {
            if(matriz[id][vec] != 0) {
                for(Vertice v1: vertices) {
                    if(v1.getId() == vec) {
                        if(v1.getColor() == v.getColor())
                            conflicto++;
                    }
                }
            }
        }
        v.setConflicto(conflicto);
    }

    private void conflictoGeneral() {
        conflictoTotal = 0;
        for(Vertice v: vertices) {
            conflictoTotal += v.getConflicto();
        }
    }

    private class Ant {

        private Vertice actual;
        private ArrayList<Vertices> tabu;
        private ColoringAnts ca = new ColoringAnts();

        public Ant(Vertice actual, ColoringAnts ca) {
            this.actual = actual;
            tabu = new ArrayList<>();
            this.ca = ca;
        }

        public int getActual(){
            return actual;
        }

        public void setActual(int actual) {
            this.actual = actual;
        }

        public void agregaTabu(Vertice v){
            if(!tabu.contains(v))
                tabu.add(v);
        }

        public void coloreaVertice(Vertice v, int coloresDisponibles){
            Vertice v1 = v.clona();
            conflictoMin = v1.getConflicto();
            colorMin = 0;
            for (int color = 1; color < coloresDisponibles; color++) {
                v1.setColor(color);
                ca.conflictoVertice(v1);
                if(v1.getConflicto() < conflictoMin) {
                    colorMin = color;
                    conflictoMin = v1.getConflicto();
                }
            }
            v.setColor(colorMin);
            v.setConflicto(conflictoMin);
            actualizarConflictoVecinos(v);
            agregaTabu(v);
        }

        public void moverAnt(){
            ArrayList<Vertice> vecinos = actual.getVecinos();
            Vertice dest = actual;
            while(tabu.contains(dest)) {
                int random = ca.rand.nextInt(vecinos.size());
                Vertice randv = vecinos.get(random);
                ArrayList<Vertice> vecinos1 = actual.getVecinos();
                int conflictoMax = 0;
                for(Vertice v: vecinos1){
                    if(v.getConflicto() > conflictoMax) {
                        conflictoMax = v.getConflicto():
                        dest = v;
                    }
                }
            }
        }

        private void actualizarConflictoVecinos(Vertice v){
            int id = v.getId();
            for(int vec = 0; vec < ca.matriz[id].size; vec++) {
                if(matriz[id][vec] != 0) {
                    for(Vertice v1: vertices) {
                        if(v1.getId() == vec) {
                            ca.conflictoVertice(v1);
                        }
                    }
                }
            }
        }

    }



    public static void main(String[] args) {
        ColoringAnts ca = new ColoringAnts();
        int k = ca.coloreaInicial();
        int mejorCantidadColores = k;
        ArrayList<Vertice> mejorColoracion = ca.clona(vertices);
        int coloresDisponibles = (int)((alpha * ((double))k) + 1.0);
        int aModificar = (int)((beta * ((double))k) + 1.0);
        int nuevaCantidadColores = (int)((gamma * ((double))k) + 1.0);
        ArrayList<Integer> elegidos = new ArrayList<>();
        int index = 1;
        while (index <= aModificar) {
            int random = ca.rand.nextInt(aModificar+1);
            while (elegidos.contains(random)) {
                random = ca.rand.nextInt(aModificar+1);
            }
            elegidos.add(random);
        }
        elegidos.sort();
        ArrayList<Integers> noElegidos = new ArrayList<>();
        for (int i = 0; i < nuevaCantidadColores; i++){
            if (!elegidos.contains(i))
                noElegidos.add(i);
        }

        if(!noElegidos.isEmpty()) {
            for(Vertice v: vertices) {
                if(elegidos.contains(v.getColor())) {
                    int newColor = elegidos.size()+rand.nextInt(noElegidos.size()) + 1;
                    v.setColor(newColor);
                }
            }
        }
        for(Vertice v: vertices) {
            if(elegidos.contains(v.getColor())) {
                int newColor = elegidos.indexOf(v.getColor()) + 1;
                v.setColor(newColor);
            }
        }
        //termina la recoloracion despues del coloreoInicial
        for(Vertice v: vertices) {
            ca.conflictoVertice(v);
        }
        ca.conflictoTotal();

        //empieza coso de hormigas


        for (int ciclo = 0; ciclo < nCiclos; ciclo++) {
            for (int ant = 0; ant < nAnts; ant++) {
                for (int move = 0; move < nMovimientos; move++) {

                }
            }
        }
    }
}
