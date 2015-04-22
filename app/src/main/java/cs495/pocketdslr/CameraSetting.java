package cs495.pocketdslr;

import android.hardware.camera2.CameraCharacteristics;
import android.util.Pair;
import android.util.Range;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Created by Chris on 4/6/2015.
 */
public class CameraSetting {

    protected String settingKey;
    protected Object[] possibleValues;

    public Object lookup;

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

    public void setRange(CameraCharacteristics cameraCharacteristics) {

        Object[] possibleValues = new Object[100];

        switch (this.settingKey) {
            case ManualCameraSettings.EXPOSURE_TIME:

                Range<Long> exposureRange = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);

                int lower = (int)(exposureRange.getLower() / (long)1000000) + 1;
                int upper = (int)(exposureRange.getUpper() / (long)1000000);

                int range = upper - lower;

                int incrementer = (int)(range / 100.0);

                for (int i = 0; i < 100; i++) {
                    Integer value = i * incrementer + lower;
                    possibleValues[i] = new Pair<Integer, String>(value, value.toString() + "ms");
                }

                this.possibleValues = possibleValues;

                break;
            case ManualCameraSettings.APERTURE_SIZE:

                float availableApertures = cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);

                //this.lookup = availableApertures;

                //possibleValues = new Object[availableApertures.length];

                //for (int i = 0; i < availableApertures.length; i++) {
                //    possibleValues[i] = new Pair<Integer, String>(i, "f " + availableApertures[i]);
                //}

                float[] preciseFocusDistances = new float[100];

                float incrementerFocus = availableApertures / 100.0f;

                for (int i = 0; i < 100; i++) {
                    Float distance = i * incrementerFocus;
                    preciseFocusDistances[i] = distance;
                    DecimalFormat decimalFormat = new DecimalFormat("##.00");
                    possibleValues[i] = new Pair<Integer, String>(i, decimalFormat.format(distance));
                }

                this.lookup = preciseFocusDistances;
                this.possibleValues = possibleValues;

                break;
            case ManualCameraSettings.ISO:

                Range<Integer> isoRange = cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);

                int rangeIso = isoRange.getUpper() - isoRange.getLower();

                int incrementerIso = (int)(rangeIso / 100.0);

                for (int i = 0; i < 99; i++) {
                    Integer value = isoRange.getLower() + i * incrementerIso;
                    possibleValues[i] = new Pair<Integer, String>(value, "ISO " + value.toString());
                }

                possibleValues[99] = new Pair<Integer, String>(isoRange.getUpper(), "ISO " + isoRange.getUpper());

                this.possibleValues = possibleValues;

                break;
        }
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

        Object[] possibleValues = new Object[1];

        possibleValues[0] = new Pair<Integer, String>(1, "14.29");

        return new CameraSetting(ManualCameraSettings.APERTURE_SIZE, possibleValues);
    }
}
