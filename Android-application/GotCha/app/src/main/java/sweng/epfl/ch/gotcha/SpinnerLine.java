package sweng.epfl.ch.gotcha;

/**
 * Created by nguyenthaitinh on 12/11/15.
 */
public class SpinnerLine {
    private int iconId;
    private String name;

    public SpinnerLine(int iconId, String name) {
        this.iconId = iconId;
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public String getName() {
        return name;
    }
}
