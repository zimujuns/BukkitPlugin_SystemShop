package com.mzimu.systemshop.util.Lottery;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LotteryListUtil {
    private List<ItemStack> data;
    private List<Integer> change,number,minMoney,maxMoney;
    public LotteryListUtil(List<ItemStack> data, List<Integer> change , List<Integer> minMoney, List<Integer> maxMoney, List<Integer> number) {
        this.data = data;
        this.minMoney = minMoney;
        this.maxMoney = maxMoney;
        this.change = change;
        this.number = number;
    }

    public List<LotteryData> getLotteryList(){
        List<LotteryData> lotteryDataList = new ArrayList<>();
        if(maxMoney.size() == minMoney.size() && maxMoney.size()==change.size() && data.size() == change.size()){
            for(int i=0;i<data.size();i++){
                lotteryDataList.add(
                        new LotteryData(
                                change.get(i),
                                data.get(i),
                                minMoney.get(i),
                                maxMoney.get(i),
                                number.get(i)
                        )
                );
            }
        }else{
            return null;
        }

        return lotteryDataList;
    }



}
