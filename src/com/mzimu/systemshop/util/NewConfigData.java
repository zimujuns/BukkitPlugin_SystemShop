package com.mzimu.systemshop.util;

import com.mzimu.systemshop.Main;
import com.mzimu.systemshop.util.Lottery.LotteryData;
import com.mzimu.systemshop.util.Lottery.LotteryListUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewConfigData {
    private Plugin plugin;

    private final String ITEM="item",CHANGE="change",MINMONEY="minMoney",MAXMONEY="maxMoney",NUMBER="number",
            NOPERMISSMSG = "noPermissMsg", NOITEMMSG = "noItemMsg",TIME = "time",MONEYMSG = "moneyMsg",ANNOUNCEMENT = "announcement",
            TITLEINVENTORY = "titleInventory";


    private int time;
    private FileConfiguration fileConfiguration;
    private String noPermissMsg,noItemMsg,announcement,moneyMsg,titleInventory;

    private List<Map<?, ?>> configList;
    private List<ItemStack> data = new ArrayList<>();
    private List<Integer> minMoney = new ArrayList<>(),maxMoney = new ArrayList<>(),change = new ArrayList<>(),number = new ArrayList<>();

    public NewConfigData(Plugin plugin) {
        this.plugin = plugin;
        this.fileConfiguration = plugin.getConfig();

        this.noPermissMsg = fileConfiguration.getString(NOPERMISSMSG);
        this.noItemMsg = fileConfiguration.getString(NOITEMMSG);
        this.time = fileConfiguration.getInt(TIME);
        this.moneyMsg = fileConfiguration.getString(MONEYMSG);
        this.announcement = fileConfiguration.getString(ANNOUNCEMENT);
        this.titleInventory = fileConfiguration.getString(TITLEINVENTORY);
        this.configList = fileConfiguration.getMapList("data");
    }

    public List<LotteryData> getConfig(){

        for(int i=0;i<configList.size();i++){
            for(Map.Entry<?, ?> entry : configList.get(i).entrySet()){
                switch (entry.getKey().toString()){
                    case ITEM:
                        data.add((ItemStack) entry.getValue());
                        break;
                    case CHANGE:
                        change.add((Integer) entry.getValue());
                        break;
                    case MINMONEY:
                        minMoney.add((Integer) entry.getValue());
                        break;
                    case MAXMONEY:
                        maxMoney.add((Integer) entry.getValue());
                        break;
                    case NUMBER:
                        number.add((Integer) entry.getValue());
                        break;
                }
            }
        }
        return new LotteryListUtil(data,change,minMoney,maxMoney,number).getLotteryList();

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

    public List<Integer> getMinMoney() {
        return minMoney;
    }

    public List<Integer> getMaxMoney() {
        return maxMoney;
    }

    public List<Integer> getChange() {
        return change;
    }

    public List<ItemStack> getData() {
        return data;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public String getTitleInventory() {
        return titleInventory;
    }

    public void save(ItemStack itemStack, int change, int minMoney, int maxMoney,int number){
        Map<Object, Object> listMap = new HashMap<>();
        listMap.put(ITEM,itemStack);
        listMap.put(CHANGE,change);
        listMap.put(NUMBER,number);
        listMap.put(MINMONEY,minMoney);
        listMap.put(MAXMONEY,maxMoney);
        configList.add(listMap);
        save();
    }

    public void save(){
        fileConfiguration.set("data",configList);
        plugin.saveConfig();
    }


}
