package net.diecode.killermoney.objects;

import net.diecode.killermoney.enums.RunMethod;

import java.util.ArrayList;

public class CItemProperties {

    private boolean keepDefaultItems = true;
    private RunMethod runMethod;
    private ArrayList<CItem> cItems;
    private boolean enabled;

    public CItemProperties(boolean keepDefaultItems, RunMethod runMethod, ArrayList<CItem> cItems, boolean enabled) {
        this.keepDefaultItems = keepDefaultItems;
        this.runMethod = runMethod;
        this.cItems = cItems;
        this.enabled = enabled;
    }

    public boolean isKeepDefaultItems() {
        return keepDefaultItems;
    }

    public RunMethod getRunMethod() {
        return runMethod;
    }

    public ArrayList<CItem> getCItems() {
        return cItems;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
