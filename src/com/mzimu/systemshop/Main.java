package com.mzimu.systemshop;

import com.mzimu.systemshop.data.Permission;
import com.mzimu.systemshop.runnable.ReloadShopList;
import com.mzimu.systemshop.trigger.OnPlayInventoryClick;
import com.mzimu.systemshop.trigger.OnPlayInventoryDrag;
import com.mzimu.systemshop.util.CommandMain;
import com.mzimu.systemshop.util.ConfigData;
import com.mzimu.systemshop.util.GuiMoneyItem;
import com.mzimu.systemshop.util.Lottery.DrawLotteryUtil;
import com.mzimu.systemshop.util.Lottery.LotteryData;
import com.mzimu.systemshop.util.Lottery.LotteryListUtil;
import com.mzimu.systemshop.util.NewConfigData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.Iterator;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    public static Economy econ = null;
    public static int time = 1;

    public static String noPermissMsg,noItemMsg,announcement,moneyMsg,titleInventory;

    private static final Logger log = Logger.getLogger("Minecraft");
    public static List<LotteryData> lotteryDataList = new ArrayList<>();
//    public static ConfigData configData;
    public static NewConfigData configData;

    public static DrawLotteryUtil drawLotteryUtil;
    public static Inventory gui;
    public static ReloadShopList reloadShopList;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onEnable() {
        getCommand("zmss").setExecutor(new CommandMain());

        new OnPlayInventoryDrag(this);
        new OnPlayInventoryClick(this);

        configData = new NewConfigData(this);
//        configData = new ConfigData(this);
        titleInventory = configData.getTitleInventory();
        time = configData.getTime();
        noPermissMsg = configData.getNoPermissMsg();
        noItemMsg = configData.getNoItemMsg();
        announcement = configData.getAnnouncement();
        moneyMsg = configData.getMoneyMsg();

        gui = Bukkit.createInventory(null ,18,titleInventory);

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - 没有Vault经济插件 请注意 无法启用SystemShop!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if(a()){
            reloadShopList = new ReloadShopList();
            reloadShopList.runTaskTimerAsynchronously(this,20L,time * 20L);
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        configData.save();
        super.onDisable();
    }

    /**
     * 用来初始化 并且刷新的
     * 单纯的刷新可以通过
     * Main.gui.setStorageContents(Main.drawLotteryUtil.refreshLottery());
     * 来刷新
     */
    public static boolean a(){
        if(configData.getChange().size()!=configData.getData().size() || configData.getMaxMoney().size()!=configData.getData().size() || configData.getMinMoney().size()!=configData.getData().size() || configData.getNumber().size()!=configData.getData().size()){
            System.out.println("文件发生错误,请删除配置文件再打开服务器,进行初始化[文件内无内容则无视错误]");
            return false;
        }
        System.out.println("读取到文件正在初始化商店");
        lotteryDataList = configData.getConfig();
        drawLotteryUtil = new DrawLotteryUtil(lotteryDataList);
        drawLotteryUtil.main();
        ItemStack[] itemStack = drawLotteryUtil.refreshLottery();
        if(itemStack==null){
            System.out.println("当前配置文件内没有东西，无法定时刷新随机商店，请进行配置后再重新启动服务器!");
            return false;
        }
        gui.setStorageContents(itemStack);
        b();
        return true;
    }

    public static void b() {
        for (int i = 0; i < 9; i++) {
            /**
             * 将价格以书的形式赋值到9~17格子之间
             */
            GuiMoneyItem guiMoneyItem = new GuiMoneyItem(drawLotteryUtil.getInventorylist().get(i));
            ItemStack itemStack = guiMoneyItem.getMoneyMsgItem();
            gui.setItem(i + 9, itemStack);
        }
    }

    public static void closeInventory(){
        Iterator iterator = getProvidingPlugin(Main.class).getServer().getOnlinePlayers().iterator();
        while(iterator.hasNext()){
            Player player = (Player) iterator.next();
            player.closeInventory();
            player.sendMessage(announcement);
        }
    }
}
