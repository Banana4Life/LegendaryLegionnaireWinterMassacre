package life.banana4.ld31.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import life.banana4.ld31.Ld31;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Legendary Legionnaire Winter Massacre";
		config.width = Ld31.SCREEN_WIDTH;
		config.height = Ld31.SCREEN_HEIGHT;
		config.foregroundFPS = 144;
		config.vSyncEnabled = true;
		config.addIcon("icons/icon_16.png", FileType.Internal);
		config.addIcon("icons/icon_32.png", FileType.Internal);
		config.addIcon("icons/icon_128.png", FileType.Internal);
		new LwjglApplication(new Ld31(), config);
	}
}
