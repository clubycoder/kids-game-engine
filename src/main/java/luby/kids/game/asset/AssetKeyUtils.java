package luby.kids.game.asset;

public class AssetKeyUtils {
    protected static String getExtension(String name) {
        int idx = name.lastIndexOf('.');
        //workaround for filenames ending with xml or json and another dot ending before that (my.mesh.xml)
        if (name.toLowerCase().endsWith(".xml") || name.toLowerCase().endsWith(".json")) {
            idx = name.substring(0, idx).lastIndexOf('.');
            if (idx == -1) {
                idx = name.lastIndexOf('.');
            }
        }
        if (idx <= 0 || idx == name.length() - 1) {
            return "";
        } else {
            return name.substring(idx + 1).toLowerCase();
        }
    }
}
