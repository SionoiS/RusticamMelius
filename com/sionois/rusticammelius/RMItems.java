package com.sionois.rusticammelius;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import TFC.TFCItems;

public class RMItems {

    public static final Set<Integer> BreedingFood = new HashSet<Integer>(Arrays.asList(
            TFCItems.WheatGrain.itemID,
            TFCItems.RyeGrain.itemID,
            TFCItems.RiceGrain.itemID,
            TFCItems.BarleyGrain.itemID, 
            TFCItems.OatGrain.itemID
            ));
    
    public static final Set<Integer> BreedingWolfFood = new HashSet<Integer>(Arrays.asList( 
            TFCItems.muttonRaw.itemID, 
            Item.porkRaw.itemID, 
            Item.beefRaw.itemID,
            Item.chickenRaw.itemID
            ));
    
	public static void LoadItems()
	{
		
	}

	public static void RegisterItems()
	{

	}

}
