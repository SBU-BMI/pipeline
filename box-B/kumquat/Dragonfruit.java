import loci.formats.IFormatReader;
import loci.formats.IFormatWriter;
import loci.formats.ImageWriter;
import loci.formats.meta.IMetadata;
import ome.xml.meta.OMEXMLMetadataRoot;
import ome.xml.model.Image;
import ome.xml.model.primitives.PositiveInteger;

/**
 * Tile extraction.
 */
public class Dragonfruit {
    private static IFormatReader reader;
    private static IMetadata meta;
    private static OMEXMLMetadataRoot root;
    private static String writeFilePath = "/data2/tcga_analysis/tmp/";

    public static void main(String[] args) {

        Dragonfruit d = new Dragonfruit();
        if (args.length < 6) {
            d.usage(1);
        }

        String svsLocation = args[0];
        String uuid = args[1];
        int xpos = Integer.parseInt(args[2]);
        int ypos = Integer.parseInt(args[3]);
        int tileSize = Integer.parseInt(args[4]);
        String filename = args[5];

        try {
            getTile(svsLocation, xpos, ypos, tileSize, filename, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.exit(0);

    }

    public void usage(int rtn) {
        System.err.println("usage: java Dragonfruit svsLocation uuid xpos ypos tileSize filename");
        System.exit(rtn);
    }

    /**
     * @param xpos
     * @param ypos
     * @throws Exception
     */
    public static void getTile(String svsLocation, int xpos, int ypos, int tileSize, String filename, String uuid) throws Exception {

        Bananas b = new Bananas();

        b.setTileSize(tileSize);
        b.readImage(svsLocation);
        reader = b.getReader();
        meta = b.getMeta();
        tileSize = b.getTileSize();
        root = b.getRoot();

        Image exportImage = root.getImage(0);

        OMEXMLMetadataRoot newRoot = (OMEXMLMetadataRoot) meta.getRoot();
        newRoot.addImage(exportImage);

        meta.setRoot(newRoot);
        meta.setPixelsSizeX(new PositiveInteger(tileSize), 0);
        meta.setPixelsSizeY(new PositiveInteger(tileSize), 0);

        byte[] buf = reader.openBytes(0, xpos, ypos, tileSize, tileSize);

        IFormatWriter writer = new ImageWriter();
        writer.setMetadataRetrieve(meta);
        writer.setWriteSequentially(true);

        writer.setSeries(0);
        String str = "";
        if (uuid != "") {
            str = uuid + "/";
        }
        String path = writeFilePath + str + filename;
        writer.setId(path);
        writer.saveBytes(0, buf);
        writer.close();

    }
}
