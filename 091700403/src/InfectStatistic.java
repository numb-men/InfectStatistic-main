/**
 * InfectStatistic TODO
 *
 * @author Doris
 * @version v1.0
 * @since 2020.2.11
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
