/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solo;

import Solo.SoloMRV;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;



class SudokuGUI extends JFrame implements ActionListener{
    int[][] table = { //Ebbe a táblába kerülnek a beirt számok(ez lesz átadva megoldásra)
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };
    JTextField[] tf; //A számok ide kerülnek, és innen megy tovább a tömbbe
    JPanel p1, px[], p2, p3; //A p1 a "főpanel", a px-re kerülnek a textfieldek, a p2 és p3 "formázásra" van
    JButton bt; //A gomb aminek a megnyomása után megfejtésre kerül a beírt rejtvény
    JLabel lb, lb2; //Az lb formázásra van, illetve az lb2 is, azonban utóbbi visszajelzésre is szolgál
    public SudokuGUI(){
        super("Solo");
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Bezáráskor leáll a program
        setPreferredSize(new Dimension(450, 600));
        setResizable(false);
        Font font1 = new Font("SansSerif", Font.BOLD, 20);

        bt = new JButton("Solve");
        tf = new JTextField[81];
        lb = new JLabel("");
        lb2 = new JLabel("");
        p2 = new JPanel();
        for(int i=0; i<81; i++){
            tf[i]=new JTextField("");
            tf[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }

        p1 = new JPanel();
        px = new JPanel[9];
        for(int i=0; i<9; i++){
            px[i] = new JPanel(); //A 9 kis négyzet felhelyezése
        }
        GridLayout gr = new GridLayout(4, 3); //A főpanel kialakítása
        setLayout(gr);
        GridLayout g2 = new GridLayout(3, 3); //A kisebb 3x3-as négyzetek
        GridLayout g3 = new GridLayout(3, 1); //A gomb és lb2 elhelyezésére

        for(int i=0; i<9; i++){
            px[i].setLayout(g2); //A kis négyzetek 3x3-as elrendezésének beállítása
            for(int j=i*9; j<i*9+9; j++){
                px[i].add(tf[j]); //A Textfieldek felrakása
                tf[j].setHorizontalAlignment(JTextField.CENTER); //Szövegigazítás
                tf[j].setFont(font1); //Betűtípus
            }
            px[i].setBorder(BorderFactory.createMatteBorder(3, 2, 3, 2, Color.BLACK)); //Szegélyek
        }


        for(int i=0; i<9; i++){
            this.add(px[i]); //Panel feltétele az ablakra
        }
        p2.setLayout(g3);
        p2.add(lb2);
        p2.add(bt);
        lb2.setHorizontalAlignment(SwingConstants.CENTER);
        lb2.setVerticalAlignment(SwingConstants.CENTER);
        bt.addActionListener(this); //A gomb lenyomását figyeljük

        this.add(lb); //Egy üres Label feltétele elrendezés céljából
        this.add(p2); //A p2 ablakra helyezése, amin egy label és a gomb található

        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==bt){ //Ha a gomb lenyomását "észleltük"
            /*
            Mivel a korábbi TextField elheyezés úgy történt, hogy először a kis négyzetek kerültek 
            feltöltésre, nem pedig a sorok végig, így egy algoritmus kellett, hogy a táblába jó sorrendben
            kerüljenek bele a számok
            */
            int alap=0, sorszorzo=0; //Az alap az aktuális sor kezdőindexe, a sorszorzora a 3. és 6. sornál lesz szükség
            for(int i=0; i<9; i++){
                
                if(i%3==0){
                    alap=sorszorzo*3*9; //A 0. sornál 0 az index, később a 3.-nál 27, majd a 6.-nál 54
                    sorszorzo++;
                }
                
                int soralap = alap;
                int oszlopindex = 0;
                
                for(int j=0; j<3; j++){
                    for(int k=0; k<3; k++){
                        if(tf[soralap+k].getText().isEmpty()){
                            table[i][oszlopindex+k]=0;
                        } else {
                            table[i][oszlopindex+k]=Integer.parseInt(tf[soralap+k].getText());
                        }
                    }
                    soralap+=9; //a kisnégyzetek kezdőindexe 1 soron belül 9-el tolódik
                    oszlopindex+=3; //a tábla oszlopai 
                }
                alap+=3;//Ha nem kell ugrani(mint minden 3. sornál), akkor elég 3-al léptetni
            }
            
            SoloMRV s = new SoloMRV(table);
            s.setMeret(9);
            if(s.Megoldhato()){ //Ha már hibás a beírt feladvány, akkor felesleges vele foglalkozni(ebben az esetben hibát is eredményez)
                if(s.megold()){ //A rejtvény megoldása
                    //Az előző algoritmus segít a grafikus felületre való visszavitellel
                    alap=0; sorszorzo=0; //Textfieldekbe a számok visszaírása
                    for(int i=0; i<9; i++){

                        if(i%3==0){
                            alap=sorszorzo*3*9;
                            sorszorzo++;
                        }

                        int soralap = alap;
                        int oszlopindex = 0;

                        for(int j=0; j<3; j++){
                            for(int k=0; k<3; k++){
                                String str = Integer.toString(table[i][oszlopindex+k]);
                                tf[soralap+k].setText(str);
                            }
                            soralap+=9;
                            oszlopindex+=3;
                        }
                        alap+=3;
                    }
                    lb2.setText("Siker!");
                } else {
                    lb2.setText("Ez nem sikerült!");
                }
            } else {
                lb2.setText("Hibás rejtvény!");
            }
        }
        //bt.setEnabled(false);
    }

    public static void main(String[] args) {
        SudokuGUI s = new SudokuGUI();
    }
}