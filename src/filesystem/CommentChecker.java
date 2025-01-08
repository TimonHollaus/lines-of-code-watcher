package filesystem;

public interface CommentChecker {
    boolean isComment(String line);
    boolean startsMultiLineComment(String line);
    boolean endsMultiLineComment(String line);
}
