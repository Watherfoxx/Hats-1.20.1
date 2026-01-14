package me.ichun.mods.hats.client.gui;

import me.ichun.mods.hats.client.gui.window.WindowTutorial;
import me.ichun.mods.hats.common.Hats;
import me.ichun.mods.hats.common.packet.PacketGiveHat;
import me.ichun.mods.ichunutil.client.gui.bns.window.Window;
import me.ichun.mods.ichunutil.client.gui.bns.window.WindowGreyout;
import net.minecraft.client.resources.I18n;

public class TutorialHandler
{
    private static void disableGreyoutClickOut(WorkspaceHats workspace)
    {
        for(Window<?> window : workspace.windows)
        {
            if(window instanceof WindowGreyout)
            {
                ((WindowGreyout)window).disableClickOut();
            }
        }
    }

    public static void startTutorial(WorkspaceHats workspace)
    {
        if(Hats.eventHandlerClient.serverHasMod && workspace.windowHatsList.getCurrentView().list.elements.isEmpty())
        {
            workspace.popup(0.6D, 0.5D, w -> cameraIntro((WorkspaceHats)w), I18n.format("hats.gui.tutorial.noHat"));
            disableGreyoutClickOut(workspace);

            workspace.windowHatsList.getCurrentView().textField.setText("");
            workspace.windowHatsList.getCurrentView().updateSearch("");

            Hats.channel.sendToServer(new PacketGiveHat());
        }
        else
        {
            cameraIntro(workspace);
        }
    }

    private static void cameraIntro(WorkspaceHats workspace)
    {
        WindowTutorial camTut = new WindowTutorial(workspace, WindowTutorial.Direction.LEFT, workspace.windowHatsList.getLeft() - 10, workspace.windowHatsList.getTop() + (workspace.windowHatsList.getHeight() / 2), (int)(workspace.windowHatsList.getWidth() * 0.8F), 200, w ->
        {
            WindowTutorial hatsTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowHatsList.getLeft() + 40, workspace.windowHatsList.getTop() + (workspace.windowHatsList.getHeight() / 2), (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w2 -> {
                if(workspace.windowHatsList.getCurrentView().list.elements.isEmpty())
                {
                    workspace.popup(0.6D, 0.5D, w1 -> hatSelectIntro((WorkspaceHats)w1), I18n.format("hats.gui.tutorial.clickForHatMissing"));
                    disableGreyoutClickOut(workspace);
                }
                else
                {
                    hatSelectIntro(workspace);
                }

            }, I18n.format("hats.gui.tutorial.hatsHere"));
            workspace.addWindowWithGreyout(hatsTut);
            disableGreyoutClickOut(workspace);
            hatsTut.init();

        }, I18n.format(workspace.fallback ? "hats.gui.tutorial.cameraFallback" : "hats.gui.tutorial.camera"));
        workspace.addWindowWithHalfGreyout(camTut);
        disableGreyoutClickOut(workspace);
        camTut.init();
    }

    private static void hatSelectIntro(WorkspaceHats workspace)
    {
        WindowTutorial hatSelectTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowHatsList.getCurrentView().list.getLeft() + 25, workspace.windowHatsList.getCurrentView().list.getTop() + 40, (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w -> {
            WindowTutorial searchTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowHatsList.getCurrentView().textField.getLeft(), workspace.windowHatsList.getCurrentView().textField.getTop() + (workspace.windowHatsList.getCurrentView().textField.getHeight() / 2), (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w1 -> {
                WindowTutorial cancelBtnTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowSidebar.getCurrentView().cancelButton.getLeft(), workspace.windowSidebar.getCurrentView().cancelButton.getTop() + 10, (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w2 -> {
                    WindowTutorial hideHelmetBtnTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowSidebar.getCurrentView().hideHelmetButton.getLeft(), workspace.windowSidebar.getCurrentView().hideHelmetButton.getTop() + 10, (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w3 -> {
                        WindowTutorial hatsBtnTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowSidebar.getCurrentView().hatsButton.getLeft(), workspace.windowSidebar.getCurrentView().hatsButton.getTop() + 10, (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w4 -> {
                            WindowTutorial lockedHatsBtnTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowSidebar.getCurrentView().lockedHatsButton.getLeft(), workspace.windowSidebar.getCurrentView().lockedHatsButton.getTop() + 10, (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w5 -> {
                                WindowTutorial manageResourcesBtnTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowSidebar.getCurrentView().manageResourcesButton.getLeft(), workspace.windowSidebar.getCurrentView().manageResourcesButton.getTop() + 10, (int)(workspace.windowHatsList.getLeft() * 0.8F), 150, w6 -> {
                                    WindowTutorial dragTut = new WindowTutorial(workspace, WindowTutorial.Direction.RIGHT, workspace.windowHatsList.getLeft(), workspace.windowHatsList.getTop() + (workspace.windowHatsList.getHeight() / 2), (int)(workspace.windowHatsList.getLeft() * 0.8F), 200, w7 -> {
                                        workspace.popup(0.6D, 0.5D, w8 -> {
                                            workspace.finishTutorial();
                                        }, I18n.format("hats.gui.tutorial.conclusion"));
                                        disableGreyoutClickOut(workspace);

                                    }, I18n.format("hats.gui.tutorial.dragBar"));
                                    workspace.addWindowWithGreyout(dragTut);
                                    disableGreyoutClickOut(workspace);
                                    dragTut.init();

                                }, I18n.format("hats.gui.tutorial.manageResourcesButton"));
                                workspace.addWindowWithGreyout(manageResourcesBtnTut);
                                disableGreyoutClickOut(workspace);
                                manageResourcesBtnTut.init();

                            }, I18n.format("hats.gui.tutorial.lockedHatsButton"));
                            workspace.addWindowWithGreyout(lockedHatsBtnTut);
                            disableGreyoutClickOut(workspace);
                            lockedHatsBtnTut.init();

                        }, I18n.format("hats.gui.tutorial.hatsButton"));
                        workspace.addWindowWithGreyout(hatsBtnTut);
                        disableGreyoutClickOut(workspace);
                        hatsBtnTut.init();

                    }, I18n.format("hats.gui.tutorial.hideHelmetButton"));
                    workspace.addWindowWithGreyout(hideHelmetBtnTut);
                    disableGreyoutClickOut(workspace);
                    hideHelmetBtnTut.init();

                }, I18n.format("hats.gui.tutorial.cancelButton"));
                workspace.addWindowWithGreyout(cancelBtnTut);
                disableGreyoutClickOut(workspace);
                cancelBtnTut.init();

            }, I18n.format("hats.gui.tutorial.search"));
            workspace.addWindowWithGreyout(searchTut);
            disableGreyoutClickOut(workspace);
            searchTut.init();

        }, I18n.format("hats.gui.tutorial.clickForHat"));
        workspace.addWindowWithGreyout(hatSelectTut);
        disableGreyoutClickOut(workspace);
        hatSelectTut.init();
    }
}
