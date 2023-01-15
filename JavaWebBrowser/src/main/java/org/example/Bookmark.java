package org.example;
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
    @Override
    public boolean equals(Object obj) {
        if(obj==null){
            return false;
        }
        if (!(obj instanceof Bookmark)) {
            return false;
        }

        Bookmark other = (Bookmark) obj;

       if(this.name!=null && this.name.equals(other.name) && this.url!=null && this.url.equals(other.url)){
           return true;
       }
       else{
           return false;
       }

    }
}
