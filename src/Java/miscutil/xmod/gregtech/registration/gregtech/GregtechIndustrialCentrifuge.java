package miscutil.xmod.gregtech.registration.gregtech;

import miscutil.core.util.Utils;
import miscutil.xmod.gregtech.api.enums.GregtechItemList;
import miscutil.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntityIndustrialCentrifuge;

public class GregtechIndustrialCentrifuge
{



	public static void run()
	{
		if (miscutil.core.lib.LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Centrifuge Multiblock.");
			run1();
		}

	}

	private static void run1()
	{
		//Industrial Centrifuge Multiblock
		GregtechItemList.Industrial_Centrifuge.set(new GregtechMetaTileEntityIndustrialCentrifuge(790, "industrialcentrifuge.controller.tier.single", "Industrial Centrifuge").getStackForm(1L));
		
	}
}