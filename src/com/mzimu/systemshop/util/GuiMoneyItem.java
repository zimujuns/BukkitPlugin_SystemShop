package com.mzimu.systemshop.util;

import com.mzimu.systemshop.util.Lottery.LotteryData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiMoneyItem {
    private LotteryData lotteryData;

    public GuiMoneyItem(LotteryData lotteryData) {
        this.lotteryData = lotteryData;
    }

    public double randomMoney(){
        double money = (lotteryData.getMaxMoney() - lotteryData.getMinMoney());
        Random random = new Random();
        money = random.nextInt((int) money) + 1;
        return (money+lotteryData.getMinMoney());
    }

    public ItemStack getMoneyMsgItem(){
        ItemStack itemStack = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§f上方商品");
        List<String> lore = new ArrayList<>();
        lore.add("§l§m            ");
        lore.add("");
        lore.add("§a当前价格为");
        lore.add(randomMoney() + "");
        lore.add("§a需求数量为");
        lore.add(lotteryData.getNumber()+"");
        lore.add("");
        lore.add("§4§l左键单个售卖,右键整组售卖");
        lore.add("§4§l左键+Shift 售卖条件数");
        lore.add("§l§m            ");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
