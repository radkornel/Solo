/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Solo;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Kornél
 */
public class SoloFC {
    
    private int[][] megoldott;
    public int meret=9; //meretXmeret nagyságú tábla lesz - Ezt MINDIG át kell írni ha méret változás történt
    public int negyzetdb = (int)Math.sqrt(meret); //egy kis negyzetben ennyi elem található egy sorban/oszlopban
    List<Boolean[][]> EEList = new ArrayList<>();
    Boolean[][] sor = new Boolean[meret+1][meret+1];
    Boolean[][] oszlop = new Boolean[meret+1][meret+1];
    Boolean[][] negyzet = new Boolean[meret+1][meret+1];
    Boolean[] tmp = new Boolean[meret+1];
    public SoloFC(int[][] table) {
        this.megoldott = table;
        //Sorok tárolása
        for(int i=0; i<meret; i++){
            for(int j=0; j<=meret; j++){
                tmp[j]=false;
            }
            for(int j=0; j<meret; j++){
                if(table[i][j]!=0){
                    tmp[table[i][j]]=true;
                }
            }
            for(int j=0; j<=meret; j++){
                sor[i][j]=tmp[j];
            }
        }
        EEList.add(sor);
        //Oszlopok tárolása
        for(int i=0; i<meret; i++){
            for(int j=0; j<=meret; j++){
                tmp[j]=false;
            }
            for(int j=0; j<meret; j++){
                if(table[j][i]!=0){
                    tmp[table[j][i]]=true;
                }
            }
            for(int j=0; j<=meret; j++){
                oszlop[i][j]=tmp[j];
            }
        }
        EEList.add(oszlop);
        //Négyzetek tárolása
        int db=0; //A db a listában lévő 2D-s negyzet tömb idexe
        for(int i=0; i<negyzetdb; i++){
            int oszlopalap=0;
            for(int o=0; o<negyzetdb; o++){ //Ballról jobbra egy sorban végigmegyünk a pl. 3x3-as négyzeteken
                for(int l=0; l<=meret; l++){
                    tmp[l]=false;
                }
                for(int j=negyzetdb*i; j<negyzetdb*i+negyzetdb; j++){ //Az aktuális kis négyzet sorai
                    for(int k=oszlopalap; k<oszlopalap+negyzetdb; k++){ //Az aktuális kis négyzet oszlopai
                        if(table[j][k]!=0){
                            tmp[table[j][k]]=true;
                        }
                    }
                }
                oszlopalap+=negyzetdb;
                for(int j=0; j<=meret; j++){
                    negyzet[db][j]=tmp[j];
                }
                db++;
            }
            
        }
        EEList.add(negyzet);
    }

    public int[][] getMegoldott() {
        return megoldott;
    }

    public int getMeret() {
        return meret;
    }

    public int getNegyzetdb() {
        return negyzetdb;
    }

    public void setMegoldott(int[][] megoldott) {
        this.megoldott = megoldott;
    }

    public void setMeret(int meret) {
        this.meret = meret;
    }

    public void setNegyzetdb(int negyzetdb) {
        this.negyzetdb = negyzetdb;
    }

    
    private boolean sorCheck(int sor, int n){
        for(int i=0; i<meret; i++){
            if(megoldott[sor][i]==n){
                return false; //ha már van ilyen szám a sorban, akkor nem mehet ide még egyszer -> false visszatérés
            }
        }
        return true; //nem találtunk ugyanolyan számot, azaz jó megoldás -> true visszatérés
    }
    
    private boolean oszlopCheck(int oszlop, int n){
        for(int i=0; i<meret; i++){
            if(megoldott[i][oszlop]==n){
                return false; //ha már van ilyen szám az oszlopban, akkor nem mehet ide még egyszer -> false visszatérés
            }
        }
        return true; //nem találtunk ugyanolyan számot, azaz jó megoldás -> true visszatérés
    }
    
