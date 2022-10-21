package net.diecode.killermoney.enums;

public enum KMPermission {

    ADMIN("admin"),
    MONEY_MULTIPLIER("money.multiplier"),

    LIMIT_MONEY_MULTIPLIER("moneylimit.multiplier"),
    //LIMIT_ITEM_MULTIPLIER("itemlimit.multiplier"),
    //LIMIT_COMMAND_MULTIPLIER("commandlimit.multiplier"),

    BYPASS_MONEY_LIMIT("bypass.moneylimit"),
    BYPASS_ITEM_LIMIT("bypass.itemlimit"),
    BYPASS_COMMAND_LIMIT("bypass.commandlimit"),
    BYPASS_MONEY_LIMIT_CASH_TRANSFER("bypass.cashtransferlimit");

    private String perm;

    KMPermission(String perm) {
        this.perm = perm;
    }

    public String get() {
        return "km." + perm;
    }

}
