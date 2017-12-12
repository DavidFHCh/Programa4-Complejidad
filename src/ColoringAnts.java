/**
 * Cuarto programa de Complejidad Computacional 2018-1
 * @author David Felipe Hernandez Chiapa
 */

 import java.util.Random;
 import java.util.ArrayList;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class ColoringAnts {

    private Random rand;
    private int numVertice;
    protected int[][] matriz;
    private Graph grafica;
    protected ArrayList<Vertice> vertices;
    private final double alpha = 0.8;
    private final double beta = 0.5;
    private final double gamma = 0.7;
    private final int nCiclos = 100;
    private final int nAnts = 20;
    private final int nMovimientos = 2;
    private int conflictoTotal;

    public ColoringAnts() {
        grafica = new SingleGraph("k-Coloracion.");
        rand = new Random();
        numVertice = rand.nextInt(11);
        vertices = new ArrayList<>();
        if(numVertice < 5){//asegurando que la grafica tenga al menos 5 vertices.
            numVertice = 5;
        }
        for (int i = 0; i < numVertice; i++) {
            grafica.addNode(Integer.toString(i));
            Vertice v = new Vertice(i,0,this);
            vertices.add(v);
        }
        //Llenado de matriz de adyacencias.
        //Aleatoriamente.
        matriz = new int[numVertice][numVertice];
        for(int i = 0; i < numVertice; i++)
            for(int j = i; j < numVertice; j++)
                if(i == j){
                    matriz[i][j] = 0;
                }else{
                    if(rand.nextDouble() < 0.5){
                        matriz[i][j] = 1;
                        grafica.addEdge(i+"-"+j,i,j);
                    }else{
                        matriz[i][j] = 0;
                    }
                }


        for(int i = 0; i < numVertice; i++)
            for(int j = 0; j < numVertice; j++){
                matriz[j][i] = matriz[i][j];
            }
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

        for (Vertice v: vertices) {
            v.vecinos();
        }
        //System.out.println("fin");
    }

    private void jolt(int availableColors){
        int jolteo = (int)(((double)vertices.size()*0.10) + 1.0);
        ArrayList<Vertice> aJoltear = new ArrayList<>();
        ArrayList<Vertice> copia = clona(vertices);
        int maxCon = 0;
        for(int i = 0; i < jolteo; i++) {
            Vertice max = null;
            for(Vertice v: copia) {
                if(v.getConflicto() > maxCon) {
                    maxCon = v.getConflicto();
                    max = v;
                }
            }
        aJoltear.add(max);
        copia.remove(max);
        }
        ArrayList<Integer> colores80 = new ArrayList<>();
        while (colores80.size() < (availableColors/8)){
            int random = rand.nextInt(availableColors) + 1;
            if(!colores80.contains(random)) {
                colores80.add(random);
            }
        }

        for(Vertice v: aJoltear) {
            for(Vertice v1: v.getVecinos()) {
                int random = rand.nextInt(colores80.size()) + 1;
                v1.setColor(random);
            }
        }
    }


    private int checkColorsUsed(){
        ArrayList<Integer> colors = new ArrayList<>();
        for (Vertice v: vertices) {
            if(!colors.contains(v.getColor()))
                colors.add(v.getColor());
        }
        return colors.size();
    }

    private class Vertice {

        private int id;
        private int color;
        private int grado;
        private int conflicto;
        private ArrayList<Vertice> vecinos;
        private ColoringAnts ca;

        public Vertice(int id, int color, ColoringAnts ca) {
            this.id = id;
            this.color = color;
            grado = 0;
            conflicto = 0;
            this.ca = ca;
            //vecinos();
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

        public int getConflicto() {
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
            return new Vertice(id,color,grado,conflicto,ca);
        }

        public void vecinos() {
            vecinos = new ArrayList<>();
            for(int vec = 0; vec < ca.matriz[id].length; vec++) {
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
                ArrayList<Vertice> vecinos = v.getVecinos();

                boolean todos = true;
                for(Vertice vec: vecinos) {
                    if(!p) {
                        if(vec.getColor() == color){
                            pr.add(v);
                            break;
                        }
                    } else {
                        if(vec.getColor() == color){
                            todos = false;
                            break;
                        }
                    }
                }
                if (p) {

                    if (todos) {
                        pr.add(v);
                        //System.out.println("neta");

                    }
                }
            }
        }
        //System.out.println(pr.size() + " " + p);
        return pr;
    }

    private ArrayList<Vertice> ordenaGrado(ArrayList<Vertice> noOrdenada) {
        //System.out.println("ordenando " + noOrdenada.isEmpty());
        ArrayList<Vertice> ordenada = new ArrayList<>();

        while(!noOrdenada.isEmpty()) {
            Vertice v = noOrdenada.get(0);
            for(Vertice v1: noOrdenada) {
                if(v1.getGrado() < v.getGrado())
                    v = v1;
            }
            noOrdenada.remove(v);
            ordenada.add(v);
        }
        return ordenada;
    }

    private boolean haySinColor() {
        for (Vertice v: vertices) {
            if (v.getColor() == 0) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Vertice> clona(ArrayList<Vertice> aClonar) {
        ArrayList<Vertice> clon = new ArrayList<>();
        for (Vertice v: aClonar) {
            Vertice v1 = new Vertice(v.getId(), v.getColor(), v.getGrado(),v.getConflicto(),this);
            clon.add(v1);
        }

        return clon;
    }

    private int coloreaInicial() {
        int k = 1;

        while(haySinColor()) {
            ArrayList<Vertice> p = ordenaGrado(createPorR(k,true));
            ArrayList<Vertice> r = createPorR(k,false);
            //System.out.println("coloreando " + p.isEmpty());
            while (!p.isEmpty()) {
                Vertice v = p.remove(p.size()-1);
                v.setColor(k);
                //System.out.println("coloreando " + haySinColor()+ " " + k);
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
        for(int vec = 0; vec < matriz[id].length; vec++) {
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
        private ArrayList<Vertice> tabu;
        private ColoringAnts ca = new ColoringAnts();

        public Ant(Vertice actual, ColoringAnts ca) {
            this.actual = actual;
            tabu = new ArrayList<>();
            this.ca = ca;
        }

        public Vertice getActual(){
            return actual;
        }

        public void setActual(Vertice actual) {
            this.actual = actual;
        }

        public void agregaTabu(Vertice v){
            if(!tabu.contains(v))
                tabu.add(v);
        }

        public void coloreaVertice(Vertice v, int coloresDisponibles){
            Vertice v1 = v.clona();
            int conflictoMin = v1.getConflicto();
            int colorMin = 0;
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
            //System.out.println(vecinos.size() + " tam vecinos");
            Vertice dest = actual;
            int attempts = 0;

            if(vecinos.size() == 0) {
                return;
            }

            while(tabu.contains(dest)) {
                int random = ca.rand.nextInt(vecinos.size());
                Vertice randv = vecinos.get(random);
                ArrayList<Vertice> vecinos1 = randv.getVecinos();
                int conflictoMax = 0;
                for(Vertice v: vecinos1){
                    if(v.getConflicto() > conflictoMax) {
                        conflictoMax = v.getConflicto();
                        dest = v;
                    }
                }
                attempts++;
                if(attempts == 10) {
                    break;
                }
            }
            actual = dest;
            actual.vecinos();
        }

        private void actualizarConflictoVecinos(Vertice v){
            int id = v.getId();
            for(int vec = 0; vec < ca.matriz[id].length; vec++) {
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

    private ArrayList<Ant> creaAnts() {
        ArrayList<Ant> ants = new ArrayList<>();
        for(int ant = 0; ant < nAnts; ant++) {
            int random = rand.nextInt(vertices.size());
            Ant a1 = new Ant(vertices.get(random),this);
            ants.add(a1);
        }
        return ants;
    }



    public static void main(String[] args) {
        ColoringAnts ca = new ColoringAnts();
        int k = ca.coloreaInicial();

        int mejorCantidadColores = k;
        ArrayList<Vertice> mejorColoracion = ca.clona(ca.vertices);
        int coloresDisponibles = (int)((ca.alpha * ((double)k) + 1.0));
        int aModificar = (int)((ca.beta * ((double)k) + 1.0));
        int nuevaCantidadColores = (int)((ca.gamma * ((double)k) + 1.0));
        ArrayList<Integer> elegidos = new ArrayList<>();
        int index = 1;

        while (index <= aModificar) {
            int random = ca.rand.nextInt(aModificar+1);
            while (elegidos.contains(random)) {
                random = ca.rand.nextInt(aModificar+1);
            }
            elegidos.add(random);
            index++;
        }
        //System.out.println("empieza");
        ArrayList<Integer> noElegidos = new ArrayList<>();
        for (int i = 0; i < nuevaCantidadColores; i++){
            if (!elegidos.contains(i))
                noElegidos.add(i);
        }

        if(!noElegidos.isEmpty()) {
            for(Vertice v: ca.vertices) {
                if(elegidos.contains(v.getColor())) {
                    int newColor = elegidos.size() + ca.rand.nextInt(noElegidos.size()) + 1;
                    v.setColor(newColor);
                }
            }
        }
        for(Vertice v: ca.vertices) {
            if(elegidos.contains(v.getColor())) {
                int newColor = elegidos.indexOf(v.getColor()) + 1;
                v.setColor(newColor);
            }
        }
        //termina la recoloracion despues del coloreoInicial
        for(Vertice v: ca.vertices) {
            ca.conflictoVertice(v);
        }
        ca.conflictoGeneral();

        //empieza coso de hormigas
        ArrayList<Ant> ants = ca.creaAnts();

        int contadorAvCol = 0;
        int contadorJolt = 0;
        int contadorBreak = 0;
        for (int ciclo = 0; ciclo < ca.nCiclos; ciclo++) {
            for (int ant = 0; ant < ca.nAnts; ant++) {
                Ant ant1 = ants.get(ant);
                for (int move = 0; move < ca.nMovimientos; move++) {
                    int random = ca.rand.nextInt(ca.vertices.size());

                    Vertice v2 = ca.vertices.get(random);
                    ant1.coloreaVertice(v2,coloresDisponibles);
                    //System.out.println(move + " move");
                    ant1.moverAnt();
                    //System.out.println("entra?");

                }
                    //System.out.println(ant + " ant " + ca.nAnts);
            }
            //System.out.println("ciclo " + ciclo);
            ca.conflictoGeneral();
            int k1 = ca.checkColorsUsed();


            //terminan las hormigas
            if(mejorCantidadColores < k1){
                contadorBreak = 0;
            }else{
                contadorBreak++;
            }
            if(ca.conflictoTotal == 0 && mejorCantidadColores < k1){
                coloresDisponibles--;
                contadorAvCol = 0;
                contadorJolt = 0;

                mejorCantidadColores = k1;
                mejorColoracion = ca.clona(ca.vertices);
            } else {
                contadorAvCol++;
                contadorJolt++;
            }

            if(contadorAvCol == 20) {
                coloresDisponibles++;
                contadorAvCol = 0;
            }

            if(contadorJolt == 600) {
                coloresDisponibles++;
                contadorJolt = 0;
                ca.jolt(coloresDisponibles);
            }
            //System.out.println(ciclo);
            if(contadorBreak > 1000) {
                break;
            }
        }

        //regresa mejorColoracion
        for(Vertice v: mejorColoracion) {
            int ver = v.getId();
            Node n = ca.grafica.getNode(Integer.toString(ver));
            n.addAttribute("ui.label", v.getColor());
        }

        ca.grafica.display();
    }
}