    private boolean negyzetCheck(int sor, int oszlop, int n){
        int negyzetSor;
        int negyzetOszlop;
        
        if(sor%negyzetdb!=0){
            negyzetSor=sor-sor%negyzetdb; //a negyzetSor lesz a kisnégyzetünk sorának kezdőindexe
        } else {
            negyzetSor=sor; //ha 0 a maradék, akkor már eleve ő a kezdőindex
        }
        
        if(oszlop%negyzetdb!=0){
            negyzetOszlop=oszlop-oszlop%negyzetdb; //a negyzetOszlop lesz a kisnégyzetünk oszlopának kezdőindexe
        } else {
            negyzetOszlop=oszlop; //ha 0 a maradék, akkor már eleve ő a kezdőindex
        }
        
        for(int i=negyzetSor; i<negyzetSor+negyzetdb; i++){
            for(int j=negyzetOszlop; j<negyzetOszlop+negyzetdb; j++){
                if(megoldott[i][j]==n){
                    return false; //ha már van ilyen szám a négyzetben, akkor nem mehet ide még egyszer -> false visszatérés
                }
            }
        }
        return true; //nem találtunk ugyanolyan számot, azaz jó megoldás -> true visszatérés
    }
 
    private boolean szamCheck(int sor, int oszlop, int n){
        if(sorCheck(sor, n) && oszlopCheck(oszlop, n) && negyzetCheck(sor, oszlop, n)){
            return true; //ha nem találtuk meg sehol -> true
        } else {
            return false; //ha már szerepel, akkor nem jó -> false
        }
    }
    
    public List<Integer> Beirhato(int sor, int oszlop){ //Egy listát létrehozunk, amiben elmentjük a beírható elemeket az adott négyzethez
        List<Integer> list = new ArrayList<>();
        int negyzetszam = sor/negyzetdb*negyzetdb + oszlop/negyzetdb; //A sor és oszlop alapján megállapítható, hogy melyik négyzetet kell nézni
        for(int i=1; i<=meret; i++){
            if(EEList.get(0)[sor][i]==false && EEList.get(1)[oszlop][i]==false && EEList.get(2)[negyzetszam][i]==false){ //Ha nincs a sorban/oszlopba és négyzetben, akkor beírható
                list.add(i);
            }
        }
        return list;
    }
    
