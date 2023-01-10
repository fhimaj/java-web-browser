public class Bookmark {

    //name of bookmark
    private String name;

    //the url of bookmark
    private String url;

    //method Bookmark
    public Bookmark(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
