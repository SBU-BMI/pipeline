import java.io.File;

import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.common.services.ServiceFactory;
import loci.formats.services.OMEXMLService;
import loci.formats.meta.MetadataStore;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import ome.xml.meta.OMEXMLMetadataRoot;
import loci.formats.CoreMetadata;
import loci.common.DebugTools;

/**
 * Reads an SVS image.
 */
public class Bananas {
    private IFormatReader reader;
    private IMetadata meta;
    private OMEXMLMetadataRoot root;

    private int width;
    private int height;
    private int numChunkX;
    private int numChunkY;

    private int tileSize;
    private String svsLocation;

    public Bananas() {
        DebugTools.enableLogging("ERROR");
    }

    public void readImage(String readFilePath) throws Exception {

        File myFile = new File(getReal(readFilePath));

        // Image Reader
        reader = new ImageReader();
        reader.setGroupFiles(true);
        reader.setMetadataFiltered(true);
        reader.setOriginalMetadataPopulated(true);

        // Service Factory
        ServiceFactory factory = new ServiceFactory();

        // Service
        OMEXMLService service = factory.getInstance(OMEXMLService.class);

        reader.setMetadataStore(service.createOMEXMLMetadata());
        reader.setId(myFile.getAbsolutePath());

        // Metadata Store
        MetadataStore store = reader.getMetadataStore();

        // Metadata Tools
        MetadataTools.populatePixels(store, reader, false, false);

        reader.setSeries(0);

        String xml = service.getOMEXML(service.asRetrieve(store));

        // this Image Metadata
        meta = service.createOMEXMLMetadata(xml);

        // this Metadata Root
        root = (OMEXMLMetadataRoot) meta.getRoot();

        // Core Metadata
        CoreMetadata big = reader.getCoreMetadataList().get(0);
        width = big.sizeX;
        height = big.sizeY;

        numChunkX = (width / tileSize);
        numChunkY = (height / tileSize);

    }

    public String getReal(String readFilePath) {
        String a = readFilePath;
        String b = "nfs";
        if (readFilePath.startsWith(b)) {
            String c = a.substring(a.indexOf(b) + b.length(), a.indexOf(":"));
            String d = c.substring(1);
            String e = "/data" + d + "/" + a.substring(a.indexOf("tcga_data"));
            svsLocation = e;
            readFilePath = e;

        } else {
            svsLocation = readFilePath;
        }

        return readFilePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumChunkX() {
        return numChunkX;
    }

    public int getNumChunkY() {
        return numChunkY;
    }

    public IFormatReader getReader() {
        return reader;
    }

    public IMetadata getMeta() {
        return meta;
    }

    public OMEXMLMetadataRoot getRoot() {
        return root;
    }

    public int getTileSize() {
        return tileSize;
    }

    public String getSvsLocation() {
        return svsLocation;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

}
