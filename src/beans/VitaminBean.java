package beans;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Jay on 4/19/14.
 */
public class VitaminBean implements Serializable {
    private Date logDate;
    private long id;
    private String foodName;
    private double vitaminA;
    private double vitaminC;
    private double vitaminD;
    private double vitaminE;
    private double vitaminK;
    private double vitaminB1;
    private double vitaminB2;
    private double vitaminB3;
    private double vitaminB6;
    private double vitaminB12;
    private double pantothenic;
    private double biotin;
    private int amount = 1;
    private int mData;
    private String type;



    public VitaminBean(String foodName, double vitaminA, double vitaminC, double vitaminD, double vitaminE, double vitaminK, double vitaminB1, double vitaminB2, double vitaminB3, double vitaminB6, double vitaminB12, int amount, String type){
        this.foodName = foodName;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.vitaminD = vitaminD;
        this.vitaminE = vitaminE;
        this.vitaminK = vitaminK;
        this.vitaminB1 = vitaminB1;
        this.vitaminB2 = vitaminB2;
        this.vitaminB3 = vitaminB3;
        this.vitaminB6 = vitaminB6;
        this.vitaminB12 = vitaminB12;
        this.type = type;
        if(amount == 0){
            amount = 1;
        }else{
            this.amount = amount;
        }


    }

    public void updateAmount(int amount){
        if(amount !=0){
            this.amount = amount;
        }
    }

    public Map<String, Double> getVitaminMap(){
        Map<String, Double> map = new HashMap<String, Double>();

        map.put("A", vitaminA);
        map.put("C", vitaminC);
        map.put("D", vitaminD);
        map.put("E", vitaminE);
        map.put("K", vitaminK);
        map.put("B1", vitaminB1);
        map.put("B2", vitaminB2);
        map.put("B3", vitaminB3);
        map.put("B6", vitaminB6);
        map.put("B12", vitaminB12);

        ValueComparator bvc =  new ValueComparator(map);
        TreeMap<String,Double> sortedMap = new TreeMap<String,Double>(bvc);

        sortedMap.putAll(map);

        return sortedMap;
    }




    public long getId(){ return id; }
    public String getFoodName(){ return foodName; }
    public double getVitaminA(){ return vitaminA; }
    public double getVitaminC(){ return vitaminC; }
    public double getVitaminD(){ return vitaminD; }
    public double getVitaminE(){ return vitaminE; }
    public double getVitaminK(){ return vitaminK; }
    public double getVitaminB1(){ return vitaminB1; }
    public double getVitaminB2(){ return vitaminB2; }
    public double getVitaminB3(){ return vitaminB3; }
    public double getVitaminB6(){ return vitaminB6; }
    public double getVitaminB12(){ return vitaminB12; }
    public int getAmount() { return amount; }
    public String getType() {return type;}

}


class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
