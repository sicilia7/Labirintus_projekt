package labyrinth;

import java.util.*;

public class Model {

    private Field [][] mtx = new Field [7][7];
    private Field missing; //pályán kívüli mező
    private int winner; //a győztes indexe, ha van nyertes; -1 egyébként
    private int player; //éppen játszó játékos
    private int maxPlayers; //legyen 1 és 4 között, bár 1-gyel nincs sok értelme
    private int lastNum; //hányadik oszlopba/sorba szúrtunk be legutóbb
    private int lastDir; //melyik irányból szúrtunk be legutóbb
    List<Integer> posI = new ArrayList<Integer>(); //a játékosok első koordinátája
    List<Integer> posJ = new ArrayList<Integer>(); //a játékosok második koordinátája
    Stack<Integer> treasureP0 = new Stack<>(); //első játékos kincses paklija, a legfelsőt keresi mindig
    Stack<Integer> treasureP1 = new Stack<>();
    Stack<Integer> treasureP2 = new Stack<>();
    Stack<Integer> treasureP3 = new Stack<>();
    public Model(int maxPlayers) {
        for (int i=0; i<7; ++i) {
            for (int j=0; j<7; ++j) {
                if((i!=0 || j!=0) && (i!=6 || j!=6)) {
                    randomizeField(i,j);
               }
           }
        }
        
        
        posI.add(0); posI.add(0); posI.add(6); posI.add(6);
        posJ.add(0); posJ.add(6); posJ.add(0); posJ.add(6);
        winner=-1;
        player=0;
        if(maxPlayers>4){
            this.maxPlayers=4;
        }else if(maxPlayers<1){
            this.maxPlayers=1;
        }
        else{
            this.maxPlayers=maxPlayers;
        }
        missingInit();
        randomizeTreasureStacks();
        randomizeTreasureLocations();
        edgeInit();
    }