    public boolean Megoldhato(){ //Ellenőrzés, hogy a megadott rejtvény megoldható-e(van-e értelme elkezdeni egyáltalán)
        for(int i=0; i<meret; i++){
            for(int j=0; j<meret; j++){
                if(megoldott[i][j]>0){
                    int x = megoldott[i][j];
                    megoldott[i][j]=0;
                    if(szamCheck(i, j, x)){
                        megoldott[i][j]=x;
                    } else {
                        megoldott[i][j]=x;
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public boolean megold(){
        int sor=0, oszlop=0;
        int min=meret+1;
        int db;
        
        for(int i=0; i<meret; i++){
            for(int j=0; j<meret; j++){
                if(megoldott[i][j]==0){ //a '0' az, ahol nincs szám (üres négyzet)
                    db=Beirhato(i, j).size();
                    if(min>db){
                        min=db;
                        sor=i;
                        oszlop=j;
                    }
                }
            }
        }
        if(min==meret+1){
            return true;//ha a minimum sosem lett kevesebb, mint 10, akkor nem találtunk sehol nullát
        }
        int negyzetszam = sor/negyzetdb*negyzetdb + oszlop/negyzetdb;
        for(int i : Beirhato(sor, oszlop)){ //A lista elemein végigmegyünk, hasonló módon mint a backtrackingnél
            megoldott[sor][oszlop]=i;
            EEList.get(0)[sor][i]=true; //Az adott sorba "bekerül" a beírt szám
            EEList.get(1)[oszlop][i]=true; //Az adott oszlopba "bekerül" a beírt szám
            EEList.get(2)[negyzetszam][i]=true; //Az adott négyzetbe "bekerül" a beírt szám
            
            if(megold()){
                return true;
            }else{       
                EEList.get(0)[sor][megoldott[sor][oszlop]]=false; //Ha visszalépünk, akkor ki kell venni a korábban beírt számot
                EEList.get(1)[oszlop][megoldott[sor][oszlop]]=false;
                EEList.get(2)[negyzetszam][megoldott[sor][oszlop]]=false;
                megoldott[sor][oszlop]=0;
            }
        }
        
        EEList.get(0)[sor][megoldott[sor][oszlop]]=false; //Ha visszalépünk, akkor ki kell venni a korábban beírt számot
        EEList.get(1)[oszlop][megoldott[sor][oszlop]]=false;
        EEList.get(2)[negyzetszam][megoldott[sor][oszlop]]=false;
        megoldott[sor][oszlop]=0;
        return false;
    }
    
    public void kiIr(){
        for(int i=0; i<meret; i++){
            for(int j=0; j<meret; j++){
                if(j==meret-1){
                    System.out.print(megoldott[i][j]); //a sor utolsó eleme után nincs space
                    break;
                }
                System.out.print(megoldott[i][j] + " ");
            }
            System.out.println("");
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        long start = System.nanoTime();
        
        //Az alábbi kódrészlet txt-ből olvas be rejtvényeket EOF-ig
        /*Scanner s = new Scanner(new BufferedReader(new FileReader("table.txt")));
        int i=0, j=0;
        int[][] ujtable= new int[9][9];
        while(s.hasNext()){
            int x = s.nextInt();
            ujtable[i][j]=x;
            j++;
            if(j==9){
                i++;
                j=0;
            }
            if(i==9){
                SoloFC a = new SoloFC(ujtable);
                if(a.Megoldhato()){
                    if(a.megold()){
                        a.kiIr(); //kitöltött tábla
                    } else {
                        System.out.println("Ez nem sikerült!");
                    }
                    i=0;
                    System.out.println("");
                }
            }
        }*/
        
        //Az alábbi kódrészlet egy 16x16-os táblát ad át megoldásra
        /*int[][] table = {
            {0, 15, 11, 1, 0, 0, 12, 9, 0, 0, 6, 4, 2, 0, 0, 13},
            {6, 0, 0, 0, 0, 7, 13, 15, 0, 8, 0, 0, 5, 11, 16, 0},
            {10, 0, 7, 13, 0, 0, 0, 0, 16, 1, 0, 0, 15, 0, 14, 3},
            {16, 0, 12, 5, 2, 0, 14, 3, 9, 7, 0, 13, 0, 0, 0, 0},
            {0, 0, 0, 12, 4, 0, 0, 16, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 3, 0, 0, 0, 1, 10, 0, 0, 16, 11, 0, 0, 0, 9, 6},
            {9, 10, 0, 6, 0, 12, 3, 8, 7, 13, 14, 0, 11, 16, 4, 0},
            {11, 4, 0, 2, 9, 0, 6, 0, 0, 12, 8, 10, 0, 15, 7, 0},
            {0, 0, 14, 3, 0, 0, 15, 0, 0, 4, 0, 9, 0, 1, 13, 11},
            {0, 6, 10, 8, 0, 9, 1, 11, 14, 0, 5, 16, 0, 0, 0, 15},
            {2, 0, 0, 0, 0, 16, 7, 10, 0, 15, 0, 12, 9, 0, 0, 14},
            {15, 0, 0, 7, 0, 0, 0, 4, 13, 11, 10, 6, 0, 0, 12, 16},
            {8, 11, 6, 0, 0, 0, 5, 0, 0, 0, 4, 0, 0, 14, 3, 0},
            {0, 5, 0, 0, 0, 0, 16, 6, 1, 0, 0, 0, 8, 2, 11, 12},
            {0, 2, 15, 0, 0, 4, 9, 1, 11, 0, 0, 8, 16, 13, 6, 0},
            {1, 0, 13, 0, 0, 3, 0, 0, 6, 2, 16, 14, 0, 9, 0, 0},
        };
        SoloFC a = new SoloFC(table);
        a.kiIr(); //eredeti (üres) tábla
        System.out.println("");
        if(a.Megoldhato()){
            if(a.megold()){
                a.kiIr(); //kitöltött tábla
            } else {
                System.out.println("Ez nem sikerült!");
            }
        }*/
        
        //Az alábbi kódrészlet egy 9x9-es táblát ad át megoldásra
        int[][] table = {
            {0, 0, 0, 2, 1, 0, 0, 0, 0},
            {0, 0, 0, 3, 0, 8, 0, 0, 1},
            {9, 5, 0, 0, 0, 0, 2, 0, 0},
            {7, 0, 0, 4, 0, 0, 1, 0, 0},
            {1, 0, 9, 0, 8, 0, 5, 0, 2},
            {0, 0, 4, 0, 0, 9, 0, 0, 7},
            {0, 0, 6, 0, 0, 0, 0, 8, 4},
            {3, 0, 0, 8, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 7, 6, 0, 0, 0},
        };
        SoloFC a = new SoloFC(table);
        a.kiIr(); //eredeti (üres) tábla
        System.out.println("");
        if(a.Megoldhato()){
            if(a.megold()){
                a.kiIr(); //kitöltött tábla
            } else {
                System.out.println("Ez nem sikerült!");
            }
        }
        
        
        
        long end = System.nanoTime();
        long eltelt = end-start;
        System.out.println("");
        System.out.println("Idő: " + eltelt + "ns");
    }
    
}