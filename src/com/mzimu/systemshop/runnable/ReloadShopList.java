package com.mzimu.systemshop.runnable;

import com.mzimu.systemshop.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadShopList extends BukkitRunnable {
    @Override
    public void run() {
        Main.closeInventory();
        Main.gui.setStorageContents(Main.drawLotteryUtil.refreshLottery());
        Main.b();
        System.out.println("商店已刷新");
    }
}
