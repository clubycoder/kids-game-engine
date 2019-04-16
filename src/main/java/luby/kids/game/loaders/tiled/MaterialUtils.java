package luby.kids.game.loaders.tiled;

import com.jme3.math.ColorRGBA;

public class MaterialUtils {

	public static ColorRGBA toColorRGBA(String str) {
		float scalor = 1f / 255f;
		
		// #AARRGGBB || AARRGGBB
		if (str.startsWith("#")) {
			str = str.substring(1);
		}

		int argb = (int) Long.parseLong(str.toUpperCase(), 16);
		int alpha = 0xFF;
		if (str.length() >= 8) {// use AARRGGBB
			alpha = (argb >> 24) & 0xFF;
		}
		int red = (argb >> 16) & 0xFF;
		int green = (argb >> 8) & 0xFF;
		int blue = (argb >> 0) & 0xFF;

		ColorRGBA color = new ColorRGBA(red * scalor, green * scalor,
				blue * scalor, alpha * scalor);

		return color;
	}
}