    /**
     * Megnézhetjük az adott játékosnak a paklija tetején lévő titokkártyáját
     * @param i játékos sorszáma
     * @return a titokkártya száma
     */
    public int getNextTreasure(int i){
        switch(i){
            case 0: return treasureP0.lastElement();
            case 1: return treasureP1.lastElement();
            case 2: return treasureP2.lastElement();
            case 3: return treasureP3.lastElement();
        }  
        return -1;
    }
    /**
     * Játékosok számát jelzi
     * @return játékosok száma
     */
    public int getMaxPlayers() {
        return maxPlayers;
    } 
    /**
     * Megmutatja a kinti mezőt
     * @return a kinti mező
     */
    public Field getMissing(){
        return this.missing;
    }
    /**
     * A nyertes játékos sorszámát kapjuk meg.
     * @return A nyertes sorszáma.
     */
    public int getWinner() {
        return winner;
    }
    /**
     * Lekérdezhetjük vele, hogy a játékosunk éppen milyen mezőn áll.
     * @return Mező összes adatát tartalmazza.
     */
    public Field getField () { 
        return mtx[posI.get(player)][posJ.get(player)];
    }
    /**
     * Lekérdezhetjük vele az adott mező tulajdonságait
     * @param i hányadik sor?
     * @param j hányadik oszlop?
     * @return Mező összes adatát tartalmazza
     */
    public Field getField (int i, int j) { 
        return mtx[i][j];
    }
    /**
     * Lekérdezhetjük vele, hogy melyik játékos játszik éppen.
     * @return Játékos sorszáma.
     */
    public int getPlayer() {
        return player;
    }
    /**
     * Kinti mező beszúrása
     * @param dir melyik irány?
     * @param num hányadik oszlop?
     * @return igaz, ha sikerült a beszúrás; hamis, ha nem
     */
    public boolean insert(int dir, int num) {
        if (dir!=lastDir || num!=lastNum) {
            switch(dir) {
                case 0: {
                    lastDir=2;
                    lastNum=num;
                    return insertFromTop(num);
                }
                case 1:{
                    lastDir=3;
                    lastNum=num;
                    return insertFromRight(num);
                }
                case 2:{
                    lastDir=0;
                    lastNum=num;
                    return insertFromBottom(num);
                }
                case 3: {
                    lastDir=1;
                    lastNum=num;
                    return insertFromLeft(num);
                }
                default: return false;
            }
        }
        else return false;
    }
    /**
     * Felülről való beszúrás
     * @param col hányadik oszlop?
     * @return sikerült?
     */
    public boolean insertFromTop(int col) {
        if (col % 2 == 1) {
            Field temp = mtx[6][col];
            for (int i=0; i<6; ++i) {
                mtx[6-i][col]=mtx[5-i][col];
            }
            mtx[0][col]=missing;
            missing=temp;
            return true;
        }
        return false;
    }
    /**
     * Alulról való beszúrás
     * @param col hányadik oszlop?
     * @return sikerült?
     */
    public boolean insertFromBottom(int col) {
        if (col % 2 == 1) {
            Field temp = mtx[0][col];
            for (int i=0; i<6; ++i) {
                mtx[i][col]=mtx[i+1][col];
            }
            mtx[6][col]=missing;
            missing=temp;
            return true;
            }
        return false;
    }
    /**
     * Balról való beszúrás
     * @param row hányadik sor?
     * @return sikerült?
     */
    public boolean insertFromLeft(int row) {
        if (row % 2 == 1) {
            Field temp = mtx[row][6];
            for (int i=0; i<6; ++i) {
                mtx[row][6-i]=mtx[row][5-i];
            }
            mtx[row][0]=missing;
            missing=temp;
            return true;
            }
        return false;
    }
    /**
     * Jobbról való beszúrás
     * @param row hányadik sor?
     * @return sikerült?
     */
    public boolean insertFromRight(int row) {
        if (row % 2 == 1) {
            Field temp = mtx[row][0];
            for (int i=0; i<6; ++i) {
                mtx[row][i]=mtx[row][i+1];
            }
            mtx[row][6]=missing;
            missing=temp;
            return true;
        }
        return false;
    }
    /**
     * A külső mezőt tudjuk forgatni
     * @param dir hányszor szeretnénk jobbra forgatni?
     */
    public void rotate(int dir) {
        boolean tempDir=missing.isUp();
        switch(dir) {
            case 1: {
                missing.setUp(missing.isLeft());
                missing.setLeft(missing.isDown());
                missing.setDown(missing.isRight());
                missing.setRight(tempDir);
                break;
            }
            case 2: {
                missing.setUp(missing.isDown());
                missing.setDown(tempDir);
                tempDir=missing.isLeft();
                missing.setLeft(missing.isRight());
                missing.setRight(tempDir);
                break;
            }
            case 3: {
                missing.setUp(missing.isRight());
                missing.setRight(missing.isDown());
                missing.setDown(missing.isLeft());
                missing.setLeft(tempDir);
                break;
            }
        }
    }
    /**
     * A soron következő játékos megpróbál az adott irányba mozogni
     * @param dir az irány száma
     */
    public void movePlayer (int dir) {
        switch(dir) {
            case 0: //felfele
                if (posI.get(player) !=0 && mtx[posI.get(player)][posJ.get(player)].isUp() && mtx[posI.get(player)-1][posJ.get(player)].isDown()) {
                    posI.set(player, posI.get(player)-1);
                }
                hasFoundTreasure();
                break;
            case 1: //jobbra
                if (posJ.get(player) !=6 && mtx[posI.get(player)][posJ.get(player)].isRight() && mtx[posI.get(player)][posJ.get(player)+1].isLeft()) {
                    posJ.set(player, posJ.get(player)+1);
                }
                hasFoundTreasure();
                break;
            case 2: //lefele
                if (posI.get(player)!=6 && mtx[posI.get(player)][posJ.get(player)].isDown() && mtx[posI.get(player)+1][posJ.get(player)].isUp()) {
                    posI.set(player, posI.get(player)+1);
                }
                hasFoundTreasure();
                break;
            case 3: //balra
                if (posJ.get(player) !=0 && mtx[posI.get(player)][posJ.get(player)].isLeft() && mtx[posI.get(player)][posJ.get(player)-1].isRight()) {
                    posJ.set(player, posJ.get(player)-1);
                }
                hasFoundTreasure();
                break;
            default:
                break;
        }
    }
    /**
     * A játékos abbahagyja a körét
     */
    public void endTurn() {
        player++;
        if (player==maxPlayers) player=0;
    }
    /**
     * Megnézi, hogy az adott játékos megtalálta-e a keresett kincset
     * @return megtalálta?
     */
    public boolean hasFoundTreasure () {
        switch(player){
            case 0: {
                if (!treasureP0.empty() && treasureP0.lastElement() == getField().getTreasure()) {
                    treasureP0.pop();
                    if (treasureP0.empty()) {
                        winner=0;
                    }
                    return true;
                }
                return false;
            }
            case 1: {
                if (!treasureP1.empty() && treasureP1.lastElement() == getField().getTreasure()) {
                    treasureP1.pop();
                    if (treasureP1.empty()) {
                        winner=1;
                    }
                    return true;
                }
                return false;
            }
            case 2: {
                if (!treasureP2.empty() && treasureP2.lastElement() == getField().getTreasure()) {
                    treasureP2.pop();
                    if (treasureP2.empty()) {
                        winner=2;
                    }
                    return true;
                }
                return false;
            }
            case 3: {
                if (!treasureP3.empty() && treasureP3.lastElement() == getField().getTreasure()) {
                    treasureP3.pop();
                    if (treasureP3.empty()) {
                        winner=3;
                    }
                    return true;
                }
                return false;
            }
            default: return false;
        }
    }
    
