package com.mzimu.systemshop.data;

public enum Permission {
    REFRESH("SystemShop.REFRESH"),
    SETSHOP("SystemShop.setshop");

    private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
