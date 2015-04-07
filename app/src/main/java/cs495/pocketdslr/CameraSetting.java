package cs495.pocketdslr;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Created by Chris on 4/6/2015.
 */
public class CameraSetting {

    protected String settingKey;
    Object[] possibleValues;

    private CameraSetting(String settingKey, Object[] possibleValues){
        this.settingKey = settingKey;
        this.possibleValues = possibleValues;
    }

    public String getSettingKey(){

        return this.settingKey;
    }

    public Pair<Integer, String> translateSettingValue(int value){

        int possibleValuesCount = this.possibleValues.length;

        int valueIndex = (int)(possibleValuesCount * (value / 100.0));

        return this.getPossibleValueAt(valueIndex);
    }

    public int reverseTranslatedValue(int value) {

        for (int i = 0; i < this.possibleValues.length; i++) {

            if (this.getPossibleValueAt(i).first.equals(value)){

                return (int)(100 * ((float)i / this.possibleValues.length));
            }
        }

        return 0;
    }

    public Pair<Integer, String> getSettingValue(int value) {

        for (int i = 0; i < this.possibleValues.length; i++) {

            Pair<Integer, String> settingValue = this.getPossibleValueAt(i);

            if (settingValue.first.equals(value)) {

                return settingValue;
            }
        }

        return this.getPossibleValueAt(0);
    }

    private Pair<Integer, String> getPossibleValueAt(int index) {

        if (index >= this.possibleValues.length) {
            index--;
        }
        else if (index < 0) {
            index = 0;
        }

        return (Pair<Integer, String>)this.possibleValues[index];
    }

    public static CameraSetting createISOCameraSetting() {

        Object[] possibleValues = new Object[100];

        for (int i = 0; i < 99; i++) {
            Integer value = 40 + i * 100;
            possibleValues[i] = new Pair<Integer, String>(value, "ISO " + value.toString());
        }

        possibleValues[99] = new Pair<Integer, String>(10000, "ISO 10000");

        return new CameraSetting(ManualCameraSettings.ISO, possibleValues);
    }

    public static CameraSetting createExposeTimeCameraSetting() {

        Object[] possibleValues = new Object[100];

        possibleValues[0] = new Pair<Integer, String>(2, "1/2");

        for (int i = 1; i < 100; i++) {
            Integer value = i * 750;
            possibleValues[i] = new Pair<Integer, String>(value, "1/" + value.toString());
        }

        return new CameraSetting(ManualCameraSettings.EXPOSURE_TIME, possibleValues);
    }

    public static CameraSetting createApertureCameraSetting() {

        Object[] possibleValues = new Object[8];

        possibleValues[0] = new Pair<Integer, String>(1, "f 1.4");
        possibleValues[1] = new Pair<Integer, String>(2, "f 2");
        possibleValues[2] = new Pair<Integer, String>(3, "f 2.8");
        possibleValues[3] = new Pair<Integer, String>(4, "f 4");
        possibleValues[4] = new Pair<Integer, String>(5, "f 5.6");
        possibleValues[5] = new Pair<Integer, String>(8, "f 8");
        possibleValues[6] = new Pair<Integer, String>(11, "f 11");
        possibleValues[7] = new Pair<Integer, String>(16, "f 16");

        return new CameraSetting(ManualCameraSettings.APERTURE_SIZE, possibleValues);
    }
}
