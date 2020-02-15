/**
 * InfectStatistic TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */
class InfectStatistic {
    public static void main(String[] args) {
        ArgsParse ap = new ArgsParse();
        ap.parse(args);
        FileHandle fh = new FileHandle();

        StringBuilder sb = fh.filesRead(ap.slog, ap.sdata);
        LogHandle lh = new LogHandle();
        lh.calculate(sb.toString());
        lh.WriteToFile(ap.sout);
    }
}
