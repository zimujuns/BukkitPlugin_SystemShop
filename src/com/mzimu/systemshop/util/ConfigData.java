package com.mzimu.systemshop.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigData {
    /**
     * 作为读取Config的类
     */
    private final String ITEM="item",CHANGE="change",MINMONEY="minMoney",MAXMONEY="maxMoney",NUMBER="number",
            NOPERMISSMSG = "noPermissMsg", NOITEMMSG = "noItemMsg",TIME = "time",MONEYMSG = "moneyMsg",ANNOUNCEMENT = "announcement",
            TITLEINVENTORY = "titleInventory";

    public Plugin plugin;
    private int time;
    private FileConfiguration fileConfiguration;
    private String noPermissMsg,noItemMsg,announcement,moneyMsg,titleInventory;
    private List<ItemStack> data;
    private List<Double> minMoney,maxMoney;
    private List<Integer> change;
    private List<Integer> number;

    public ConfigData(Plugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        this.fileConfiguration = plugin.getConfig();

        this.change = fileConfiguration.getIntegerList(CHANGE);
        this.minMoney = fileConfiguration.getDoubleList(MINMONEY);
        this.maxMoney = fileConfiguration.getDoubleList(MAXMONEY);
        this.number = fileConfiguration.getIntegerList(NUMBER);
        this.data = getItemList();


        this.noPermissMsg = fileConfiguration.getString(NOPERMISSMSG);
        this.noItemMsg = fileConfiguration.getString(NOITEMMSG);
        this.time = fileConfiguration.getInt(TIME);
        this.moneyMsg = fileConfiguration.getString(MONEYMSG);
        this.announcement = fileConfiguration.getString(ANNOUNCEMENT);
        this.titleInventory = fileConfiguration.getString(TITLEINVENTORY);
    }

    public String getMoneyMsg() {
        return moneyMsg;
    }

    public int getTime() {
        return time;
    }

    public String getNoPermissMsg() {
        return noPermissMsg;
    }

    public String getNoItemMsg() {
        return noItemMsg;
    }

    public List<Integer> getNumber() {
        return number;
    }

    public List<Double> getMinMoney() {
        return minMoney;
    }

    public List<Double> getMaxMoney() {
        return maxMoney;
    }

    public List<Integer> getChange() {
        return change;
    }

    public List<ItemStack> getData() {
        return data;
    }



    /**
     * 重载
     */
    public void reload(){
        plugin.reloadConfig();
        this.data = getItemList();
        this.change = fileConfiguration.getIntegerList(CHANGE);
        this.minMoney = fileConfiguration.getDoubleList(MINMONEY);
        this.maxMoney = fileConfiguration.getDoubleList(MAXMONEY);
        this.number = fileConfiguration.getIntegerList(NUMBER);

        getMsg();
    }

    /**
     * 保存
     * @param itemStack
     * @param change
     * @param minMoney
     * @param maxMoney
     */

    public void save(ItemStack itemStack, int change, double minMoney, double maxMoney,int number){
        this.data.add(itemStack);
        this.change.add(change);
        this.minMoney.add(minMoney);
        this.maxMoney.add(maxMoney);
        this.number.add(number);
        save();
    }

    public void save(){
        getMsg();
        if(!fileConfiguration.isString(NOPERMISSMSG)){
            fileConfiguration.set(NOPERMISSMSG,noPermissMsg);
            System.out.println("config中失去：没有权限的消息，将缓存中的消息存入");
        }
        if(!fileConfiguration.isString(NOITEMMSG)){
            fileConfiguration.set(NOITEMMSG,noItemMsg);
            System.out.println("config中失去：没有物品的消息，将缓存中的消息存入");
        }
        if(!fileConfiguration.isString(TIME)){
            fileConfiguration.set(TIME,time);
            System.out.println("config中失去：对时间的设置，将缓存中的消息存入");
        }
        if(!fileConfiguration.isString(MONEYMSG)){
            fileConfiguration.set(MONEYMSG,moneyMsg);
            System.out.println("config中失去：获得金钱的消息，将缓存中的消息存入");
        }
        if(!fileConfiguration.isString(ANNOUNCEMENT)){
            fileConfiguration.set(ANNOUNCEMENT,announcement);
            System.out.println("config中失去：刷新公告的消息，将缓存中的消息存入");
        }
        if(!fileConfiguration.isString(TITLEINVENTORY)){
            fileConfiguration.set(TITLEINVENTORY,titleInventory);
            System.out.println("config中失去：随即商店标题，将缓存中的消息存入");
        }

        fileConfiguration.set(CHANGE,change);
        fileConfiguration.set(MINMONEY,minMoney);
        fileConfiguration.set(MAXMONEY,maxMoney);
        fileConfiguration.set(NUMBER,number);

        saveItemList();
        plugin.saveConfig();
    }

    public void saveItemList(){
        for(int i=0;i<data.size();i++){
            fileConfiguration.set(ITEM+"."+i,data.get(i));
        }
    }

    public List<ItemStack> getItemList(){
        List<ItemStack> itemList = new ArrayList<>();
        for(int i=0;i<change.size();i++){
            ItemStack item = fileConfiguration.getItemStack(ITEM+"."+i);
            itemList.add(item);
        }
        return itemList;
    }


    public String getAnnouncement() {
        return announcement;
    }

    public String getTitleInventory() {
        return titleInventory;
    }

    public void getMsg(){
        this.noPermissMsg = fileConfiguration.getString(NOPERMISSMSG);
        this.noItemMsg = fileConfiguration.getString(NOITEMMSG);
        this.time = fileConfiguration.getInt(TIME);
        this.moneyMsg = fileConfiguration.getString(MONEYMSG);
        this.announcement = fileConfiguration.getString(ANNOUNCEMENT);
        this.titleInventory = fileConfiguration.getString(TITLEINVENTORY);
    }
}
