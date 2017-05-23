/**
 * Spawn qsub jobs.
 */
public class Cherries {

    private Fig fig;

    public Cherries(Fig fig) {
        this.fig = fig;
    }

    public void spawnJobs() {

        for (int i = 0; i < fig.getNumChunkX(); i++) {
            for (int j = 0; j < fig.getNumChunkY(); j++) {
                int x = i * fig.getTileSize();
                int y = j * fig.getTileSize();

                try {
                    Thread.sleep(2000); //1000 milliseconds is one second.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                spawnJob(x, y, getName(x, y));

            }
        }

    }

    public void testTile(int xpos, int ypos, String name) {
        // Testing:
        spawnJob(xpos, ypos, name);

        // Testing (bypass job creation, just write tile):
        /*
        Dragonfruit d = new Dragonfruit();
        try {
            d.getTile(fig.getSvsLocation(), xpos, ypos, fig.getTileSize(), name, "");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    public void testTiles() {
        // Testing (bypass job creation, just write tiles):
        Dragonfruit d = new Dragonfruit();

        for (int i = 0; i < fig.getNumChunkX(); i++) {
            for (int j = 0; j < fig.getNumChunkY(); j++) {
                int x = i * fig.getTileSize();
                int y = j * fig.getTileSize();

                try {
                    d.getTile(fig.getSvsLocation(), x, y, fig.getTileSize(), getName(x, y), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getName(int xpos, int ypos) {
        StringBuilder sb = new StringBuilder();
        sb.append(fig.getCaseId());
        sb.append("_x");
        sb.append(xpos);
        sb.append("_y");
        sb.append(ypos);
        //public pgm, use jpg
        sb.append("-tile.jpg");
        //private pgm, use png
        //sb.append("-tile.png");

        return sb.toString();
    }

    public String getCommand(int xpos, int ypos, String tileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("qsub -v svsLocation=");
        sb.append(fig.getSvsLocation());
        sb.append(",caseId=");
        sb.append(fig.getCaseId());
        sb.append(",subjectId=");
        sb.append(fig.getSubjectId());
        sb.append(",declumping=");
        sb.append(fig.getDeclumping());
        sb.append(",xpos=");
        sb.append(xpos);
        sb.append(",ypos=");
        sb.append(ypos);
        sb.append(",tileSize=");
        sb.append(fig.getTileSize());
        sb.append(",tileName=");
        sb.append(tileName);
        sb.append(",execId=");
        sb.append(fig.getExecId());
        sb.append(",dbName=");
        sb.append(fig.getDbName());

        if (fig.getOtsu() != null) {
            sb.append(",otsu=");
            sb.append(fig.getOtsu());
        }

        if (fig.getCurvWeight() != null) {
            sb.append(",curvWeight=");
            sb.append(fig.getCurvWeight());
        }

        if (fig.getKernel() != null) {
            sb.append(",kernel=");
            sb.append(fig.getKernel());
        }

        if (fig.getLowThld() != null) {
            sb.append(",lowThld=");
            sb.append(fig.getLowThld());
        }

        if (fig.getUpThld() != null) {
            sb.append(",upThld=");
            sb.append(fig.getUpThld());
        }

        if (fig.getMpp() != null) {
            sb.append(",mpp=");
            sb.append(fig.getMpp());
        }

        sb.append(" /cm/shared/apps/u24_software/pipeline_bwang/kumquat/eggplant.pbs");

        return sb.toString();
    }

    public void spawnJob(int xpos, int ypos, String tileName) {

        String command = getCommand(xpos, ypos, tileName);

        try {
            // using the Runtime exec method:
            Runtime.getRuntime().exec(command);

        } catch (Exception e) {
            System.out.println("exception happened: " + e.toString());
        }

    }

}
