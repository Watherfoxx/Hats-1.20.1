package me.ichun.mods.hats.client.gui.window;

import com.mojang.blaze3d.matrix.MatrixStack;
import lain.mods.cos.api.CosArmorAPI;
import lain.mods.cos.api.inventory.CAStacksBase;
import lain.mods.cos.impl.ModObjects;
import lain.mods.cos.impl.network.packet.PacketSetSkinArmor;
import me.ichun.mods.hats.client.gui.IHatSetter;
import me.ichun.mods.hats.client.gui.WorkspaceHats;
import me.ichun.mods.hats.client.gui.window.element.ElementHatRender;
import me.ichun.mods.hats.common.world.HatsSavedData;
import me.ichun.mods.ichunutil.client.gui.bns.window.Window;
import me.ichun.mods.ichunutil.client.gui.bns.window.constraint.Constraint;
import me.ichun.mods.ichunutil.client.gui.bns.window.view.View;
import me.ichun.mods.ichunutil.client.gui.bns.window.view.element.Element;
import me.ichun.mods.ichunutil.client.gui.bns.window.view.element.ElementButtonTextured;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class WindowSidebar extends Window<WorkspaceHats>
        implements IHatSetter
{
    public WindowSidebar(WorkspaceHats parent)
    {
        super(parent);

        setBorderSize(() -> 0);

        disableBringToFront();
        disableDocking();
        disableDockStacking();
        disableUndocking();
        disableDrag();
        disableDragResize();
        disableTitle();

        setId("windowSidebar");

        setView(new ViewSidebar(this));
    }

    @Override
    public void renderBackground(MatrixStack stack){} //no BG

    @Override
    public ViewSidebar getCurrentView()
    {
        return super.getCurrentView();
    }

    public static class ViewSidebar extends View<WindowSidebar>
            implements IHatSetter
    {
        public static ResourceLocation TEX_CANCEL = new ResourceLocation("hats", "textures/icon/cancel.png");
        public static ResourceLocation TEX_RANDOMISE = new ResourceLocation("hats", "textures/icon/randomise.png");
        public static ResourceLocation TEX_HATS = new ResourceLocation("hats", "textures/icon/hat.png");
        public static ResourceLocation TEX_HIDE_HELMET = new ResourceLocation("hats", "textures/icon/hide_helmet.png");
        public static ResourceLocation TEX_LOCKED_HATS = new ResourceLocation("hats", "textures/icon/locked_hat.png");
        public static ResourceLocation TEX_CATEGORIES = new ResourceLocation("hats", "textures/icon/categories.png");
        public static ResourceLocation TEX_RELOAD = new ResourceLocation("hats", "textures/icon/reload.png");
        public static ResourceLocation TEX_CONFIRM = new ResourceLocation("hats", "textures/icon/confirm.png");

        public ElementButtonTextured<?> cancelButton;
        public ElementButtonTextured<?> hideHelmetButton;
        public ElementButtonTextured<?> randomButton;

        public ElementButtonTextured<?> hatsButton;
        public ElementButtonTextured<?> lockedHatsButton;
        public ElementButtonTextured<?> manageResourcesButton;

        public ViewSidebar(@Nonnull WindowSidebar parent)
        {
            super(parent, "hats.gui.window.sidebar");

            ElementButtonTextured<?> btnStack;
            ElementButtonTextured<?> btnStackLast;

            int padding = 2;

            //CANCEL button
            cancelButton = btnStack = new ElementButtonTextured<>(this, TEX_CANCEL, btn -> {
                parent.parent.setNewHat(null, false);
            });
            if(parent.parent.hatDetails.name.isEmpty())
            {
                cancelButton.disabled = true;
            }
            btnStack.setTooltip(I18n.format("hats.gui.button.removeHat"));
            btnStack.setSize(20, 20);
            btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(this, Constraint.Property.Type.TOP, 0);
            elements.add(btnStack);
            btnStackLast = btnStack;

            //RANDOMISE
            randomButton = btnStack = new ElementButtonTextured<>(this, TEX_RANDOMISE, btn -> {
                if(parent.parent.hatLauncher != null)
                {
                    for(Element<?> element : parent.parent.windowHatsList.getCurrentView().list.elements)
                    {
                        if(element instanceof ElementHatRender)
                        {
                            ((ElementHatRender<?>)element).toggleState = false;
                        }
                    }
                    parent.parent.setNewHat(new HatsSavedData.HatPart(":random"), false);
                    btn.disabled = true;
                }
                else
                {
                    List<Element<?>> elements = parent.parent.windowHatsList.getCurrentView().list.elements;
                    if(!elements.isEmpty())
                    {
                        Element<?> element1 = elements.get(parentFragment.parent.hatEntity.getRNG().nextInt(elements.size()));
                        if(element1 instanceof ElementHatRender)
                        {
                            ElementHatRender hatRender = (ElementHatRender)element1;
                            hatRender.onClickRelease();
                            if(Screen.hasShiftDown())
                            {
                                hatRender.hatLevel.hsbiser[0] = parentFragment.parent.hatEntity.getRNG().nextFloat();
                                hatRender.hatLevel.isNew = true; //workaround to force set notify
                            }
                            hatRender.callback.accept(hatRender);

                            parent.parent.windowHatsList.resize(parent.parent.getMinecraft(), parent.parent.windowHatsList.getWidth(), parent.parent.windowHatsList.getHeight()); // to rearrange the scroll bar
                        }
                    }
                }
            });
            if(parent.parent.hatLauncher != null && parent.parent.hatDetails.name.equals(":random") || parent.parent.windowHatsList.getCurrentView().list.elements.isEmpty())
            {
                randomButton.disabled = true;
            }
            btnStack.setTooltip(parent.parent.hatLauncher != null ? I18n.format("hats.gui.button.randomHatLauncher") : I18n.format("hats.gui.button.randomHat"));
            btnStack.setSize(20, 20);
            btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
            //elements.add(btnStack);
            //btnStackLast = btnStack;

            if(parent.parent.hatLauncher == null)
            {
                //ALL HATS
                btnStack = new ElementButtonTextured<>(this, TEX_HATS, btn -> {
                    parent.parent.openWindowInCenter(new WindowAllHats(parent.parent), 0.0D, 0.0D, true);
                });
                btnStack.setTooltip(I18n.format("hats.gui.window.allHats.title"));
                btnStack.setSize(20, 20);
                btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
                //elements.add(btnStack);
                //btnStackLast = btnStack;

                //TOGGLE BUTTON FOR HIDING HELMET
                hideHelmetButton = btnStack = new ElementButtonTextured<>(this, TEX_HIDE_HELMET, btn -> {

                    PlayerEntity player = (PlayerEntity) parent.parent.hatEntity;

                    CAStacksBase invCosArmor = CosArmorAPI.getCAStacksClient(player.getUniqueID());
                    if(invCosArmor.isSkinArmor(3)) // helmet is hidden
                    {
                        invCosArmor.setSkinArmor(3, false);
                    }
                    else
                    {
                        invCosArmor.setSkinArmor(3, true);
                    }
                    // Server update for every clients
                    ModObjects.network.sendToServer(new PacketSetSkinArmor(3, invCosArmor.isSkinArmor(3)));
                });
                btnStack.setTooltip(I18n.format("hats.gui.button.hide_helmet"));
                btnStack.setSize(20, 20);
                btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
                elements.add(btnStack);
                btnStackLast = btnStack;

                //FILTER ALL UNLOCKED HATS
                hatsButton = btnStack = new ElementButtonTextured<>(this, TEX_HATS, btn -> {

                    /*ArrayList<String> filterSorterTextHas = new ArrayList<>();
                    filterSorterTextHas.add("filterHas"); // Dynamic:   SortHandler.SORTERS.get(FilterHas.class)
                    filterSorterTextHas.add("sorterFavourite");
                    filterSorterTextHas.add("sorterRarity:inverse");
                    filterSorterTextHas.add("sorterAlphabetical");*/
                    //String[] array = {"filterHas", "sorterFavourite", "sorterRarity:inverse", "sorterAlphabetical"};

                    //Hats.configClient.filterSorterConfig = Arrays.asList(array);
                    //Hats.configClient.save(); //This calls onConfigLoaded. No need to reset the config.

                    parent.parent.windowHatsList.getCurrentView().updateSearch("", "unlocked");
                    //parent.parent.openWindowInCenter(new WindowHatSorter(parent.parent), 0.7D, 0.7D, true);
                });
                btnStack.setTooltip(I18n.format("hats.gui.button.unlockedHats"));
                btnStack.setSize(20, 20);
                btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
                elements.add(btnStack);
                btnStackLast = btnStack;

                //FILTER ALL LOCKED HATS
                lockedHatsButton = btnStack = new ElementButtonTextured<>(this, TEX_LOCKED_HATS, btn -> {

                    /*ArrayList<String> filterSorterTextHasNot = new ArrayList<>();
                    filterSorterTextHasNot.add("filterHas:inverse"); // Dynamic:   SortHandler.SORTERS.get(FilterHas.class)
                    filterSorterTextHasNot.add("sorterFavourite");
                    filterSorterTextHasNot.add("sorterRarity:inverse");
                    filterSorterTextHasNot.add("sorterAlphabetical");*/
                    //String[] array = {"filterHas:inverse", "sorterFavourite", "sorterRarity:inverse", "sorterAlphabetical"};

                    //Hats.configClient.filterSorterConfig = Arrays.asList(array);
                    //Hats.configClient.save(); //This calls onConfigLoaded. No need to reset the config.

                    parent.parent.windowHatsList.getCurrentView().updateSearch("", "locked");
                    //parent.parent.openWindowInCenter(new WindowHatSorter(parent.parent), 0.7D, 0.7D, true);
                });
                btnStack.setTooltip(I18n.format("hats.gui.button.lockedHats"));
                btnStack.setSize(20, 20);
                btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
                elements.add(btnStack);
                btnStackLast = btnStack;

                //SORTING OPTIONS
                btnStack = new ElementButtonTextured<>(this, TEX_CATEGORIES, btn -> {
                    parent.parent.openWindowInCenter(new WindowHatSorter(parent.parent), 0.7D, 0.7D, true);
                });
                btnStack.setTooltip(I18n.format("hats.gui.button.sortingOptions"));
                btnStack.setSize(20, 20);
                btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
                //elements.add(btnStack);
                //btnStackLast = btnStack;

                //RELOAD
                manageResourcesButton = btnStack = new ElementButtonTextured<>(this, TEX_RELOAD, btn -> {
                    parent.parent.openWindowInCenter(new WindowHatManagement(parent.parent), 0.7D, 0.7D, true);
                });
                btnStack.setTooltip(I18n.format("hats.gui.button.hatResourceManagement"));
                btnStack.setSize(20, 20);
                btnStack.setId("btnResourceManagement");
                btnStack.constraints().left(this, Constraint.Property.Type.LEFT, 0).top(btnStackLast, Constraint.Property.Type.BOTTOM, padding);
                elements.add(btnStack);
                btnStackLast = btnStack;
            }
        }

        @Override
        public void onNewHatSet(HatsSavedData.HatPart newHat)
        {
            cancelButton.disabled = newHat == null;
            if(parentFragment.parent.hatLauncher != null)
            {
                randomButton.disabled = false;
            }
        }

        @Override
        public void renderBackground(MatrixStack stack){}
    }
}
