package com.mzimu.systemshop.util.Lottery;


import com.mzimu.systemshop.Main;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawLotteryUtil {
    private List<LotteryData> inventorylist = new ArrayList<>();
    private List<LotteryData> lotteryDataList;
    private List<Integer> changeList = new ArrayList<>();
    private List<Section> sectionList = new ArrayList<>();
    private int a = 0;

    public DrawLotteryUtil(List<LotteryData> lotteryList) {
        this.lotteryDataList = lotteryList;
    }

    /**
     * 主要初始化的代码
     * 将奖池赋予进入
     * 再将概率区间赋予到对应变量
     */
    public void main(){
        for(int i=0;i<lotteryDataList.size();i++){
            LotteryData lotteryData = lotteryDataList.get(i);
            int change = lotteryData.getChange();
            changeList.add(change);
        }

        for(int i=0;i<changeList.size();i++) {
            int b = changeList.get(i) + a;
            sectionList.add(new Section(a, b));
            a = b + 1;
        }
    }

    /**
     * 刷新奖池的代码
     * @return
     */
    public ItemStack[] refreshLottery(){
        inventorylist.clear();
        ItemStack[] itemStacks = new ItemStack[9];
        for(int k=0;k<9;k++){
            Random random = new Random();
            int number =0;
            try{
                number = random.nextInt(a-1)+1;
            }catch (IllegalArgumentException e){
                System.out.println("文件内没有物品 注销插件运行");
                return null;
            }

            for(int i=0;i<sectionList.size();i++){
                if(sectionList.get(i).isAB(number)){
                    inventorylist.add(lotteryDataList.get(i));
                    break;
                }
            }
            itemStacks[k] = inventorylist.get(k).getItemStack();
        }
        return itemStacks;
    }

    public List<LotteryData> getInventorylist() {
        return inventorylist;
    }

    /**
     * 内部类：
     *  用于输出数字区间
     *  来用作抽奖判断
     */
    private class Section{
        private int a;
        private int b;

        public Section(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
        
        public Boolean isAB(int number){
            if(a<=number && b>=number){
               return true; 
            }
            return false;
        }
    }


}