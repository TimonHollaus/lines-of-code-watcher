package filesystem;

public class CommentTypeChooser{
    public static CommentChecker getcheckertype(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "java":
            case "js":
                return new JavaCommentChecker();
            case "html":
                return new HTMLCommentChecker();
            default:
                throw new IllegalArgumentException("Dieser Filetype wird nicht unterstützt: " + fileExtension);
        }
    }
}