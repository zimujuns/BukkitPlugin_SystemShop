package com.mzimu.systemshop.util.Lottery;

import org.bukkit.inventory.ItemStack;

public class LotteryData {
    private int change;
    private ItemStack itemStack;
    private double minMoney;
    private double maxMoney;
    private int number =-1;

    public LotteryData(int change, ItemStack itemStack, double minMoney, double maxMoney, int number) {
        this.change = change;
        this.itemStack = itemStack;
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
        this.number = number;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(double minMoney) {
        this.minMoney = minMoney;
    }

    public double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
