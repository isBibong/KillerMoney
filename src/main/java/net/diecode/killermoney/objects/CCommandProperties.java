package net.diecode.killermoney.objects;

import net.diecode.killermoney.enums.RunMethod;

import java.util.ArrayList;

public class CCommandProperties {

    private RunMethod runMethod;
    private ArrayList<CCommand> cCommands;
    private boolean enabled;

    public CCommandProperties(RunMethod runMethod, ArrayList<CCommand> cCommands, boolean enabled) {
        this.runMethod = runMethod;
        this.cCommands = cCommands;
        this.enabled = enabled;
    }

    public RunMethod getRunMethod() {
        return runMethod;
    }

    public ArrayList<CCommand> getCCommands() {
        return cCommands;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
