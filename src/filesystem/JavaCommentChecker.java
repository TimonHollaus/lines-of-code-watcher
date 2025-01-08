package filesystem;

public class JavaCommentChecker implements CommentChecker {
    @Override
    public boolean isComment(String line) {
        return line.startsWith("//");
    }

    @Override
    public boolean startsMultiLineComment(String line) {
        return line.contains("/*");
    }

    @Override
    public boolean endsMultiLineComment(String line) {
        return line.contains("*/");
    }
}
