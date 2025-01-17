package gtPlusPlus.xmod.gregtech.common.tileentities.generators;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.CONTAINER_AdvancedBoiler;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_AdvancedBoiler;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_MetaTileEntity_Boiler_Base extends GT_MetaTileEntity_Boiler {

	final private int mSteamPerSecond;
	final private int mPollutionPerSecond;
	final private int mBoilerTier;

	public GT_MetaTileEntity_Boiler_Base(int aID, String aNameRegional, int aBoilerTier) {
		super(aID, "electricboiler." + aBoilerTier + ".tier.single", aNameRegional,
				"Produces " + (CORE.ConfigSwitches.boilerSteamPerSecond * aBoilerTier) + "L of Steam per second");
		this.mSteamPerSecond = (CORE.ConfigSwitches.boilerSteamPerSecond * aBoilerTier);
		this.mPollutionPerSecond = 20 + (15 * aBoilerTier);
		this.mBoilerTier = aBoilerTier;
	}

	public GT_MetaTileEntity_Boiler_Base(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
		super(aName, aTier, aDescription, aTextures);
		this.mSteamPerSecond = (CORE.ConfigSwitches.boilerSteamPerSecond * aTier);
		this.mPollutionPerSecond = 20 + (15 * aTier);
		this.mBoilerTier = aTier;
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				this.mDescription,
				"Consumes "+(mSteamPerSecond/160)+"L of water per second",
				"Produces "+this.mPollutionPerSecond+" pollution/sec",};
	}

	public ITexture getOverlayIcon() {
		return new GT_RenderedTexture(Textures.BlockIcons.BOILER_FRONT);
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	protected GT_RenderedTexture getCasingTexture() {
		if (this.mBoilerTier == 1) {
			return new GT_RenderedTexture(Textures.BlockIcons.MACHINE_LV_SIDE);
		}
		else if (this.mBoilerTier == 2) {

			return new GT_RenderedTexture(Textures.BlockIcons.MACHINE_MV_SIDE);
		}
		else {

			return new GT_RenderedTexture(Textures.BlockIcons.MACHINE_HV_SIDE);
		}
		// return new
		// GT_RenderedTexture(TexturesGtBlock.Casing_Machine_Simple_Top);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0
				: aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex
				                                                                                           + 1];
	}

	public ITexture[] getFront(final byte aColor) {
		return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mBoilerTier][aColor + 1],
				this.getCasingTexture() };
	}

	public ITexture[] getBack(final byte aColor) {
		return this.getSides(aColor);
	}

	public ITexture[] getBottom(final byte aColor) {
		return this.getSides(aColor);
	}

	public ITexture[] getTop(final byte aColor) {
		return this.getSides(aColor);
	}

	public ITexture[] getSides(final byte aColor) {
		return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mBoilerTier][aColor + 1],
				this.getCasingTexture() };
	}

	public ITexture[] getFrontActive(final byte aColor) {
		return this.getFront(aColor);
	}

	public ITexture[] getBackActive(final byte aColor) {
		return this.getSides(aColor);
	}

	public ITexture[] getBottomActive(final byte aColor) {
		return this.getBottom(aColor);
	}

	public ITexture[] getTopActive(final byte aColor) {
		return this.getTop(aColor);
	}

	public ITexture[] getSidesActive(final byte aColor) {
		return this.getSides(aColor);
	}

	@Override
	public boolean isOutputFacing(final byte aSide) {
		return aSide != this.getBaseMetaTileEntity().getFrontFacing();
	}

	@Override
	public boolean isFacingValid(final byte aSide) {
		return aSide > 1;
	}

	// Please find out what I do.
	// I do stuff within the GUI.
	// this.mTemperature = Math.min(54, Math.max(0, this.mTemperature * 54 /
	// (((GT_MetaTileEntity_Boiler)
	// this.mTileEntity.getMetaTileEntity()).maxProgresstime() - 10)));
	@Override
	public int maxProgresstime() {
		return 1000 + (250 * mBoilerTier);
	}

	// Electric boiler? Okay.
	@Override
	public boolean isElectric() {
		return false;
	}

	// Hold more Steam
	@Override
	public int getCapacity() {
		return (16000 + (16000 * mBoilerTier));
	}

	@Override
	protected int getProductionPerSecond() {
		return 0;
	}

	@Override
	protected int getMaxTemperature() {
		return 0;
	}

	@Override
	protected int getEnergyConsumption() {
		return 0;
	}

	@Override
	protected int getCooldownInterval() {
		return 0;
	}

	@Override
	protected void updateFuel(IGregTechTileEntity iGregTechTileEntity, long l) {

	}

	// We want automation.
	@Override
	public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 1 || aIndex == 3;
	}

	// We want automation.
	@Override
	public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
		return aIndex == 2;
	}

	@Override
	protected int getPollution() {
		return 0;
	}

	@Override
	public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new CONTAINER_AdvancedBoiler(aPlayerInventory, aBaseMetaTileEntity, getCapacity());
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_AdvancedBoiler(aPlayerInventory, aBaseMetaTileEntity, "AdvancedBoiler.png", getCapacity());
	}

	@Override
	public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GT_MetaTileEntity_Boiler_Base(this.mName, mBoilerTier, this.mDescription, this.mTextures);
	}

	@Override
	public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		if ((aBaseMetaTileEntity.isServerSide()) && (aTick > 20L)) {
			// Utils.LOG_MACHINE_INFO("Ticking Boiler");

			//if (aTick % 60L == 0L) {
			// Utils.LOG_MACHINE_INFO("Temp:"+this.mTemperature);
			// Utils.LOG_MACHINE_INFO("getCapacity():"+this.getCapacity());
			// Utils.LOG_MACHINE_INFO("maxProgresstime():"+this.maxProgresstime());
			// Utils.LOG_MACHINE_INFO("mSteamPerSecond:"+this.mSteamPerSecond);
			// Utils.LOG_MACHINE_INFO("mProcessingEnergy:"+this.mProcessingEnergy);
			//}

			int mTempSteam = this.mSteam != null ? this.mSteam.amount : 0;
			if (this.mTemperature <= 20) {
				this.mTemperature = 20;
				this.mLossTimer = 0;
			}
			if (++this.mLossTimer > 40) {
				this.mTemperature -= 1;
				this.mLossTimer = 0;
			}
			for (byte i = 1; (this.mSteam != null) && (i < 6); i = (byte) (i + 1)) {
				if (i != aBaseMetaTileEntity.getFrontFacing()) {
					IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(i);
					if (tTileEntity != null) {
						FluidStack tDrained = aBaseMetaTileEntity.drain(ForgeDirection.getOrientation(i),
								Math.max(1, this.mSteam.amount / 2), false);
						if (tDrained != null) {
							int tFilledAmount = tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(),
									tDrained, false);
							if (tFilledAmount > 0) {
								tTileEntity.fill(ForgeDirection.getOrientation(i).getOpposite(), aBaseMetaTileEntity
										.drain(ForgeDirection.getOrientation(i), tFilledAmount, true), true);
							}
						}
					}
				}
			}
			if (aTick % 20L == 0L) {
				if (this.mTemperature > 100) {
					if ((this.mFluid == null) || (!GT_ModHandler.isWater(this.mFluid)) || (this.mFluid.amount <= 0)) {
						this.mHadNoWater = true;
					}
					else {
						if (this.mHadNoWater) {
							aBaseMetaTileEntity.doExplosion(4096L);
							return;
						}

						//this.mFluid.amount -= (10 * this.mBoilerTier);

						this.mFluid.amount -= (mSteamPerSecond/160);

						Logger.MACHINE_INFO("Draining "+(mSteamPerSecond/160)+"L of water. There is "+this.mFluid.amount+"L left.");
						if (this.mSteam == null) {
							this.mSteam = GT_ModHandler.getSteam((this.mSteamPerSecond));
							Logger.MACHINE_INFO("Added "+(this.mSteam.amount-mTempSteam)+"L of steam.");

						}
						else if (GT_ModHandler.isSteam(this.mSteam)) {
							this.mSteam.amount += (this.mSteamPerSecond);
							Logger.MACHINE_INFO("Added "+(this.mSteam.amount-mTempSteam)+"L of steam.");
						}
						else {
							this.mSteam = GT_ModHandler.getSteam((this.mSteamPerSecond));
							Logger.MACHINE_INFO("Added "+(this.mSteam.amount-mTempSteam)+"L of steam.");
						}
					}
				}
				else {
					this.mHadNoWater = false;
				}
			}
			if ((this.mSteam != null) && (this.mSteam.amount > (getCapacity() * 2))) {
				sendSound((byte) 1);
				this.mSteam.amount = (getCapacity() + (getCapacity() / 2));
			}
			ItemStack fuelSlot = this.mInventory[2];
			if ((this.mProcessingEnergy <= 0) && (aBaseMetaTileEntity.isAllowedToWork()) && (fuelSlot != null)) {

				if (isInputFuelItem(fuelSlot) && (this.mTemperature < (maxProgresstime() - 250))) {
					Logger.MACHINE_INFO("Current Heat:" + this.mTemperature + "/" + (maxProgresstime() - 250)
							+ " Burning fuel because not yet at a suitable temp.");
					useInputFuelItem(aBaseMetaTileEntity, fuelSlot);
				}

			}

			if ((this.mTemperature < maxProgresstime()) && (this.mProcessingEnergy > 0) && (aTick % 10L == 0L)) {
				// Utils.LOG_MACHINE_INFO("Adding +1 Temp.");
				this.mProcessingEnergy -= 2;
				this.mTemperature += 1;
			}
			if (this.mProcessingEnergy > 0 && (aTick % 20L == 0L)) {
				// Utils.LOG_MACHINE_INFO("Current Temp is at: "+this.mTemperature+"C");
				// GT_Pollution.addPollution(getBaseMetaTileEntity(),
				// this.mPollutionPerSecond);
			}
			aBaseMetaTileEntity.setActive(this.mProcessingEnergy > 0);
		}

	}

	public boolean isInputFuelItem(ItemStack inputItem) {
		int vCurrentBurnTime = 0;
		vCurrentBurnTime = GameRegistry.getFuelValue(inputItem);
		if (vCurrentBurnTime <= 0) {
			int furnaceTime = TileEntityFurnace.getItemBurnTime(inputItem);
			if (furnaceTime > 0) {
				vCurrentBurnTime = furnaceTime;
			}
		}
		if (vCurrentBurnTime > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean useInputFuelItem(IGregTechTileEntity aBaseMetaTileEntity, ItemStack inputItem) {
		int vCurrentBurnTime = 0;
		vCurrentBurnTime = GameRegistry.getFuelValue(inputItem);

		if (vCurrentBurnTime <= 0) {
			int furnaceTime = TileEntityFurnace.getItemBurnTime(inputItem);
			if (furnaceTime > 0) {
				vCurrentBurnTime = furnaceTime;
			}
		}

		if (vCurrentBurnTime > 0) {
			this.mProcessingEnergy += (vCurrentBurnTime / 10);
			if ((vCurrentBurnTime / 500) > 0) {
				this.mTemperature += (vCurrentBurnTime / 500);
			}
			aBaseMetaTileEntity.decrStackSize(2, 1);
			if (aBaseMetaTileEntity.getRandomNumber(3) == 0) {
				if (inputItem.getDisplayName().toLowerCase().contains("charcoal")
						|| inputItem.getDisplayName().toLowerCase().contains("coke")) {
					aBaseMetaTileEntity.addStackToSlot(3,
							GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1L));
				}
				else {
					aBaseMetaTileEntity.addStackToSlot(3,
							GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1L));
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean allowCoverOnSide(byte aSide, GT_ItemStack aCover) {
		if (aSide != this.getBaseMetaTileEntity().getFrontFacing()) {
			return true;
		}
		return super.allowCoverOnSide(aSide, aCover);
	}
}
