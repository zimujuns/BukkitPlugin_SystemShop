package com.mzimu.systemshop.util;

import com.mzimu.systemshop.Main;
import com.mzimu.systemshop.data.Permission;
import com.mzimu.systemshop.runnable.ReloadShopList;
import com.mzimu.systemshop.util.Lottery.LotteryData;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.mzimu.systemshop.Main.*;

public class CommandMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;
            if(strings.length == 0){
                helpMsg(player);
                return false;
            }
            switch (strings[0]){
                case "open":
                    player.openInventory(gui);
                    return true;
                case "set":
                    if(player.hasPermission(Permission.SETSHOP.getPermission())==false){
                        player.sendMessage(noPermissMsg);
                        return false;
                    }
                    if(strings.length<4){
                        player.sendMessage("指令有误,指令为‘/zmss set [概率(请输入整数)] [回收价(最小)] [回收价(最大)]’以此来设置");
                        player.sendMessage("指令有误,指令为‘/zmss set [概率(请输入整数)] [回收价(最小)] [回收价(最大)] [数量]’以此来设置超过64的物品");
                        return false;
                    }

                    ItemStack itemStack = player.getInventory().getItemInMainHand();
                    if(itemStack.getType() == Material.AIR){
                        player.sendMessage("手上没有物品，请注意");
                        return false;
                    }
                    /**
                     * 添加至临时表中
                     */
                    int chance = Integer.parseInt(strings[1]);
                    int minMoney = Integer.parseInt(strings[2]);
                    int maxMoney = Integer.parseInt(strings[3]);
                    int number = strings.length==5?Integer.parseInt(strings[4]):itemStack.getAmount();
                    itemStack.setAmount(1);
                    Main.lotteryDataList.add(new LotteryData(chance,itemStack,minMoney,maxMoney,number));
                    /**
                     * 添加商品
                     * 至配置文件内
                     */
                    Main.configData.save(itemStack,chance,minMoney,maxMoney,number);
                    player.sendMessage("物品设置成功,在所有物品设置完毕后请通过指令重启服务器进行保存,以免服务器崩溃造成的数据丢失。");
                    return true;
                case "refresh":
                    if(player.hasPermission(Permission.REFRESH.getPermission())==false){
                        player.sendMessage(noPermissMsg);
                        return false;
                    }
                    if(refresh()){
                        player.sendMessage("[系统] 商店刷新完毕");
                    }else{
                        player.sendMessage("当前无法刷新，请设置完物品后重新启动服务器再执行此命令");
                    }
                    return true;
                default:
                    helpMsg(player);
                    return false;
            }

        }else{
            if(strings.length > 0 && strings[0].equalsIgnoreCase("refresh")){
                refresh();
            }else{
                System.out.println("除了刷新以外的命令只能在游戏内使用,请勿在cmd中使用");
                System.out.println("/zmss refresh 刷新当前商店[请勿过分使用这个指令]");
            }
        }
        return false;
    }


    public void helpMsg(Player player){
        if(player.hasPermission(Permission.SETSHOP.getPermission())){
            player.sendMessage("/zmss set [概率(请输入整数)] [回收价(最小)] [回收价(最大)] 以此来设置小于64的物品");
            player.sendMessage("/zmss set [概率(请输入整数)] [回收价(最小)] [回收价(最大)] [数量] 以此来设置超过64的物品");
        }
        if(player.hasPermission(Permission.REFRESH.getPermission())){
            player.sendMessage("/zmss refresh 刷新当前商店[请勿过分使用这个指令]");
        }
    }

    public boolean refresh(){
        /**
         * 卸载定时器再重新设置
         * 定时器内会触发刷新
         */
        try {
            reloadShopList.cancel();
        }catch (NullPointerException e){
            System.out.println("当前无法刷新，请设置完物品后重新启动服务器再执行此命令");
            return false;
        }

        reloadShopList = new ReloadShopList();
        reloadShopList.runTaskTimerAsynchronously(getProvidingPlugin(Main.class),20L,time * 20L);
        return true;
    }
}
