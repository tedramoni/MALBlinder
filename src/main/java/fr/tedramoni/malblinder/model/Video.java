package fr.tedramoni.malblinder.model;

/**
 * Created by Ted on 25/03/2016.
 */
public class Video {
    String imageLink;
    String videoId;

    public Video(String imageLink, String videoId) {
        this.imageLink = imageLink;
        this.videoId = videoId;
    }

    public Video() {
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public String toString() {
        return "Video{" +
                "imageLink='" + imageLink + '\'' +
                ", videoId='" + videoId + '\'' +
                '}';
    }
}
