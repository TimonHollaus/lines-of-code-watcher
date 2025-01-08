package filesystem;

public class HTMLCommentChecker implements CommentChecker {
    @Override
    public boolean isComment(String line) {
        return false;
    }

    @Override
    public boolean startsMultiLineComment(String line) {
        return line.contains("<!--");
    }

    @Override
    public boolean endsMultiLineComment(String line) {
        return line.contains("-->");
    }
}
