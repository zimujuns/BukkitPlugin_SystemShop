package com.mzimu.systemshop.trigger;

import com.mzimu.systemshop.Main;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnPlayInventoryClick implements Listener {
    private Plugin plugin;

    public OnPlayInventoryClick(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void main(InventoryClickEvent inventoryClickEvent){

        if (!(inventoryClickEvent.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) inventoryClickEvent.getWhoClicked();
        int rawSlot = inventoryClickEvent.getRawSlot();
        if(rawSlot == -999){
            return;
        }
        if(inventoryClickEvent.getInventory().getTitle().equalsIgnoreCase(Main.titleInventory)){
            if(rawSlot>=18 && rawSlot<=53){
                return;
            }else
            /**
             * 以防玩家拿走物品
             */
            if(inventoryClickEvent.getSlot()>=0 && inventoryClickEvent.getSlot()<=8){
                inventoryClickEvent.setCancelled(true);
                player.closeInventory();
                player.openInventory(Main.gui);
            }
            /**
             * 点击9~17进行贩卖
             */
            if(inventoryClickEvent.getSlot()>=9 && inventoryClickEvent.getSlot()<=17){
                inventoryClickEvent.setCancelled(true);
                /**
                 * 获取价格
                 */
                ItemStack itemStack =inventoryClickEvent.getInventory().getItem(inventoryClickEvent.getSlot());
                ItemStack targetItem =inventoryClickEvent.getInventory().getItem(inventoryClickEvent.getSlot()-9);
                ItemMeta itemMeta = itemStack.getItemMeta();

                double money = Double.parseDouble(itemMeta.getLore().get(3));
                int number = Integer.parseInt(itemMeta.getLore().get(5));
                int isContentItem = 0;
                /**
                 * 判断是否拥有这个物品
                 */
                switch (inventoryClickEvent.getClick()){
                    case LEFT:
                        isContentItem = 1;
                        break;
                    case RIGHT:
                        isContentItem = 64;
                        break;
                    case SHIFT_LEFT:
                        isContentItem = number;
                }


                if(player.getInventory().containsAtLeast(targetItem,isContentItem)){
                    /**
                     * 返回格子
                     */
                    List<Integer> solt= new ArrayList<>();
                    ItemStack[] imsk = player.getInventory().getStorageContents();
                    for(int i=0;i<imsk.length;i++){
                        if(imsk[i]!=null && imsk[i].getType().equals(targetItem.getType()) && imsk[i].getItemMeta().equals(targetItem.getItemMeta())){
                            solt.add(i);
                        }
                    }
                    if(solt==null){
                        player.closeInventory();
                        System.out.println("插件出现异常，终止交易");
                        return;
                    }
                    /**
                     * 判断交易是否成功
                     */
                    EconomyResponse r = Main.econ.depositPlayer(player,money*((float)isContentItem/number));
                    if(r.transactionSuccess()) {
                        /**
                         * 成功后删除一个物品
                         */
                        int itemAmount=0;
                        if(isContentItem<=64){
                            itemAmount = imsk[solt.get(0)].getAmount();
                            if((itemAmount-isContentItem)>0 ) {
                                imsk[solt.get(0)].setAmount(itemAmount - isContentItem);
                            }else{
                                imsk[solt.get(0)].setType(Material.AIR);
                            }
                        }else{
                            for(int i=0;i<solt.size();i++){
                                itemAmount+=imsk[solt.get(i)].getAmount();
                            }
                            itemAmount-=isContentItem;
                            int a = itemAmount/64;
                            if(a>=1){
                                for(int i=1;i<a;i++){
                                    imsk[solt.get(i)].setAmount(64);
                                }
                            }
                            int i;
                            if(itemAmount%64>0){
                                i=1;
                            }else{
                                i=0;
                            }
                            for(;i<solt.size();i++){
                                imsk[solt.get(i)].setType(Material.AIR);
                            }
                            imsk[solt.get(0)].setAmount(itemAmount%64);

                        }

                        player.getInventory().setStorageContents(imsk);
                        player.sendMessage(String.format(Main.moneyMsg, isContentItem,Main.econ.format(r.amount), Main.econ.format(r.balance)));
                    } else {
                        player.sendMessage(String.format("发生了一个错误: %s", r.errorMessage));
                    }
                }else{
                    player.sendMessage(Main.noItemMsg);
                }


            }
        }
    }
}
