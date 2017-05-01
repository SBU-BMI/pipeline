import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.cli.*;

/**
 * Lookup SVS location from TCGA ID.
 * Call programs to chunk the image.
 */
public class Apples {

    private Options options = new Options();
    private static final int tileSize = 2048;
    private Fig fig = new Fig();

    private String fileLocationList = "/cm/shared/apps/u24_software/pipeline/analysis-package/all_images.list";


    public Apples() {
        options.addOption("c", true, "case id/image id");
        options.addOption("a", true, "analysis execution id");
        options.addOption("o", true, "database name");
        options.addOption("r", true, "otsuRatio");
        options.addOption("w", true, "curvatureWeight");
        options.addOption("l", true, "sizeLowerThld");
        options.addOption("u", true, "sizeUpperThld");
        options.addOption("k", true, "msKernel");
        options.addOption("m", true, "mpp");
        //options.addOption("e", "email"); // TBA.
    }

    public void parse(String[] args) {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd;
        boolean good = true;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("c")) {
                fig.setCaseId(cmd.getOptionValue("c"));
            } else {
                good = false;
            }

            if (cmd.hasOption("a")) {
                fig.setExecId(cmd.getOptionValue("a"));
            } else {
                good = false;
            }

            if (cmd.hasOption("o")) {
                fig.setDbName(cmd.getOptionValue("o"));
            } else {
                good = false;
            }

            if (cmd.hasOption("r")) {
                fig.setOtsu(cmd.getOptionValue("r"));
            }

            if (cmd.hasOption("w")) {
                fig.setCurvWeight(cmd.getOptionValue("w"));
            }

            if (cmd.hasOption("l")) {
                fig.setLowThld(cmd.getOptionValue("l"));
            }

            if (cmd.hasOption("u")) {
                fig.setUpThld(cmd.getOptionValue("u"));
            }

            if (cmd.hasOption("k")) {
                fig.setKernel(cmd.getOptionValue("k"));
            }

            if (cmd.hasOption("m")) {
                fig.setMpp(cmd.getOptionValue("m"));
            }

            if (!good) {
                usage(1);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Apples a = new Apples();

        a.parse(args);
        Fig fig = a.fig;

        String fileLocation = a.getFileLocation(fig.getCaseId());

        Bananas b = new Bananas();
        try {
            b.setTileSize(tileSize);
            b.readImage(fileLocation);

            int sx = b.getNumChunkX();
            int sy = b.getNumChunkY();

            fig.setSvsLocation(b.getSvsLocation());
            fig.setTileSize(tileSize);
            fig.setNumChunkX(sx);
            fig.setNumChunkY(sy);

            Cherries c = new Cherries(fig);
            c.spawnJobs();
            //c.testTiles();
            //a.testing(c, tileSize, 4096, 10240);

        } catch (Exception ex) {
            System.err.println("Yep, that's an error.");
            ex.printStackTrace();
        }

    }

    public void testing(Cherries c, int tileSize, int xpos, int ypos) {
        // Hardest tile: xpos = 8192; ypos = 12288;
        String str = c.getName(xpos, ypos);
        c.testTile(xpos, ypos, "test_" + tileSize + "_" + str);
    }

    public void usage(int rtn) {
        System.err.println("usage: java Apples -c TCGA-06-0148-01Z-00-DX1 -a execId-123 -o dbName");
        System.exit(rtn);
    }

    public String getFileLocation(String caseId) {

        String line;
        String rtnStr = null;

        try {
            FileReader fileReader = new FileReader(fileLocationList);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains(caseId)) {
                    rtnStr = line;
                    break;
                }

            }

            bufferedReader.close();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        if (rtnStr == null) {
            System.err.println("File location not found for file: " + caseId + "\nExiting.");
            System.exit(1);
        }

        return rtnStr;
    }
}