    /**
     * Külső mező inicializálása
     */
    private void missingInit() { 
        Random rand = new Random();
        int trueCounter = 0;
        boolean up = rand.nextBoolean();
        if (up==true) {
            trueCounter++;
        }
        boolean right = rand.nextBoolean();
        if (right==true) {
            trueCounter++;
        }
        boolean down, left;
        if (trueCounter==0) {
            down = left = true;
        }
        else {
            down=rand.nextBoolean();
            if (trueCounter==1 && down==false) {
                left=true;
            }
            else left=rand.nextBoolean();
        }
        
        missing = new Field(up, right, down, left, -1);
    }
    /**
     * Minden játékos kap 6, különböző random kártyát
     */
    private void randomizeTreasureStacks() { 
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<6*maxPlayers; ++i) {
            list.add(i);
        }
        Random rand = new Random();
        int max = 6*maxPlayers;
        for (int i=0; i<6; ++i) {
            int n = rand.nextInt(max--);
            treasureP0.push(list.get(n));
            list.remove(n);
            if (maxPlayers>1) {
                n = rand.nextInt(max--);
                treasureP1.push(list.get(n)); 
                list.remove(n);
            }
            if (maxPlayers>2) {
                n = rand.nextInt(max--);
                treasureP2.push(list.get(n)); 
                list.remove(n);
            }
            if (maxPlayers>3) {
                n = rand.nextInt(max--);
                treasureP3.push(list.get(n)); 
                list.remove(n);
            }
            
        }
    }
    /**
     * kincses mezők randomizálása
     */
    private void randomizeTreasureLocations() {
        List<Integer> gotNumber = new ArrayList();
        Random rand = new Random();
        for (int i=0; i<24; ++i) {
            boolean ok;
            int n;
            do  {
                ok=true;
                n = rand.nextInt(46); //0-tól 45-ig generál, sorfolytonosan nézve a mezőket mindegyik szám egy mezőt reprezentál a sarkokat kihagyva, a 45-ös a külső mező
                for (int j=0; j<gotNumber.size() && ok; ++j) {
                    if (n==gotNumber.get(j)) ok=false;
                }
            } while(ok==false);
            gotNumber.add(n);
            if (n<5) {
                mtx[0][n+1].setTreasure(i);
            }
            else if (n==45) {
                missing.setTreasure(i);
            }
            else if (n>39) {
                mtx[6][(n+3)%7].setTreasure(i);
            }
            else {
                mtx[(n+2)/7][(n+2)%7].setTreasure(i);
            }
        }
    }
    /**
     * mezők falainak randomizálása
     * @param i hányadik sor?
     * @param j hányadik oszlop?
     */
    private void randomizeField(int i, int j) {
        Random rand = new Random();
        int trueCounter = 0;
        boolean up = rand.nextBoolean();
        if (up==true) {
            trueCounter++;
        }
        boolean right = rand.nextBoolean();
        if (right==true) {
            trueCounter++;
        }
        boolean down, left;
        if (trueCounter==0) {
            down = left = true;
        }
        else {
            down=rand.nextBoolean();
            if (trueCounter==1 && down==false) {
                left=true;
            }
            else left=rand.nextBoolean();
        }
        
        mtx[i][j] = new Field(up, right, down, left, -1);
    }
    /**
     * a sarkok inicializálása
     */
    private void edgeInit() {
        mtx[0][0]=new Field(false, true, true, false, -1);
        mtx[0][6]=new Field(false, false, true, true, -1);
        mtx[6][0]=new Field(true, true, false, false, -1);
        mtx[6][6]=new Field(true, false, false, true, -1);
    }
}